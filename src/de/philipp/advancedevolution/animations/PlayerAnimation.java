package de.philipp.advancedevolution.animations;

import com.google.gson.internal.$Gson$Preconditions;
import net.md_5.bungee.api.chat.hover.content.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class PlayerAnimation extends LivingEntityAnimation {

    private Player player;

    public PlayerAnimation(Plugin plugin, Player player) {
        super(plugin, player);
        this.player = player;
    }

    @Override
    public void onAnimationTick(LivingEntity entity) {
        onAnimationTick(player);
    }

    public abstract void onAnimationTick(Player player);
}
