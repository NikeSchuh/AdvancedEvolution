package de.philipp.advancedevolution.dimension.generator;

import de.philipp.advancedevolution.util.RandomUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public interface OreRegistryEntry {

    int getSpawnTries();
    int getSpawnChance();
    int getExpandingChance();
    int getOreAmount();
    Material getSpawnInMaterial();
    Material getOreMaterial();

    default void tryGenerateOre(World world, Random random, Chunk chunk, int maxHeight, int minHeight) {
        for(int i = 0; i < getSpawnTries(); i++) {
            if (random.nextInt(100) < getSpawnChance()) {
                Location location = new Location(world, chunk.getX() * 16, random.nextInt(maxHeight - minHeight) + minHeight, chunk.getZ() * 16);
                generateVein(getOreMaterial(), location.getBlock(), getOreAmount());
            }
        }
    }

    default void generateVein(final Material material, final Block startBlock, final int nbrBlocks) {
        final List<Block> blocks = this.getAdjacentsBlocks(startBlock, nbrBlocks);
        for (final Block block : blocks) {
            block.setType(material);
        }
    }


    default List<Block> getAdjacentsBlocks(final Block startBlock, final int nbrBlocks) {
        int failedAttempts = 0;
        final List<Block> adjacentBlocks = new ArrayList<Block>();
        adjacentBlocks.add(startBlock);
        while (adjacentBlocks.size() < nbrBlocks && failedAttempts < 25) {
            final Block block = adjacentBlocks.get(RandomUtils.randomInteger(0, adjacentBlocks.size() - 1));
            final BlockFace face = RandomUtils.randomAdjacentFace();
            final Location blockLocation = block.getLocation();
            if ((blockLocation.getBlockY() <= 1 && face.equals((Object)BlockFace.DOWN)) || (blockLocation.getBlockY() >= 255 && face.equals((Object)BlockFace.UP))) {
                ++failedAttempts;
            }
            else {
                final Block adjacent = block.getRelative(face);
                if (adjacentBlocks.contains(adjacent) /*|| !adjacent.getType().equals((Object)Material.STONE)*/) {
                    ++failedAttempts;
                }
                else {
                    adjacentBlocks.add(adjacent);
                }
            }
        }
        return adjacentBlocks;
    }

}
