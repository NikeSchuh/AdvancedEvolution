package de.philipp.advancedevolution.dimension;

import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class WarpedTreePopulator extends BlockPopulator {
    @Override
    public void populate(World world, Random random, Chunk chunk) {
        if(chunk.getBlock(8, 50, 8).getBiome() != Biome.WARPED_FOREST) return;
        if (random.nextBoolean()) {
            int amount = random.nextInt(5) + 1;  // Amount of trees
            for (int i = 1; i < amount; i++) {
                int X = random.nextInt(15);
                int Z = random.nextInt(15);
                int Y;
                for (Y = 90; chunk.getBlock(X, Y, Z).getType() == Material.AIR; Y--); // Find the highest block of the (X,Z) coordinate chosen.
                if(chunk.getBlock(X, Y, Z).getType() != Material.RED_CONCRETE) return;
                spawnTree(world, random, chunk, chunk.getBlock(X, Y + 1, Z), 10, 4);
            }
        }
    }

    public void spawnTree(World world, Random random, Chunk chunk, Block block, int maxLogHeight, int minLogHeight) {
        int treeHeight = random.nextInt(maxLogHeight - minLogHeight) + minLogHeight;
        Location startLocation = block.getLocation();
        Location croneLocation = block.getLocation().add(0.5, treeHeight + (treeHeight / 2) - 2, 0.5);
        for(int i = 0; i < treeHeight; i++) {
            startLocation.add(0, i, 0);
            startLocation.getBlock().setType(Material.WARPED_STEM);
            startLocation.subtract(0, i, 0);
        }
        for(Location location : generateSphere(croneLocation, treeHeight / 2, false)) {
            if(random.nextInt(100) < 45) {
                location.getBlock().setType(Material.WARPED_WART_BLOCK);
            }
        }
        for(Location location : generateSphere(croneLocation, (treeHeight / 4), false)) {
            if(random.nextInt(100) < 30) {
                location.getBlock().setType(Material.SHROOMLIGHT);
            }
        }
        Bukkit.getLogger().log(Level.INFO, "Generating Tree [h:" +  treeHeight);
    }

    public List<Location> generateSphere(Location centerBlock, int radius, boolean hollow) {
        if (centerBlock == null) {
            return new ArrayList<>();
        }

        List<Location> circleBlocks = new ArrayList<Location>();

        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();

        for(int x = bx - radius; x <= bx + radius; x++) {
            for(int y = by - radius; y <= by + radius; y++) {
                for(int z = bz - radius; z <= bz + radius; z++) {

                    double distance = ((bx-x) * (bx-x) + ((bz-z) * (bz-z)) + ((by-y) * (by-y)));

                    if(distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {

                        Location l = new Location(centerBlock.getWorld(), x, y, z);

                        circleBlocks.add(l);

                    }

                }
            }
        }

        return circleBlocks;
    }


}
