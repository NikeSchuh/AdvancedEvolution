package de.philipp.advancedevolution.items.items.armor;

import de.philipp.advancedevolution.items.ArmorItem;
import de.philipp.advancedevolution.items.DraconicArmorType;
import de.philipp.advancedevolution.items.custombases.DraconicArmorItemBase;
import de.philipp.advancedevolution.util.item.ArmorType;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

public class WyvernArmorHelmet extends DraconicArmorItemBase {
    @Override
    public double getShieldCapacity() {
        return 18;
    }

    @Override
    public float getShieldRegeneration() {
        return 0.5f;
    }

    @Override
    public DraconicArmorType getDraconicArmorType() {
        return DraconicArmorType.WYVERN;
    }

    @Override
    public double getEnergyCapacity() {
        return 4000000;
    }

    @Override
    public String getDataName() {
        return "WyvernArmorHelmet";
    }

    @Override
    public ItemStack getBaseStack() {
        return new ArmorItem(Color.PURPLE, ArmorType.HELMET, 4, 3, "§5Wyvern Armor Helmet", "", "").getStack();
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}
