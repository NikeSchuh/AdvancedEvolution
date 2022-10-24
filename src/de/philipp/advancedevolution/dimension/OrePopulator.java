package de.philipp.advancedevolution.dimension;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.dimension.generator.OreRegistryEntry;
import de.philipp.advancedevolution.dimension.util.Registry;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class OrePopulator extends BlockPopulator {

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        Registry<OreRegistryEntry> registry = AdvancedEvolution.getDimensionManager().draconic.getOreRegister();
        for(OreRegistryEntry oreRegistryEntry : registry) {
            oreRegistryEntry.tryGenerateOre(world, random, chunk, 30, 5);
        }
    }

}
