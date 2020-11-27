package de.philipp.advancedevolution.items.armormodifier;

import com.google.gson.internal.$Gson$Preconditions;
import de.philipp.advancedevolution.items.ArmorModifier;
import de.philipp.advancedevolution.items.DraconicArmorType;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.menus.EditType;
import de.philipp.advancedevolution.util.item.ArmorType;
import org.bukkit.attribute.Attribute;

public class FlightSpeedModifier extends ArmorModifier {
    @Override
    public String getName() {
        return "FlightSpeed";
    }

    @Override
    public XMaterial getIcon() {
        return XMaterial.FEATHER;
    }

    @Override
    public String[] getDescription() {
        return new String[] {"", "This modifier represents the", "movement speed when flying in %"};
    }

    @Override
    public ArmorType getType() {
        return ArmorType.CHESTPLATE;
    }

    @Override
    public EditType getEditType() {
        return EditType.ATTRIBUTE_EDITOR;
    }

    @Override
    public Attribute getAttribute() {
        return Attribute.GENERIC_FLYING_SPEED;
    }


}
