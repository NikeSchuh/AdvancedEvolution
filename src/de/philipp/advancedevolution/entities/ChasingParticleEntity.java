package de.philipp.advancedevolution.entities;

import de.philipp.advancedevolution.lib.xseries.XMaterial;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public abstract class ChasingParticleEntity extends ParticleEntity implements ICollisionEntity{

    private Entity target;
    private Vector speed;
    private Hitbox hitbox;

    public ChasingParticleEntity(Plugin plugin, Location startLoc, Entity target, Vector speed, Hitbox hitbox) {
        super(plugin, startLoc);
        this.target = target;
        this.speed = speed;
        this.hitbox = hitbox;
    }

    @Override
    public void tick() {
        spawnParticle(getLocation());
        Location loc2 = target.getLocation();
        Location loc1 = getLocation();
        final double distance = loc1.distance(loc2);
        setVelocity(new Vector(
                ((loc2.getX() - loc1.getX()) / (distance)) * speed.getX(),
                ((loc2.getY() - loc1.getY()) / (distance)) * speed.getY(),
                ((loc2.getZ() - loc1.getZ()) / (distance)) * speed.getZ()));
        if(getLocation().getBlock().getType() != XMaterial.AIR.parseMaterial()) {
            onCollision(getLocation().getBlock());
        }
        for(Entity e : getNearbyEntities(hitbox.getDx(), hitbox.getDy(), hitbox.getDz())) {
            if(e.getUniqueId().equals(target.getUniqueId())) {
                onHit(target);
            }
        }

    }

    public abstract void onCollision(Block block);
    public abstract void onHit(Entity target);
    public abstract void spawnParticle(Location location);
}
