package me.akadeax.infinityquest;

import org.bukkit.plugin.java.JavaPlugin;

public final class InfinityQuest extends JavaPlugin {

    private static InfinityQuest instance;
    public static InfinityQuest getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        initCommands();
    }

    @SuppressWarnings("ConstantConditions")
    private void initCommands() {
        getCommand("test").setExecutor(new TestCmd());
    }

}
