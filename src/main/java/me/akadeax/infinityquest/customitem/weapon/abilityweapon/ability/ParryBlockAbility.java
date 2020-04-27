package me.akadeax.infinityquest.customitem.weapon.abilityweapon.ability;

import me.akadeax.infinityquest.util.TimeUtil;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class ParryBlockAbility extends BlockAbility {

    public ParryBlockAbility(String name) {
        super(name);
        addDefault("blockTime", 30);
        addDefault("blockCooldownTime", 100);

        addDefault("rotateDamagerAmount", 5d);
    }

    @Override
    public void onBlockButtonPress(Player p) {
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_CAT_HISS, SoundCategory.MASTER, 1, 1);
        p.getWorld().spawnParticle(Particle.CRIT, p.getLocation(), 50);
    }

    @Override
    public void whileBlocking(Player p) {
        p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation(), 5, new Particle.DustOptions(Color.RED, 1));
    }

    @Override
    public void onBlockCooldownStart(Player p) {

    }

    @Override
    public void whileBlockCooldown(Player p) {

    }

    @Override
    public void afterBlockCooldown(Player p) {

    }

    @Override
    public void onBlockerHit(Player p, EntityDamageByEntityEvent e, TimeUtil timeUtil) {
        p.setVelocity(new Vector(0, p.getVelocity().getY(), 0));
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1, 1);
        p.getWorld().spawnParticle(Particle.FLAME, p.getLocation(), 20);

        if(e.getDamager() instanceof LivingEntity) {
            LivingEntity entityDamager = (LivingEntity)e.getDamager();
            entityDamager.damage(e.getDamage(), p);
            e.setCancelled(true);

            float newYaw = (float) (entityDamager.getLocation().getYaw() + get("rotateDamagerAmount", Double.class));
            Location loc = entityDamager.getLocation();
            loc.setYaw(newYaw);
            entityDamager.teleport(loc);
        }
    }
}
