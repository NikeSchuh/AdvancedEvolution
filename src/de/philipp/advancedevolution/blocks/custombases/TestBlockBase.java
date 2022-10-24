package de.philipp.advancedevolution.blocks.custombases;

import de.philipp.advancedevolution.blocks.DraconicBlock;
import de.philipp.advancedevolution.blocks.DraconicBlockBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

public class TestBlockBase extends DraconicBlockBase {


    @Override
    public void onPlace(DraconicBlock block, Cancellable cancellable, Player player) {

    }

    @Override
    public void onBreak(DraconicBlock block, Cancellable cancellable, Player player) {

    }

    @Override
    public void onBlockInteract(Player player, Cancellable cancellable, DraconicBlock block) {
        cancellable.setCancelled(true);
        player.sendMessage("Interacted with " + block);
    }


    @Override
    public String getDataName() {
        return "TestBlock";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("Test", XMaterial.FURNACE, "§cLul");
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}
