package de.philipp.advancedevolution.achievments;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.lib.xseries.XSound;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

public abstract class DraconicAchievement implements Listener {

    public DraconicAchievement(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public abstract ChatColor getAchievementColor();
    public abstract String getStorageKey();
    public abstract String getAchievementName();
    public abstract String getAchievementDescription();
    public abstract XSound getAchievementSound();
    public abstract float getAchievementSoundPitch();

    public void executeAdvancement(Player player, DraconicAchievement achievement) {
        if(isAnnounceAdvancementsEnabled(player.getWorld())) {
            if(!hasAchievement(player.getUniqueId())) {
                TextComponent message = new TextComponent();
                message.setText(buildAchievementString());
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getAchievementColor() + getAchievementDescription()).create()));
                for(Player s : Bukkit.getOnlinePlayers()) {
                    s.spigot().sendMessage(new TextComponent(buildAchievementBase(player) + " "), message);
                }
                player.playSound(player.getLocation(), getAchievementSound().parseSound(), 100f, getAchievementSoundPitch());
                setAchieved(player.getUniqueId(), true);
            }
        }
    }

    private String buildAchievementString() {
        return getAchievementColor() + "[" + getAchievementName() + "]";
    }

    private String buildAchievementBase(Player player) {
        return player.getDisplayName() + " has made the advancement";
    }

    public boolean hasAchievement(UUID uuid) {
        FileConfiguration configuration = AdvancedEvolution.cache.getConfig();
        if(configuration.getString("Achievement." + uuid + "." + getStorageKey()) != null) {
            return configuration.getBoolean("Achievement." + uuid + "." + getStorageKey());
        }
        return false;
    }

    public void setAchieved(UUID uuid, boolean b) {
        FileConfiguration configuration = AdvancedEvolution.cache.getConfig();
        configuration.set("Achievement." + uuid + "." + getStorageKey(), b);
        AdvancedEvolution.cache.saveData();
    }

    public boolean isAnnounceAdvancementsEnabled(World world) {
        return world.isGameRule("announceAdvancements");
    }

}
