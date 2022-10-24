package de.philipp.advancedevolution.entities.bosses.gaiagurdian;

import de.philipp.advancedevolution.entities.particles.ChasingParticleEntity;
import de.philipp.advancedevolution.entities.particles.Hitbox;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.nms.SkyUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class GaiaGuardianAttack extends ChasingParticleEntity {

    private long flyticks;
    private GaiaGuardian guardian;

    public GaiaGuardianAttack(Plugin plugin, Location startLoc, Entity target, GaiaGuardian damager, long flyticks) {
        super(plugin, startLoc, target, new Vector(15, 20, 15), new Hitbox(0.25, 0.25, 0.25));
        this.flyticks = flyticks;
        this.guardian = damager;
    }

    @Override
    public void tick() {
        if(!isValid()) {
            remove();
        }
        spawnParticle(getLocation());
        Location loc2 = getFocusLocation(target);
        Location loc1 = getLocation();
        final double distance = loc1.distance(loc2);
        if(flyticks > 0) {
            setVelocity(new Vector(
                    ((loc2.getX() - loc1.getX()) / (distance)) * speed.getX(),
                    7,
                    ((loc2.getZ() - loc1.getZ()) / (distance)) * speed.getX()));
            flyticks--;
        } else {
            setVelocity(new Vector(
                    ((loc2.getX() - loc1.getX()) / (distance)) * speed.getX(),
                    ((loc2.getY() - loc1.getY()) / (distance)) * speed.getY(),
                    ((loc2.getZ() - loc1.getZ()) / (distance)) * speed.getX()));
        }
        if(getLocation().getBlock().getType() != XMaterial.AIR.parseMaterial()) {
            onCollision(getLocation().getBlock());
        }
        for(Entity e : getNearbyEntities(hitbox.getDx(), hitbox.getDy(), hitbox.getDz())) {
            if(e.getUniqueId().equals(target.getUniqueId())) {
                onHit(target);
            }
        }

    }

    @Override
    public void onCollision(Block block) {

    }

    @Override
    public void onHit(Entity target) {
      if(target instanceof Player) {
          Player player = (Player) target;
          player.damage(8, guardian.getBossEntity());
          player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 85, 2));
      }
      remove();
    }

    @Override
    public void spawnParticle(Location location) { location.getWorld().spawnParticle(Particle.SPELL_WITCH, location, 5, 0.25, 0.25 ,0.25 ,0);
    }
}
