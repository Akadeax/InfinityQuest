package me.akadeax.infinityquest;

import me.akadeax.infinityquest.customitem.CustomItemType;
import me.akadeax.infinityquest.customitem.CustomItemUtil;
import me.akadeax.infinityquest.customitem.weapon.WeaponItem;
import me.akadeax.infinityquest.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class EntityHitEvent implements Listener {

    private final TimeUtil timeUtil;

    private final FileConfiguration config;
    private final int attackCooldownLeewayTicks;
    public EntityHitEvent(JavaPlugin plugin) {
        config = plugin.getConfig();

        attackCooldownLeewayTicks = config.getInt("attack.cooldownLeewayTicks");

        timeUtil = new TimeUtil(plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof LivingEntity) || !(e.getEntity() instanceof LivingEntity)) return;
        if(e.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
            e.setCancelled(true);
            return;
        }
        LivingEntity damager = (LivingEntity) e.getDamager();

        EntityEquipment equipment = damager.getEquipment();
        if (equipment == null) {
            e.setCancelled(true);
            return;
        }
        ItemStack itemInHand = equipment.getItemInMainHand();

        PersistentDataContainer container = CustomItemUtil.getItemContainer(itemInHand);
        if (container == null) {
            e.setCancelled(true);
            return;
        }

        double damage = container.getOrDefault(WeaponItem.NBTKeys.attackDamage, PersistentDataType.DOUBLE, -1d);
        if (damage == -1d) {
            e.setCancelled(true);
            return;
        }

        if (damager instanceof Player) {
            Player p = (Player) damager;
            if(hasCooldown(p)) {
                e.setCancelled(true);
            }
            setCooldown(p);
        }

        e.setDamage(damage);
    }


    @EventHandler
    public void onItemChange(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();

        ItemStack newItem = p.getInventory().getItem(e.getNewSlot());
        String newItemType = CustomItemUtil.getCustomItemType(newItem);
        if(!newItemType.equals(CustomItemType.WEAPON)) return;

        setCooldown(p);
    }

    HashMap<UUID, Integer> hitCooldown = new HashMap<>();
    boolean hasCooldown(Player p) {
        return hitCooldown.containsKey(p.getUniqueId());
    }
    void setCooldown(Player p) {
        if(hasCooldown(p)) {
            Bukkit.getScheduler().cancelTask(hitCooldown.get(p.getUniqueId()));
        }
        timeUtil.runDelayed(() -> {
            double playerAttackSpeed = p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getValue();

            int delay = (int) (1 / playerAttackSpeed * 20) - attackCooldownLeewayTicks;
            if(delay < 0) delay = 0;

            int cooldownTask = timeUtil.runDelayed(() -> {
                hitCooldown.remove(p.getUniqueId());
            }, delay);
            hitCooldown.put(p.getUniqueId(), cooldownTask);
        }, 2);
    }
}