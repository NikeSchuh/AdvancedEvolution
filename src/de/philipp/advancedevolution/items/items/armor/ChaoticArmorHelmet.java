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

public class ChaoticArmorHelmet extends DraconicArmorItemBase {
    @Override
    public double getShieldCapacity() {
        return 136;
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
        return "ChaoticArmorHelmet";
    }

    @Override
    public ItemStack getBaseStack() {
        return new ArmorItem(Color.BLACK, ArmorType.HELMET, 16, 10, "§0Chaotic Armor Helmet", "", "").getStack();
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
