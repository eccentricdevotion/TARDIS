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

    public static String[][][] schematic(File file) {

        // load data from csv file
        try {
            blocks = new String[8][11][11];

            BufferedReader bufRdr = new BufferedReader(new FileReader(file));
            String line;
            //read each line of text file
            for (int level = 0; level < 8; level++) {
                for (int row = 0; row < 11; row++) {
                    line = bufRdr.readLine();
                    String[] strArr = line.split(",");
                    System.arraycopy(strArr, 0, blocks[level][row], 0, 11);
                }
            }
        } catch (IOException io) {
            System.err.println(Constants.MY_PLUGIN_NAME + " Could not read csv file");
        }
        return blocks;
    }
}