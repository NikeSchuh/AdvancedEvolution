package de.philipp.advancedevolution.animations.animations;

import de.philipp.advancedevolution.animations.LivingEntityAnimation;
import de.philipp.advancedevolution.animations.PlayerAnimation;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class GodExecuteAnimation extends LivingEntityAnimation  {

    private long delay = 200;
    private LivingEntity executed;

    public GodExecuteAnimation(Plugin plugin, LivingEntity entity, LivingEntity executed) {
        super(plugin, entity);
        this.executed = executed;
    }


    @Override
    public void onAnimationTick(LivingEntity entity) {
       for(int i = 0; i < 100; i++) {
           executed.getLocation().getWorld().strikeLightningEffect(executed.getLocation());
       }
       executed.damage(9999, entity);
        this.cancel();
    }
}
