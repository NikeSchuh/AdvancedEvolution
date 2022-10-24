package de.philipp.advancedevolution.blocks;

import de.philipp.advancedevolution.StorageKeys;
import de.philipp.advancedevolution.blocks.custombases.DraconicMachineBase;
import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.items.ItemRegistry;
import de.philipp.advancedevolution.items.custombases.DraconicArmorItemBase;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTTileEntity;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public class DraconicBlock {

    private NBTCompound dataContainer;
    private Block block;
    private DraconicBlockBase blockBase;
    private String dataName;

    public DraconicBlock(Block block) {
        if(!isDraconicBlock(block)) {
            throw new IllegalArgumentException("The given block is not an DraconicBlock.");
        }
        this.dataContainer = new NBTTileEntity(block.getState()).getPersistentDataContainer();
        this.block = block;
        this.dataName = dataContainer.getString(StorageKeys.REGISTRY_NAME.getNBTKey());
        this.blockBase = (DraconicBlockBase) ItemRegistry.getItemBase(dataName);
        if(blockBase == null) {
            throw new NullPointerException("The Block seems to not be registred correclty????? " + dataName);
        }
    }

    public NBTCompound getStoredData() {
        return dataContainer;
    }

    public NBTCompound getTileEntity() {
        return dataContainer;
    }

    public DraconicBlockBase getBlockBase() {
        return blockBase;
    }

    public Block getBlock() {
        return block;
    }

    public String getDataName() {
        return dataName;
    }

    public boolean isMachine() {
        return this.getBlockBase() instanceof DraconicMachineBase;
    }

    public boolean isTickable()  {
        try {
            this.getBlockBase().getClass().getMethod("tick", Block.class);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public static boolean isBlockBase(DraconicItemBase base) {
        return DraconicBlockBase.class.isInstance(base);
    }

    public static boolean isDraconicBlock(Block block) {
        try{
            NBTTileEntity tileEntity = new NBTTileEntity(block.getState());
            return tileEntity.getPersistentDataContainer().hasKey(StorageKeys.PLUGIN.getNBTKey());
        }catch (Exception e) {
            return false;
        }
    }

    public String toString() {
        return "{" + this.getClass().getSimpleName() + ":{InternalData:{" + dataContainer.toString() + "}}}";
    }




}
