package de.philipp.advancedevolution.animations.animations;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.lib.xseries.XSound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.Random;

public class MightyChickenSpawn {

    private Location loc;

    public MightyChickenSpawn(Location location) {
        this.loc = location;
        run();
    }

    public void run() {
        for(Player player : loc.getWorld().getPlayers()) {
            player.sendMessage("§cThe world will end soon ...");
            player.playSound(player.getLocation(), XSound.ENTITY_ENDER_DRAGON_GROWL.parseSound(), 10f, 0.1f);
            player.playSound(player.getLocation(), XSound.ENTITY_CHICKEN_AMBIENT.parseSound(), 10f, 0.1f);
        }
        Random rand = new Random();
        Bukkit.getScheduler().scheduleSyncDelayedTask(AdvancedEvolution.getInstance(), new Runnable() {
            public void run() {

                Bukkit.getScheduler().scheduleSyncDelayedTask(AdvancedEvolution.getInstance(), new Runnable() {
                    public void run() {

                    }
                }, 20);

                FallingBlock BLK = loc.getWorld().spawnFallingBlock(loc, XMaterial.REDSTONE_TORCH.parseMaterial().createBlockData());
                BLK.setGravity(false);
                BLK.setInvulnerable(true);
                BLK.setGlowing(true);
                BLK.setHurtEntities(false);
                BLK.setVelocity(new Vector(0, 0.1, 0));
                BLK.setDropItem(false);

                loc.getWorld().strikeLightning(BLK.getLocation());

                Bukkit.getScheduler().scheduleSyncDelayedTask(AdvancedEvolution.getInstance(), new Runnable() {
                    public void run() {
                        loc.getWorld().strikeLightning(BLK.getLocation());
                        for (Entity en : loc.getWorld().getNearbyEntities(loc, 50, 50, 50)) {
                            if (en instanceof Player) {
                                Player player = (Player) en;
                                player.playSound(player.getLocation(), XSound.ENTITY_ENDER_DRAGON_GROWL.parseSound(), 100,
                                        (float) 0.1);
                            }
                        }
                        Bukkit.getScheduler().scheduleSyncDelayedTask(AdvancedEvolution.getInstance(), new Runnable() {
                            public void run() {
                                for (Entity en : loc.getWorld().getNearbyEntities(loc, 50, 50, 50)) {
                                    if (en instanceof Player) {
                                        Player player = (Player) en;
                                        player.playSound(player.getLocation(), XSound.ENTITY_ENDER_DRAGON_GROWL.parseSound(), 100,
                                                (float) 0.1);
                                    }
                                }
                                loc.getWorld().strikeLightning(BLK.getLocation());
                                Bukkit.getScheduler().scheduleSyncDelayedTask(AdvancedEvolution.getInstance(), new Runnable() {
                                    public void run() {
                                        loc.getWorld().strikeLightning(BLK.getLocation());
                                        Bukkit.getScheduler().scheduleSyncDelayedTask(AdvancedEvolution.getInstance(), new Runnable() {
                                            @SuppressWarnings("deprecation")
                                            public void run() {
                                                for (Entity en : loc.getWorld().getNearbyEntities(loc, 50, 50, 50)) {
                                                    if (en instanceof Player) {
                                                        Player player = (Player) en;
                                                        player.playSound(player.getLocation(), XSound.ENTITY_ENDER_DRAGON_GROWL.parseSound(), 100,
                                                                (float) 0.1);
                                                    }
                                                }

                                                loc.getWorld().strikeLightning(BLK.getLocation());

                                                FallingBlock BL1 = loc.getWorld().spawnFallingBlock(BLK.getLocation(), XMaterial.DRAGON_EGG.parseMaterial().createBlockData());
                                                BL1.setGravity(false);
                                                BL1.setInvulnerable(true);
                                                BL1.setGlowing(true);
                                                BL1.setHurtEntities(false);
                                                BL1.setVelocity(new Vector(0, 0.2, 0));
                                                BL1.setDropItem(false);

                                                Bukkit.getScheduler().scheduleSyncDelayedTask(AdvancedEvolution.getInstance(), new Runnable() {
                                                    public void run() {
                                                        loc.getWorld().strikeLightning(BL1.getLocation());

                                                        FallingBlock BL2 = loc.getWorld().spawnFallingBlock(BL1.getLocation(), XMaterial.MAGMA_BLOCK.parseMaterial().createBlockData());
                                                        FallingBlock BL3=  loc.getWorld().spawnFallingBlock(BL1.getLocation(), XMaterial.MAGMA_BLOCK.parseMaterial().createBlockData());
                                                        FallingBlock BL4 = loc.getWorld().spawnFallingBlock(BL1.getLocation(), XMaterial.MAGMA_BLOCK.parseMaterial().createBlockData());
                                                        FallingBlock BL5 = loc.getWorld().spawnFallingBlock(BL1.getLocation(), XMaterial.MAGMA_BLOCK.parseMaterial().createBlockData());


                                                        BL2.setGravity(false);
                                                        BL2.setInvulnerable(true);
                                                        BL2.setGlowing(true);
                                                        BL2.setHurtEntities(false);
                                                        BL2.setVelocity(new Vector(0.3, -0.1, 0));
                                                        BL2.setDropItem(false);

                                                        BL3.setGravity(false);
                                                        BL3.setInvulnerable(true);
                                                        BL3.setGlowing(true);
                                                        BL3.setHurtEntities(false);
                                                        BL3.setVelocity(new Vector(-0.3, -0.1, 0));
                                                        BL3.setDropItem(false);

                                                        BL4.setGravity(false);
                                                        BL4.setInvulnerable(true);
                                                        BL4.setGlowing(true);
                                                        BL4.setHurtEntities(false);
                                                        BL4.setVelocity(new Vector(0, -0.1, 0.3));
                                                        BL4.setDropItem(false);

                                                        BL5.setGravity(false);
                                                        BL5.setInvulnerable(true);
                                                        BL5.setGlowing(true);
                                                        BL5.setHurtEntities(false);
                                                        BL5.setVelocity(new Vector(0, -0.1, -0.3));
                                                        BL5.setDropItem(false);

                                                        Bukkit.getScheduler().scheduleSyncDelayedTask(AdvancedEvolution.getInstance(), new Runnable() {
                                                            public void run() {
                                                                loc.getWorld().strikeLightning(BL2.getLocation());


                                                                EnderCrystal enderCrystal1 = (EnderCrystal) BL2.getLocation().getWorld().spawnEntity(BL2.getLocation(), EntityType.ENDER_CRYSTAL);
                                                                enderCrystal1.setBeamTarget(BL1.getLocation().add(0, -1.5, 0));
                                                                enderCrystal1.setShowingBottom(false);
                                                                enderCrystal1.teleport(BL2);
                                                                enderCrystal1.setInvulnerable(true);

                                                                Bukkit.getScheduler().scheduleSyncDelayedTask(AdvancedEvolution.getInstance(), new Runnable() {
                                                                    public void run() {
                                                                        loc.getWorld().strikeLightning(BL3.getLocation());


                                                                        EnderCrystal enderCrystal2 = (EnderCrystal) BL3.getLocation().getWorld().spawnEntity(BL3.getLocation(), EntityType.ENDER_CRYSTAL);
                                                                        enderCrystal2.setBeamTarget(BL1.getLocation().add(0, -1.5, 0));
                                                                        enderCrystal2.setShowingBottom(false);
                                                                        enderCrystal2.teleport(BL3);
                                                                        enderCrystal2.setInvulnerable(true);

                                                                        Bukkit.getScheduler().scheduleSyncDelayedTask(AdvancedEvolution.getInstance(), new Runnable() {
                                                                            public void run() {
                                                                                loc.getWorld().strikeLightning(BL4.getLocation());


                                                                                EnderCrystal enderCrystal3 = (EnderCrystal) BL3.getLocation().getWorld().spawnEntity(BL4.getLocation(), EntityType.ENDER_CRYSTAL);
                                                                                enderCrystal3.setBeamTarget(BL1.getLocation().add(0, -1.5, 0));
                                                                                enderCrystal3.setShowingBottom(false);
                                                                                enderCrystal3.teleport(BL4);
                                                                                enderCrystal3.setInvulnerable(true);

                                                                                Bukkit.getScheduler().scheduleSyncDelayedTask(AdvancedEvolution.getInstance(), new Runnable() {
                                                                                    public void run() {
                                                                                        loc.getWorld().strikeLightning(BL5.getLocation());


                                                                                        EnderCrystal enderCrystal4 = (EnderCrystal) BL5.getLocation().getWorld().spawnEntity(BL5.getLocation(), EntityType.ENDER_CRYSTAL);
                                                                                        enderCrystal4.setBeamTarget(BL1.getLocation().add(0, -1.5, 0));
                                                                                        enderCrystal4.setShowingBottom(false);
                                                                                        enderCrystal4.teleport(BL5);
                                                                                        enderCrystal4.setInvulnerable(true);

                                                                                        Bukkit.getScheduler().scheduleSyncDelayedTask(AdvancedEvolution.getInstance(), new Runnable() {
                                                                                            public void run() {

                                                                                                for (Entity en : loc.getWorld().getNearbyEntities(loc, 50, 50, 50)) {
                                                                                                    if (en instanceof Player) {
                                                                                                        Player player = (Player) en;
                                                                                                        player.playSound(player.getLocation(), XSound.ENTITY_ENDER_DRAGON_GROWL.parseSound(), 100,
                                                                                                                (float) 0.1);
                                                                                                    }
                                                                                                }

                                                                                                Bukkit.getScheduler().scheduleSyncDelayedTask(AdvancedEvolution.getInstance(), new Runnable() {
                                                                                                    public void run() {

                                                                                                        for (Entity en : loc.getWorld().getNearbyEntities(loc, 50, 50, 50)) {
                                                                                                            if (en instanceof Player) {
                                                                                                                Player player = (Player) en;
                                                                                                                player.playSound(player.getLocation(), XSound.ENTITY_ENDER_DRAGON_GROWL.parseSound(), 100,
                                                                                                                        (float) 0.1);
                                                                                                            }
                                                                                                        }

                                                                                                        Bukkit.getScheduler().scheduleSyncDelayedTask(AdvancedEvolution.getInstance(), new Runnable() {
                                                                                                            public void run() {

                                                                                                                for (Entity en : loc.getWorld().getNearbyEntities(loc, 50, 50, 50)) {
                                                                                                                    if (en instanceof Player) {
                                                                                                                        Player player = (Player) en;
                                                                                                                        player.playSound(player.getLocation(), XSound.ENTITY_ENDER_DRAGON_GROWL.parseSound(), 100,
                                                                                                                                (float) 0.1);
                                                                                                                    }
                                                                                                                }

                                                                                                                Bukkit.getScheduler().scheduleSyncDelayedTask(AdvancedEvolution.getInstance(), new Runnable() {
                                                                                                                    public void run() {

                                                                                                                        for (Entity en : loc.getWorld().getNearbyEntities(loc, 50, 50, 50)) {
                                                                                                                            if (en instanceof Player) {
                                                                                                                                Player player = (Player) en;
                                                                                                                                player.playSound(player.getLocation(), XSound.ENTITY_ENDER_DRAGON_GROWL.parseSound(), 100,
                                                                                                                                        (float) 0.1);
                                                                                                                            }
                                                                                                                        }

                                                                                                                        Bukkit.getScheduler().scheduleSyncDelayedTask(AdvancedEvolution.getInstance(), new Runnable() {
                                                                                                                            public void run() {

                                                                                                                                for (Player player : Bukkit.getOnlinePlayers()) {
                                                                                                                                    player.playSound(player.getLocation(), XSound.ENTITY_WITHER_SPAWN.parseSound(), 100, (float) 0.1);
                                                                                                                                }

                                                                                                                                loc.getWorld().strikeLightning(BL1.getLocation());
                                                                                                                                loc.getWorld().strikeLightning(BL2.getLocation());
                                                                                                                                loc.getWorld().strikeLightning(BL3.getLocation());
                                                                                                                                loc.getWorld().strikeLightning(BL4.getLocation());
                                                                                                                                loc.getWorld().strikeLightning(BL5.getLocation());

                                                                                                                                loc.getWorld().createExplosion(BL1.getLocation(), 4);
                                                                                                                                loc.getWorld().createExplosion(BL2.getLocation(), 4);
                                                                                                                                loc.getWorld().createExplosion(BL3.getLocation(), 4);
                                                                                                                                loc.getWorld().createExplosion(BL4.getLocation(), 4);
                                                                                                                                loc.getWorld().createExplosion(BL5.getLocation(), 4);

                                                                                                                                BL1.remove();
                                                                                                                                BL2.remove();
                                                                                                                                BL3.remove();
                                                                                                                                BL4.remove();
                                                                                                                                BL5.remove();

                                                                                                                                enderCrystal1.remove();
                                                                                                                                enderCrystal2.remove();
                                                                                                                                enderCrystal3.remove();
                                                                                                                                enderCrystal4.remove();

                                                                                                                                loc.getWorld().spawnEntity(loc, EntityType.CHICKEN);
                                                                                                                            }
                                                                                                                        }, 20);

                                                                                                                    }
                                                                                                                }, 10);

                                                                                                            }
                                                                                                        }, 10);

                                                                                                    }
                                                                                                }, 10);

                                                                                            }
                                                                                        }, 10);


                                                                                    }
                                                                                }, 40);

                                                                            }
                                                                        }, 40);

                                                                    }
                                                                }, 40);

                                                            }
                                                        }, 160);



                                                    }
                                                }, 80);


                                            }
                                        }, 100L);

                                    }
                                }, 10L);
                            }
                        }, 10L);
                    }
                }, 10L);

            }
        }, 20);

    }

}
