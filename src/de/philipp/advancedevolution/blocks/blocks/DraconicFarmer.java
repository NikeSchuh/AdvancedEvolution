package de.philipp.advancedevolution.blocks.blocks;

import de.philipp.advancedevolution.blocks.DraconicBlock;
import de.philipp.advancedevolution.blocks.DraconicMachine;
import de.philipp.advancedevolution.blocks.EnergyInfo;
import de.philipp.advancedevolution.blocks.InventoryContainer;
import de.philipp.advancedevolution.blocks.custombases.DraconicMachineBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.lib.xseries.XSound;
import de.philipp.advancedevolution.recipes.DraconicIngredient;
import de.philipp.advancedevolution.recipes.EnhancedRecipe;
import de.philipp.advancedevolution.recipes.VanillaIngredient;
import de.philipp.advancedevolution.util.Cuboid;
import de.philipp.advancedevolution.util.LocationUtil;
import de.philipp.advancedevolution.util.item.ItemUtil;
import de.tr7zw.nbtapi.utils.ApiMetricsLite;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.entity.*;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class DraconicFarmer extends DraconicMachineBase {

    private HashMap<Block, Integer> current = new HashMap<>();
    private HashMap<Block, List<Block>> blockCache = new HashMap<>();

    @Override
    public void onUnload(DraconicBlock block) {
        InventoryContainer container = new InventoryContainer(new DraconicBlock(block.getBlock()));
        container.syncInventoryWithContainer();
    }

    @Override
    public void onPlace(DraconicBlock block, Cancellable cancellable, Player player) {
        EnergyInfo energyInfo = new EnergyInfo(new DraconicMachine(block.getBlock()));
        InventoryContainer container = new InventoryContainer(new DraconicBlock(block.getBlock()));
        container.createInventory("Draconic Farmer", 5 * 9);
    }

    @Override
    public void onBreak(DraconicBlock block, Cancellable cancellable, Player player) {
        InventoryContainer container = new InventoryContainer(new DraconicBlock(block.getBlock()));
        container.syncInventoryWithContainer();
        EnergyInfo energyInfo = new EnergyInfo(new DraconicMachine(block.getBlock()));
        energyInfo.remove();
    }

    @Override
    public void onBlockInteract(Player player, Cancellable cancellable, DraconicBlock block) {
        if(player.isSneaking()) return;
        cancellable.setCancelled(true);
        InventoryContainer container = new InventoryContainer(new DraconicBlock(block.getBlock()));
        container.syncInventoryWithContainer();
        player.openInventory(container.getInventory());
    }

    @Override
    public double getEnergyCapacity() {
        return 200000;
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
    public long getSyncInterval() {
        return 18;
    }

    @Override
    public void tickSync(Block block, DraconicMachine draconicMachine) {
        EnergyInfo energyInfo = new EnergyInfo(draconicMachine);
        if(block.isBlockPowered()) return;
        InventoryContainer container = new InventoryContainer(draconicMachine);
        Directional directional = (Directional) block.getBlockData();
        if(isFull(container)) return;
        Location loc1 = block.getLocation().add(3, 0, -3);
        Location loc2 = block.getLocation().add(-3, 0, 3);;
        Cuboid cuboid = new Cuboid(block.getWorld(), loc1.getBlockX(), loc1.getBlockY(), loc1.getBlockZ(), loc2.getBlockX(), loc2.getBlockY(), loc2.getBlockZ());
        if(!blockCache.containsKey(block)) {
            blockCache.put(block,  cuboid.getBlocks());
        }
        int now = current.getOrDefault(block, 0);
        List<Block> blocks = blockCache.get(block);
        Block b =blocks.get(now);
        harvest(b, container, draconicMachine);
        b.getWorld().spawnParticle(Particle.DRIP_WATER, b.getLocation().add(0.5, 0.5, 0.5), 5, 0, 0, 0, 0.01);
        if((now + 1) >= blocks.size()) {
            current.put(block, 0);
            now = -1;
        }
        current.put(block, now + 1);

    }

    public void harvest(Block block, InventoryContainer container, DraconicMachine machine) {
        if(block.getBlockData() instanceof Ageable) {
            Ageable ageable = (Ageable) block.getBlockData();
            if(ageable.getAge() == ageable.getMaximumAge()) {
                if(machine.hasEnergy(60)) {
                    block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(0.5, 0.5, 0.5), 30, 0.25, 0.25, 0.25, 0.1, block.getBlockData());
                    block.getWorld().playSound(block.getLocation(), XSound.BLOCK_CROP_BREAK.parseSound(), 1f, 1f);
                    block.breakNaturally();
                    for (Entity entity : block.getLocation().getWorld().getNearbyEntities(block.getLocation(), 1, 1, 1)) {
                        if (entity instanceof Item) {
                            Item item = (Item) entity;
                            if (!isFull(container)) {
                                container.getInventory().addItem(item.getItemStack().clone());
                                item.remove();
                            }
                        }
                    }
                    machine.removeEnergyStored(60);
                }

            }
        }
        if(block.getType() == Material.OAK_LOG) {
            Location loc1 = block.getLocation().add(5, 0, -5);
            Location loc2 = block.getLocation().add(-5, 10, 5);;
            Cuboid cuboid = new Cuboid(block.getWorld(), loc1.getBlockX(), loc1.getBlockY(), loc1.getBlockZ(), loc2.getBlockX(), loc2.getBlockY(), loc2.getBlockZ());
            for(Block harvest : cuboid.getBlocks()) {
                    if (harvest.getType() == Material.OAK_LEAVES) {
                        if(!machine.hasEnergy(256)) break;
                        machine.removeEnergyStored(256);
                        Location location = harvest.getLocation();
                        block.getWorld().spawnParticle(Particle.BLOCK_CRACK, harvest.getLocation().add(0.5, 0.5, 0.5), 30, 0.25, 0.25, 0.25, 0.1, harvest.getBlockData());
                        harvest.breakNaturally();
                        for (Entity entity : location.getWorld().getNearbyEntities(location, 1, 1, 1)) {
                            if (entity instanceof Item) {
                                Item item = (Item) entity;
                                if (!isFull(container)) {
                                    container.getInventory().addItem(item.getItemStack().clone());
                                    item.remove();
                                }
                            }
                        }
                    } else if (harvest.getType() == Material.OAK_LOG) {
                        if(!machine.hasEnergy(256)) break;
                        machine.removeEnergyStored(256);
                        Location location = harvest.getLocation();
                        harvest.getWorld().playSound(harvest.getLocation(), XSound.BLOCK_WOOD_BREAK.parseSound(), 10f, 1f);
                        block.getWorld().spawnParticle(Particle.BLOCK_CRACK, harvest.getLocation().add(0.5, 0.5, 0.5), 30, 0.25, 0.25, 0.25, 0.1, harvest.getBlockData());
                        harvest.breakNaturally();
                        for (Entity entity : location.getWorld().getNearbyEntities(location, 1, 1, 1)) {
                            if (entity instanceof Item) {
                                Item item = (Item) entity;
                                if (!isFull(container)) {
                                    container.getInventory().addItem(item.getItemStack().clone());
                                    item.remove();
                                }
                            }
                        }
                    }else if (harvest.getType() == Material.BEE_NEST) {
                        if(!machine.hasEnergy(256)) break;
                        machine.removeEnergyStored(256);
                        Location location = harvest.getLocation();
                        harvest.getWorld().playSound(harvest.getLocation(), XSound.BLOCK_WOOD_BREAK.parseSound(), 10f, 1f);
                        block.getWorld().spawnParticle(Particle.BLOCK_CRACK, harvest.getLocation().add(0.5, 0.5, 0.5), 30, 0.25, 0.25, 0.25, 0.1, harvest.getBlockData());
                        harvest.breakNaturally();
                        for (Entity entity : location.getWorld().getNearbyEntities(location, 1, 1, 1)) {
                            if (entity instanceof Item) {
                                Item item = (Item) entity;
                                if (!isFull(container)) {
                                    container.getInventory().addItem(item.getItemStack().clone());
                                    item.remove();
                                }
                            }
                        }
                    }
            }
            return;
        }
        if(machine.hasEnergy(120)) {
            if (block.isEmpty() || block.getType() == Material.AIR) {
                Block ground = block.getLocation().add(0, -1, 0).getBlock();
                if (ground.getType() == Material.DIRT || ground.getType() == Material.GRASS_BLOCK) {
                    ground.setType(Material.FARMLAND);
                    machine.removeEnergyStored(120);
                    ground.getWorld().playSound(ground.getLocation(), XSound.ITEM_HOE_TILL.parseSound(), 1f, 1f);
                } else if (ground.getType() == Material.FARMLAND) {
                    if (container.getInventory().contains(Material.WHEAT_SEEDS)) {
                        ground.getWorld().playSound(ground.getLocation(), XSound.ITEM_HOE_TILL.parseSound(), 1f, 1f);
                        block.setType(Material.WHEAT);
                        machine.removeEnergyStored(120);
                        container.removeItem(Material.WHEAT_SEEDS, 1);
                    } else if (container.getInventory().contains(Material.BEETROOT_SEEDS)) {
                        ground.getWorld().playSound(ground.getLocation(), XSound.ITEM_HOE_TILL.parseSound(), 1f, 1f);
                        block.setType(Material.BEETROOTS);
                        machine.removeEnergyStored(120);
                        container.removeItem(Material.BEETROOT_SEEDS, 1);
                    } else if (container.getInventory().contains(Material.SWEET_BERRIES)) {
                        ground.getWorld().playSound(ground.getLocation(), XSound.ITEM_HOE_TILL.parseSound(), 1f, 1f);
                        block.setType(Material.SWEET_BERRY_BUSH);
                        machine.removeEnergyStored(120);
                        container.removeItem(Material.SWEET_BERRIES, 1);
                    } else if (container.getInventory().contains(Material.CARROT)) {
                        ground.getWorld().playSound(ground.getLocation(), XSound.ITEM_HOE_TILL.parseSound(), 1f, 1f);
                        block.setType(Material.CARROTS);
                        machine.removeEnergyStored(120);
                        container.removeItem(Material.CARROT, 1);
                    } else if (container.getInventory().contains(Material.POTATO)) {
                        ground.getWorld().playSound(ground.getLocation(), XSound.ITEM_HOE_TILL.parseSound(), 1f, 1f);
                        block.setType(Material.POTATOES);
                        machine.removeEnergyStored(120);
                        container.removeItem(Material.POTATO, 1);
                    } else if (container.getInventory().contains(Material.OAK_SAPLING)) {
                        ground.getWorld().playSound(ground.getLocation(), XSound.ITEM_HOE_TILL.parseSound(), 1f, 1f);
                        block.setType(Material.OAK_SAPLING);
                        machine.removeEnergyStored(120);
                        container.removeItem(Material.OAK_SAPLING, 1);
                    }
                }
                return;
            }
        }
    }

    @Override
    public void tickAsync(Block block, DraconicMachine draconicMachine) {

    }

    @Override
    public String getDataName() {
        return "DraconicFarmer";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§6Draconic Farmer", XMaterial.BLAST_FURNACE);
    }


    @Override
    public boolean isStackable() {
        return true;
    }
}
