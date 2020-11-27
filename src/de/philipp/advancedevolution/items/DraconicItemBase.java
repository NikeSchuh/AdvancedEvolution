package de.philipp.advancedevolution.items;

import de.philipp.advancedevolution.events.customevents.DraconicItemInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class DraconicItemBase {

    public abstract String getDataName();
    public abstract ItemStack getBaseStack();
    public abstract boolean isStackable();

    public boolean isUnbreakable() {
        return false;
    }

    public void onInteract(DraconicItemInteractEvent event) {

    }

    public String toString() {
        return "{" + getDataName()  + "}";
    }

}
