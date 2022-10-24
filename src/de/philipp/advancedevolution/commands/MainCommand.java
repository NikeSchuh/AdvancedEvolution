package de.philipp.advancedevolution.commands;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.advancements.DraconicAdvancements;
import de.philipp.advancedevolution.animations.animations.WyvernGuardianSpawn;
import de.philipp.advancedevolution.blocks.BlockManager;
import de.philipp.advancedevolution.blocks.DraconicBlock;
import de.philipp.advancedevolution.blocks.DraconicBlockBase;
import de.philipp.advancedevolution.blocks.InventoryContainer;
import de.philipp.advancedevolution.enchantments.DraconicEnchantments;
import de.philipp.advancedevolution.entities.bosses.blazeinferno.BlazeInferno;
import de.philipp.advancedevolution.entities.bosses.chaosgurdian.ChaosGuardian;
import de.philipp.advancedevolution.entities.bosses.gaiagurdian.GaiaGuardian;
import de.philipp.advancedevolution.entities.bosses.god.God;
import de.philipp.advancedevolution.entities.bosses.piglinlord.PiglinLord;
import de.philipp.advancedevolution.entities.bosses.supreme.SupremeLeader;
import de.philipp.advancedevolution.entities.bosses.wyvernguardian.WyvernGuardian;
import de.philipp.advancedevolution.entities.particles.EnergyToEntity;
import de.philipp.advancedevolution.entities.particles.Hitbox;
import de.philipp.advancedevolution.items.*;
import de.philipp.advancedevolution.items.custombases.DraconicArmorItemBase;
import de.philipp.advancedevolution.items.custombases.DraconicSwordItemBase;
import de.philipp.advancedevolution.items.items.Selector;
import de.philipp.advancedevolution.lib.xseries.XSound;
import de.philipp.advancedevolution.menus.AdminPanel;
import de.philipp.advancedevolution.menus.ItemBrowser;
import de.philipp.advancedevolution.nms.SkyUtils;
import de.philipp.advancedevolution.shield.Shield;
import de.philipp.advancedevolution.shield.ShieldManager;
import de.philipp.advancedevolution.structures.FloatingIsle;
import de.philipp.advancedevolution.structures.Structure;
import de.philipp.advancedevolution.structures.StructureFile;
import de.philipp.advancedevolution.util.Cuboid;
import de.philipp.advancedevolution.util.Pair;
import de.philipp.advancedevolution.util.npl.config.Datafile;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

