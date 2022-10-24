package de.philipp.advancedevolution.shield;

import de.philipp.advancedevolution.*;
import de.philipp.advancedevolution.animations.Delay;
import de.philipp.advancedevolution.entities.RedShot;
import de.philipp.advancedevolution.entities.particles.Hitbox;
import de.philipp.advancedevolution.events.customevents.ArmorEquipEvent;
import de.philipp.advancedevolution.items.*;
import de.philipp.advancedevolution.items.armormodifier.AutoFeedModifier;
import de.philipp.advancedevolution.items.armormodifier.FlightEnabledModifier;
import de.philipp.advancedevolution.items.armormodifier.FlightSpeedModifier;
import de.philipp.advancedevolution.items.armormodifier.NightVisionModifier;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.lib.xseries.XSound;
import de.philipp.advancedevolution.menus.ArmorSettings;
import de.philipp.advancedevolution.util.BadEffects;
import de.philipp.advancedevolution.util.NumberConverter;
import de.philipp.advancedevolution.util.item.ArmorType;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Shield implements Listener {

    public static final double ENERGY_COST_PER_SHIELD = 1000;
    private final ArmorHolder armorHolder;
    private DraconicArmorType armorType;
    private final Plugin plugin;
    private Player player;
    private final UUID id;
    private boolean disabled = false;
    private boolean performanceSave = false;
    private boolean flight = true;
    private double shieldCurrent = 0;
    private double shieldCapacity = 0;
    private double entropyLevel = 1.0;
    private float shieldRegeneration = 0;
    private double damageMultiplier = 1.0;
    private int hitDelay = 0;
    private int entropyReg = 0;

    private long jumps;

    private final BukkitTask shieldUpdater;
    private final BukkitTask modifierUpdater;

    private final List<IShieldHook> hooks = new ArrayList<>();

    public Shield(Plugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.armorHolder = new ArmorHolder(this);
        this.id = player.getUniqueId();
        this.jumps = player.getStatistic(Statistic.JUMP);

        this.shieldUpdater = createShieldUpdater();
        this.modifierUpdater = createModifierUpdater();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        checkCompleteArmor();
        if(armorHolder.hasChestPlate()) {
            checkFlightEnabled(armorHolder.getChestPlate());
        }
        checkFlight();
    }

    public ArmorHolder getArmorHolder() {
        return armorHolder;
    }

    public DraconicArmorType getArmorType() {
        return armorType;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public boolean isPerformanceSave() {
        return performanceSave;
    }

    public boolean isFlight() {
        return flight;
    }

    public double getShieldCurrent() {
        return shieldCurrent;
    }

    public double getShieldCapacity() {
        return shieldCapacity;
    }

    public double getEntropyLevel() {
        return entropyLevel;
    }

    public float getShieldRegeneration() {
        return shieldRegeneration;
    }

    public double getDamageMultiplier() {
        return damageMultiplier;
    }

    public int getHitDelay() {
        return hitDelay;
    }

    public int getEntropyReg() {
        return entropyReg;
    }

    public List<IShieldHook> getHooks() {
        return hooks;
    }

    public BukkitTask getShieldUpdater() {
        return shieldUpdater;
    }
    public boolean registerHook(IShieldHook hook) { return hooks.add(hook); }
    public void regenerateShield(double value) {
        if(shieldCurrent == shieldCapacity) return;
        double energyCost = (value * ENERGY_COST_PER_SHIELD) * entropyLevel;
        double shieldValue = value * entropyLevel;
        if (shieldValue <= 0) return;
        if(energyCost > getEnergyCurrent()) {
            if(getEnergyCurrent() == 0) return;
            double loweredShieldValue = shieldValue * (getEnergyCurrent() / energyCost);
            removeEnergy(getEnergyCurrent());
            shieldCurrent += loweredShieldValue;
        } else {
            if (shieldValue + shieldCurrent <= shieldCapacity) {
                if (removeEnergy(energyCost)) {
                    shieldCurrent += shieldValue;
                }
                return;
            } else if (shieldValue + shieldCurrent > shieldCapacity) {
                if (removeEnergy(energyCost)) {
                    shieldCurrent = shieldCapacity;
                }
                return;
            }
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
            for(DraconicArmorItem armorPiece : armorHolder.getEquipedArmor()) {
                if(armorPiece.hasEnergy(value)) {
                    armorPiece.subtractEnergy(value);
                    return true;
                } else {
                    armorPiece.setEnergyStored(0);
                    value -= armorPiece.getEnergyStored();
                }
            }
            return true;
        }
    }

    public void addEnergy(double value) {
        double amount = value;
        for(DraconicArmorItem armorItem : armorHolder.getEquipedArmor()) {
            if(armorItem.hasSpaceFor(value)) {
                armorItem.storeEnergy(value);
                break;
            } else {
                amount -= armorItem.getSpaceLeft();
                armorItem.storeEnergy(armorItem.getSpaceLeft());
                continue;
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

    private BukkitTask createModifierUpdater() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                updateModifiers();
            }
        }.runTaskTimer(plugin, 0, 100);
    }


    public void onUnload() {
        shieldUpdater.cancel();
        for(DraconicArmorItem item : armorHolder.getEquipedArmor()) {
            item.saveEnergy();
            item.updateItemStack();
        }
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
        for (double i = 0; i <= Math.PI; i += Math.PI / 7) {
            double radius = Math.sin(i);
            double y = Math.cos(i) + 1;
            for (double a = 0; a < Math.PI * 2; a+= Math.PI / 7) {
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
            if(armorItem.getType() == ArmorType.CHESTPLATE) {
                if(armorItem.getDraconicArmorType().canFlight()) {
                    player.setFlySpeed(0.1f);
                }
            }
            checkFlight();
        }

    }

    public void setFlight(boolean flight) {
        this.flight = flight;
        if((player.getGameMode() == GameMode.CREATIVE) && !(player.getGameMode() == GameMode.SPECTATOR)) return;
        if(!player.getAllowFlight()) {
            player.setFlying(false);
        }
        if(flight) {
            player.setAllowFlight(true);
        } else {
            player.setFlySpeed(0.1f);
        }
    }

    public void checkFlight() {
        if(!flight) return;
        if(!armorHolder.hasChestPlate()) {
            if(!(player.getGameMode() == GameMode.CREATIVE) && !(player.getGameMode() == GameMode.SPECTATOR)) {
                player.setAllowFlight(false);
            }
        } else if(armorHolder.hasChestPlate()) {
            if(armorHolder.getChestPlate().getDraconicArmorType().canFlight()) {
                player.setAllowFlight(true);
            } else {
                if(!(player.getGameMode() == GameMode.CREATIVE) && !(player.getGameMode() == GameMode.SPECTATOR)) {
                    player.setAllowFlight(false);
                }
            }
        }
    }



    public void addStats(DraconicArmorItem armorItem) {
        this.shieldCapacity += armorItem.getShieldCapacity();
        this.shieldRegeneration += armorItem.getShieldRegeneration();

        armorHolder.setArmor(armorItem.getType(), armorItem);
        this.armorType = checkDraconicArmorType();
        checkFlightEnabled(armorItem);
        checkFlight();
    }

    public void checkFlightEnabled(DraconicArmorItem armorItem) {
        if(armorHolder.hasModifier(FlightEnabledModifier.class)) {
            FlightEnabledModifier flightEnabledModifier = (FlightEnabledModifier) armorHolder.getModifier(FlightEnabledModifier.class);
            setFlight(flightEnabledModifier.getValue(armorItem.getCurrentStack()));
        }
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        if(event.getPlayer().getUniqueId().equals(player.getUniqueId())) {
            boolean isFlying = event.getPlayer().isFlying();
            new Delay(1) {
                @Override
                public void end() {
                    checkFlight();
                    if(event.getPlayer().getAllowFlight()) {
                        event.getPlayer().setFlying(isFlying);
                    }
                }
            };
        }
    }

    @EventHandler
    public void inventoryClose(InventoryCloseEvent event) {
        if(event.getPlayer().getUniqueId().equals(player.getUniqueId())) {
            if(armorHolder.hasChestPlate()) {
                if(DraconicArmorType.isTierHigher(armorHolder.getChestPlate().getDraconicArmorType(), DraconicArmorType.WYVERN)) {
                    checkFlightEnabled(new DraconicArmorItem(player.getInventory().getChestplate()));
                    player.setFlySpeed(((float) (((FlightSpeedModifier) armorHolder.getModifier(FlightSpeedModifier.class)).getValue(player.getInventory().getChestplate()) + 1) / 10));
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
                if(event.getClick() == ClickType.RIGHT) {
                    if(event.getSlotType() == InventoryType.SlotType.ARMOR) {
                    ItemStack clicked = event.getCurrentItem();
                    if(clicked != null) {
                        if (clicked.getType() != Material.AIR) {
                            if (DraconicItem.isDraconicItem(clicked)) {
                                DraconicItem draconicItem = new DraconicItem(clicked);
                                if (DraconicItem.isArmorItem(draconicItem.getDataName())) {
                                    event.setCancelled(true);
                                    DraconicArmorItem armorItem = new DraconicArmorItem(draconicItem.getCurrentStack());
                                    new ArmorSettings(plugin, p, armorItem).open();
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
        Random random = new Random();
        if(entityEvent.getEntity().getUniqueId() == player.getUniqueId()) {
            if(entityEvent.getDamager() instanceof Player) return;
            if (armorType == DraconicArmorType.GODLY) {
                for (Entity e : player.getNearbyEntities(20, 5, 20)) {
                    if (e instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) e;
                        if (!(e.getUniqueId().equals(player.getUniqueId()))) {
                            if (livingEntity instanceof Mob) {
                                Mob mob = (Mob) livingEntity;
                                if (mob.getTarget() != null) {
                                    if(mob.isCustomNameVisible()) continue;
                                    if (mob.getTarget().getUniqueId().equals(player.getUniqueId())) {
                                       // player.giveExp(new Random().nextInt(10));
                                       // mob.damage(1000, player);

                                       // mob.remove();
                                        new RedShot(AdvancedEvolution.getInstance(),player, mob.getLocation().add(random.nextInt(5 + 5) - 5, 5, random.nextInt(5 + 5) - 5), mob, new Vector(Math.random() * 5 + 2, Math.random() * 5 + 2, Math.random() * 5 + 2), new Hitbox(0.25, 0.25, 0.25));
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
    public boolean hasSpaceFor(double amount) {
        return amount + getEnergyCurrent() <= getEnergyCapacity();
    }
    public double getSpaceLeft() {
        return getEnergyCapacity() - getEnergyCurrent();
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
                    if(event.getDamage() > 1000000000) {
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
                if(event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                    hitDelay = 8;
                }
                if(shieldCurrent >= event.getDamage()) {
                    shieldCurrent -= event.getDamage();
                    removeEntropy(event.getFinalDamage() / shieldCapacity);
                    event.setCancelled(true);
                    playDamageEffect();
                    playDamageSound();
                    return;
                } else if(shieldCurrent <= event.getDamage()) {
                    if(armorType.isRevivable()) {
                        event.setDamage(event.getDamage() - shieldCurrent);
                        if(event.getFinalDamage() >= player.getHealth()) {
                            if(removeEnergy(10000000)) {
                                removeEntropy(event.getFinalDamage()/ shieldCapacity);
                                player.playSound(player.getLocation(), XSound.BLOCK_ANVIL_PLACE.parseSound(), 100f, 1f);
                                player.sendMessage("§4Warning! Received critical damage!");
                                playDeathDamageEffect();
                                player.setHealth(1);
                                event.setDamage(0.001);
                                shieldCurrent = 0;
                            } else {
                                if(shieldCurrent > 0) {
                                    event.setDamage(event.getDamage() - shieldCurrent);
                                    removeEntropy(event.getFinalDamage() / shieldCapacity);
                                    playDamageEffect();
                                    playDamageSound();
                                    shieldCurrent = 0;
                                }
                            }
                        } else {
                            shieldCurrent = 0;
                        }
                    } else {
                        if(shieldCurrent > 0) {
                            event.setDamage(event.getDamage() - shieldCurrent);
                            removeEntropy(event.getFinalDamage() / shieldCapacity);
                            playDamageEffect();
                            playDamageSound();
                            shieldCurrent = 0;
                        }
                    }
                }
            }
        }

    }

    public IArmorModifier getModifier(Class<? extends IArmorModifier> modifierClass) {
        return armorHolder.getModifier(modifierClass);
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



    public void updateModifiers() {
        if(armorHolder.hasHelmet()) {
            if (hasEnergy(500)) {
                if (DraconicArmorType.isTierHigher(armorHolder.getHelmet().getDraconicArmorType(), DraconicArmorType.WYVERN)) {
                    if(player.getLocation().getBlock().getLightLevel() <= 6) {
                        if(removeEnergy(1)) {
                            if (((NightVisionModifier) armorHolder.getModifier(NightVisionModifier.class)).getValue(armorHolder.getHelmet().getCurrentStack())) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 350, 0));
                            }
                        }
                    }
                    if (((AutoFeedModifier) armorHolder.getModifier(AutoFeedModifier.class)).getValue(armorHolder.getHelmet().getCurrentStack())) {
                            if (player.getFoodLevel() < 20) {
                                    for (ItemStack itemStack : player.getInventory().getContents()) {
                                        if (itemStack == null) continue;
                                        if (itemStack.getType().isEdible()) {
                                            XMaterial xMaterial = XMaterial.matchXMaterial(itemStack);
                                            player.getWorld().playSound(player.getLocation(), XSound.ENTITY_GENERIC_EAT.parseSound(), 100f, 1f);
                                            player.setFoodLevel(player.getFoodLevel() + (3 * armorType.getUpgradeLevel()));
                                            player.getWorld().spawnParticle(Particle.ITEM_CRACK, player.getEyeLocation().add(player.getLocation().getDirection().multiply(0.25)).add(0, -0.2, 0), 10, 0, 0, 0, 0.1, xMaterial.parseItem());
                                            itemStack.setAmount(itemStack.getAmount() - 1);
                                            new Delay(4) {
                                                @Override
                                                public void end() {
                                                    player.getWorld().playSound(player.getLocation(), XSound.ENTITY_GENERIC_EAT.parseSound(), 100f, 1f);
                                                    player.getWorld().spawnParticle(Particle.ITEM_CRACK, player.getEyeLocation().add(player.getLocation().getDirection().multiply(0.25)).add(0, -0.2, 0), 10, 0, 0, 0, 0.1, xMaterial.parseItem());
                                                    new Delay(4) {
                                                        @Override
                                                        public void end() {
                                                            player.getWorld().playSound(player.getLocation(), XSound.ENTITY_PLAYER_BURP.parseSound(), 100f, 1f);
                                                            player.getWorld().spawnParticle(Particle.ITEM_CRACK, player.getEyeLocation().add(player.getLocation().getDirection().multiply(0.25)).add(0, -0.2, 0), 15, 0, 0, 0, 0.1, xMaterial.parseItem());
                                                            removeEnergy(500);
                                                        }
                                                    };
                                                }
                                            };
                                            break;
                                        }
                            }
                        }
                    }
                }
            }
        }

        if(armorHolder.hasChestPlate()) {
            if (DraconicArmorType.isTierHigher(armorHolder.getChestPlate().getDraconicArmorType(), DraconicArmorType.WYVERN)) {
                if (player.getActivePotionEffects().size() > 0) {
                    for (PotionEffect potionEffect : getPlayer().getActivePotionEffects()) {
                        if (new BadEffects().isBadEffect(potionEffect.getType())) {
                            player.removePotionEffect(potionEffect.getType());
                        }
                    }
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
                if(shieldCurrent == shieldCapacity) {
                    addEntropy(0.0005);
                }
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
       // if(player.getStatistic(Statistic.JUMP) > jumps) {
        //    ((CraftPlayer) player).getHandle().setSprinting(false);
        //    player.setVelocity(player.getVelocity().setY(2));
         //   jumps = player.getStatistic(Statistic.JUMP);
       // }
        if(hitDelay > 0) {
            hitDelay--;
        }
        if(entropyReg > 0) {
            entropyReg--;
        }
        for(DraconicArmorItem armorItem : armorHolder.getEquipedArmor()) {
            armorItem.saveEnergy();
            if(!performanceSave) {
                armorItem.updateItemStack();
            }
        }

        // Hooks (API)
        for(IShieldHook hook : new ArrayList<>(hooks)) {
            try{
                hook.run(this);
            }catch (Exception e) {
                AdvancedEvolution.send("Hook failed from " + hook.getHandle().getName());
                e.printStackTrace();
                hooks.remove(hook);
            }
        }

        // Information
        if(!performanceSave) {
            if (shieldCapacity > 0) {
                if (!player.isSneaking()) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                TextComponent.fromLegacyText("§c" + Math.round(shieldCurrent) + " §7/§c " + Math.round(shieldCapacity)
                                        + "  §c" + Math.round(entropyLevel * 100) + "% §c" + NumberConverter.format(Math.round(getEnergyCurrent())) + " §7/§c " + NumberConverter.format(Math.round(getEnergyCapacity()))));
                } else {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText("§7Shield §c" + Math.round(shieldCurrent) + "§7/§c" + Math.round(shieldCapacity)
                                    + " §7Reg §c" + NumberConverter.format(entropyLevel * 100) + "% §7Energy §c" + NumberConverter.format(Math.round(getEnergyCurrent())) + " §7/§c " + NumberConverter.format(Math.round(getEnergyCapacity())) + " §7Shield p/s §c" + Math.round(((shieldRegeneration) * entropyLevel) * 20)));

                }
            }
        }

    }


}
