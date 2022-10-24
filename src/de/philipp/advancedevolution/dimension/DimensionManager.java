package de.philipp.advancedevolution.dimension;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.philipp.advancedevolution.dimension.dimensions.DraconicDimension;
import de.philipp.advancedevolution.dimension.generator.Dimension;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.Plugin;

public class DimensionManager {

    private Plugin plugin;

    public Dimension draconic;

    public DimensionManager(Plugin plugin) {
        this.plugin = plugin;
        if(isDraconicDimensionEnabled()) {
            draconic = new DraconicDimension();
        }
    }

    public boolean isDraconicDimensionEnabled() {
       return (boolean) AdvancedEvolution.getSettings().DraconicDimension.getValue();
    }



}
