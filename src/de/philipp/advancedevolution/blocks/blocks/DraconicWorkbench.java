package de.philipp.advancedevolution.blocks.blocks;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.blocks.DraconicBlock;
import de.philipp.advancedevolution.blocks.DraconicBlockBase;
import de.philipp.advancedevolution.crafting.DraconicCrafterGUI;
import de.philipp.advancedevolution.items.ItemRegistry;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;

public class DraconicWorkbench extends DraconicBlockBase {

    public static HashMap<Block, Boolean> craftingMap = new HashMap<>();

    @Override
    public void onPlace(DraconicBlock block, Cancellable cancellable, Player player) {

    }

    @Override
    public void onBreak(DraconicBlock block, Cancellable cancellable, Player player) {
        if(craftingMap.containsKey(block.getBlock())) {
            craftingMap.remove(block.getBlock());
        }
    }

    @Override
    public void onBlockInteract(Player player, Cancellable cancellable, DraconicBlock block) {
        cancellable.setCancelled(true);
       // new DraconicWorkbenchGUI(AdvancedEvolution.getInstance(), player).open();
        if(craftingMap.getOrDefault(block.getBlock(), false)) {
            player.sendMessage("§cCurrently crafting!");
            return;
        }
        new DraconicCrafterGUI(Arrays.asList(ItemRegistry.getRegisteredCraftableItemsSorted()), AdvancedEvolution.getInstance(), player, block);
    }

    @Override
    public String getDataName() {
        return "DraconicWorkbench";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§cDraconic Forge", XMaterial.LECTERN);
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}
