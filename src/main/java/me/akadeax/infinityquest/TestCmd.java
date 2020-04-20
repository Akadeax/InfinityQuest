package me.akadeax.infinityquest;

import me.akadeax.infinityquest.customitem.CustomItem;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

//TODO: Remove, not needed for long
public class TestCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;

        Player p = (Player)sender;
        CustomItem newItem = new CustomItem.CustomItemBuilder("Sword!", Material.STONE_SWORD)
                .setVendorPrice(5)
                .setLore(Arrays.asList("Damn, finally a good sword."))
                .build();
        p.getInventory().addItem(newItem.createItemStack());

        return true;
    }


}
