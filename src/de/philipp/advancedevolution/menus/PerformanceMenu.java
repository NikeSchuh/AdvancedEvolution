package de.philipp.advancedevolution.menus;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.blocks.BlockManager;
import de.philipp.advancedevolution.blocks.DraconicBlock;
import de.philipp.advancedevolution.blocks.DraconicMachine;
import de.philipp.advancedevolution.blocks.custombases.DraconicMachineBase;
import de.philipp.advancedevolution.items.DraconicItem;
import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.gui.PagedMenu;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class PerformanceMenu extends PagedMenu<Block> {

    public PerformanceMenu() {
        super(AdvancedEvolution.getInstance(), BlockManager.blocks, 4 * 9, 5);
    }

    @Override
    public String getTitle() {
        return "Performance";
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
        Block bl = (Block) super.getObject(currentPage.get((Player) event.getWhoClicked()), event.getSlot());
        DraconicBlock block = new DraconicBlock(bl);
        event.getWhoClicked().teleport(block.getBlock().getLocation());
    }


    @Override
    public int getRows() {
        return 5;
    }

    @Override
    public ItemStack generateStack(Object obj) {
        try {
            Block block = (Block) obj;
            DraconicBlock draconicBlock = new DraconicBlock(block);
                return ItemUtil.createSimpleStack(draconicBlock.getBlockBase().getBaseStack().getItemMeta().getDisplayName(), draconicBlock.getBlock().getType(), "", "§7Coords : §5x" + draconicBlock.getBlock().getLocation().getBlockX() + " y" + draconicBlock.getBlock().getLocation().getBlockY() + " z" + draconicBlock.getBlock().getLocation().getBlockZ());
        }catch(Exception e) {
            return ItemUtil.createSimpleStack("§cInvalid Block", XMaterial.BARRIER.parseMaterial(), "", "§7This block thrown an exception", "§7and is scheduled to be removed." + "§4" + e.getCause());
        }
    }
}
