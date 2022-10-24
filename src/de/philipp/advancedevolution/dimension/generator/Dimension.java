package de.philipp.advancedevolution.dimension.generator;

import de.philipp.advancedevolution.dimension.util.Registry;
import de.philipp.advancedevolution.entities.DraconicEntity;
import de.philipp.advancedevolution.util.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.io.File;
import java.io.IOException;

public interface Dimension {

    Registry<OreRegistryEntry> getOreRegister();
    Registry<DraconicEntity> getEntityRegister();
    World getWorld();
    String getName();
    ChunkGenerator getGenerator();

    default boolean hasWorld() {
        return getWorld() != null;
    }

    default boolean hasGenerator() {
        return getGenerator() != null;
    }

    default boolean delete() {
        if(!hasWorld()) return false;
        Bukkit.unloadWorld(getWorld(), false);
        File file = new File(getName());
        return FileUtils.deleteDirectory(file);
    }

    void create();


}
