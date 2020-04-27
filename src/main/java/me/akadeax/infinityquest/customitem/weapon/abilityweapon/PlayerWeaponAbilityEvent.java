package me.akadeax.infinityquest.customitem.weapon.abilityweapon;

import me.akadeax.infinityquest.customitem.CustomItemType;
import me.akadeax.infinityquest.customitem.CustomItemUtil;
import me.akadeax.infinityquest.customitem.weapon.abilityweapon.ability.BlockAbility;
import me.akadeax.infinityquest.customitem.weapon.abilityweapon.ability.BlockAbilityHandler;
import me.akadeax.infinityquest.util.TimeUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.PacketPlayOutAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class PlayerWeaponAbilityEvent implements Listener {

    TimeUtil timeUtil;
    public FileConfiguration config;

    public PlayerWeaponAbilityEvent(JavaPlugin plugin) {
        config = plugin.getConfig();
        timeUtil = new TimeUtil(plugin);
    }

    private Integer currentBlockTime;
    private Integer currentBlockCooldownTime;

    private HashMap<UUID, int[]> playerBlockTasks = new HashMap<>();

    @EventHandler
    void onPlayerBlock(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack playerHand = e.getPlayer().getInventory().getItemInMainHand();
        // if in hand can't have an ability, cancel
        if(!CustomItemUtil.getCustomItemType(playerHand).equals(CustomItemType.BLOCKABLE_WEAPON)) return;
        if(e.getHand() != EquipmentSlot.HAND || hasBlockCooldown(p)) return;

        // get the ability of the player's weapon from the string stored on it
        PersistentDataContainer handContainer = CustomItemUtil.getItemContainer(playerHand);
        if(handContainer == null) return;
        String abilityString = handContainer.get(AbilityWeaponItem.NBTKeys.blockAbility, PersistentDataType.STRING);
        BlockAbility ability = BlockAbilityHandler.getBlockAbility(abilityString);

        // store both timings here
        currentBlockTime = ability.get("blockTime", Integer.class);
        currentBlockCooldownTime = ability.get("blockCooldownTime", Integer.class);

        // start triggering functions of ability when they're supposed to be
        onBlockButtonPress(p, ability);
        // store task id's so we can cancel them later
        int updateTask = timeUtil.repeat(() -> whileBlocking(p, ability), 0, 1, currentBlockTime);
        int startCooldownTask = timeUtil.runDelayed(() -> startBlockCooldown(p, ability), currentBlockTime);

        playerBlockTasks.put(p.getUniqueId(), new int[] { updateTask, startCooldownTask });
    }

    // from here on it's basically just triggering the BLockAbility's methods at the right time
    void startBlockCooldown(Player p, BlockAbility ability) {
        onBlockCooldownStart(p, ability);
        timeUtil.repeat(() -> whileBlockCooldown(p, ability), 0, 1, currentBlockCooldownTime);
        timeUtil.runDelayed(() -> afterBlockCooldown(p, ability), currentBlockCooldownTime);
    }

    private void onBlockButtonPress(Player p, BlockAbility ability) {
        setBlockCooldown(p, currentBlockCooldownTime + currentBlockTime);
        ability.onBlockButtonPress(p);
    }

    private void whileBlocking(Player p, BlockAbility ability) {
        ability.whileBlocking(p);
    }

    private void onBlockCooldownStart(Player p, BlockAbility ability) {
        ability.onBlockCooldownStart(p);
    }

    private void whileBlockCooldown(Player p, BlockAbility ability) {
        String playerCooldownRounded = String.format("§a%.1fs", getBlockCooldown(p) / 20d);
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(playerCooldownRounded));
        ability.whileBlockCooldown(p);
    }

    private void afterBlockCooldown(Player p, BlockAbility ability) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));
        ability.afterBlockCooldown(p);
    }


    @EventHandler
    public void onBlockerHit(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player hit = (Player)e.getEntity();

        if(!isBlocking(hit)) return;
        ItemStack hitHand = hit.getInventory().getItemInMainHand();
        if(!CustomItemUtil.getCustomItemType(hitHand).equals(CustomItemType.BLOCKABLE_WEAPON)) return;

        PersistentDataContainer handContainer = CustomItemUtil.getItemContainer(hitHand);
        if(handContainer == null) return;

        String abilityName = handContainer.get(AbilityWeaponItem.NBTKeys.blockAbility, PersistentDataType.STRING);
        BlockAbility hitAbility = BlockAbilityHandler.getBlockAbility(abilityName);

        hitAbility.onBlockerHit(hit, e, timeUtil);

        EntityPlayer player = ((CraftPlayer)hit).getHandle();
        PacketPlayOutAnimation packet = new PacketPlayOutAnimation(player, 0);
        player.playerConnection.sendPacket(packet);

        setBlockCooldown(hit, currentBlockCooldownTime);

        // cancel tasks when blocking is successful, start cooldown task from new
        if(playerBlockTasks.containsKey(hit.getUniqueId())) {
            for(int task : playerBlockTasks.get(hit.getUniqueId())) {
                Bukkit.getScheduler().cancelTask(task);
            }
            startBlockCooldown(hit, hitAbility);
            playerBlockTasks.remove(hit.getUniqueId());
        }


    }


    // wrapper functions around using cooldown(Material.AIR) to measure ability cooldown
    private boolean hasBlockCooldown(Player p) {
        return p.hasCooldown(Material.AIR);
    }
    private int getBlockCooldown(Player p) {
        return p.getCooldown(Material.AIR);
    }
    private void setBlockCooldown(Player p, int ticks) {
        p.setCooldown(Material.AIR, ticks);
    }
    public boolean isBlocking(Player p) {
        if(currentBlockTime == null) return false;
        return p.getCooldown(Material.AIR) - currentBlockCooldownTime > 0;
    }
}
