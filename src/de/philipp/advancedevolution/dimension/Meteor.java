package de.philipp.advancedevolution.dimension;

import de.philipp.advancedevolution.util.Cuboid;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.Random;

/**
 * Created by Brandon on 28/08/2014.
 * (Modified)
 */

public class Meteor {

    private int spawnX;
    private int spawnY;
    private int spawnZ;
    private int tailX;
    private int tailY;
    private int tailZ;
    private int size;

    private void initialize(Random rand, int x, int y, int z) {
        spawnX = x;
        spawnY = y;
        spawnZ = z;
        double rotation = rand.nextInt();
        double xmod = Math.sin(rotation);
        double zmod = Math.cos(rotation);
        int distMod = 150 + rand.nextInt(50);
        tailX = x + (int) (xmod * distMod);
        tailY = y + 40 + rand.nextInt(40);
        tailZ = z + (int) (zmod * distMod);
        size = 2 + rand.nextInt(8);
    }


    
    public boolean generate(World world, Random random, Location pos) {
        initialize(random, pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
        Cuboid bb = new Cuboid(new Location(world, Math.min(spawnX, tailX), Math.min(spawnY, tailY), Math.min(spawnZ, tailZ)), new Location(world, Math.max(spawnX, tailX), Math.max(spawnY, tailY), Math.max(spawnZ, tailZ)));
        bb.expand(Cuboid.CuboidDirection.North,  size+ 5);
        for (int x = (int) bb.getLowerX(); x <= bb.getUpperX(); x++) {
            for (int y = (int) bb.getLowerY(); y <= bb.getUpperY(); y++) {
                for (int z = (int) bb.getLowerZ(); z <= bb.getUpperZ(); z++) {
                    int sm = ((x == bb.getLowerX() || x == bb.getUpperX()) ? 1 : 0) + ((y == bb.getLowerY() || y == bb.getUpperY()) ? 1 : 0) + ((z == bb.getLowerZ() || z == bb.getUpperZ()) ? 1 : 0);
                    if (sm > 1) {
                        pos.getBlock().setType(Material.GLOWSTONE);
                   }
                }
            }
        }

        generateCore(world, random, size);
        generateTrail(world, random);

        return true;
    }

    private void generateCore(World world, Random rand, int r) {
        for (int x = spawnX - r; x <= spawnX + r; x++) {
            for (int z = spawnZ - r; z <= spawnZ + r; z++) {
                for (int y = spawnY - r; y <= spawnY + r; y++) {
                    if ((int) (getDistance(x, y, z, spawnX, spawnY, spawnZ)) <= r) {
                        float genP = rand.nextFloat();
                        Location pos = new Location(world, x, y, z).getBlock().getLocation();
                        if (0.1F > genP) {
                            pos.getBlock().setType(Material.REDSTONE_ORE);
                        }
                        else if (0.4F > genP) {
                            pos.getBlock().setType(Material.OBSIDIAN);
                        }
                       else {
                            pos.getBlock().setType(Material.OBSIDIAN);
                       }
                    }
                }
            }
        }
    }

    private void generateTrail(World world, Random rand) {
        int xDiff = tailX - spawnX;
        int yDiff = tailY - spawnY;
        int zDiff = tailZ - spawnZ;

        for (int p = 0; p < 100; p += 2) {
            int cX = spawnX + (int) (((float) p / 100F) * xDiff);
            int cY = spawnY + (int) (((float) p / 100F) * yDiff);
            int cZ = spawnZ + (int) (((float) p / 100F) * zDiff);
            float pc = (float) p / 100F;

            int density = 500 - (int) (pc * 550);
            if (density < 20) density = 20;
            generateTrailSphere(world, cX, cY, cZ, (size + 3) - (int) (pc * (size - 2)), density, rand);

            density = 1000 - (int) (pc * 10000);
            generateTrailSphere(world, cX, cY, cZ, (size + 3) - (int) (pc * (size - 2)), density, rand);

        }
    }

    public void generateTrailSphere(World world, int xi, int yi, int zi, int r, int density, Random rand) {
       if (density <= 0) return;
        if (density > 10000) density = 10000;
        for (int x = xi - r; x <= xi + r; x++) {
            for (int z = zi - r; z <= zi + r; z++) {
                for (int y = yi - r; y <= yi + r; y++) {
                    Location pos = new Location(world, x, y, z).getBlock().getLocation();
                    if ((density >= rand.nextInt(10000))  && (int) (getDistance(x, y, z, xi, yi, zi)) == r) {
                        if(world.getBlockAt(pos).getType() == Material.AIR) {
                            if (0.9F >= rand.nextFloat()) {
                                pos.getBlock().setType(Material.END_STONE);
                            } else if (rand.nextBoolean()) {
                                pos.getBlock().setType(Material.OBSIDIAN);
                            } else {
                                pos.getBlock().setType(Material.REDSTONE_ORE);
                            }
                        }
                    }
                }
            }
        }
    }

    public static double getDistance(int x1, int y1, int z1, int x2, int y2, int z2) {
        int dx = x1 - x2;
        int dy = y1 - y2;
        int dz = z1 - z2;
        return Math.sqrt((dx * dx + dy * dy + dz * dz));
    }



}
