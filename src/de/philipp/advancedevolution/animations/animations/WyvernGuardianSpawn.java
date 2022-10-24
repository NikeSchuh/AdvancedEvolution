package de.philipp.advancedevolution.animations.animations;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.animations.Delay;
import de.philipp.advancedevolution.entities.bosses.wyvernguardian.WyvernGuardian;
import de.philipp.advancedevolution.entities.particles.ChasingParticleEntity;
import de.philipp.advancedevolution.entities.particles.Hitbox;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.lib.xseries.XSound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;

public class WyvernGuardianSpawn {

    private Location location;

    public WyvernGuardianSpawn(Location location) {
        this.location = location.clone();
        run();
    }

    public void run() {
        location.getWorld().strikeLightningEffect(location);
        FallingBlock dragonEgg = location.getWorld().spawnFallingBlock(location, Material.DRAGON_EGG.createBlockData());
        dragonEgg.getLocation().getWorld().spawnParticle(Particle.END_ROD, dragonEgg.getLocation(), 10, 0, 0, 0, 0.002);
        dragonEgg.setGravity(false);
        dragonEgg.setInvulnerable(true);
        dragonEgg.setGlowing(true);
        dragonEgg.setHurtEntities(false);
        dragonEgg.setVelocity(new Vector(0, 0.2, 0));
        dragonEgg.setDropItem(false);
        new Delay(50) {

            @Override
            public void end() {
                dragonEgg.getLocation().getWorld().spawnParticle(Particle.END_ROD, dragonEgg.getLocation(), 10, 0, 0, 0, 0.05);
                dragonEgg.getWorld().playSound(dragonEgg.getLocation(), XSound.ENTITY_EVOKER_PREPARE_SUMMON.parseSound(), 100f, 0.5f);

                for(int i = 0; i < 4; i++) {
                    FallingBlock sideBlock = location.getWorld().spawnFallingBlock(dragonEgg.getLocation(), Material.END_ROD.createBlockData());
                    sideBlock.setGravity(false);
                    sideBlock.setInvulnerable(true);
                    sideBlock.setGlowing(true);
                    sideBlock.setHurtEntities(false);

                    switch (i) {
                        case 0:
                            sideBlock.setVelocity(new Vector(0.3, -0.1, 0));
                            break;
                        case 1:
                            sideBlock.setVelocity(new Vector(-0.3, -0.1, 0));
                            break;
                        case 2:
                            sideBlock.setVelocity(new Vector(0, -0.1, 0.3));
                            break;
                        case 3:
                            sideBlock.setVelocity(new Vector(0, -0.1, -0.3));
                            break;
                    }
                    sideBlock.setDropItem(false);

                    new Delay(60) {
                        @Override
                        public void end() {
                            int current = 28;
                            spawn(sideBlock, dragonEgg, current);

                            new Delay(420) {

                                @Override
                                public void end() {
                                    dragonEgg.remove();
                                    sideBlock.remove();
                                }
                            };
                        }
                    };
                }

                new Delay(500) {
                    @Override
                    public void end() {
                        new WyvernGuardian(dragonEgg.getLocation());
                    }
                };
            }
        };
    }


    public void spawn(FallingBlock block, Entity dragonEgg, int current) {
        if(current == 0) {
            return;
        }
        dragonEgg.getWorld().playSound(dragonEgg.getLocation(), XSound.ENTITY_ELDER_GUARDIAN_AMBIENT.parseSound(), 100f, 2f / current);
        dragonEgg.setTicksLived(1);
        block.setTicksLived(1);
        block.getWorld().spawnParticle(Particle.CRIMSON_SPORE, block.getLocation(), 5, 0, 0, 0, 0.01);
        int now = current;
        new Delay(now - 1) {

            @Override
            public void end() {
                new ChasingParticleEntity(AdvancedEvolution.getInstance(), block.getLocation().add(0, 1, 0), dragonEgg, new Vector((Math.random() * 10) + 15, (Math.random() * 10) + 15, (Math.random() * 10) + 15), new Hitbox(1, 1, 1)) {

                    @Override
                    public void onCollision(Block block) {
                        if(block.getType() != XMaterial.BEDROCK.parseMaterial()) {
                            block.setType(XMaterial.AIR.parseMaterial());
                        }
                    }

                    @Override
                    public void onHit(Entity target) {
                        remove();
                    }

                    @Override
                    public void spawnParticle(Location location) {
                        location.getWorld().spawnParticle(Particle.FLAME, location, 1, 0, 0, 0, 0);
                    }
                };
                spawn(block, dragonEgg,now - 1);
            }
        };
    }
}
