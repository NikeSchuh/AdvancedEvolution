package de.philipp.advancedevolution.items.items.armor;

import de.philipp.advancedevolution.items.ArmorItem;
import de.philipp.advancedevolution.items.DraconicArmorType;
import de.philipp.advancedevolution.items.IArmorModifier;
import de.philipp.advancedevolution.items.armormodifier.AutoFeedModifier;
import de.philipp.advancedevolution.items.armormodifier.FlightEnabledModifier;
import de.philipp.advancedevolution.items.armormodifier.FlightSpeedModifier;
import de.philipp.advancedevolution.items.armormodifier.NightVisionModifier;
import de.philipp.advancedevolution.items.custombases.DraconicArmorItemBase;
import de.philipp.advancedevolution.util.item.ArmorType;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

public class GodlyArmorHelmet extends DraconicArmorItemBase {
    @Override
    public double getShieldCapacity() {
        return 1088;
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
        return "GodlyArmorHelmet";
    }

    @Override
    public ItemStack getBaseStack() {
        return new ArmorItem(Color.RED, ArmorType.HELMET, 30, 20, "§cGodly Armor Helmet", "", "").getStack();
    }

    @Override
    public IArmorModifier[] getModifiers() {
        return new IArmorModifier[] {new NightVisionModifier(), new AutoFeedModifier()};
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}
