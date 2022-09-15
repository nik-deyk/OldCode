"""Module for user storage interface."""
from abc import ABCMeta, abstractmethod
from ipaddress import IPv4Address
from users.user import User
from tokens.token_storage import ITokenStorage

class IUserStorage:
    """Basic operations for user saving."""

    __metaclass__ = ABCMeta

    ts: ITokenStorage

    @abstractmethod
    def __init__(self, tok_stor: ITokenStorage):
        """Create storage for users."""

    @abstractmethod
    def get(self, chat_id: int) -> User:
        """Get user object by chat id.

        Returns None if user was not found.
        """

    @abstractmethod
    def update(self, user: User) -> bool:
        """Update user data in storage.

        Returns True if user was found.
        """

    @abstractmethod
    def add(self, chat_id: int) -> User:
        """Create new user with default params.

        Add it to storage and return.
        """

    @abstractmethod
    def used_ips(self) -> set[IPv4Address]:
        """Return used clients ips."""

    def obtain(self, chat_id: int) -> User:
        """Try to get and return newly created on fail."""
        if (user := self.get(chat_id)) is None:
            return self.add(chat_id)
        return user
