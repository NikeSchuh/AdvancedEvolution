package de.philipp.advancedevolution.entities;

import de.philipp.advancedevolution.entities.particles.ChasingParticleEntity;
import de.philipp.advancedevolution.entities.particles.Hitbox;
import de.philipp.advancedevolution.lib.xseries.XSound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class RedShot extends ChasingParticleEntity {

    public static List<Entity> targets = new ArrayList<>();
    private Player shooter;

    public RedShot(Plugin plugin, Player shooter, Location startLoc, Entity target, Vector speed, Hitbox hitbox) {
        super(plugin, startLoc, target, speed, hitbox);
        if(target.isInvulnerable()) remove();
        if(!(target instanceof LivingEntity)) remove();
        if(targets.contains(target)) remove();
        targets.add(target);
        this.shooter = shooter;
    }

    @Override
    public void onCollision(Block block) {

    }

    @Override
    public void onHit(Entity target) {
        if(target instanceof LivingEntity) {
            LivingEntity mob = (LivingEntity) target;
            mob.getWorld().playSound(mob.getLocation(), XSound.AMBIENT_CRIMSON_FOREST_MOOD.parseSound(), 100f, 1f);
            mob.getWorld().spawnParticle(Particle.WHITE_ASH, mob.getEyeLocation(), 50, 0.3, 0.5, 0.3, 0.2);
            mob.getWorld().spawnParticle(Particle.SOUL, mob.getEyeLocation(), 5, 0.3, 0.5, 0.3, 0.2);
            mob.damage(100, shooter);
        }

        remove();
    }

    public Location getFocusLocation(Entity target) {
        return ((LivingEntity) target).getEyeLocation();
    }

    @Override
    public void spawnParticle(Location location) {
        location.getWorld().spawnParticle(Particle.SOUL, location, 2, 0.125 ,0, 0.125, 0);
    }
    public void remove() {
        targets.remove(target);
        super.remove();
    }
}
