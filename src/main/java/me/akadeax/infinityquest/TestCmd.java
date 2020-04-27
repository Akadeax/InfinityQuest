package me.akadeax.infinityquest;

import me.akadeax.infinityquest.customitem.weapon.abilityweapon.AbilityWeaponItem;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class TestCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;

        Player p = (Player)sender;

        AbilityWeaponItem newStack = new AbilityWeaponItem.Builder<>()
                .setDisplayName("namename")
                .setLore(Arrays.asList("lorelore"))
                .setMaterial(Material.DIAMOND_AXE)
                .setAttackDamage(10d)
                .setAttackSpeed(-3.5d)
                .setAbility("guard")
                .build();

        p.getInventory().addItem(newStack.generateItemStack());

        return true;
    }
}
