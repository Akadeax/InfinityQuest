package me.akadeax.infinityquest.customitem.weapon.abilityweapon.ability;

import java.util.HashMap;

public class BlockAbilityHandler {
    public static HashMap<String, BlockAbility> blockAbilities = new HashMap<>();

    public static void init() {
        blockAbilities.put("guard", new GuardBlockAbility("guard"));
        blockAbilities.put("parry", new ParryBlockAbility("parry"));
    }

    public static BlockAbility getBlockAbility(String name) {
        return blockAbilities.get(name);
    }
}
