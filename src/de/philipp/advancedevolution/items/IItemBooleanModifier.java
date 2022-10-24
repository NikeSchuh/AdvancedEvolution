package de.philipp.advancedevolution.items;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

public interface IItemBooleanModifier extends ICompoundModifier{

    default boolean getValue(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        if(nbtItem.hasKey(getAccessKey())) {
            return nbtItem.getBoolean(getAccessKey());
        } else {
            return false;
        }
    }

    default void setValue(boolean value, ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack, true);
        nbtItem.setBoolean(getAccessKey(), value);

    }


}
