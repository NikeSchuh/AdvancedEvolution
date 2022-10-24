package de.philipp.advancedevolution.blocks;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTTileEntity;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryContainer {

    public static HashMap<Block, Inventory> accessCache = new HashMap<>();

    private DraconicBlock draconicBlock;
    private NBTCompound inventoryData;
    private NBTCompound content;
    private NBTCompound blockData;

    public InventoryContainer(DraconicBlock draconicBlock) {
        this.draconicBlock = draconicBlock;
        this.blockData = draconicBlock.getStoredData();
        if(blockData.hasKey("inventoryData")) {
            inventoryData = blockData.getCompound("inventoryData");
            content = inventoryData.getCompound("content");
        }
    }

    public void syncInventoryWithContainer() {
        Inventory inventory = getInventory();
        for(int i = 0; i < inventoryData.getInteger("slots"); i++) {
            content.setItemStack(i + "", inventory.getItem(i));
        }
    }


    public boolean hasInventory() {
        return inventoryData != null;
    }

    public Inventory getInventory() {
        if(accessCache.containsKey(draconicBlock.getBlock())) {
            return accessCache.get(draconicBlock.getBlock());
        } else {
         //   AdvancedEvolution.send("Creating new Inventory for" + draconicBlock.getDataName());
            Inventory inventory = Bukkit.createInventory(null, inventoryData.getInteger("slots"), inventoryData.getString("title"));
            for(int slot = 0; slot < inventoryData.getInteger("slots"); slot++) {
                if(content.hasKey(slot + "")) {
                    inventory.setItem(slot, content.getItemStack(slot + ""));
                } else {
                    inventory.setItem(slot, null);
                }
            }
            accessCache.put(draconicBlock.getBlock(), inventory);
            return inventory;
        }
    }

    public static boolean isContainer(DraconicBlock draconicBlock) {
        return draconicBlock.getStoredData().hasKey("inventoryData");
    }


    public void createInventory(String title, int slots) {
        if(!hasInventory()) {
            this.inventoryData = blockData.addCompound("inventoryData");
            inventoryData.setString("title", title);
            inventoryData.setInteger("slots", slots);
            this.content = inventoryData.addCompound("content");
        }
    }

    public void setItem(int slot, ItemStack stack) {
        if(slot > content.getKeys().size()) throw new ArrayIndexOutOfBoundsException("Accessed slot : " + slot + " Maximum Slots:" + content.getKeys().size());
        content.setItemStack(slot + "", stack);
    }

    public void removeItem(Material material, int itemsToRemove) {
        Inventory inventory = getInventory();
        int count = 0;
        for(ItemStack invItem : inventory.getContents()) {
            if(invItem != null) {
                if(invItem.getType().equals(material)) {
                    int preAmount = invItem.getAmount();
                    int newAmount = Math.max(0, preAmount - itemsToRemove);
                    itemsToRemove = Math.max(0, itemsToRemove - preAmount);
                    invItem.setAmount(newAmount);
                    setItem(count, invItem);
                    count++;
                    if(itemsToRemove == 0) {
                        break;
                    }
                }
            }
        }
    }

    public ItemStack[] getExactContents() {
        List<ItemStack> stacks = new ArrayList<>();
        for(String key : content.getKeys()) {
            ItemStack stack = content.getItemStack(key);
            stacks.add(stack);
        }
        return stacks.toArray(new ItemStack[stacks.size()]);
    }

    public ItemStack[] getContents() {
        return getInventory().getContents();
    }

    public boolean contains(ItemStack stack) {
        Inventory inventory = getInventory();
        return inventory.contains(stack);
    }

    public boolean contains(Material material) {
        Inventory inventory = getInventory();
        return inventory.contains(material);
    }

    public boolean contains(Material material, int amount) {
        Inventory inventory = getInventory();
        return inventory.contains(material, amount);
    }

}
