"""Module for user storage implementation."""
import sqlite3
import logging
from ipaddress import IPv4Address

from users.user_state import UserState
from users.user_storage import IUserStorage, User
from tokens.token_storage import ITokenStorage

logger = logging.getLogger("UserDatabase")

class UserDatabase(IUserStorage):
    """User saving in sqlite3 database."""

    __token_storage: ITokenStorage
    __db_columns: tuple = ('chat_id', 'state', 'token_id', 'address',
                           'config_create_time', 'token_requests', 'lang')

    def __get_db_connection(self):
        """Get connection to a database."""
        return sqlite3.connect(self.database_name + '.db')

    def __create_table(self):
        """Create table if not exists."""
        with self.__get_db_connection() as conn:
            conn.execute(f"CREATE TABLE IF NOT EXISTS {self.database_name} "
                         f"({self.__db_columns[0]} INTEGER PRIMARY KEY NOT NULL, "
                         f"{self.__db_columns[1]} INTEGER NOT NULL, "
                         f"{self.__db_columns[2]} TEXT, "
                         f"{self.__db_columns[3]} TEXT, "
                         f"{self.__db_columns[4]} INTEGER, "
                         f"{self.__db_columns[5]} INTEGER NOT NULL, "
                         f"{self.__db_columns[6]} TEXT DEFAULT \"ru\" NOT NULL);")
            conn.commit()

    def __init__(self, tok_stor: ITokenStorage,
                 database_name='users'):
        """Connect to db and create table if not exists."""
        super().__init__(tok_stor)
        self.__token_storage = tok_stor
        self.database_name = database_name
        self.__create_table()

    @classmethod
    def __user_to_row(cls, user: User) -> tuple:
        """Save all user fields to tuple."""
        output: list = []
        output.append(user.chat_id)
        output.append(int(user.state))
        output.append(None if user.token is None else user.token.id)
        output.append(None if user.address is None else format(user.address))
        output.append(None if user.config_create_time is None else
                      int(user.config_create_time))
        output.append(int(user.token_requests))
        output.append(str(user.lang))
        return tuple(output)

    def update(self, user: User) -> bool:
        """Update user record in database."""
        if self.get(user.chat_id) is None:
            return False
        query = f"UPDATE {self.database_name} SET "
        query += " = ?, ".join(self.__db_columns[1:]) + " = ?"
        with self.__get_db_connection() as conn:
            conn.execute(query + " WHERE chat_id = ?",
                          (*UserDatabase.__user_to_row(user)[1:], user.chat_id))
            conn.commit()
        return True

    def __convert_row(self, row: list) -> tuple:
        """Convert record from database to correct types."""
        row[0] = int(row[0])
        row[1] = UserState(row[1])
        if row[2] is not None:
            row[2] = self.__token_storage.get(row[2])
        if row[3] is not None:
            row[3] = IPv4Address(row[3])
        for k in (4, 5):
            if row[k] is not None:
                row[k] = int(row[k])
        row[6] = str(row[6])
        return tuple(row)

    def get(self, chat_id: int) -> User:
        """Return user from database.

        If there is no user in storage, return None.
        """
        with self.__get_db_connection() as conn:
            columns = ", ".join(self.__db_columns)
            cursor = conn.execute(f"SELECT {columns} "
                                  f"FROM {self.database_name} "
                                  "WHERE chat_id LIKE ?", (chat_id,))
        for row in cursor:
            return User(self.__convert_row(list(row)), self)
        return None

    def __add_user(self, chat_id: int) -> bool:
        """Add user to the database. Return true if there was no such user."""
        if self.get(chat_id) is not None:
            return False
        query = f"INSERT INTO {self.database_name} ("
        query += ", ".join(self.__db_columns) + ") VALUES ("
        query += ", ".join("?" for _ in self.__db_columns) + ")"
        with self.__get_db_connection() as conn:
            conn.execute(query, (chat_id, int(User.DEFAULT_STATE),
                                 None, None, None, 0, "ru"))
            conn.commit()
        return True

    def add(self, chat_id: int) -> User:
        """Add random user to the database and return it."""
        if not self.__add_user(chat_id):
            logger.error("User '%s' already exist in the storage!", str(chat_id))
        return self.get(chat_id)

    def used_ips(self) -> set[IPv4Address]:
        """Return used ip addresses."""
        query = (f"SELECT {self.__db_columns[3]} from {self.database_name} "
                 f"WHERE {self.__db_columns[3]} NOTNULL")
        with self.__get_db_connection() as conn:
            cursor = conn.execute(query)
        return set(IPv4Address(row[0]) for row in cursor)
