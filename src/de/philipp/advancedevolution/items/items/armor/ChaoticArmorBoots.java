package de.philipp.advancedevolution.items.items.armor;

import de.philipp.advancedevolution.items.ArmorItem;
import de.philipp.advancedevolution.items.DraconicArmorType;
import de.philipp.advancedevolution.items.custombases.DraconicArmorItemBase;
import de.philipp.advancedevolution.util.item.ArmorType;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

public class ChaoticArmorBoots extends DraconicArmorItemBase {
    @Override
    public double getShieldCapacity() {
        return 144;
    }

    @Override
    public float getShieldRegeneration() {
        return 3f ;
    }

    @Override
    public DraconicArmorType getDraconicArmorType() {
        return DraconicArmorType.CHAOTIC;
    }

    @Override
    public double getEnergyCapacity() {
        return 64000000;
    }

    @Override
    public String getDataName() {
        return "ChaoticArmorBoots";
    }

    @Override
    public ItemStack getBaseStack() {
        return new ArmorItem(Color.BLACK, ArmorType.BOOTS, 16, 10,  "§0Chaotic Armor Boots", "", "").getStack();
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}
