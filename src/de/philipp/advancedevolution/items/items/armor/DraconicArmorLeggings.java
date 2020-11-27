package de.philipp.advancedevolution.items.items.armor;

import de.philipp.advancedevolution.items.ArmorItem;
import de.philipp.advancedevolution.items.ArmorModifier;
import de.philipp.advancedevolution.items.DraconicArmorType;
import de.philipp.advancedevolution.items.armormodifier.MovementSpeedModifier;
import de.philipp.advancedevolution.items.custombases.DraconicArmorItemBase;
import de.philipp.advancedevolution.util.item.ArmorType;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

public class DraconicArmorLeggings extends DraconicArmorItemBase {
    @Override
    public double getShieldCapacity() {
        return 90;
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
        return "DraconicArmorLeggings";
    }

    @Override
    public ItemStack getBaseStack() {
        return new ArmorItem(Color.ORANGE, ArmorType.LEGGINGS, 0.15f, 8, 4, "§6Draconic Armor Leggings", "", "").getStack();
    }

    @Override
    public ArmorModifier[] getModifiers() {
        return new ArmorModifier[] {new MovementSpeedModifier()};
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}
