package de.philipp.advancedevolution.items.armormodifier;

import de.philipp.advancedevolution.items.IArmorModifier;
import de.philipp.advancedevolution.items.IItemBooleanModifier;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.menus.EditType;
import de.philipp.advancedevolution.util.item.ArmorType;

public class AutoFeedModifier implements IItemBooleanModifier, IArmorModifier {
    @Override
    public String getAccessKey() {
        return "autofeed";
    }

    @Override
    public String getName() {
        return "Autofeed";
    }

    @Override
    public XMaterial getIcon() {
        return XMaterial.APPLE;
    }

    @Override
    public String[] getDescription() {
        return new String[] {"", "Should your helmet automatically", "feed you if it senses your hunger"};
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
