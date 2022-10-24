package de.philipp.advancedevolution.dimension;

import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;

import java.util.Random;
import java.util.logging.Level;

public class PillarPopulator extends BlockPopulator {

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        if(chunk.getBlock(8, 8, 8).getBiome() != Biome.NETHER_WASTES) return;
        if (random.nextBoolean()) {
            int amount = random.nextInt(2)+1;  // Amount of trees
            for (int i = 1; i < amount; i++) {
                int X = random.nextInt(15);
                int Z = random.nextInt(15);
                int Y;
                for (Y = 98; chunk.getBlock(X, Y, Z).getType() == Material.AIR; Y--); // Find the highest block of the (X,Z) coordinate chosen.
               generateTree(random, chunk.getBlock(X, Y, Z).getLocation());
            }
        }
    }

    public void generateTree(Random random, Location location) {
        for(int i = 0; i < (random.nextInt(15) + 2); i++) {
            Location loc = location.clone();
            loc.setY(location.getY() + i);
            loc.getBlock().setType(Material.BLACK_CONCRETE);
            if(random.nextBoolean()) {
                switch (random.nextInt(4)) {
                    case 0:
                        location.add(1, 0, 0).getBlock().setType(Material.BLACK_CONCRETE);
                        location.subtract(1, 0, 0);
                        break;
                    case 1:
                        location.add(-1, 0, 0).getBlock().setType(Material.BLACK_CONCRETE);
                        location.subtract(-1, 0, 0);
                        break;
                    case 2:
                        location.add(0, 0, 1).getBlock().setType(Material.BLACK_CONCRETE);
                        location.subtract(0, 0, 1);
                        break;
                    case 3:
                        location.add(0, 0, -1).getBlock().setType(Material.BLACK_CONCRETE);
                        location.subtract(0, 0, -1);
                        break;
                }
            }
        }
    }

}
