"""Module for storage implementation."""
import sqlite3
import random
import string
import logging

from tokens.token_storage import ITokenStorage, Token

logger = logging.getLogger('TokenDatabase')

class TokenDatabase(ITokenStorage):
    """Token saving in sqlite3 database."""

    allowed_characters = (string.ascii_uppercase +
                          string.digits)

    def __get_db_connection(self):
        """Return database connection."""
        return sqlite3.connect(self.database_name + '.db')

    def __create_table(self):
        """Create table if not exists."""
        with self.__get_db_connection() as conn:
            conn.execute(f"CREATE TABLE IF NOT EXISTS {self.database_name} "
                              "(id TEXT PRIMARY KEY NOT NULL, "
                              "status INTEGER NOT NULL);")
            conn.commit()

    def __init__(self, token_len=ITokenStorage.TOKEN_LEN,
                 database_name='tokens'):
        """Connect to db and create table if not exists."""
        super().__init__(token_len)
        self.token_len = token_len
        self.database_name = database_name
        self.__create_table()

    def __random_string(self) -> str:
        """Return new random token as string."""
        return ''.join(random.choice(TokenDatabase.allowed_characters) for _
                       in range(self.token_len))

    def update(self, token: Token) -> bool:
        """Update token record in database."""
        if self.get(token.id) is None:
            return False
        with self.__get_db_connection() as conn:
            conn.execute(f"UPDATE {self.database_name} set status = ? where id = ?",
                         (int(token.state), token.id))
            conn.commit()
        return True

    def get(self, token_str: str) -> Token:
        """Return token from database.

        If there is no token in storage, return None.
        """
        if not self.sanitize_token(token_str):
            return None
        with self.__get_db_connection() as conn:
            cursor = conn.execute(f"SELECT id, status FROM {self.database_name}"
                                  " WHERE id LIKE ?", (token_str,))
        for row in cursor:
            return Token(row[0], row[1], self)
        return None

    def __add_token(self, token_str: str) -> bool:
        """Add token to the database. Return true if there was no such token."""
        if self.get(token_str) is not None:
            return False
        with self.__get_db_connection() as conn:
            conn.execute(f"INSERT INTO {self.database_name} (id, status)"
                         "VALUES (?, ?)", (token_str, int(Token.DEFAULT_STATE)))
            conn.commit()
        return True

    def sanitize_token(self, char_sequence: str) -> bool:
        """Check that the string consist only of allowed for token characters."""
        result = (len(char_sequence) == self.token_len and
                  not any(char not in TokenDatabase.allowed_characters for char
                          in list(char_sequence)))
        if not result:
            logger.warning("Token sanitization failed.")
        return result

    def create(self) -> Token:
        """Add random token to the database and return it."""
        new_token: str = self.__random_string()
        if not self.__add_token(new_token):
            logger.error("New generated random token '%s' already exist in the storage!",
                        new_token)
        return Token(new_token, Token.DEFAULT_STATE, self)
