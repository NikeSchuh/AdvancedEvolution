package de.philipp.advancedevolution.recipes;

import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.items.ItemRegistry;

public class DraconicIngredient implements EnhancedRecipe.Ingredient {

    private String dataName;
    private int amount;

    public DraconicIngredient(String dataName, int amount) {
        this.dataName = dataName;
        this.amount = amount;
    }

    public DraconicItemBase getBase() {
        return ItemRegistry.getItemBase(dataName);
    }

    public String getDataName() {
        return dataName;
    }

    public int getAmount() {
        return amount;
    }
}
