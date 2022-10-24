package de.philipp.advancedevolution.items.items.items;

import de.philipp.advancedevolution.events.customevents.DraconicItemInteractEvent;
import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.recipes.DraconicIngredient;
import de.philipp.advancedevolution.recipes.EnhancedRecipe;
import de.philipp.advancedevolution.recipes.VanillaIngredient;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.inventory.ItemStack;

public class ItemWirelessTransmitter extends DraconicItemBase {
    @Override
    public String getDataName() {
        return "WirelessTransmitter";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§cWireless Transmitter", XMaterial.REDSTONE_TORCH, "", "§7Mainly used for transmitting", "§7data through the air and blocks.", "");
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
        recipe.addIngredient(new VanillaIngredient(XMaterial.ENDER_EYE.parseMaterial(), 4));
        recipe.addIngredient(new DraconicIngredient("Circuit", 2));
        recipe.addIngredient(new DraconicIngredient("CircuitAdvanced", 1));
        recipe.setDurationFactor(1);
        return recipe;
    }
}
