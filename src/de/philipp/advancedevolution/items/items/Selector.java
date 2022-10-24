package de.philipp.advancedevolution.items.items;

import de.philipp.advancedevolution.events.customevents.DraconicItemInteractEvent;
import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.Pair;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Selector extends DraconicItemBase {

    public static HashMap<UUID, Pair<Location, Location>> locationMap = new HashMap<>();

    @Override
    public String getDataName() {
        return "selector";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§cRegion Selector", XMaterial.STICK.parseMaterial(), "");
    }

    @Override
    public void onInteract(DraconicItemInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        if(!locationMap.containsKey(player.getUniqueId())) {
            locationMap.put(player.getUniqueId(), new Pair<>(null, null));
        }
        Pair<Location, Location> locations = locationMap.get(player.getUniqueId());
        if(action == Action.RIGHT_CLICK_BLOCK) {
            locationMap.put(player.getUniqueId(), new Pair<>(locations.getKey(), event.getClicked().getLocation()));
            event.setCancelled(true);
        } else if(action == Action.LEFT_CLICK_BLOCK) {
            locationMap.put(player.getUniqueId(), new Pair<>(event.getClicked().getLocation(), locations.getValue()));
            event.setCancelled(true);
        }
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}
