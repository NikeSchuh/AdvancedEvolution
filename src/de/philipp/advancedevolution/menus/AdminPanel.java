package de.philipp.advancedevolution.menus;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.gui.Menu;
import de.philipp.advancedevolution.util.item.ItemUtil;
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

        inv.setItem(10, ItemUtil.createSimpleStack("§cItems", XMaterial.PAPER, "", "§aClick §7to view all items"));
        inv.setItem(12, ItemUtil.createSimpleStack("§cRegions", XMaterial.WOODEN_AXE, "", "§aClick §7to view the regions"));
        inv.setItem(32, ItemUtil.createSimpleStack("§cSettings", XMaterial.WRITABLE_BOOK, "", "§aClick §7to view the settings tab"));
        inv.setItem(34, ItemUtil.createSimpleStack("§cPerformance", XMaterial.CHAIN_COMMAND_BLOCK, "", "§aClick §7to view the performance tab"));

        return inv;
    }

    @Override
    public void onClick(InventoryClickEvent event, Player player, ItemStack clicked) {
        event.setCancelled(true);
        if(clicked == null) return;
        if(clicked.getType() == XMaterial.PAPER.parseMaterial()) {
            AdvancedEvolution.itemBrowser.open(player, 1);
        } else if(clicked.getType() == XMaterial.WOODEN_AXE.parseMaterial()) {
            AdvancedEvolution.regionBrowser.open(player, 1);
        } else if(clicked.getType() == XMaterial.CHAIN_COMMAND_BLOCK.parseMaterial()) {
            AdvancedEvolution.performanceMenu.open(player, 1);
        }
    }

}
