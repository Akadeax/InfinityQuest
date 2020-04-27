package me.akadeax.infinityquest.customitem.weapon;

import me.akadeax.infinityquest.InfinityQuest;
import me.akadeax.infinityquest.customitem.CustomItem;
import me.akadeax.infinityquest.customitem.CustomItemType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class WeaponItem extends CustomItem {
    public static final class NBTKeys {
        public static final NamespacedKey attackDamage = new NamespacedKey(InfinityQuest.getInstance(), "attackDamage");
    }

    public double attackDamage;
    public double attackSpeed;

    public WeaponItem(String displayName, Material material, List<String> lore, double attackDamage, double attackSpeed) {
        super(displayName, material, lore);
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
    }

    @Override
    public ItemStack generateItemStack() {
        ItemStack newStack = super.generateItemStack();
        ItemMeta newMeta = newStack.getItemMeta();
        if(newMeta == null) return null;

        AttributeModifier mod = new AttributeModifier("generic.attackSpeed", attackSpeed, AttributeModifier.Operation.ADD_NUMBER);
        newMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, mod);

        List<String> newLore = newMeta.getLore();
        if(newLore == null) newLore = new ArrayList<>();

        // add attack dmg & speed as lore below the item
        newLore.add(String.format("§fAttack damage: §4%s♥§f", attackDamage));

        String roundedAttackSpeed = String.format("%.1f", 1 / Math.abs(attackSpeed) * 10);
        newLore.add(String.format("§fAttack speed: §a%s", roundedAttackSpeed));

        newMeta.setLore(newLore);

        // set weapon-specific NBT tags
        PersistentDataContainer container = newMeta.getPersistentDataContainer();
        container.set(CustomItem.NBTKeys.itemType, PersistentDataType.STRING, CustomItemType.WEAPON);
        container.set(NBTKeys.attackDamage, PersistentDataType.DOUBLE, attackDamage);

        newStack.setItemMeta(newMeta);
        return newStack;
    }


    public static class Builder<T extends Builder<T>> extends CustomItem.Builder<T> {

        protected double attackDamage = 7d;
        protected double attackSpeed = -2.5d;

        public T setAttackDamage(double attackDamage) {
            this.attackDamage = attackDamage;
            return self();
        }

        public T setAttackSpeed(double attackSpeed) {
            this.attackSpeed = attackSpeed;
            return self();
        }

        @Override
        public WeaponItem build() {
            return new WeaponItem(displayName, material, lore, attackDamage, attackSpeed);
        }
    }

}
