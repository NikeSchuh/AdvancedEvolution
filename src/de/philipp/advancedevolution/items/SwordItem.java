package de.philipp.advancedevolution.items;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.UUID;

public class SwordItem {

    private String displayName;
    private int attackDamage;
    private SwordLevel swordLevel;
    private String[] lore;

    public SwordItem(String displayName, int attackDamage, SwordLevel swordLevel, String ... lore) {
        this.displayName = displayName;
        this.attackDamage = attackDamage;
        this.swordLevel = swordLevel;
        this.lore = lore;
    }

    public ItemStack getItem() {
        ItemStack stack = new ItemStack(swordLevel.getMaterial(), 1);
        ItemMeta meta = stack.getItemMeta();

        meta.setDisplayName(displayName);
        meta.setLore(Arrays.asList(lore));

        AttributeModifier attributeModifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", attackDamage, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, attributeModifier);

        stack.setItemMeta(meta);
        return stack;
    }



}
