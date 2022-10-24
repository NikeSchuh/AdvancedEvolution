package de.philipp.advancedevolution.region;

import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.util.Cuboid;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

public class DraconicRegion {

    private Cuboid region;
    private Material icon;
    private String dataName;
    private boolean allowBossSpawning, allowRituals;

    public DraconicRegion(Cuboid region, Material icon, String dataName, boolean allowBossSpawning, boolean allowRituals) {
        this.region = region;
        this.allowBossSpawning = allowBossSpawning;
        this.allowRituals = allowRituals;
        this.icon = icon;
        this.dataName = dataName;
    }

    public Material getIcon() {
        return icon;
    }

    public String getDataName() {
        return dataName;
    }

    public void save(ConfigurationSection section) {
        section.set("Region", region.serialize());
        section.set("DataName", dataName);
        section.set("Settings.Icon", XMaterial.matchXMaterial(icon).name());
        section.set("Settings.AllowRituals", true);
        section.set("Settings.AllowBosses", true);
    }

    public void setAllowBossSpawning(boolean allowBossSpawning) {
        this.allowBossSpawning = allowBossSpawning;
    }

    public void setAllowRituals(boolean allowRituals) {
        this.allowRituals = allowRituals;
    }

    public Cuboid getRegion() {
        return region;
    }

    public boolean isAllowBossSpawning() {
        return allowBossSpawning;
    }

    public boolean isAllowRituals() {
        return allowRituals;
    }

    public boolean contains(Location location) {
        return region.contains(location);
    }

    public boolean contains(Block block) {
        return region.contains(block);
    }

    public boolean contains(int x, int y, int z) {
        return region.contains(x, y, z);
    }

    public Block[] corners() {
        return region.corners();
    }

    public Location getCenter() {
        return region.getCenter();
    }

    public static DraconicRegion of(ConfigurationSection section) {
        Cuboid cuboid = new Cuboid((Map<String, Object>) section.getConfigurationSection("Region").getValues(false));
        boolean allowRituals = section.getBoolean("Settings.AllowRituals");
        boolean allowBosses = section.getBoolean("Settings.AllowBosses");
        XMaterial icon = XMaterial.valueOf(section.getString("Settings.Icon"));
        String dataName = section.getString("DataName");
        return new DraconicRegion(cuboid, icon.parseMaterial(), dataName, allowRituals, allowBosses);
    }

    public boolean isAllowed(ActionType actionType) {
        if(actionType == ActionType.BOSS_ACTION) return allowBossSpawning;
        if(actionType == ActionType.RITUAL_ACTION) return allowRituals;
        return true;
    }

    public enum ActionType {
        BOSS_ACTION, RITUAL_ACTION;
    }

    @Override
    public String toString() {
        return "DraconicRegion{" +
                "region=" + region +
                ", icon=" + icon +
                ", allowBossSpawning=" + allowBossSpawning +
                ", allowRituals=" + allowRituals +
                '}';
    }
}
