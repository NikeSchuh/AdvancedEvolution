package de.philipp.advancedevolution.items;

import de.philipp.advancedevolution.lib.xseries.XMaterial;
import org.bukkit.Material;

public interface IItemModifier {

    String getName();
    XMaterial getIcon();
    String[] getDescription();

    default Material getBukkitIcon() {
        return getIcon().parseMaterial();
    }

}
