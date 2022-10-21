package com.velikiyprikalel.game.backend;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "players")
public class Player extends PlayerId {
    private int lastNumber;

    Player() {
    }

    public Player(String username, int number) {
        super(username);
        this.lastNumber = number;
    }

    public int getLastNumber() {
        return lastNumber;
    }

    public void setLastNumber(int lastNumber) {
        this.lastNumber = lastNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Player)) {
            return false;
        }
        Player player = (Player) obj;
        return Objects.equals(this.username, player.username) && Objects.equals(this.lastNumber, player.lastNumber);
    }

    @Override
    public int hashCode() {
        return  Objects.hash(this.username, this.lastNumber);
    }

    @Override
    public String toString() {
        return "Player [username=" + this.username + ", lastNumber=" + this.lastNumber + "]";
    }
}
