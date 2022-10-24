package de.philipp.advancedevolution.items.armormodifier;

import de.philipp.advancedevolution.items.DraconicArmorType;
import de.philipp.advancedevolution.items.IArmorAttibuteModifier;
import de.philipp.advancedevolution.items.IPercentageModifier;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.menus.EditType;
import de.philipp.advancedevolution.util.item.ArmorType;
import org.bukkit.attribute.Attribute;

public class FlightSpeedModifier implements IArmorAttibuteModifier, IPercentageModifier {

    private final DraconicArmorType armorType;

    public FlightSpeedModifier(DraconicArmorType armorType) {
        this.armorType = armorType;
    }

    @Override
    public String getName() {
        return "Flight Speed";
    }

    @Override
    public XMaterial getIcon() {
        return XMaterial.ELYTRA;
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
        return EditType.PERCENTAGE_EDITOR;
    }

    @Override
    public Attribute getAttribute() {
        return Attribute.GENERIC_FLYING_SPEED;
    }


    @Override
    public double getDefaultValue() {
        return 0;
    }

    @Override
    public double getMaximumValue() {
        return 3 + armorType.getUpgradeLevel();
    }

    @Override
    public double getMinimumValue() {
        return 0.0d;
    }

    @Override
    public String getAccessKey() {
        return "flySpeed";
    }
}
