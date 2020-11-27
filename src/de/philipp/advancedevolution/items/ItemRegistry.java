package de.philipp.advancedevolution.items;

import de.philipp.advancedevolution.AdvancedEvolution;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemRegistry {

    public static HashMap<String, DraconicItemBase> itemList = new HashMap<>();

    public static void registerItem(DraconicItemBase base) {
        if(!itemList.containsKey(base.getDataName())) {
            AdvancedEvolution.send("Loading item " + base + " with ItemLoader-1");
            itemList.put(base.getDataName(), base);
        } else {
            AdvancedEvolution.send("ItemRegistry: Error " + base.getDataName() + " is already registered.");
            throw new IllegalArgumentException("Cannot register " + base.getDataName() +" twice.");
        }
    }

    public static DraconicItemBase getItemBase(String dataName) {
        if(itemList.containsKey(dataName)) {
            return itemList.get(dataName);
        }
        return null;
    }

}
