package de.philipp.advancedevolution.items.items.tools.weapons;

import de.philipp.advancedevolution.items.SwordItem;
import de.philipp.advancedevolution.items.SwordLevel;
import de.philipp.advancedevolution.items.ToolMaterial;
import de.philipp.advancedevolution.items.custombases.DraconicSwordItemBase;
import org.bukkit.inventory.ItemStack;

public class ChaoticSwordItem extends DraconicSwordItemBase {

    @Override
    public ToolMaterial getToolMaterial() {
        return new ToolMaterial() {
            @Override
            public int attackDamage() {
                return 160;
            }
        };
    }

    @Override
    public String getDataName() {
        return "SwordChaotic";
    }

    @Override
    public ItemStack getBaseStack() {
        return new SwordItem("§0Chaotic Sword", getToolMaterial().attackDamage(), SwordLevel.DIAMOND).getItem();
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}
