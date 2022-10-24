package de.philipp.advancedevolution.animations;

import de.philipp.advancedevolution.AdvancedEvolution;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Delay {

    public Delay(long delay) {
        new BukkitRunnable() {
            @Override
            public void run() {
                end();
            }
        }.runTaskLater(AdvancedEvolution.getInstance(), delay);
    }

    public abstract void end();

}
