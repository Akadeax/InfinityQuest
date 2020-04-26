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

    public WeaponItem(String displayName, Material material, double attackDamage, double attackSpeed) {
        super(displayName, material);
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

        newMeta.setDisplayName("§f" + displayName);

        List<String> newLore = newMeta.getLore();
        if(newLore == null) newLore = new ArrayList<>();
        String roundedAttackSpeed = String.format("%.1f", 1 / Math.abs(attackSpeed) * 10);
        newLore.add(String.format("§fAttack damage: §4%s♥§f, hit speed: §a%s", attackDamage, roundedAttackSpeed));
        newMeta.setLore(newLore);

        PersistentDataContainer container = newMeta.getPersistentDataContainer();
        container.set(CustomItem.NBTKeys.itemType, PersistentDataType.STRING, CustomItemType.WEAPON);
        container.set(NBTKeys.attackDamage, PersistentDataType.DOUBLE, attackDamage);

        newStack.setItemMeta(newMeta);
        return newStack;
    }
}
