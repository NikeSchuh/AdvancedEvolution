package de.philipp.advancedevolution.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class BeheadingEnchantment extends Enchantment {

    private final String name;
    private final int maxLvl;

    public BeheadingEnchantment(String namespace, String name, int level) {
        super(NamespacedKey.minecraft(namespace));
        this.name = name;
        this.maxLvl = level;

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMaxLevel() {
        return maxLvl;
    }

    @Override
    public int getStartLevel() {
        return 0;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.WEAPON;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        if(itemStack.getType().name().contains("SWORD")) return true;
        return false;
    }
}
