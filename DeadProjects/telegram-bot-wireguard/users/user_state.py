"""Enum with user possible states."""
from enum import IntEnum

class UserState(IntEnum):
    """User possible states."""

    NO_TOKEN = 0
    GONNA_READ_TOKEN = 1
    HAVE_TOKEN = 2
