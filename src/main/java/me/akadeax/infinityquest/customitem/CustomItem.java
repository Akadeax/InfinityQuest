package me.akadeax.infinityquest.customitem;

import me.akadeax.infinityquest.InfinityQuest;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class CustomItem {
    public static final class NBTKeys {
        public static final NamespacedKey itemType = new NamespacedKey(InfinityQuest.getInstance(), "itemType");
        public static final NamespacedKey vendorPrice = new NamespacedKey(InfinityQuest.getInstance(), "vendorPrice");
    }

    public String displayName;
    public Material material;
    public List<String> lore = new ArrayList<>();

    public int vendorPrice = 0;

    public CustomItem(String displayName, Material material) {
        this.displayName = displayName;
        this.material = material;
    }

    public ItemStack generateItemStack() {
        ItemStack newStack = new ItemStack(material);
        ItemMeta newMeta = newStack.getItemMeta();
        if(newMeta == null) return null;

        PersistentDataContainer container = newMeta.getPersistentDataContainer();
        container.set(NBTKeys.itemType, PersistentDataType.STRING, CustomItemType.NONE);
        container.set(NBTKeys.vendorPrice, PersistentDataType.INTEGER, vendorPrice);

        newMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);

        List<String> newLore = new ArrayList<>();
        for(String s : lore) {
            newLore.add("§7" + s);
        }

        newMeta.setLore(newLore);
        newStack.setItemMeta(newMeta);
        return newStack;
    }

}


