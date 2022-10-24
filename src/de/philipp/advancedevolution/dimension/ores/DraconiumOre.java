package de.philipp.advancedevolution.dimension.ores;

import de.philipp.advancedevolution.dimension.generator.OreRegistryEntry;
import org.bukkit.Material;

import java.util.Random;

public class DraconiumOre implements OreRegistryEntry {

    @Override
    public int getSpawnTries() {
        return 10;
    }

    @Override
    public int getSpawnChance() {
        return 50;
    }

    @Override
    public int getExpandingChance() {
        return 50;
    }

    @Override
    public int getOreAmount() {
        return new Random().nextInt(10) + 1;
    }

    @Override
    public Material getSpawnInMaterial() {
        return Material.RED_CONCRETE;
    }

    @Override
    public Material getOreMaterial() {
        return Material.NETHER_BRICK;
    }
}
