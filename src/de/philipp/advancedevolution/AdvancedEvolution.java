package de.philipp.advancedevolution;

import de.philipp.advancedevolution.advancements.DraconicAdvancements;
import de.philipp.advancedevolution.blocks.BlockManager;
import de.philipp.advancedevolution.blocks.blocks.*;
import de.philipp.advancedevolution.commands.MainCommand;
import de.philipp.advancedevolution.commands.Test;
import de.philipp.advancedevolution.config.SettingsConfig;
import de.philipp.advancedevolution.crafting.RecipeManager;
import de.philipp.advancedevolution.dimension.DimensionManager;
import de.philipp.advancedevolution.dimension.DraconicGenerator;
import de.philipp.advancedevolution.dimension.GeneratorHook;
import de.philipp.advancedevolution.dimension.dimensions.DraconicDimension;
import de.philipp.advancedevolution.enchantments.DraconicEnchantments;
import de.philipp.advancedevolution.entities.DraconicBossEntity;
import de.philipp.advancedevolution.entities.MobManager;
import de.philipp.advancedevolution.entities.effects.DragonHeart;
import de.philipp.advancedevolution.events.customevents.listeners.ArmorListener;
import de.philipp.advancedevolution.items.InteractManager;
import de.philipp.advancedevolution.items.ItemRegistry;
import de.philipp.advancedevolution.items.items.Selector;
import de.philipp.advancedevolution.items.items.armor.*;
import de.philipp.advancedevolution.items.items.items.*;
import de.philipp.advancedevolution.items.items.spawners.ItemWyvernGuardianSpawner;
import de.philipp.advancedevolution.items.items.tools.weapons.WyvernSwordItem;
import de.philipp.advancedevolution.items.items.tools.weapons.BasicSwordItem;
import de.philipp.advancedevolution.items.items.tools.weapons.ChaoticSwordItem;
import de.philipp.advancedevolution.items.items.tools.weapons.DraconicSwordItem;
import de.philipp.advancedevolution.items.items.tools.weapons.GodlySwordItem;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.menus.ItemBrowser;
import de.philipp.advancedevolution.menus.PerformanceMenu;
import de.philipp.advancedevolution.menus.RegionBrowser;
import de.philipp.advancedevolution.region.DraconicRegion;
import de.philipp.advancedevolution.region.RegionManager;
import de.philipp.advancedevolution.shield.IShieldHook;
import de.philipp.advancedevolution.shield.Shield;
import de.philipp.advancedevolution.shield.ShieldManager;
import de.philipp.advancedevolution.util.Cuboid;
import de.philipp.advancedevolution.util.npl.config.Datafile;
import de.philipp.advancedevolution.util.npl.config.Datafolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class AdvancedEvolution extends JavaPlugin {

    public static String ConsolePrefix = "§c[§fAdvancedEvolution§c] §7";
    public static String ChatPrefix = "§c[§6AdvancedEvolution§c] §7";
    public static Datafolder pluginFolder = new Datafolder("plugins/AdvancedEvolution");
    public static Datafolder structureFolder = new Datafolder("plugins/AdvancedEvolution/Structures");
    public static Datafile cache = new Datafile("plugins/AdvancedEvolution/cache.yml");
    public static Datafile regions = new Datafile("plugins/AdvancedEvolution/regions.yml");

    private static AdvancedEvolution instance;
    private static SettingsConfig settingsConfig;
    private static ShieldManager shieldManager;
    private static BlockManager blockManager;
    private static RecipeManager recipeManager;
    private static GeneratorHook generatorHook;
    private static RegionManager regionManager;
    private static MobManager mobManager;
    private static DimensionManager dimensionManager;

    public static ItemBrowser itemBrowser;
    public static RegionBrowser regionBrowser;
    public static PerformanceMenu performanceMenu;


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

        ItemRegistry.registerItem(new DraconicGrinder());
        ItemRegistry.registerItem(new DraconicCharger());
        ItemRegistry.registerItem(new DraconicMiner());
        ItemRegistry.registerItem(new DraconicMiner2());
        ItemRegistry.registerItem(new DraconicMiner3());
        ItemRegistry.registerItem(new DraconicWorkbench());
        ItemRegistry.registerItem(new de.philipp.advancedevolution.blocks.blocks.DraconicGenerator());
        ItemRegistry.registerItem(new DraconicEnergyStorage1());
        ItemRegistry.registerItem(new DraconicEnergyStorage2());
        ItemRegistry.registerItem(new DraconicEnergyStorage3());
        ItemRegistry.registerItem(new DraconicEnergyStorageCreative());
        ItemRegistry.registerItem(new DraconicSolarPanel1());
        ItemRegistry.registerItem(new DraconicWirelessCharger());
        ItemRegistry.registerItem(new DraconicFarmer());
        ItemRegistry.registerItem(new DraconicPortal());
        ItemRegistry.registerItem(new DraconicHopper());

        ItemRegistry.registerItem(new ItemDraconiumDust());
        ItemRegistry.registerItem(new ItemWyvernCore());
        ItemRegistry.registerItem(new ItemDraconiumCore());
        ItemRegistry.registerItem(new ItemAwakenedCore());
        ItemRegistry.registerItem(new ItemChaoticCore());
        ItemRegistry.registerItem(new ItemGodlyCore());
        ItemRegistry.registerItem(new ItemInfernoPowder());
        ItemRegistry.registerItem(new ItemCrystalBinder());
        ItemRegistry.registerItem(new Selector());
        ItemRegistry.registerItem(new ItemWyvernGuardianSpawner());
        ItemRegistry.registerItem(new ItemEyeWyvernGuardian());
        ItemRegistry.registerItem(new ItemAwakenedDraconium());
        ItemRegistry.registerItem(new ItemCircuit());
        ItemRegistry.registerItem(new ItemAdvancedCircuit());
        ItemRegistry.registerItem(new ItemWirelessTransmitter());
        ItemRegistry.registerItem(new ItemChaosShard());
        ItemRegistry.registerItem(new ItemDragonHeart());

        send("Preloading crafting recipes ...");

        ItemRegistry.reloadCraftables();

        DraconicEnchantments.registerAll();

        send("Items successfully registered and loaded");

        getCommand("test").setExecutor(new Test());
        getCommand("advancedevolution").setExecutor(new MainCommand());

        send("Loading configs ...");
        send("settings.yml successfully loaded.");
        settingsConfig = new SettingsConfig();

        send("Initiating managers ...");
        shieldManager = new ShieldManager(this);
        generatorHook = new GeneratorHook(this);
        regionManager = new RegionManager(this);
        mobManager = new MobManager(this);
        dimensionManager = new DimensionManager(this);
        send("Loading shield manager ...");
        shieldManager.onLoad();
        send("Successfully loaded shield manager.");
        send("Registering listeners ...");

        Bukkit.getPluginManager().registerEvents(new ArmorListener(new ArrayList<>()), this);
        Bukkit.getPluginManager().registerEvents(new InteractManager(), this);
        Bukkit.getPluginManager().registerEvents(new DraconicEnchantments(), this);


        DraconicAdvancements.registerAllAdvancements();

        send("Loading BlockManager ...");
        recipeManager = new RecipeManager(this, new ItemRegistry());
        blockManager = new BlockManager(this);

        for(String key : regions.getConfig().getKeys(false)) {
            ConfigurationSection section = regions.getConfig().getConfigurationSection(key);
            try{
                regionManager.loadRegion(DraconicRegion.of(section));
            }catch (Exception e) {
                send("Error loading Region " + section.getCurrentPath());
                e.printStackTrace();
            }
        }

        regionManager.registerRegion(new DraconicRegion(new Cuboid(new Location(Bukkit.getWorld("world"), 50, 50, 50), new Location(Bukkit.getWorld("world"), -50, 0, -50)), XMaterial.ELYTRA.parseMaterial(), "Test", false, true));


        itemBrowser = new ItemBrowser(Arrays.asList(ItemRegistry.getRegisteredItemsSorted()));
        regionBrowser = new RegionBrowser(regionManager.getRegions());
        performanceMenu = new PerformanceMenu();


        send("Successfully loaded AdvancedEvolution :)");
    }

    public static AdvancedEvolution getInstance() {
        return instance;
    }
    public static ShieldManager getShieldManager() {return shieldManager;}
    public static BlockManager getBlockManager() {
        return blockManager;
    }
    public static DimensionManager getDimensionManager() {
        return dimensionManager;
    }
    public static SettingsConfig getSettings() {
        return settingsConfig;
    }

    public static RegionManager getRegionManager() {
        return regionManager;
    }

    public static MobManager getMobManager() {
        return mobManager;
    }

    public static boolean registerHook(Player player, IShieldHook iShieldHook) {
        return ShieldManager.playerShieldMap.get(player.getUniqueId()).registerHook(iShieldHook);
    }

    public static Shield wrapShield(Player player) {
        return ShieldManager.playerShieldMap.get(player.getUniqueId());
    }

    public static Shield wrapShield(UUID uuid) {
        return ShieldManager.playerShieldMap.getOrDefault(uuid, null);
    }



    public static RecipeManager getRecipeManager() {
        return recipeManager;
    }
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String uid) {
        return new DraconicGenerator((DraconicDimension) dimensionManager.draconic);
    }

    @Override
    public void onDisable() {
        shieldManager.onDisable();
        blockManager.onDisable();
        send("");
        int i = DraconicBossEntity.bossEntities.size();
        for(int ii = 0; ii < i; ii++) {
            DraconicBossEntity.bossEntities.get(0).remove();
        }

    }

    public static void send(String s) {
         Bukkit.getConsoleSender().sendMessage(ConsolePrefix + s);
    }
}
