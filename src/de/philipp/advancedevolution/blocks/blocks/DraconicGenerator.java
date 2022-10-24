package de.philipp.advancedevolution.blocks.blocks;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.blocks.*;
import de.philipp.advancedevolution.blocks.custombases.DraconicMachineBase;
import de.philipp.advancedevolution.entities.particles.EnergyOrb;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.lib.xseries.XSound;
import de.philipp.advancedevolution.recipes.EnhancedRecipe;
import de.philipp.advancedevolution.recipes.VanillaIngredient;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DraconicGenerator extends DraconicMachineBase {

    private HashMap<Block, Integer> burnTimeMap = new HashMap<>();
    private List<Block> receivers = new ArrayList<>();
    private HashMap<Block, HashMap<Block, Boolean>> receiverMap = new HashMap<>();
    
    private final double maxTransfer = 512;
    private final double energyPerTick = 30;

    @Override
    public void onPlace(DraconicBlock block, Cancellable cancellable, Player player) {
        InventoryContainer container = new InventoryContainer(block);
        container.createInventory("Draconic Generator I", 9);
        CrystalLinkManager crystalLinkManager = new CrystalLinkManager(new DraconicMachine(block.getBlock()));
        crystalLinkManager.createCrystalData(1);
    }

    @Override
    public void onBreak(DraconicBlock block, Cancellable cancellable, Player player) {
        EnergyInfo energyInfo = new EnergyInfo(new DraconicMachine(block.getBlock()));
        energyInfo.remove();
        InventoryContainer container = new InventoryContainer(block);
        container.syncInventoryWithContainer();
    }

    @Override
    public void onUnload(DraconicBlock block) {
        InventoryContainer container = new InventoryContainer(block);
        container.syncInventoryWithContainer();
    }

    public boolean sendEnergy(DraconicMachine receiver, DraconicMachine sender, double amount, BlockFace face) {
        if(!receiver.getBlock().getChunk().isLoaded()) return false;
        if(receiver.isEnergyAcceptable() && sender.isEnergyPullAble()) {
            receiverMap.get(sender.getBlock()).put(receiver.getBlock(), true);
            new EnergyOrb(AdvancedEvolution.getInstance(), receiver, sender, amount, face, 6) {
                @Override
                public void end(DraconicMachine draconicMachine) {
                    receiverMap.get(sender.getBlock()).remove(receiver.getBlock());
                }
            };
            return true;
        }
        return false;
    }

    @Override
    public void onBlockInteract(Player player, Cancellable cancellable, DraconicBlock block) {
        if(player.isSneaking()) {
            if(player.getInventory().getItemInMainHand().getType() != Material.AIR) return;
        }
        cancellable.setCancelled(true);
        InventoryContainer container = new InventoryContainer(block);
        container.syncInventoryWithContainer();
        player.openInventory(container.getInventory());
    }

    @Override
    public double getEnergyCapacity() {
        return 100000;
    }

    @Override
    public boolean isEnergyPullAble() {
        return true;
    }

    @Override
    public boolean isEnergyAcceptable() {
        return false;
    }

    @Override
    public boolean isLinkManager() {
        return true;
    }

    @Override
    public EnhancedRecipe getCraftingRecipe() {
        EnhancedRecipe recipe = new EnhancedRecipe();
        recipe.addIngredient(new VanillaIngredient(XMaterial.BLAST_FURNACE.parseMaterial(), 1));
        recipe.addIngredient(new VanillaIngredient(XMaterial.CHEST.parseMaterial(), 1));
        recipe.addIngredient(new VanillaIngredient(XMaterial.DIAMOND.parseMaterial(), 2));
        recipe.addIngredient(new VanillaIngredient(XMaterial.COMPARATOR.parseMaterial(), 3));
        recipe.addIngredient(new VanillaIngredient(XMaterial.SMOOTH_STONE.parseMaterial(), 4));
        recipe.addIngredient(new VanillaIngredient(XMaterial.WEEPING_VINES.parseMaterial(), 8));
        recipe.addIngredient(new VanillaIngredient(XMaterial.IRON_INGOT.parseMaterial(), 16));
        return recipe;
    }


    @Override
    public void onEnergyReceive(DraconicMachine sender, DraconicMachine receiver, double amount, BlockFace senderDirection) {
        receiver.addEnergyStored(amount);
    }


    @Override
    public boolean isRestricted() {
        return false;
    }

    @Override
    public String getDataName() {
        return "DraconicGenerator";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§cDraconic Generator", XMaterial.FURNACE, "", "§7This machine generates", "§c" + energyPerTick + "AE/t", "§7fueled with burnables.", "");
    }

    @Override
    public boolean isStackable() {
        return false;
    }

    @Override
    public void tickSync(Block block, DraconicMachine generator) {
        EnergyInfo energyInfo = new EnergyInfo(generator);
        InventoryContainer container = new InventoryContainer(generator);
        Integer burnTime = burnTimeMap.getOrDefault(block, 0);
        CrystalLinkManager crystalLinkManager = new CrystalLinkManager(generator);
        crystalLinkManager.validateLinks();

        if(!receiverMap.containsKey(block)) {
            receiverMap.put(block, new HashMap<>());
        }


        for(Block block1 : ((HashMap<Block, Boolean>) receiverMap.get(block).clone()).keySet()) {
            if(!block1.getChunk().isLoaded()) continue;
            if(block1.isEmpty() || block1.getType() == Material.AIR) {
                receiverMap.get(block).remove(block1);
            }
        }

        for(DraconicMachine connected : crystalLinkManager.getMachineLinks()) {
            if(!connected.getBlock().getChunk().isLoaded()) continue;
            if(receiverMap.get(block).containsKey(connected.getBlock())) continue;
            if(generator.hasEnergy(maxTransfer)) {
                if (connected.isEnoughCapacityFor(maxTransfer)) {
                    sendEnergy(connected, generator, maxTransfer, null);
                    generator.removeEnergyStored(maxTransfer);
                } else {
                    if(connected.getEnergyStored() == connected.getEnergyCapacity()) continue;
                    generator.removeEnergyStored(connected.getEnergySpaceLeft());
                    sendEnergy(connected, generator, connected.getEnergySpaceLeft(), null);
                }
            } else {
                break;
            }

        }
        if(generator.getEnergyStored() == generator.getEnergyCapacity()) {
            return;
        }
        if(burnTime == 0) {
            if (container.hasInventory()) {
                if(!container.getInventory().isEmpty()) {
                    for (ItemStack stack : container.getInventory().getContents()) {
                        if (stack == null) continue;
                        if (!(stack.getType().isFuel())) {
                            continue;

                        }
                        Material material = stack.getType();
                        if (material == Material.COAL) {
                            container.removeItem(material, 1);
                            burnTimeMap.put(block, 1600);
                            break;
                        } else if (material == Material.COAL_BLOCK) {
                            container.removeItem(material, 1);
                            burnTimeMap.put(block, 16000);
                            break;
                        } else if (material == Material.LAVA_BUCKET) {
                            container.removeItem(material, 1);
                            burnTimeMap.put(block, 20000);
                            break;
                        } else {
                            container.removeItem(material, 1);
                            burnTimeMap.put(block, 200);
                            break;
                        }


                    }
                }
            }
        } else {
            if(block.isBlockPowered() || block.isBlockIndirectlyPowered()) return;
            if(Math.random() < 0.2) {
                block.getWorld().playSound(block.getLocation(), XSound.BLOCK_BLASTFURNACE_FIRE_CRACKLE.parseSound(), 0.7f, (float) ((float) 1 + (Math.random() - Math.random())));
            }
            block.getWorld().spawnParticle(Particle.SMOKE_NORMAL, block.getLocation().add(0.5, 1, 0.5), 1, 0.25, 0, 0.25, 0.01);

            burnTimeMap.put(block, burnTime - 1);
            generator.addEnergyStored(energyPerTick);
        }
    }

    @Override
    public void tickAsync(Block block, DraconicMachine draconicMachine) {

    }

}
