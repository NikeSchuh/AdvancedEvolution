package de.philipp.advancedevolution.blocks;

import org.bukkit.block.BlockFace;

public interface IEnergyStorage {

    double getEnergyCapacity();
    boolean isEnergyPullAble();
    boolean isEnergyAcceptable();
    void onEnergyReceive(DraconicMachine sender, DraconicMachine receiver, double amount, BlockFace senderDirection);

}
