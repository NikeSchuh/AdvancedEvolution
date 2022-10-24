package de.philipp.advancedevolution.blocks.blocks;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.blocks.*;
import de.philipp.advancedevolution.blocks.custombases.DraconicMachineBase;
import de.philipp.advancedevolution.items.DraconicEnergyItem;
import de.philipp.advancedevolution.items.DraconicItem;
import de.philipp.advancedevolution.items.custombases.DraconicEnergyItemBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.lib.xseries.XSound;
import de.philipp.advancedevolution.recipes.DraconicIngredient;
import de.philipp.advancedevolution.recipes.EnhancedRecipe;
import de.philipp.advancedevolution.recipes.VanillaIngredient;
import de.philipp.advancedevolution.shield.Shield;
import de.philipp.advancedevolution.shield.ShieldManager;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;

import java.util.HashMap;
import java.util.UUID;

public class DraconicCharger extends DraconicMachineBase implements ITickAbleBlock{

    public HashMap<UUID, Inventory> inventoryMap;
    public static final double maxTransfer = 5000;

    @Override
    public void onPlace(DraconicBlock block, Cancellable cancellable, Player player) {
        InventoryContainer container = new InventoryContainer(new DraconicMachine(block.getBlock()));
        container.createInventory("Draconic Charger", 9);
    }

    @Override
    public void onBreak(DraconicBlock block, Cancellable cancellable, Player player) {
        InventoryContainer container = new InventoryContainer(block);
        container.syncInventoryWithContainer();
    }

    @Override
    public void onBlockInteract(Player player, Cancellable cancellable, DraconicBlock block) {
        cancellable.setCancelled(true);
        DraconicMachine draconicMachine = new DraconicMachine(block.getBlock());
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
    }


    @Override
    public boolean isRestricted() {
        return true;
    }

    @Override
    public String getDataName() {
        return "DraconicCharger";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§6Draconic Charger", XMaterial.SMOKER, "", "§7Holds infinite power");
    }

    @Override
    public boolean isStackable() {
        return false;
    }


    @Override
    public void onUnload(DraconicBlock block) {
        InventoryContainer container = new InventoryContainer(block);
        container.syncInventoryWithContainer();
    }


    @Override
    public EnhancedRecipe getCraftingRecipe() {
        EnhancedRecipe recipe = new EnhancedRecipe();
        recipe.addIngredient(new VanillaIngredient(XMaterial.IRON_INGOT.parseMaterial(), 32));
        recipe.addIngredient(new VanillaIngredient(XMaterial.GOLD_INGOT.parseMaterial(), 16));
        recipe.addIngredient(new VanillaIngredient(XMaterial.REDSTONE_BLOCK.parseMaterial(), 4));
        recipe.addIngredient(new VanillaIngredient(XMaterial.CHEST.parseMaterial(), 1));
        recipe.addIngredient(new DraconicIngredient("DraconiumDust", 4));
        recipe.addIngredient(new DraconicIngredient("Circuit", 4));
        recipe.addIngredient(new DraconicIngredient("CircuitAdvanced", 1));
        return recipe;
    }

    @Override
    public void tickSync(Block block, DraconicMachine charger) {
        EnergyInfo energyInfo = new EnergyInfo(charger);
        InventoryContainer container = new InventoryContainer(charger);
        Inventory inventory = container.getInventory();

        if(charger.getEnergyStored() == 0) return;
        for(ItemStack stack : inventory.getContents()) {
            if(DraconicItem.isDraconicItem(stack)) {
                DraconicItem draconicItem = new DraconicItem(stack);
                if(draconicItem.isEnergyItem()) {
                    DraconicEnergyItem energyItem = new DraconicEnergyItem(stack);
                    if(energyItem.getSpaceLeft() == 0) continue;
                    if(charger.hasEnergy(maxTransfer)) {
                        if(energyItem.hasSpaceFor(maxTransfer)) {
                            energyItem.storeEnergy(maxTransfer);
                            charger.removeEnergyStored(maxTransfer);
                            energyItem.saveEnergy();
                            energyItem.updateItemStack();
                            continue;
                        } else {
                            double remove = energyItem.getSpaceLeft();
                            energyItem.storeEnergy(remove);
                            charger.removeEnergyStored(remove);
                            energyItem.saveEnergy();
                            energyItem.updateItemStack();
                        }
                    } else {
                        if(energyItem.hasSpaceFor(charger.getEnergyStored())) {
                            energyItem.storeEnergy(charger.getEnergyStored());
                            charger.setEnergyStored(0);
                            energyItem.saveEnergy();
                            energyItem.updateItemStack();
                            continue;
                        } else {
                            double remove = energyItem.getSpaceLeft();
                            energyItem.storeEnergy(remove);
                            charger.removeEnergyStored(remove);
                            energyItem.saveEnergy();
                            energyItem.updateItemStack();
                            continue;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void tickAsync(Block block, DraconicMachine draconicMachine) {

    }
}
