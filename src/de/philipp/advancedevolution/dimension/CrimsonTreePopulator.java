package de.philipp.advancedevolution.dimension;

import de.philipp.advancedevolution.lib.xseries.XMaterial;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class CrimsonTreePopulator extends BlockPopulator {
    @Override
    public void populate(World world, Random random, Chunk chunk) {
        if(chunk.getBlock(8, 8, 8).getBiome() != Biome.CRIMSON_FOREST) return;
        if (random.nextBoolean()) {
            int amount = random.nextInt(5)+1;  // Amount of trees
            for (int i = 1; i < amount; i++) {
                int X = random.nextInt(15);
                int Z = random.nextInt(15);
                int Y;
                for (Y = 98; chunk.getBlock(X, Y, Z).getType() == Material.AIR; Y--); // Find the highest block of the (X,Z) coordinate chosen.
                if(chunk.getBlock(X, Y -1, Z).getType() == Material.RED_CONCRETE) {
                    spawnTree(chunk.getBlock(X, Y, Z).getLocation(), world, random);
                }
            }
        }
    }

    public void spawnTree(Location location, World world, Random random) {
            int treeHeight = random.nextInt(9) + 7;
            for (int i = 0; i < treeHeight; i++) {
                if (i + 1 != treeHeight) {
                    if (Math.random() < 0.7) {
                        switch (random.nextInt(4)) {
                            case 0:
                                location.add(1, 0, 0);
                                break;
                            case 1:
                                location.add(-1, 0, 0);
                                break;
                            case 2:
                                location.add(0, 0, 1);
                                break;
                            case 3:
                                location.add(0, 0, -1);
                                break;
                        }
                    }
                }
                location.add(0, 1, 0).getBlock().setType(Material.NETHER_WART_BLOCK);
            }
            location.clone().add(1, 0, 0).getBlock().setType(Material.SHROOMLIGHT);
            location.clone().add(-1, 0, 0).getBlock().setType(Material.SHROOMLIGHT);
            location.clone().add(0, 0, 1).getBlock().setType(Material.SHROOMLIGHT);
            location.clone().add(0, 0, -1).getBlock().setType(Material.SHROOMLIGHT);
        }
}
