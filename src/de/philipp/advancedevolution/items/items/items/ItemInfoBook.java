package de.philipp.advancedevolution.items.items.items;

import de.philipp.advancedevolution.events.customevents.DraconicItemInteractEvent;
import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.inventory.ItemStack;

public class ItemInfoBook extends DraconicItemBase {
    @Override
    public String getDataName() {
        return "InfoBook";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§aDraconic Book", XMaterial.BOOK, "", "§7Right click in the Hand", "§7to open.");
    }

    @Override
    public boolean isStackable() {
        return false;
    }

    @Override
    public void onInteract(DraconicItemInteractEvent event) {


    }
}
