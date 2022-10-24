package de.philipp.advancedevolution.dimension;

import de.philipp.advancedevolution.dimension.dimensions.DraconicDimension;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DraconicGenerator extends ChunkGenerator {

    // Remember this
    int currentHeight = 50;
    public DraconicDimension dimension;
    private boolean spawnLoc = false;

    public DraconicGenerator(DraconicDimension dimension) {
        this.dimension = dimension;
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomeGrid) {
        SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 50);
        ChunkData chunk = createChunkData(world);
        generator.setScale(0.005D);
        for (int X = 0; X < 16; X++)
            for (int Z = 0; Z < 16; Z++) {
                chunk.setBlock(X, 100, Z, Material.RED_CONCRETE);
                currentHeight = (int) (generator.noise(chunkX*16+X, chunkZ*16+Z, 2D, 0.5D)*15D+50D);
                if(biomeGrid.getBiome(X, currentHeight-1, Z) == Biome.CRIMSON_FOREST) {
                    if(random.nextBoolean() && random.nextBoolean()) {
                        chunk.setBlock(X, currentHeight - 1, Z, Material.CRIMSON_NYLIUM);
                    } else {
                        chunk.setBlock(X, currentHeight - 1, Z, Material.RED_CONCRETE);
                    }
                } else {
                    chunk.setBlock(X, currentHeight - 1, Z, Material.RED_CONCRETE);
                }
                for (int i = currentHeight-2; i > 0; i--)
                    chunk.setBlock(X, i, Z, Material.RED_CONCRETE);
                chunk.setBlock(X, 0, Z, Material.BEDROCK);
            }
        if(!spawnLoc) {
            int Y;
            for (Y = 97; chunk.getType(0, Y, 0) == Material.AIR; Y--);
            Location location = new Location(world, 0.5, Y, 0.5);
            world.setSpawnLocation(location);
            spawnLoc=true;
        }
        return chunk;
    }


    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Arrays.asList(new GrassPopulator(), new WarpedTreePopulator(), new PillarPopulator(), new CrimsonTreePopulator(), new CrimsonFungusPopulator(), new VinePopulator());
    }

}
