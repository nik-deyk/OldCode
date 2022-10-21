package com.velikiyprikalel.game.backend;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PlayerId {
    protected @Id String username;

    protected PlayerId() {
    }

    public PlayerId(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PlayerId)) {
            return false;
        }
        PlayerId player = (PlayerId) obj;
        return Objects.equals(this.username, player.username);
    }

    @Override
    public int hashCode() {
        return this.username.hashCode();
    }

    @Override
    public String toString() {
        return "Player [username=" + this.username + "]";
    }
}
