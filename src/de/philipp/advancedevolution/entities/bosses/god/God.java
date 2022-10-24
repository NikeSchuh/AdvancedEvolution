package de.philipp.advancedevolution.entities.bosses.god;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.entities.DraconicBossEntity;
import de.philipp.advancedevolution.entities.bosses.wyvernguardian.WyvernGuardian;
import de.philipp.advancedevolution.entities.bosses.wyvernguardian.WyvernGuardianGreenAttack;
import de.philipp.advancedevolution.entities.bosses.wyvernguardian.WyvernGuardianHeavyAttack;
import de.philipp.advancedevolution.lib.xseries.XSound;
import de.philipp.advancedevolution.util.LocationUtil;
import de.philipp.advancedevolution.util.ParticleUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

public class God extends DraconicBossEntity {

    private Evoker boss;
    private Player mainTarget;
    private boolean dead = false;
    private Phase currentPhase;
    private Vector speed = new Vector(1, 1, 1);
    private Random random = new Random();

    public God(Location location) {
        super(location, EntityType.EVOKER, AdvancedEvolution.getInstance(), "§4§kLeroin The Creator", "§4§kLeroin The Creator", BarColor.RED, BarStyle.SEGMENTED_20, 2048, 2048);
        this.boss = (Evoker) getBossEntity();
        this.currentPhase = Phase.FIRST_PHASE;
        setMainTarget(getNearestPlayer(50, 20, 50));
    }

    public void setMainTarget(Player mainTarget) {
        this.mainTarget = mainTarget;
    }

    public void attack() {
        List<Player> list = getNearbyPlayers(18, 14, 18);
        for(Player player : list) {
            new GodAttackNormal(boss.getEyeLocation(), player, new Vector(Math.random() * 0.1, Math.random() * 0.1, Math.random() * 0.1), this, 200);
        }
       // for(int i = 0; i < 20; i++) {
       //     for (Player player : list) {
        //        new WyvernGuardianHeavyAttack(AdvancedEvolution.getInstance(), getLocation().add(0, 1, 0), player, new Vector((20 * Math.random()) + 20, (20 * Math.random()) + 20, (20 * Math.random()) + 20), this, 200);
         //       new WyvernGuardianGreenAttack(AdvancedEvolution.getInstance(), getLocation().add(0, 1, 0), player, new Vector((20 * Math.random()) + 20, (20 * Math.random()) + 20, (20 * Math.random()) + 20), this, 200);
         //   }
       // }
    }

