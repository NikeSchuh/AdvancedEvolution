package de.philipp.advancedevolution.items.custombases;

import de.philipp.advancedevolution.items.DraconicItem;
import de.philipp.advancedevolution.items.DraconicItemBase;
import de.philipp.advancedevolution.items.ToolMaterial;
import de.philipp.advancedevolution.items.ToolType;
import org.bukkit.inventory.ItemStack;

public abstract class DraconicSwordItemBase extends DraconicItemBase {

    public ToolType getToolType() {
        return ToolType.SWORD;
    }
    public abstract ToolMaterial getToolMaterial();
    public boolean isBurnable() {return false; }

}
