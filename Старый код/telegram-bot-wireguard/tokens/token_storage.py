"""Module for storage interface."""
from abc import ABCMeta, abstractmethod
from tokens.token_class import Token

class ITokenStorage:
    """Basic operations for token saving."""

    __metaclass__ = ABCMeta

    TOKEN_LEN: int = 20

    @abstractmethod
    def __init__(self, token_len: int):
        """Create storage for tokens."""

    @abstractmethod
    def get(self, token_str: str) -> Token:
        """Get token object by id.

        Returns None if token was not found.
        """

    @abstractmethod
    def update(self, token: Token) -> bool:
        """Update token in storage.

        Returns True if token was found.
        """

    @abstractmethod
    def create(self) -> Token:
        """Create new token with default params.

        Add it to storage and return.
        """
