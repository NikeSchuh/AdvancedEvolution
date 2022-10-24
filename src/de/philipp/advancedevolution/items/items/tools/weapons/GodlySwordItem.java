package de.philipp.advancedevolution.items.items.tools.weapons;

import de.philipp.advancedevolution.items.SwordItem;
import de.philipp.advancedevolution.items.SwordLevel;
import de.philipp.advancedevolution.items.ToolMaterial;
import de.philipp.advancedevolution.items.custombases.DraconicSwordItemBase;
import org.bukkit.inventory.ItemStack;

public class GodlySwordItem extends DraconicSwordItemBase {

    @Override
    public ToolMaterial getToolMaterial() {
        return new ToolMaterial() {
            @Override
            public int attackDamage() {
                return 9999;
            }
        };
    }

    @Override
    public String getDataName() {
        return "SwordGodly";
    }

    @Override
    public ItemStack getBaseStack() {
        return new SwordItem("§cGodly Sword", getToolMaterial().attackDamage(), SwordLevel.DIAMOND).getItem();
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}
