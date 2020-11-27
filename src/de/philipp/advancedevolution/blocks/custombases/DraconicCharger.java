package de.philipp.advancedevolution.blocks.custombases;

import de.philipp.advancedevolution.blocks.DraconicBlock;
import de.philipp.advancedevolution.blocks.DraconicMachine;
import de.philipp.advancedevolution.blocks.IEnergyStorage;
import de.philipp.advancedevolution.blocks.ITickAbleBlock;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.shield.Shield;
import de.philipp.advancedevolution.shield.ShieldManager;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;

import java.util.UUID;

public class DraconicCharger extends DraconicMachineBase implements ITickAbleBlock{

    @Override
    public void onPlace(DraconicBlock block, Cancellable cancellable, Player player) {

    }

    @Override
    public void onBreak(DraconicBlock block, Cancellable cancellable, Player player) {
        DraconicMachine draconicMachine = new DraconicMachine(block.getBlock());
        if (isRestricted()) {
            if (!player.getUniqueId().equals(draconicMachine.getOwner())) {
                cancellable.setCancelled(true);
            }
        }
    }

    @Override
    public void onBlockInteract(Player player, Cancellable cancellable, DraconicBlock block) {
        DraconicMachine draconicMachine = new DraconicMachine(block.getBlock());

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
    public void tick(Block block) {

    }

}
