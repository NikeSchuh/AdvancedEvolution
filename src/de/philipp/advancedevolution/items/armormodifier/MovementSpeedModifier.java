package de.philipp.advancedevolution.items.armormodifier;

import de.philipp.advancedevolution.items.ArmorModifier;
import de.philipp.advancedevolution.items.DraconicArmorType;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.menus.EditType;
import de.philipp.advancedevolution.util.item.ArmorType;
import org.bukkit.attribute.Attribute;

public class MovementSpeedModifier extends ArmorModifier {

    @Override
    public String getName() {
        return "Movement Speed";
    }

    @Override
    public XMaterial getIcon() {
        return XMaterial.LEATHER_BOOTS;
    }

    @Override
    public String[] getDescription() {
        return new String[] {"", "This modifier represents the", "movement speed in %"};
    }

    @Override
    public EditType getEditType() {
        return EditType.ATTRIBUTE_EDITOR;
    }

    @Override
    public ArmorType getType() {
        return ArmorType.LEGGINGS;
    }

    @Override
    public Attribute getAttribute() {
        return Attribute.GENERIC_MOVEMENT_SPEED;
    }


}
