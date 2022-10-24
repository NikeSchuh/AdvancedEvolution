package de.philipp.advancedevolution.blocks.blocks;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.blocks.DraconicBlock;
import de.philipp.advancedevolution.blocks.DraconicMachine;
import de.philipp.advancedevolution.blocks.custombases.DraconicMachineBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.*;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DraconicPortal extends DraconicMachineBase {

    public static List<Player> justTeleported = new ArrayList<>();

    @Override
    public void onPlace(DraconicBlock block, Cancellable cancellable, Player player) {
        if(!AdvancedEvolution.getDimensionManager().isDraconicDimensionEnabled()) {
            cancellable.setCancelled(true);
            player.sendMessage("§cThe dimension is sadly disabled on this server ;C");
        } else {
            if(player.getWorld().getUID().equals(AdvancedEvolution.getDimensionManager().draconic.getWorld().getUID())) {
                player.sendMessage("§cNo. Rip u xd");
                cancellable.setCancelled(true);
                return;
            }
            block.getBlock().getWorld().playSound(block.getBlock().getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 10f, 0.1f);
            block.getBlock().getWorld().spawnParticle(Particle.FIREWORKS_SPARK, block.getBlock().getLocation().add(0.5, 2, 0.5), 500);
        }
    }

    @Override
    public void onBreak(DraconicBlock block, Cancellable cancellable, Player player) {

    }

    @Override
    public void onBlockInteract(Player player, Cancellable cancellable, DraconicBlock block) {
        if(player.isSneaking()) {
            if(player.getInventory().getItemInMainHand() != null || player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                return;
            }
        }
        cancellable.setCancelled(true);
    }

    @Override
    public double getEnergyCapacity() {
        return 0;
    }

    @Override
    public boolean isEnergyPullAble() {
        return false;
    }

    @Override
    public boolean isEnergyAcceptable() {
        return false;
    }

    @Override
    public void onEnergyReceive(DraconicMachine sender, DraconicMachine receiver, double amount, BlockFace senderDirection) {

    }

    @Override
    public boolean isRestricted() {
        return true;
    }

    private double t = 0;

    @Override
    public void tickSync(Block block, DraconicMachine draconicMachine) {
        if (!AdvancedEvolution.getDimensionManager().isDraconicDimensionEnabled()) return;
        if(block.isBlockPowered()) return;
        for (Entity entity : block.getWorld().getNearbyEntities(block.getLocation().add(0.5, 2, 0.5), 0.5, 1, 0.5)) {
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                if (livingEntity.getWorld().getUID().equals(AdvancedEvolution.getDimensionManager().draconic.getWorld().getUID())) {
                    double x = livingEntity.getLocation().getX() * 32;
                    double z = livingEntity.getLocation().getZ() * 32;
                    int Y;
                    for (Y = 256; AdvancedEvolution.getDimensionManager().draconic.getWorld().getBlockAt((int) x, Y, (int) z).getType() == Material.AIR; Y--)
                        if (Y < 0) {
                            Y = 100;
                        }
                    livingEntity.teleport(new Location(Bukkit.getWorlds().get(0), x, Y, z));
                    return;
                } else {
                    double x = livingEntity.getLocation().getX() / 32;
                    double z = livingEntity.getLocation().getZ() / 32;
                    int Y;
                    for (Y = 98; AdvancedEvolution.getDimensionManager().draconic.getWorld().getBlockAt((int) x, Y, (int) z).getType() == Material.AIR; Y--)
                        livingEntity.getWorld().spawnParticle(Particle.WARPED_SPORE, livingEntity.getLocation(), 50);
                        livingEntity.getWorld().playSound(livingEntity.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 5f, 2f);
                        livingEntity.teleport(new Location(AdvancedEvolution.getDimensionManager().draconic.getWorld(), x, Y, z));
                    if (livingEntity instanceof Player) {
                        Player player = (Player) entity;
                        player.playSound(livingEntity.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, 5f, 2f);
                    }
                }
            }
        }
        for(int i = 0; i < 20; i++) {
            Location location = block.getLocation().add(0.5, 1, 0.5);
            double r = 1;
            t = (t + Math.PI / 32);
            double x = r * Math.cos(t);
            double y = Math.atan(Math.cos(t));
            double z = r * Math.sin(t);
            location.add(x, y, z);
            block.getWorld().spawnParticle(Particle.PORTAL, location, 5, 0, 0, 0, 0.001f);
            location.subtract(x, y, z);
            if (t > Math.PI * 8) {
                t = 0;
            }
        }
        if (Math.random() < 0.01) {
            block.getWorld().playSound(block.getLocation(), Sound.BLOCK_PORTAL_AMBIENT, 1f, 0.1f);
        }
        if(Math.random() < 0.001) {
            block.getWorld().strikeLightningEffect(block.getLocation().add(0.5, 0.5, 0.5));
        }
    }

    @Override
    public void tickAsync(Block block, DraconicMachine draconicMachine) {

    }

    @Override
    public void onUnload(DraconicBlock block) {

    }

    @Override
    public String getDataName() {
        return "DraconicPortal1";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§6Draconic Portal", XMaterial.BEACON);
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}
