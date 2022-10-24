package de.philipp.advancedevolution.items.items.items;

import de.philipp.advancedevolution.events.customevents.DraconicItemInteractEvent;
import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.recipes.DraconicIngredient;
import de.philipp.advancedevolution.recipes.EnhancedRecipe;
import de.philipp.advancedevolution.recipes.VanillaIngredient;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.inventory.ItemStack;

public class ItemGodlyCore extends DraconicItemBase {

    @Override
    public String getDataName() {
        return "GodlyCore";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§4Godly Core", XMaterial.BEACON, "", "§7Whaat? Hoow?", "§7This ...", "§7should not be possible ...", "§7it thinks and evolves ...", "§7it lives ...", "" , "§cGodly");
    }

    @Override
    public void onInteract(DraconicItemInteractEvent event) {
        event.setCancelled(true);
    }

    @Override
    public boolean isStackable() {
        return true;
    }


    @Override
    public EnhancedRecipe getCraftingRecipe() {
        EnhancedRecipe recipe = new EnhancedRecipe();
        recipe.addIngredient(new VanillaIngredient(XMaterial.GOLD_INGOT.parseMaterial(), 1));
        return recipe;
    }
}