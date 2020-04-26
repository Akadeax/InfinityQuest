package me.akadeax.infinityquest.customitem.weapon.blockableweapon.ability;

import me.akadeax.infinityquest.InfinityQuest;
import me.akadeax.infinityquest.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;

public abstract class BlockAbility {
    private String name;
    public BlockAbility(String name) {
        this.name = name;
    }

    private FileConfiguration config = InfinityQuest.getInstance().getConfig();

    // cache already used config objects for performance reasons
    private HashMap<String, Object> cachedConfigObjects = new HashMap<>();

    /**
     * gets config value for current ability from active cache or config
     */
    public <T> T get(String subpath, Class<T> clazz) {
        if(cachedConfigObjects.containsKey(subpath)) {
            Object cachedObj = cachedConfigObjects.get(subpath);
            return clazz.cast(cachedObj);
        }
        Object newObj = config.get(String.format("ability.block.%s.%s", name, subpath));
        cachedConfigObjects.put(subpath, newObj);
        return clazz.cast(newObj);
    }

    /**
     * adds default to the plugin config
     * @param subpath value is stored in |...|.block.|name|.|subpath|
     * @param value the default value to set
     */
    protected void addDefault(String subpath, Object value) {
        config.addDefault(String.format("ability.block.%s.%s", name, subpath), value);
    }

    /**
     * called once when p starts blocking
     */
    public abstract void onBlockButtonPress(Player p);

    /**
     * called every tick while p is blocking
     */
    public abstract void whileBlocking(Player p);

    /**
     * called after p has finished blocking, once cooldown starts
     */
    public abstract void onBlockCooldownStart(Player p);

    /**
     * called every tick while p's blocking cooldown is active
     */
    public abstract void whileBlockCooldown(Player p);

    /**
     * called after p's blocking cooldown has ended
     */
    public abstract void afterBlockCooldown(Player p);

    /**
     * called when p is hit while blocking
     */
    public abstract void onBlockerHit(Player p, EntityDamageByEntityEvent e, TimeUtil timeUtil);
}
