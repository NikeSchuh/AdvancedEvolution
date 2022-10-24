package de.philipp.advancedevolution.items;

import de.philipp.advancedevolution.StorageKeys;
import de.philipp.advancedevolution.blocks.DraconicBlockBase;
import de.philipp.advancedevolution.items.custombases.DraconicArmorItemBase;
import de.philipp.advancedevolution.items.custombases.DraconicEnergyItemBase;
import de.philipp.advancedevolution.items.custombases.DraconicSwordItemBase;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.plugin.NBTAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
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

    public boolean compareItemBase(@Nonnull  DraconicItem draconicItem) {
        return draconicItem.getDataName().equals(this.getDataName());
    }

    public boolean compareItemBase(@Nonnull DraconicItemBase base) {
        return base.getDataName().equals(this.getDataName());
    }

    public static DraconicItem instantiateTrueItem(String dataName) {
        DraconicItemBase base = ItemRegistry.getItemBase(dataName);
        if(base instanceof DraconicSwordItemBase) {
            return instantiateSwordItem(dataName);
        }
        if(base instanceof DraconicArmorItemBase) {
            return instantiateArmorItem(dataName);
        }
        if(base instanceof DraconicEnergyItemBase) {
            return instantiateEnergyItem(dataName);
        }
        return instantiateItem(dataName);
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


    public static boolean isDraconicItem(ItemStack stack) {
        if(stack == null) return  false;
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

    public static boolean containsAtLeast(Inventory inventory, DraconicItemBase itemBase, int amount) {
        int itemCount = 0;
        for(ItemStack stack : inventory.getContents()) {
            if(isDraconicItem(stack)) {
                DraconicItem item = new DraconicItem(stack);
                if(itemCount >= amount) return true;
                if(item.getItemBase().getDataName().equals(itemBase.getDataName())) {
                    itemCount+= stack.getAmount();
                }
            }
        }
        return itemCount >= amount;
    }

    public static boolean containsAtLeastVanilla(Inventory inventory, Material material, int amount) {
        int itemCount = 0;
        for(ItemStack stack : inventory.getContents()) {
            if(itemCount >= amount) return true;
            if(stack == null || stack.getType() == Material.AIR) continue;
            if(stack.getType() == material) {
                if(!isDraconicItem(stack)) {
                    itemCount+=stack.getAmount();
                }
            }
        }
        return false;
    }

    public static boolean removeItem(Inventory inventory, DraconicItemBase itemBase, int amount) {
        if(!containsAtLeast(inventory, itemBase, amount)) return false;
        for(ItemStack stack : inventory.getContents()) {
            if(amount == 0) return true;
            if(stack == null || stack.getType() == Material.AIR) continue;
            if(isDraconicItem(stack)) {
                DraconicItem draconicItem = new DraconicItem(stack);
                if(draconicItem.getDataName().equals(itemBase.getDataName())) {
                    if (amount >= stack.getAmount()) {
                        amount -= stack.getAmount();
                        stack.setAmount(0);
                    } else {
                        stack.setAmount(stack.getAmount() - amount);
                        return true;
                    }
                }
            }
        }
        return true;
    }

    public static boolean removeItem(Inventory inventory, Material material, int amount) {
        if(!containsAtLeastVanilla(inventory, material, amount)) return false;
        for(ItemStack stack : inventory.getContents()) {
            if(amount == 0) return true;
            if(stack == null || stack.getType() == Material.AIR) continue;
            if(stack.getType() != material) continue;
            if(!isDraconicItem(stack)) {
                if (amount >= stack.getAmount()) {
                    amount -= stack.getAmount();
                    stack.setAmount(0);
                } else {
                    stack.setAmount(stack.getAmount() - amount);
                    return true;
                }
            }
        }
        return true;
    }

    public static HashMap<Material, Integer> map(Inventory inventory) {
        HashMap<Material, Integer> map = new HashMap();
        for(ItemStack stack : inventory.getContents()) {
            if(stack == null || stack.getType() == Material.AIR) continue;
            if(!isDraconicItem(stack)) {
                map.put(stack.getType(), stack.getAmount() + map.getOrDefault(stack.getType(), 0));
            }
        }
        return map;
    }


}
