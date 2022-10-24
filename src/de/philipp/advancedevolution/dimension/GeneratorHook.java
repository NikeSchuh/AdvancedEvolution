package de.philipp.advancedevolution.dimension;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.structures.FloatingIsle;
import de.philipp.advancedevolution.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.plugin.Plugin;

import java.util.Random;

public class GeneratorHook implements Listener {

    private Plugin plugin;

    public Random rand = new Random();

    public GeneratorHook(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onGenerate(ChunkLoadEvent event) {
        if(event.isNewChunk()) {
            if(Math.random() < 0.0005) {
                if(event.getWorld().getEnvironment() == World.Environment.THE_END) {
                    new FloatingIsle().generate(LocationUtil.randomChunkBlockLocation(event.getChunk(), 110, 80), rand);
                }
            }
            if (Math.random() < 0.0005) {
                if (event.getWorld().getEnvironment() == World.Environment.THE_END) {
                    Chunk chunk = event.getChunk();
                    Location randLoc = chunk.getBlock(rand.nextInt(16), rand.nextInt(20) + 80, rand.nextInt(16)).getLocation();
                    new Meteor().generate(event.getWorld(), rand, randLoc);
                }
            }
        }
    }

}
