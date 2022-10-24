package de.philipp.advancedevolution.dimension;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class CrimsonFungusPopulator extends BlockPopulator {
    @Override
    public void populate(World world, Random random, Chunk chunk) {
        if(chunk.getBlock(8, 8, 8).getBiome() != Biome.CRIMSON_FOREST) return;
        if (random.nextBoolean()) {
            int amount = random.nextInt(3)+1;  // Amount of trees
            for (int i = 1; i < amount; i++) {
                int X = random.nextInt(15);
                int Z = random.nextInt(15);
                int Y;
                for (Y = 98; chunk.getBlock(X, Y, Z).getType() == Material.AIR; Y--); // Find the highest block of the (X,Z) coordinate chosen.
                if(chunk.getBlock(X, Y -1, Z).getType() == Material.RED_CONCRETE) {
                    world.generateTree(chunk.getBlock(X, Y, Z).getLocation(), TreeType.CRIMSON_FUNGUS);
                }
            }
        }
    }
}
