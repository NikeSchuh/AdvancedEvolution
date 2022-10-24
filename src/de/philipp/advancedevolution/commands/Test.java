package de.philipp.advancedevolution.commands;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.animations.animations.MightyChickenSpawn;
import de.philipp.advancedevolution.dimension.DimensionManager;
import de.philipp.advancedevolution.dimension.DraconicGenerator;
import de.philipp.advancedevolution.dimension.Meteor;
import de.philipp.advancedevolution.enchantments.DraconicEnchantments;
import de.philipp.advancedevolution.entities.bosses.BossArena;
import de.philipp.advancedevolution.entities.bosses.blazeinferno.BlazeInferno;
import de.philipp.advancedevolution.entities.bosses.gaiagurdian.GaiaGuardian;
import de.philipp.advancedevolution.entities.bosses.god.God;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.lib.xseries.XSound;
import de.philipp.advancedevolution.structures.FloatingIsle;
import de.philipp.advancedevolution.util.LocationUtil;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Test implements CommandExecutor {

    private BossArena bossArena;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return true;
        Player player = (Player) commandSender;
        if(strings.length > 0) {
            Location location = player.getLocation();
            new BukkitRunnable() {
                private int i = 0;
                @Override
                public void run() {
                    for(int d = 0; d < 1; d++) {
                        location.add(Math.sin(i), Math.sin(i), Math.sin(i));
                        location.getWorld().spawnParticle(Particle.DRAGON_BREATH, location, 1, 0, 0, 0, 0);
                        i++;
                    }
                }
            }.runTaskTimer(AdvancedEvolution.getInstance(), 0, 1);
            return true;
        }
        DimensionManager dimensionManager = AdvancedEvolution.getDimensionManager();
        if(player.getWorld().getName().equals(dimensionManager.draconic.getWorld().getName())) {
            player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            player.sendMessage("§cDeleting world ...");
            if(dimensionManager.draconic.delete()) {
                player.sendMessage("§cCreating world");
                dimensionManager.draconic.create();
                player.sendMessage("§aWorld created");
                player.teleport(dimensionManager.draconic.getWorld().getSpawnLocation());
            } else {
                player.sendMessage("§cDeletion failed. See console for further information.");
            }
        } else {
            player.teleport(Bukkit.getWorld("draconic").getSpawnLocation());
        }
        return true;
   }


}
