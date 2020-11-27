package de.philipp.advancedevolution.items.items.armor;

import de.philipp.advancedevolution.items.ArmorItem;
import de.philipp.advancedevolution.items.DraconicArmorType;
import de.philipp.advancedevolution.items.custombases.DraconicArmorItemBase;
import de.philipp.advancedevolution.util.item.ArmorType;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

public class DraconicArmorChestplate extends DraconicArmorItemBase {
    @Override
    public double getShieldCapacity() {
        return 96;
    }

    @Override
    public float getShieldRegeneration() {
        return 1.2f;
    }

    @Override
    public DraconicArmorType getDraconicArmorType() {
        return DraconicArmorType.DRACONIC;
    }

    @Override
    public double getEnergyCapacity() {
        return 16000000;
    }

    @Override
    public String getDataName() {
        return "DraconicArmorChestplate";
    }

    @Override
    public ItemStack getBaseStack() {
        return new ArmorItem(Color.ORANGE, ArmorType.CHESTPLATE, 0.1d, 10, 4, "§6Draconic Armor Chestplate", "", "").getStack();
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}
