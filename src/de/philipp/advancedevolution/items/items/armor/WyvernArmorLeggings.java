package de.philipp.advancedevolution.items.items.armor;

import de.philipp.advancedevolution.items.ArmorItem;
import de.philipp.advancedevolution.items.ArmorModifier;
import de.philipp.advancedevolution.items.DraconicArmorType;
import de.philipp.advancedevolution.items.armormodifier.MovementSpeedModifier;
import de.philipp.advancedevolution.items.custombases.DraconicArmorItemBase;
import de.philipp.advancedevolution.util.item.ArmorType;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

public class WyvernArmorLeggings extends DraconicArmorItemBase {
    @Override
    public double getShieldCapacity() {
        return 42;
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
        return "WyvernArmorLeggings";
    }

    @Override
    public ItemStack getBaseStack() {
        return new ArmorItem(Color.PURPLE, ArmorType.LEGGINGS, 0.1f, 6, 3, "§5Wyvern Armor Leggings", "", "").getStack();
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
