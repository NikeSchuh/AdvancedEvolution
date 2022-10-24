package de.philipp.advancedevolution.recipes;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.api.API;
import de.philipp.advancedevolution.items.DraconicItem;
import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.items.ItemRegistry;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EnhancedRecipe {

    private List<VanillaIngredient> vanillaIngredients;
    private List<DraconicIngredient> draconicIngredients;
    private double durationFactor = 1;

    public EnhancedRecipe(List<VanillaIngredient> vanillaIngredients, List<DraconicIngredient> draconicIngredients) {
        this.vanillaIngredients = vanillaIngredients;
        this.draconicIngredients = draconicIngredients;
    }

    public EnhancedRecipe() {
        this.vanillaIngredients = new ArrayList<>();
        this.draconicIngredients = new ArrayList<>();
    }

    public void addIngredient(VanillaIngredient ingredient) {
        vanillaIngredients.add(ingredient);
    }

    public void addIngredient(DraconicIngredient ingredient) {
        draconicIngredients.add(ingredient);
    }

    public void setDurationFactor(double durationFactor) {
        this.durationFactor = durationFactor;
    }

    public DraconicIngredient[] getDraconicIngredients() {
        return draconicIngredients.toArray(new DraconicIngredient[draconicIngredients.size()]);
    }

    public VanillaIngredient[] getVanillaIngredients() {
        return vanillaIngredients.toArray(new VanillaIngredient[vanillaIngredients.size()]);
    }

    public double getDurationFactor() {
        return durationFactor;
    }

    public interface Ingredient {


    }

    public static boolean hasIngredients(Inventory inventory, VanillaIngredient[] vanillaIngredients, DraconicIngredient[] draconicIngredients) {
        for(VanillaIngredient vanillaIngredient : vanillaIngredients) {
            if(!(inventory.contains(vanillaIngredient.getMaterial(), vanillaIngredient.getAmount()))) return false;
        }
        HashMap<String, Integer> draconicMap = new HashMap<>();
        for(ItemStack stack : inventory.getContents()) {
            if(DraconicItem.isDraconicItem(stack)) {
                DraconicItem draconicItem = new DraconicItem(stack);
                draconicMap.put(draconicItem.getDataName(), draconicMap.getOrDefault(draconicItem.getDataName(), 0) + stack.getAmount());
            }
        }
        for(DraconicIngredient draconicIngredient : draconicIngredients) {
            String dataName = draconicIngredient.getDataName();
            if(draconicMap.getOrDefault(dataName, 0) < draconicIngredient.getAmount()) return false;
        }
        return true;
    }

}
