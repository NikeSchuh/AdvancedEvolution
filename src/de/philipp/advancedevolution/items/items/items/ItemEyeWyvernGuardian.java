package de.philipp.advancedevolution.items.items.items;

import de.philipp.advancedevolution.events.customevents.DraconicItemInteractEvent;
import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.inventory.ItemStack;

public class ItemEyeWyvernGuardian extends DraconicItemBase {
    @Override
    public String getDataName() {
        return "WyvernGuardianEye";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§5Wyvern Guardian Eye", XMaterial.ENDER_EYE, "", "§7This item can be used", "§7to craft various", "§7utilities and upgrades", "", "§5Boss Drop");
    }

    @Override
    public boolean isStackable() {
        return false;
    }

    public void onInteract(DraconicItemInteractEvent event) {
        event.setCancelled(true);
    }
}
