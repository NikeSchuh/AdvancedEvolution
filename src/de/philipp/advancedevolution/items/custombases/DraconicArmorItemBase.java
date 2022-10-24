package de.philipp.advancedevolution.items.custombases;

import de.philipp.advancedevolution.items.IArmorModifier;
import de.philipp.advancedevolution.items.DraconicArmorType;

public abstract class DraconicArmorItemBase extends DraconicEnergyItemBase {

    public abstract double getShieldCapacity();
    public abstract float getShieldRegeneration();
    public abstract DraconicArmorType getDraconicArmorType();
    public IArmorModifier[] getModifiers() {
        return new IArmorModifier[0];
    }

    public String toString() {
        return "{" + getDataName() + ","+ getEnergyCapacity() + "," + getShieldCapacity() + "," + getShieldRegeneration() +"}";
    }

    public boolean isBurnable() {return false; }

}
