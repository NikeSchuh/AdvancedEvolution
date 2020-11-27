package de.philipp.advancedevolution.menus;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.items.custombases.DraconicArmorItemBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.gui.PagedMenu;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class ItemBrowser extends PagedMenu<DraconicItemBase> {


    public ItemBrowser(List<DraconicItemBase> list) {
        super(AdvancedEvolution.getInstance(), list, 36, 60);
    }

    @Override
    public String getTitle() {
        return AdvancedEvolution.ChatPrefix;
    }

    @Override
    public ItemStack getForwardButton() {
        return ItemUtil.createSimpleStack("§aNext", XMaterial.GREEN_DYE);
    }

    @Override
    public ItemStack getBackwardButton() {
        return ItemUtil.createSimpleStack("§cBack", XMaterial.GREEN_DYE);
    }

    @Override
    public ItemStack getGeneralBackButton() {
        return null;
    }

    @Override
    public ItemStack getSearchButton() {
        return null;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        DraconicItemBase base = (DraconicItemBase) super.getObject(currentPage.get((Player) event.getWhoClicked()), event.getSlot());
    }

    @Override
    public int getRows() {
        return 5;
    }

    @Override
    public ItemStack generateStack(Object obj) {
        return ((DraconicItemBase) obj).getBaseStack();
    }
}
