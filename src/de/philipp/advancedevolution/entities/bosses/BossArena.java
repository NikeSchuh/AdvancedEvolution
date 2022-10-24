package de.philipp.advancedevolution.entities.bosses;

import de.philipp.advancedevolution.util.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class BossArena {

    protected List<Player> players;

    public Location center;
    public double diameter;
    public int height;
    public int count;
    public double particleSpeed;

    public double speed = 0;
    public double grow = 0;
    public double shrink = 0;

    public final Predicate<Entity> filter = (e) -> e.getType() == EntityType.PLAYER;

    public BossArena(Location center, double diameter, int height, int count, double particleSpeed) {
        this.center = center;
        this.diameter = diameter;
        this.height = height;
        this.count = count;
        this.particleSpeed = particleSpeed;
        this.players = new ArrayList<>();

        for(Entity entity : center.getWorld().getNearbyEntities(center, diameter, diameter, diameter, filter)) {
            Player player = (Player) entity;
            if(player.getLocation().distance(center) > diameter) continue;
            players.add(player);
            enter(player);
        }
    }

    public void tick() {
        secondTick();
        // Grow and Shrink
        if(grow > 0) {
            if(grow - speed <= 0) {
                diameter += grow;
                grow = 0;
            } else {
                diameter += speed;
                grow -= speed;
            }
        } else if(shrink > 0) {
            if(shrink - speed < 0) {
                diameter -= shrink;
                shrink = 0;
            } else {
                diameter -= speed;
                shrink -= speed;
            }
        }
        //
        for(Entity entity : center.getWorld().getNearbyEntities(center, diameter, diameter, diameter, filter)) {
            Player player = (Player) entity;
            if(players.contains(player)) continue;
            if(player.getLocation().distance(center) > diameter) continue;
            if(player.isDead()) continue;
            players.add(player);
            enter(player);
        }
        for(Player player : center.getWorld().getPlayers()) {
            if(!players.contains(player)) continue;
            if(player.getLocation().distance(center) > diameter || player.isDead()) {
                players.remove(player);
                leave(player);
            }
        }
        if(!(arenaRing() == null)) {
            ParticleUtil.spawnParticleWall(center, arenaRing(), height, count, particleSpeed, diameter, 0.4);
        }
    }

    public abstract Particle arenaRing();
    public abstract void secondTick();
    public abstract void enter(Player player);
    public abstract void leave(Player player);

    public void grow(double amount, double speed) {
        this.speed = speed;
        this.grow += amount;
    }

    public void shrink(double amount, double speed) {
        this.speed = speed;
        this.shrink += amount;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

}