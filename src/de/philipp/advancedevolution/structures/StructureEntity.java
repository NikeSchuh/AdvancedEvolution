package de.philipp.advancedevolution.structures;

import de.tr7zw.nbtapi.NBTCompound;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.Map;

public class StructureEntity {

    private EntityType entityType;
    private Location location;

    public StructureEntity(Location location, EntityType entityType) {
        this.entityType = entityType;
        this.location = location;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public String toString() {
        return location.getX() + " " + location.getY() + " " + location.getZ() + " " + entityType.name();
    }

}
