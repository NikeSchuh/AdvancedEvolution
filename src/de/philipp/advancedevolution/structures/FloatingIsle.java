package de.philipp.advancedevolution.structures;

import de.philipp.advancedevolution.items.DraconicItem;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class FloatingIsle implements Structure {
    @Override
    public void generate(Location loc, Random random) {
        generateBlocks(loc, random);
        getStructuralData().spawnEntities(loc);
    }

    @Override
    public void populateChest(Inventory inventory, Random random) {
        for(int i = 0; i < inventory.getSize(); i++) {
            if(random.nextInt(100) < 30) {
                    switch (random.nextInt(5)) {
                        case 0:
                            ItemStack diamond = XMaterial.DIAMOND.parseItem();
                            diamond.setAmount(random.nextInt(3) + 1);
                            inventory.setItem(i, diamond);
                            break;
                        case 1:
                            ItemStack draconiumDust = DraconicItem.instantiateItem("DraconiumDust").getCurrentStack();
                            draconiumDust.setAmount(random.nextInt(6) + 1);
                            inventory.setItem(i, draconiumDust);
                            break;
                        case 2:
                            ItemStack pearl = XMaterial.ENDER_PEARL.parseItem();
                            pearl.setAmount(random.nextInt(1) + 1);
                            inventory.setItem(i, pearl);
                            break;
                        case 3:
                            inventory.setItem(i, ItemUtil.randomEnchantedBook(3));
                            break;
                        case 4:
                            if(Math.random() < 0.1) {
                                inventory.setItem(i, XMaterial.ENCHANTED_GOLDEN_APPLE.parseItem());
                            }
                            break;
                }
            }
        }
    }

    @Override
    public StructureFile getStructuralData() {
        return new StructureFile("FloatingIsle");
    }
}
