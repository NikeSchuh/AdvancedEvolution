package de.philipp.advancedevolution.crafting;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.animations.Delay;
import de.philipp.advancedevolution.animations.animations.CraftingAnimation;
import de.philipp.advancedevolution.blocks.DraconicBlock;
import de.philipp.advancedevolution.blocks.blocks.DraconicWorkbench;
import de.philipp.advancedevolution.items.DraconicItem;
import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.lib.xseries.XSound;
import de.philipp.advancedevolution.recipes.DraconicIngredient;
import de.philipp.advancedevolution.recipes.EnhancedRecipe;
import de.philipp.advancedevolution.recipes.VanillaIngredient;
import de.philipp.advancedevolution.util.gui.BoundPagedMenu;
import de.philipp.advancedevolution.util.gui.PagedMenu;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class DraconicCrafterGUI extends BoundPagedMenu<DraconicItemBase> {

    private DraconicBlock draconicBlock;

    public DraconicCrafterGUI(List<DraconicItemBase> elements, Plugin plugin, Player player, DraconicBlock draconicBlock) {
        super(elements, plugin, player);
        this.draconicBlock = draconicBlock;
    }

    @Override
    public String getTitle() {
        return "Crafting Table";
    }

    @Override
    public int getRows() {
        return 5;
    }

    @Override
    public Integer getMaxObjectsPerPage() {
        return 4*9;
    }

    @Override
    public ItemStack getObjectGeneratedStack(Object o) {
        DraconicItemBase itemBase = (DraconicItemBase) o;
        List<String> lore = new ArrayList<>();
        if(itemBase.isCraftAble()) {
            EnhancedRecipe recipe = itemBase.getCraftingRecipe();
            if(recipe == null) return ItemUtil.createSimpleStack("§4" + itemBase.getDataName(), XMaterial.BLACK_WOOL, "", "§7No recipe found!", "§7Make sure to return a recipe" + " §7in the item base.");
            ItemStack baseStack = itemBase.getBaseStack();
            ItemMeta meta = baseStack.getItemMeta();
            if(recipe.getVanillaIngredients().length > 0) {
                lore.add("");
                for (VanillaIngredient vanillaIngredient : recipe.getVanillaIngredients()) {
                    if (DraconicItem.containsAtLeastVanilla(player.getInventory(), vanillaIngredient.getMaterial(), vanillaIngredient.getAmount())) {
                        lore.add("§a" + vanillaIngredient.getAmount() + "x " + WordUtils.capitalizeFully(vanillaIngredient.getMaterial().name().replace("_", " ")));
                        continue;
                    }
                    lore.add("§8" + vanillaIngredient.getAmount() + "x " + WordUtils.capitalizeFully(vanillaIngredient.getMaterial().name().replace("_", " ")));
                }
            }
            if(recipe.getDraconicIngredients().length > 0) {
                lore.add("");
                for(DraconicIngredient draconicIngredient : recipe.getDraconicIngredients()) {
                    if(draconicIngredient.getBase() == null) {
                        lore.add("§cItem not registered/found");
                        continue;
                    }
                    if(DraconicItem.containsAtLeast(player.getInventory(), draconicIngredient.getBase(), draconicIngredient.getAmount())) {
                        lore.add("§a" + draconicIngredient.getAmount() +  "x " + ChatColor.stripColor(draconicIngredient.getBase().getBaseStack().getItemMeta().getDisplayName()));
                        continue;
                    }
                    lore.add("§8" + draconicIngredient.getAmount() +  "x " + ChatColor.stripColor(draconicIngredient.getBase().getBaseStack().getItemMeta().getDisplayName()));
                }
            }
            lore.add("");
            meta.setLore(lore);
            meta.setDisplayName("§aCraft " + meta.getDisplayName());
            baseStack.setItemMeta(meta);
            return baseStack;
        } else return ItemUtil.createSimpleStack(itemBase.getBaseStack().getItemMeta().getDisplayName(), XMaterial.RED_WOOL, "§cNot craftable");
    }

    @Override
    public long getUpdateDelay() {
        return 0;
    }

    @Override
    public boolean isRefreshing() {
        return false;
    }

    @Override
    public void onMissingPage(int page) {

    }

    @Override
    public HashMap<Integer, ItemStack> getBaseItems() {
        HashMap<Integer, ItemStack> baseItems = new HashMap<>();

        baseItems.put(36, ItemUtil.createSimpleStack("§7Back", XMaterial.ARROW));
        baseItems.put(44, ItemUtil.createSimpleStack("§7Next", XMaterial.ARROW));

        return baseItems;
    }

    @Override
    public void onClick(Player player, DraconicItemBase objClicked, InventoryClickEvent event) {
        event.setCancelled(true);
        if(event.getSlot() == 36) {
            if(page != 1) {
                player.playSound(player.getLocation(), XSound.ITEM_BOOK_PAGE_TURN.parseSound(), 10f, 0.8f);
                setPage(page - 1);
            }
            return;
        }
        if(event.getSlot() == 44) {
            if(hasPage(page +1)) {
                player.playSound(player.getLocation(), XSound.ITEM_BOOK_PAGE_TURN.parseSound(), 10f, 0.8f);
                setPage(page + 1);
            }
            return;
        }
        if(objClicked == null) return;
        if(objClicked.isCraftAble()) {
            EnhancedRecipe recipe = objClicked.getCraftingRecipe();
            HashMap<Material, Integer> vanillaMap = DraconicItem.map(player.getInventory());
            for(VanillaIngredient vanillaIngredient : recipe.getVanillaIngredients()) {
                if(!(vanillaMap.getOrDefault(vanillaIngredient.getMaterial(), 0) >= vanillaIngredient.getAmount())) {
                    player.sendMessage("§cYou are missing some vanilla ingredients!");
                    return;
                }
            }
            for(DraconicIngredient draconicIngredient : recipe.getDraconicIngredients()) {
                if(!DraconicItem.containsAtLeast(player.getInventory(), draconicIngredient.getBase(), draconicIngredient.getAmount())) {
                    player.sendMessage("§cYou are missing some draconic ingredients!");
                    return;
                }
            }

            // Player has everything

            // Removing Items
            for(DraconicIngredient draconicIngredient : recipe.getDraconicIngredients()) {
                DraconicItem.removeItem(player.getInventory(), draconicIngredient.getBase(), draconicIngredient.getAmount());
            }
            for(VanillaIngredient vanillaIngredient : recipe.getVanillaIngredients()) {
                DraconicItem.removeItem(player.getInventory(), vanillaIngredient.getMaterial(), vanillaIngredient.getAmount());
            }

            player.getWorld().playSound(player.getLocation(), XSound.BLOCK_ANVIL_USE.parseSound(), 1f, 0.5f);

            player.closeInventory();
            DraconicWorkbench.craftingMap.put(draconicBlock.getBlock(), true);
            CraftingAnimation craftingAnimation = new CraftingAnimation(AdvancedEvolution.getInstance(), draconicBlock.getBlock().getLocation().add(0.5, 2, 0.5), recipe.getDurationFactor()) {
                @Override
                public void animationEnd(Location location) {
                    DraconicWorkbench.craftingMap.put(draconicBlock.getBlock(), false);
                    Item item = player.getWorld().dropItem(draconicBlock.getBlock().getLocation().add(0.5, 5, 0.5), DraconicItem.instantiateTrueItem(objClicked.getDataName()).getCurrentStack());
                    item.setGravity(false);
                    item.setVelocity(new Vector(0, -0.075, 0));
                    location.getWorld().spawnParticle(Particle.ASH, item.getLocation(), 100, 0, 0, 0, 0.5f);
                    location.getWorld().spawnParticle(Particle.CRIMSON_SPORE, item.getLocation().add(0, 0.2, 0), 200, 0, 0, 0, 0.5f);
                    location.getWorld().playSound(item.getLocation(), XSound.BLOCK_PORTAL_TRAVEL.parseSound(), 0.1f, 1.2f);
                    new Delay(200) {
                        @Override
                        public void end() {
                            item.setGravity(true);
                        }
                    };
                }
            };

        }
    }
}
