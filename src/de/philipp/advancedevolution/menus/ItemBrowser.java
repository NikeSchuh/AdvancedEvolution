package de.philipp.advancedevolution.menus;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.blocks.custombases.DraconicMachineBase;
import de.philipp.advancedevolution.items.DraconicItem;
import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.items.custombases.DraconicArmorItemBase;
import de.philipp.advancedevolution.items.custombases.DraconicEnergyItemBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.gui.PagedMenu;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class ItemBrowser extends PagedMenu<DraconicItemBase> {


    public ItemBrowser(List<DraconicItemBase> list) {
        super(AdvancedEvolution.getInstance(), list, 4 * 9, 60);
    }

    @Override
    public String getTitle() {
        return "items";
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

    @Override
    public ItemStack getSearchButton() {
        return null;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if(event.getCurrentItem() == null) return;
        if(event.getCurrentItem().equals(this.getForwardButton()) || event.getCurrentItem().equals(this.getBackwardButton())) return;
        if(event.getCurrentItem().equals(getGeneralBackButton())) {
            new AdminPanel(getMain(), (Player) event.getWhoClicked()).open();
            return;
        }
        DraconicItemBase base = (DraconicItemBase) super.getObject(currentPage.get((Player) event.getWhoClicked()), event.getSlot());
        event.getWhoClicked().getInventory().addItem(DraconicItem.instantiateTrueItem(base.getDataName()).getCurrentStack());
    }

    @Override
    public int getRows() {
        return 5;
    }

    @Override
    public ItemStack generateStack(Object obj) {
       DraconicItemBase draconicItemBase = (DraconicItemBase) obj;
       ItemStack defaultStack = draconicItemBase.getBaseStack();
       ItemStack displayStack = new ItemStack(defaultStack.getType(), 1);
       ItemMeta meta = defaultStack.getItemMeta();
       List<String> lore = new ArrayList<>();
       meta.setDisplayName(defaultStack.getItemMeta().getDisplayName());
       lore.add("");
       lore.add("§7Item ID: §c" + draconicItemBase.getDataName());
       lore.add("§7Craftable: §c" + draconicItemBase.isCraftAble());
       if(draconicItemBase instanceof DraconicArmorItemBase) {
           DraconicArmorItemBase base = (DraconicArmorItemBase) draconicItemBase;
           lore.add("§7Default Shield: §c" + base.getShieldCapacity());
           lore.add("§7Default Shield Reg: §c" + base.getShieldRegeneration());
           lore.add("§7Armor Tier: §c" + base.getDraconicArmorType().name().toLowerCase());
       }
        if(draconicItemBase instanceof DraconicEnergyItemBase) {
            DraconicEnergyItemBase base = (DraconicEnergyItemBase) draconicItemBase;
            lore.add("§7Default Energy Capacity: §c" + base.getEnergyCapacity());
        }
        if(draconicItemBase instanceof DraconicMachineBase) {
            DraconicMachineBase base = (DraconicMachineBase) draconicItemBase;
            lore.add("§7Link Manager: §c" + base.isLinkManager());
            lore.add("§7Energy Capacity: §c" + base.getEnergyCapacity());
        }

       meta.setLore(lore);
       displayStack.setItemMeta(meta);
       return displayStack;
    }
}
