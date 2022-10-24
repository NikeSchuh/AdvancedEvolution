package de.philipp.advancedevolution.util.item;

import de.philipp.advancedevolution.enchantments.DraconicEnchantments;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class ItemUtil {

    private static final Random random = new Random();

    public static void setUnbreakable(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
    }

    public static void setDurability(int s, ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        if(meta instanceof Damageable){
            Damageable durability = (Damageable) meta;
            durability.setDamage(stack.getType().getMaxDurability() - s);
            stack.setItemMeta(meta);
        }
    }

    public static ItemStack randomEnchantedBook(int maxLevel) {
        ItemStack stack = XMaterial.ENCHANTED_BOOK.parseItem();
        EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        Enchantment enchantment = Enchantment.values()[random.nextInt(Enchantment.values().length)];
        if(DraconicEnchantments.isDraconicEnchant(enchantment)) return randomEnchantedBook(maxLevel);
        int level;
        if(maxLevel > enchantment.getMaxLevel()) {
            level = random.nextInt(enchantment.getMaxLevel()) + 1;
        } else {
            level = random.nextInt(maxLevel) + 1;
        }
        enchantmentStorageMeta.addStoredEnchant(enchantment, level, false);
        stack.setItemMeta(enchantmentStorageMeta);
        return stack;
    }

    public static ItemStack createSimpleStack(String displayName, XMaterial material, String ... strings) {
        ItemStack stack = new ItemStack(material.parseMaterial(), 1);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(Arrays.asList(strings));
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack createSimpleStack(String displayName, Material material, String ... strings) {
        ItemStack stack = new ItemStack(material, 1);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(Arrays.asList(strings));
        stack.setItemMeta(meta);
        return stack;
    }

}
