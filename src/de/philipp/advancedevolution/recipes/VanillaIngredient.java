package de.philipp.advancedevolution.recipes;

import de.philipp.advancedevolution.lib.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public  class VanillaIngredient implements EnhancedRecipe.Ingredient {

    private Material material;
    private int amount;

    public VanillaIngredient(Material material, int amount) {
        this.material = material;
        this.amount = amount;
    }

    public VanillaIngredient(XMaterial material, int amount) {
        this.material = material.parseMaterial();
        this.amount = amount;
    }

    public Material getMaterial() {
        return material;
    }

    public boolean hasIngredient(Inventory inventory) {
        return inventory.contains(material, amount);
    }

    public int getAmount() {
        return amount;
    }
}
