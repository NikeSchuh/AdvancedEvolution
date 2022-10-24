package de.philipp.advancedevolution.entities.particles;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.UUID;

public abstract class ParticleEntity {

    private Plugin plugin;
    private Location currentLocation;
    private Vector velocity;
    private UUID uuid;
    private BukkitTask entityTick;

    public ParticleEntity(Plugin plugin, Location startLoc) {
        this.plugin = plugin;
        this.currentLocation = startLoc.clone();
        this.velocity = new Vector(0, 0 ,0);
        this.uuid = UUID.randomUUID();

        entityTick = new BukkitRunnable() {
            @Override
            public void run() {
                entityTick();
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public Collection<Entity> getNearbyEntities(double xd, double yd, double zd) {
        return currentLocation.getWorld().getNearbyEntities(currentLocation, xd, yd, zd);
    }

    public void remove() {
        entityTick.cancel();
    }

    public void teleport(Location location) {
        currentLocation = location;
    }

    public Location getLocation() {
        return currentLocation;
    }

    public UUID getUUID() {
        return uuid;
    }

    public World getWorld() {
        return currentLocation.getWorld();
    }

    public void entityTick() {
        currentLocation = currentLocation.add(velocity.getX() / 20, velocity.getY() / 20, velocity.getZ() / 20);
        tick();
    }

    public abstract void tick();

}
