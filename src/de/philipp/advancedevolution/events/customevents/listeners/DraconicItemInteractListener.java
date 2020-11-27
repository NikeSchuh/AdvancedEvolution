package de.philipp.advancedevolution.events.customevents.listeners;

import de.philipp.advancedevolution.events.customevents.DraconicItemInteractEvent;
import de.philipp.advancedevolution.items.DraconicItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class DraconicItemInteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getItem() != null) {
            ItemStack stack = event.getItem();
            if(DraconicItem.isDraconicItem(stack)) {
                DraconicItem item = new DraconicItem(stack);
                DraconicItemInteractEvent interactEvent = new DraconicItemInteractEvent(player, event.getAction(), event.getItem(), item, event.getClickedBlock(), event.getBlockFace(), player, event.getAction(), event.getItem(), event.getClickedBlock(), event.getBlockFace());
                Bukkit.getPluginManager().callEvent(interactEvent);
                if(interactEvent.isCancelled()) {
                    event.setCancelled(true);
                }
               }
            }
    }

}
