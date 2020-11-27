package de.philipp.advancedevolution.blocks;

import com.mysql.fabric.xmlrpc.base.Array;
import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.blocks.custombases.DraconicMachineBase;
import de.philipp.advancedevolution.items.DraconicItem;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockManager implements Listener {

    public static ArrayList<Block> blocks = new ArrayList<>();
    private BukkitTask blockTicker;

    public BlockManager(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        blockTicker = new BukkitRunnable() {
            @Override
            public void run() {
                for(Block block : (List<Block>) blocks.clone()) {
                    if(block.getType() == Material.AIR) {
                        removeBlock(block);
                        continue;
                    }
                    if(DraconicBlock.isDraconicBlock(block)) {
                        DraconicBlock draconicBlock = new DraconicBlock(block);
                        if(draconicBlock.isTickable()) {
                            try {
                                Method method = draconicBlock.getBlockBase().getClass().getMethod("tick", Block.class);
                                try {
                                    method.invoke(draconicBlock.getBlockBase(), block);
                                } catch (IllegalAccessException e) {
                                    AdvancedEvolution.send("BlockManager Error: " + e);
                                    e.printStackTrace();
                                    removeBlock(block);
                                } catch (InvocationTargetException e) {
                                    AdvancedEvolution.send("BlockManager Error: " + e);
                                    e.printStackTrace();
                                    removeBlock(block);
                                }
                            } catch (NoSuchMethodException e) {
                                removeBlock(block);
                            }
                        } else {
                            removeBlock(block);
                        }
                    } else {
                        removeBlock(block);
                    }
                }


            }
        }.runTaskTimer(plugin, 0, 1);

        for(World w : Bukkit.getWorlds()) {
            for(Chunk chunk : w.getLoadedChunks()) {
                for(BlockState s : chunk.getTileEntities()) {
                    addBlock(s.getBlock());
                }
            }
        }

    }

    public BukkitTask getBlockTicker() {
        return blockTicker;
    }

    public static List<DraconicBlock> getBlocks() {
        return (ArrayList<DraconicBlock>) blocks.clone();
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if(DraconicItem.isDraconicItem(event.getItemInHand())) {
            DraconicItem item = new DraconicItem(event.getItemInHand());
            if(DraconicBlock.isBlockBase(item.getItemBase())) {
                DraconicBlockBase blockBase = (DraconicBlockBase) item.getItemBase();
                blockBase.onPlace(event.getBlockPlaced(), item.getStoredData(), event, player);
                if(!event.isCancelled()) {
                    addBlock(event.getBlockPlaced());
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(DraconicBlock.isDraconicBlock(event.getBlock())) {
            DraconicBlock draconicBlock = new DraconicBlock(event.getBlock());
            player.sendMessage(draconicBlock.getBlockBase() + "");
            draconicBlock.getBlockBase().onBreak(draconicBlock, event, player);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getClickedBlock() == null) return;
        if(event.getClickedBlock().getType() == XMaterial.AIR.parseMaterial()) return;
        if(!(event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if(DraconicBlock.isDraconicBlock(event.getClickedBlock())) {
            DraconicBlock draconicBlock = new DraconicBlock(event.getClickedBlock());
            player.sendMessage(draconicBlock.getBlockBase() + "");
            draconicBlock.getBlockBase().onBlockInteract(player, event, draconicBlock);
        }
    }

    @EventHandler
    public void chunkLoad(ChunkLoadEvent event) {
        if(event.isNewChunk()) return;
        Chunk chunk = event.getChunk();
        for(BlockState s : chunk.getTileEntities()) {
            addBlock(s.getBlock());
        }
    }

    @EventHandler
    public void chunkUnload(ChunkUnloadEvent event) {
        Chunk chunk = event.getChunk();
        for(BlockState s : chunk.getTileEntities()) {
            removeBlock(s.getBlock());
        }
    }

    public void addBlock(Block block) { if (!blocks.contains(block)) { blocks.add(block); } }

    public void removeBlock(Block block) {
            if(blocks.contains(block)) {
                blocks.remove(block);
            }
    }

}
