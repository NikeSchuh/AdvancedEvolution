package de.philipp.advancedevolution.animations;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class Animation {

    private Plugin plugin;
    private BukkitTask task;

    public Animation(Plugin plugin) {
        this.plugin = plugin;

        task = new BukkitRunnable() {

            @Override
            public void run() {
                onAnimationTick();
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public void cancel() {
        if(task == null) return;
        task.cancel();
    }

    public abstract void onAnimationTick();

}
