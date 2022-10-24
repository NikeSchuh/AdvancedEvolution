package de.philipp.advancedevolution.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class SoulboundEnchantment extends Enchantment {

    private final String name;
    private final int maxLvl;

    public SoulboundEnchantment(String namespace, String name, int level) {
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
        return EnchantmentTarget.ALL;
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
        return enchantment.equals(Enchantment.VANISHING_CURSE);
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        if(itemStack.getType().isBlock()) return false;
        if(itemStack.getType().isBurnable()) return false;
        return true;
    }
}
