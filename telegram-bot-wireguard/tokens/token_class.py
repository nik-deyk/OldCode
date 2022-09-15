"""Token structure contains token and activated flag."""

import logging
from tokens.token_state import TokenState

logger = logging.getLogger("TokenUpdater")

class Token:
    """Access token for user."""

    __storage = None
    __id: str = ""
    __state = TokenState.FREE

    DEFAULT_STATE = TokenState.FREE

    def __init__(self, token_id: str, state: TokenState,
                 storage):
        """Create token."""
        self.__id = token_id
        self.__state = state
        self.__storage = storage

    # pylint: disable=C0103 # disable invalid name.
    @property
    def id(self) -> str:
        """Get string id."""
        return self.__id

    @property
    def state(self) -> TokenState:
        """Get state."""
        return self.__state

    @state.setter
    def state(self, value: TokenState):
        if self.__state != value:
            self.__state = value
            if not self.__storage.update(self):
                logger.error("No such token '%s' in storage while setting new state!",
                             self.__id)

    @state.deleter
    def state(self):
        del self.__state