public class MainCommand implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(strings.length == 1) {
                if(strings[0].equalsIgnoreCase("admin")) {
                    new AdminPanel(AdvancedEvolution.getInstance(), player).open();
                } else if(strings[0].equalsIgnoreCase("checknbt")) {
                    NBTItem item = new NBTItem(player.getInventory().getItemInMainHand());
                    player.sendMessage(item + "");
                } else if(strings[0].equalsIgnoreCase("fillenergy")) {
                    Shield shield = ShieldManager.playerShieldMap.get(player.getUniqueId());
                    shield.addEnergy(shield.getEnergyCapacity());
                } else if(strings[0].equalsIgnoreCase("wg")) {
                   // new WyvernGuardianSpawn(player.getLocation());
                    new PiglinLord(player.getLocation().add(0, -1, 0));
                }else if(strings[0].equalsIgnoreCase("block")) {
                    Block block = player.getTargetBlockExact(5);
                    if(DraconicBlock.isDraconicBlock(block)) {
                        DraconicBlock draconicBlock = new DraconicBlock(block);
                        DraconicBlockBase base = draconicBlock.getBlockBase();
                        player.sendMessage("Base:" + base.getDataName());
                        if(InventoryContainer.isContainer(draconicBlock)) {
                            InventoryContainer container = new InventoryContainer(draconicBlock);
                            for(ItemStack stack : container.getExactContents()) {
                                player.sendMessage("§c" + stack.getAmount() + "x §8" + stack.getType());
                            }
                        }

                    }
                } else if(strings[0].equalsIgnoreCase("sync")) {
                    Block block = player.getTargetBlockExact(5);
                    if(DraconicBlock.isDraconicBlock(block)) {
                        DraconicBlock draconicBlock = new DraconicBlock(block);
                        DraconicBlockBase base = draconicBlock.getBlockBase();
                        player.sendMessage("Base:" + base.getDataName());
                        if (InventoryContainer.isContainer(draconicBlock)) {
                            InventoryContainer container = new InventoryContainer(draconicBlock);
                            container.syncInventoryWithContainer();
                        }
                    }
                } else if(strings[0].equalsIgnoreCase("boss")) {
                    new SupremeLeader(player.getLocation());
                } else if(strings[0].equalsIgnoreCase("god")) {
                    BlazeInferno blazeInferno = new BlazeInferno(player.getLocation());
                } else if(strings[0].equalsIgnoreCase("enchant")) {
                    DraconicEnchantments.addDraconicEnchant(DraconicEnchantments.SOUL_BOUND, 1, player.getInventory().getItemInMainHand());
                } else if(strings[0].equalsIgnoreCase("dim")) {
                    if(!AdvancedEvolution.getDimensionManager().isDraconicDimensionEnabled()) {
                        player.sendMessage("§cNot enabled.");
                    } else {
                        if(player.getWorld().getUID().equals(AdvancedEvolution.getDimensionManager().draconic.getWorld().getUID())) {
                            for(Player players : AdvancedEvolution.getDimensionManager().draconic.getWorld().getPlayers()) {
                                players.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
                            }
                            AdvancedEvolution.getDimensionManager().draconic.delete();
                            AdvancedEvolution.getDimensionManager().draconic.create();
                            player.teleport(AdvancedEvolution.getDimensionManager().draconic.getWorld().getSpawnLocation());
                            return true;
                        }
                        player.teleport(AdvancedEvolution.getDimensionManager().draconic.getWorld().getSpawnLocation());
                    }
                } else if(strings[0].equalsIgnoreCase("performance")) {
                    HashMap<String, Integer> map = new HashMap<>();

                    player.sendMessage("§7Ticked Blocks:");
                    for(Block block : BlockManager.blocks) {
                        DraconicBlock draconicBlock = new DraconicBlock(block);
                        map.put(draconicBlock.getDataName(), map.getOrDefault(draconicBlock.getDataName(), 0) + 1);
                    }
                    for(String key : map.keySet()) {
                        Integer amount = map.get(key);
                        player.sendMessage("§c" + key + "§7: §e" + amount);
                    }
                    player.sendMessage("§7Task Manager:");
                    for(Block block : BlockManager.syncedTicker.keySet()) {
                        BukkitTask bukkitTask = BlockManager.syncedTicker.get(block);
                        player.sendMessage("§cTask " + bukkitTask.getTaskId() + "(§e" + bukkitTask.getOwner().getName() + "§c)§7: §e" + bukkitTask);
                    }

                }
            } else if(strings.length == 2) {
                if(strings[0].equalsIgnoreCase("getitem")) {
                   DraconicItem item = DraconicItem.instantiateItem(strings[1]);
                   if(item.getItemBase() instanceof DraconicArmorItemBase) {
                       DraconicArmorItem armorItem = DraconicItem.instantiateArmorItem(strings[1]);
                       player.getInventory().addItem(armorItem.getCurrentStack());
                   } else if(item.getItemBase() instanceof DraconicSwordItemBase) {
                       DraconicSwordItem swordItem = DraconicItem.instantiateSwordItem(strings[1]);
                       player.getInventory().addItem(swordItem.getCurrentStack());
                   } else {
                       player.getInventory().addItem(DraconicItem.instantiateItem(strings[1]).getCurrentStack());
                   }
                } else if(strings[0].equalsIgnoreCase("achieve")) {
                    DraconicAdvancements draconicAdvancements = DraconicAdvancements.valueOf(strings[1]);
                    DraconicAdvancements.addCriteria(draconicAdvancements, player);
                } else if(strings[0].equalsIgnoreCase("damage")) {
                        double dmg = 0;
                        try{
                            dmg = Double.valueOf(strings[1]);
                            player.damage(dmg, player);
                        }catch (Exception e) {
                            player.sendMessage(AdvancedEvolution.ChatPrefix + "§6" + strings[1]+ " §7seems to not be a number ;(");
                        }
                } else if(strings[0].equalsIgnoreCase("save")) {
                    String name = strings[1];
                    StructureFile structureFile = new StructureFile(name);
                    Pair<Location, Location> pair = Selector.locationMap.get(player.getUniqueId());
                    Cuboid cuboid = new Cuboid(pair.getKey(), pair.getValue());
                    structureFile.saveBlocks(cuboid);
                    structureFile.saveEntities(cuboid);
                } else if(strings[0].equalsIgnoreCase("load")) {
                    String name = strings[1];
                    new Structure() {
                        @Override
                        public void generate(Location location, Random random) {
                            generateBlocks(location, random);
                            getStructuralData().spawnEntities(location);
                        }

                        @Override
                        public void populateChest(Inventory inventory, Random random) {

                        }

                        @Override
                        public StructureFile getStructuralData() {
                            return new StructureFile(name);
                        }
                    }.generate(player.getLocation(), new Random());
                }
            }  else if(strings.length == 4) {
                if(strings[0].equalsIgnoreCase("save")) {
                    String name = strings[1];
                    boolean generate = Boolean.valueOf(strings[2]);
                    double chance = Double.valueOf(strings[3]);
                    StructureFile structureFile = new StructureFile(name);
                    structureFile.setGenerate(generate);
                    structureFile.setChance(chance);
                    Pair<Location, Location> pair = Selector.locationMap.get(player.getUniqueId());
                    Cuboid cuboid = new Cuboid(pair.getKey(), pair.getValue());
                    structureFile.saveBlocks(cuboid);
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> list = new ArrayList<>();
        if(strings.length == 1) {
            list.add("admin");
            list.add("checknbt");
            list.add("getitem");
            list.add("fireball");
            list.add("fillenergy");
            list.add("wg");
            list.add("define");
            list.add("dim");
            list.add("achieve");
            for(String string : new ArrayList<>(list)) {
                if(!string.contains(strings[0])) {
                    list.remove(string);
                }
            }
        } else if(strings.length == 2) {
            if(strings[0].equalsIgnoreCase("getitem")) {
                if(strings[1] == null) {
                    for (String s1 : ItemRegistry.itemList.keySet()) {
                        list.add(s1);
                    }
                    return list;
                } else {
                    for (String s1 : ItemRegistry.itemList.keySet()) {
                        if(s1.contains(strings[1])) {
                            list.add(s1);
                        }
                    }
                    return list;
                }
            } else if(strings[0].equalsIgnoreCase("achieve")) {
                Arrays.stream(DraconicAdvancements.values()).forEach(key -> {
                    list.add(key.name());
                });
            }
        }
        return list;
    }
}
