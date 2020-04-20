package me.akadeax.infinityquest;

import java.util.UUID;

public class User {
    private UUID uuid;
    private int balance;

    public User(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public void setBalance(int setTo) {
        balance = setTo;
    }
    public int getBalance() {
        return balance;
    }
    public void addBalance(int amount) {
        setBalance(getBalance() + amount);
    }
    public void subtract(int amount) {
        setBalance(getBalance() - amount);
    }
    public boolean hasEnoughBalance(int toCheck) {
        return getBalance() >= toCheck;
    }
}
