package de.philipp.advancedevolution.items;

import de.philipp.advancedevolution.items.IArmorModifier;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

public interface IPercentageModifier extends ICompoundModifier{

    double getDefaultValue();
    double getMaximumValue();
    double getMinimumValue();

    default double getValue(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        if(nbtItem.hasKey(getAccessKey())) {
            return nbtItem.getDouble(getAccessKey());
        } else {
            return getDefaultValue();
        }
    }

    default void setValue(double value, ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack, true);
        nbtItem.setDouble(getAccessKey(), value);
    }

}
