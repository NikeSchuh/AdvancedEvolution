package de.philipp.advancedevolution.blocks;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.StorageKeys;
import de.philipp.advancedevolution.blocks.custombases.DraconicMachineBase;
import de.philipp.advancedevolution.entities.particles.EnergyOrb;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.NumberConverter;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
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
        return base.isEnergyPullAble();
    }

    @Override
    public boolean isEnergyAcceptable() {
        return base.isEnergyAcceptable();
    }

    public boolean isLinkManager() {
       return base.isLinkManager();
    }


    public boolean isEnoughCapacityFor(double amount) {
        return getEnergyStored() + amount <= getEnergyCapacity();
    }

    public double getEnergySpaceLeft() {
        return getEnergyCapacity() - getEnergyStored();
    }

    @Override
    public void onEnergyReceive(DraconicMachine sender, DraconicMachine receiver, double amount, BlockFace face) {
        return;
    }


    public boolean hasEnergy(double value) {
        return getEnergyStored() > value;
    }


    public void removeEnergyStored(double value) {
        if(getEnergyStored() - value <= 0) {
            setEnergyStored(0);
        } else {
            setEnergyStored(getEnergyStored() - value);
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

    public static ItemStack ofMachine(DraconicMachine draconicMachine) {
        NBTItem item = new NBTItem(draconicMachine.getBlockBase().getBaseStack());
        item.mergeCompound(draconicMachine.getStoredData());
        ItemStack stack = item.getItem();
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = new ArrayList<>();

        lore.add("§7");
        lore.add("§7Energy Storage: §c" + NumberConverter.format((long) draconicMachine.getEnergyStored()) + " §7/ §c" + NumberConverter.format((long) draconicMachine.getEnergyCapacity()));
        if(draconicMachine.isLinkManager()) {
            CrystalLinkManager crystalLinkManager = new CrystalLinkManager(draconicMachine);
            lore.add("§7Links: §c" + crystalLinkManager.getCurrentLinks() + " §7/ §c" + crystalLinkManager.getMaxLinks());
        }

        InventoryContainer container = new InventoryContainer(draconicMachine);
        if(container.hasInventory()) {
            if(!container.getInventory().isEmpty()) {
                String[] array = items(container.getInventory());
                lore.add("§7Inventory:");
                int count = 0;
                for(String a : array) {
                    if(count == 8) {
                        lore.add("§c(" + (a.length() - count) + " more)");
                        break;
                    }
                    lore.add("§b" + a);
                    count++;
                }
            }
        }
        lore.add("");

        meta.setLore(lore);
        stack.setItemMeta(meta);
        return item.getItem();
    }

    public static String[] items(Inventory inventory) {
        int count = 0;
        int tick = 0;
        List<String> list = new ArrayList<>();
        HashMap<Material, Integer> amounts = new HashMap<>();
        for(ItemStack s : inventory.getContents()) {
            if(s == null) continue;
            if(s.getType() == Material.AIR) continue;
            amounts.put(s.getType(), amounts.getOrDefault(s.getType(), 0) + s.getAmount());
        }
        for(Material material : amounts.keySet()) {
            int amount = amounts.get(material);
            list.add(material.name().toLowerCase().replaceFirst(String.valueOf(Character.toLowerCase(material.name().charAt(0))), String.valueOf(Character.toUpperCase(material.name().charAt(0)))) + " x" + amount);
        }
        return list.toArray(new String[list.size()]);
    }

}
