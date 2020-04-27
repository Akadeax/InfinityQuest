package me.akadeax.infinityquest.customitem.weapon.abilityweapon;

import me.akadeax.infinityquest.InfinityQuest;
import me.akadeax.infinityquest.customitem.CustomItem;
import me.akadeax.infinityquest.customitem.CustomItemType;
import me.akadeax.infinityquest.customitem.weapon.WeaponItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

/**
 * Abilities, in this case, trigger when getting hit a short time period after blocking
 */
public class AbilityWeaponItem extends WeaponItem {

    public static final class NBTKeys {
        public static final NamespacedKey blockAbility = new NamespacedKey(InfinityQuest.getInstance(), "blockAbility");
    }

    private String ability;

    public AbilityWeaponItem(String displayName, Material material, List<String> lore, double attackDamage, double attackSpeed, String ability) {
        super(displayName, material, lore, attackDamage, attackSpeed);
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


    public static class Builder<T extends Builder<T>> extends WeaponItem.Builder<T> {
        protected String ability = "guard";

        public T setAbility(String ability) {
            this.ability = ability;
            return self();
        }

        @Override
        public AbilityWeaponItem build() {
            return new AbilityWeaponItem(displayName, material, lore, attackDamage, attackSpeed, ability);
        }
    }

}
