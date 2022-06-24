"""Module for user fields."""

import logging
from ipaddress import IPv4Address
from users.user_state import UserState
from tokens.token_class import Token

logger = logging.getLogger("UserUpdateModule")

class User:
    """Basic data fields for user.

    You can access such values as:
        chat_id: int
        state: UserState
        token: Token (may be None)
        address: IPv4Address (may be None)
        config_create_time: int (may be None)
        token_requests: int (initialized with 0)
        lang: str
        banned: bool.
    """

    __storage = None # Type of IUserStorage.
    MAX_REQUESTS: int = 20
    DEFAULT_STATE: UserState = UserState.NO_TOKEN

    _chat_id: int
    _state: UserState
    _token: Token
    _address: IPv4Address
    _config_create_time: int  # Unix timestamp.
    _token_requests: int
    _lang: str

    def __init__(self, data: tuple, storage):
        """Init inner fields.

        Data: [0] is char_id, [1] - user state,
        [2] - token, [3] - address, [4] - config create time,
        [5] - token requests, [6] - selected language.
        """
        self.__storage = storage
        (self._chat_id, self._state, self._token,
         self._address, self._config_create_time,
         self._token_requests, self._lang) = data
        User.__init_properties(['state', 'token', 'address',
                                'config_create_time', 'token_requests', 'lang'])

    @classmethod
    def __default_setter(cls, user, attr_name: str, new_value):
        """Set logic for attributes."""
        if getattr(user, attr_name) != new_value:
            setattr(user, attr_name, new_value)
            if not user.storage.update(user):
                logger.error("No such user '%s' while updating in storage!",
                             str(user.chat_id))

    @classmethod
    def __get_attr_func(cls, attr_name):
        """Method-getter for attribute."""
        def getter(user):
            return getattr(user, attr_name)
        return getter

    @classmethod
    def __set_attr_func(cls, attr_name):
        """Method-setter that will update record in storage."""
        def setter(user, new_value):
            User.__default_setter(user, attr_name, new_value)
        return setter

    @classmethod
    def __del_attr_func(cls, attr_name):
        """Method-deleter for attribute."""
        def deleter(user):
            return delattr(user, attr_name)
        return deleter

    @classmethod
    def __init_properties(cls, fields: list[str]):
        """Add properties to object.

        For each property new functions are defined:
            getter, setter and deleter. They are
            common except for setter, which will
            update the user in database if the
            new value is different than current.
        """
        for field_name in fields:
            attr_name: str = '_' + field_name
            prop = property(User.__get_attr_func(attr_name),
                            User.__set_attr_func(attr_name),
                            User.__del_attr_func(attr_name),
                            f"{field_name} field."
                           )
            setattr(User, field_name, prop)

    @property
    def chat_id(self) -> int:
        """Get chat id."""
        return self._chat_id

    @property
    def storage(self):
        """Get storage."""
        return self.__storage

    @property
    def banned(self) -> bool:
        """If the user is banned."""
        return self._token_requests > self.MAX_REQUESTS
