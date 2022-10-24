package de.philipp.advancedevolution.blocks.custombases;

import de.philipp.advancedevolution.blocks.*;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ListIterator;
import java.util.UUID;

public abstract class DraconicMachineBase extends DraconicBlockBase implements IOwnAble, IEnergyStorage, ITickAbleBlock {


    public boolean isLinkManager() {
        return false;
    }
    public abstract void onUnload(DraconicBlock block);
    public long getSyncInterval() {
        return 1;
    }
    public long getAsyncInterval() {
        return 1;
    }
    public void updateEnergyNetwork(DraconicMachine draconicMachine) {
       DraconicMachineBase draconicMachineBase = (DraconicMachineBase) draconicMachine.getBlockBase();
       if(draconicMachineBase.isLinkManager()) {
           CrystalLinkManager crystalLinkManager = new CrystalLinkManager(draconicMachine);
           crystalLinkManager.validateLinks();
       }
    }

    public static boolean isFull(InventoryContainer inventoryContainer) {
        return inventoryContainer.getInventory().firstEmpty() == -1;
    }

    public static boolean isFull(Inventory inventory) {
        return inventory.firstEmpty() == -1;
    }

    public static void eject(Block block, InventoryContainer container) {
        Directional directional = (Directional) block.getBlockData();
        Block data = block.getLocation().add(directional.getFacing().getDirection().multiply(-1)).getBlock();
        if(data.getType() != Material.AIR) {
            if (data.getState() instanceof Container) {
                Container chest = (Container) data.getState();
                if(isFull(chest.getInventory())) return;
                Inventory containerBlock = chest.getInventory();
                if(!container.getInventory().isEmpty()) {
                    for (ListIterator<ItemStack> it = container.getInventory().iterator(); it.hasNext(); ) {
                        ItemStack stack = it.next();
                        if(stack == null) continue;
                        ItemStack s = stack.clone();
                        s.setAmount(1);
                        containerBlock.addItem(s);
                        container.removeItem(s.getType(), 1);
                        break;
                    }

                }
            }
        }
    }


}
