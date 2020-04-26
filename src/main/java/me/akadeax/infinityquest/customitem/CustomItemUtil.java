package me.akadeax.infinityquest.customitem;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CustomItemUtil {

    public static PersistentDataContainer getItemContainer(ItemStack item) {
        if(item == null || item.getType() == Material.AIR) {
            return null;
        }

        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta == null) {
            return null;
        }

        return itemMeta.getPersistentDataContainer();
    }

    public static String getCustomItemType(ItemStack item) {
        PersistentDataContainer container = getItemContainer(item);
        if(container == null) {
            return "";
        }

        return container.getOrDefault(CustomItem.NBTKeys.itemType, PersistentDataType.STRING, "");
    }

    public static boolean isCustomItem(ItemStack item) {
        String type = getCustomItemType(item);
        return !"".equals(type);
    }

}

