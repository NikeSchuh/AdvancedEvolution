package de.philipp.advancedevolution.blocks.blocks;

import de.philipp.advancedevolution.blocks.DraconicBlock;
import de.philipp.advancedevolution.blocks.DraconicMachine;
import de.philipp.advancedevolution.blocks.EnergyInfo;
import de.philipp.advancedevolution.blocks.InventoryContainer;
import de.philipp.advancedevolution.blocks.custombases.DraconicMachineBase;
import de.philipp.advancedevolution.items.DraconicItem;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.lib.xseries.XSound;
import de.philipp.advancedevolution.recipes.DraconicIngredient;
import de.philipp.advancedevolution.recipes.EnhancedRecipe;
import de.philipp.advancedevolution.recipes.VanillaIngredient;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

public class DraconicMiner3 extends DraconicMachineBase {
    @Override
    public void onPlace(DraconicBlock block, Cancellable cancellable, Player player) {
        InventoryContainer container = new InventoryContainer(block);
        container.createInventory("Draconic Miner III", 9 * 5);
        EnergyInfo energyInfo = new EnergyInfo(new DraconicMachine(block.getBlock()));
    }

    @Override
    public void onBreak(DraconicBlock block, Cancellable cancellable, Player player) {
        EnergyInfo energyInfo = new EnergyInfo(new DraconicMachine(block.getBlock()));
        energyInfo.remove();
        InventoryContainer container = new InventoryContainer(new DraconicMachine(block.getBlock()));
        container.syncInventoryWithContainer();
    }

    @Override
    public void onBlockInteract(Player player, Cancellable cancellable, DraconicBlock block) {
        cancellable.setCancelled(true);
        InventoryContainer container = new InventoryContainer(block);
        container.syncInventoryWithContainer();
        player.openInventory(container.getInventory());
    }

    @Override
    public double getEnergyCapacity() {
        return 1000000;
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
        EnergyInfo energyInfo = new EnergyInfo(receiver);
    }


    @Override
    public boolean isRestricted() {
        return false;
    }

    @Override
    public String getDataName() {
        return "DraconiumMiner3";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§cDraconium Miner III", XMaterial.BLAST_FURNACE);
    }

    @Override
    public boolean isStackable() {
        return true;
    }

    @Override
    public void onUnload(DraconicBlock block) {
        InventoryContainer container = new InventoryContainer(new DraconicMachine(block.getBlock()));
        container.syncInventoryWithContainer();
    }

    @Override
    public long getSyncInterval() {
        return 50;
    }

    @Override
    public void tickSync(Block block, DraconicMachine farmer) {
        InventoryContainer container = new InventoryContainer(farmer);
        EnergyInfo energyInfo = new EnergyInfo(farmer);
        if(farmer.hasEnergy(512)) {
            farmer.removeEnergyStored(512);
        }
        if(farmer.hasEnergy(2000)) {
            if (Math.random() < 0.25) {
                farmer.removeEnergyStored(2000);
                block.getWorld().playSound(block.getLocation(), XSound.BLOCK_BEACON_DEACTIVATE.parseSound(), 0.1f, 0.8f);
                block.getWorld().spawnParticle(Particle.CRIT_MAGIC, block.getLocation().add(0.5, 1, 0.5), 50, 0.25, 0, 0.25, 0.05);
                container.getInventory().addItem(DraconicItem.instantiateTrueItem("DraconiumDust").getCurrentStack());
            }
        }
    }

    @Override
    public void tickAsync(Block block, DraconicMachine farmer) {
    }

    @Override
    public EnhancedRecipe getCraftingRecipe() {
        EnhancedRecipe recipe = new EnhancedRecipe();
        recipe.addIngredient(new DraconicIngredient("IngotAwakenedDraconium", 10));
        recipe.addIngredient(new DraconicIngredient("InfernoPowder", 8));
        recipe.addIngredient(new DraconicIngredient("AwakenedCore", 4));
        recipe.addIngredient(new DraconicIngredient("WyvernCore", 2));
        recipe.addIngredient(new DraconicIngredient("DraconiumMiner2", 2));
        return recipe;
    }
}
