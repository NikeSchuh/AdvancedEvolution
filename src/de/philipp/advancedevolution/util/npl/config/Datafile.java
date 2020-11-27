package de.philipp.advancedevolution.util.npl.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Datafile {
    public static List<Datafile> datafiles = new ArrayList<Datafile>();

    private FileConfiguration bukkitConfiguration;
    private String fileName;
    private File dataFolder;

    public Datafile(String file) {

        this.fileName = file;

        create();

        Datafile.datafiles.add(this);

    }

    public void create() {

        File datafile = new File(fileName);

        if (!(datafile.exists())) {
            try {
                datafile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.bukkitConfiguration = YamlConfiguration.loadConfiguration(datafile);

        this.dataFolder = datafile;

    }

    public File getDatafolder() {
        return this.dataFolder;
    }

    public FileConfiguration getConfig() {
        return bukkitConfiguration;
    }

    public boolean hasKey(String key) {
        return getConfig().getKeys(false).stream().collect(Collectors.toList()).contains(key);
    }

    public void reloadConfig() {
        bukkitConfiguration = YamlConfiguration.loadConfiguration(getDatafolder());
    }

    public void saveData() {
        try {
            bukkitConfiguration.save(dataFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

