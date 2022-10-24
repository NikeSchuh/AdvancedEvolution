package de.philipp.advancedevolution.util.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public abstract class Menu implements Listener {

    protected Player player;
    protected Plugin plugin;
    protected Inventory inv;

    public Menu(Plugin pl, Player player) {
        this.player = player;
        this.plugin = pl;
    }

    public abstract String getTitle();
    public abstract Inventory getInventory();
    public abstract void onClick(InventoryClickEvent event, Player player, ItemStack clicked);

   public void open() {
       this.inv = getInventory();
        player.openInventory(inv);
       Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if(event.getPlayer().getUniqueId().equals(player.getUniqueId())) {
            HandlerList.unregisterAll(this);
            onRemoval(event);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(event.getWhoClicked().getUniqueId().equals(player.getUniqueId())) {
            onClick(event, (Player) event.getWhoClicked(), event.getCurrentItem());
        }
    }

    public void close() {
       player.closeInventory();
    }
    public void onRemoval(InventoryCloseEvent event) {

    }


    public void switchMenu(Menu menu) {

    }
}
