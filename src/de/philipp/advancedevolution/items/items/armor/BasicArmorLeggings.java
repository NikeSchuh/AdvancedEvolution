package de.philipp.advancedevolution.items.items.armor;

import de.philipp.advancedevolution.items.ArmorItem;
import de.philipp.advancedevolution.items.ArmorModifier;
import de.philipp.advancedevolution.items.DraconicArmorType;
import de.philipp.advancedevolution.items.armormodifier.MovementSpeedModifier;
import de.philipp.advancedevolution.items.custombases.DraconicArmorItemBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.item.ArmorType;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

public class BasicArmorLeggings extends DraconicArmorItemBase {
    @Override
    public double getShieldCapacity() {
        return 6;
    }

    @Override
    public float getShieldRegeneration() {
        return 0.01f;
    }

    @Override
    public DraconicArmorType getDraconicArmorType() {
        return DraconicArmorType.BASIC;
    }

    @Override
    public double getEnergyCapacity() {
        return 256000;
    }

    @Override
    public String getDataName() {
        return "BasicArmorLeggings";
    }

    @Override
    public ItemStack getBaseStack() {
        return new ArmorItem(Color.BLUE, ArmorType.LEGGINGS, 0.05f, 6, 2,"§bBasic Armor Boots", "", "").getStack();
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
