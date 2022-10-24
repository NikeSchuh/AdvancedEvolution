package de.philipp.advancedevolution.entities.bosses.piglinlord;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.animations.Delay;
import de.philipp.advancedevolution.entities.DraconicBossEntity;
import de.philipp.advancedevolution.entities.particles.Hitbox;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.lib.xseries.XSound;
import de.philipp.advancedevolution.util.LocationUtil;
import de.philipp.advancedevolution.util.ParticleUtil;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.Random;

public class PiglinLord extends DraconicBossEntity {

    private boolean axeThrown = false;
    private boolean dead = false;
    private boolean eating = false;
    private boolean stamping = false;
    private Player mainTarget = null;
    private Phase currentPhase;
    private Random random = new Random();
    private PiglinBrute piglin;

    public PiglinLord(Location location) {
        super(location, EntityType.PIGLIN_BRUTE, AdvancedEvolution.getInstance(), "§6Piglin Lord", "§cPiglin Lord", BarColor.RED, BarStyle.SEGMENTED_10, 300, 300);
        currentPhase = Phase.FIRST_PHASE;
        setMainTarget(getNearestPlayer(100, 50, 100));
        piglin = (PiglinBrute) getBossEntity();
        piglin.setSilent(true);
        piglin.setAI(true);
        piglin.setGravity(true);
        piglin.setAdult();
        piglin.getEquipment().clear();
        piglin.getEquipment().setItemInMainHand(XMaterial.NETHERITE_AXE.parseItem());
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

    public void setAxeThrown(boolean axeThrown) {
        this.axeThrown = axeThrown;
        if(!axeThrown) {
            piglin.getEquipment().setItemInMainHand(XMaterial.NETHERITE_AXE.parseItem());
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
        if(Math.random() < 0.5) {
            makeMove();
        }
        if(eating) {
            if(Math.random() < 0.25) {
                getLocation().getWorld().playSound(getLocation(), XSound.ENTITY_PLAYER_BURP.parseSound(), 3f, (float) Math.random());
            }
            getLocation().getWorld().spawnParticle(Particle.ITEM_CRACK, piglin.getEyeLocation().add(piglin.getLocation().getDirection().multiply(0.25)).add(0, -0.2, 0), 10, 0, 0, 0, 0.1, XMaterial.GOLDEN_APPLE.parseItem());
        }
        piglin.setTarget(mainTarget);
      //  LocationUtil.faceLocation(getBossEntity(), mainTarget.getLocation());
    }

    @Override
    public void tickAsync() {
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
        bossEntity.getBossEntity().setAI(false);
    }

    public void throwAxe() {
        if (!axeThrown) {
            piglin.swingMainHand();
            piglin.getEquipment().setItemInMainHand(null);
            axeThrown = true;
            getLocation().getWorld().playSound(getLocation(), XSound.ITEM_FIRECHARGE_USE.parseSound(), 2f, 1);
            getLocation().getWorld().playSound(getLocation(), XSound.ENTITY_EVOKER_PREPARE_ATTACK.parseSound(), 10f, 2f);
            new PiglinThrowAxe(this, getLocation(), getTarget(), new Vector(30, 15, 30), new Hitbox(1, 1, 1)) {
                @Override
                public void onHit(Entity target) {
                    target.getLocation().getWorld().createExplosion(getLocation(), 2f);
                    armorStand.remove();
                    piglinLord.setAxeThrown(false);
                    target.getLocation().getWorld().strikeLightning(target.getLocation());
                    player.damage(15, piglinLord.getBossEntity());
                    player.playSound(player.getLocation(), XSound.ITEM_SHIELD_BREAK.parseSound(), 2f, 1.25f);
                    if (target.getLocation().distance(piglinLord.getLocation()) > 30) {
                        piglinLord.getBossEntity().teleport(player.getLocation().add(-1 + random.nextInt(2), 0.1, -1 + random.nextInt(2)));
                        piglinLord.getLocation().getWorld().playSound(piglinLord.getLocation(), XSound.ENTITY_ENDERMAN_TELEPORT.parseSound(), 0.5F, 1.5f);
                    }
                    remove();
                }
            };
        }
    }

    public void makeMove() {
        if(eating) return;
        if(stamping) return;
        if(Math.random() < 0.25) {
            if(!axeThrown && !eating) {
                if (getLocation().distance(mainTarget.getLocation()) < 5) {
                    piglin.swingMainHand();
                    getLocation().getWorld().playSound(getLocation(), XSound.ENTITY_PIGLIN_ANGRY.parseSound(), 0.5f, (float) (1f + (Math.random() - Math.random())));
                    mainTarget.damage(20, piglin);
                }
            }
            return;
        }
        if(Math.random() < 0.15) {
            if(!axeThrown && !eating) {
                if (getLocation().distance(mainTarget.getLocation()) < 4) {
                    piglin.swingMainHand();
                    getLocation().getWorld().playSound(getLocation(), XSound.ENTITY_PIGLIN_CELEBRATE.parseSound(), 1f, 2f);
                    mainTarget.damage(40, piglin);
                    mainTarget.setVelocity(mainTarget.getLocation().toVector().subtract(getLocation().toVector()).multiply(5).setY(0.5));
                    new Delay(20) {
                        @Override
                        public void end() {
                            throwAxe();
                        }
                    };
                }
            }
            return;
        }
        if(Math.random() < 0.075) {
            if(mainTarget != null) {
                if(mainTarget.getLocation().distance(getLocation()) < 25) {
                    if(mainTarget.getLocation().distance(getLocation()) > 15) {
                        int ringCount = random.nextInt(20) + 1;
                        stamping = true;
                        piglin.setVelocity(mainTarget.getLocation().toVector().subtract(getLocation().toVector()).multiply(0.2));
                        piglin.setVelocity(new Vector(piglin.getVelocity().getX(), 1.5, piglin.getVelocity().getZ()));
                        new Delay(30) {
                            @Override
                            public void end() {
                                // piglin.setVelocity(new Vector(piglin.getVelocity().getX(), -3, piglin.getVelocity().getZ()));
                                new Delay(5) {
                                    @Override
                                    public void end() {
                                        for (int i = 0; i < ringCount; i++) {
                                            int finalI = i;
                                            new Delay(5 + (finalI * 3)) {
                                                @Override
                                                public void end() {
                                                    for (Location location : ParticleUtil.getWall(getLocation(), 1, (finalI * 2) + 2)) {
                                                        if (random.nextInt(100) > 10) continue;
                                                        location.getWorld().spawnEntity(location, EntityType.EVOKER_FANGS);
                                                    }
                                                }
                                            };
                                        }
                                        new Delay(100) {
                                            @Override
                                            public void end() {
                                                stamping = false;
                                            }
                                        };
                                    }
                                };
                            }
                        };
                        return;
                    }
                }
            }
        }
        if(Math.random() < (0.25 + (mainTarget.getLocation().distance(getLocation()) / 30))) {
            if(currentPhase.isHigher(Phase.SECOND_PHASE)) {
                throwAxe();
            }
            return;
        }
        if(Math.random() < 0.1) {
            if(!(piglin.getHealth() == piglin.getMaxHealth())) {
                eating = true;
                piglin.getEquipment().setItemInMainHand(XMaterial.GOLDEN_APPLE.parseItem());
                new Delay(40) {
                    @Override
                    public void end() {
                        if (eating = false) return;
                        eating = false;
                        piglin.getEquipment().setItemInMainHand(XMaterial.NETHERITE_AXE.parseItem());
                        if (piglin.getHealth() + 30 > piglin.getMaxHealth()) {
                            piglin.setHealth(getDefaultMaxHealth());
                        } else {
                            piglin.setHealth(piglin.getHealth() + 30);
                        }
                    }
                };
            }
            return;
        }
    }

    @Override
    public void bossDamaged(EntityDamageEvent event, DraconicBossEntity bossEntity) {
        if(event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) event.setCancelled(true);
        if(event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING) event.setCancelled(true);
        if(event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) event.setCancelled(true);
        if(event.getCause() == EntityDamageEvent.DamageCause.FALL) event.setCancelled(true);
    }

    @Override
    public void bossDamaged(EntityDamageByEntityEvent event, Entity damager, DraconicBossEntity boss) {

    }

    @Override
    public void bossKilled(EntityDeathEvent event, Player killer, DraconicBossEntity boss) {
        dead = true;
        remove();

    }

    @Override
    public void bossAttack(EntityDamageByEntityEvent event, Entity damaged, double damage, DraconicBossEntity bossEntity) {

    }
}
