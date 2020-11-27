package de.philipp.advancedevolution.items;

import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.menus.EditType;
import de.philipp.advancedevolution.util.item.ArmorType;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.ItemStack;

public abstract class ArmorModifier {

    public abstract String getName();
    public abstract XMaterial getIcon();
    public abstract String[] getDescription();
    public abstract ArmorType getType();
    public abstract EditType getEditType();
    public abstract Attribute getAttribute();


}
