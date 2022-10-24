package de.philipp.advancedevolution.advancements;

import de.philipp.advancedevolution.lib.xseries.XMaterial;
import eu.endercentral.crazy_advancements.*;
import eu.endercentral.crazy_advancements.advancement.Advancement;
import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay;
import eu.endercentral.crazy_advancements.advancement.AdvancementVisibility;
import eu.endercentral.crazy_advancements.advancement.criteria.Criteria;
import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;


public enum DraconicAdvancements {

    ROOT(XMaterial.TOTEM_OF_UNDYING, 1, "{\"text\":\"Draconic Evolution\",\"color\":\"red\"}", "{\"text\":\"Advancements for the plugin AdvancedEvolution\"}", "textures/block/polished_blackstone_bricks.png", false, false, AdvancementVisibility.ALWAYS, AdvancementDisplay.AdvancementFrame.TASK),
    RECIPE_BOOK(ROOT, XMaterial.KNOWLEDGE_BOOK, 1, "{\"text\":\"So many recipes\"}", "{\"text\":\"Craft a Recipe Book\"}", true, true, AdvancementVisibility.ALWAYS, 1, 0, AdvancementDisplay.AdvancementFrame.TASK),
    DRACONIC_GENERATOR(RECIPE_BOOK, XMaterial.FURNACE, 1, "{\"text\":\"Coal Power\"}", "{\"text\":\"Craft a Draconic Generator\"}", true, true, AdvancementVisibility.ALWAYS, 2, 0, AdvancementDisplay.AdvancementFrame.CHALLENGE),
    ENERGY_BINDER(DRACONIC_GENERATOR, XMaterial.STICK, 1, "{\"text\":\"The Energy Binder\"}", "{\"text\":\"Craft a Energy Binder\"}", true, true, AdvancementVisibility.ALWAYS, 4, 0, AdvancementDisplay.AdvancementFrame.TASK),
    DRACONIUM_MINER1(ENERGY_BINDER, XMaterial.BLAST_FURNACE, 1, "{\"text\":\"The mysterious miner\"}", "{\"text\":\"Craft an Draconium Miner I\"}", true, true, AdvancementVisibility.ALWAYS, 6, 0, AdvancementDisplay.AdvancementFrame.TASK),
    DRACONIUM_MINER2(DRACONIUM_MINER1, XMaterial.BLAST_FURNACE, 1, "{\"text\":\"The Mysterious Miner v2\"}", "{\"text\":\"Craft an Draconium Miner II\"}", true, true, AdvancementVisibility.ALWAYS, 6, -1, AdvancementDisplay.AdvancementFrame.TASK),
    DRACONIUM_MINER3(DRACONIUM_MINER2, XMaterial.BLAST_FURNACE, 1, "{\"text\":\"It can get better?\"}", "{\"text\":\"Craft an Draconium Miner III\"}", true, true, AdvancementVisibility.ALWAYS, 6, -2, AdvancementDisplay.AdvancementFrame.TASK),
    DRACONIUM_MINER4(DRACONIUM_MINER3, XMaterial.BLAST_FURNACE, 1, "{\"text\":\"The Miner that mines worlds\"}", "{\"text\":\"Craft an Draconium Miner IV\"}", true, true, AdvancementVisibility.ALWAYS, 6, -3, AdvancementDisplay.AdvancementFrame.CHALLENGE),
    LINK_ENERGY(DRACONIUM_MINER1, XMaterial.STICK, 1, "{\"text\":\"The first link\"}", "{\"text\":\"Link any Machine with another Machine\"}", true, true, AdvancementVisibility.ALWAYS, 7, 0, AdvancementDisplay.AdvancementFrame.TASK),

            ;

    public static AdvancementManager advancementManager;

