/*
 * Copyright (C) 2013 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import me.eccentric_nz.TARDIS.TARDIS;

/**
 * Daleks are the mutated descendants of the Kaleds of the planet Skaro. They
 * fought the Time Lords in the Last Great Time War, ending in the near-total
 * destruction of both races. Regarded by the Doctor as his greatest enemy, the
 * Daleks are hated and feared throughout time and space.
 *
 * @author eccentric_nz
 */
public class TARDISSchematic {

    private static String[][][] blocks;

    /**
     * Loads data from a CSV file into a 3D array.
     *
     * @param file the CSV file to read
     * @param l the length in blocks of the cuboid object
     * @param h the height in blocks of the cuboid object
     * @param w the width in blocks of the cuboid object
     * @return a 3-dimensional array of id:data values
     */
    public static String[][][] schematic(File file, short h, short w, short l) {

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
            TARDIS.plugin.console.sendMessage(TARDIS.plugin.pluginName + "Could not read csv file");
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

    private TARDISSchematic() {
    }
}
