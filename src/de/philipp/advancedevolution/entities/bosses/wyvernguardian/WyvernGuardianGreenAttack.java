package de.philipp.advancedevolution.entities.bosses.wyvernguardian;

import de.philipp.advancedevolution.entities.DraconicBossEntity;
import de.philipp.advancedevolution.entities.bosses.wyvernguardian.WyvernGuardian;
import de.philipp.advancedevolution.entities.particles.ChasingParticleEntity;
import de.philipp.advancedevolution.entities.particles.Hitbox;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class WyvernGuardianGreenAttack extends ChasingParticleEntity {

    private double damage;
    private DraconicBossEntity guardian;

    public WyvernGuardianGreenAttack(Plugin plugin, Location startLoc, Player target, DraconicBossEntity bossEntity, double damage) {
        super(plugin, startLoc, target, new Vector(5, 5, 5), new Hitbox(2, 2, 2));
        this.damage = damage;
        this.guardian = bossEntity;

        if(target.isInvulnerable()) {
            remove();
        }
    }

    public WyvernGuardianGreenAttack(Plugin plugin, Location startLoc, Player target, Vector speed, DraconicBossEntity bossEntity, double damage) {
        super(plugin, startLoc, target, speed, new Hitbox(2, 2, 2));
        this.damage = damage;
        this.guardian = bossEntity;

        if(target.isInvulnerable()) {
            remove();
        }
    }

    @Override
    public void onCollision(Block block) {
        if(block.getType().isSolid()) {
            block.getLocation().getWorld().createExplosion(block.getLocation(), 2f);
            remove();
        }
    }

    @Override
    public void onHit(Entity target) {
        ((Player) target).damage(damage, guardian.getBossEntity());
        ((Player) target).setNoDamageTicks(0);
        remove();
    }

    public Location getFocusLocation(Entity target) {
        return ((LivingEntity) target).getEyeLocation();
    }

    @Override
    public void spawnParticle(Location location) {
        location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location, 1, 0, 0, 0, 0.02);
    }
}
