package de.philipp.advancedevolution.entities.bosses.god;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.entities.particles.ChasingParticleEntity;
import de.philipp.advancedevolution.entities.particles.Hitbox;
import de.philipp.advancedevolution.entities.particles.ParticleVelocityChasing;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class GodAttackNormal extends ParticleVelocityChasing {

    private double damage;
    private Player target;
    private God god;

    public GodAttackNormal(Location startLoc, Player target, Vector speed, God god, double damage) {
        super(AdvancedEvolution.getInstance(), startLoc, target, speed, 0.1, new Hitbox(2, 2, 2));
        this.damage = damage;
        this.target = target;
        this.god = god;
    }

    @Override
    public void onCollision(Block block) {

    }

    @Override
    public void onHit(Entity target) {
        this.target.damage(damage, god.getBossEntity());
        remove();
    }

    @Override
    public void spawnParticle(Location location) {
        location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location, 1, 0, 0, 0, 0.02);
    }
}
