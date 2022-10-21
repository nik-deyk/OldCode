package com.velikiyprikalel.game.backend.exceptions;

import com.velikiyprikalel.game.backend.PlayerId;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(PlayerId id) {
        super("Could not find player " + id);
    }
}
