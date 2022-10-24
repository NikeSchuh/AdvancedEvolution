package de.philipp.advancedevolution.blocks;

import com.mysql.fabric.xmlrpc.base.Array;
import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.blocks.custombases.DraconicMachineBase;
import de.philipp.advancedevolution.items.DraconicItem;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTTileEntity;
import de.tr7zw.nbtapi.plugin.NBTAPI;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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
    public static HashMap<Block, BukkitTask> syncedTicker = new HashMap();
    public static HashMap<Block, BukkitTask> asyncTicker = new HashMap();

    private BukkitTask blockTicker;

    public BlockManager(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        blockTicker = new BukkitRunnable() {
            @Override
            public void run() {
               // AdvancedEvolution.send("§c[BlockManager] -> TaskManager registered " + blocks.size());
                for(Block block : (List<Block>) blocks.clone()) {
                    if(!block.getChunk().isLoaded()) {
                        removeBlock(block);
                        continue;
                    }
                    if(block.getType() == Material.AIR) {
                        removeBlock(block);
                     //   AdvancedEvolution.send("§c[BlockManager] -> Removed Invalid Block " + block.getType() + " §7at " + block.getX() + " "  +block.getY() + " "  +block.getZ());
                        continue;
                    } else if(!DraconicBlock.isDraconicBlock(block)) {
                        removeBlock(block);
                    //    AdvancedEvolution.send("§c[BlockManager] -> Removed Invalid Block " + block.getType() + " §7at " + block.getX() + " "  +block.getY() + " "  +block.getZ());
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

    @EventHandler
    public void hopperIn(InventoryMoveItemEvent event) {
        if(event.getDestination().getLocation() != null) {
            Location location = event.getDestination().getLocation();
            Block block = location.getBlock();
            if(DraconicBlock.isDraconicBlock(block)) {
                DraconicBlock draconicBlock =  new DraconicBlock(block);
                if(InventoryContainer.isContainer(draconicBlock)) {
                    ItemStack stack = event.getItem();
                    InventoryContainer inventoryContainer = new InventoryContainer(draconicBlock);
                    if(inventoryContainer.getInventory().firstEmpty() == -1) {
                        event.setCancelled(true);
                        return;
                    }
                    inventoryContainer.getInventory().addItem(stack);
                    ItemStack r = event.getSource().getItem(event.getSource().first(stack.getType()));
                    r.setAmount(r.getAmount() - 1);
                    event.getDestination().clear();
                } else event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void hopperOut(InventoryMoveItemEvent event) {
        if(!DraconicMachineBase.isFull(event.getDestination())) {
            Block block = event.getDestination().getLocation().getBlock();
            if(DraconicBlock.isDraconicBlock(block)) {
                DraconicBlock draconicBlock = new DraconicBlock(block);
                InventoryContainer inventoryContainer = new InventoryContainer(draconicBlock);
                if(inventoryContainer.hasInventory()) {
                    DraconicMachineBase.eject(event.getDestination().getLocation().getBlock(), inventoryContainer);
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
    public void onExplode(EntityExplodeEvent event) {
        List<Block> clone = new ArrayList<Block>(event.blockList());
        for(Block block : event.blockList()) {
            if(DraconicBlock.isDraconicBlock(block)) {
                clone.remove(block);
            }
        }
        event.blockList().clear();
        event.blockList().addAll(clone);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(!(player.getGameMode() == GameMode.CREATIVE)) {
            if(DraconicBlock.isDraconicBlock(event.getBlock())) {
                DraconicBlock draconicBlock = new DraconicBlock(event.getBlock());
                draconicBlock.getBlockBase().onBreak(draconicBlock, event, player);
                Location location = event.getBlock().getLocation();
                if(draconicBlock.getBlockBase() instanceof DraconicMachineBase) {
                    if(!event.isCancelled()) {
                        event.setDropItems(false);
                        location.getWorld().dropItemNaturally(location, DraconicMachine.ofMachine(new DraconicMachine(draconicBlock.getBlock())));
                        if(InventoryContainer.isContainer(draconicBlock)) {
                            InventoryContainer.accessCache.remove(draconicBlock.getBlock());
                        }
                        return;
                    }
                }

                if(InventoryContainer.isContainer(draconicBlock)) {
                    InventoryContainer.accessCache.remove(draconicBlock.getBlock());
                }

                NBTCompound storedData = draconicBlock.getStoredData();
                ItemStack stack = draconicBlock.getBlockBase().getBaseStack();
                NBTItem item = new NBTItem(stack);
                item.mergeCompound(storedData);
                event.setDropItems(false);
                location.getWorld().dropItemNaturally(location, item.getItem());
            }
            return;
        } else {
            if(DraconicBlock.isDraconicBlock(event.getBlock())) {
                DraconicBlock draconicBlock = new DraconicBlock(event.getBlock());
                draconicBlock.getBlockBase().onBreak(draconicBlock, event, player);
                Location location = event.getBlock().getLocation();

                if(InventoryContainer.isContainer(draconicBlock)) {
                    InventoryContainer container = new InventoryContainer(draconicBlock);
                    container.getInventory().clear();
                    container.syncInventoryWithContainer();
                    InventoryContainer.accessCache.remove(draconicBlock.getBlock());
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getClickedBlock() == null) return;
        if(event.getClickedBlock().getType() == XMaterial.AIR.parseMaterial()) return;
        if(!(event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if(DraconicBlock.isDraconicBlock(event.getClickedBlock())) {
            DraconicBlock draconicBlock = new DraconicBlock(event.getClickedBlock());
            draconicBlock.getBlockBase().onBlockInteract(player, event, draconicBlock);
        }
    }


    @EventHandler
    public void chunkLoad(ChunkLoadEvent event) {
        if(event.isNewChunk()) return;
        Chunk chunk = event.getChunk();
        for(BlockState s : chunk.getTileEntities()) {
            double lastDistance = Double.MAX_VALUE;
            for (Player p : s.getWorld().getPlayers()) {
                double distance = s.getLocation().distance(p.getLocation());
                if (distance < lastDistance) {
                    lastDistance = distance;
                }
            }
            if(lastDistance > (Bukkit.getServer().getViewDistance() * 16)) {
                AdvancedEvolution.send("Prevented illegal block load at " + s.getLocation());
                continue;
            }
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

    public void onDisable() {
        for(Block block : (List<Block>) blocks.clone()) {
            DraconicBlock draconicBlock = new DraconicBlock(block);
            if(draconicBlock.getBlockBase() instanceof DraconicMachineBase) {
                ((DraconicMachineBase) draconicBlock.getBlockBase()).onUnload(draconicBlock);
            }
        }
    }

    public void addBlock(Block block) {
        if (!blocks.contains(block)) {
            if(DraconicBlock.isDraconicBlock(block)) {
                DraconicBlock draconicBlock = new DraconicBlock(block);
                AdvancedEvolution.send("[BlockManager] Adding Machine " + draconicBlock.getBlockBase().getDataName());
                if (draconicBlock.getBlockBase() instanceof DraconicMachineBase) {
                    blocks.add(block);
                    DraconicMachineBase draconicMachineBase = (DraconicMachineBase) draconicBlock.getBlockBase();
                    final DraconicMachine draconicMachine = new DraconicMachine(block);
                    syncedTicker.put(block, new BukkitRunnable() {
                        @Override
                        public void run() {
                            try {
                                draconicMachineBase.tickSync(block, draconicMachine);
                            }catch (Exception e) {
                                AdvancedEvolution.send("§cError while ticking (Synced) block" + block);
                                AdvancedEvolution.send("§c" +e.getCause());
                                e.printStackTrace();
                                removeBlock(block);
                            }
                        }
                    }.runTaskTimer(AdvancedEvolution.getInstance(), 0, draconicMachineBase.getSyncInterval()));
                    asyncTicker.put(block, new BukkitRunnable() {
                        @Override
                        public void run() {
                            try{
                                draconicMachineBase.tickAsync(block, draconicMachine);
                            }catch (Exception e) {
                                AdvancedEvolution.send("§cError while ticking (Async) block" + block);
                                AdvancedEvolution.send("§c" +e.getCause());
                                e.printStackTrace();
                                removeBlock(block);
                            }
                        }
                    }.runTaskTimerAsynchronously(AdvancedEvolution.getInstance(), 0, draconicMachineBase.getAsyncInterval()));
                }
            }
        }
    }

    public void removeBlock(Block block) {
            if(blocks.contains(block)) {
                blocks.remove(block);
                    if(syncedTicker.containsKey(block) && asyncTicker.containsKey(block)) {
                        BukkitTask sync = syncedTicker.get(block);
                        BukkitTask async = asyncTicker.get(block);

                        sync.cancel();
                        async.cancel();

                        syncedTicker.remove(block);
                        asyncTicker.remove(block);
                    }
            }
    }

}
