package me.akadeax.infinityquest.customitem.weapon.blockableweapon;

import me.akadeax.infinityquest.customitem.CustomItemType;
import me.akadeax.infinityquest.customitem.CustomItemUtil;
import me.akadeax.infinityquest.customitem.weapon.blockableweapon.ability.BlockAbility;
import me.akadeax.infinityquest.customitem.weapon.blockableweapon.ability.BlockAbilityHandler;
import me.akadeax.infinityquest.util.TimeUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
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

public class PlayerBlockEvent implements Listener {

    TimeUtil timeUtil;
    public FileConfiguration config;

    public PlayerBlockEvent(JavaPlugin plugin) {
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
        if(!CustomItemUtil.isCustomItem(playerHand)) return;
        if(e.getHand() != EquipmentSlot.HAND || hasBlockCooldown(p)) return;

        PersistentDataContainer handContainer = CustomItemUtil.getItemContainer(playerHand);
        if(handContainer == null) return;
        String abilityString = handContainer.get(BlockableWeaponItem.NBTKeys.blockAbility, PersistentDataType.STRING);
        BlockAbility ability = BlockAbilityHandler.getBlockAbility(abilityString);

        currentBlockTime = ability.get("blockTime", Integer.class);
        currentBlockCooldownTime = ability.get("blockCooldownTime", Integer.class);

        onBlockButtonPress(p, ability);
        int updateTask = timeUtil.repeat(() -> whileBlocking(p, ability), 0, 1, currentBlockTime);
        int startCooldownTask = timeUtil.runDelayed(() -> startBlockCooldown(p, ability), currentBlockTime);

        playerBlockTasks.put(p.getUniqueId(), new int[] { updateTask, startCooldownTask });
    }

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

        String abilityName = handContainer.get(BlockableWeaponItem.NBTKeys.blockAbility, PersistentDataType.STRING);
        BlockAbility hitAbility = BlockAbilityHandler.getBlockAbility(abilityName);

        hitAbility.onBlockerHit(hit, e, timeUtil);

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
