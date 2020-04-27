package me.akadeax.infinityquest;

import me.akadeax.infinityquest.customitem.weapon.abilityweapon.PlayerWeaponAbilityEvent;
import me.akadeax.infinityquest.customitem.weapon.abilityweapon.ability.BlockAbilityHandler;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class InfinityQuest extends JavaPlugin {

    private static InfinityQuest instance;
    public static InfinityQuest getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        initConfig();
        initEvents();
        initCommands();
    }

    private void initConfig() {
        getConfig().options().copyDefaults(true);
        getConfig().options().header("Config for InfinityQuest.\nAll timings in here are in ticks (20 ticks = 1 second)");
        getConfig().options().copyHeader(true);

        BlockAbilityHandler.init();

        getConfig().addDefault("attack.cooldownLeewayTicks", 5);

        saveConfig();
    }

    private void initEvents() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new EntityHitEvent(this), this);
        pm.registerEvents(new PlayerWeaponAbilityEvent(this), this);
    }

    @SuppressWarnings("ConstantConditions")
    private void initCommands() {
        getCommand("test").setExecutor(new TestCmd());
    }
}
