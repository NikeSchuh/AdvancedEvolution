package de.philipp.advancedevolution.items;

import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.menus.EditType;
import de.philipp.advancedevolution.util.item.ArmorType;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.ItemStack;

public interface IArmorModifier extends IItemModifier {

    ArmorType getType();
    EditType getEditType();

}
