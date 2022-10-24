package de.philipp.advancedevolution.structures;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

public class StructureBlock {

    private BlockData blockData;
    private Location location;

    public StructureBlock(Location location, BlockData blockData) {
        this.blockData = blockData;
        this.location = location;
    }

    public BlockData getBlockData() {
        return blockData;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
       return location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + " " + blockData.getAsString(true);
    }


}
