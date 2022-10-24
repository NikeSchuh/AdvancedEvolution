package de.philipp.advancedevolution.entities.bosses.wyvernguardian;

import de.philipp.advancedevolution.entities.bosses.wyvernguardian.WyvernGuardian;
import de.philipp.advancedevolution.entities.particles.ChasingParticleEntity;
import de.philipp.advancedevolution.entities.particles.Hitbox;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class WyvernGuardianPurpleAttack extends ChasingParticleEntity {

    private double damage;
    private WyvernGuardian guardian;

    public WyvernGuardianPurpleAttack(Plugin plugin, Location startLoc, Player target, WyvernGuardian wyvernGuardian, double damage) {
        super(plugin, startLoc, target, new Vector(5, 5, 5), new Hitbox(2, 2, 2));
        this.damage = damage;
        this.guardian = wyvernGuardian;

        if(target.isInvulnerable()) {
            remove();
        }
    }

    public WyvernGuardianPurpleAttack(Plugin plugin, Location startLoc, Player target, Vector speed, WyvernGuardian wyvernGuardian, double damage) {
        super(plugin, startLoc, target, speed, new Hitbox(2, 2, 2));
        this.damage = damage;
        this.guardian = wyvernGuardian;

        if(target.isInvulnerable()) {
            remove();
        }
    }

    @Override
    public void onCollision(Block block) {
        if(block.getType().isSolid()) {
            block.getLocation().getWorld().createExplosion(block.getLocation(), 1f);
            remove();
        }
    }

    @Override
    public void onHit(Entity target) {
        ((Player) target).damage(damage, guardian.getBossEntity());
        ((Player) target).setNoDamageTicks(0);
        remove();
    }

    @Override
    public void spawnParticle(Location location) {
        location.getWorld().spawnParticle(Particle.DRAGON_BREATH, location, 3, 0, 0, 0, 0.02);
    }
}
