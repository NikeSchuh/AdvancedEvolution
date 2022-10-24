package de.philipp.advancedevolution.blocks.blocks;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.blocks.*;
import de.philipp.advancedevolution.blocks.custombases.DraconicMachineBase;
import de.philipp.advancedevolution.entities.particles.EnergyOrb;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class DraconicSolarPanel1 extends DraconicMachineBase {

    public static final int generationRate = 500;
    private final double maxTransfer = generationRate * 20;

    private HashMap<Block, HashMap<Block, Boolean>> receiverMap = new HashMap<>();

    @Override
    public void onPlace(DraconicBlock block, Cancellable cancellable, Player player) {
        CrystalLinkManager crystalLinkManager = new CrystalLinkManager(new DraconicMachine(block.getBlock()));
        crystalLinkManager.createCrystalData(1);
    }

    @Override
    public void onBreak(DraconicBlock block, Cancellable cancellable, Player player) {
        EnergyInfo energyInfo = new EnergyInfo(new DraconicMachine(block.getBlock()));
        energyInfo.remove();
    }

    @Override
    public void onBlockInteract(Player player, Cancellable cancellable, DraconicBlock block) {
        cancellable.setCancelled(true);
    }

    @Override
    public double getEnergyCapacity() {
        return 100000d;
    }

    @Override
    public boolean isEnergyPullAble() {
        return true;
    }

    @Override
    public boolean isEnergyAcceptable() {
        return false;
    }

    @Override
    public void onEnergyReceive(DraconicMachine sender, DraconicMachine receiver, double amount, BlockFace senderDirection) {

    }

    @Override
    public boolean isRestricted() {
        return false;
    }

    @Override
    public void tickSync(Block block, DraconicMachine draconicMachine) {
        EnergyInfo energyInfo = new EnergyInfo(draconicMachine);
        CrystalLinkManager crystalLinkManager = new CrystalLinkManager(draconicMachine);
        crystalLinkManager.validateLinks();

        if(!receiverMap.containsKey(block)) {
            receiverMap.put(block, new HashMap<>());
        }


        for(Block block1 : ((HashMap<Block, Boolean>) receiverMap.get(block).clone()).keySet()) {
            if(!block1.getChunk().isLoaded()) continue;
            if(block1.isEmpty() || block1.getType() == Material.AIR) {
                receiverMap.get(block).remove(block1);
            }
        }

        for(DraconicMachine connected : crystalLinkManager.getMachineLinks()) {
            if(!connected.getBlock().getChunk().isLoaded()) continue;
            if(receiverMap.get(block).containsKey(connected.getBlock())) continue;
            if(draconicMachine.hasEnergy(maxTransfer)) {
                if (connected.isEnoughCapacityFor(maxTransfer)) {
                    sendEnergy(connected, draconicMachine, maxTransfer, null);
                    draconicMachine.removeEnergyStored(maxTransfer);
                } else {
                    if(connected.getEnergyStored() == connected.getEnergyCapacity()) continue;
                    draconicMachine.removeEnergyStored(connected.getEnergySpaceLeft());
                    sendEnergy(connected, draconicMachine, connected.getEnergySpaceLeft(), null);
                }
            } else {
                break;
            }

        }
        if(draconicMachine.getEnergyStored() == draconicMachine.getEnergyCapacity()) {
            return;
        }
        long time = block.getWorld().getTime();
        if(time < 12300 || time > 23850) {
            draconicMachine.addEnergyStored(generationRate);
        }
    }


    public boolean sendEnergy(DraconicMachine receiver, DraconicMachine sender, double amount, BlockFace face) {
        if(!receiver.getBlock().getChunk().isLoaded()) return false;
        if(receiver.isEnergyAcceptable() && sender.isEnergyPullAble()) {
            receiverMap.get(sender.getBlock()).put(receiver.getBlock(), true);
            new EnergyOrb(AdvancedEvolution.getInstance(), receiver, sender, amount, face, 6) {
                @Override
                public void end(DraconicMachine draconicMachine) {
                    receiverMap.get(sender.getBlock()).remove(receiver.getBlock());
                }
            };
            return true;
        }
        return false;
    }

    @Override
    public void tickAsync(Block block, DraconicMachine draconicMachine) {

    }

    @Override
    public void onUnload(DraconicBlock block) {

    }

    @Override
    public String getDataName() {
        return "DraconicSolarPanelI";
    }

    @Override
    public boolean isLinkManager() {
        return true;
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§cSolar Panel I", XMaterial.DAYLIGHT_DETECTOR);
    }

    @Override
    public boolean isStackable() {
        return true;
    }
}
