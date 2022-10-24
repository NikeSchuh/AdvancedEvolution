package de.philipp.advancedevolution.config;

import de.philipp.advancedevolution.util.npl.config.Datafile;

public class SettingsConfig extends Datafile {

    public ConfigEntry DebugMode;
    public ConfigEntry DraconicDimension;

    public SettingsConfig() {
        super("plugins/AdvancedEvolution/settings.yml");
        this.DebugMode = new ConfigEntry(this, "General.Debug", false);
        this.DraconicDimension = new ConfigEntry(this, "General.DimensionEnabled", true);

    }

}
