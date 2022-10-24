package de.philipp.advancedevolution.util;

import de.philipp.advancedevolution.lib.xseries.XMaterial;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

import java.util.Random;

public class LocationUtil {

    private static final Random rand = new Random();

    public static Location faceLocation(Entity entity, Location to) {
        if (entity.getWorld() != to.getWorld()) {
            return null;
        }
        Location fromLocation = entity.getLocation();

        double xDiff = to.getX() - fromLocation.getX();
        double yDiff = to.getY() - fromLocation.getY();
        double zDiff = to.getZ() - fromLocation.getZ();

        double distanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        double distanceY = Math.sqrt(distanceXZ * distanceXZ + yDiff * yDiff);

        double yaw = Math.toDegrees(Math.acos(xDiff / distanceXZ));
        double pitch = Math.toDegrees(Math.acos(yDiff / distanceY)) - 90.0D;
        if (zDiff < 0.0D) {
            yaw += Math.abs(180.0D - yaw) * 2.0D;
        }
        Location loc = entity.getLocation();
        loc.setYaw((float) (yaw - 90));
        loc.setPitch((float) (pitch));
        entity.teleport(loc);
        return loc;
    }

    public static Location faceLocationGod(Entity entity, Location to) {
        if (entity.getWorld() != to.getWorld()) {
            return null;
        }
        Location fromLocation = entity.getLocation();

        double xDiff = to.getX() - fromLocation.getX();
        double yDiff = to.getY() - fromLocation.getY();
        double zDiff = to.getZ() - fromLocation.getZ();

        double distanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        double distanceY = Math.sqrt(distanceXZ * distanceXZ + yDiff * yDiff);

        double yaw = Math.toDegrees(Math.acos(xDiff / distanceXZ));
        double pitch = Math.toDegrees(Math.acos(yDiff / distanceY)) - 90.0D;
        if (zDiff < 0.0D) {
            yaw += Math.abs(180.0D - yaw) * 2.0D;
        }
        Location loc = entity.getLocation();
        loc.setYaw((float) (yaw - 90));
        loc.setPitch((float) (pitch));
        entity.teleport(loc);
        return loc;
    }

    public static int getDistance(Entity e){
        Location loc = e.getLocation().clone();
        double y = loc.getBlockY();
        int distance = 0;
        for (double i = y; i >= 0; i--){
            loc.setY(i);
            if(loc.getBlock().getType() != Material.AIR)break;
            distance++;
        }
        return distance;
    }

    public static int getDistanceToGround(Location location){
        Location loc = location.clone();
        double y = loc.getBlockY();
        int distance = 0;
        for (double i = y; i >= 0; i--){
            loc.setY(i);
            if(loc.getBlock().getType() != Material.AIR)break;
            distance++;
        }
        return distance;
    }

    public static Location randomChunkBlockLocation(Chunk chunk, int maxHeight, int minHeight) {
        return chunk.getBlock(rand.nextInt(16), rand.nextInt(maxHeight - minHeight) + minHeight, rand.nextInt(16)).getLocation();
    }

}
