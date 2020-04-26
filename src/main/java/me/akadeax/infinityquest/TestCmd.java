package me.akadeax.infinityquest;

import me.akadeax.infinityquest.customitem.weapon.blockableweapon.BlockableWeaponItem;
import me.akadeax.infinityquest.customitem.weapon.blockableweapon.ability.BlockAbility;
import me.akadeax.infinityquest.customitem.weapon.blockableweapon.ability.BlockAbilityHandler;
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

        BlockableWeaponItem newStack = new BlockableWeaponItem(
                "§4Nice.",
                Material.DIAMOND_SWORD,
                5d,
                -2.5d,
                "guard"
        );

        newStack.lore = Arrays.asList("ok, quite the epic sword.", "isn't it?");
        p.getInventory().addItem(newStack.generateItemStack());

        return true;
    }
}
