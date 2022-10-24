package de.philipp.advancedevolution.items.items.items;

import de.philipp.advancedevolution.events.customevents.DraconicItemInteractEvent;
import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.recipes.DraconicIngredient;
import de.philipp.advancedevolution.recipes.EnhancedRecipe;
import de.philipp.advancedevolution.recipes.VanillaIngredient;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.inventory.ItemStack;

public class ItemCircuit extends DraconicItemBase {
    @Override
    public String getDataName() {
        return "Circuit";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§cCircuit", XMaterial.COMPARATOR, "", "§7Mainly used for machines", "§7and other devices", "");
    }

    @Override
    public boolean isStackable() {
        return true;
    }

    @Override
    public void onInteract(DraconicItemInteractEvent event) {
        event.setCancelled(true);
    }

    @Override
    public EnhancedRecipe getCraftingRecipe() {
        EnhancedRecipe recipe = new EnhancedRecipe();
        recipe.addIngredient(new VanillaIngredient(XMaterial.GOLD_INGOT.parseMaterial(), 4));
        recipe.addIngredient(new VanillaIngredient(XMaterial.REDSTONE.parseMaterial(), 4));
        recipe.addIngredient(new VanillaIngredient(XMaterial.COMPARATOR.parseMaterial(), 1));
        recipe.setDurationFactor(0.1);
        return recipe;
    }
}
