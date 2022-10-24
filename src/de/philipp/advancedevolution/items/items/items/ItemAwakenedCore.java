package de.philipp.advancedevolution.items.items.items;

import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.recipes.DraconicIngredient;
import de.philipp.advancedevolution.recipes.EnhancedRecipe;
import de.philipp.advancedevolution.recipes.VanillaIngredient;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.inventory.ItemStack;

public class ItemAwakenedCore extends DraconicItemBase {

    @Override
    public String getDataName() {
        return "AwakenedCore";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§6Awakened Core", XMaterial.NETHER_STAR, "", "§7Its a bit sentient", "§7and very very powerful", "§7be careful!", "");
    }

    @Override
    public boolean isStackable() {
        return true;
    }

    @Override
    public EnhancedRecipe getCraftingRecipe() {
        EnhancedRecipe recipe = new EnhancedRecipe();
        recipe.addIngredient(new VanillaIngredient(XMaterial.NETHER_STAR.parseMaterial(), 1));
        recipe.addIngredient(new DraconicIngredient("WyvernGuardianEye", 1));
        recipe.addIngredient(new DraconicIngredient("WyvernCore", 4));
        recipe.addIngredient(new DraconicIngredient("IngotAwakenedDraconium", 4));
        recipe.setDurationFactor(5);
        return recipe;
    }

}
