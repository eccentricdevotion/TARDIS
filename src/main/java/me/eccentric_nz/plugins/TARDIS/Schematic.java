package me.eccentric_nz.plugins.TARDIS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Schematic {

    private final TARDIS plugin;
    private static String[][][] blocks;

    public Schematic(TARDIS plugin) {
        this.plugin = plugin;
    }

    public static String[][][] schematic(File file, short h, short w, short l) {

        // load data from csv file
        BufferedReader bufRdr = null;
        try {
            blocks = new String[h][w][l];

            bufRdr = new BufferedReader(new FileReader(file));
            String line;
            //read each line of text file
            for (int level = 0; level < h; level++) {
                for (int row = 0; row < w; row++) {
                    line = bufRdr.readLine();
                    if (line != null) {
                        String[] strArr = line.split(",");
                        System.arraycopy(strArr, 0, blocks[level][row], 0, l);
                    }
                }
            }
        } catch (IOException io) {
            TARDIS.plugin.console.sendMessage(Constants.MY_PLUGIN_NAME + " Could not read csv file");
        } finally {
            try {
                if (bufRdr != null) {
                    bufRdr.close();
                }
            } catch (Exception e) {
            }
        }
        return blocks;
    }
}