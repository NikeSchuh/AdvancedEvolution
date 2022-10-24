package de.philipp.advancedevolution.items.armormodifier;

import de.philipp.advancedevolution.items.IArmorAttibuteModifier;
import de.philipp.advancedevolution.items.IPercentageModifier;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.menus.EditType;
import de.philipp.advancedevolution.util.item.ArmorType;
import org.bukkit.attribute.Attribute;

public class JumpHeightModifier implements IPercentageModifier, IArmorAttibuteModifier {
    @Override
    public double getDefaultValue() {
        return 0;
    }

    @Override
    public double getMaximumValue() {
        return 0;
    }

    @Override
    public double getMinimumValue() {
        return 0;
    }

    @Override
    public String getAccessKey() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public XMaterial getIcon() {
        return null;
    }

    @Override
    public String[] getDescription() {
        return new String[] {"", "Determines how high you should jump"};
    }

    @Override
    public ArmorType getType() {
        return ArmorType.BOOTS;
    }

    @Override
    public EditType getEditType() {
        return EditType.PERCENTAGE_EDITOR;
    }

    @Override
    public Attribute getAttribute() {
        return Attribute.HORSE_JUMP_STRENGTH;
    }
}
