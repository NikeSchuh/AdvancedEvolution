package de.philipp.advancedevolution.items;

import de.philipp.advancedevolution.AdvancedEvolution;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;

import java.util.*;

public class ItemRegistry {

    public static HashMap<String, DraconicItemBase> itemList = new HashMap<>();
    public static List<DraconicItemBase> orderedCraftableItems = new ArrayList<>();

    public static void reloadCraftables() {
        for(DraconicItemBase base : getRegisteredItemsSorted()) {
            if(base.isCraftAble()) {
                orderedCraftableItems.add(base);
            }
        }
    }

    public static void registerItem(DraconicItemBase base) {
        if(!itemList.containsKey(base.getDataName())) {
            AdvancedEvolution.send("Loading item " + base + " with ItemLoader-1");
            itemList.put(base.getDataName(), base);
        } else {
            AdvancedEvolution.send("ItemRegistry: Error " + base.getDataName() + " is already registered.");
            throw new IllegalArgumentException("Cannot register " + base.getDataName() +" twice.");
        }
    }

    public static boolean isRegistered(String dataName) {
        return itemList.containsKey(dataName);
    }

    public static DraconicItemBase getItemBase(String dataName) {
        if(itemList.containsKey(dataName)) {
            return itemList.get(dataName);
        }
        return null;
    }

    public static DraconicItemBase[] getRegisteredItems() {
        List<DraconicItemBase> s = new ArrayList<>();
        for(String string : itemList.keySet()) {
         s.add(itemList.get(string));
        }
        return s.toArray(new DraconicItemBase[s.size()]);
    }

    public static DraconicItemBase[] getRegisteredItemsSorted() {
        List<DraconicItemBase> s = new ArrayList<>();
        List<String> registryIds = new ArrayList<>(itemList.keySet());
        registryIds.sort(Comparator.comparing(String::toString));
        for(int i = 0; i < registryIds.size(); i++) {
            s.add(getItemBase(registryIds.get(i)));
        }
        return s.toArray(new DraconicItemBase[s.size()]);
    }

    public static DraconicItemBase[] getRegisteredCraftableItemsSorted() {
        return orderedCraftableItems.toArray(new DraconicItemBase[orderedCraftableItems.size()]);
    }

}
