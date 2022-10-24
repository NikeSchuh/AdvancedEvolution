package de.philipp.advancedevolution.blocks.blocks;

import de.philipp.advancedevolution.blocks.DraconicBlock;
import de.philipp.advancedevolution.blocks.DraconicMachine;
import de.philipp.advancedevolution.blocks.EnergyInfo;
import de.philipp.advancedevolution.blocks.InventoryContainer;
import de.philipp.advancedevolution.blocks.custombases.DraconicMachineBase;
import de.philipp.advancedevolution.items.DraconicItem;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class DraconicHopper extends DraconicMachineBase {

    private int range = 5;
    private HashMap<Item, Integer> itemMap = new HashMap<>();

    @Override
    public void onPlace(DraconicBlock block, Cancellable cancellable, Player player) {
        InventoryContainer container = new InventoryContainer(block);
        container.createInventory("Vacuum Hopper", 6 * 9);
    }

    @Override
    public void onBreak(DraconicBlock block, Cancellable cancellable, Player player) {
        EnergyInfo energyInfo = new EnergyInfo(new DraconicMachine(block.getBlock()));
        energyInfo.remove();
        InventoryContainer container = new InventoryContainer(block);
        container.syncInventoryWithContainer();
    }

    @Override
    public void onBlockInteract(Player player, Cancellable cancellable, DraconicBlock block) {
        if(player.isSneaking()) return;
        cancellable.setCancelled(true);
        InventoryContainer container = new InventoryContainer(block);
        container.syncInventoryWithContainer();
        player.openInventory(container.getInventory());
    }

    @Override
    public double getEnergyCapacity() {
        return 100000;
    }

    @Override
    public boolean isEnergyPullAble() {
        return false;
    }

    @Override
    public boolean isEnergyAcceptable() {
        return true;
    }

    @Override
    public void onEnergyReceive(DraconicMachine sender, DraconicMachine receiver, double amount, BlockFace senderDirection) {
        receiver.addEnergyStored(amount);
        EnergyInfo energyInfo = new EnergyInfo(receiver);
    }

    @Override
    public boolean isRestricted() {
        return true;
    }

    @Override
    public void tickSync(Block block, DraconicMachine draconicMachine) {
        // Pickup Loop
        InventoryContainer container = new InventoryContainer(draconicMachine);
        if(container.getInventory().firstEmpty() != -1) {
            for (Entity entity : block.getWorld().getNearbyEntities(block.getLocation().clone().add(0.5D, 0.5D, 0.5D), 0.5, 0.5, 0.5)) {
                if (!(entity instanceof Item)) continue;
                Item item = (Item) entity;
                // Bukkit.getConsoleSender().sendMessage("Empty " + container.getInventory().firstEmpty() + "  Inv Size:" + container.getInventory().getSize());
                if (container.getInventory().firstEmpty() != -1) {
                    container.getInventory().addItem(item.getItemStack());
                    if(DraconicItem.isDraconicItem(item.getItemStack())) {
                        item.getPassengers().get(0).remove();
                    }
                    item.remove();
                }
            }

            // Attraction Loop
            for (Entity entity : block.getWorld().getNearbyEntities(block.getLocation(), range, range, range)) {
                if (!(entity instanceof Item)) continue;
                Item item = (Item) entity;
                /*/if(itemMap.getOrDefault(item, 0) == 100) continue;
                itemMap.put(item, itemMap.getOrDefault(item, 0) + 1);
                int rangeSqr = range * range;
                double x = (block.getX() + 0.5D - entity.getLocation().getX());
                double y = (block.getY() + 0.5D - entity.getLocation().getY());
                double z = (block.getZ() + 0.5D - entity.getLocation().getZ());

                double distance = Math.sqrt(x * x + y * y + z * z);
                if (distance < 0.75 || range == 0) {

                } else {
                    double speed = 0.06;
                    double distScale = 1.0 - Math.min(0.9, (distance - 1) / rangeSqr);
                    distScale *= distScale;
                    item.setVelocity(new Vector(entity.getVelocity().getX() + (x / distance * distScale * speed), entity.getVelocity().getY() + (y / distance * distScale * 0.4), entity.getVelocity().getZ() + ( z / distance * distScale * speed)));
                }/*/
                item.setVelocity(item.getVelocity().add(block.getLocation().add(0.5, 0.5, 0.5).subtract(item.getLocation()).toVector().normalize().multiply(0.1f)));
            }
        }
        EnergyInfo energyInfo = new EnergyInfo(draconicMachine);
    }

    @Override
    public void tickAsync(Block block, DraconicMachine draconicMachine) {

    }

    @Override
    public void onUnload(DraconicBlock block) {
        InventoryContainer container = new InventoryContainer(new DraconicMachine(block.getBlock()));
        container.syncInventoryWithContainer();
    }

    @Override
    public String getDataName() {
        return "DraconicHopper";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§6Vacuum Hopper", XMaterial.CONDUIT);
    }


    @Override
    public boolean isStackable() {
        return false;
    }
}
