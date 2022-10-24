package de.philipp.advancedevolution.crafting;

import com.google.gson.internal.$Gson$Preconditions;
import de.philipp.advancedevolution.items.DraconicItem;
import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.items.ItemRegistry;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.HashMap;

public class DraconicRecipe extends ShapedRecipe implements Keyed {

    private HashMap<Integer, String> draconicItems = new HashMap<>();
    private String dataName;
    private DraconicItem result;

    public DraconicRecipe(DraconicKey key, String dataName) {
        super(key.getKey(), DraconicItem.instantiateTrueItem(dataName).getCurrentStack());
        this.dataName = dataName;
        this.result = new DraconicItem(this.getResult());
    }

    public HashMap<Integer, String> getDraconicItems() {
        return draconicItems;
    }

    public void setDraconicItem(int slot, String dataName) {
        draconicItems.put(slot, dataName);
    }

    public void removeDraconicItem(int slot) {
        draconicItems.remove(slot);
    }

    public boolean isDraconicIngredient(int slot) {
        return draconicItems.containsKey(slot);
    }

    public DraconicItemBase getBase(int slot) {
        return ItemRegistry.getItemBase(draconicItems.get(slot));
    }

    public String getDataName() {
        return dataName;
    }

    public DraconicItem getDraconicResult() {
        return result;
    }
}
