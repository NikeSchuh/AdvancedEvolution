package de.philipp.advancedevolution.blocks.blocks;

import de.philipp.advancedevolution.blocks.DraconicBlock;
import de.philipp.advancedevolution.blocks.DraconicMachine;
import de.philipp.advancedevolution.blocks.EnergyInfo;
import de.philipp.advancedevolution.blocks.InventoryContainer;
import de.philipp.advancedevolution.blocks.custombases.DraconicMachineBase;
import de.philipp.advancedevolution.items.DraconicItem;
import de.philipp.advancedevolution.lib.xseries.XItemStack;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.lib.xseries.XSound;
import de.philipp.advancedevolution.recipes.EnhancedRecipe;
import de.philipp.advancedevolution.recipes.VanillaIngredient;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.Particle;
import org.bukkit.block.BlastFurnace;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

public class DraconicMiner extends DraconicMachineBase {
    @Override
    public void onPlace(DraconicBlock block, Cancellable cancellable, Player player) {
        InventoryContainer container = new InventoryContainer(block);
        container.createInventory("Draconic Farmer", 9 * 3);
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
        return 5000;
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
        return "DraconiumMiner1";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§cDraconium Miner I", XMaterial.BLAST_FURNACE);
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
    public EnhancedRecipe getCraftingRecipe() {
        EnhancedRecipe recipe = new EnhancedRecipe();
        recipe.addIngredient(new VanillaIngredient(XMaterial.IRON_INGOT.parseMaterial(), 32));
        recipe.addIngredient(new VanillaIngredient(XMaterial.GOLD_INGOT.parseMaterial(), 16));
        recipe.addIngredient(new VanillaIngredient(XMaterial.ENDER_PEARL.parseMaterial(), 5));
        recipe.addIngredient(new VanillaIngredient(XMaterial.DIAMOND.parseMaterial(), 4));
        recipe.addIngredient(new VanillaIngredient(XMaterial.REDSTONE_BLOCK.parseMaterial(), 4));
        recipe.addIngredient(new VanillaIngredient(XMaterial.DIAMOND_PICKAXE.parseMaterial(), 3));
        recipe.addIngredient(new VanillaIngredient(XMaterial.ENDER_EYE.parseMaterial(), 1));
        recipe.addIngredient(new VanillaIngredient(XMaterial.CHEST.parseMaterial(), 1));
        return recipe;
    }

    @Override
    public long getSyncInterval() {
        return 50;
    }

    @Override
    public void tickSync(Block block, DraconicMachine farmer) {
        InventoryContainer container = new InventoryContainer(farmer);
        EnergyInfo energyInfo = new EnergyInfo(farmer);

        if(farmer.hasEnergy(2500)) {
            if (Math.random() < 0.0050) {
                farmer.removeEnergyStored(2500);
                block.getWorld().playSound(block.getLocation(), XSound.BLOCK_BEACON_DEACTIVATE.parseSound(), 0.1f, 0.5f);
                block.getWorld().spawnParticle(Particle.CRIT_MAGIC, block.getLocation().add(0.5, 1, 0.5), 20, 0.25, 0, 0.25, 0.05);
                container.getInventory().addItem(DraconicItem.instantiateTrueItem("DraconiumDust").getCurrentStack());
            }
        }
    }



    @Override
    public void tickAsync(Block block, DraconicMachine farmer) {
    }
}
