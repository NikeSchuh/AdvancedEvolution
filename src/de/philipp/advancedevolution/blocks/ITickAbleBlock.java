package de.philipp.advancedevolution.blocks;


import org.bukkit.block.Block;

public interface ITickAbleBlock {

    void tickSync(Block block, DraconicMachine draconicMachine);
    void tickAsync(Block block, DraconicMachine draconicMachine);

}
