package de.philipp.advancedevolution.drops;

import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.items.ItemRegistry;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class DropRegistry implements Listener {

    public static HashMap<EntityType, DraconicItemBase> dropList = new HashMap<>();

    public static void registerDrop(String dataName, EntityType entityType) {
        if(!dropList.containsKey(entityType)) {
            dropList.put(entityType, ItemRegistry.getItemBase(dataName));
        }
    }

    public static void registerListener(Plugin plugin) {

    }

}
