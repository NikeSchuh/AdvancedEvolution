package de.philipp.advancedevolution.blocks.blocks;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.blocks.CrystalLinkManager;
import de.philipp.advancedevolution.blocks.DraconicBlock;
import de.philipp.advancedevolution.blocks.DraconicMachine;
import de.philipp.advancedevolution.blocks.EnergyInfo;
import de.philipp.advancedevolution.blocks.custombases.DraconicMachineBase;
import de.philipp.advancedevolution.entities.particles.EnergyOrb;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.recipes.DraconicIngredient;
import de.philipp.advancedevolution.recipes.EnhancedRecipe;
import de.philipp.advancedevolution.recipes.VanillaIngredient;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DraconicEnergyStorage3 extends DraconicMachineBase {

    private List<Block> receivers = new ArrayList<>();
    private final double maxTransfer = 300000;

    @Override
    public void onUnload(DraconicBlock block) {

    }

    @Override
    public void onPlace(DraconicBlock block, Cancellable cancellable, Player player) {
        EnergyInfo energyInfo = new EnergyInfo(new DraconicMachine(block.getBlock()));
        CrystalLinkManager crystalLinkManager = new CrystalLinkManager(new DraconicMachine(block.getBlock()));
        crystalLinkManager.createCrystalData(64);
    }

    @Override
    public void onBreak(DraconicBlock block, Cancellable cancellable, Player player) {
        EnergyInfo energyInfo = new EnergyInfo(new DraconicMachine(block.getBlock()));
        energyInfo.remove();
    }

    @Override
    public void onBlockInteract(Player player, Cancellable cancellable, DraconicBlock block) {
        EnergyInfo energyInfo = new EnergyInfo(new DraconicMachine(block.getBlock()));
        cancellable.setCancelled(true);
        if(player.getInventory().getItemInMainHand() == null) {
            CrystalLinkManager crystalLinkManager = new CrystalLinkManager(new DraconicMachine(block.getBlock()));
            player.sendMessage("§aLinks: " + crystalLinkManager.getCurrentLinks() + " / " + crystalLinkManager.getMaxLinks());
        } else if(player.getInventory().getItemInMainHand().getType() == XMaterial.AIR.parseMaterial()) {
            CrystalLinkManager crystalLinkManager = new CrystalLinkManager(new DraconicMachine(block.getBlock()));
            player.sendMessage("§aLinks: " + crystalLinkManager.getCurrentLinks() + " / " + crystalLinkManager.getMaxLinks());
        }
    }

    @Override
    public EnhancedRecipe getCraftingRecipe() {
        EnhancedRecipe recipe = new EnhancedRecipe();
        recipe.addIngredient(new VanillaIngredient(XMaterial.REDSTONE_BLOCK.parseMaterial(), 16));
        recipe.addIngredient(new VanillaIngredient(XMaterial.REPEATER.parseMaterial(), 4));
        recipe.addIngredient(new DraconicIngredient("DraconicEnergyStorage2", 4));
        recipe.addIngredient(new DraconicIngredient("AwakenedCore", 2));
        return recipe;
    }

    @Override
    public double getEnergyCapacity() {
        return 10000000000d;
    }

    @Override
    public boolean isEnergyPullAble() {
        return true;
    }

    @Override
    public boolean isEnergyAcceptable() {
        return true;
    }

    @Override
    public boolean isLinkManager() {
        return true;
    }

    @Override
    public void onEnergyReceive(DraconicMachine sender, DraconicMachine receiver, double amount, BlockFace senderDirection) {
        receiver.addEnergyStored(amount);
        EnergyInfo energyInfo = new EnergyInfo(new DraconicMachine(receiver.getBlock()));
    }

    @Override
    public boolean isRestricted() {
        return false;
    }

    public long getSyncInterval() {
        return 30;
    }

    public boolean sendEnergy(DraconicMachine receiver, DraconicMachine sender, double amount, BlockFace face) {
        if(!receiver.getBlock().getChunk().isLoaded()) return false;
        double distance = receiver.getBlock().getLocation().distance(sender.getBlock().getLocation());
        if(receiver.isEnergyAcceptable() && sender.isEnergyPullAble()) {
            new EnergyOrb(AdvancedEvolution.getInstance(), receiver, sender, amount, face, distance / 2) {
                @Override
                public void end(DraconicMachine draconicMachine) {
                    receivers.remove(draconicMachine.getBlock());
                }
            };
            return true;
        }
        return false;
    }

    @Override
    public void tickSync(Block block, DraconicMachine storage) {
        CrystalLinkManager crystalLinkManager = new CrystalLinkManager(storage);
        crystalLinkManager.validateLinks();

        if(storage.hasEnergy(1)) {
            for(Block block1 : receivers.toArray(new Block[receivers.size()])) {
                if(!block1.getChunk().isLoaded()) continue;
                if(block1.isEmpty() || block1.getType() == Material.AIR) {
                    receivers.remove(block1);
                }
            }
            for(DraconicMachine connected : crystalLinkManager.getMachineLinks()) {
                if(!connected.getBlock().getChunk().isLoaded()) continue;
                if (receivers.contains(connected.getBlock())) continue;
                if (storage.hasEnergy(maxTransfer)) {
                    if (connected.isEnoughCapacityFor(maxTransfer)) {
                        sendEnergy(connected, storage, maxTransfer, null);
                        storage.removeEnergyStored(maxTransfer);
                    } else {
                        if (connected.getEnergyStored() == connected.getEnergyCapacity()) continue;
                        storage.removeEnergyStored(connected.getEnergySpaceLeft());
                        sendEnergy(connected, storage, connected.getEnergySpaceLeft(), null);
                    }
                    receivers.add(connected.getBlock());
                } else {
                    double transfer = storage.getEnergyStored();
                    if (connected.isEnoughCapacityFor(transfer)) {
                        sendEnergy(connected, storage, transfer, null);
                        receivers.add(connected.getBlock());
                        storage.removeEnergyStored(transfer);
                        break;
                    } else {
                        if (connected.getEnergyStored() == connected.getEnergyCapacity()) continue;
                        storage.removeEnergyStored(connected.getEnergySpaceLeft());
                        sendEnergy(connected, storage, connected.getEnergySpaceLeft(), null);
                        receivers.add(connected.getBlock());
                        continue;
                    }
                }

            }
        }
        EnergyInfo energyInfo = new EnergyInfo(new DraconicMachine(block));
    }

    @Override
    public void tickAsync(Block block, DraconicMachine draconicMachine) {

    }

    @Override
    public String getDataName() {
        return "DraconicEnergyStorage3";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§cEnergy Storage III", XMaterial.BARREL);
    }

    @Override
    public boolean isStackable() {
        return true;
    }
}
