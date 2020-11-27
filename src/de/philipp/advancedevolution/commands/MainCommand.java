package de.philipp.advancedevolution.commands;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.FireBall;
import de.philipp.advancedevolution.entities.EnergyToEntity;
import de.philipp.advancedevolution.entities.Hitbox;
import de.philipp.advancedevolution.entities.ParticleEntity;
import de.philipp.advancedevolution.items.*;
import de.philipp.advancedevolution.items.custombases.DraconicArmorItemBase;
import de.philipp.advancedevolution.items.custombases.DraconicSwordItemBase;
import de.philipp.advancedevolution.lib.xseries.XSound;
import de.philipp.advancedevolution.menus.AdminPanel;
import de.philipp.advancedevolution.menus.ItemBrowser;
import de.philipp.advancedevolution.shield.Shield;
import de.philipp.advancedevolution.shield.ShieldManager;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(strings.length == 1) {
                if(strings[0].equalsIgnoreCase("admin")) {
                    new AdminPanel(AdvancedEvolution.getInstance(), player);
                } else if(strings[0].equalsIgnoreCase("checknbt")) {
                    NBTItem item = new NBTItem(player.getInventory().getItemInMainHand());
                    for(String s1 : item.getKeys()) {
                        player.sendMessage(s1);
                    }

                } else if(strings[0].equalsIgnoreCase("admin")) {
                    List<DraconicItemBase> itemBases = new ArrayList<>();
                    for(String dataName : ItemRegistry.itemList.keySet()) {
                        itemBases.add(ItemRegistry.getItemBase(dataName));
                    }
                    ItemBrowser browser = new ItemBrowser(itemBases);
                    browser.open(player, 1);
                } else if(strings[0].equalsIgnoreCase("fireball")) {
                   Location location = player.getLocation().add(0, 5, 0);
                    Shield shield = ShieldManager.playerShieldMap.get(player.getUniqueId());
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if(shield.getEnergyCurrent() + 20000000 > shield.getEnergyCapacity()) return;
                            EnergyToEntity energyToEntity = new EnergyToEntity(
                                    AdvancedEvolution.getInstance(),
                                    location.clone(),
                                    player,
                                    new Vector(15, 15 ,15),
                                    new Hitbox(2d, 2d, 2d),
                                    20000000,
                                    XSound.AMBIENT_UNDERWATER_EXIT);
                            energyToEntity.getUUID();
                            energyToEntity.setVelocity(new Vector((Math.random() - Math.random()) * 10, (Math.random() - Math.random()) * 10, (Math.random() - Math.random()) * 10));
                        }
                    }.runTaskTimer(AdvancedEvolution.getInstance(), 0, 30);




                } else if(strings[0].equalsIgnoreCase("fillenergy")) {
                    Shield shield = ShieldManager.playerShieldMap.get(player.getUniqueId());
                    shield.addEnergy(shield.getEnergyCapacity());
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
            }
        }
        return list;
    }
}
