package de.philipp.advancedevolution.entities.bosses.supreme;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.animations.Delay;
import de.philipp.advancedevolution.entities.DraconicBossEntity;
import de.philipp.advancedevolution.entities.DraconicEntity;
import de.philipp.advancedevolution.entities.bosses.BossArena;
import de.philipp.advancedevolution.lib.xseries.XSound;
import de.philipp.advancedevolution.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SupremeLeader extends DraconicBossEntity {

    private List<DraconicEntity> subentities = new ArrayList<>();
    private IronGolem boss;
    private BossArena arena;
    private Random random = new Random();
    private Plugin handle;
    private Phase bossPhase;

    public SupremeLeader(Location location) {
        super(location, EntityType.IRON_GOLEM, AdvancedEvolution.getInstance(), "§cSupreme King", "§cSupreme King", BarColor.PURPLE, BarStyle.SEGMENTED_20, 2048, 2048);

        boss = (IronGolem) getBossEntity();
        handle = getPlugin();
        bossPhase = Phase.FIRST_PHASE;
        arena = new BossArena(location, 20, 5, 1, 0.01) {
            @Override
            public Particle arenaRing() {
                return Particle.DRAGON_BREATH;
            }

            @Override
            public void secondTick() {

            }

            @Override
            public void enter(Player player) {
                getBossBar().addPlayer(player);
            }

            @Override
            public void leave(Player player) {
                    if(!(player.getLocation().distance(center) > (diameter + 5))) {
                        if(!player.isDead()) {
                            player.setVelocity(player.getLocation().toVector().subtract(center.toVector()).multiply(-0.1));
                            player.getWorld().playSound(player.getLocation(), XSound.BLOCK_BEACON_POWER_SELECT.parseSound(), 5f, (float) (Math.random() + Math.random()));
                        }
                    } else {
                        new Delay(10) {
                            @Override
                            public void end() {
                                if (!arena.getPlayers().contains(player)) {
                                    player.resetPlayerWeather();
                                    getBossBar().removePlayer(player);
                                }
                            }
                        };
                    }
            }

            public void tick() {
                secondTick();
                // Grow and Shrink
                if(grow > 0) {
                    if(grow - speed <= 0) {
                        diameter += grow;
                        grow = 0;
                    } else {
                        diameter += speed;
                        grow -= speed;
                    }
                } else if(shrink > 0) {
                    if(shrink - speed < 0) {
                        diameter -= shrink;
                        shrink = 0;
                    } else {
                        diameter -= speed;
                        shrink -= speed;
                    }
                }
                //
                for(Entity entity : center.getWorld().getNearbyEntities(center, diameter, diameter, diameter, filter)) {
                    Player player = (Player) entity;
                    if(players.contains(player)) continue;
                    if(player.getLocation().distance(center) > diameter) continue;
                    if(player.isDead()) continue;
                    players.add(player);
                    enter(player);
                }
                for(Player player : center.getWorld().getPlayers()) {
                    if(!players.contains(player)) continue;
                    if(player.getLocation().distance(center) > diameter || player.isDead()) {
                        players.remove(player);
                        leave(player);
                    }
                }
                if(!(arenaRing() == null)) {
                    ParticleUtil.spawnParticleWall(center, arenaRing(), height, count, particleSpeed, diameter, 0.01);
                }
            }
        };
    }

    @Override
    public void tickSync() {
        refreshBossBar();
        arena.tick();
        if(!arena.getPlayers().isEmpty()) {
            boss.setTarget(arena.getPlayers().get(0));
        } else {
            boss.setTarget(null);
        }
        if(boss.getTarget() != null) {
            face(boss, boss.getTarget().getLocation());
        }
    }

    @Override
    public void tickAsync() {

    }

    @Override
    public void bossSpawned(Location spawnLocation, DraconicBossEntity bossEntity) {

    }

    @Override
    public void bossDamaged(EntityDamageByEntityEvent event, Entity damager, DraconicBossEntity bossE) {
        boss.setNoDamageTicks(1);
        getBossEntity().getWorld().playSound(getLocation(), XSound.ENTITY_PLAYER_HURT.parseSound(), 100,1);
        event.setDamage(event.getDamage() * 0.05);
    }

    @Override
    public void bossKilled(EntityDeathEvent event, Player killer, DraconicBossEntity boss) {
        remove();
    }

    @Override
    public void bossAttack(EntityDamageByEntityEvent event, Entity damaged, double damage, DraconicBossEntity bossEntity) {

    }
}
