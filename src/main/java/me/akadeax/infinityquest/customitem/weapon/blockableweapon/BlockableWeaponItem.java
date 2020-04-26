package me.akadeax.infinityquest.customitem.weapon.blockableweapon;

import me.akadeax.infinityquest.InfinityQuest;
import me.akadeax.infinityquest.customitem.CustomItem;
import me.akadeax.infinityquest.customitem.CustomItemType;
import me.akadeax.infinityquest.customitem.weapon.WeaponItem;
import me.akadeax.infinityquest.customitem.weapon.blockableweapon.ability.BlockAbility;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class BlockableWeaponItem extends WeaponItem {

    public static final class NBTKeys {
        public static final NamespacedKey blockAbility = new NamespacedKey(InfinityQuest.getInstance(), "blockAbility");
    }

    private String ability;

    public BlockableWeaponItem(String displayName, Material material, double attackDamage, double attackSpeed, String ability) {
        super(displayName, material, attackDamage, attackSpeed);
        this.ability = ability;
    }

    @Override
    public ItemStack generateItemStack() {
        ItemStack newStack = super.generateItemStack();
        ItemMeta newMeta = newStack.getItemMeta();
        if (newMeta == null) return null;

        PersistentDataContainer container = newMeta.getPersistentDataContainer();
        container.set(CustomItem.NBTKeys.itemType, PersistentDataType.STRING, CustomItemType.BLOCKABLE_WEAPON);
        container.set(NBTKeys.blockAbility, PersistentDataType.STRING, ability);

        newStack.setItemMeta(newMeta);
        return newStack;
    }
}
