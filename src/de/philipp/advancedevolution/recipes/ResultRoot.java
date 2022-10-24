package de.philipp.advancedevolution.recipes;

import de.philipp.advancedevolution.items.DraconicItem;
import de.philipp.advancedevolution.items.ItemRegistry;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ResultRoot {

    private ItemStack baseStack;

    public ResultRoot(String dataName) {
        if(!ItemRegistry.isRegistered(dataName)) {
            new NullPointerException("DraconicItem " + dataName + " could not be found in the registry.");
            return;
        }
        baseStack = DraconicItem.instantiateTrueItem(dataName).getCurrentStack();
    }

    public ResultRoot(String dataName, int amount) {
        if(!ItemRegistry.isRegistered(dataName)) {
            new NullPointerException("DraconicItem " + dataName + " could not be found in the registry.");
            return;
        }
        baseStack = DraconicItem.instantiateTrueItem(dataName).getCurrentStack();
        baseStack.setAmount(amount);
    }

    public ResultRoot(ItemStack stack) {
        baseStack = stack;
    }

    public ResultRoot(XMaterial material) {
        baseStack = material.parseItem();
    }

    public ResultRoot(Material material) {
        baseStack = new ItemStack(material, 1);
    }

    public ItemStack formStack() {
        return baseStack;
    }

}
