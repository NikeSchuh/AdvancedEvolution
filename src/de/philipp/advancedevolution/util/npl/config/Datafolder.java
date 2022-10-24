package de.philipp.advancedevolution.util.npl.config;

import java.io.File;

public class Datafolder {

    private File datafolder;
    private String file;

    public Datafolder(String file) {

        this.file = file;
        create();

    }


    public void create() {

        File datafolder = new File(file);

        if (!(datafolder.exists())) {
            datafolder.mkdir();
        }

        this.datafolder = datafolder;

    }

    public File getDatafolder() {
        return this.datafolder;
    }


}

