package de.philipp.advancedevolution.shield;

import de.philipp.advancedevolution.AdvancedEvolution;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class ShieldManager implements Listener {

    public static final HashMap<UUID, Shield> playerShieldMap = new HashMap<>();

    private Plugin plugin;

    public ShieldManager(Plugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(!playerShieldMap.containsKey(event.getPlayer().getUniqueId())) {
            playerShieldMap.put(event.getPlayer().getUniqueId(), new Shield(plugin, event.getPlayer()));
        } else {
            Shield shield = playerShieldMap.get(event.getPlayer().getUniqueId());
            shield.setDisabled(false);
        }
    }

    public void onDisable() {
        for(UUID uuid : playerShieldMap.keySet()) {
            Shield shield = playerShieldMap.get(uuid);
            shield.onUnload();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if(playerShieldMap.containsKey(event.getPlayer().getUniqueId())) {
            Shield shield = playerShieldMap.get(event.getPlayer().getUniqueId());
            shield.setDisabled(false);
        } else {
            playerShieldMap.put(event.getPlayer().getUniqueId(), new Shield(plugin, event.getPlayer()));
            Shield shield = playerShieldMap.get(event.getPlayer().getUniqueId());
            shield.setDisabled(false);
        }
    }

    public void onLoad() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            Shield shield = new Shield(plugin, player);
            if(!playerShieldMap.containsKey(player)) {
                playerShieldMap.put(player.getUniqueId(), shield);
            }
        }
    }

    public Shield getShield(Player player) {
        return playerShieldMap.get(player.getUniqueId());
    }




}
