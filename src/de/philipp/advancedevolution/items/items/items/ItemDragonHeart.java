package de.philipp.advancedevolution.items.items.items;

import de.philipp.advancedevolution.events.customevents.DraconicItemInteractEvent;
import de.philipp.advancedevolution.items.DraconicItem;
import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.inventory.ItemStack;

public class ItemDragonHeart extends DraconicItemBase {

    @Override
    public String getDataName() {
        return "DragonHeart";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§6Dragon Heart", XMaterial.ORANGE_DYE, "", "§7It pulses...", "", "§5Boss Drop");
    }

    @Override
    public boolean isStackable() {
        return true;
    }

    public void onInteract(DraconicItemInteractEvent event) {
        event.setCancelled(true);
    }
}
