package de.philipp.advancedevolution.items.custombases;

import de.philipp.advancedevolution.items.DraconicItemBase;
import org.bukkit.inventory.ItemStack;

public abstract class DraconicEnergyItemBase extends DraconicItemBase {

    public abstract double getEnergyCapacity();


    @Override
    public boolean isUnbreakable() {
        return true;
    }

    public String toString() {
        return "{" + getDataName() + ","+ getEnergyCapacity() + "}";
    }
    public boolean isBurnable() {return false; }
}
