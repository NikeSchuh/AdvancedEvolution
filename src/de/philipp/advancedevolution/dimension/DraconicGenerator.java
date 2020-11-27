package de.philipp.advancedevolution.dimension;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.PerlinNoiseGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DraconicGenerator extends ChunkGenerator {
    int currentHeight = 50;

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
        ChunkData chunk = createChunkData(world);
        generator.setScale(0.02D);

        for (int X = 0; X < 16; X++)
            for (int Z = 0; Z < 16; Z++) {
                currentHeight = (int) (generator.noise(chunkX*16+X, chunkZ*16+Z , 0.2D, 0.5D)*15D+50D) + 5;


                chunk.setBlock(X, currentHeight, Z, Material.STICKY_PISTON);
                chunk.setBlock(X, currentHeight - 1, Z, Material.REDSTONE_BLOCK);
                chunk.setBlock(X, currentHeight - 2, Z, Material.DIRT);

                for (int i = currentHeight-2; i > 0; i--)
                    chunk.setBlock(X, i, Z, Material.STONE);
                chunk.setBlock(X, 0, Z, Material.BEDROCK);
            }
        return chunk;
    }
}
