package de.philipp.advancedevolution.items.custombases;

import de.philipp.advancedevolution.items.ArmorModifier;
import de.philipp.advancedevolution.items.DraconicArmorType;
import de.philipp.advancedevolution.util.item.ArmorType;
import org.bukkit.inventory.ItemStack;

public abstract class DraconicArmorItemBase extends DraconicEnergyItemBase {

    public abstract double getShieldCapacity();
    public abstract float getShieldRegeneration();
    public abstract DraconicArmorType getDraconicArmorType();
    public ArmorModifier[] getModifiers() {
        return new ArmorModifier[0];
    }

    public String toString() {
        return "{" + getDataName() + ","+ getEnergyCapacity() + "," + getShieldCapacity() + "," + getShieldRegeneration() +"}";
    }

}
