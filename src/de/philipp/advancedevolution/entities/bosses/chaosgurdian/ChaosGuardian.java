package de.philipp.advancedevolution.entities.bosses.chaosgurdian;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.entities.DraconicBossEntity;
import de.philipp.advancedevolution.lib.xseries.XSound;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class ChaosGuardian extends DraconicBossEntity {

    private EnderDragon entity;
    private int currentRotation = 0;

    public ChaosGuardian(Location location) {
        super(location, EntityType.ENDER_DRAGON, AdvancedEvolution.getInstance(), "§6Chaos Guardian", "§6Chaos Guardian", BarColor.RED, BarStyle.SEGMENTED_10, 2000, 2000);
        entity = (EnderDragon) getBossEntity();
        entity.setAI(false);
        entity.setRotation(currentRotation, 0);
    }

    @Override
    public void tickSync() {
        currentRotation++;
        if(Math.random() < 0.1) {
            getLocation().getWorld().playSound(getLocation(), XSound.ENTITY_ENDER_DRAGON_SHOOT.parseSound(), 1f, 1f);
            for(Entity entity : getBossEntity().getNearbyEntities(100, 100, 100)) {
                if(entity instanceof Player) {
                    LivingEntity livingEntity = (LivingEntity) entity;

                    new ChaosGuardianAttack1(this, livingEntity, new Vector((7 * Math.random()) + 2, (7 * Math.random()) + 2, (7 * Math.random()) + 2));
                }
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

    }

    @Override
    public void bossAttack(EntityDamageByEntityEvent event, Entity damaged, double damage, DraconicBossEntity bossEntity) {

    }
}
