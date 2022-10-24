package de.philipp.advancedevolution.items.items.items;

import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.inventory.ItemStack;

public class ItemAwakenedDraconium extends DraconicItemBase {
    @Override
    public String getDataName() {
        return "IngotAwakenedDraconium";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§6Awakened Draconium", XMaterial.BRICK, "", "§7Even stronger than", "§7draconium", "");
    }

    @Override
    public boolean isStackable() {
        return true;
    }
}
