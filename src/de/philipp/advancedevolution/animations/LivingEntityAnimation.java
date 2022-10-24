package de.philipp.advancedevolution.animations;

import net.md_5.bungee.api.chat.hover.content.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

public abstract class LivingEntityAnimation extends Animation{

    private LivingEntity entity;

    public LivingEntityAnimation(Plugin plugin, LivingEntity entity) {
        super(plugin);
        this.entity = entity;
    }

    @Override
    public void onAnimationTick() {
        onAnimationTick(entity);
    }

    public abstract void onAnimationTick(LivingEntity entity);
}
