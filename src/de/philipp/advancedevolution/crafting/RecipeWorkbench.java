package de.philipp.advancedevolution.crafting;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.items.DraconicItem;
import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.gui.Menu;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class RecipeWorkbench extends Menu {

    private RecipeManager recipeManager;

    public RecipeWorkbench(Plugin pl, Player player) {
        super(pl, player);
        this.recipeManager = AdvancedEvolution.getRecipeManager();

    }

    @Override
    public String getTitle() {
        return "Draconic Workbench";
    }



    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 5*9, getTitle());
        for(int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, ItemUtil.createSimpleStack("§c", XMaterial.BLACK_STAINED_GLASS_PANE));
        }

        inventory.setItem(11, null);
        inventory.setItem(12, null);
        inventory.setItem(13, null);
        inventory.setItem(20, null);
        inventory.setItem(21, null);
        inventory.setItem(22, null);
        inventory.setItem(24, null);
        inventory.setItem(29, null);
        inventory.setItem(30, null);
        inventory.setItem(31, null);


        return inventory;
    }

    @Override
    public void onClick(InventoryClickEvent event, Player player, ItemStack clicked) {
            if(event.getCursor() != null) {
                if (event.getClickedInventory().equals(inv)) {
                    if (event.getSlot() == 24) {
                        event.setCancelled(true);
                    }
                }
            }
        if(clicked != null) {
            if (event.getClickedInventory().equals(inv)) {
                if(clicked.getType() == XMaterial.BLACK_STAINED_GLASS_PANE.parseMaterial()) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    public void setResult(ItemStack stack) {
        inv.setItem(24, stack);
    }


}
