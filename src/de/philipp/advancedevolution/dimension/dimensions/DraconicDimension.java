package de.philipp.advancedevolution.dimension.dimensions;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.dimension.DraconicGenerator;
import de.philipp.advancedevolution.dimension.generator.Dimension;
import de.philipp.advancedevolution.dimension.generator.OreRegistryEntry;
import de.philipp.advancedevolution.dimension.ores.DraconiumOre;
import de.philipp.advancedevolution.dimension.util.Registry;
import de.philipp.advancedevolution.entities.DraconicEntity;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.EventListener;

public class DraconicDimension implements Dimension {

    public static boolean exist = false;
    public static final String prefix = "AE/DimensionBuilder ";

    private World world;
    private DimensionEventHandler eventHandler;

    public DraconicDimension() {
        if(exist) throw new IllegalArgumentException("Object reference already exists.");
        exist = true;
        Bukkit.getConsoleSender().sendMessage(prefix + "loading " + getName());
        eventHandler = new DraconicDimension.DimensionEventHandler(AdvancedEvolution.getInstance(), this);
        create();
    }

    @Override
    public Registry<OreRegistryEntry> getOreRegister() {
        Registry<OreRegistryEntry> registry = new Registry<>();
        registry.register(new DraconiumOre());
        return registry;
    }

    @Override
    public Registry<DraconicEntity> getEntityRegister() {
        return new Registry<>();
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public String getName() {
        return "draconic";
    }

    @Override
    public ChunkGenerator getGenerator() {
        return new DraconicGenerator(this);
    }

    @Override
    public void create() {
        WorldCreator worldCreator = new WorldCreator(getName());
        worldCreator.environment(World.Environment.NETHER);
        worldCreator.generator(getGenerator());
        this.world = worldCreator.createWorld();
    }

    public static class DimensionEventHandler implements Listener {

        private Dimension dimension;

        public DimensionEventHandler(Plugin plugin, DraconicDimension dimension) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
            this.dimension = dimension;
        }

        @EventHandler
        public void creatureSpawn(CreatureSpawnEvent event) {
            if(event.getEntity().getWorld().getUID().equals(dimension.getWorld().getUID())) {
               // event.setCancelled(false);
            }
        }

        @EventHandler
        public void onBlockBreak(BlockBreakEvent event) {
            if (event.getPlayer().getWorld().getUID().equals(dimension.getWorld().getUID())) {
                if (event.getBlock().getType() == XMaterial.RED_CONCRETE.parseMaterial()) {
                    event.setDropItems(false);
                }
            }
        }

    }
}
