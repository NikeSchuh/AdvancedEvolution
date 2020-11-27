package de.philipp.advancedevolution.menus;

import de.philipp.advancedevolution.items.ArmorModifier;
import de.philipp.advancedevolution.util.gui.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ArmorBooleanEditor extends Menu {

    private ArmorModifier modifier;

    public ArmorBooleanEditor(Plugin pl, Player player, ArmorModifier modifier) {
        super(pl, player);
        this.modifier = modifier;
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
}
