package de.philipp.advancedevolution.items.items.items;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.crafting.DraconicKey;
import de.philipp.advancedevolution.crafting.DraconicRecipe;
import de.philipp.advancedevolution.crafting.RecipeManager;
import de.philipp.advancedevolution.items.DraconicItem;
import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.items.ItemRegistry;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.recipes.DraconicIngredient;
import de.philipp.advancedevolution.recipes.EnhancedRecipe;
import de.philipp.advancedevolution.recipes.VanillaIngredient;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

public class ItemWyvernCore extends DraconicItemBase {
    @Override
    public String getDataName() {
        return "WyvernCore";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§5Wyvern Core", XMaterial.NETHER_STAR, "", "§7Holds much power,", "§7too much ...");
    }

    @Override
    public boolean isStackable() {
        return true;
    }

    @Override
    public EnhancedRecipe getCraftingRecipe() {
        EnhancedRecipe recipe = new EnhancedRecipe();
        recipe.addIngredient(new VanillaIngredient(XMaterial.NETHER_STAR.parseMaterial(), 1));
        recipe.addIngredient(new VanillaIngredient(XMaterial.REDSTONE_BLOCK.parseMaterial(), 4));
        recipe.addIngredient(new DraconicIngredient("DraconicCore", 4));
        recipe.setDurationFactor(2);
        return recipe;
    }




}
