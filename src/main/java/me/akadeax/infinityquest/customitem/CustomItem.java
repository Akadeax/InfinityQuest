package me.akadeax.infinityquest.customitem;

import me.akadeax.infinityquest.InfinityQuest;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class CustomItem {
    public static final class NBTKeys {
        public static final NamespacedKey vendorPrice = new NamespacedKey(InfinityQuest.getInstance(), "vendorPrice");
    }

    private String displayName;
    private Material material;

    private List<String> lore;
    private int vendorPrice;

    CustomItem(CustomItemBuilder builder) {
        this.displayName = builder.displayName;
        this.material = builder.material;

        this.lore = builder.lore;
        this.vendorPrice = builder.vendorPrice;
    }

    public ItemStack createItemStack() {
        ItemStack newStack = new ItemStack(material);
        ItemMeta newMeta = newStack.getItemMeta();
        if(newMeta == null) return null;

        // set vendorPrice as NBT tag on item for easy recall when trying to sell to vendor
        PersistentDataContainer container = newMeta.getPersistentDataContainer();
        container.set(NBTKeys.vendorPrice, PersistentDataType.INTEGER, vendorPrice);

        newMeta.setDisplayName(displayName);

        List<String> newLore = new ArrayList<>();
        for(String s : lore) {
            newLore.add("§7" + s);
        }
        newLore.add("§7Vendorable for: §6" + vendorPrice);
        newMeta.setLore(newLore);
        newStack.setItemMeta(newMeta);

        return newStack;
    }

    public static class CustomItemBuilder {
        private String displayName;
        private Material material;

        private List<String> lore;
        private int vendorPrice;

        public CustomItemBuilder(String displayName, Material material) {
            this.displayName = displayName;
            this.material = material;
        }

        public CustomItemBuilder setLore(List<String> lore) {
            this.lore = lore;
            return this;
        }


        public CustomItemBuilder setVendorPrice(int vendorPrice) {
            this.vendorPrice = vendorPrice;
            return this;
        }

        public CustomItem build() {
            return new CustomItem(this);
        }
    }
}
