package de.philipp.advancedevolution.entities;

import de.philipp.advancedevolution.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.List;

public abstract class DraconicBossEntity extends DraconicEntity{

    public static List<DraconicBossEntity> bossEntities = new ArrayList<>();

    private String displayName;
    private String bossBarName;
    private BarColor bossBarColor;
    private double defaultMaxHealth;
    private double defaultHealth;
    private BarStyle barStyle;
    private BossBar bossBar;
    private Plugin plugin;
    private BukkitTask updateTaskSync;
    private BukkitTask updateTaskAsync;
    private LivingEntity bossEntity;
    private List<ItemStack> drops;
    private BossEventHandler eventHandler;

    public DraconicBossEntity(Location location, EntityType type, Plugin plugin, String displayName, String bossBarName, BarColor bossBarColor, BarStyle style, double defaultMaxHealth, double defaultHealth) {
        super(location, type);
        this.displayName = displayName;
        this.bossBarName = bossBarName;
        this.bossBarColor = bossBarColor;
        this.defaultMaxHealth = defaultMaxHealth;
        this.defaultHealth = defaultHealth;
        this.barStyle = style;
        this.plugin = plugin;
        this.bossBar = Bukkit.createBossBar(bossBarName, bossBarColor, barStyle);
        this.drops = new ArrayList<>();

        if(!(getBukkitEntity() instanceof LivingEntity)) {
            throw new IllegalArgumentException("The entity must be a LivingEntity!");
        }

        this.bossEntity = (LivingEntity) getBukkitEntity();

        // Entity Attributes
        bossEntity.setMaxHealth(defaultMaxHealth);
        bossEntity.setHealth(defaultHealth);
        bossEntity.setCustomName(displayName);


        bossEntity.setAI(false);
        bossEntity.setCustomNameVisible(true);

        this.eventHandler = new BossEventHandler(plugin, this);

        updateTaskSync = createTaskSync();
        updateTaskAsync = createTaskAsync();

        bossSpawned(getSpawnLocation(), this);
        bossEntities.add(this);
    }

    public DraconicBossEntity(LivingEntity entity, Plugin plugin, String bossBarName, BarColor bossBarColor, BarStyle style) {
        super(entity);
        this.displayName = entity.getCustomName();
        this.bossBarName = bossBarName;
        this.bossBarColor = bossBarColor;
        this.defaultMaxHealth = entity.getMaxHealth();
        this.defaultHealth = entity.getHealth();
        this.barStyle = style;
        this.plugin = plugin;
        this.bossBar = Bukkit.createBossBar(bossBarName, bossBarColor, barStyle);
        this.drops = new ArrayList<>();
        this.bossEntity = entity;

        entity.setAI(false);
        entity.setCustomNameVisible(true);

        this.eventHandler = new BossEventHandler(plugin, this);

        updateTaskSync = createTaskSync();
        updateTaskAsync = createTaskAsync();

    }

    private BukkitTask createTaskSync() {
        return new BukkitRunnable() {

            @Override
            public void run() {
                tickSync();
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    private BukkitTask createTaskAsync() {
        return new BukkitRunnable() {

            @Override
            public void run() {
                tickAsync();
            }
        }.runTaskTimerAsynchronously(plugin, 0, 1);
    }


    public abstract void tickSync();
    public abstract void tickAsync();
    public abstract void bossSpawned(Location spawnLocation, DraconicBossEntity bossEntity);
    public abstract void bossDamaged(EntityDamageByEntityEvent event, Entity damager, DraconicBossEntity boss);
    public abstract void bossKilled(EntityDeathEvent event, Player killer, DraconicBossEntity boss);
    public abstract void bossAttack(EntityDamageByEntityEvent event, Entity damaged, double damage, DraconicBossEntity bossEntity);

    public void bossDamaged(EntityDamageEvent event, DraconicBossEntity bossEntity) {

    }

    @Override
    public void remove() {
        updateTaskSync.cancel();
        updateTaskAsync.cancel();
        eventHandler.deregister();
        bossBar.setVisible(false);
        bossBar.removeAll();
        bossEntities.remove(this);
        super.remove();
    }

    public BossEventHandler getEventHandler() {
        return eventHandler;
    }

    public void refreshBossBar() {
        double maxHealth = bossEntity.getMaxHealth();
        double health = bossEntity.getHealth();
        bossBar.setProgress(health / maxHealth);
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBossBarName() {
        return bossBarName;
    }

    public BarColor getBossBarColor() {
        return bossBarColor;
    }

    public double getDefaultMaxHealth() {
        return defaultMaxHealth;
    }

    public double getDefaultHealth() {
        return defaultHealth;
    }

    public BarStyle getBarStyle() {
        return barStyle;
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public BukkitTask getUpdateTaskSync() {
        return updateTaskSync;
    }

    public BukkitTask getUpdateTaskAsync() {
        return updateTaskAsync;
    }

    public LivingEntity getBossEntity() {
        return bossEntity;
    }

    public List<ItemStack> getDrops() {
        return drops;
    }

    public void setDrops(List<ItemStack> drops) {
        this.drops = drops;
    }

    public void face(Entity entity, Location location) {
        LocationUtil.faceLocation(entity, location);
    }

    private class BossEventHandler implements Listener {

        private DraconicBossEntity entity;

        public BossEventHandler(Plugin plugin, DraconicBossEntity entity) {
            this.entity = entity;
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }

        public void deregister() {
            HandlerList.unregisterAll(this);
        }

        @EventHandler
        public void onDamage(EntityDamageByEntityEvent entityEvent) {
            if(entityEvent.getEntity().getUniqueId().equals(entity.getBossEntity().getUniqueId())) {
                entity.bossDamaged(entityEvent, entityEvent.getDamager(), entity);
            }
        }

        @EventHandler
        public void onDeath(EntityDeathEvent entityEvent) {
            if(entityEvent.getEntity().getUniqueId().equals(entity.getBossEntity().getUniqueId())) {
                entityEvent.getDrops().clear();
                entityEvent.getDrops().addAll(entity.getDrops());
                entity.bossKilled(entityEvent, entityEvent.getEntity().getKiller(), entity);
            }
        }

        @EventHandler
        public void onAttack(EntityDamageByEntityEvent entityEvent) {
            if(entityEvent.getDamager().getUniqueId().equals(entity.getBossEntity().getUniqueId())) {
                if(entityEvent.getEntity() instanceof Player) {
                    entity.bossAttack(entityEvent, entityEvent.getEntity(), entityEvent.getDamage(), entity);
                }
            }
        }

        @EventHandler
        public void onGeneralDamage(EntityDamageEvent entityEvent) {
            if(entityEvent.getEntity().getUniqueId().equals(entity.getBossEntity().getUniqueId())) {
                entity.bossDamaged(entityEvent, entity);
            }
        }

    }

    public enum Phase {

        FIRST_PHASE(1), SECOND_PHASE(2), THIRD_PHASE(3), FINAL_PHASE(4);

        private final int rank;

        Phase(int rank) {
            this.rank = rank;
        }

        public int getRank() {
            return rank;
        }

        public boolean isHigher(Phase phase) {
            return rank > phase.getRank();
        }

        public boolean isSmaller(Phase phase) {
            return rank < phase.getRank();
        }

    }

}
