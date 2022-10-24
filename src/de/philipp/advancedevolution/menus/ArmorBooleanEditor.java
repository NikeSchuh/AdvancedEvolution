package de.philipp.advancedevolution.menus;

import de.philipp.advancedevolution.items.IItemBooleanModifier;
import de.philipp.advancedevolution.items.DraconicArmorItem;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.gui.Menu;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ArmorBooleanEditor extends Menu {

    private IItemBooleanModifier modifier;
    private boolean current;
    private DraconicArmorItem editedItem;

    public ArmorBooleanEditor(Plugin handle, Player player, DraconicArmorItem editedItem, IItemBooleanModifier modifier) {
        super(handle, player);
        this.modifier = modifier;
        this.editedItem = editedItem;
        this.current = modifier.getValue(editedItem.getCurrentStack());
    }

    @Override
    public String getTitle() {
        return "Attribute Editor Boolean";
    }

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 5 * 9, getTitle());

        inventory.setItem(19, ItemUtil.createSimpleStack("§aActivate", XMaterial.EMERALD_BLOCK));

        if(current) {
            inventory.setItem(22, ItemUtil.createSimpleStack("§eStatus : Activated", XMaterial.YELLOW_CONCRETE));
        } else {
            inventory.setItem(22, ItemUtil.createSimpleStack("§eStatus : Deactivated", XMaterial.YELLOW_CONCRETE));
        }

        inventory.setItem(25, ItemUtil.createSimpleStack("§cDeactivate", XMaterial.REDSTONE_BLOCK));
        return inventory;
    }

    @Override
    public void onClick(InventoryClickEvent event, Player player, ItemStack clicked) {
        event.setCancelled(true);
        if(event.getSlot() == 19) {
            modifier.setValue(true, editedItem.getCurrentStack());
            current = true;
            if(current) {
                inv.setItem(22, ItemUtil.createSimpleStack("§eStatus : Activated", XMaterial.YELLOW_CONCRETE));
            } else {
                inv.setItem(22, ItemUtil.createSimpleStack("§eStatus : Deactivated", XMaterial.YELLOW_CONCRETE));
            }
        } else if(event.getSlot() == 25) {
            modifier.setValue(false, editedItem.getCurrentStack());
            current = false;
            if(current) {
                inv.setItem(22, ItemUtil.createSimpleStack("§eStatus : Activated", XMaterial.YELLOW_CONCRETE));
            } else {
                inv.setItem(22, ItemUtil.createSimpleStack("§eStatus : Deactivated", XMaterial.YELLOW_CONCRETE));
            }
        }
    }
}
