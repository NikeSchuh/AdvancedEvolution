package de.philipp.advancedevolution.entities.bosses.wyvernguardian;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.animations.animations.WyvernGuardianDeath;
import de.philipp.advancedevolution.entities.DraconicBossEntity;
import de.philipp.advancedevolution.items.DraconicItem;
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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WyvernGuardian extends DraconicBossEntity {

    private Random random = new Random();

    private Player mainTarget;
    private Phase currentPhase;
    private boolean attacking = false;
    private Vector speed = new Vector(0.5, 0.5, 0.5);
    private List<Monster> spawnedEntities = new ArrayList<>();
    private boolean dead = false;

    public WyvernGuardian(Location location) {
        super(location, EntityType.ELDER_GUARDIAN, AdvancedEvolution.getInstance(), "§5Wyvern Guardian", "§5Wyvern Guardian", BarColor.PURPLE, BarStyle.SEGMENTED_6, 500, 500);
        currentPhase = Phase.FIRST_PHASE;
        setMainTarget(getNearestPlayer(100, 50, 100));
        getBossEntity().setSilent(true);
    }

    public WyvernGuardian(LivingEntity entity) {
        super(entity, AdvancedEvolution.getInstance(), "§5Wyvern Guardian", BarColor.PURPLE, BarStyle.SEGMENTED_6);
        currentPhase = Phase.FIRST_PHASE;
        setMainTarget(getNearestPlayer(100, 50, 100));
        getBossEntity().setSilent(true);
    }

    public Player getTarget() {
        return mainTarget;
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(Phase currentPhase) {
        this.currentPhase = currentPhase;
    }

    public void setMainTarget(Player mainTarget) {
        this.mainTarget = mainTarget;
    }

    public void attack() {
        if(attacking) return;
        if(dead) return;
        switch (currentPhase) {
            case FIRST_PHASE:
                new WyvernGuardianPurpleAttack(AdvancedEvolution.getInstance(), getLocation().add(0, 1, 0), mainTarget, new Vector(30, 30, 30), this, random.nextInt(20) + 10);
                getLocation().getWorld().playSound(getLocation(), XSound.ENTITY_ILLUSIONER_CAST_SPELL.parseSound(), 100f, 1.5f);
                break;
            case SECOND_PHASE:
                if (Math.random() < 0.5) {
                    getLocation().getWorld().playSound(getLocation(), XSound.ENTITY_ILLUSIONER_CAST_SPELL.parseSound(), 100f, 0.5f);
                    new WyvernGuardianGreenAttack(AdvancedEvolution.getInstance(), getLocation().add(0, 1, 0), mainTarget, new Vector(4, 4, 4), this, random.nextInt(30) + 20);
                } else {
                    ShulkerBullet shulkerBullet = (ShulkerBullet) getLocation().getWorld().spawnEntity(getLocation().add(0, -2, 0), EntityType.SHULKER_BULLET);
                    shulkerBullet.setTarget(mainTarget);
                }
                break;
            case THIRD_PHASE:
                if(Math.random() < 0.7) {
                    new WyvernGuardianHeavyAttack(AdvancedEvolution.getInstance(), getLocation().add(0, 1, 0), mainTarget, new Vector(10, 10, 10), this, random.nextInt(20) + 25);
                    getBossEntity().setHealth(getBossEntity().getHealth() + 10);
                    getBossEntity().getLocation().getWorld().spawnParticle(Particle.CRIMSON_SPORE, getLocation().add(0, 1, 0), 20, 0, 0, 0.2);
                    getLocation().getWorld().playSound(getLocation(), XSound.ENTITY_EVOKER_PREPARE_SUMMON.parseSound(), 100f, 2f);
                } else {
                    getLocation().getWorld().playSound(getLocation(), XSound.ENTITY_EVOKER_PREPARE_SUMMON.parseSound(), 100f, 0.5f);
                    for(Monster monster : spawnedEntities) {
                        if(!monster.isDead()) {
                            monster.setTarget(mainTarget);
                            monster.getWorld().spawnParticle(Particle.WARPED_SPORE, monster.getEyeLocation().add(0, 1, 0), 5, 0, 0, 0, 0);
                        }
                    }
                    for(int i = 0; i < random.nextInt(5) + 10; i++) {
                        if(Math.random() < 0.5) {
                            spawnedEntities.add((Monster) getLocation().getWorld().spawnEntity(getLocation(), EntityType.ZOMBIE));
                        } else {
                            Vex vex = (Vex) getLocation().getWorld().spawnEntity(getLocation(), EntityType.VEX);
                            vex.setTarget(mainTarget);
                            vex.setCharging(true);
                            spawnedEntities.add(vex);
                        }
                    }
                }
                break;
            case FINAL_PHASE:
                getLocation().getWorld().playSound(getLocation(), XSound.ENTITY_EVOKER_PREPARE_ATTACK.parseSound(), 100f, 0.1f);
                for(Player player : getNearbyPlayers(18, 14, 18)) {
                    new WyvernGuardianHeavyAttack(AdvancedEvolution.getInstance(), getLocation().add(0, 1, 0), player, new Vector((7 * Math.random()) + 2, (7 * Math.random()) + 2, (7 * Math.random()) + 2), this, random.nextInt(20) + 20);
                    new WyvernGuardianGreenAttack(AdvancedEvolution.getInstance(), getLocation().add(0, 1, 0), player, new Vector((7 * Math.random()) + 2, (7 * Math.random()) + 2, (7 * Math.random()) + 2), this, random.nextInt(20) + 20);
                }
                if(getNearbyPlayers(18, 14, 18).size() > 30) break;
                for(int i = 0; i < 4; i++) {
                    Location location = getLocation().add(random.nextInt(10 + 10) - 10, -7, random.nextInt(10 + 10) - 10);
                    if(Math.random() < 0.05) {
                        spawnedEntities.add((Monster) getLocation().getWorld().spawnEntity(location, EntityType.WITHER_SKELETON));
                    }
                    if(Math.random() < 0.3) {
                        spawnedEntities.add((Monster)getLocation().getWorld().spawnEntity(location, EntityType.ZOMBIE));
                    }
                    if(Math.random() < 0.3) {
                        spawnedEntities.add((Monster) getLocation().getWorld().spawnEntity(location, EntityType.SKELETON));
                    }
                    if(Math.random() < 0.2) {
                        spawnedEntities.add((Monster) getLocation().getWorld().spawnEntity(location, EntityType.VEX));
                    }

                }
                break;
        }

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
        refreshBossBar();
        for(Player player : getNearbyPlayers(30, 5, 30)) {
            if(!(getBossBar().getPlayers().contains(player))) {
                getBossBar().addPlayer(player);
            }
        }
        if(Math.random() < 0.01) {
            getLocation().getWorld().playSound(getLocation(), XSound.ENTITY_ELDER_GUARDIAN_AMBIENT.parseSound(), 10f, 0.1f);
        }
        LocationUtil.faceLocation(getBossEntity(), mainTarget.getLocation());
        if(currentPhase == Phase.FIRST_PHASE) {
            if(Math.random() < 0.05) {
                attack();
            }
            if (getLocation().distance(mainTarget.getLocation()) > 5) {
                Location loc2 = mainTarget.getLocation().add(0, 2, 0);
                Location loc1 = getLocation();
                final double distance = loc1.distance(loc2);
                Vector v = new Vector(
                        ((loc2.getX() - loc1.getX()) / (distance)) * speed.getX(),
                        ((loc2.getY() - loc1.getY()) / (distance)) * speed.getY(),
                        ((loc2.getZ() - loc1.getZ()) / (distance)) * speed.getZ());
                getBossEntity().teleport(getLocation().add(v.getX(), v.getY(), v.getZ()));
            }
        } else if(currentPhase == Phase.SECOND_PHASE) {
            if(Math.random() < 0.05) {
                attack();
            }
            if (getLocation().distance(mainTarget.getLocation()) > 15) {
                Location loc2 = mainTarget.getLocation().add(0, 4, 0);
                Location loc1 = getLocation();
                final double distance = loc1.distance(loc2);
                Vector v = new Vector(
                        ((loc2.getX() - loc1.getX()) / (distance)) * speed.getX(),
                        ((loc2.getY() - loc1.getY()) / (distance)) * speed.getY(),
                        ((loc2.getZ() - loc1.getZ()) / (distance)) * speed.getZ());
                getBossEntity().teleport(getLocation().add(v.getX(), v.getY(), v.getZ()));
            } else {
                Location loc1 = mainTarget.getLocation();
                Location loc2 = getLocation().add(0, 4, 0);
                final double distance = loc1.distance(loc2);
                Vector v = new Vector(
                        ((loc2.getX() - loc1.getX()) / (distance)) * speed.getX(),
                        ((loc2.getY() - loc1.getY()) / (distance)) * speed.getY(),
                        ((loc2.getZ() - loc1.getZ()) / (distance)) * speed.getZ());
                getBossEntity().teleport(getLocation().add(v.getX(), v.getY(), v.getZ()));
            }
        } else if(currentPhase == Phase.THIRD_PHASE) {
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
        } else if(currentPhase == Phase.FINAL_PHASE) {
            if(Math.random() < 0.02) {
                attack();
            }
            refreshBossBar();
            getLocation().getWorld().spawnParticle(Particle.FALLING_LAVA, getLocation().add(0, 3, 0), 5, 20, -5, 20, 0.1);
            if(LocationUtil.getDistance(getBossEntity()) > 12) {
                getBossEntity().teleport(getLocation().add(0,-0.5 , 0));
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
            player.playSound(getLocation(), XSound.ENTITY_ELDER_GUARDIAN_CURSE.parseSound(), 100f, 0.01f);
        }
        mainTarget = getNearestPlayer(30, 10, 30);
    }

    @Override
    public void bossDamaged(EntityDamageByEntityEvent event, Entity damager, DraconicBossEntity boss) {
        if(event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {event.setCancelled(true);}
        if(currentPhase == Phase.FINAL_PHASE) {
            event.setDamage(event.getDamage() * 0.3);
        }
        boss.getBossEntity().getWorld().playSound(boss.getLocation(), XSound.ENTITY_ENDER_DRAGON_HURT.parseSound(), 10, 0.1f);
        boss.getLocation().getWorld().playSound(boss.getLocation(), XSound.ENTITY_ELDER_GUARDIAN_HURT.parseSound(), 5f, (float) Math.random());
        if(event.getFinalDamage() >= getBossEntity().getHealth()) {
            dead = true;
            event.setCancelled(true);
            getBossEntity().setHealth(0.01);
            getBossEntity().setInvulnerable(true);
            getBossEntity().setArrowsInBody(0);
            refreshBossBar();
            new WyvernGuardianDeath(AdvancedEvolution.getInstance(), this) {

                @Override
                public void end() {
                    getBossEntity().setHealth(0);
                }
            };
        }
    }

    @Override
    public void bossKilled(EntityDeathEvent event, Player killer, DraconicBossEntity boss) {
        List<ItemStack> drops = new ArrayList<>();
        for(Monster monster : spawnedEntities) {
            monster.remove();
        }
        for(int i = 0; i < 16; i++) {
            if(Math.random() < 0.5) {
                drops.add(DraconicItem.instantiateItem("DraconiumDust").getCurrentStack());
            }
        }
        Item item = boss.getBossEntity().getWorld().dropItem(boss.getLocation(), DraconicItem.instantiateItem("WyvernGuardianEye").getCurrentStack());
        item.setGravity(false);
        item.setGlowing(true);
        item.setVelocity(new Vector(0,0,0));
        event.getDrops().clear();
        event.getDrops().addAll(drops);
        remove();
    }

    @Override
    public void bossAttack(EntityDamageByEntityEvent event, Entity damaged, double damage, DraconicBossEntity bossEntity) {

    }

    public void bossAttack(WyvernGuardianPurpleAttack attack, Player target, Location location) {
        location.getWorld().playSound(location, XSound.ENTITY_ILLUSIONER_CAST_SPELL.parseSound(), 100f, 1.5f);
    }



}