    private Advancement advancement;
    private final DraconicAdvancements parent;
    private final XMaterial icon;
    private final String title, description, backgroundTexture;
    private final int required;
    private final boolean showToast;
    private final boolean displayMessage;
    private final AdvancementVisibility visibility;
    private final float x, y;
    private final AdvancementDisplay.AdvancementFrame frame;

    DraconicAdvancements(DraconicAdvancements parent, XMaterial icon, int required, String title, String description, boolean showToast, boolean displayMessage, AdvancementVisibility visibility, float x, float y, AdvancementDisplay.AdvancementFrame frame) {
        this.icon = icon;
        this.description = description;
        this.required = required;
        this.title = title;
        this.frame = frame;
        this.backgroundTexture = null;
        this.showToast = showToast;
        this.displayMessage = displayMessage;
        this.visibility = visibility;
        this.parent = parent;
        this.x = x;
        this.y = y;
    }

    DraconicAdvancements(XMaterial icon, int required, String title, String description, String backgroundTexture, boolean showToast, boolean displayMessage, AdvancementVisibility visibility, AdvancementDisplay.AdvancementFrame frame) {
        this.backgroundTexture = backgroundTexture;
        this.frame = frame;
        this.parent = null;
        this.icon = icon;
        this.description = description;
        this.title = title;
        this.required = required;
        this.showToast = showToast;
        this.displayMessage = displayMessage;
        this.visibility = visibility;
        this. x = 0;
        this.y = 0;

    }

    public Advancement getAdvancement() {
        return advancement;
    }

    public XMaterial getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public int getRequired() {
        return required;
    }

    public String getDescription() {
        return description;
    }

    public AdvancementDisplay.AdvancementFrame getFrame() {
        return frame;
    }

    public boolean isShowToast() {
        return showToast;
    }

    public boolean isDisplayMessage() {
        return displayMessage;
    }

    public AdvancementVisibility getVisibility() {
        return visibility;
    }

    public DraconicAdvancements getParent() {
        return parent;
    }

    public String getBackgroundTexture() {
        return backgroundTexture;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public static void addCriteria(DraconicAdvancements advancement, Player player) {
        int progress = DraconicAdvancements.advancementManager.getCriteriaProgress(player, advancement.getAdvancement());
        if(DraconicAdvancements.advancementManager.getCriteriaProgress(player, advancement.getAdvancement()) < advancement.getRequired()) {
            DraconicAdvancements.advancementManager.setCriteriaProgress(player, advancement.getAdvancement(), progress + 1);
        }
        DraconicAdvancements.advancementManager.saveProgress(player.getPlayer(), advancement.getAdvancement());
    }

    public static void registerAllAdvancements() {
        ArrayList<Advancement> arrayList = new ArrayList<>();
        advancementManager = new AdvancementManager(new NameKey("DraconicAdvancements", "dmanager"), Bukkit.getOnlinePlayers().toArray(new Player[Bukkit.getOnlinePlayers().size()]));
        for(DraconicAdvancements adv : DraconicAdvancements.values()) {
            AdvancementDisplay display = new AdvancementDisplay(adv.getIcon().parseMaterial(), adv.getTitle(), adv.getDescription(), adv.getFrame(), adv.getVisibility());
            if(adv.getParent() == null) {
                display.setBackgroundTexture(adv.getBackgroundTexture());
            } else {
                display.setCoordinates(adv.getX(), adv.getY());
            }
            adv.advancement = new Advancement(adv.getParent() != null ? adv.getParent().getAdvancement() : null, new NameKey("DraconicAdvancements", adv.name().toLowerCase()), display);
            adv.advancement.setCriteria(new Criteria(adv.getRequired()));
            arrayList.add(adv.getAdvancement());
        }

        Advancement[] advancementsAdded = arrayList.toArray(new Advancement[arrayList.size()]);

        for(Player player : Bukkit.getOnlinePlayers()) {
            advancementManager.loadProgress(player, advancementsAdded);
        }
        advancementManager.addAdvancement(advancementsAdded);

    }
}
