package de.philipp.advancedevolution.items.armormodifier;

import de.philipp.advancedevolution.items.IArmorModifier;
import de.philipp.advancedevolution.items.IItemBooleanModifier;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.menus.EditType;
import de.philipp.advancedevolution.util.item.ArmorType;

public class FlightEnabledModifier implements IItemBooleanModifier, IArmorModifier {
    @Override
    public String getAccessKey() {
        return "flight";
    }

    @Override
    public String getName() {
        return "Flight";
    }

    @Override
    public XMaterial getIcon() {
        return XMaterial.FEATHER;
    }

    @Override
    public String[] getDescription() {
        return new String[] {"", "If enabled you can fly if not" , "you wont be able to fly"};
    }

    @Override
    public ArmorType getType() {
        return ArmorType.CHESTPLATE;
    }

    @Override
    public EditType getEditType() {
        return EditType.BOOLEAN_EDITOR;
    }
}
