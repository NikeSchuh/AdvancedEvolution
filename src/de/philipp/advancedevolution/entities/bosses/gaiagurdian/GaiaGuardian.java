package de.philipp.advancedevolution.entities.bosses.gaiagurdian;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.animations.Delay;
import de.philipp.advancedevolution.animations.LivingEntityAnimation;
import de.philipp.advancedevolution.entities.DraconicBossEntity;
import de.philipp.advancedevolution.entities.bosses.BossArena;
import de.philipp.advancedevolution.lib.xseries.XSound;
import de.philipp.advancedevolution.nms.SkyUtils;
import de.philipp.advancedevolution.util.LocationUtil;
import de.philipp.advancedevolution.util.ParticleUtil;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import javax.sound.midi.Sequence;
import javax.sound.midi.spi.MidiFileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;

public class GaiaGuardian extends DraconicBossEntity {

    private Evoker boss;
    private BossArena arena;
    private Random random = new Random();
    private Plugin handle;
    private Phase bossPhase;

    private long teleportDelay = 60;
    private long spawnDelay = 60;
    private long attackDelay = 30;

    private List<Entity> minions = new ArrayList<>();

    private boolean animation = false;

    public GaiaGuardian(Location location) {
        super(location, EntityType.EVOKER, AdvancedEvolution.getInstance(), "§5", "§5Gaia Guardian I", BarColor.GREEN, BarStyle.SEGMENTED_6, 320, 320);

        this.boss = (Evoker) getBossEntity();
        this.arena = new GaiaGuardianArena(boss.getEyeLocation());
        this.handle = AdvancedEvolution.getInstance();
        this.bossPhase = Phase.FIRST_PHASE;


        boss.setCustomName("Gaia Guardian I");
        boss.setAI(true);
        boss.setGravity(true);
        boss.setCanPickupItems(false);
        boss.getEquipment().setItemInOffHand(null);
        boss.getEquipment().setItemInMainHand(null);
        boss.setCustomNameVisible(false);

        getBossBar().addFlag(BarFlag.DARKEN_SKY);
        getBossBar().addFlag(BarFlag.CREATE_FOG);
        getBossBar().addFlag(BarFlag.PLAY_BOSS_MUSIC);
        getBossBar().setTitle(getBossBar().getTitle() + " §b" + arena.getPlayers().size());


    }

    public String players(List<Player> players) {
        StringBuilder strings = new StringBuilder();
        for(Player player : players) {
            strings.append(player.getName() + " ");
        }
        return strings.toString();
    }

    public void dump() {
        AdvancedEvolution.send("----Draconic Entity Debug----");
        AdvancedEvolution.send("Location: " + getLocation().getX() + " " + getLocation().getY() + " " + getLocation().getZ() + " " + getLocation().getYaw() + " " + getLocation().getPitch());
        AdvancedEvolution.send("World: " + boss.getWorld().getName());
        AdvancedEvolution.send("Entity Class: " + this.getClass().getName());
        AdvancedEvolution.send("Entity Type: " + boss.getType().name());
        AdvancedEvolution.send("Default Max Health: " + getDefaultMaxHealth());
        AdvancedEvolution.send("Default Health: " + getDefaultHealth());
        AdvancedEvolution.send("Health: " + boss.getHealth() + " / " + boss.getMaxHealth());
        AdvancedEvolution.send("Last Damage: " + boss.getLastDamageCause());
        AdvancedEvolution.send("Arena:");
        AdvancedEvolution.send("  Height: " + arena.height);
        AdvancedEvolution.send("  Diameter: " + arena.diameter);
        AdvancedEvolution.send("  Center: " + "x" + arena.center.getX() + " y" + arena.center.getY() + "z" + arena.center.getZ());
        AdvancedEvolution.send("  World: " + arena.center.getWorld());
        AdvancedEvolution.send("  Particle Type: " + arena.arenaRing().name());
        AdvancedEvolution.send("  Particle Count p/b: " + arena.count);
        AdvancedEvolution.send("  Particle Speed: " + arena.particleSpeed);
        AdvancedEvolution.send("  Player Count: " + arena.getPlayers().size());
        AdvancedEvolution.send("  Players: " + players(arena.getPlayers()));
        AdvancedEvolution.send("Nearby Players Count: " + getNearbyPlayers(10, 10, 10).size());
        AdvancedEvolution.send("Nearby Players: " + players(getNearbyPlayers(10, 10, 10)));
        if(getNearestPlayer(10, 10, 10) != null) {
            AdvancedEvolution.send("Nearest Player: " + getNearestPlayer(10, 10, 10).getName());
        }
        AdvancedEvolution.send("Attack Delay: " + attackDelay);
        AdvancedEvolution.send("Teleport Delay: " + teleportDelay);
        AdvancedEvolution.send("Spawn Delay: " + spawnDelay);
        AdvancedEvolution.send("Boss Phase: " + bossPhase.name());
        AdvancedEvolution.send("Expected Boss Phase: " + getExpectedPhase().name());
        AdvancedEvolution.send("Animation: " + animation);
        AdvancedEvolution.send("Minions: " + minions.size());
        AdvancedEvolution.send("Handle: " + handle.getName());
        AdvancedEvolution.send("Boss Bar:");
        AdvancedEvolution.send("  Title: " + getBossBar().getTitle());
        AdvancedEvolution.send("  Player Count: " + getBossBar().getPlayers().size());
        AdvancedEvolution.send("  Players: " + players(getBossBar().getPlayers()));
        AdvancedEvolution.send("  Progress: " + getBossBar().getProgress());
        AdvancedEvolution.send("  Style: " + getBossBar().getStyle().name());
        AdvancedEvolution.send("  Color: " + getBossBar().getColor().name());
    }

