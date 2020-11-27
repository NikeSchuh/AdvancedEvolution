package de.philipp.advancedevolution.util.npl.config;

import de.philipp.advancedevolution.AdvancedEvolution;
import de.tr7zw.nbtapi.NBTFile;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NBTDatafile {

    private NBTFile nbtFile;
    private String fileName;
    private File dataFolder;

    public NBTDatafile(String file) {

        this.fileName = file;
        create();
        try {
            this.nbtFile = new NBTFile(new File(AdvancedEvolution.pluginFolder.getDatafolder(), fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        this.dataFolder = datafile;

    }

    public File getDatafolder() {
        return this.dataFolder;
    }

    public NBTFile getNbtFile() {
        return nbtFile;
    }



}
