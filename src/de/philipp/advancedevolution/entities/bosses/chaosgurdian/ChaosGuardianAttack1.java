package de.philipp.advancedevolution.entities.bosses.chaosgurdian;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.entities.particles.ChasingParticleEntity;
import de.philipp.advancedevolution.entities.particles.Hitbox;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class ChaosGuardianAttack1 extends ChasingParticleEntity {

    private ChaosGuardian guardian;

    public ChaosGuardianAttack1(ChaosGuardian guardian, LivingEntity target) {
        super(AdvancedEvolution.getInstance(), guardian.getBossEntity().getEyeLocation(), target, new Vector(15, 15, 15), new Hitbox(0.5, 0.5, 0.5));
        this.guardian = guardian;
    }

    public ChaosGuardianAttack1(ChaosGuardian guardian, LivingEntity target, Vector vector) {
        super(AdvancedEvolution.getInstance(), guardian.getBossEntity().getEyeLocation(), target, vector, new Hitbox(0.5, 0.5, 0.5));
        this.guardian = guardian;
    }

    @Override
    public void onCollision(Block block) {
        remove();
    }

    public Location getFocusLocation(Entity target) {
        return ((LivingEntity) target).getEyeLocation();
    }

    @Override
    public void onHit(Entity target) {
        if(target instanceof LivingEntity) {
            ((LivingEntity)target).damage(100, guardian.getBossEntity());
        }
        remove();
    }

    @Override
    public void spawnParticle(Location location) {
        location.getWorld().spawnParticle(Particle.FLAME, location, 1, 0, 0, 0, 0);
    }
}
