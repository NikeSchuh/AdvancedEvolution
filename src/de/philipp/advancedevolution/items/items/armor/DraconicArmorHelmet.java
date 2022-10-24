package de.philipp.advancedevolution.items.items.armor;

import de.philipp.advancedevolution.items.ArmorItem;
import de.philipp.advancedevolution.items.DraconicArmorType;
import de.philipp.advancedevolution.items.IArmorModifier;
import de.philipp.advancedevolution.items.armormodifier.AutoFeedModifier;
import de.philipp.advancedevolution.items.armormodifier.NightVisionModifier;
import de.philipp.advancedevolution.items.custombases.DraconicArmorItemBase;
import de.philipp.advancedevolution.util.item.ArmorType;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

public class DraconicArmorHelmet extends DraconicArmorItemBase {
    @Override
    public double getShieldCapacity() {
        return 34;
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
        return "DraconicArmorHelmet";
    }

    @Override
    public ItemStack getBaseStack() {
        return new ArmorItem(Color.ORANGE, ArmorType.HELMET, 4, 4, "§6Draconic Armor Helmet", "", "").getStack();
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
