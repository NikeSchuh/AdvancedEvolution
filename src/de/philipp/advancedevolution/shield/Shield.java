package de.philipp.advancedevolution.shield;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.animations.animations.GodExecuteAnimation;
import de.philipp.advancedevolution.events.customevents.ArmorEquipEvent;
import de.philipp.advancedevolution.items.ArmorHolder;
import de.philipp.advancedevolution.items.DraconicArmorItem;
import de.philipp.advancedevolution.items.DraconicArmorType;
import de.philipp.advancedevolution.items.DraconicItem;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.lib.xseries.XSound;
import de.philipp.advancedevolution.menus.ArmorSettings;
import de.philipp.advancedevolution.util.ArmorEffect;
import de.philipp.advancedevolution.util.NumberConverter;
import de.philipp.advancedevolution.util.item.ArmorType;
import de.philipp.advancedevolution.util.item.EquipMethod;
import de.tr7zw.nbtapi.NBTEntity;
import de.tr7zw.nbtapi.NBTTileEntity;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.loot.LootContext;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class Shield implements Listener {

    public static final double ENERGY_COST_PER_SHIELD = 1000;
    private ArmorHolder armorHolder;
    private DraconicArmorType armorType;
    private Plugin plugin;
    private Player player;
    private UUID id;
    private boolean disabled = false;
    private boolean performanceSave = false;
    private boolean flight = false;
    private double shieldCurrent = 0;
    private double shieldCapacity = 0;
    private double entropyLevel = 1.0;
    private float shieldRegeneration = 0;
    private double damageMultiplier = 1.0;
    private int hitDelay = 0;
    private int entropyReg = 0;
    private BukkitTask shieldUpdater;

    public Shield(Plugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.armorHolder = new ArmorHolder(this);
        this.id = player.getUniqueId();

        this.shieldUpdater = createShieldUpdater();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        checkCompleteArmor();
    }

    public BukkitTask getShieldUpdater() {
        return shieldUpdater;
    }

    public void regenerateShield(double value) {
        if(shieldCurrent == shieldCapacity) return;
        double energyCost = (value * ENERGY_COST_PER_SHIELD) * entropyLevel;
        double shieldValue = value * entropyLevel;
        if (shieldValue <= 0) return;
        if (shieldValue + shieldCurrent <= shieldCapacity) {
            if(removeEnergy(energyCost)) {
                shieldCurrent += shieldValue;
            }
            return;
        } else if(shieldValue + shieldCurrent > shieldCapacity) {
            if(removeEnergy(energyCost)) {
                shieldCurrent = shieldCapacity;
            }
            return;
        }
    }

    public void setDisabled(boolean b) {
        this.disabled = b;
        if(!b) {
            this.player = Bukkit.getPlayer(id);
        }
    }

    public boolean removeEnergy(double value) {
        if(value > getEnergyCurrent()) {
            return false;
        } else {
            double basicValue = value / armorHolder.getEquipedArmor().length;
            double multiplier = 1.0;
            for(DraconicArmorItem armorItem : armorHolder.getEquipedArmor()) {
                if(armorItem.getEnergyStored() > value) {
                    armorItem.subtractEnergy(value);
                    return true;
                }
                if(armorItem.getEnergyStored() >= basicValue * multiplier) {
                    armorItem.subtractEnergy(basicValue * multiplier);
                } else {
                    multiplier = armorItem.getEnergyStored() / basicValue + multiplier;
                    armorItem.setEnergyStored(0);
                }
            }
            return true;
        }
    }

    public void addEnergy(double value) {
        double basicValue = value / armorHolder.getEquipedArmor().length;
        double next = 1.0;
        for(DraconicArmorItem armorItem : armorHolder.getEquipedArmor()) {
            if((armorItem.getEnergyStored() + value) * next > armorItem.getEnergyCapacity()) {
                next = ((armorItem.getEnergyStored() + value) * next) / armorItem.getEnergyCapacity();
                armorItem.storeEnergy(armorItem.getEnergyCapacity());
            } else {
                armorItem.storeEnergy(basicValue * next);
                next = 1.0;
            }
        }
    }

    public double getEnergyCurrent() {
        double current = 0;
        for(DraconicArmorItem armorItem : armorHolder.getEquipedArmor()) {
            current += armorItem.getEnergyStored();
        }
        return current;
    }

    public double getEnergyCapacity() {
        double capacity = 0;
        for(DraconicArmorItem armorItem : armorHolder.getEquipedArmor()) {
            capacity += armorItem.getEnergyCapacity();
        }
        return capacity;
    }

    private BukkitTask createShieldUpdater() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                updateShield();
            }
        }.runTaskTimer(plugin, 0, 1);
    }


    public void onUnload() {
        
    }

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent equipEvent) {
        if(disabled) return;
        if(!equipEvent.getPlayer().getUniqueId().equals(player.getUniqueId())) return;
        if(equipEvent.getNewArmorPiece() == null) return;
        if(equipEvent.getNewArmorPiece().getType() == XMaterial.AIR.parseMaterial()) return;
            ItemStack armorPiece = equipEvent.getNewArmorPiece();
            if(DraconicItem.isDraconicItem(armorPiece)) {
                DraconicItem item = new DraconicItem(equipEvent.getNewArmorPiece());
                if(item.isArmorItem()) {
                    DraconicArmorItem armorItem = new DraconicArmorItem(equipEvent.getNewArmorPiece());
                    if(armorHolder.hasArmor(armorItem.getType())) {
                        removeStats(armorHolder.getArmor(armorItem.getType()).getType());
                    }
                    addStats(armorItem);
                }
            }
    }

    @EventHandler
    public void onArmorUnequipped(ArmorEquipEvent equipEvent) {
        if(disabled) return;
        if(!equipEvent.getPlayer().getUniqueId().equals(player.getUniqueId())) return;
        if(equipEvent.getOldArmorPiece() == null) return;
        if(equipEvent.getOldArmorPiece().getType() == XMaterial.AIR.parseMaterial()) return;
            ItemStack armorPiece = equipEvent.getOldArmorPiece();
            if(DraconicItem.isDraconicItem(armorPiece)) {
                DraconicItem item = new DraconicItem(equipEvent.getOldArmorPiece());
                if(item.isArmorItem()) {
                    DraconicArmorItem armorItem = new DraconicArmorItem(equipEvent.getOldArmorPiece());
                    removeStats(armorItem.getType());
                }
            }

    }

    public void removeEntropy(double value) {
        if(entropyLevel <= 0) return;
        if(entropyLevel - value <= 0) {
            entropyLevel = 0;
        } else {
            entropyLevel -= value;
        }
    }

    public void playDamageEffect() {
        Location loc = player.getLocation();
        for (double i = 0; i <= Math.PI; i += Math.PI / 10) {
            double radius = Math.sin(i);
            double y = Math.cos(i) + 1;
            for (double a = 0; a < Math.PI * 2; a+= Math.PI / 10) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                loc.add(x, y, z);
                loc.getWorld().spawnParticle(Particle.CRIT_MAGIC, loc, 1, 0, 0, 0, 0.3);
                loc.subtract(x, y, z);
            }
        }
    }

    public void playDeathDamageEffect() {
        Location loc = player.getLocation();
        for (double i = 0; i <= Math.PI; i += Math.PI / 10) {
            double radius = Math.sin(i);
            double y = Math.cos(i) + 1;
            for (double a = 0; a < Math.PI * 2; a+= Math.PI / 10) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                loc.add(x, y, z);
                loc.getWorld().spawnParticle(Particle.CRIMSON_SPORE, loc, 1, 0, 0, 0, 0.3);
                loc.subtract(x, y, z);
            }
        }
    }

    public void addEntropy(double value) {
        if(entropyLevel + value >= 1) {
            entropyLevel = 1;
        } else {
            entropyLevel += value;
        }
    }

    public Player getPlayer() {
        return player;
    }

    public DraconicArmorType checkDraconicArmorType(ArmorType except) {
        DraconicArmorType highestType = null;
        if(armorHolder.hasBoots()) {
            if(!(armorHolder.getBoots().getType() == except)) {
                if(DraconicArmorType.isTierHigher(armorHolder.getBoots().getDraconicArmorType(), highestType)) {
                    highestType = armorHolder.getBoots().getDraconicArmorType();
                }
            }
        }

        if(armorHolder.hasLeggings()) {
            if(!(armorHolder.getLeggings().getType() == except)) {
                if(DraconicArmorType.isTierHigher(armorHolder.getLeggings().getDraconicArmorType(), highestType)) {
                    highestType = armorHolder.getLeggings().getDraconicArmorType();
                }
            }
        }

        if(armorHolder.hasChestPlate()) {
            if(!(armorHolder.getChestPlate().getType() == except)) {
                if(DraconicArmorType.isTierHigher(armorHolder.getChestPlate().getDraconicArmorType(), highestType)) {
                    highestType = armorHolder.getChestPlate().getDraconicArmorType();
                }
            }
        }

        if(armorHolder.hasHelmet()) {
            if(!(armorHolder.getHelmet().getType() == except)) {
                if(DraconicArmorType.isTierHigher(armorHolder.getHelmet().getDraconicArmorType(), highestType)) {
                    highestType = armorHolder.getHelmet().getDraconicArmorType();
                }
            }
        }
        return highestType;
    }

    public DraconicArmorType checkDraconicArmorType() {
        DraconicArmorType highestType = null;
        if(armorHolder.hasHelmet()) {
            if(DraconicArmorType.isTierHigher(armorHolder.getHelmet().getDraconicArmorType(), highestType)) {
                highestType = armorHolder.getHelmet().getDraconicArmorType();
            }

        }
        if(armorHolder.hasLeggings()) {
                if(DraconicArmorType.isTierHigher(armorHolder.getLeggings().getDraconicArmorType(), highestType)) {
                    highestType = armorHolder.getLeggings().getDraconicArmorType();
                }
        }

        if(armorHolder.hasChestPlate()) {
                if(DraconicArmorType.isTierHigher(armorHolder.getChestPlate().getDraconicArmorType(), highestType)) {
                    highestType = armorHolder.getChestPlate().getDraconicArmorType();
                }

        }
        if(armorHolder.hasBoots()) {
            if(DraconicArmorType.isTierHigher(armorHolder.getBoots().getDraconicArmorType(), highestType)) {
                highestType = armorHolder.getBoots().getDraconicArmorType();
            }
        }



        return highestType;
    }



    public void removeStats(ArmorType type) {
        if(armorHolder.hasArmor(type)) {
            boolean draconicShield = false;
            DraconicArmorItem armorItem = armorHolder.getArmor(type);
            this.shieldCapacity -= armorItem.getShieldCapacity();
            this.shieldRegeneration -= armorItem.getShieldRegeneration();
            this.shieldCurrent -= shieldCurrent / armorHolder.getEquipedArmor().length;

            armorHolder.setArmor(armorItem.getType(), null);
            this.armorType = checkDraconicArmorType(armorItem.getType());

        }

            if(!armorHolder.hasChestPlate()) {
                if(!(player.getGameMode() == GameMode.CREATIVE) && !(player.getGameMode() == GameMode.SPECTATOR)) {
                    player.setAllowFlight(false);
                }
            } else if(armorHolder.hasChestPlate()) {
                if(armorHolder.getChestPlate().getDraconicArmorType().canFlight()) {
                    player.setAllowFlight(armorHolder.getChestPlate().getDraconicArmorType().canFlight());
                } else {
                    if(!(player.getGameMode() == GameMode.CREATIVE) && !(player.getGameMode() == GameMode.SPECTATOR)) {
                        player.setAllowFlight(false);
                    }
                }
            }


    }



    public void addStats(DraconicArmorItem armorItem) {
        boolean draconicShield = false;
        this.shieldCapacity += armorItem.getShieldCapacity();
        this.shieldRegeneration += armorItem.getShieldRegeneration();

        armorHolder.setArmor(armorItem.getType(), armorItem);
        this.armorType = checkDraconicArmorType();



        if(!armorHolder.hasChestPlate()) {
            if(!(player.getGameMode() == GameMode.CREATIVE) && !(player.getGameMode() == GameMode.SPECTATOR)) {
                player.setAllowFlight(false);
            }
        } else if(armorHolder.hasChestPlate()) {
            if(armorHolder.getChestPlate().getDraconicArmorType().canFlight()) {
                player.setAllowFlight(armorHolder.getChestPlate().getDraconicArmorType().canFlight());
            } else {
                if(!(player.getGameMode() == GameMode.CREATIVE) && !(player.getGameMode() == GameMode.SPECTATOR)) {
                    player.setAllowFlight(false);
                }
            }
        }
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        if(event.getPlayer().getUniqueId().equals(player.getUniqueId())) {
            if(!armorHolder.hasChestPlate()) {
                if(!(player.getGameMode() == GameMode.CREATIVE) && !(player.getGameMode() == GameMode.SPECTATOR)) {
                    player.setAllowFlight(false);
                }
            } else if(armorHolder.hasChestPlate()) {
                if(armorHolder.getChestPlate().getDraconicArmorType().canFlight()) {
                    player.setAllowFlight(armorHolder.getChestPlate().getDraconicArmorType().canFlight());
                } else {
                    if(!(player.getGameMode() == GameMode.CREATIVE) && !(player.getGameMode() == GameMode.SPECTATOR)) {
                        player.setAllowFlight(false);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(player.getKiller() != null) {
            Player killer = player.getKiller();
            Shield shield = ShieldManager.playerShieldMap.get(killer.getUniqueId());
            if(shield.armorType == DraconicArmorType.GODLY) {
                if(armorType != DraconicArmorType.GODLY) {
                    event.setDeathMessage(player.getDisplayName() + " was not worthy enough for the ascended " + killer.getDisplayName());
                }
            }
        }
        if(player.getUniqueId().equals(this.player.getUniqueId())) {

            entropyLevel = 1.0;
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(event.getWhoClicked() instanceof Player) {
            Player p = (Player) event.getWhoClicked();
            if(p.getUniqueId().equals(player.getUniqueId())) {
                if(event.getClick() == ClickType.MIDDLE) {
                    if(event.getSlotType() == InventoryType.SlotType.ARMOR) {
                    ItemStack clicked = event.getCurrentItem();
                    if(clicked != null) {
                        if (clicked.getType() != Material.AIR) {
                            if (DraconicItem.isDraconicItem(clicked)) {
                                DraconicItem draconicItem = new DraconicItem(clicked);
                                if (DraconicItem.isArmorItem(draconicItem.getDataName())) {
                                    DraconicArmorItem armorItem = new DraconicArmorItem(draconicItem.getCurrentStack());
                                    new ArmorSettings(AdvancedEvolution.getInstance(), p, armorItem).open();
                                }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void handleIncomingDamageByEntity(EntityDamageByEntityEvent entityEvent) {
        if(entityEvent.getEntity().getUniqueId() == player.getUniqueId()) {
            if(entityEvent.getDamager() instanceof Player) {
                    Player damaged = (Player) entityEvent.getDamager();
                    Shield shield = ShieldManager.playerShieldMap.get(damaged.getUniqueId());
                    if(shield.armorType == DraconicArmorType.GODLY) return;
                    player.getWorld().playSound(player.getLocation(), XSound.AMBIENT_WARPED_FOREST_MOOD.parseSound(), 100, 1f);
                    damaged.damage(1000, player);
                return;
            }
            if (armorType == DraconicArmorType.GODLY) {
                entityEvent.setCancelled(true);
                for (Entity e : player.getNearbyEntities(20, 5, 20)) {
                    if (e instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) e;
                        if (!(e.getUniqueId().equals(player.getUniqueId()))) {
                            if (livingEntity instanceof Mob) {
                                Mob mob = (Mob) livingEntity;
                                if (mob.getTarget() != null) {
                                    if (mob.getTarget().getUniqueId().equals(player.getUniqueId())) {
                                        player.giveExp(new Random().nextInt(10));
                                        mob.damage(1000, player);
                                        mob.getWorld().playSound(mob.getLocation(), XSound.AMBIENT_CRIMSON_FOREST_MOOD.parseSound(), 100f, 1f);
                                        mob.getWorld().spawnParticle(Particle.WHITE_ASH, mob.getEyeLocation(), 50, 0.3, 0.5, 0.3, 0.2);
                                        mob.remove();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean hasEnergy(double value) {
        return getEnergyCurrent() > value;
    }

    public void playDamageSound() {
        player.playSound(player.getLocation(), XSound.BLOCK_END_PORTAL_FRAME_FILL.parseSound(), 100f, (float) ((float) shieldCurrent / shieldCapacity) + 0.5f);
        player.getNearbyEntities(10, 10, 10).forEach(key -> {
            if (key instanceof Player) {
                Player entity = (Player) key;
                if (!entity.getUniqueId().equals(player.getUniqueId())) {
                    entity.playSound(player.getLocation(), XSound.BLOCK_END_PORTAL_FRAME_FILL.parseSound(), 100f, (float) ((float) shieldCurrent / shieldCapacity) + 0.5f);
                }
            }
        });
    }

    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void handleIncomingDamage(EntityDamageEvent event) {
        if(disabled) return;
        if(shieldCapacity <= 0) return;
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if(player.getUniqueId().equals(this.player.getUniqueId())) {
                if(event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                    if(event.getDamage() > 100000) {
                        return;
                    }
                }
                if(event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    if(DraconicArmorType.isTierHigher(armorType, DraconicArmorType.WYVERN)) {
                        event.setCancelled(true);
                        return;
                    }
                }
                entropyReg = 120;
                if(hitDelay > 0) {
                    event.setCancelled(true);
                    return;
                }
                hitDelay = 8;
                if(shieldCurrent >= event.getFinalDamage()) {
                    shieldCurrent -= event.getFinalDamage();
                    removeEntropy(event.getDamage() / shieldCapacity);
                    event.setCancelled(true);
                    playDamageEffect();
                    playDamageSound();
                    return;
                } else if(shieldCurrent <= event.getFinalDamage()) {
                    shieldCurrent = 0;
                    if(armorType.isRevivable()) {
                        if(event.getFinalDamage() - shieldCurrent >= player.getHealth()) {
                            if(removeEnergy(10000000)) {
                                removeEntropy(event.getDamage() / shieldCapacity);
                                player.playSound(player.getLocation(), XSound.BLOCK_ANVIL_PLACE.parseSound(), 100f, 1f);
                                player.sendMessage("§4Warning! Received critical damage!");
                                playDeathDamageEffect();
                                player.setHealth(1);
                                event.setDamage(0.000001);
                            } else {
                                event.setDamage(event.getFinalDamage() - shieldCurrent);
                                removeEntropy(event.getDamage() / shieldCapacity);
                            }
                        }
                    } else {
                        event.setDamage(event.getFinalDamage() - shieldCurrent);
                        removeEntropy(event.getDamage() / shieldCapacity);
                    }
                }
            }
        }

    }

    public void checkCompleteArmor() {
        PlayerInventory playerInventory = player.getInventory();
        if(playerInventory.getHelmet() != null) {
            ItemStack stack = playerInventory.getHelmet();
            if(DraconicItem.isDraconicItem(stack)) {
                DraconicItem draconicItem = new DraconicItem(stack);
                if(DraconicItem.isArmorItem(draconicItem.getDataName())) {
                    DraconicArmorItem armorItem = new DraconicArmorItem(stack);
                    if(armorHolder.hasArmor(armorItem.getType())) {
                        removeStats(armorHolder.getArmor(armorItem.getType()).getType());
                    }
                    addStats(armorItem);
                }
            }
        }

        if(playerInventory.getChestplate() != null) {
            ItemStack stack = playerInventory.getChestplate();
            if(DraconicItem.isDraconicItem(stack)) {
                DraconicItem draconicItem = new DraconicItem(stack);
                if(DraconicItem.isArmorItem(draconicItem.getDataName())) {
                    DraconicArmorItem armorItem = new DraconicArmorItem(stack);
                    if(armorHolder.hasArmor(armorItem.getType())) {
                        removeStats(armorHolder.getArmor(armorItem.getType()).getType());
                    }
                    addStats(armorItem);
                }
            }
        }

        if(playerInventory.getLeggings() != null) {
            ItemStack stack = playerInventory.getLeggings();
            if(DraconicItem.isDraconicItem(stack)) {
                DraconicItem draconicItem = new DraconicItem(stack);
                if(DraconicItem.isArmorItem(draconicItem.getDataName())) {
                    DraconicArmorItem armorItem = new DraconicArmorItem(stack);
                    if(armorHolder.hasArmor(armorItem.getType())) {
                        removeStats(armorHolder.getArmor(armorItem.getType()).getType());
                    }
                    addStats(armorItem);
                }
            }
        }

        if(playerInventory.getBoots() != null) {
            ItemStack stack = playerInventory.getBoots();
            if(DraconicItem.isDraconicItem(stack)) {
                DraconicItem draconicItem = new DraconicItem(stack);
                if(DraconicItem.isArmorItem(draconicItem.getDataName())) {
                    DraconicArmorItem armorItem = new DraconicArmorItem(stack);
                    if(armorHolder.hasArmor(armorItem.getType())) {
                        removeStats(armorHolder.getArmor(armorItem.getType()).getType());
                    }
                    addStats(armorItem);
                }
            }
        }
    }

    public void updateShield() {
        if(disabled) return;
        regenerateShield(shieldRegeneration);
        if(entropyReg == 0) {
            if(armorType == DraconicArmorType.BASIC) {
                addEntropy(0.00001);
            } else if(armorType == DraconicArmorType.WYVERN) {
                addEntropy(0.00005);
            } else if(armorType == DraconicArmorType.DRACONIC) {
                addEntropy(0.0001);
            } else if(armorType == DraconicArmorType.CHAOTIC) {
                addEntropy(0.0002);
            } else if(armorType == DraconicArmorType.GODLY) {
                addEntropy(0.002);
            }

        }


        if(hitDelay > 0) {
            hitDelay--;
        }
        if(entropyReg > 0) {
            entropyReg--;
        }
        if(!performanceSave) {
            if (shieldCapacity > 0) {
                if (!player.isSneaking()) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText("§c" + Math.round(shieldCurrent) + " §7/§c " + Math.round(shieldCapacity)
                                    + "  §c" + Math.round(entropyLevel * 100) + "% §c" + NumberConverter.format(Math.round(getEnergyCurrent())) + " §7/§c " + NumberConverter.format(Math.round(getEnergyCapacity()))));
                } else {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText("§7Shield §c" + Math.round(shieldCurrent) + "§7/§c" + Math.round(shieldCapacity)
                                    + " §7Reg §c" + Math.round(entropyLevel * 100) + "% §7Energy §c" + NumberConverter.format(Math.round(getEnergyCurrent())) + " §7/§c " + NumberConverter.format(Math.round(getEnergyCapacity())) + " §7Shield p/s §c" + Math.round(((shieldRegeneration) * entropyLevel) * 20)));
                }
            }
        }
        for(DraconicArmorItem armorItem : armorHolder.getEquipedArmor()) {
            if(!performanceSave) {
                armorItem.updateItemStack();
            }
            armorItem.saveEnergy();
        }

    }


}
