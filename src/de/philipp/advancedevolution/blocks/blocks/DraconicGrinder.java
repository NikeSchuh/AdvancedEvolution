package de.philipp.advancedevolution.blocks.blocks;

import com.mojang.datafixers.kinds.IdF;
import de.philipp.advancedevolution.blocks.DraconicBlock;
import de.philipp.advancedevolution.blocks.DraconicMachine;
import de.philipp.advancedevolution.blocks.EnergyInfo;
import de.philipp.advancedevolution.blocks.custombases.DraconicMachineBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Smoker;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.craftbukkit.libs.org.apache.maven.artifact.repository.metadata.Metadata;
import org.bukkit.entity.*;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.BoundingBox;

import java.awt.*;

public class DraconicGrinder extends DraconicMachineBase {
    @Override
    public void onUnload(DraconicBlock block) {

    }

    @Override
    public void onPlace(DraconicBlock block, Cancellable cancellable, Player player) {

    }

    @Override
    public void onBreak(DraconicBlock block, Cancellable cancellable, Player player) {
        EnergyInfo energyInfo = new EnergyInfo(new DraconicMachine(block.getBlock()));
        energyInfo.remove();
    }

    @Override
    public void onBlockInteract(Player player, Cancellable cancellable, DraconicBlock block) {
        cancellable.setCancelled(true);
    }

    @Override
    public double getEnergyCapacity() {
        return 5000000;
    }

    @Override
    public boolean isEnergyPullAble() {
        return false;
    }

    @Override
    public boolean isEnergyAcceptable() {
        return true;
    }

    @Override
    public void onEnergyReceive(DraconicMachine sender, DraconicMachine receiver, double amount, BlockFace senderDirection) {
        receiver.addEnergyStored(amount);
        EnergyInfo energyInfo = new EnergyInfo(receiver);
    }

    @Override
    public boolean isRestricted() {
        return false;
    }

    @Override
    public String getDataName() {
        return "DraconicGrinder";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§6Draconic Grinder", XMaterial.SMOKER, "", "§7This machine can deal", "§7nearly infinite damage.", "");
    }

    @Override
    public boolean isStackable() {
        return true;
    }

    @Override
    public long getSyncInterval() {
        return 5;
    }

    @Override
    public void tickSync(Block block, DraconicMachine grinder) {
        Directional directional = (Directional) block.getBlockData();
        Location location = block.getLocation().add(directional.getFacing().getDirection().multiply(4.5)).add(0.5, 3.50, 0.5);
        for(Entity entity : location.getWorld().getNearbyEntities(location, 4, 3.5, 4)) {
            if(entity.isDead()) continue;
            if(entity instanceof LivingEntity) {
                if(entity instanceof ArmorStand) continue;
                if(entity instanceof Player) continue;
                LivingEntity livingEntity = (LivingEntity) entity;
                if(livingEntity.isInvulnerable()) continue;
                if(livingEntity.isDead()) continue;
                if(!livingEntity.hasAI()) continue;
                if(livingEntity instanceof Wither) {
                    Wither wither = (Wither) livingEntity;
                    if(wither.getTicksLived() < 11 * 20) continue;
                }
                if(grinder.hasEnergy(livingEntity.getHealth() * 80)) {
                    grinder.removeEnergyStored(livingEntity.getHealth() * 80);
                    livingEntity.damage(livingEntity.getHealth(), null);
                }
            }
        }
        location.getWorld().spawnParticle(Particle.CRIT, location, 1, 0, 0, 0, 0);
        EnergyInfo energyInfo = new EnergyInfo(grinder);
    }

    @Override
    public void tickAsync(Block block, DraconicMachine draconicMachine) {

    }
}
