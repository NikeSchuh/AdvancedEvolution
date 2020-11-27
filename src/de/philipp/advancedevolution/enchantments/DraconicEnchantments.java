package de.philipp.advancedevolution.enchantments;

import com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector;
import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.lib.xseries.XSound;
import de.philipp.advancedevolution.util.NumberConverter;
import de.philipp.advancedevolution.util.item.ItemUtil;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTEntity;
import de.tr7zw.nbtapi.NBTFile;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DraconicEnchantments implements Listener {


    public static final Enchantment BEHEADING = new BeheadingEnchantment("beheading", "Beheading", 7);
    public static final Enchantment SOUL_BOUND = new SoulboundEnchantment("soulbound", "Soulbound", 1);

    public static void registerAll() {
        boolean beheading = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(BEHEADING);
        boolean soulbound = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(SOUL_BOUND);
        if(!beheading) {
            registerEnchantment(BEHEADING);
        }
        if(!soulbound) {
            registerEnchantment(SOUL_BOUND);
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

    public static void addDraconicEnchant(Enchantment enchantment, int level, ItemStack stack) {
        int actualLevel = level;
        if(actualLevel > enchantment.getMaxLevel()) {
            actualLevel = enchantment.getMaxLevel();
        }
        stack.addUnsafeEnchantment(enchantment, actualLevel);
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add("§7" + enchantment.getName() + " " + NumberConverter.IntegerToRoman(actualLevel));
        if(meta.hasLore()) {
            List<String> currentLore = meta.getLore();
            int i = 0;
            for(String s : meta.getLore()) {
                if(s.contains(enchantment.getName())) {
                    currentLore.remove(i);
                }
                i++;
            }
            lore.addAll(currentLore);
        }
        meta.setLore(lore);
        stack.setItemMeta(meta);
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
}
