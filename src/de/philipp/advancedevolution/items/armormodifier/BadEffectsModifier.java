package de.philipp.advancedevolution.items.armormodifier;

import de.philipp.advancedevolution.items.IArmorModifier;
import de.philipp.advancedevolution.items.IItemBooleanModifier;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.menus.EditType;
import de.philipp.advancedevolution.util.item.ArmorType;

public class BadEffectsModifier implements IItemBooleanModifier, IArmorModifier {
    @Override
    public String getAccessKey() {
        return "badEffects";
    }

    @Override
    public String getName() {
        return "Bad Effects";
    }

    @Override
    public XMaterial getIcon() {
        return XMaterial.POTION;
    }

    @Override
    public String[] getDescription() {
        return new String[] {"", "Should bad effects be removed"};
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
