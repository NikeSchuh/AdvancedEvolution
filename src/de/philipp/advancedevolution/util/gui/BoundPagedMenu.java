package de.philipp.advancedevolution.util.gui;

import de.philipp.advancedevolution.AdvancedEvolution;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class BoundPagedMenu<E> implements Listener {

    protected List<E> elements;
    private final Plugin plugin;
    protected final Player player;
    private Inventory inv;
    protected int page;
    private BukkitRunnable task;

    public BoundPagedMenu(List<E> elements, final Plugin plugin, final Player player) {
        this.elements = elements;
        this.player = player;
        this.plugin = plugin;
        this.inv = Bukkit.createInventory(null, this.getRows() * 9, getTitle());

        openInv();
        register();


        if (this.isRefreshing()) {
            task = new BukkitRunnable() {

                @Override
                public void run() {

                    refreshInventory();

                }

            };

            task.runTaskTimerAsynchronously(plugin, 0, getUpdateDelay());
        }

    }

    public abstract String getTitle();

    public abstract int getRows();

    public abstract Integer getMaxObjectsPerPage();

    public abstract ItemStack getObjectGeneratedStack(Object o);

    public abstract long getUpdateDelay();

    public abstract void onMissingPage(int page);

    public abstract HashMap<Integer, ItemStack> getBaseItems();

    public abstract void onClick(Player player, E objClicked, InventoryClickEvent event);

    public void onClose(Player player, InventoryCloseEvent e) {

    }

    public boolean isRefreshing() {
        return true;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        final Player p = (Player) e.getWhoClicked();
        if (p.getUniqueId().equals(player.getUniqueId())) {
            if(e.getSlotType() == InventoryType.SlotType.OUTSIDE) {
                this.onClick(player, null, e);
                return;
            }
            final Integer s = e.getSlot();
            if(hasElement(s)) {
                this.onClick(player, getElement(s), e);
            } else this.onClick(player, null, e);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        final Player p = (Player) e.getPlayer();
        if (p.getUniqueId().equals(player.getUniqueId())) {
            this.onClose(player, e);
            this.remove();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        if (p.getUniqueId().equals(player.getUniqueId())) {
            this.remove();
        }
    }

    private void openInv() {
        this.page = 1;
        player.openInventory(inv);
        this.refreshInventory();
    }

    public void setPage(int page) {
        this.page = page;
        this.refreshInventory();
    }

    public boolean refreshInventory() {
        final int previousPage = page;
        int count = 0;
        if (page * getMaxObjectsPerPage() - getMaxObjectsPerPage() > elements.size()) {
            this.page = previousPage;
            return false;
        }
        inv.clear();
        for (Object x : elements) {
            if (count >= page * getMaxObjectsPerPage() - getMaxObjectsPerPage()) {
                if (!(count >= page * getMaxObjectsPerPage())) {
                    inv.setItem(count - (page * getMaxObjectsPerPage() - getMaxObjectsPerPage()),
                            this.getObjectGeneratedStack(x));
                }
            }
            count++;
        }

        for(int i : this.getBaseItems().keySet()) {
            inv.setItem(i, getBaseItems().get(i));
        }

        return true;
    }

    public E getElement(int slot) {
        final int startNumber = page * getMaxObjectsPerPage() - getMaxObjectsPerPage();
        final int indexNumber = startNumber + slot;
        if(elements.size() > indexNumber) {
            return elements.get(indexNumber);

        }
        return null;
    }

    public boolean hasElement(int slot) {
        return getElement(slot) != null;
    }
    public boolean hasPage(int page) {
        return elements.size() > (page - 1) * getMaxObjectsPerPage();
    }

    public BukkitRunnable getRefreshTask() {
        return task;
    }

    private void remove() {
        HandlerList.unregisterAll(this);
        this.elements = null;
        this.inv = null;
        this.page = 0;

        if(this.isRefreshing()) {
            task.cancel();
        }
    }

    private void register() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

}