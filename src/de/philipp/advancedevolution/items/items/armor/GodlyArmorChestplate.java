package de.philipp.advancedevolution.items.items.armor;

import de.philipp.advancedevolution.items.ArmorItem;
import de.philipp.advancedevolution.items.DraconicArmorType;
import de.philipp.advancedevolution.items.IArmorModifier;
import de.philipp.advancedevolution.items.armormodifier.BadEffectsModifier;
import de.philipp.advancedevolution.items.armormodifier.FlightEnabledModifier;
import de.philipp.advancedevolution.items.armormodifier.FlightSpeedModifier;
import de.philipp.advancedevolution.items.custombases.DraconicArmorItemBase;
import de.philipp.advancedevolution.util.item.ArmorType;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

public class GodlyArmorChestplate extends DraconicArmorItemBase {
    @Override
    public double getShieldCapacity() {
        return 3072;
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
        return "GodlyArmorChestplate";
    }

    @Override
    public ItemStack getBaseStack() {
        return new ArmorItem(Color.RED, ArmorType.CHESTPLATE, 0.2d, 50, 50, "§cGodly Armor Chestplate", "", "").getStack();
    }

    @Override
    public IArmorModifier[] getModifiers() {
        return new IArmorModifier[] {new FlightEnabledModifier(), new FlightSpeedModifier(DraconicArmorType.CHAOTIC), new BadEffectsModifier()};
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}
