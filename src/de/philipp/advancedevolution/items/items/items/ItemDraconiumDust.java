package de.philipp.advancedevolution.items.items.items;

import de.philipp.advancedevolution.crafting.DraconicKey;
import de.philipp.advancedevolution.events.customevents.DraconicItemInteractEvent;
import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.util.RayTraceResult;
import org.spigotmc.AsyncCatcher;

import java.util.List;

public class ItemDraconiumDust extends DraconicItemBase {

    @Override
    public String getDataName() {
        return "DraconiumDust";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§cDraconium Dust", XMaterial.REDSTONE, "", "§7Seems to be made", "§7by gods ...", "");
    }

    @Override
    public boolean isStackable() {
        return true;
    }

    public void onInteract(DraconicItemInteractEvent event) {
        event.setCancelled(true);
    }
}
