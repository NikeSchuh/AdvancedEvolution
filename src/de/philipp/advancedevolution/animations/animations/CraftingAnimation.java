package de.philipp.advancedevolution.animations.animations;

import de.philipp.advancedevolution.blocks.DraconicBlock;
import de.philipp.advancedevolution.lib.xseries.XSound;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public abstract class CraftingAnimation {

    public static List<CraftingAnimation> runningCrafters = new ArrayList<>();

    private Plugin plugin;
    private Location center;
    private double durationFactor;

    public CraftingAnimation(Plugin plugin, Location location, double durationFactor) {
        runningCrafters.add(this);

        this.plugin = plugin;
        this.center = location;
        this.durationFactor = durationFactor;

        run();
    }

    public void run() {
        Location location = center.clone();
        new BukkitRunnable() {
            int ticks = 0;
            private double t = 0;
            public void run() {
                center.getWorld().playSound(center, XSound.BLOCK_CONDUIT_AMBIENT.parseSound(), (float) (0.5f + (ticks / (512 * durationFactor))), (float) ((0.0125 / durationFactor) * ticks));
                if(ticks > 40 * durationFactor) {
                    location.getWorld().spawnParticle(Particle.PORTAL, center.clone().add(0, 3, 0), (int) ((ticks - (35 * durationFactor)) / 50) + 1, 0.5, 0.5, 0.5, 0.05f);
                    center.getWorld().playSound(center, XSound.AMBIENT_BASALT_DELTAS_ADDITIONS.parseSound(), 0.25f, (float) ((0.075 / durationFactor) * ticks));
                }
                for(int i = 0; i < 10; i++) {
                    double r = 0.75;
                    t = t + Math.PI / (128 / ((ticks / 20) / durationFactor));
                    double x = r * Math.cos(t);
                    double y = Math.cos(t * 0.5);
                    double z = r * Math.sin(t);
                    location.add(x, y, z);
                    location.getWorld().spawnParticle(Particle.PORTAL, location, 2, 0, 0, 0, 0.001f);
                    location.subtract(x, y, z);
                    if (t > Math.PI * 8) {
                        t = 0;
                    }
                }
                ticks++;
                if(ticks == (80 * durationFactor)) {
                    animationEnd(center.add(0, 5, 0));
                    cancel();
                }
            }

        }.runTaskTimer(plugin, 0, 1);
    }

    public abstract void animationEnd(Location location);


}
