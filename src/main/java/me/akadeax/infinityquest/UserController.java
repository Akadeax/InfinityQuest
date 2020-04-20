package me.akadeax.infinityquest;

import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class UserController {
    private static List<User> loadedUsers = new LinkedList<>();

    public static void loadUser(Player toLoad) {
        loadedUsers.add(new User(toLoad.getUniqueId()));
    }
    public static void unloadUser() {

    }
}
