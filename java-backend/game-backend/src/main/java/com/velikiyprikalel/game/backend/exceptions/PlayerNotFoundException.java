package com.velikiyprikalel.game.backend.exceptions;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String id) {
        super("Could not find player " + id);
    }
}
