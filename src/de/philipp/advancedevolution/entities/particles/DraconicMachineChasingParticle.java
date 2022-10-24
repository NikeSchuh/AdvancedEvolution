package de.philipp.advancedevolution.entities.particles;

import de.philipp.advancedevolution.blocks.DraconicMachine;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.awt.image.ByteLookupTable;

public abstract class DraconicMachineChasingParticle extends ParticleEntity {

    private DraconicMachine target;
    private Vector speed;

    public DraconicMachineChasingParticle(Plugin plugin, Location startLoc, DraconicMachine target, Vector speed) {
        super(plugin, startLoc);
        this.target = target;
        this.speed = speed;
    }

    public boolean isValid() {
        if(target.getBlock().getType() == XMaterial.AIR.parseMaterial()) {
            return false;
        }
        return true;
    }

    public DraconicMachine getTarget() {
        return target;
    }

    public Vector getSpeed() {
        return speed;
    }

    public abstract void spawnParticle(Location location);
    public abstract void onCollision(Block block);
    public abstract void onHit(DraconicMachine target);


    @Override
    public void tick() {
        if(!getLocation().getChunk().isLoaded()) return;
        if(!isValid()) {
            remove();
        }
        spawnParticle(getLocation());
        Location loc2 = target.getBlock().getLocation().add(0.5, 0.5, 0.5);
        Location loc1 = getLocation();
        final double distance = loc1.distance(loc2);
        setVelocity(new Vector(
                ((loc2.getX() - loc1.getX()) / (distance)) * speed.getX(),
                ((loc2.getY() - loc1.getY()) / (distance)) * speed.getY(),
                ((loc2.getZ() - loc1.getZ()) / (distance)) * speed.getZ()));
        if(getLocation().getBlock().getType() != XMaterial.AIR.parseMaterial()) {
            Block block = getLocation().getBlock();
            if(block.getLocation().equals(target.getBlock().getLocation())) {
                onHit(target);
            } else {
                onCollision(block);
            }
        }
        }

    }
