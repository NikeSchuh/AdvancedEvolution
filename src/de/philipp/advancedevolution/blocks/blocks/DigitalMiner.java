package de.philipp.advancedevolution.blocks.blocks;

import de.philipp.advancedevolution.blocks.DraconicBlock;
import de.philipp.advancedevolution.blocks.DraconicMachine;
import de.philipp.advancedevolution.blocks.custombases.DraconicMachineBase;
import de.philipp.advancedevolution.util.Cuboid;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class DigitalMiner extends DraconicMachineBase {

    private HashMap<Block, Cuboid> region = new HashMap<>();

    @Override
    public void onPlace(DraconicBlock block, Cancellable cancellable, Player player) {

    }

    @Override
    public void onBreak(DraconicBlock block, Cancellable cancellable, Player player) {

    }

    @Override
    public void onBlockInteract(Player player, Cancellable cancellable, DraconicBlock block) {

    }

    @Override
    public double getEnergyCapacity() {
        return 100000;
    }

    @Override
    public boolean isEnergyPullAble() {
        return false;
    }

    @Override
    public boolean isEnergyAcceptable() {
        return true;
    }

    @Override
    public void onEnergyReceive(DraconicMachine sender, DraconicMachine receiver, double amount, BlockFace senderDirection) {
        receiver.addEnergyStored(amount);
    }

    @Override
    public boolean isRestricted() {
        return false;
    }

    @Override
    public void tickSync(Block block, DraconicMachine draconicMachine) {

    }

    @Override
    public void tickAsync(Block block, DraconicMachine draconicMachine) {

    }

    @Override
    public void onUnload(DraconicBlock block) {

    }

    @Override
    public String getDataName() {
        return "DigitalMiner";
    }

    @Override
    public ItemStack getBaseStack() {
        return null;
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}
