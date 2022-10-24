package de.philipp.advancedevolution.items;

import de.philipp.advancedevolution.StorageKeys;
import de.philipp.advancedevolution.items.custombases.DraconicEnergyItemBase;
import de.philipp.advancedevolution.util.NumberConverter;
import de.philipp.advancedevolution.util.item.ItemUtil;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class DraconicEnergyItem extends DraconicItem {

    protected double energyCapacity = 0;
    protected double energyStored = 0;

    public DraconicEnergyItem(ItemStack stack) {
        super(stack);
        if(super.isEnergyItem()) {
            this.energyCapacity = data.getDouble(StorageKeys.ENERGY_CAPACITY.getNBTKey());
            this.energyStored = data.getDouble(StorageKeys.ENERGY_STORED.getNBTKey());
        } else {
            throw new IllegalArgumentException("Cannot cast an DraconicItem to an DraconicEnergyItem");
        }
    }

    public double getEnergyCapacity() {
        return energyCapacity;
    }

    public double getEnergyStored() {
        return energyStored;
    }

    public boolean hasEnergy(double value) {
        return getEnergyStored()- value >= 0;
    }

    public double getSpaceLeft() {
        return getEnergyCapacity() - getEnergyStored();
    }

    public boolean hasSpaceFor(double amount) {
        return getEnergyStored() + amount <= getEnergyCapacity();
    }

    public boolean updateEnergy() {
        NBTItem data = new NBTItem(getCurrentStack());
        this.energyCapacity = data.getDouble(StorageKeys.ENERGY_CAPACITY.getNBTKey());
        this.energyStored = data.getDouble(StorageKeys.ENERGY_STORED.getNBTKey());
        return true;
    }

    public void saveEnergy() {
        NBTItem data = new NBTItem(getCurrentStack(), true);
        data.setDouble(StorageKeys.ENERGY_CAPACITY.getNBTKey(), energyCapacity);
        data.setDouble(StorageKeys.ENERGY_STORED.getNBTKey(), energyStored);
    }

    public void updateItemStack() {
        ItemStack stack = getCurrentStack();
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = meta.getLore();
        lore.set(lore.size() - 1, "§c" + NumberConverter.format((long) energyStored) + " / " + NumberConverter.format((long) energyCapacity));
        meta.setLore(lore);
        stack.setItemMeta(meta);
    }

    public boolean storeEnergy(double value) {
        NBTItem data = new NBTItem(getCurrentStack(), true);
        if(energyStored + value >= energyCapacity) {
            energyStored = energyCapacity;
            return true;
        } else if (energyStored + value < energyCapacity) {
            energyStored += value;
            return true;
        }
        return true;
    }

    public void setEnergyStored(double v) {
        NBTItem data = new NBTItem(getCurrentStack(), true);
        if(v > energyCapacity)  {
            energyStored = energyCapacity;
        } else if (v <= 0) {
            energyStored = 0;
        } else {
            energyStored = v;
        }
    }

    public void subtractEnergy(double value) {
        if(energyStored == 0) return;
        if(value >= energyStored) {
            energyStored = 0;
        } else if (energyStored >= value) {
            energyStored = energyStored - value;
        }
    }


}
