package de.philipp.advancedevolution.dimension;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class GrassPopulator extends BlockPopulator {
    @Override
    public void populate(World world, Random random, Chunk chunk) {
        int amount = random.nextInt(10)+1;
        for (int i = 1; i < amount; i++) {
            int X = random.nextInt(15);
            int Z = random.nextInt(15);
            int Y;
            for (Y = 80; chunk.getBlock(X, Y, Z).getType() == Material.AIR; Y--);
            if(chunk.getBlock(X, Y, Z).getType() == Material.RED_CONCRETE || chunk.getBlock(X, Y, Z).getType() == Material.CRIMSON_NYLIUM) {
                if(random.nextBoolean()) {
                    chunk.getBlock(X, Y + 1, Z).setType(Material.WEEPING_VINES_PLANT);
                }else{
                    chunk.getBlock(X, Y + 1, Z).setType(Material.CRIMSON_ROOTS);
                }

            }
        }

    }
}
