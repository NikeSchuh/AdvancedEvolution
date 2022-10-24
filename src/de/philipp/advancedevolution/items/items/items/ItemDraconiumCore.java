package de.philipp.advancedevolution.items.items.items;

import de.philipp.advancedevolution.crafting.DraconicKey;
import de.philipp.advancedevolution.crafting.DraconicRecipe;
import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.recipes.DraconicIngredient;
import de.philipp.advancedevolution.recipes.EnhancedRecipe;
import de.philipp.advancedevolution.recipes.VanillaIngredient;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.inventory.ItemStack;

public class ItemDraconiumCore extends DraconicItemBase {

    @Override
    public String getDataName() {
        return "DraconicCore";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§cDraconic Core", XMaterial.HEART_OF_THE_SEA, "", "§7The core to start everything", "§7better than vanilla materials.", "");
    }

    @Override
    public boolean isStackable() {
        return true;
    }

    public boolean isUseAbleInVanillaWorkbench() {
        return true;
    }

    /*/
    @Override
    public DraconicRecipe getCraftingRecipe() {
        DraconicRecipe recipe = new DraconicRecipe(new DraconicKey("draconiccore"), "DraconicCore");
        recipe.shape("DGD", "GXG", "DGD");
        recipe.setIngredient('X', XMaterial.DIAMOND.parseMaterial());
        recipe.setIngredient('G', XMaterial.GOLD_INGOT.parseMaterial());
        recipe.setIngredient('D', XMaterial.REDSTONE.parseMaterial());
        recipe.setDraconicItem(0, "DraconiumDust");
        recipe.setDraconicItem(2, "DraconiumDust");
        recipe.setDraconicItem(6, "DraconiumDust");
        recipe.setDraconicItem(8, "DraconiumDust");
        return recipe;
    }
    /*/

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
}