package de.philipp.advancedevolution.entities.particles;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public abstract class ParticleEntityVelocity extends ParticleEntity {

    private Vector speed;

    public ParticleEntityVelocity(Plugin plugin, Location startLoc, Vector speed) {
        super(plugin, startLoc);
        this.speed = speed;
    }

    @Override
    public void entityTick() {
        Location location = getLocation().clone().add(getVelocity().getX(), getVelocity().getY(), getVelocity().getZ());
        teleport(location);
        tick();
    }

}
