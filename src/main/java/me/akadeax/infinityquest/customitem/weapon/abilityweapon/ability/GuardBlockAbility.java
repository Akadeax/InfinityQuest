package me.akadeax.infinityquest.customitem.weapon.abilityweapon.ability;

import me.akadeax.infinityquest.util.TimeUtil;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class GuardBlockAbility extends BlockAbility {

    public GuardBlockAbility(String name) {
        super(name);
        addDefault("blockTime", 30);
        addDefault("blockCooldownTime", 100);

        addDefault("knockbackMultiplier", 3d);
    }

    @Override
    public void onBlockButtonPress(Player p) {
        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, SoundCategory.MASTER, 1, 1);
        p.getWorld().spawnParticle(Particle.CRIT, p.getLocation(), 50);
    }

    @Override
    public void whileBlocking(Player p) {
        p.setVelocity(new Vector(0,p.getVelocity().getY(),0));
        p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation(), 5, new Particle.DustOptions(Color.YELLOW, 1));
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
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1, 1);
        p.getWorld().spawnParticle(Particle.FLAME, p.getLocation(), 20);

        // reduce damage taken by blocker to 25%
        e.setDamage(e.getDamage() * 0.25d);
        if(e.getDamager() instanceof LivingEntity) {
            LivingEntity damager = (LivingEntity)e.getDamager();
            damager.damage(0, p);
            timeUtil.runDelayed(() -> {
                Vector damagerVelocity = damager.getVelocity();
                double knockbackMultiplier = get("knockbackMultiplier", Double.class);
                damagerVelocity.multiply(knockbackMultiplier);
                damager.setVelocity(damagerVelocity);
            }, 1);
        }
    }
}
