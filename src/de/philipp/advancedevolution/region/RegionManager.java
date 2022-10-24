package de.philipp.advancedevolution.region;

import de.philipp.advancedevolution.AdvancedEvolution;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegionManager {

    private Plugin plugin;
    private List<DraconicRegion> regions = new ArrayList<>();
    private HashMap<String, DraconicRegion> identifierMap = new HashMap<>();

    public RegionManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public List<DraconicRegion> getRegions() {
        return regions;
    }

    public DraconicRegion getRegion(Location location) {
        for(DraconicRegion region : regions) {
            if(region.contains(location)) {
                return region;
            }
        }
        return null;
    }

    public void registerRegion(DraconicRegion region) {
        if(identifierMap.containsKey(region.getDataName())) return;
        regions.add(region);
        identifierMap.put(region.getDataName(), region);

        FileConfiguration hash = AdvancedEvolution.regions.getConfig();
        ConfigurationSection section = hash.createSection(region.getDataName());
        region.save(section);
        AdvancedEvolution.regions.saveData();
    }

    public void loadRegion(DraconicRegion region) {
        regions.add(region);
        identifierMap.put(region.getDataName(), region);
    }

    public void deregisterRegion(DraconicRegion region) {
        if(!identifierMap.containsKey(region.getDataName())) return;
        regions.remove(region);
        identifierMap.remove(region.getDataName());

        FileConfiguration hash = AdvancedEvolution.regions.getConfig();
        hash.set(region.getDataName(), null);
        AdvancedEvolution.regions.saveData();
    }

    public DraconicRegion getRegion(String name) {
        return identifierMap.get(name);
    }

}
