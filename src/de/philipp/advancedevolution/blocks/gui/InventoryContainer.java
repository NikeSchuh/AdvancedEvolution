package de.philipp.advancedevolution.blocks.gui;

import de.philipp.advancedevolution.blocks.DraconicBlock;
import de.tr7zw.nbtapi.NBTCompound;
import org.bukkit.inventory.ItemStack;

public class InventoryContainer {

   private NBTCompound itemRoot;
   public InventoryContainer(NBTCompound compound) {
      this.itemRoot = compound;
   }

   public int getRows() {
      return itemRoot.getInteger("inventorySize");
   }

}
