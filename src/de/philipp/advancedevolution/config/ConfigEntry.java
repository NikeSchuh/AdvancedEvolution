package de.philipp.advancedevolution.config;

import de.philipp.advancedevolution.util.npl.config.Datafile;

public class ConfigEntry {

    private Datafile datafile;
    private String key;
    private Object object;

    public ConfigEntry(Datafile datafile, String key, Object obj) {
        if(datafile.getConfig() == null) {
            throw new IllegalArgumentException("The datafile this ConfigEntry is bound to does not exist.");
        }

        this.key = key;
        this.datafile = datafile;
        this.object = obj;

        if(!isExisting()) {
            datafile.getConfig().set(key, obj);
            datafile.saveData();
        }
    }

    public Object getValue() {
        return datafile.getConfig().get(key);
    }

    public boolean isExisting() {
        return datafile.getConfig().get(key) != null;
    }



}
