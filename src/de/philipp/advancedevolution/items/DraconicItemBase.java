package de.philipp.advancedevolution.items;

import de.philipp.advancedevolution.crafting.DraconicRecipe;
import de.philipp.advancedevolution.events.customevents.DraconicItemInteractEvent;
import de.philipp.advancedevolution.recipes.EnhancedRecipe;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

public abstract class DraconicItemBase {

    public abstract String getDataName();
    public abstract ItemStack getBaseStack();
    public abstract boolean isStackable();

    public boolean isUnbreakable() {
        return false;
    }
    public boolean isUseAbleInVanillaWorkbench() {
        return false;
    }
    public boolean isCraftAble() {
        return getCraftingRecipe() != null;
    }
    public EnhancedRecipe getCraftingRecipe() {
       return null;
    }
    public void onInteract(DraconicItemInteractEvent event) {

    }
    public boolean isBurnable() {return true; }


    public String toString() {
        return "{" + getDataName()  + "}";
    }

}
