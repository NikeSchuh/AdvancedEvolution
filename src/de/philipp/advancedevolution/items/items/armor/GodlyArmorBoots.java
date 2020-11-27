package de.philipp.advancedevolution.items.items.armor;

import de.philipp.advancedevolution.items.ArmorItem;
import de.philipp.advancedevolution.items.DraconicArmorType;
import de.philipp.advancedevolution.items.custombases.DraconicArmorItemBase;
import de.philipp.advancedevolution.util.item.ArmorType;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

public class GodlyArmorBoots extends DraconicArmorItemBase {
    @Override
    public double getShieldCapacity() {
        return 1152;
    }

    @Override
    public float getShieldRegeneration() {
        return 10f;
    }

    @Override
    public DraconicArmorType getDraconicArmorType() {
        return DraconicArmorType.GODLY;
    }

    @Override
    public double getEnergyCapacity() {
        return 256000000d;
    }

    @Override
    public String getDataName() {
        return "GodlyArmorBoots";
    }

    @Override
    public ItemStack getBaseStack() {
        return new ArmorItem(Color.RED, ArmorType.BOOTS, 30, 40, "§cGodly Armor Boots", "", "").getStack();
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}