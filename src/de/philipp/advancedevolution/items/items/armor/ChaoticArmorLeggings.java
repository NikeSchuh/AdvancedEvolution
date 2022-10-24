package de.philipp.advancedevolution.items.items.armor;

import de.philipp.advancedevolution.items.ArmorItem;
import de.philipp.advancedevolution.items.IArmorModifier;
import de.philipp.advancedevolution.items.DraconicArmorType;
import de.philipp.advancedevolution.items.armormodifier.MovementSpeedModifier;
import de.philipp.advancedevolution.items.custombases.DraconicArmorItemBase;
import de.philipp.advancedevolution.util.item.ArmorType;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

public class ChaoticArmorLeggings extends DraconicArmorItemBase {
    @Override
    public double getShieldCapacity() {
        return 360;
    }

    @Override
    public float getShieldRegeneration() {
        return 3f;
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
        return "ChaoticArmorLeggings";
    }

    @Override
    public ItemStack getBaseStack() {
        return new ArmorItem(Color.BLACK, ArmorType.LEGGINGS,0.2f,24, 20, "§0Chaotic Armor Leggings", "", "").getStack();
    }

    @Override
    public IArmorModifier[] getModifiers() {
        return new IArmorModifier[] {new MovementSpeedModifier()};
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}
