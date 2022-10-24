package de.philipp.advancedevolution.items.items.tools.weapons;

import de.philipp.advancedevolution.items.SwordItem;
import de.philipp.advancedevolution.items.SwordLevel;
import de.philipp.advancedevolution.items.ToolMaterial;
import de.philipp.advancedevolution.items.custombases.DraconicSwordItemBase;
import org.bukkit.inventory.ItemStack;

public class DraconicSwordItem extends DraconicSwordItemBase {

    @Override
    public ToolMaterial getToolMaterial() {
        return new ToolMaterial() {
            @Override
            public int attackDamage() {
                return 40;
            }
        };
    }

    @Override
    public String getDataName() {
        return "SwordDraconic";
    }

    @Override
    public ItemStack getBaseStack() {
        return new SwordItem("§6Draconic Sword", getToolMaterial().attackDamage(), SwordLevel.DIAMOND).getItem();
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}