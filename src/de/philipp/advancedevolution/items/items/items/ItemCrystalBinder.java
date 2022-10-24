package de.philipp.advancedevolution.items.items.items;

import de.philipp.advancedevolution.blocks.CrystalLinkManager;
import de.philipp.advancedevolution.blocks.DraconicBlock;
import de.philipp.advancedevolution.blocks.DraconicMachine;
import de.philipp.advancedevolution.blocks.custombases.DraconicMachineBase;
import de.philipp.advancedevolution.crafting.DraconicKey;
import de.philipp.advancedevolution.crafting.DraconicRecipe;
import de.philipp.advancedevolution.events.customevents.DraconicItemInteractEvent;
import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.recipes.EnhancedRecipe;
import de.philipp.advancedevolution.recipes.VanillaIngredient;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.awt.image.ByteLookupTable;
import java.util.HashMap;
import java.util.UUID;

public class ItemCrystalBinder extends DraconicItemBase {

    private HashMap<UUID, Block> selectedBlock = new HashMap<>();

    @Override
    public String getDataName() {
        return "EnergyBinder";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§cEnergy Binder", XMaterial.STICK, "", "§aSHIFT + RIGHTCLICK §7to select an CrystalManager", "§aSHIFT + RIGHTCLICK Air §7to deselect the manager", "§aRIGHTCLICK §7to link the block", "", "");
    }

    public void onInteract(DraconicItemInteractEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();
        player.closeInventory();
        UUID uuid = player.getUniqueId();
        if(event.getAction() == Action.RIGHT_CLICK_AIR) {
            if(selectedBlock.containsKey(uuid)) {
                selectedBlock.remove(uuid);
                player.sendMessage("§aSelected block has been cleared");
            }
        }
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(event.getPlayer().isSneaking()) {
                Block block = event.getClicked();
                if(DraconicBlock.isDraconicBlock(block)) {
                    DraconicBlock draconicBlock = new DraconicBlock(block);
                    if(draconicBlock.getBlockBase() instanceof DraconicMachineBase) {
                        DraconicMachine draconicMachine = new DraconicMachine(block);
                        DraconicMachineBase base = (DraconicMachineBase) draconicMachine.getBlockBase();
                        if(base.isLinkManager()) {
                            Location location = draconicBlock.getBlock().getLocation();
                            base.updateEnergyNetwork(draconicMachine);
                            player.sendMessage("§aSelected §e" + draconicMachine.getDataName() + " §aat §a(" + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + ")");
                            selectedBlock.put(uuid, draconicMachine.getBlock());
                        } else {
                            player.sendMessage("§cCannot be used as an Link Manager");
                        }
                    }
                }
            } else {
                if(selectedBlock.containsKey(uuid)) {
                    Block block = event.getClicked();
                    if (DraconicBlock.isDraconicBlock(block)) {
                        DraconicBlock draconicBlock = new DraconicBlock(block);
                        if (draconicBlock.getBlockBase() instanceof DraconicMachineBase) {
                            DraconicMachine draconicMachine = new DraconicMachine(block);
                            if (draconicMachine.isEnergyAcceptable()) {
                                DraconicMachine linkmanager = new DraconicMachine(selectedBlock.get(uuid));
                                CrystalLinkManager crystalLinkManager = new CrystalLinkManager(linkmanager);
                                DraconicMachineBase draconicMachineBase = (DraconicMachineBase) linkmanager.getBlockBase();
                                draconicMachineBase.updateEnergyNetwork(linkmanager);
                                Location location = draconicBlock.getBlock().getLocation();
                                if (!crystalLinkManager.contains(block)) {
                                    if (crystalLinkManager.acceptsLink()) {
                                        player.sendMessage("§aAdded " + draconicMachine.getDataName() + " §cat §e(" + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + ") §cto Link Manager (" + (crystalLinkManager.getCurrentLinks() + 1) + " / " + crystalLinkManager.getMaxLinks() + ")");
                                        crystalLinkManager.addLink(draconicMachine);
                                    } else {
                                        player.sendMessage("§c" + draconicBlock.getDataName() + " cannot accept more than " + crystalLinkManager.getMaxLinks());
                                    }
                                } else {
                                    crystalLinkManager.removeLink(crystalLinkManager.getLinkUniqueId(block));
                                    player.sendMessage("§cRemoved " + draconicMachine.getDataName() + " §cat §e(" + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + ") §cfrom Link Manager");
                                }
                            }
                        }
                    }
                } else {
                    player.sendMessage("§cYou need to select an Link Manager first");
                }
            }
        }
    }

    @Override
    public boolean isCraftAble() {
        return false;
    }

    @Override
    public EnhancedRecipe getCraftingRecipe() {
        EnhancedRecipe recipe = new EnhancedRecipe();
        recipe.addIngredient(new VanillaIngredient(XMaterial.STICK.parseMaterial(), 2));
        recipe.addIngredient(new VanillaIngredient(XMaterial.EMERALD.parseMaterial(), 1));
        return recipe;
    }

    /*/
    @Override
    public DraconicRecipe getCraftingRecipe() {
        DraconicRecipe recipe = new DraconicRecipe(new DraconicKey("energybinder"), "EnergyBinder");
        recipe.shape("  D", " S ", "E  ");
        recipe.setIngredient('D', XMaterial.HEART_OF_THE_SEA.parseMaterial());
        recipe.setIngredient('S', XMaterial.STICK.parseMaterial());
        recipe.setIngredient('E', XMaterial.ENDER_PEARL.parseMaterial());
        recipe.setDraconicItem(2, "DraconicCore");
        return recipe;
    }
    /*/

    @Override
    public boolean isStackable() {
        return true;
    }
}
