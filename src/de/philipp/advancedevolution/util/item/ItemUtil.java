package de.philipp.advancedevolution.util.item;

import de.philipp.advancedevolution.lib.xseries.XMaterial;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemUtil {

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

    public static ItemStack createSimpleStack(String displayName, XMaterial material, String ... strings) {
        ItemStack stack = new ItemStack(material.parseMaterial(), 1);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(Arrays.asList(strings));
        stack.setItemMeta(meta);
        return stack;
    }

}
