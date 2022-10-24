package de.philipp.advancedevolution.items.items.items;

import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.inventory.ItemStack;

public class ItemChaosShard extends DraconicItemBase {

    @Override
    public String getDataName() {
        return "ChaosShard";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§cChaos Shard", XMaterial.BLACK_STAINED_GLASS, "", "§7Used to construct", "§7powerful cores and more.", "", "§5Boss Drop");
    }

    @Override
    public boolean isStackable() {
        return true;
    }
}
