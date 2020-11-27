package de.philipp.advancedevolution.items;

import de.philipp.advancedevolution.StorageKeys;
import de.philipp.advancedevolution.items.custombases.DraconicArmorItemBase;
import de.philipp.advancedevolution.items.custombases.DraconicEnergyItemBase;
import de.philipp.advancedevolution.items.custombases.DraconicSwordItemBase;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.plugin.NBTAPI;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class DraconicItem {

    protected NBTCompound data;
    private ItemStack currentStack;
    private ItemStack defaultStack;

    public DraconicItem(ItemStack stack) {
        this.data = new NBTItem(stack, true);
        if(!isDraconicItem(stack)) {
            throw new IllegalArgumentException("Cannot create an instance of an DraconicItem with an non registered dataname");
        }

        this.currentStack = stack;
        this.defaultStack = ItemRegistry.getItemBase(data.getString(StorageKeys.REGISTRY_NAME.getNBTKey())).getBaseStack();
    }

    public void setCurrentStack(ItemStack currentStack) {
        this.currentStack = currentStack;
    }

    public ItemStack getCurrentStack() {
        return currentStack;
    }

    public ItemStack getDefaultStackStack() {
        return defaultStack;
    }

    public String getDataName() {
        return data.getString(StorageKeys.REGISTRY_NAME.getNBTKey());
    }

    public NBTCompound getStoredData() {
        return data;
    }

    public DraconicItemBase getItemBase() {
        return ItemRegistry.getItemBase(getDataName());
    }

    public static DraconicItem instantiateItem(String dataName) {
        DraconicItemBase itemBase = ItemRegistry.getItemBase(dataName);
        ItemStack defaultStack = itemBase.getBaseStack();
        NBTItem item = new NBTItem(defaultStack);
        item.setBoolean(StorageKeys.PLUGIN.getNBTKey(), true);
        item.setString(StorageKeys.REGISTRY_NAME.getNBTKey(), dataName);

        if(!itemBase.isStackable()) {
            item.setString(StorageKeys.UNIQUE_ITEM_ID.getNBTKey(), UUID.randomUUID().toString());
        }

        return new DraconicItem(item.getItem());
    }

    public static DraconicSwordItem instantiateSwordItem(String dataName) {
        DraconicSwordItemBase itemBase = (DraconicSwordItemBase) ItemRegistry.getItemBase(dataName);
        ItemStack defaultStack = itemBase.getBaseStack();
        NBTItem item = new NBTItem(defaultStack);
        item.setBoolean(StorageKeys.PLUGIN.getNBTKey(), true);
        item.setString(StorageKeys.REGISTRY_NAME.getNBTKey(), dataName);
        if(!itemBase.isStackable()) {
            item.setString(StorageKeys.UNIQUE_ITEM_ID.getNBTKey(), UUID.randomUUID().toString());
        }

        return new DraconicSwordItem((item.getItem()));
    }

    public static DraconicEnergyItem instantiateEnergyItem(String dataName) {
        DraconicEnergyItemBase itemBase = (DraconicEnergyItemBase) ItemRegistry.getItemBase(dataName);
        ItemStack defaultStack = itemBase.getBaseStack();



        NBTItem item = new NBTItem(defaultStack);
        item.setBoolean(StorageKeys.PLUGIN.getNBTKey(), true);
        item.setString(StorageKeys.REGISTRY_NAME.getNBTKey(), dataName);
        item.setDouble(StorageKeys.ENERGY_CAPACITY.getNBTKey(), itemBase.getEnergyCapacity());
        item.setDouble(StorageKeys.ENERGY_STORED.getNBTKey(), 0.0);

        if(!itemBase.isStackable()) {
            item.setString(StorageKeys.UNIQUE_ITEM_ID.getNBTKey(), UUID.randomUUID().toString());
        }

        return new DraconicEnergyItem(item.getItem());
    }


    public static DraconicArmorItem instantiateArmorItem(String dataName) {
        DraconicArmorItemBase itemBase = (DraconicArmorItemBase) ItemRegistry.getItemBase(dataName);
        ItemStack defaultStack = itemBase.getBaseStack();


        NBTItem item = new NBTItem(defaultStack);
        item.setBoolean(StorageKeys.PLUGIN.getNBTKey(), true);
        item.setString(StorageKeys.REGISTRY_NAME.getNBTKey(), dataName);
        item.setDouble(StorageKeys.ENERGY_CAPACITY.getNBTKey(), itemBase.getEnergyCapacity());
        item.setDouble(StorageKeys.ENERGY_STORED.getNBTKey(), 0.0);
        item.setString(StorageKeys.SHIELD_TYPE.getNBTKey(), itemBase.getDraconicArmorType().name());
        item.setDouble(StorageKeys.SHIELD_CAPACITY.getNBTKey(), itemBase.getShieldCapacity());
        item.setFloat(StorageKeys.SHIELD_REGENERATION.getNBTKey(), itemBase.getShieldRegeneration());
        item.setString(StorageKeys.SHIELD_TYPE.getNBTKey(), itemBase.getDraconicArmorType().name());

        if(!itemBase.isStackable()) {
            item.setString(StorageKeys.UNIQUE_ITEM_ID.getNBTKey(), UUID.randomUUID().toString());
        }

        return new DraconicArmorItem(item.getItem());
    }


    public static Boolean isDraconicItem(ItemStack stack) {
        NBTItem data = new NBTItem(stack);
        return data.hasKey(StorageKeys.PLUGIN.getNBTKey());
    }

    public boolean isEnergyItem() {
        return DraconicEnergyItemBase.class.isInstance(getItemBase());
    }

    public boolean isArmorItem() {
        return DraconicArmorItemBase.class.isInstance(getItemBase());
    }


    public static boolean isArmorItem(String dataName) {
        return DraconicArmorItemBase.class.isInstance(ItemRegistry.getItemBase(dataName));
    }



}
