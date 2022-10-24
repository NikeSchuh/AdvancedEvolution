package de.philipp.advancedevolution.entities.bosses.piglinlord;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.entities.particles.ChasingParticleEntity;
import de.philipp.advancedevolution.entities.particles.Hitbox;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.lib.xseries.XSound;
import de.philipp.advancedevolution.util.LocationUtil;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class PiglinThrowAxe extends ChasingParticleEntity {

    protected Player player;
    protected PiglinLord piglinLord;
    protected ArmorStand armorStand;
    protected EulerAngle currentAngle = new EulerAngle(0, 0, 0);
    private int health = 4;

    public PiglinThrowAxe(PiglinLord piglinLord, Location startLoc, Player target, Vector speed, Hitbox hitbox) {
        super(AdvancedEvolution.getInstance(), startLoc, target, speed, hitbox);
        this.player = target;
        this.piglinLord = piglinLord;
        this.armorStand = (ArmorStand) startLoc.getWorld().spawnEntity(startLoc.clone(), EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setMarker(true);
        armorStand.setItemInHand(XMaterial.NETHERITE_AXE.parseItem());

    }

    @Override
    public Location getFocusLocation(Entity target) {
        return player.getEyeLocation();
    }

    @Override
    public void onCollision(Block block) {
        health--;
        block.breakNaturally();
        if(health <= 0) {
            piglinLord.setAxeThrown(false);
            armorStand.remove();
            block.getLocation().getWorld().playSound(block.getLocation(), XSound.ENTITY_WITHER_BREAK_BLOCK.parseSound(), 1f, 1f);
            remove();
        }
    }

    @Override
    public void onHit(Entity target) {
        armorStand.remove();
        piglinLord.setAxeThrown(false);
        target.getLocation().getWorld().strikeLightning(target.getLocation());
        player.damage(15, piglinLord.getBossEntity());
        player.playSound(player.getLocation(), XSound.ITEM_SHIELD_BREAK.parseSound(), 2f, 1.25f);
        remove();
    }

    @Override
    public void spawnParticle(Location location) {
        LocationUtil.faceLocation(armorStand, target.getLocation());
        currentAngle = currentAngle.add(0.3, 0, 0);
        armorStand.teleport(location);
        armorStand.setRightArmPose(currentAngle);
        if(Math.random() < 0.075) {
            location.getWorld().playSound(location, XSound.ITEM_ELYTRA_FLYING.parseSound(), 0.1f, 5f);
        }
        location.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location.clone().add(0, 1, 0), 3, 0.5, 0.5, 0.5, 0);
    }
}
