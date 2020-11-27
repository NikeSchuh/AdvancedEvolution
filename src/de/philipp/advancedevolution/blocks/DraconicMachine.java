package de.philipp.advancedevolution.blocks;

import de.philipp.advancedevolution.StorageKeys;
import de.philipp.advancedevolution.blocks.custombases.DraconicMachineBase;
import de.philipp.advancedevolution.blocks.gui.InventoryContainer;
import de.tr7zw.nbtapi.NBTCompound;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.UUID;

public class DraconicMachine extends DraconicBlock implements IEnergyStorage, IOwnAble {

    private NBTCompound storedData;
    private DraconicMachineBase base;

    public DraconicMachine(Block block) {
        super(block);
        this.storedData = getStoredData();
        this.base = (DraconicMachineBase) this.getBlockBase();
    }


    public double getEnergyStored() {
        return storedData.getDouble(StorageKeys.ENERGY_STORED.getNBTKey());
    }

    public void setEnergyStored(double value) {
        storedData.setDouble(StorageKeys.ENERGY_STORED.getNBTKey(), value);
    }

    public void addEnergyStored(double value) {
        double energy = getEnergyStored();
        if(energy + value > getEnergyCapacity()) {
            setEnergyStored(getEnergyCapacity());
        } else {
            setEnergyStored(energy + value);
        }
    }

    @Override
    public double getEnergyCapacity() {
        return storedData.getDouble(StorageKeys.ENERGY_CAPACITY.getNBTKey());
    }

    @Override
    public boolean isEnergyPullAble() {
        return false;
    }

    public void removeEnergyStored(double value) {
        double energy = getEnergyStored();
        if(energy - value >= 0) {
            setEnergyStored(0);
        } else {
            setEnergyStored(energy - value);
        }
    }

    public UUID getOwner() {
        return storedData.getUUID(StorageKeys.OWNING_PLAYER.getNBTKey());
    }

    @Override
    public boolean isRestricted() {
        return storedData.getBoolean(StorageKeys.ACCESS_PRIVATE.getNBTKey());
    }

    public void setRestricted(boolean restricted) {
        storedData.setBoolean(StorageKeys.ACCESS_PRIVATE.getNBTKey(), restricted);
    }
}
