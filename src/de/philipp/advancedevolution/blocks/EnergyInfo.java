package de.philipp.advancedevolution.blocks;

import de.philipp.advancedevolution.entities.DraconicEntity;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.NumberConverter;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class EnergyInfo {

    private ArmorStand stand;

    public EnergyInfo(DraconicMachine draconicMachine) {
        Location location = draconicMachine.getBlock().getLocation().add(0.5, 0.75, 0.5);
        for(Entity entity : location.getWorld().getNearbyEntities(location, 0.5, 0.5 ,0.5)) {
            if(entity.getType() == EntityType.ARMOR_STAND) {
                if(stand != null && DraconicEntity.isDraconicEntity(entity)) {
                    entity.remove();
                }
                if (DraconicEntity.isDraconicEntity(entity)) {
                    stand = (ArmorStand) entity;
                    stand.setCustomName("§c" + NumberConverter.formatE((long) draconicMachine.getEnergyStored()) + "§7/§c" + NumberConverter.formatE((long) draconicMachine.getEnergyCapacity()));
                }
            }
        }
        if(stand == null) {
            stand = (ArmorStand) new DraconicEntity(location, EntityType.ARMOR_STAND).getBukkitEntity();
            stand.setAI(false);
            stand.setSmall(true);
            stand.setInvulnerable(true);
            stand.setGravity(false);
            stand.setMarker(true);
            stand.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING_OR_CHANGING);
            stand.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.ADDING_OR_CHANGING);
            stand.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.ADDING_OR_CHANGING);
            stand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.ADDING_OR_CHANGING);
            stand.setInvisible(true);
            stand.setCustomNameVisible(true);
            stand.setCustomName("§c" + NumberConverter.formatE((long) draconicMachine.getEnergyStored()) + "§7/§c" + NumberConverter.formatE((long) draconicMachine.getEnergyCapacity()));

        }
    }

    public void remove() {
        for(ItemStack s : stand.getEquipment().getArmorContents()) {
            if(s != null) {
                if(s.getType() != XMaterial.AIR.parseMaterial()) {
                   stand.getWorld().dropItemNaturally(stand.getLocation(), s);
              }
            }
        }
        stand.remove();
    }

    public ArmorStand getStand() {
        return stand;
    }
}
