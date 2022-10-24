package de.philipp.advancedevolution.items;

import de.philipp.advancedevolution.lib.xseries.XMaterial;
import org.bukkit.Material;

public enum SwordLevel {

    WOOD(XMaterial.WOODEN_SWORD),
    STONE(XMaterial.STONE_SWORD),
    IRON(XMaterial.IRON_SWORD),
    DIAMOND(XMaterial.DIAMOND_SWORD),
    NETHERITE(XMaterial.NETHERITE_SWORD);

    private XMaterial xMaterial;

    SwordLevel(XMaterial xMaterial) {
        this.xMaterial = xMaterial;
    }

    public Material getMaterial() {
        if(xMaterial.parseMaterial() == null) {
            return Material.STONE;
        } else return xMaterial.parseMaterial();
    }

}
