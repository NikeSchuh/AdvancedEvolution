package de.philipp.advancedevolution.items.items.armor;

import de.philipp.advancedevolution.items.ArmorItem;
import de.philipp.advancedevolution.items.DraconicArmorType;
import de.philipp.advancedevolution.items.custombases.DraconicArmorItemBase;
import de.philipp.advancedevolution.util.item.ArmorType;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

public class WyvernArmorBoots extends DraconicArmorItemBase {
    @Override
    public double getShieldCapacity() {
        return 20;
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
        return "WyvernArmorBoots";
    }

    @Override
    public ItemStack getBaseStack() {
        return new ArmorItem(Color.PURPLE, ArmorType.BOOTS,4, 3, "§5Wyvern Armor Boots", "", "").getStack();
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}
