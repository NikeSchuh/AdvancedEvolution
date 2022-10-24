package de.philipp.advancedevolution.structures;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.loot.LootTable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

public interface Structure {

    void generate(Location location, Random random);
    void populateChest(Inventory inventory, Random random);

    StructureFile getStructuralData();

    default void generateBlocks(Location location, Random random) {
        Collection<StructureBlock> blockMap = getStructuralData().getBlocks(location);
        for(StructureBlock block : blockMap) {
            if(block.getBlockData().getMaterial() == Material.CHEST) {
                generateChest(location,  block, random);
                continue;
            }
            block.getLocation().getBlock().setType(block.getBlockData().getMaterial());
            block.getLocation().getBlock().setBlockData(block.getBlockData());
        }
    }

    default void generateChest(Location location, StructureBlock block, Random random) {
        block.getLocation().getBlock().setType(Material.CHEST);
        block.getLocation().getBlock().setBlockData(Bukkit.createBlockData(Material.CHEST));
        Chest chest = (Chest) block.getLocation().getBlock().getState();
        populateChest(chest.getBlockInventory(), random);
    }

}