    @Override
    public void tickSync() {
        refreshBossBar();
        boss.setAware(false);
        boss.setFireTicks(0);
        boss.setVelocity(new Vector(0, boss.getVelocity().getY() ,0));
        if(boss.getTarget() != null) {
            face(boss, boss.getTarget().getLocation());
        }
        attackDelay--;
        if(attackDelay <= 0) {
            attackDelay = 15;
            for(Player player : arena.getPlayers()) {
                player.playSound(boss.getEyeLocation(), XSound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS.parseSound(), 1f, 2f);
                new GaiaGuardianAttack(AdvancedEvolution.getInstance(), boss.getEyeLocation(), player, this, 10);
            }
        }
        if(boss.getLocation().distance(arena.center) > arena.diameter) {
            randomTeleport();
        }
        if(arena.getPlayers().size() == 0) {
            dump();
            remove();
        }
        arena.tick();
        if(animation) {
            spawnDelay--;
            if(spawnDelay <= 0) {
                spawnDelay = 60;
                for (int i = 0; i < 8; i++) {
                    Location location = getLocation().add(random.nextInt(5 + 5) - 5, -1, random.nextInt(5 + 5) - 5);
                    if (Math.random() < 0.06) {
                        minions.add(getLocation().getWorld().spawnEntity(location, EntityType.WITHER_SKELETON));
                    } else if (Math.random() < 0.46) {
                        minions.add(getLocation().getWorld().spawnEntity(location, EntityType.ZOMBIE));
                    } else if (Math.random() < 0.44) {
                        minions.add(getLocation().getWorld().spawnEntity(location, EntityType.SKELETON));
                    } else if (Math.random() < 0.04) {
                        minions.add(getLocation().getWorld().spawnEntity(location, EntityType.WITCH));
                    }
                }
            }
        } else {
            teleportDelay--;
            boss.getWorld().spawnParticle(Particle.ASH, boss.getEyeLocation(), 3, 0.5, 1, 0.5, 0.01);
            if(teleportDelay <= 0) {
                randomTeleport();
                teleportDelay = 60;
            }
        }
    }

    public Phase getExpectedPhase() {
        double currentHealth = boss.getHealth();
        if(currentHealth > getDefaultHealth() * 0.80) {
            return Phase.FIRST_PHASE;
        } else if(currentHealth > getDefaultHealth() * 0.50) {
            return Phase.SECOND_PHASE;
        } else if(currentHealth > getDefaultHealth() * 0.25) {
            return Phase.THIRD_PHASE;
        } else if(currentHealth > getDefaultHealth() * 0) {
            return Phase.FINAL_PHASE;
        }
        return null;
    }

    public Phase getBossPhase() {
        return bossPhase;
    }

    @Override
    public void tickAsync() {

    }

    @Override
    public void bossSpawned(Location spawnLocation, DraconicBossEntity bossEntity) {

    }

    public EntityDamageEvent.DamageCause test() {
        return EntityDamageEvent.DamageCause.VOID;
    }

