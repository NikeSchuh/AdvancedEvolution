package de.philipp.advancedevolution.animations.animations;

import de.philipp.advancedevolution.animations.LivingEntityAnimation;
import de.philipp.advancedevolution.entities.bosses.wyvernguardian.WyvernGuardian;
import de.philipp.advancedevolution.lib.xseries.XSound;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class WyvernGuardianDeath extends LivingEntityAnimation {

    private WyvernGuardian wyvernGuardian;
    private int currentTick = 0;

    public WyvernGuardianDeath(Plugin plugin, WyvernGuardian wyvernGuardian) {
        super(plugin, wyvernGuardian.getBossEntity());
        new BukkitRunnable(){
            double t = Math.PI/4;
            Location loc = wyvernGuardian.getLocation();
            public void run(){
                t = t + 0.1*Math.PI;
                for (double theta = 0; theta <= 2*Math.PI; theta = theta + Math.PI/32){
                    double x = t*cos(theta);
                    double y = 2*Math.exp(-0.1*t) * sin(t) + 1.5;
                    double z = t*sin(theta);
                    loc.add(x,y,z);
                    loc.getWorld().spawnParticle(Particle.DRIP_LAVA, loc.clone().add(0, 1, 0), 10, 0, 0, 0, 0);
                    loc.subtract(x,y,z);

                    theta = theta + Math.PI/64;

                    x = t*cos(theta);
                    y = 2*Math.exp(-0.1*t) * sin(t) + 1.5;
                    z = t*sin(theta);
                    loc.add(x,y,z);
                    loc.getWorld().spawnParticle(Particle.DRIP_LAVA, loc.clone().add(0, 1, 0), 10, 0, 0, 0, 0);
                    loc.subtract(x,y,z);
                }
                if (t > 20){
                    this.cancel();
                }
            }

        }.runTaskTimer(plugin, 0, 1);
    }

    public double cos(double d) {
        return Math.cos(d);
    }

    public double sin(double d) {
        return Math.sin(d);
    }

    @Override
    public void onAnimationTick(LivingEntity entity) {
        entity.getLocation().getWorld().playSound(entity.getLocation(), XSound.ENTITY_GUARDIAN_DEATH.parseSound(), 100, (float) Math.random() * 2);
        Location loc = entity.getLocation();
        loc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc.clone().add(0, 1, 0), 10, 0, 0, 0.05);
        loc.setYaw(loc.getYaw() + 20);
        loc.add(0, 0.1, 0);
        entity.teleport(loc);
        currentTick++;
        if(currentTick == 100) {
            entity.getLocation().getWorld().strikeLightningEffect(entity.getLocation());
            end();
            cancel();
        }
    }

    public abstract void end();
}
