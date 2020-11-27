package de.philipp.advancedevolution.commands;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.FireBall;
import de.philipp.advancedevolution.enchantments.DraconicEnchantments;
import de.philipp.advancedevolution.items.*;
import de.philipp.advancedevolution.items.custombases.DraconicArmorItemBase;
import de.philipp.advancedevolution.util.item.ItemUtil;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Test implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return true;
        Player player = (Player) commandSender;
        player.damage(Integer.valueOf(strings[0]));
        return true;
}
}
