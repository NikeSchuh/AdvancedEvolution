package de.philipp.advancedevolution.enchantments;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.advancements.DraconicAdvancements;
import de.philipp.advancedevolution.entities.particles.ChasingParticleEntity;
import de.philipp.advancedevolution.entities.particles.Hitbox;
import de.philipp.advancedevolution.items.DraconicItem;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.lib.xseries.XSound;
import de.philipp.advancedevolution.util.NumberConverter;
import de.philipp.advancedevolution.util.item.ItemUtil;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTEntity;
import de.tr7zw.nbtapi.NBTFile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class DraconicEnchantments implements Listener {

    public static final Collection<String> draconicEnchantments = new ArrayList<>();

    public static final Enchantment BEHEADING = new BeheadingEnchantment("beheading", "Beheading", 7);
    public static final Enchantment SOUL_BOUND = new SoulboundEnchantment("soulbound", "Soulbound", 1);
    public static final Enchantment AUTO_SMELT = new AutoSmeltEnchantment("autosmelt", "Autosmelt", 1);
    public static final Enchantment LIFE_STEEL = new LifesteelEnchantment("lifesteal", "Lifesteal", 3);

    public static void registerAll() {
        boolean beheading = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(BEHEADING);
        boolean soulbound = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(SOUL_BOUND);
        boolean autosmelt = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(AUTO_SMELT);
        boolean lifesteel = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(LIFE_STEEL);
        draconicEnchantments.add("autosmelt");
        draconicEnchantments.add("soulbound");
        draconicEnchantments.add("beheading");
        draconicEnchantments.add("lifesteal");
        if(!beheading) {
            registerEnchantment(BEHEADING);
        }
        if(!soulbound) {
            registerEnchantment(SOUL_BOUND);
        }
        if(!autosmelt) {
            registerEnchantment(AUTO_SMELT);
        }
        if(!lifesteel) {
            registerEnchantment(LIFE_STEEL);
        }
    }

    public static void registerEnchantment(Enchantment enchantment) {
        boolean registered = true;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (Exception e) {
            registered = false;
            e.printStackTrace();
        }
    }

    public static boolean isDraconicEnchant(Enchantment enchantment) {
        return draconicEnchantments.contains(enchantment.getKey().getKey());
    }

    public static void refreshBookLore(ItemStack stack) {
        EnchantmentStorageMeta storageMeta = (EnchantmentStorageMeta) stack.getItemMeta();
        Map<Enchantment, Integer> enchants = storageMeta.getEnchants();
        List<String> lore = new ArrayList<>();
        Bukkit.getConsoleSender().sendMessage("Item : " + stack.getEnchantments() + " Storage: " + storageMeta.getEnchants());
        for(Enchantment enchantment : enchants.keySet()) {
            Bukkit.getLogger().log(Level.INFO, "Checking " + enchantment);
            if(isDraconicEnchant(enchantment)) {
                Bukkit.getLogger().log(Level.INFO, "Detected Draconic Enchantment " + enchantment.getKey().getKey());
                lore.add("§7" + enchantment.getName() + " " + NumberConverter.IntegerToRoman(enchants.get(enchantment)));
            }
        }
        Bukkit.getLogger().log(Level.INFO, "Print for edit: " + lore);
        storageMeta.setLore(lore);
        stack.setItemMeta(storageMeta);
    }

    public static void addDraconicEnchant(Enchantment enchantment, int level, ItemStack stack) {
        if(isDraconicEnchant(enchantment)) {
            if(!enchantment.canEnchantItem(stack)) return;
            int actualLevel = level;
            if (actualLevel > enchantment.getMaxLevel()) {
                actualLevel = enchantment.getMaxLevel();
            }
            stack.addUnsafeEnchantment(enchantment, actualLevel);
            ItemMeta meta = stack.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add("§7" + enchantment.getName() + " " + NumberConverter.IntegerToRoman(actualLevel));
            if (meta.hasLore()) {
                List<String> currentLore = meta.getLore();
                int i = 0;
                for (String s : meta.getLore()) {
                    if (s.contains(enchantment.getName())) {
                        currentLore.remove(i);
                    }
                    i++;
                }
                lore.addAll(currentLore);
            }
            meta.setLore(lore);
            stack.setItemMeta(meta);
        } else stack.addUnsafeEnchantment(enchantment, level);
    }


    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        List<ItemStack> stacks = event.getDrops();
        List<ItemStack> keep = new ArrayList<>();
        List<ItemStack> drop = new ArrayList<>();
        for(ItemStack stack : event.getDrops()) {
            if(stack.hasItemMeta()) {
                if(stack.getItemMeta().hasEnchant(DraconicEnchantments.SOUL_BOUND)) {
                    keep.add(stack);
                    continue;
                }
            }
            drop.add(stack);
        }
        event.getDrops().clear();
        event.getDrops().addAll(drop);
        if(keep.size() > 0) {
            AdvancedEvolution.cache.getConfig().createSection(event.getEntity().getUniqueId() + "");
            int count = 0;
            for (ItemStack s : keep) {
                AdvancedEvolution.cache.getConfig().getConfigurationSection(event.getEntity().getUniqueId() + "").set("" + count, s);
                count++;
            }
            AdvancedEvolution.cache.saveData();
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();


        if(AdvancedEvolution.cache.hasKey(player.getUniqueId() + "")) {
            ConfigurationSection section = AdvancedEvolution.cache.getConfig().getConfigurationSection(player.getUniqueId() + "");
            for(String s : section.getKeys(false)) {
                event.getPlayer().getInventory().addItem(section.getItemStack(s));
            }
            player.playSound(player.getLocation(), XSound.BLOCK_BEACON_ACTIVATE.parseSound(), 100f, 2.0f);
            AdvancedEvolution.cache.getConfig().set(player.getUniqueId().toString(), null);
            AdvancedEvolution.cache.saveData();
        }

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand() == null) return;
        if (!(player.getInventory().getItemInMainHand().hasItemMeta())) return;
        if (!(player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(DraconicEnchantments.AUTO_SMELT)))
            return;
        Block block = event.getBlock();
        if(block.getType() == XMaterial.SAND.parseMaterial()) {
            event.setDropItems(false);
            block.getLocation().getWorld().dropItemNaturally(block.getLocation(), XMaterial.GLASS.parseItem());
            block.getLocation().getWorld().spawnParticle(Particle.FLAME, block.getLocation().add(0.5, 0.5, 0.5), 5, 0 ,0, 0, 0.05);
        } else if(block.getType() == XMaterial.OAK_LOG.parseMaterial() || block.getType() == XMaterial.BIRCH_LOG.parseMaterial() || block.getType() == XMaterial.DARK_OAK_LOG.parseMaterial() || block.getType() == XMaterial.ACACIA_LOG.parseMaterial() || block.getType() == XMaterial.SPRUCE_LOG.parseMaterial() || block.getType() == XMaterial.JUNGLE_LOG.parseMaterial()) {
            event.setDropItems(false);
            block.getLocation().getWorld().dropItemNaturally(block.getLocation(), XMaterial.CHARCOAL.parseItem());
            block.getLocation().getWorld().spawnParticle(Particle.FLAME, block.getLocation().add(0.5, 0.5, 0.5), 5, 0 ,0, 0, 0.05);
            block.getLocation().getWorld().playSound(block.getLocation(), XSound.ITEM_FLINTANDSTEEL_USE.parseSound(), 0.5f, 1f);
        } else if(block.getType() == XMaterial.STONE.parseMaterial()) {
            event.setDropItems(false);
            block.getLocation().getWorld().dropItemNaturally(block.getLocation(), XMaterial.STONE.parseItem());
            block.getLocation().getWorld().spawnParticle(Particle.FLAME, block.getLocation().add(0.5, 0.5, 0.5), 1, 0 ,0, 0, 0.05);
        } else if(block.getType() == XMaterial.IRON_ORE.parseMaterial()) {
            event.setDropItems(false);
            block.getLocation().getWorld().dropItemNaturally(block.getLocation(), XMaterial.IRON_INGOT.parseItem());
            block.getLocation().getWorld().spawnParticle(Particle.FLAME, block.getLocation().add(0.5, 0.5, 0.5), 5, 0 ,0, 0, 0.05);
            block.getLocation().getWorld().playSound(block.getLocation(), XSound.ITEM_FLINTANDSTEEL_USE.parseSound(), 0.5f, 1f);
        } else if(block.getType() == XMaterial.GOLD_ORE.parseMaterial()) {
            event.setDropItems(false);
            block.getLocation().getWorld().dropItemNaturally(block.getLocation(), XMaterial.GOLD_INGOT.parseItem());
            block.getLocation().getWorld().spawnParticle(Particle.FLAME, block.getLocation().add(0.5, 0.5, 0.5), 5, 0 ,0, 0, 0.05);
            block.getLocation().getWorld().playSound(block.getLocation(), XSound.ITEM_FLINTANDSTEEL_USE.parseSound(), 0.5f, 1f);
        } else if(block.getType() == XMaterial.GOLD_ORE.parseMaterial()) {
            event.setDropItems(false);
            block.getLocation().getWorld().dropItemNaturally(block.getLocation(), XMaterial.GOLD_INGOT.parseItem());
            block.getLocation().getWorld().spawnParticle(Particle.FLAME, block.getLocation().add(0.5, 0.5, 0.5), 5, 0 ,0, 0, 0.05);
            block.getLocation().getWorld().playSound(block.getLocation(), XSound.ITEM_FLINTANDSTEEL_USE.parseSound(), 0.5f, 1f);
        } else if(block.getType() == XMaterial.ANCIENT_DEBRIS.parseMaterial()) {
            event.setDropItems(false);
            block.getLocation().getWorld().dropItemNaturally(block.getLocation(), XMaterial.NETHERITE_SCRAP.parseItem());
            block.getLocation().getWorld().spawnParticle(Particle.FLAME, block.getLocation().add(0.5, 0.5, 0.5), 5, 0 ,0, 0, 0.05);
            block.getLocation().getWorld().playSound(block.getLocation(), XSound.ITEM_FLINTANDSTEEL_USE.parseSound(), 0.5f, 1f);
        }

    }

    @EventHandler
    public void beheadingDamageByEntity(EntityDeathEvent entityEvent) {
        if(entityEvent.getEntity().getKiller() == null) return;
        Player player = entityEvent.getEntity().getKiller();
        if(player.getInventory().getItemInMainHand() == null) return;
        if(!(player.getInventory().getItemInMainHand().hasItemMeta())) return;
        if(!(player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(DraconicEnchantments.BEHEADING))) return;
        int enchantmentLevel = player.getInventory().getItemInMainHand().getItemMeta().getEnchantLevel(DraconicEnchantments.BEHEADING);

        if(entityEvent.getEntity() instanceof Zombie) {
            ItemStack stack = null;
            for(ItemStack s : entityEvent.getDrops()) {
                if(s.getType() == XMaterial.ZOMBIE_HEAD.parseMaterial()) {
                    stack = s;
                }
            }
            if(!(stack == null)) {
                entityEvent.getDrops().remove(stack);
            }

            ItemStack dropped = XMaterial.ZOMBIE_HEAD.parseItem();
            if(Math.random() < (enchantmentLevel * 14.285714285714285714285714285714d) / 100) {
                entityEvent.getDrops().add(dropped);
            }


        }

        if(entityEvent.getEntity() instanceof Creeper) {
            ItemStack stack = null;
            for(ItemStack s : entityEvent.getDrops()) {
                if(s.getType() == XMaterial.CREEPER_HEAD.parseMaterial()) {
                    stack = s;
                }
            }
            if(!(stack == null)) {
                entityEvent.getDrops().remove(stack);
            }

            ItemStack dropped = XMaterial.CREEPER_HEAD.parseItem();
            if(Math.random() < (enchantmentLevel * 14.285714285714285714285714285714d) / 100) {
                entityEvent.getDrops().add(dropped);
            }




        }

        if(entityEvent.getEntity() instanceof WitherSkeleton) {
            ItemStack stack = null;
            for(ItemStack s : entityEvent.getDrops()) {
                if(s.getType() == XMaterial.WITHER_SKELETON_SKULL.parseMaterial()) {
                    stack = s;
                }
            }
            if(!(stack == null)) {
                entityEvent.getDrops().remove(stack);
            }

            ItemStack dropped = XMaterial.WITHER_SKELETON_SKULL.parseItem();
            if(Math.random() < (enchantmentLevel * 14.285714285714285714285714285714d) / 100) {
                entityEvent.getDrops().add(dropped);
            }

            return;
        }

        if(entityEvent.getEntity() instanceof Skeleton) {
            ItemStack stack = null;
            for(ItemStack s : entityEvent.getDrops()) {
                if(s.getType() == XMaterial.SKELETON_SKULL.parseMaterial()) {
                    stack = s;
                }
            }
            if(!(stack == null)) {
                entityEvent.getDrops().remove(stack);
            }

            ItemStack dropped = XMaterial.SKELETON_SKULL.parseItem();
            if(Math.random() < (enchantmentLevel * 14.285714285714285714285714285714d) / 100) {
                entityEvent.getDrops().add(dropped);
            }

            return;


        }

        if(entityEvent.getEntity() instanceof Player) {
            Player killed = (Player) entityEvent.getEntity();
            ItemStack head = ItemUtil.createSimpleStack("§c" + killed.getName(), XMaterial.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(killed.getUniqueId()));
            head.setItemMeta(skullMeta);

            if(Math.random() < (enchantmentLevel * 14.285714285714285714285714285714d) / 100) {
                entityEvent.getDrops().add(head);
            }


        }


    }

    @EventHandler
    public void lifestealManager(EntityDamageByEntityEvent entityEvent) {
        if(entityEvent.getDamager() instanceof  Player) {
            Player player = (Player) entityEvent.getDamager();
            if(player.getInventory().getItemInMainHand() == null) return;
            if(!(player.getInventory().getItemInMainHand().hasItemMeta())) return;
            if(player.getHealth() == player.getMaxHealth()) return;
            if(entityEvent.getFinalDamage() < 1) return;
            if (!(player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(DraconicEnchantments.LIFE_STEEL)))
                return;
            int enchantmentLevel = player.getInventory().getItemInMainHand().getEnchantmentLevel(DraconicEnchantments.LIFE_STEEL);
            if(Math.random() < 0.1 * enchantmentLevel) {
                double stolenHP = (entityEvent.getFinalDamage() * 0.08) * enchantmentLevel;
                entityEvent.getEntity().getWorld().spawnParticle(Particle.SPELL_WITCH, entityEvent.getEntity().getLocation(), 5, 0, 0, 0, 0.2);
                new ChasingParticleEntity(AdvancedEvolution.getInstance(), entityEvent.getEntity().getLocation(), player, new Vector(Math.random() * 10 + 5, Math.random() * 10 + 5, Math.random() * 10 + 5), new Hitbox(1, 1, 1)) {

                    @Override
                    public void onCollision(Block block) {

                    }

                    @Override
                    public boolean isValid() {
                        if(!super.isValid()) {
                            return false;
                        }
                        if(!player.isOnline()) {
                            return false;
                        }
                        return true;
                    }

                    @Override
                    public Location getFocusLocation(Entity target) {
                        return ((LivingEntity)target).getEyeLocation();
                    }

                    @Override
                    public void onHit(Entity target) {
                        Player player = (Player) target;
                        addHealth(player, stolenHP);
                        player.playSound(player.getLocation(), XSound.BLOCK_CHORUS_FLOWER_GROW.parseSound(), 100f, 1f);
                       // player.getLocation().getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, player.getEyeLocation(), (int) (stolenHP / 2), 0.5, 0.5, 0.5 ,0);
                        remove();
                    }

                    @Override
                    public void spawnParticle(Location location) {
                        location.getWorld().spawnParticle(Particle.SPELL_WITCH, location, 1, 0, 0, 0, 0);
                    }
                };
            }
        }
    }

    private void addHealth(Player player, double value) {
        double health = player.getHealth();
        double maxHealth = player.getMaxHealth();
        if(health + value >= maxHealth) {
            player.setHealth(maxHealth);
        } else {
            player.setHealth(health + value);
        }
    }

    public static ItemStack book(Enchantment enchantment, int level) {
        ItemStack stack = XMaterial.ENCHANTED_BOOK.parseItem();
        addDraconicEnchant(enchantment, level, stack);
        return stack;
    }

    public static Enchantment randomDraconicEnchantment() {
        Random random = new Random();
        Field[] enchantments = DraconicEnchantments.class.getFields();
        try {
            return (Enchantment) enchantments[random.nextInt(enchantments.length)].get(DraconicEnchantments.class);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
