package de.philipp.advancedevolution.items.items.tools.weapons;

import de.philipp.advancedevolution.items.SwordItem;
import de.philipp.advancedevolution.items.SwordLevel;
import de.philipp.advancedevolution.items.ToolMaterial;
import de.philipp.advancedevolution.items.custombases.DraconicSwordItemBase;
import org.bukkit.inventory.ItemStack;

public class WyvernSwordItem extends DraconicSwordItemBase {

    @Override
    public ToolMaterial getToolMaterial() {
        return new ToolMaterial() {
            @Override
            public int attackDamage() {
                return 20;
            }
        };
    }

    @Override
    public String getDataName() {
        return "SwordWyvern";
    }

    @Override
    public ItemStack getBaseStack() {
        return new SwordItem("§5Wyvern Sword", getToolMaterial().attackDamage(), SwordLevel.DIAMOND).getItem();
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}
