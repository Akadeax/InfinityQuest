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

public abstract class CustomItem {
    public static final class NBTKeys {
        public static final NamespacedKey itemType = new NamespacedKey(InfinityQuest.getInstance(), "itemType");
        public static final NamespacedKey vendorPrice = new NamespacedKey(InfinityQuest.getInstance(), "vendorPrice");
    }

    public String displayName;
    public Material material;
    public List<String> lore;

    public int vendorPrice = 0;

    public CustomItem(String displayName, Material material, List<String> lore) {
        this.displayName = displayName;
        this.material = material;
        this.lore = lore;
    }

    /**
     * generates a bukkit ItemStack from this object
     */
    public ItemStack generateItemStack() {
        ItemStack newStack = new ItemStack(material);
        final ItemMeta newMeta = newStack.getItemMeta();
        if(newMeta == null) return null;

        newMeta.setDisplayName("§f" + displayName);

        // set NBT tags
        final PersistentDataContainer container = newMeta.getPersistentDataContainer();
        container.set(NBTKeys.itemType, PersistentDataType.STRING, CustomItemType.NONE);
        container.set(NBTKeys.vendorPrice, PersistentDataType.INTEGER, vendorPrice);

        newMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
        newMeta.setUnbreakable(true);

        // add lore 1:1 but with colorcode in front (in case non was specified in the actual lore)
        List<String> newLore = new ArrayList<>();
        for(String s : lore) {
            newLore.add("§7" + s);
        }

        newMeta.setLore(newLore);
        newStack.setItemMeta(newMeta);
        return newStack;
    }


    public abstract static class Builder<T extends Builder<T>> {

        @SuppressWarnings("unchecked")
        protected final T self() { return (T)this; }

        protected String displayName;
        protected Material material;
        protected List<String> lore;

        protected int vendorPrice;

        public T setDisplayName(String displayName) {
            this.displayName = displayName;
            return self();
        }

        public T setMaterial(Material material) {
            this.material = material;
            return self();
        }

        public T setLore(List<String> lore) {
            this.lore = lore;
            return self();
        }

        public T setVendorPrice(int vendorPrice) {
            this.vendorPrice = vendorPrice;
            return self();
        }

        public abstract CustomItem build();
    }

}


