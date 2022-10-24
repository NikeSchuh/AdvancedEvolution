package de.philipp.advancedevolution.menus;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.items.IArmorAttibuteModifier;
import de.philipp.advancedevolution.items.DraconicArmorItem;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.gui.Menu;
import de.philipp.advancedevolution.util.item.ArmorType;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class ArmorAttributePropertiesEditor extends Menu {

    private DraconicArmorItem editedItem;
    private double currentValue;
    private double maximumValue;
    private double minimumValue;
    private IArmorAttibuteModifier modifier;
    private String attributeKey;

    public ArmorAttributePropertiesEditor(Plugin pl, Player player, DraconicArmorItem editedItem, IArmorAttibuteModifier modifier) {
        super(pl, player);
        this.editedItem = editedItem;
        this.modifier = modifier;

        for(AttributeModifier attributeModifier : editedItem.getCurrentStack().getItemMeta().getAttributeModifiers(modifier.getAttribute())) {
               this.currentValue = attributeModifier.getAmount();
               this.attributeKey = attributeModifier.getName();

        }

        this.maximumValue = 0.1 * (editedItem.getDraconicArmorType().getUpgradeLevel() + 1);
        this.minimumValue = 0;
    }

    @Override
    public String getTitle() {
        return AdvancedEvolution.ChatPrefix +  "Editor";
    }

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 5 * 9, getTitle());

        inventory.setItem(19, ItemUtil.createSimpleStack("§a+ 50%", XMaterial.EMERALD_BLOCK));
        inventory.setItem(22, ItemUtil.createSimpleStack("§aCurrent: " + (Math.round(currentValue * 1000) + 100)  + "%", XMaterial.YELLOW_CONCRETE));
        inventory.setItem(25, ItemUtil.createSimpleStack("§a- 50%", XMaterial.RED_CONCRETE));
        return inventory;
    }

    @Override
    public void onClick(InventoryClickEvent event, Player player, ItemStack clicked) {
        if(clicked == null) return;
        event.setCancelled(true);
        if(clicked.getType() == XMaterial.EMERALD_BLOCK.parseMaterial()) {
            currentValue += 0.05;
            if(currentValue >= maximumValue) {
                currentValue = maximumValue;
            }
            event.getInventory().setItem(22, ItemUtil.createSimpleStack("§aCurrent: " + (Math.round(currentValue * 1000) + 100) + "%", XMaterial.YELLOW_CONCRETE));
            setCurrent(editedItem.getCurrentStack());
        } else if(clicked.getType() == XMaterial.RED_CONCRETE.parseMaterial()) {
            this.currentValue -= 0.05;
            if(currentValue <= minimumValue) {
                currentValue = minimumValue;
            }
            event.getInventory().setItem(22, ItemUtil.createSimpleStack("§aCurrent: " + (Math.round(currentValue * 1000) + 100) + "%", XMaterial.YELLOW_CONCRETE));
            setCurrent(editedItem.getCurrentStack());
        }
    }

    public void setCurrent(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        meta.removeAttributeModifier(modifier.getAttribute());

        if(editedItem.getType() == ArmorType.HELMET) {
            AttributeModifier attributeModifier = new AttributeModifier(UUID.randomUUID(), attributeKey, currentValue, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
            meta.addAttributeModifier(modifier.getAttribute(), attributeModifier);
        }

        if(editedItem.getType() == ArmorType.CHESTPLATE) {
            AttributeModifier attributeModifier = new AttributeModifier(UUID.randomUUID(), attributeKey, currentValue, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
            meta.addAttributeModifier(modifier.getAttribute(), attributeModifier);
        }

        if(editedItem.getType() == ArmorType.LEGGINGS) {
            AttributeModifier attributeModifier = new AttributeModifier(UUID.randomUUID(), attributeKey, currentValue, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
            meta.addAttributeModifier(modifier.getAttribute(), attributeModifier);
        }

        if(editedItem.getType() == ArmorType.BOOTS) {
            AttributeModifier attributeModifier = new AttributeModifier(UUID.randomUUID(), attributeKey, currentValue, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
            meta.addAttributeModifier(modifier.getAttribute(), attributeModifier);
        }

        stack.setItemMeta(meta);

    }

}
