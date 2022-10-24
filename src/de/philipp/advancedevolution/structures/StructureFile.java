package de.philipp.advancedevolution.structures;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.lib.xseries.XEntity;
import de.philipp.advancedevolution.util.Cuboid;
import de.philipp.advancedevolution.util.npl.config.Datafile;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

public class StructureFile extends Datafile {

    private String structureName;

    public StructureFile(String structureName) {
        super("plugins/AdvancedEvolution/Structures/" + structureName + ".structure");
        this.structureName = structureName;
    }

    public void saveBlocks(Cuboid cuboid) {
        FileConfiguration config = getConfig();
        Iterator<Block> iterator = cuboid.iterator();
        int count = 0;
        for(Block block : cuboid.getBlocks()) {
            if(block.isEmpty()) continue;
            Location location = block.getLocation().subtract(cuboid.getCenter().clone());
            location.setY(block.getLocation().getBlockY() - cuboid.getCenter().getBlockY());
            config.set("Blocks." + count + "",new StructureBlock(location, block.getBlockData()).toString());
            count++;
        }
        saveData();
    }

    public void saveEntities(Cuboid cuboid) {
        FileConfiguration config = getConfig();
        int count = 0;
        for(Chunk chunk : cuboid.getChunks()) {
            for(Entity entity : chunk.getEntities()) {
                if(entity.getType().isSpawnable()) {
                    Location location = entity.getLocation().subtract(cuboid.getCenter().clone());
                    location.setY(entity.getLocation().getY() - cuboid.getCenter().getY());
                    config.set("Entities." + count + ".Base", new StructureEntity(location, entity.getType()).toString());
                    ConfigurationSection section = config.createSection("Entities." + count + ".Advanced");
                    count++;
                }
            }
        }
        saveData();
    }

    public void spawnEntities(Location center) {
        FileConfiguration config = getConfig();
        ConfigurationSection entitySection = config.getConfigurationSection("Entities");
        entitySection.getKeys(false).forEach(key -> {
            //ConfigurationSection advancedData = entitySection.getConfigurationSection("Advanced");
            String locS = entitySection.getString(key + ".Base");
            String[] locS2 = locS.split(" ");
            Location location = new Location(center.getWorld(), Float.valueOf(locS2[0]), Float.valueOf(locS2[1]), Float.valueOf(locS2[2]));
            EntityType entityType = EntityType.valueOf(locS2[3]);
            center.add(location);

            location.getWorld().spawnEntity(center.clone(), entityType);
            center.subtract(location);
        });
    }

    public Collection<StructureBlock> getBlocks(Location center) {
        Collection<StructureBlock> map = new ArrayList<>();
        ConfigurationSection config = getConfig().getConfigurationSection("Blocks");
        for(String key : config.getKeys(false)) {
            String locS = config.getString(key);
            String[] locS2 = locS.split(" ");
            Location location = new Location(center.getWorld(), Integer.valueOf(locS2[0]), Integer.valueOf(locS2[1]), Integer.valueOf(locS2[2]));
            BlockData blockData = Bukkit.createBlockData(locS2[3]);
            center.add(location);
            map.add(new StructureBlock(center.clone(), blockData));
            center.subtract(location);
        }
        return map;
    }

    public void setGenerate(boolean b) {
        getConfig().set("Settings.Generate", b);
        saveData();
    }

    public void setChance(double chance) {
        getConfig().set("Settings.GenerationChance", chance);
        saveData();
    }

    public boolean isGenerating() {
        return getConfig().getBoolean("Settings.Generate", false);
    }

    public double getGenerationChance() {
        return getConfig().getDouble("Settings.GenerationChance", 0);
    }
}