    @Override
    public void bossDamaged(EntityDamageByEntityEvent event, Entity damager, DraconicBossEntity boss) {
        if(animation) {
            event.setCancelled(true);
            return;
        }
        randomTeleport();
        if(event.getDamage() > 25) {
            event.setDamage(25);
        }
        teleportDelay = 60;
        Phase expected = getExpectedPhase();
        if(expected != bossPhase) {
            if(expected.isHigher(bossPhase)) {
                new FlyAnimation(this, 300);
                bossPhase = expected;
            }
        }

    }

    private Location random() {
        Location location = getLocation().clone().add(random.nextInt(9 + 9) - 9, 0, random.nextInt(9 + 9) - 9);
        if(!(location.distance(arena.center) > arena.diameter)) {
            location.setY(arena.center.getY());
             return location;
        } else return random();
    }

    public void randomTeleport() {
        Location location = random();
        if(!(location.distance(arena.center) > arena.diameter)) {
            boss.getWorld().spawnParticle(Particle.CRIMSON_SPORE, boss.getLocation(), 50, 0, 1 ,0, 0.2);
            boss.teleport(location);
            boss.getWorld().playSound(boss.getLocation(), XSound.ENTITY_ENDERMAN_TELEPORT.parseSound(), 2f, 1.5f);
        }
    }

    @Override
    public void bossKilled(EntityDeathEvent event, Player killer, DraconicBossEntity boss) {
        for(Entity entity : minions) {
            entity.remove();
        }
        remove();
    }

    @Override
    public void bossAttack(EntityDamageByEntityEvent event, Entity damaged, double damage, DraconicBossEntity bossEntity) {

    }

    public Plugin getHandle() {
        return handle;
    }

    class GaiaGuardianArena extends BossArena {

        private List<UUID> whiteList;

        public GaiaGuardianArena(Location center) {
            super(center, 10, 1, 1, 0.02);
            List<UUID> ids = new ArrayList<>();
            getPlayers().forEach(player -> {
                ids.add(player.getUniqueId());
            });
            this.whiteList = ids;
        }

        @Override
        public Particle arenaRing() {
            return Particle.DRAGON_BREATH;
        }

        @Override
        public void secondTick() {
            for(Player player : getPlayers()) {
                player.spawnParticle(Particle.WHITE_ASH, center, 20, diameter, height, diameter, 0.01);
            }
        }

        @Override
        public void enter(Player player) {
            if(whiteList != null) {
                if (!whiteList.contains(player.getUniqueId())) {
                    Location out = center.clone().add(player.getLocation().toVector().subtract(center.toVector()).multiply(1.1));
                    out.setYaw(player.getLocation().getYaw());
                    out.setPitch(player.getLocation().getPitch());
                    out.setY(player.getLocation().getY());
                    player.teleport(out);
                    player.setVelocity(player.getLocation().toVector().subtract(center.toVector()).multiply(0.2));
                    player.playSound(player.getLocation(), XSound.ENTITY_ENDER_DRAGON_HURT.parseSound(), 5f, 0.2f);
                    player.sendMessage("§cYou cannot join the fight if you weren't in the arena at start.");
                    return;
                }
            }
            getBossBar().addPlayer(player);
            player.setPlayerWeather(WeatherType.CLEAR);
        }

        @Override
        public void leave(Player player) {
            if(whiteList != null) {
                if (!whiteList.contains(player.getUniqueId())) {
                    return;
                }
                if(!(player.getLocation().distance(center) > (diameter + 5))) {
                    if(!player.isDead()) {
                        if(whiteList.contains(player.getUniqueId())) {
                            players.add(player);
                        }
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

        }
    }

    class FlyAnimation extends LivingEntityAnimation {

        private GaiaGuardian guardian;
        private long duration;

        public FlyAnimation(GaiaGuardian gaiaGuardian, long duration) {
            super(gaiaGuardian.getHandle(), gaiaGuardian.boss);
            this.duration = duration;
            this.guardian = gaiaGuardian;
            guardian.animation = true;
            guardian.getBossEntity().setInvisible(true);
        }

        @Override
        public void onAnimationTick(LivingEntity entity) {
            if(duration <= 0) {
                end();
                cancel();
                return;
            }
            double ground = LocationUtil.getDistance(entity);
            entity.getWorld().spawnParticle(Particle.SMOKE_LARGE, guardian.getLocation(), 4, 0.5, 1, 0.5, 0.01);
            if(ground < 5) {
                entity.teleport(entity.getLocation().add(0, 0.1, 0));
            }
            duration--;
        }

        public void end() {
            guardian.animation = false;
            guardian.getBossEntity().setInvisible(false);
            guardian.getBossEntity().teleport(guardian.arena.center);
            guardian.randomTeleport();
        }
    }



}
