package de.philipp.advancedevolution.items.items.items;

import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.recipes.DraconicIngredient;
import de.philipp.advancedevolution.recipes.EnhancedRecipe;
import de.philipp.advancedevolution.recipes.VanillaIngredient;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.inventory.ItemStack;

public class ItemChaoticCore extends DraconicItemBase {

    @Override
    public String getDataName() {
        return "ChaoticCore";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§0Chaotic Core", XMaterial.NETHER_STAR, "", "§7Very strong", "§7even stronger than", "§7Draconic ... way", "");
    }

    @Override
    public EnhancedRecipe getCraftingRecipe() {
        EnhancedRecipe recipe = new EnhancedRecipe();
        recipe.addIngredient(new VanillaIngredient(XMaterial.GOLD_INGOT.parseMaterial(), 10));
        recipe.addIngredient(new VanillaIngredient(XMaterial.DIAMOND.parseMaterial(), 4));
        recipe.addIngredient(new VanillaIngredient(XMaterial.REDSTONE_BLOCK.parseMaterial(), 4));
        recipe.addIngredient(new DraconicIngredient("DraconiumDust", 4));
        recipe.setDurationFactor(1);
        return recipe;
    }

    @Override
    public boolean isStackable() {
        return true;
    }
}
