package de.philipp.advancedevolution.blocks.gui;

import de.philipp.advancedevolution.util.gui.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public abstract class MachineGUI extends Menu {

    public static HashMap<InventoryContainer, UUID> container;
    private BukkitTask ticker;
    private Inventory inventoryChild;

    public MachineGUI(Plugin pl, Player player) {
        super(pl, player);
    }

    @Override
    public void open() {

    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    @Override
    public void onClick(InventoryClickEvent event, Player player, ItemStack clicked) {

    }

    public abstract void onClick(InventoryClickEvent event, Player player);
}
