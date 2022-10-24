package de.philipp.advancedevolution.entities.bosses.blazeinferno;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.FireBall;
import de.philipp.advancedevolution.entities.DraconicBossEntity;
import de.philipp.advancedevolution.items.DraconicItem;
import de.philipp.advancedevolution.lib.xseries.XSound;
import de.philipp.advancedevolution.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BlazeInferno extends DraconicBossEntity {

    private Blaze blaze;

    public BlazeInferno(Location location) {
        super(location, EntityType.BLAZE, AdvancedEvolution.getInstance(), "§6Inferno Blaze", "§6Inferno Blaze", BarColor.YELLOW, BarStyle.SEGMENTED_6, 150, 150);
        this.blaze = (Blaze) getBossEntity();
        blaze.setAI(true);
        List<ItemStack> drops = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            if(Math.random() < 0.5) {
                drops.add(DraconicItem.instantiateTrueItem("InfernoPowder").getCurrentStack());
            }
        }
        setDrops(drops);
    }

    @Override
    public void tickSync() {
        refreshBossBar();
        List<Player> nearbyPlayers = getNearbyPlayers(30, 5, 30);
        for(Player player : new ArrayList<Player>(getBossBar().getPlayers())) {
            if(!nearbyPlayers.contains(player)) {
                getBossBar().removePlayer(player);
            }
        }
        for(Player player : nearbyPlayers) {
            getBossBar().addPlayer(player);
        }
        if(LocationUtil.getDistance(blaze) < 6) {
            blaze.teleport(blaze.getLocation().add(0, 0.1, 0));
        }
        if(blaze.getTarget() != null) {
            LocationUtil.faceLocation(blaze, blaze.getTarget().getLocation());
            if(Math.random() < 0.2) {
                blaze.getWorld().playSound(blaze.getLocation(), XSound.ENTITY_BLAZE_SHOOT.parseSound(), 100f, (float) (1f + Math.random()));
                SmallFireball fireBall = (SmallFireball) blaze.getTarget().getWorld().spawnEntity(blaze.getLocation().add((getLocation().getDirection().multiply(1))).add(0, 1, 0), EntityType.SMALL_FIREBALL);
                fireBall.setVelocity(getLocation().getDirection().multiply(1).add(new Vector((Math.random() - Math.random()) / 5, (Math.random() - Math.random()) / 5, (Math.random() - Math.random()) / 5)).add(blaze.getTarget().getVelocity()));
                fireBall.setTicksLived(200);
            }
        }
        }

    @Override
    public void tickAsync() {

    }

    @Override
    public void bossSpawned(Location spawnLocation, DraconicBossEntity bossEntity) {

    }

    @Override
    public void bossDamaged(EntityDamageByEntityEvent event, Entity damager, DraconicBossEntity boss) {

    }

    @Override
    public void bossKilled(EntityDeathEvent event, Player killer, DraconicBossEntity boss) {
        event.getDrops().clear();
        event.getDrops().addAll(getDrops());
        remove();
    }

    @Override
    public void bossAttack(EntityDamageByEntityEvent event, Entity damaged, double damage, DraconicBossEntity bossEntity) {

    }
}
