package de.philipp.advancedevolution.menus;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.gui.Menu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class AdminPanel extends Menu {

    public AdminPanel(Plugin pl, Player player) {
        super(pl, player);
    }

    @Override
    public String getTitle() {
        return AdvancedEvolution.ChatPrefix +  "Admin";
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(null, 5*9, getTitle());

        inv.setItem(10, new ItemStack(XMaterial.BONE.parseMaterial(), 1));

        return inv;
    }

    @Override
    public void onClick(InventoryClickEvent event, Player player, ItemStack clicked) {
        event.setCancelled(true);
    }

}
