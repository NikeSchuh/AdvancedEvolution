package de.philipp.advancedevolution.events.customevents;

import de.philipp.advancedevolution.items.DraconicItem;
import de.philipp.advancedevolution.items.DraconicItemBase;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public class DraconicItemInteractEvent extends Event implements Cancellable {

    public HandlerList handlerList = new HandlerList();

    private DraconicItem draconicItem;
    private DraconicItemBase draconicItemBase;
    private Player player;
    private Action action;
    private ItemStack item;
    private Block clicked;
    private BlockFace blockFace;
    private boolean cancelled = false;

    public DraconicItemInteractEvent(Player who, Action action, ItemStack item, DraconicItem draconicItem, Block clickedBlock, BlockFace clickedFace, Player player, Action action1, ItemStack item1, Block clicked, BlockFace blockFace) {
        this.draconicItem = draconicItem;
        this.draconicItemBase = draconicItem.getItemBase();
        this.player = who;
        this.action = action;
        this.item = item;
        this.clicked = clicked;
        this.blockFace = blockFace;
    }

    public DraconicItem getDraconicItem() {
        return draconicItem;
    }

    public DraconicItemBase getItemBase() {
        return draconicItemBase;
    }

    public DraconicItemBase getDraconicItemBase() {
        return draconicItemBase;
    }

    public Player getPlayer() {
        return player;
    }

    public Action getAction() {
        return action;
    }

    public ItemStack getItem() {
        return item;
    }

    public Block getClicked() {
        return clicked;
    }

    public BlockFace getBlockFace() {
        return blockFace;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }


    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
