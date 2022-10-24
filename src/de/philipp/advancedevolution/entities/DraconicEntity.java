package de.philipp.advancedevolution.entities;

import com.google.common.base.Predicates;
import de.philipp.advancedevolution.StorageKeys;
import de.tr7zw.nbtapi.NBTEntity;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DraconicEntity {

    private Entity bukkitEntity;
    private NBTEntity nbtEntity;
    private Location spawnLocation;

    public DraconicEntity(Location location, EntityType type) {
        this.bukkitEntity = location.getWorld().spawnEntity(location, type);
        this.spawnLocation = location.clone();
        this.nbtEntity = new NBTEntity(bukkitEntity);
        this.nbtEntity.getPersistentDataContainer().setBoolean(StorageKeys.PLUGIN.getNBTKey(), true);
    }

    public DraconicEntity(Entity entity) {
        if(isDraconicEntity(entity)) {
            this.bukkitEntity = entity;
            this.spawnLocation = entity.getLocation();
            this.nbtEntity = new NBTEntity(bukkitEntity);
        } else {
            throw new IllegalArgumentException("Given entity isn't an DraconicEntity");
        }
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public Entity getBukkitEntity() {
        return bukkitEntity;
    }

    public Location getLocation() {
        return bukkitEntity.getLocation();
    }

    public List<Player> getNearbyPlayers(double v, double v1, double v2) {
        return Arrays.asList(bukkitEntity.getNearbyEntities(v, v1, v2).stream().filter(Predicates.instanceOf(Player.class)).toArray(size -> new Player[size]));
    }

    public List<Player> getNearbyPlayers(Location location, double v, double v1, double v2) {
        return Arrays.asList(bukkitEntity.getWorld().getNearbyEntities(location, v, v1, v2).stream().filter(Predicates.instanceOf(Player.class)).toArray(size -> new Player[size]));
    }

    public Player getNearestPlayer(double v, double v1, double v2) {
        double nearestDistance = 1000000;
        Location loc = getLocation();
        Player nearest = null;
        for(Player pl : getNearbyPlayers(v, v1, v2)) {
            if(pl.getLocation().distance(loc) < nearestDistance) {
                nearest = pl;
            }
        }
        return nearest;
    }

    public void attack(LivingEntity entity, double value) {
        entity.damage(value, bukkitEntity);
    }

    public void jump(double height) {
        getBukkitEntity().getVelocity().add(new Vector(0, height / 20, 0));
    }

    public void remove() {
        getBukkitEntity().remove();
    }

    public static boolean isDraconicEntity(Entity entity) {
        NBTEntity entity1 = new NBTEntity(entity);
        return entity1.getPersistentDataContainer().hasKey(StorageKeys.PLUGIN.getNBTKey());
    }

}
