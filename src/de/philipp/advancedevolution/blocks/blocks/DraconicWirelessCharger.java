package de.philipp.advancedevolution.blocks.blocks;

import de.philipp.advancedevolution.blocks.DraconicBlock;
import de.philipp.advancedevolution.blocks.DraconicMachine;
import de.philipp.advancedevolution.blocks.EnergyInfo;
import de.philipp.advancedevolution.blocks.custombases.DraconicMachineBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.lib.xseries.XSound;
import de.philipp.advancedevolution.recipes.DraconicIngredient;
import de.philipp.advancedevolution.recipes.EnhancedRecipe;
import de.philipp.advancedevolution.recipes.VanillaIngredient;
import de.philipp.advancedevolution.shield.Shield;
import de.philipp.advancedevolution.shield.ShieldManager;
import de.philipp.advancedevolution.util.item.ItemUtil;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DraconicWirelessCharger extends DraconicMachineBase {

    public static final int chargeRange = 64;
    public static final double transfer = 5024;
    private static List<UUID> receivers = new ArrayList<>();

    @Override
    public void onUnload(DraconicBlock block) {

    }

    @Override
    public void onPlace(DraconicBlock block, Cancellable cancellable, Player player) {
        EnergyInfo energyInfo = new EnergyInfo(new DraconicMachine(block.getBlock()));
    }

    @Override
    public void onBreak(DraconicBlock block, Cancellable cancellable, Player player) {
        EnergyInfo energyInfo = new EnergyInfo(new DraconicMachine(block.getBlock()));
        energyInfo.remove();
    }

    @Override
    public void onBlockInteract(Player player, Cancellable cancellable, DraconicBlock block) {
        cancellable.setCancelled(true);
    }

    @Override
    public EnhancedRecipe getCraftingRecipe() {
        EnhancedRecipe recipe = new EnhancedRecipe();
        recipe.addIngredient(new VanillaIngredient(XMaterial.ENDER_PEARL.parseMaterial(), 8));
        recipe.addIngredient(new DraconicIngredient("DraconiumDust", 4));
        recipe.addIngredient(new DraconicIngredient("DraconicCore", 4));
        recipe.addIngredient(new DraconicIngredient("WirelessTransmitter", 2));
        recipe.addIngredient(new DraconicIngredient("DraconicCharger", 2));
        return recipe;
    }

    @Override
    public double getEnergyCapacity() {
        return 1000000;
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
        return false;
    }

    @Override
    public long getSyncInterval() {
        return 5;
    }

    @Override
    public void tickSync(Block block, DraconicMachine draconicMachine) {
        EnergyInfo energyInfo = new EnergyInfo(draconicMachine);
        if(draconicMachine.getEnergyStored() == 0) return;
        block.getWorld().spawnParticle(Particle.DRAGON_BREATH, block.getLocation().add(0.5, 1, 0.5), 3, 0, 0, 0, 0.1);
        for(Player player : block.getWorld().getPlayers()) {
            if(player.getLocation().distance(block.getLocation()) <= chargeRange) {
                Shield shield = ShieldManager.playerShieldMap.get(player.getUniqueId());
                if(shield.getSpaceLeft() == 0) continue;
                player.playSound(player.getLocation(), XSound.BLOCK_BEACON_POWER_SELECT.parseSound(), 0.01f, 1f);
                if(draconicMachine.hasEnergy(transfer)) {
                    if (shield.hasSpaceFor(transfer)) {
                        shield.addEnergy(transfer);
                        draconicMachine.removeEnergyStored(transfer);
                        continue;
                    } else {
                        draconicMachine.removeEnergyStored(shield.getSpaceLeft());
                        shield.addEnergy(shield.getSpaceLeft());
                        continue;
                    }
                } else {
                    if (shield.hasSpaceFor(draconicMachine.getEnergyStored())) {
                        shield.addEnergy(draconicMachine.getEnergyStored());
                        draconicMachine.setEnergyStored(0);
                        continue;
                    } else {
                        double remove = shield.getSpaceLeft();
                        shield.addEnergy(remove);
                        draconicMachine.removeEnergyStored(remove);
                        continue;
                    }
                }
            }
        }
    }

    @Override
    public void tickAsync(Block block, DraconicMachine draconicMachine) {

    }

    @Override
    public String getDataName() {
        return "WirelessCharger";
    }

    @Override
    public ItemStack getBaseStack() {
        return ItemUtil.createSimpleStack("§6Wireless Charger", XMaterial.BREWING_STAND, "");
    }

    @Override
    public boolean isStackable() {
        return true;
    }
}
