package de.philipp.advancedevolution.util;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ArmorEffect {

    private Player player;
    private BukkitTask handler;
    private boolean canceled = false;

    public ArmorEffect(Player player, Plugin plugin) {
        this.player = player;
        this.handler = new BukkitRunnable() {
            @Override
            public void run() {
                if(canceled) {
                    this.cancel();
                }


            }
        }.runTaskTimerAsynchronously(plugin, 0 ,1);
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
