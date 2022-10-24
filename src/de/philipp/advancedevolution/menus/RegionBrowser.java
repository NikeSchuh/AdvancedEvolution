package de.philipp.advancedevolution.menus;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.items.DraconicItem;
import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.region.DraconicRegion;
import de.philipp.advancedevolution.util.gui.PagedMenu;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class RegionBrowser extends PagedMenu<DraconicRegion> {

    public RegionBrowser(List<DraconicRegion> list) {
        super(AdvancedEvolution.getInstance(), list, 4 * 9, 60);
    }

    @Override
    public String getTitle() {
        return "§7Regions";
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
        return ItemUtil.createSimpleStack("§cBack", XMaterial.OAK_SIGN, "", "§aClick to go back to the", "§eAdmin Panel");
    }

    public void refreshRegions() {
        setList(AdvancedEvolution.getRegionManager().getRegions());
    }

    @Override
    public ItemStack getSearchButton() {
        return null;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        if(event.getCurrentItem() == null) return;
        if(event.getCurrentItem().equals(this.getForwardButton()) || event.getCurrentItem().equals(this.getBackwardButton())) return;
        if(event.getCurrentItem().equals(getGeneralBackButton())) {
            new AdminPanel(getMain(), (Player) event.getWhoClicked()).open();
            return;
        }
        DraconicRegion draconicRegion = (DraconicRegion) super.getObject(currentPage.get((Player)event.getWhoClicked()), event.getSlot());
        if(event.getClick() == ClickType.DROP) {
            AdvancedEvolution.getRegionManager().deregisterRegion(draconicRegion);
            refreshRegions();
            refreshInventory(player);
        } else {
            player.closeInventory();
            player.teleport(draconicRegion.getCenter());
        }
    }

    @Override
    public int getRows() {
        return 5;
    }


    @Override
    public ItemStack generateStack(Object obj) {
        DraconicRegion draconicRegion = (DraconicRegion) obj;
        return ItemUtil.createSimpleStack("§c" + draconicRegion.getDataName(), draconicRegion.getIcon(), "", "§7Blocks : §c" + draconicRegion.getRegion().getVolume(),"§7Allow Bosses : §c" + draconicRegion.isAllowBossSpawning(), "§7Allow Rituals : §c" + draconicRegion.isAllowRituals(), "", "§aDrop §7to delete", "§aClick §7to §5teleport");
    }
}
