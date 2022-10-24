package de.philipp.advancedevolution.blocks.blocks;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.tr7zw.nbtapi.NBTCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

public abstract class DraconicalBlockEntity {

    private Location location;
    protected Block block;
    protected Material type;

    public DraconicalBlockEntity(Location location, XMaterial xMaterial) {
        this.location = location;
        this.block = location.getBlock();
        this.type = xMaterial.parseMaterial();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(AdvancedEvolution.getInstance(), new Runnable() {
            @Override
            public void run() {
                tick();
            }
        }, 0, getTickPeriod());
    }

    public long getTickPeriod() {
        return 1;
    }

    public Material getMaterial() {
        return block.getType();
    }

    public boolean isValid() {
        return getMaterial() == type;
    }

    public Location getLocation() {
        return block.getLocation();
    }

    public abstract void tick();
    public abstract void onPlace(BlockPlaceEvent event, Player player);
    public abstract void onBreak(BlockPlaceEvent event, Player player);




}
