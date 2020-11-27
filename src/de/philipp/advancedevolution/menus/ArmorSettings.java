package de.philipp.advancedevolution.menus;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.items.ArmorModifier;
import de.philipp.advancedevolution.items.DraconicArmorItem;
import de.philipp.advancedevolution.items.custombases.DraconicArmorItemBase;
import de.philipp.advancedevolution.shield.Shield;
import de.philipp.advancedevolution.shield.ShieldManager;
import de.philipp.advancedevolution.util.gui.Menu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;

public class ArmorSettings extends Menu {

    private Shield shieldInstance;
    private DraconicArmorItem armorItem;
    private DraconicArmorItemBase armorItemBase;

    public ArmorSettings(Plugin pl, Player player, DraconicArmorItem armorItem) {
        super(pl, player);
        this.armorItem = armorItem;
        this.armorItemBase = (DraconicArmorItemBase) armorItem.getItemBase();
        this.shieldInstance = ShieldManager.playerShieldMap.get(player);
    }

    @Override
    public String getTitle() {
        return AdvancedEvolution.ChatPrefix +  "";
    }

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 5*9, getTitle());

        int count = 10;
        for(ArmorModifier am : armorItemBase.getModifiers()) {
            ItemStack stack = new ItemStack(am.getIcon().parseMaterial(), 1);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName("§3" + am.getName());
            ArrayList<String> lore = new ArrayList<>();
            Arrays.asList(am.getDescription()).forEach(key -> {
                lore.add("§7" + key);
            });
            meta.setLore(lore);
            stack.setItemMeta(meta);
            inventory.setItem(count, stack);
            count++;
        }



        return inventory;
    }

    @Override
    public void onClick(InventoryClickEvent clickEvent, Player player, ItemStack clicked) {
        clickEvent.setCancelled(true);
        if(clicked != null) {
            for(ArmorModifier modifier : armorItemBase.getModifiers()) {
                if(clicked.getType() == modifier.getIcon().parseMaterial()) {
                    close();
                    if(modifier.getEditType() == EditType.ATTRIBUTE_EDITOR) {
                        new ArmorAttributePropertiesEditor(plugin, player, armorItem, modifier).open();
                    }
                    return;
                }
            }
        }
    }
}
