package de.philipp.advancedevolution.entities.particles;

import de.philipp.advancedevolution.blocks.DraconicMachine;
import de.philipp.advancedevolution.blocks.custombases.DraconicMachineBase;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public abstract class EnergyOrb extends DraconicMachineChasingParticle {

    private double amount;
    private DraconicMachine sender;
    private BlockFace acceptor;

    public EnergyOrb(Plugin plugin, DraconicMachine receiver, DraconicMachine sender, double amount, BlockFace acceptor) {
        super(plugin, sender.getBlock().getLocation().add(0.5, 0.5, 0.5), receiver, new Vector(2, 2, 2));
        this.sender = sender;
        this.acceptor = acceptor;
        this.amount = amount;
    }

    public EnergyOrb(Plugin plugin, DraconicMachine receiver, DraconicMachine sender, double amount, BlockFace acceptor, double speed) {
        super(plugin, sender.getBlock().getLocation().add(0.5, 0.5, 0.5), receiver, new Vector(speed, speed, speed));
        this.sender = sender;
        this.acceptor = acceptor;
        this.amount = amount;
    }

    public abstract void end(DraconicMachine draconicMachine);

    @Override
    public void spawnParticle(Location location) {
        location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location, 1, 0,0, 0, 0);
    }

    @Override
    public void onCollision(Block block) {

    }

    @Override
    public void onHit(DraconicMachine receiver) {
        ((DraconicMachineBase) receiver.getBlockBase()).onEnergyReceive(sender, getTarget(), amount, acceptor);
        end(receiver);
        remove();
    }
}
