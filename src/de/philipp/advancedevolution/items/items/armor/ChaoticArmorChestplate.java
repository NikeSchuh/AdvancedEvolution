package de.philipp.advancedevolution.items.items.armor;

import de.philipp.advancedevolution.items.ArmorItem;
import de.philipp.advancedevolution.items.IArmorModifier;
import de.philipp.advancedevolution.items.DraconicArmorType;
import de.philipp.advancedevolution.items.armormodifier.BadEffectsModifier;
import de.philipp.advancedevolution.items.armormodifier.FlightEnabledModifier;
import de.philipp.advancedevolution.items.armormodifier.FlightSpeedModifier;
import de.philipp.advancedevolution.items.custombases.DraconicArmorItemBase;
import de.philipp.advancedevolution.util.item.ArmorType;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

public class ChaoticArmorChestplate extends DraconicArmorItemBase {
    @Override
    public double getShieldCapacity() {
        return 384;
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
        return "ChaoticArmorChestplate";
    }

    @Override
    public ItemStack getBaseStack() {
        return new ArmorItem(Color.BLACK, ArmorType.CHESTPLATE, 0.2d, 40, 30, "§0Chaotic Armor Chestplate", "", "").getStack();
    }

    @Override
    public IArmorModifier[] getModifiers() {
        return new IArmorModifier[] {new FlightEnabledModifier(), new FlightSpeedModifier(DraconicArmorType.GODLY), new BadEffectsModifier()};
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}
