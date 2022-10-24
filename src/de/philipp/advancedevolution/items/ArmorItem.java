package de.philipp.advancedevolution.items;

import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.item.ArmorType;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.Color;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ArmorItem {

    private List lore;
    private ItemStack stack;

    public ArmorItem(Color armorColor, ArmorType type, int armor, int armorThoughness, String displayName, String ... strings) {
        this.lore = Arrays.asList(strings);
        ItemStack baseStack = new ItemStack(XMaterial.valueOf("LEATHER_" + type.name()).parseMaterial(), 1);
        ItemUtil.setUnbreakable(baseStack);
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) baseStack.getItemMeta();
        leatherArmorMeta.setColor(armorColor);
        leatherArmorMeta.setLore(lore);
        leatherArmorMeta.setDisplayName(displayName);

        if(type == ArmorType.HELMET) {
            AttributeModifier armorModifier = new AttributeModifier(UUID.randomUUID(),
                    "generic.armor",
                    armor,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.HEAD);
            AttributeModifier toughnessModifier = new AttributeModifier(UUID.randomUUID(),
                    "generic.armorToughness",
                    armorThoughness,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.HEAD);

            leatherArmorMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier);
            leatherArmorMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughnessModifier);
        }

        if(type == ArmorType.CHESTPLATE) {
            AttributeModifier armorModifier = new AttributeModifier(UUID.randomUUID(),
                    "generic.armor",
                    armor,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.CHEST);
            AttributeModifier toughnessModifier = new AttributeModifier(UUID.randomUUID(),
                    "generic.armorToughness",
                    armorThoughness,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.CHEST);

            leatherArmorMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier);
            leatherArmorMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughnessModifier);
        }

        if(type == ArmorType.LEGGINGS) {
            AttributeModifier armorModifier = new AttributeModifier(UUID.randomUUID(),
                    "generic.armor",
                    armor,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.LEGS);
            AttributeModifier toughnessModifier = new AttributeModifier(UUID.randomUUID(),
                    "generic.armorToughness",
                    armorThoughness,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.LEGS);

            leatherArmorMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier);
            leatherArmorMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughnessModifier);
        }

        if(type == ArmorType.BOOTS) {
            AttributeModifier armorModifier = new AttributeModifier(UUID.randomUUID(),
                    "generic.armor",
                    armor,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.FEET);
            AttributeModifier toughnessModifier = new AttributeModifier(UUID.randomUUID(),
                    "generic.armorToughness",
                    armorThoughness,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.FEET);

            leatherArmorMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier);
            leatherArmorMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughnessModifier);
        }



        baseStack.setItemMeta(leatherArmorMeta);
        this.stack = baseStack;

    }

    public ArmorItem(Color armorColor, ArmorType type, float movementSpeed, int armor, int armorThoughness, String displayName, String ... strings) {
        this.lore = Arrays.asList(strings);
        ItemStack baseStack = new ItemStack(XMaterial.valueOf("LEATHER_" + type.name()).parseMaterial(), 1);
        ItemUtil.setUnbreakable(baseStack);

        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) baseStack.getItemMeta();
        leatherArmorMeta.setColor(armorColor);
        leatherArmorMeta.setLore(lore);
        leatherArmorMeta.setDisplayName(displayName);

        if(type == ArmorType.HELMET) {
            AttributeModifier armorModifier = new AttributeModifier(UUID.randomUUID(),
                    "generic.armor",
                    armor,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.HEAD);
            AttributeModifier toughnessModifier = new AttributeModifier(UUID.randomUUID(),
                    "generic.armorToughness",
                    armorThoughness,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.HEAD);

            leatherArmorMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier);
            leatherArmorMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughnessModifier);
        }

        if(type == ArmorType.CHESTPLATE) {
            AttributeModifier armorModifier = new AttributeModifier(UUID.randomUUID(),
                    "generic.armor",
                    armor,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.CHEST);
            AttributeModifier toughnessModifier = new AttributeModifier(UUID.randomUUID(),
                    "generic.armorToughness",
                    armorThoughness,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.CHEST);

            leatherArmorMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier);
            leatherArmorMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughnessModifier);
        }

        if(type == ArmorType.LEGGINGS) {
            AttributeModifier armorModifier = new AttributeModifier(UUID.randomUUID(),
                    "generic.armor",
                    armor,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.LEGS);
            AttributeModifier toughnessModifier = new AttributeModifier(UUID.randomUUID(),
                    "generic.armorToughness",
                    armorThoughness,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.LEGS);

            AttributeModifier speedsModifier = new AttributeModifier(UUID.randomUUID(),
                    "generic.movementSpeed",
                    movementSpeed,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.LEGS);


            leatherArmorMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, speedsModifier);
            leatherArmorMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier);
            leatherArmorMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughnessModifier);
        }

        if(type == ArmorType.BOOTS) {
            AttributeModifier armorModifier = new AttributeModifier(UUID.randomUUID(),
                    "generic.armor",
                    armor,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.FEET);
            AttributeModifier toughnessModifier = new AttributeModifier(UUID.randomUUID(),
                    "generic.armorToughness",
                    armorThoughness,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.FEET);

            leatherArmorMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier);
            leatherArmorMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughnessModifier);
        }



        baseStack.setItemMeta(leatherArmorMeta);
        this.stack = baseStack;

    }

    public ArmorItem(Color armorColor, ArmorType type, double flightSpeed, int armor, int armorThoughness, String displayName, String ... strings) {
        this.lore = Arrays.asList(strings);
        ItemStack baseStack = new ItemStack(XMaterial.valueOf("LEATHER_" + type.name()).parseMaterial(), 1);
        ItemUtil.setUnbreakable(baseStack);

        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) baseStack.getItemMeta();
        leatherArmorMeta.setColor(armorColor);
        leatherArmorMeta.setLore(lore);
        leatherArmorMeta.setDisplayName(displayName);

        if(type == ArmorType.HELMET) {
            AttributeModifier armorModifier = new AttributeModifier(UUID.randomUUID(),
                    "generic.armor",
                    armor,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.HEAD);
            AttributeModifier toughnessModifier = new AttributeModifier(UUID.randomUUID(),
                    "generic.armorToughness",
                    armorThoughness,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.HEAD);

            leatherArmorMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier);
            leatherArmorMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughnessModifier);
        }

        if(type == ArmorType.CHESTPLATE) {
            AttributeModifier armorModifier = new AttributeModifier(UUID.randomUUID(),
                    "generic.armor",
                    armor,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.CHEST);
            AttributeModifier toughnessModifier = new AttributeModifier(UUID.randomUUID(),
                    "generic.armorToughness",
                    armorThoughness,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.CHEST);
            AttributeModifier speedsModifier = new AttributeModifier(UUID.randomUUID(),
                    "generic.flyingSpeed",
                    flightSpeed,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.CHEST);

            leatherArmorMeta.addAttributeModifier(Attribute.GENERIC_FLYING_SPEED, speedsModifier);
            leatherArmorMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier);
            leatherArmorMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughnessModifier);
        }

        if(type == ArmorType.LEGGINGS) {
            AttributeModifier armorModifier = new AttributeModifier(UUID.randomUUID(),
                    "generic.armor",
                    armor,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.LEGS);
            AttributeModifier toughnessModifier = new AttributeModifier(UUID.randomUUID(),
                    "generic.armorToughness",
                    armorThoughness,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.LEGS);



            leatherArmorMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier);
            leatherArmorMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughnessModifier);
        }

        if(type == ArmorType.BOOTS) {
            AttributeModifier armorModifier = new AttributeModifier(UUID.randomUUID(),
                    "generic.armor",
                    armor,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.FEET);
            AttributeModifier toughnessModifier = new AttributeModifier(UUID.randomUUID(),
                    "generic.armorToughness",
                    armorThoughness,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.FEET);

            leatherArmorMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier);
            leatherArmorMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughnessModifier);
        }



        baseStack.setItemMeta(leatherArmorMeta);
        this.stack = baseStack;

    }

    public ItemStack getStack() {
        return stack;
    }

}
