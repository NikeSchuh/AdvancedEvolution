package de.philipp.advancedevolution;

import de.philipp.advancedevolution.entities.particles.ParticleEntity;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public abstract class FireBall extends ParticleEntity {

    private Location location;
    private Entity sender;
    private Entity target;
    private Plugin plugin;


    public FireBall(Plugin plugin, Location location, Entity sender, Entity target) {
        super(plugin, location);
        this.location = location;
        this.sender = sender;
        this.target = target;
        this.plugin = plugin;

    }

    public abstract void onTargetHit(Entity target);
    public void tick() {
        Location loc2 = target.getLocation();
        Location loc1 = getLocation();
        final double distance = loc1.distance(loc2);
        setVelocity(new Vector(
                (loc2.getX() - loc1.getX()) / (distance),
                ((loc2.getY() - loc1.getY()) / (distance)),
                (loc2.getZ() - loc1.getZ()) / (distance)));
        if(getNearbyEntities(2, 2, 2).contains(target)) {
            remove();
            onTargetHit(target);
        }

        Location loc3 = loc1.clone();
        for (double i = 0; i <= Math.PI; i += Math.PI / 2) {
            double radius = Math.sin(i);
            double y = Math.cos(i) + 1;
            for (double a = 0; a < Math.PI * 2; a+= Math.PI / 2) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                loc3.add(x, y - 0.5, z);
                loc3.getWorld().spawnParticle(Particle.FLAME, loc1, 1, 0, 0, 0, 0.1);
                loc3.subtract(x, y - 0.5, z);
            }
        }
    }


}
