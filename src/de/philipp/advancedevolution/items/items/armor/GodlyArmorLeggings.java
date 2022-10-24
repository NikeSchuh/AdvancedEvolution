package de.philipp.advancedevolution.items.items.armor;

import de.philipp.advancedevolution.items.ArmorItem;
import de.philipp.advancedevolution.items.IArmorModifier;
import de.philipp.advancedevolution.items.DraconicArmorType;
import de.philipp.advancedevolution.items.armormodifier.MovementSpeedModifier;
import de.philipp.advancedevolution.items.custombases.DraconicArmorItemBase;
import de.philipp.advancedevolution.util.item.ArmorType;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

public class GodlyArmorLeggings extends DraconicArmorItemBase {
    @Override
    public double getShieldCapacity() {
        return 2880;
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
        return "GodlyArmorLeggings";
    }

    @Override
    public ItemStack getBaseStack() {
        return new ArmorItem(Color.RED, ArmorType.LEGGINGS, 0.25f, 40, 30, "§cGodly Armor Leggings", "", "").getStack();
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
