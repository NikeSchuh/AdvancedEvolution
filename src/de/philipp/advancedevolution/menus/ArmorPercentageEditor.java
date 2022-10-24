package de.philipp.advancedevolution.menus;

import de.philipp.advancedevolution.items.DraconicArmorItem;
import de.philipp.advancedevolution.items.IPercentageModifier;
import de.philipp.advancedevolution.items.armormodifier.FlightSpeedModifier;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.NumberConverter;
import de.philipp.advancedevolution.util.gui.Menu;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.NumberConversions;

public class ArmorPercentageEditor extends Menu {

    private IPercentageModifier percentageModifier;
    private final DraconicArmorItem armorItem;

    private final double max;
    private final double min;
    private final double perStep;
    private double current;


    public ArmorPercentageEditor(Plugin pl, Player player, DraconicArmorItem armorItem, int steps, IPercentageModifier modifier) {
        super(pl, player);
        this.percentageModifier = modifier;
        this.armorItem = armorItem;

        this.max = percentageModifier.getMaximumValue();
        this.min = percentageModifier.getMinimumValue();
        this.current = percentageModifier.getValue(armorItem.getCurrentStack());

        this.perStep = max / steps;
    }

    @Override
    public String getTitle() {
        return "Percentage Editor";
    }

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 5 * 9, getTitle());

        inventory.setItem(22, ItemUtil.createSimpleStack("§aInfo", XMaterial.OAK_SIGN, "", "§7Currently : §a" + NumberConverter.asPercent(current) + " §7/ §a" + NumberConverter.asPercent(max), "§7Add / Remove : §a" + NumberConverter.asPercent(perStep), "§aAdd §ewith Left Click", "§cRemove §ewith Right Click "));

        return inventory;
    }

    public void updateInventory() {
        inv.setItem(22, ItemUtil.createSimpleStack("§aInfo", XMaterial.OAK_SIGN, "", "§7Currently : §a" + NumberConverter.asPercent(current) + " §7/ §a" + NumberConverter.asPercent(max), "§7Add / Remove : §a" + NumberConverter.asPercent(perStep), "§aAdd §ewith Left Click", "§cRemove §ewith Right Click "));
    }


    @Override
    public void onClick(InventoryClickEvent event, Player player, ItemStack clicked) {
        event.setCancelled(true);
        if(clicked != null) {
            if(event.getClick() == ClickType.LEFT) {
                if(!(current + perStep > max))  {
                    current += perStep;
                } else {
                    current = max;
                }
                percentageModifier.setValue(current, armorItem.getCurrentStack());
            } else if(event.getClick() == ClickType.RIGHT) {
                if(!(current - perStep < min))  {
                    current -= perStep;
                } else {
                    current = min;
                }
                percentageModifier.setValue(current, armorItem.getCurrentStack());
            }
        }

        updateInventory();
    }
}
