package de.philipp.advancedevolution.config;

import de.philipp.advancedevolution.util.npl.config.Datafile;

public class SettingsConfig extends Datafile {

    private ConfigEntry DebugMode;

    public SettingsConfig() {
        super("plugins/AdvancedEvolution/settings.yml");
        this.DebugMode = new ConfigEntry(this, "General.Debug", false);
    }

}
