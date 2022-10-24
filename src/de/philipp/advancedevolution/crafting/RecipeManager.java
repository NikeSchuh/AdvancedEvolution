package de.philipp.advancedevolution.crafting;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.items.DraconicItem;
import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.items.ItemRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Deprecated
public class RecipeManager implements Listener {

    private ItemRegistry itemRegistry;
    public static List<DraconicRecipe> recipes = new ArrayList<>();
    private Plugin plugin;

    public RecipeManager(Plugin plugin, ItemRegistry itemRegistry) {
        this.plugin = plugin;
        this.itemRegistry = itemRegistry;

        Bukkit.getPluginManager().registerEvents(this, AdvancedEvolution.getInstance());

        for(DraconicItemBase base : itemRegistry.getRegisteredItems()) {
            if(base.isCraftAble()) {
               // recipes.add(base.getCraftingRecipe());
                AdvancedEvolution.send("Registered recipe for " + base.getDataName());
               // Bukkit.addRecipe(base.getCraftingRecipe());
            }
        }
    }

    public void send(String ms) {
        Bukkit.getConsoleSender().sendMessage(ms);
    }

    @EventHandler
    private void onCraft(PrepareItemCraftEvent event) {
        int count = 0;
        if(event.getRecipe() == null) return;
        for(DraconicRecipe draconicRecipe : recipes) {
            send(event.getRecipe().getResult().equals(draconicRecipe.getResult()) + "");
            if(event.getRecipe().getResult().equals(draconicRecipe.getResult())) {
                count++;
                CraftingInventory craftingInventory = event.getInventory();
                for(int i = 0; i < craftingInventory.getMatrix().length; i++) {
                    if(draconicRecipe.isDraconicIngredient(i)) {
                        if(DraconicItem.isDraconicItem(craftingInventory.getMatrix()[i])) {
                            DraconicItem inRecipe = new DraconicItem(craftingInventory.getMatrix()[i]);
                            if(inRecipe.compareItemBase(draconicRecipe.getBase(i))) {
                               continue;
                            } else {
                                craftingInventory.setResult(null);
                                break;
                            }
                        }else {
                            craftingInventory.setResult(null);
                            break;
                        }
                    } else if(DraconicItem.isDraconicItem(craftingInventory.getMatrix()[i])) {
                        craftingInventory.setResult(null);
                        break;
                    }

                }
            }
        }
        if(count == 0) {
            for(ItemStack s : event.getInventory().getContents()) {
                if(s == null) continue;
                if(DraconicItem.isDraconicItem(s)) {
                    DraconicItem item = new DraconicItem(s);
                    if(!item.getItemBase().isUseAbleInVanillaWorkbench()) {
                        event.getInventory().setResult(null);
                        return;
                    }
                }
            }
        }

    }

}
