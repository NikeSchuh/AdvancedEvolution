package de.philipp.advancedevolution.util;

import org.bukkit.Location;
import org.bukkit.Particle;

import java.util.ArrayList;
import java.util.List;


public class ParticleUtil {


    public static void spawnParticle(Location loc, Particle particle, int count, double speed, double radius) {
        Location l = loc.clone();
        double r = radius;
        for(double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / (radius * 8)) {
            double x = r * Math.cos(theta);
            double z = r * Math.sin(theta);

            l.add(x, 0, z);
            loc.getWorld().spawnParticle(particle, l, count, 0, 0, 0, speed);
            l.subtract(x, 0, z);
        }
    }

    public static void spawnParticleWall(Location loc, Particle particle, int height, int count, double speed, double radius) {
        Location l = loc.clone();
        double r = radius;
        double startHeight = loc.getY();

        for(double y = 0; y < height; y++) {
            for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / (radius * 8)) {
                double x = r * Math.cos(theta);
                double z = r * Math.sin(theta);

                l.add(x, y, z);
                loc.getWorld().spawnParticle(particle, l, count, 0, 0, 0, speed);
                l.subtract(x, y, z);
            }
        }
    }

    public static void spawnParticleWall(Location loc, Particle particle, int height, int count, double speed, double radius, double displayChance) {
        Location l = loc.clone();
        double r = radius;
        double startHeight = loc.getY();

        for(double y = 0; y < height; y++) {
            for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / (radius * 8)) {
                double x = r * Math.cos(theta);
                double z = r * Math.sin(theta);
                if(Math.random() < displayChance) {
                    l.add(x, y, z);
                    loc.getWorld().spawnParticle(particle, l, count, 0, 0, 0, speed);
                    l.subtract(x, y, z);
                }
            }
        }
    }

    public static List<Location> getWall(Location loc, int height, double radius) {
        List<Location> locations = new ArrayList<>();
        Location l = loc.clone();
        double r = radius;
        double startHeight = loc.getY();

        for(double y = 0; y < height; y++) {
            for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / (radius * 8)) {
                double x = r * Math.cos(theta);
                double z = r * Math.sin(theta);
                    l.add(x, y, z);
                    locations.add(l.clone());
                    l.subtract(x, y, z);

            }
        }
        return locations;
    }




}