    @Override
    public void tickSync() {
        if(dead) return;
        if(mainTarget == null) {
            remove();
            return;
        }
        if(!mainTarget.isOnline()) {
            if(getNearbyPlayers(30, 10, 30).size() == 0) {
                remove();
                return;
            }
        }
        if(mainTarget.getLocation().distance(getLocation()) > 100) {
            mainTarget = getNearestPlayer(50, 30, 50);
            if(mainTarget == null) {
                remove();
                return;
            }
        }
        for(Player player : getNearbyPlayers(30, 5, 30)) {
            if(!(getBossBar().getPlayers().contains(player))) {
                getBossBar().addPlayer(player);
            }
        }
        boss.setAI(true);
        boss.setGravity(false);
        boss.setSpell(Spellcaster.Spell.FANGS);
        LocationUtil.faceLocationGod(getBossEntity(), mainTarget.getLocation());
        if(currentPhase == WyvernGuardian.Phase.FIRST_PHASE) {
            if(Math.random() < 1) {
                attack();
            }
            if (getLocation().distance(mainTarget.getLocation()) > 8) {
                Location loc2 = mainTarget.getLocation().add(0, 0, 0);
                Location loc1 = getLocation();
                final double distance = loc1.distance(loc2);
                Vector v = new Vector(
                        ((loc2.getX() - loc1.getX()) / (distance)) * speed.getX(),
                        ((loc2.getY() - loc1.getY()) / (distance)) * speed.getY(),
                        ((loc2.getZ() - loc1.getZ()) / (distance)) * speed.getZ());
                getBossEntity().teleport(getLocation().add(v.getX(), v.getY(), v.getZ()));
            } else if (getLocation().distance(mainTarget.getLocation()) < 7) {
                Location loc1 = mainTarget.getLocation();
                Location loc2 = getLocation().add(0, 0, 0);
                final double distance = loc1.distance(loc2);
                Vector v = new Vector(
                        ((loc2.getX() - loc1.getX()) / (distance)) * speed.getX(),
                        ((loc2.getY() - loc1.getY()) / (distance)) * speed.getY(),
                        ((loc2.getZ() - loc1.getZ()) / (distance)) * speed.getZ());
                getBossEntity().teleport(getLocation().add(v.getX(), v.getY(), v.getZ()));
            }
        } else if(currentPhase == WyvernGuardian.Phase.SECOND_PHASE) {
            if(Math.random() < 0.05) {
                attack();
            }
            if (getLocation().distance(mainTarget.getLocation()) > 15) {
                Location loc2 = mainTarget.getLocation().add(0, 0, 0);
                Location loc1 = getLocation();
                final double distance = loc1.distance(loc2);
                Vector v = new Vector(
                        ((loc2.getX() - loc1.getX()) / (distance)) * speed.getX(),
                        ((loc2.getY() - loc1.getY()) / (distance)) * speed.getY(),
                        ((loc2.getZ() - loc1.getZ()) / (distance)) * speed.getZ());
                getBossEntity().teleport(getLocation().add(v.getX(), v.getY(), v.getZ()));
            } else {
                Location loc1 = mainTarget.getLocation();
                Location loc2 = getLocation().add(0, 0, 0);
                final double distance = loc1.distance(loc2);
                Vector v = new Vector(
                        ((loc2.getX() - loc1.getX()) / (distance)) * speed.getX(),
                        ((loc2.getY() - loc1.getY()) / (distance)) * speed.getY(),
                        ((loc2.getZ() - loc1.getZ()) / (distance)) * speed.getZ());
                getBossEntity().teleport(getLocation().add(v.getX(), v.getY(), v.getZ()));
            }
        } else if(currentPhase == WyvernGuardian.Phase.THIRD_PHASE) {
            if(Math.random() < 0.01) {
                attack();
            }
            getLocation().getWorld().spawnParticle(Particle.CRIMSON_SPORE, getLocation(), 50, 20, -5, 20, 0.1);
            if (getLocation().distance(mainTarget.getLocation()) > 7) {
                Location loc2 = mainTarget.getLocation().add(0, 1, 0);
                Location loc1 = getLocation();
                final double distance = loc1.distance(loc2);
                Vector v = new Vector(
                        ((loc2.getX() - loc1.getX()) / (distance)) * speed.getX(),
                        ((loc2.getY() - loc1.getY()) / (distance)) * speed.getY(),
                        ((loc2.getZ() - loc1.getZ()) / (distance)) * speed.getZ());
                getBossEntity().teleport(getLocation().add(v.getX(), v.getY(), v.getZ()));
            } else {
                Location loc1 = mainTarget.getLocation();
                Location loc2 = getLocation().add(0, 1, 0);
                final double distance = loc1.distance(loc2);
                Vector v = new Vector(
                        ((loc2.getX() - loc1.getX()) / (distance)) * speed.getX(),
                        ((loc2.getY() - loc1.getY()) / (distance)) * speed.getY(),
                        ((loc2.getZ() - loc1.getZ()) / (distance)) * speed.getZ());
                getBossEntity().teleport(getLocation().add(v.getX(), v.getY(), v.getZ()));
            }
        } else if(currentPhase == WyvernGuardian.Phase.FINAL_PHASE) {
            if(Math.random() < 0.02) {
                attack();
            }
            getLocation().getWorld().spawnParticle(Particle.FALLING_LAVA, getLocation().add(0, 3, 0), 5, 20, -5, 20, 0.1);
            if(LocationUtil.getDistance(getBossEntity()) > 12) {
                getBossEntity().teleport(getLocation().add(0,-LocationUtil.getDistance(getBossEntity()) / 8 , 0));
            } else if(LocationUtil.getDistance(getBossEntity()) < 11) {
                getBossEntity().teleport(getLocation().add(0, 0.1, 0));
            } else {
                ParticleUtil.spawnParticle(getLocation().clone().add(0, -9, 0), Particle.DRAGON_BREATH, 1, 0.01, 16);
                for(Player player : getNearbyPlayers(20, 20, 20)) {
                    if(player.getLocation().distance(getLocation()) > 18.5) {
                        player.setVelocity(getLocation().getDirection().multiply(-1));
                        player.playSound(player.getLocation(), XSound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS.parseSound(), 100f, 0.5f);
                    }
                }


            }
        }


    }

    @Override
    public void tickAsync() {
        if(dead) return;
        getBossBar().setColor(BarColor.values()[random.nextInt(BarColor.values().length)]);
        refreshBossBar();
        if(getBossEntity().getHealth() + 0.02 <= getBossEntity().getMaxHealth()) {
            getBossEntity().setHealth(getBossEntity().getHealth() + 0.02);
        }
        double currentHealth = getBossEntity().getHealth();
        if(currentHealth > getDefaultHealth() * 0.80) {
            currentPhase = Phase.FIRST_PHASE;
        } else if(currentHealth > getDefaultHealth() * 0.50) {
            currentPhase = Phase.SECOND_PHASE;
        } else if(currentHealth > getDefaultHealth() * 0.25) {
            currentPhase = Phase.THIRD_PHASE;
        } else if(currentHealth > getDefaultHealth() * 0) {
            currentPhase = Phase.FINAL_PHASE;
        }
    }

    @Override
    public void bossSpawned(Location spawnLocation, DraconicBossEntity bossEntity) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getEyeLocation(), XSound.ENTITY_ENDER_DRAGON_DEATH.parseSound(), 100f, 0.1f);
        }
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
