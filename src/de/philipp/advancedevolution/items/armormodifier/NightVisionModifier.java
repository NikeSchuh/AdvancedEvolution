package de.philipp.advancedevolution.items.armormodifier;

import de.philipp.advancedevolution.items.IArmorModifier;
import de.philipp.advancedevolution.items.IItemBooleanModifier;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.menus.EditType;
import de.philipp.advancedevolution.util.item.ArmorType;

public class NightVisionModifier implements IItemBooleanModifier, IArmorModifier {
    @Override
    public String getAccessKey() {
        return "nightVision";
    }

    @Override
    public String getName() {
        return "Nightvision";
    }

    @Override
    public XMaterial getIcon() {
        return XMaterial.ENDER_EYE;
    }

    @Override
    public String[] getDescription() {
        return new String[] {"", "Determine if you want to see", "in the night"};
    }

    @Override
    public ArmorType getType() {
        return ArmorType.HELMET;
    }

    @Override
    public EditType getEditType() {
        return EditType.BOOLEAN_EDITOR;
    }
}
