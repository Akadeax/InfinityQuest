package me.akadeax.infinityquest.util;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TimeUtil {

    private JavaPlugin plugin;

    public TimeUtil(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public int runDelayed(Runnable toRun, int delay) {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                toRun.run();
            }
        };
        runnable.runTaskLater(plugin, delay);
        return runnable.getTaskId();
    }

    public int repeat(Runnable toRun, int initialDelay, int ticksBetweenRepeats, int repeatForTicks) {
        BukkitRunnable runnable = new BukkitRunnable() {

            int timesRepeated = 0;
            @Override
            public void run() {
                if(timesRepeated * ticksBetweenRepeats >= repeatForTicks) {
                    this.cancel();
                    return;
                }

                toRun.run();

                timesRepeated++;
            }
        };
        runnable.runTaskTimer(plugin, initialDelay + 1, ticksBetweenRepeats);
        return runnable.getTaskId();
    }
}
