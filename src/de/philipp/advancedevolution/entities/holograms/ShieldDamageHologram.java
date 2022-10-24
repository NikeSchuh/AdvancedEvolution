package de.philipp.advancedevolution.entities.holograms;

import de.philipp.advancedevolution.animations.Delay;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ShieldDamageHologram {

    public static List<ShieldDamageHologram> holograms = new ArrayList<>();

    private ArmorStand holo;
    private Location spawnLocation;
    private Plugin plugin;
    private double shieldDamage;
    private double normalDamage;

    public ShieldDamageHologram(Plugin plugin, Location location, int shieldDamage, int normalDamage) {
        spawnLocation = location.clone().add(0, 100, 0);
        holo = (ArmorStand) location.getWorld().spawnEntity(spawnLocation, EntityType.ARMOR_STAND);
        this.shieldDamage = shieldDamage;
        this.normalDamage = normalDamage;

        holo.setSmall(true);
        holo.setInvulnerable(true);
        holo.setInvisible(true);
        for(EquipmentSlot slot : EquipmentSlot.values()) {
            holo.addEquipmentLock(slot, ArmorStand.LockType.ADDING_OR_CHANGING);
        }
        holo.setMarker(false);
        holo.teleport(location);
        if(shieldDamage > 0) {
            if(normalDamage == 0) {
                holo.setCustomName("▼ §3" + shieldDamage + "");
            } else {
                holo.setCustomName("▼ §3" + shieldDamage + "§f▲§c" + normalDamage);
            }
        }
        holo.setCustomNameVisible(true);
        holo.setGravity(false);
        holo.setVelocity(new Vector(0, 0.1, 0));
        new Delay(50) {
            @Override
            public void end() {
                holo.remove();
                holograms.remove(this);
            }
        };
    }

}
