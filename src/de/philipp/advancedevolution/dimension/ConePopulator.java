package de.philipp.advancedevolution.dimension;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;


public class ConePopulator extends BlockPopulator {
    @Override
    public void populate(World world, Random random, Chunk chunk) {
        if(chunk.getBlock(8, 50, 8).getBiome() != Biome.NETHER_WASTES) return;
        if (random.nextBoolean()) {
            int amount = random.nextInt(1);  // Amount of trees
            for(int i = 0; i < amount; i++) {

            }
        }

    }

    public void spike(Block block) {

    }

}
