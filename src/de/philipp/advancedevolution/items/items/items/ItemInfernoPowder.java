package de.philipp.advancedevolution.items.items.items;

import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.inventory.ItemStack;

public class ItemInfernoPowder extends DraconicItemBase {
    @Override
    public String getDataName() {
        return "InfernoPowder";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§cInferno Blaze Powder", XMaterial.BLAZE_POWDER, "", "§7This is even hotter", "§7then normal Blaze Powder.");
    }

    @Override
    public boolean isStackable() {
        return true;
    }
}
