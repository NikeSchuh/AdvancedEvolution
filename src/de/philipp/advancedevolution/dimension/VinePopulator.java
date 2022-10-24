package de.philipp.advancedevolution.dimension;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class VinePopulator extends BlockPopulator {
@Override
public void populate(World world, Random random, Chunk chunk) {
    if(chunk.getBlock(8, 8, 8).getBiome() != Biome.CRIMSON_FOREST) return;
        int amount = 6;
        for (int i = 1; i < amount; i++) {
            int X = random.nextInt(15);
            int Z = random.nextInt(15);
            for(int Y = 99; chunk.getBlock(X, Y, Z).getType() == Material.AIR; Y --) {
                if(random.nextInt(1000) < 100) break;
                chunk.getBlock(X, Y, Z).setType(Material.WEEPING_VINES);
            }

        }
}
}
