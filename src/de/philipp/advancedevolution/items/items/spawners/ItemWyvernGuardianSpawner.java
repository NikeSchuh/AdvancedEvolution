package de.philipp.advancedevolution.items.items.spawners;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.animations.animations.WyvernGuardianSpawn;
import de.philipp.advancedevolution.events.customevents.DraconicItemInteractEvent;
import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.recipes.DraconicIngredient;
import de.philipp.advancedevolution.recipes.EnhancedRecipe;
import de.philipp.advancedevolution.recipes.VanillaIngredient;
import de.philipp.advancedevolution.region.DraconicRegion;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public class ItemWyvernGuardianSpawner extends DraconicItemBase {
    @Override
    public String getDataName() {
        return "WyvernGuardianSummonItem";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§5Wyvern Guardian Egg", XMaterial.DRAGON_EGG, "", "§7Place this too summon", "§7the §5Wyvern Guardian §7here.", "", "§7Make sure there is space!", "§7It also could be explosive.", "");
    }

    @Override
    public boolean isStackable() {
        return false;
    }

    @Override
    public EnhancedRecipe getCraftingRecipe() {
        EnhancedRecipe recipe = new EnhancedRecipe();
        recipe.addIngredient(new VanillaIngredient(XMaterial.TOTEM_OF_UNDYING.parseMaterial(), 1));
        recipe.addIngredient(new VanillaIngredient(XMaterial.BEACON.parseMaterial(), 1));
        recipe.addIngredient(new VanillaIngredient(XMaterial.DRAGON_EGG.parseMaterial(), 1));
        recipe.addIngredient(new VanillaIngredient(XMaterial.GOLDEN_APPLE.parseMaterial(), 1));
        recipe.addIngredient(new VanillaIngredient(XMaterial.PRISMARINE.parseMaterial(), 4));
        recipe.addIngredient(new VanillaIngredient(XMaterial.NETHERITE_SCRAP.parseMaterial(), 8));
        recipe.addIngredient(new VanillaIngredient(XMaterial.DRAGON_BREATH.parseMaterial(), 16));
        recipe.addIngredient(new DraconicIngredient("WyvernCore", 4));
        recipe.addIngredient(new DraconicIngredient("DraconiumDust", 32));
        recipe.setDurationFactor(20);
        return recipe;
    }


    @Override
    public void onInteract(DraconicItemInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            DraconicRegion region = AdvancedEvolution.getRegionManager().getRegion(event.getClicked().getLocation());
            if(region == null || region.isAllowBossSpawning()) {
                WyvernGuardianSpawn wyvernGuardianSpawn = new WyvernGuardianSpawn(event.getClicked().getLocation().add(0.5, 2, 0.5));
                event.setCancelled(true);
                event.getItem().setAmount(event.getItem().getAmount() - 1);
            } else {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§cCannot spawn any boss here ;c");
            }
        } else {
            event.setCancelled(true);
        }
    }
}
