package de.philipp.advancedevolution.items;


import de.philipp.advancedevolution.events.customevents.DraconicItemInteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

public class InteractManager implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getItem() != null) {
            if(DraconicItem.isDraconicItem(event.getItem())) {
                DraconicItem draconicItem = new DraconicItem(event.getItem());
                draconicItem.getItemBase().onInteract(new DraconicItemInteractEvent(event.getPlayer(), event.getAction(), event.getItem(), draconicItem, event.getClickedBlock(), event));
            }
        }

    }

}
