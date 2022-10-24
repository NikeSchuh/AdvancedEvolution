package de.philipp.advancedevolution.entities;

import de.philipp.advancedevolution.animations.Delay;
import de.philipp.advancedevolution.entities.bosses.blazeinferno.BlazeInferno;
import de.philipp.advancedevolution.items.DraconicItem;
import de.philipp.advancedevolution.lib.xseries.XSound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class MobManager implements Listener {

    public MobManager(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void onDisable() {

    }

    @EventHandler
    public void onDrop(ItemSpawnEvent event) {
        ItemStack stack = event.getEntity().getItemStack();
        if(DraconicItem.isDraconicItem(stack)) {
            DraconicItem draconicItem = new DraconicItem(stack);
            ArmorStand stand = (ArmorStand) event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation().add(0, 20, 0), EntityType.ARMOR_STAND);
            stand.setMarker(true);
            stand.setVisible(false);
            stand.setCustomName(draconicItem.getDefaultStackStack().getItemMeta().getDisplayName());
            stand.setCustomNameVisible(true);
            stand.setGravity(false);
            stand.setSmall(true);
            event.getEntity().addPassenger(stand);
        }
    }

    @EventHandler
    public void onMerge(ItemMergeEvent event) {
        ItemStack stack = event.getEntity().getItemStack();
        if(DraconicItem.isDraconicItem(stack)) {
            if(event.getEntity().getPassengers().size() > 0) {
                event.getEntity().getPassengers().get(0).remove();
            }
        }
    }

    @EventHandler
    public void onDrop(EntityPickupItemEvent event) {
        ItemStack stack = event.getItem().getItemStack();
        if(DraconicItem.isDraconicItem(stack)) {
            if(event.getItem().getPassengers().size() > 0) {
                event.getItem().getPassengers().get(0).remove();
            }
        }
    }

    @EventHandler
    public void onDespawn(ItemDespawnEvent event) {
        ItemStack stack = event.getEntity().getItemStack();
        if(DraconicItem.isDraconicItem(stack)) {
            if(event.getEntity().getPassengers().size() > 0) {
                event.getEntity().getPassengers().get(0).remove();
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        Entity e = event.getEntity();
        if (e instanceof Item) {
            Item item = (Item) e;
            ItemStack stack = item.getItemStack();
            if(DraconicItem.isDraconicItem(stack)) {
                DraconicItem draconicItem = new DraconicItem(stack);
                if(event.getEntity().getPassengers().size() > 0) {
                    event.getEntity().getPassengers().get(0).remove();
                }
                if(!draconicItem.getItemBase().isBurnable()) {
                    if (event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.LAVA ||
                            event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
                        item.remove();
                        Item item2 = item.getLocation().getWorld().dropItem(item.getLocation().add(0, 1, 0), stack);
                        item2.getWorld().playSound(e.getLocation(), XSound.ENTITY_ENDERMAN_TELEPORT.parseSound(), 10f, 2f);
                        item2.getWorld().spawnParticle(Particle.WARPED_SPORE, item2.getLocation(), 10);
                        item2.teleport(event.getEntity().getLocation().add(0, 0.01, 0));
                        item2.setVelocity(new Vector((Math.random() - Math.random()) * 0.3, 0.1, (Math.random() - Math.random()) * 0.3));
                        item2.setGravity(false);

                    }
                }

            }
        }
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        if(event.getEntity() instanceof Blaze) {
            if(event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER || event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL){
                if(Math.random() < 0.01) {
                    Location location = event.getLocation();
                    event.setCancelled(true);
                    new BlazeInferno(location);
                }
            }
        }
    }

}
