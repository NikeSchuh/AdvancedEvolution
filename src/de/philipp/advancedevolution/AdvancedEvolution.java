package de.philipp.advancedevolution;

import de.philipp.advancedevolution.achievments.TestAchievment;
import de.philipp.advancedevolution.blocks.BlockManager;
import de.philipp.advancedevolution.blocks.custombases.DraconicCharger;
import de.philipp.advancedevolution.blocks.custombases.TestBlockBase;
import de.philipp.advancedevolution.commands.MainCommand;
import de.philipp.advancedevolution.commands.Test;
import de.philipp.advancedevolution.config.SettingsConfig;
import de.philipp.advancedevolution.dimension.DraconicGenerator;
import de.philipp.advancedevolution.enchantments.DraconicEnchantments;
import de.philipp.advancedevolution.events.customevents.listeners.ArmorListener;
import de.philipp.advancedevolution.events.customevents.listeners.DraconicItemInteractListener;
import de.philipp.advancedevolution.items.ItemRegistry;
import de.philipp.advancedevolution.items.items.armor.*;
import de.philipp.advancedevolution.items.items.tools.weapons.WyvernSwordItem;
import de.philipp.advancedevolution.items.items.tools.weapons.BasicSwordItem;
import de.philipp.advancedevolution.items.items.tools.weapons.ChaoticSwordItem;
import de.philipp.advancedevolution.items.items.tools.weapons.DraconicSwordItem;
import de.philipp.advancedevolution.items.items.tools.weapons.GodlySwordItem;
import de.philipp.advancedevolution.shield.ShieldManager;
import de.philipp.advancedevolution.util.npl.config.Datafile;
import de.philipp.advancedevolution.util.npl.config.Datafolder;
import org.bukkit.Bukkit;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class AdvancedEvolution extends JavaPlugin {

    public static String ConsolePrefix = "§c[§fAdvancedEvolution§c] §7";
    public static String ChatPrefix = "§c[§6AdvancedEvolution§c] §7";
    public static Datafolder pluginFolder = new Datafolder("plugins/AdvancedEvolution");
    public static Datafile cache = new Datafile("plugins/AdvancedEvolution/cache.yml");

    private static AdvancedEvolution instance;
    private static SettingsConfig settingsConfig;
    private static ShieldManager shieldManager;
    private static BlockManager blockManager;


    @Override
    public void onEnable() {
        instance = this;
        send("Loading Plugin ...");
        send("Loading NBTApi ...");

        send("Registering Items ...");

        ItemRegistry.registerItem(new BasicArmorBoots());
        ItemRegistry.registerItem(new BasicArmorChestplate());
        ItemRegistry.registerItem(new BasicArmorLeggings());
        ItemRegistry.registerItem(new BasicArmorHelmet());

        ItemRegistry.registerItem(new WyvernArmorBoots());
        ItemRegistry.registerItem(new WyvernArmorChestplate());
        ItemRegistry.registerItem(new WyvernArmorLeggings());
        ItemRegistry.registerItem(new WyvernArmorHelmet());

        ItemRegistry.registerItem(new DraconicArmorBoots());
        ItemRegistry.registerItem(new DraconicArmorChestplate());
        ItemRegistry.registerItem(new DraconicArmorLeggings());
        ItemRegistry.registerItem(new DraconicArmorHelmet());

        ItemRegistry.registerItem(new ChaoticArmorBoots());
        ItemRegistry.registerItem(new ChaoticArmorChestplate());
        ItemRegistry.registerItem(new ChaoticArmorHelmet());
        ItemRegistry.registerItem(new ChaoticArmorLeggings());

        ItemRegistry.registerItem(new GodlyArmorBoots());
        ItemRegistry.registerItem(new GodlyArmorChestplate());
        ItemRegistry.registerItem(new GodlyArmorHelmet());
        ItemRegistry.registerItem(new GodlyArmorLeggings());

        ItemRegistry.registerItem(new BasicSwordItem());
        ItemRegistry.registerItem(new WyvernSwordItem());
        ItemRegistry.registerItem(new DraconicSwordItem());
        ItemRegistry.registerItem(new ChaoticSwordItem());
        ItemRegistry.registerItem(new GodlySwordItem());

        ItemRegistry.registerItem(new TestBlockBase());
        ItemRegistry.registerItem(new DraconicCharger());

        DraconicEnchantments.registerAll();

        send("Items successfully registered and loaded");

        getCommand("test").setExecutor(new Test());
        getCommand("advancedevolution").setExecutor(new MainCommand());

        send("Loading configs ...");
        send("settings.yml successfully loaded.");
        settingsConfig = new SettingsConfig();

        send("Initiating shield manager ...");
        shieldManager = new ShieldManager(this);
        send("Loading shield manager ...");
        shieldManager.onLoad();
        send("Successfully loaded shield manager.");
        send("Registering listeners ...");

        Bukkit.getPluginManager().registerEvents(new ArmorListener(new ArrayList<>()), this);
        Bukkit.getPluginManager().registerEvents(new DraconicItemInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new DraconicEnchantments(), this);

        send("Loading BlockManager ...");
        blockManager = new BlockManager(this);
        send("Successfully loaded AdvancedEvolution :)");
        new TestAchievment(this);
    }

    public static AdvancedEvolution getInstance() {
        return instance;
    }
    public static ShieldManager getShieldManager() {return shieldManager;}
    public static BlockManager getBlockManager() {
        return blockManager;
    }


    public ChunkGenerator getDefaultWorldGenerator(String worldName, String uid) {
        return new DraconicGenerator();
    }

    @Override
    public void onDisable() {

        send("");

    }

    public static void send(String s) {
        Bukkit.getConsoleSender().sendMessage(ConsolePrefix + s);
    }
}
