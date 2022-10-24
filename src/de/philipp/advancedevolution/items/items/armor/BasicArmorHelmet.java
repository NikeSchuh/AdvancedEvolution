package de.philipp.advancedevolution.items.items.armor;

import de.philipp.advancedevolution.items.ArmorItem;
import de.philipp.advancedevolution.items.DraconicArmorType;
import de.philipp.advancedevolution.items.custombases.DraconicArmorItemBase;
import de.philipp.advancedevolution.util.item.ArmorType;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

public class BasicArmorHelmet extends DraconicArmorItemBase {
    @Override
    public double getShieldCapacity() {
        return 6;
    }

    @Override
    public float getShieldRegeneration() {
        return 0.025f;
    }

    @Override
    public DraconicArmorType getDraconicArmorType() {
        return DraconicArmorType.BASIC;
    }

    @Override
    public double getEnergyCapacity() {
        return 256000;
    }

    @Override
    public String getDataName() {
        return "BasicArmorHelmet";
    }

    @Override
    public ItemStack getBaseStack() {
        return new ArmorItem(Color.BLUE, ArmorType.HELMET, 3, 2, "§bBasic Armor Helmet", "", "").getStack();
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}
