package de.philipp.advancedevolution.util.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.philipp.advancedevolution.AdvancedEvolution;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class PagedMenu<E> implements Listener {

    public static List<PagedMenu<?>> guis = new ArrayList<PagedMenu<?>>();

    private List<E> list;
    private int maxObjectsPerPage;
    private int updateDelay;
    public HashMap<Player, Integer> currentPage = new HashMap<Player, Integer>();
    public HashMap<Player, String> currentSearch = new HashMap<Player, String>();
    public HashMap<Player, Boolean> search = new HashMap<Player, Boolean>();
    protected Plugin main;

    public PagedMenu(final Plugin main, List<E> list, final int maxObjectsPerPage, final int updateDelay) {
        this.list = list;
        this.updateDelay = updateDelay;
        this.setMaxObjectsPerPage(maxObjectsPerPage);
        this.main = main;

        register();
    }

    public abstract String getTitle();

    public abstract ItemStack getForwardButton();

    public abstract ItemStack getBackwardButton();

    public abstract ItemStack getGeneralBackButton();

    public abstract ItemStack getSearchButton();

    public abstract void onClick(InventoryClickEvent event);

    public abstract int getRows();

    public void onOpen() {}

    public Integer getCurrentPage(Player player) {
        return currentPage.getOrDefault(player, 1);
    }

    @EventHandler
    public void callClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (getTitle().equals(event.getView().getTitle())) {
            onClick(event);
            if (event.getCurrentItem() == null)
                return;
            if (event.getCurrentItem().equals(getForwardButton())) {

                if (isSearching(player)) {
                    if (doesPageExist(getCurrentPage(player) + 1, getCurrentSearchQuery(player))) {
                        open(player, getCurrentPage(player) + 1, getCurrentSearchQuery(player));
                    }
                } else {
                    if (doesPageExist(getCurrentPage(player) + 1)) {
                        open(player, getCurrentPage(player) + 1);
                    }
                }

            } else if (event.getCurrentItem().equals(getBackwardButton())) {
                if (isSearching(player)) {
                    if (doesPageExist(getCurrentPage(player) - 1, getCurrentSearchQuery(player))) {
                        open(player, getCurrentPage(player) - 1, getCurrentSearchQuery(player));
                    }
                } else {
                    if (doesPageExist(getCurrentPage(player) - 1)) {
                        open(player, getCurrentPage(player) - 1);
                    }
                }
            } else if (event.getCurrentItem().equals(getSearchButton())) {

                if (isSearching(player)) {
                    open(player, 1);
                    search.put(player, false);
                }

            }
        }
    }

    public void deregister() {
        HandlerList.unregisterAll(this);
    }

    public boolean doesPageExist(int page) {
        int startNumber = page * getMaxObjectsPerPage() - getMaxObjectsPerPage();

        if (startNumber > getList().size()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean doesPageExist(int page, String query) {
        int startNumber = page * getMaxObjectsPerPage() - getMaxObjectsPerPage();

        if (startNumber > getList(query).size()) {
            return false;
        } else {
            return true;
        }
    }

    public Inventory generateSpecificInventory(int page, String searchQuery) {
        Inventory inv = Bukkit.createInventory(null, getRows() * 9, getTitle());

        int maxNumber = page * getMaxObjectsPerPage();
        int startNumber = page * getMaxObjectsPerPage() - getMaxObjectsPerPage();

        int count = -1;

        if (startNumber > getList().size()) {
            return Bukkit.createInventory(null, getRows() * 9, getTitle());
        }

        for (ItemStack x : getStacks()) {
            count++;

            if (count >= startNumber) {
                if (!(count >= maxNumber)) {
                    String name = x.getItemMeta().getDisplayName();
                    if (name.contains(searchQuery) || x.toString().contains(searchQuery)) {

                        inv.setItem(count - startNumber, x);
                    } else {
                        count--;
                    }

                }
            }

        }

        if (getSearchButton() != null) {
            inv.setItem(42, getSearchButton());
        }

        if (doesPageExist(page + 1)) {
            inv.setItem(44, getForwardButton());
        }

        if (page >= 2) {
            inv.setItem(36, getBackwardButton());
        }

        return inv;

    }

    public Inventory getBaseInventory() {
        return Bukkit.createInventory(null, getRows() * 9, getTitle());
    }

    public Inventory generateInventory(int page) {
        Inventory inv = getBaseInventory();

        int maxNumber = page * getMaxObjectsPerPage();
        int startNumber = page * getMaxObjectsPerPage() - getMaxObjectsPerPage();

        int count = -1;

        if (startNumber > getList().size()) {
            return Bukkit.createInventory(null, getRows() * 9, getTitle());
        }

        for (ItemStack x : getStacks()) {
            count++;

            if (count >= startNumber) {
                if (!(count >= maxNumber)) {

                    inv.setItem(count - startNumber, x);

                }
            }

        }

        if (getSearchButton() != null) {
            inv.setItem(42, getSearchButton());
        }

        if (doesPageExist(page + 1)) {
            inv.setItem(44, getForwardButton());
        }

        if (page >= 2) {
            inv.setItem(36, getBackwardButton());
        }

        inv.setItem(40, getGeneralBackButton());

        return inv;

    }

    @SuppressWarnings("hiding")
    public <E> Object getObject(int page, int slot) {
        int startNumber = page * getMaxObjectsPerPage() - getMaxObjectsPerPage();
        return list.get(startNumber + slot);
    }

    public void refreshInventory(Player player) {
        player.getOpenInventory().getTopInventory()
                .setContents(generateInventory(getCurrentPage(player)).getContents());
    }

    public void refreshList(List<E> list) {
        this.list = list;
    }

    public void open(Player player, int page) {

        onOpen();

        player.openInventory(generateInventory(page));
        currentPage.put(player, page);
        currentSearch.put(player, null);
        search.put(player, false);

        new BukkitRunnable() {
            public void run() {
                if (!(player.isOnline())) {
                    this.cancel();
                }

                if (isSearching(player)) {
                    this.cancel();
                    return;
                }

                if (!(player.getOpenInventory().getTitle().equals(getTitle()))) {
                    this.cancel();
                } else {
                    player.getOpenInventory().getTopInventory()
                            .setContents(generateInventory(getCurrentPage(player)).getContents());
                }

            }
        }.runTaskTimer(AdvancedEvolution.getInstance(), 0, updateDelay);

    }

    public void open(Player player, int page, String query) {

        player.openInventory(generateInventory(page));
        currentPage.put(player, page);
        currentSearch.put(player, query);
        search.put(player, true);

        new BukkitRunnable() {
            public void run() {
                if (!(player.isOnline())) {
                    this.cancel();
                }

                if (!(isSearching(player))) {
                    this.cancel();
                }

                if (!(player.getOpenInventory().getTitle().equals(getTitle()))) {
                    this.cancel();
                } else {
                    if (getCurrentSearchQuery(player) != null) {
                        player.getOpenInventory().getTopInventory().setContents(
                                generateSpecificInventory(getCurrentPage(player), getCurrentSearchQuery(player))
                                        .getContents());
                    } else {
                        this.cancel();
                    }
                }

            }
        }.runTaskTimer(AdvancedEvolution.getInstance(), 0, updateDelay);

    }

    public Boolean isSearching(Player player) {
        if (search.get(player) != null) {
            return search.get(player);
        } else {
            return false;
        }
    }

    public String getCurrentSearchQuery(Player player) {
        if (isSearching(player)) {
            return currentSearch.get(player);
        } else {
            return null;
        }
    }

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, getMain());
    }

    public int getMaxObjectsPerPage() {
        return maxObjectsPerPage;
    }

    public void setMaxObjectsPerPage(int maxObjectsPerPage) {
        this.maxObjectsPerPage = maxObjectsPerPage;
    }

    public List<E> getList() {
        return list;
    }

    public abstract ItemStack generateStack(Object obj);

    public List<ItemStack> getStacks() {
        List<ItemStack> stacks = new ArrayList<ItemStack>();
        for (Object obj : list) {
            stacks.add(generateStack(obj));
        }
        return stacks;
    }

    public List<ItemStack> getList(String query) {
        List<ItemStack> items = new ArrayList<ItemStack>();
        for (ItemStack x : getStacks()) {
            String name = x.getItemMeta().getDisplayName();
            if (name.contains(query) || x.toString().contains(query)) {
                items.add(x);
            }
        }
        return items;
    }

    public void setList(List<E> list) {
        this.list = list;
    }

    public Plugin getMain() {
        return main;
    }

}
