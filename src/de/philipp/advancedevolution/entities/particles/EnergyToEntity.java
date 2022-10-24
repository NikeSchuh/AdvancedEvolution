package de.philipp.advancedevolution.entities.particles;

import de.philipp.advancedevolution.entities.particles.ChasingParticleEntity;
import de.philipp.advancedevolution.entities.particles.Hitbox;
import de.philipp.advancedevolution.lib.xseries.XSound;
import de.philipp.advancedevolution.shield.Shield;
import de.philipp.advancedevolution.shield.ShieldManager;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public abstract class EnergyToEntity extends ChasingParticleEntity {

    private double energyToSend;
    private XSound receiveSound;

    public EnergyToEntity(Plugin plugin, Location startLoc, Player target, Vector speed, Hitbox hitBox, double energyToSend, XSound receiveSound) {
        super(plugin, startLoc, target, speed, hitBox);
        this.energyToSend = energyToSend;
        this.receiveSound = receiveSound;
    }

    public abstract void onHit();

    @Override
    public void onCollision(Block block) {

    }

    @Override
    public void onHit(Entity target) {
        Player player = (Player) target;
        Shield shield = ShieldManager.playerShieldMap.get(player.getUniqueId());
        shield.addEnergy(energyToSend);
        player.playSound(player.getLocation(), receiveSound.parseSound(), 1f, 100);
        onHit();
        remove();
    }

    @Override
    public void spawnParticle(Location location) {
        getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location, 1, 0, 0 ,0,0.01f);
    }

}
