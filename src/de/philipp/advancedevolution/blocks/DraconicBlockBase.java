package de.philipp.advancedevolution.blocks;

import de.philipp.advancedevolution.StorageKeys;
import de.philipp.advancedevolution.blocks.custombases.DraconicMachineBase;
import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTTileEntity;
import de.tr7zw.nbtinjector.NBTInjector;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

public abstract class DraconicBlockBase extends DraconicItemBase {

    public abstract void onPlace(DraconicBlock block,Cancellable cancellable, Player player);
    public abstract void onBreak(DraconicBlock block, Cancellable cancellable, Player player);
    public abstract void onBlockInteract(Player player, Cancellable cancellable, DraconicBlock block);

    public void onPlace(Block block, NBTCompound itemContainer, Cancellable cancellable, Player player) {
        NBTTileEntity tileEntity = new NBTTileEntity(block.getState());
        NBTCompound dataContainer = tileEntity.getPersistentDataContainer();

        if(itemContainer == null) {
            dataContainer.setBoolean(StorageKeys.PLUGIN.getNBTKey(), true);
            dataContainer.setString(StorageKeys.REGISTRY_NAME.getNBTKey(), getDataName());

            if (this instanceof IOwnAble) {
                IOwnAble iOwnAble = (IOwnAble) this;
                dataContainer.setUUID(StorageKeys.OWNING_PLAYER.getNBTKey(), player.getUniqueId());
                dataContainer.setBoolean(StorageKeys.ACCESS_PRIVATE.getNBTKey(), iOwnAble.isRestricted());
            }

            if (this instanceof IEnergyStorage) {
                IEnergyStorage component = (IEnergyStorage) this;
                dataContainer.setDouble(StorageKeys.ENERGY_CAPACITY.getNBTKey(), component.getEnergyCapacity());
                dataContainer.setDouble(StorageKeys.ENERGY_STORED.getNBTKey(), 0d);
            }
        }

        if(itemContainer != null) {
            dataContainer.mergeCompound(itemContainer);

            if (this instanceof IOwnAble) {
                IOwnAble iOwnAble = (IOwnAble) this;
                if(!(dataContainer.hasKey(StorageKeys.OWNING_PLAYER.getNBTKey()))) {
                    dataContainer.setUUID(StorageKeys.OWNING_PLAYER.getNBTKey(), player.getUniqueId());
                    dataContainer.setBoolean(StorageKeys.ACCESS_PRIVATE.getNBTKey(), iOwnAble.isRestricted());
                }
            }

            if (this instanceof IEnergyStorage) {
                IEnergyStorage component = (IEnergyStorage) this;
                if(!(dataContainer.hasKey(StorageKeys.ENERGY_CAPACITY.getNBTKey()))) {
                    dataContainer.setDouble(StorageKeys.ENERGY_CAPACITY.getNBTKey(), component.getEnergyCapacity());
                    dataContainer.setDouble(StorageKeys.ENERGY_STORED.getNBTKey(), 0d);
                }
            }
        }

        DraconicBlock draconicBlock = new DraconicBlock(block);
        onPlace(draconicBlock, cancellable, player);
    }

    public void onBreak(Block block, Cancellable cancellable, Player player) {
        DraconicBlock draconicBlock = new DraconicBlock(block);
        onBreak(draconicBlock, cancellable, player);
    }

}
