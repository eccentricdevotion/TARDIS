/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.schematic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author eccentric_nz
 */
class TARDISCSVPaster {

    private final TARDIS plugin;

    TARDISCSVPaster(TARDIS plugin) {
        this.plugin = plugin;
    }

    String[][][] arrayFromCSV(File file) {
        String[][][] blocks = new String[8][11][11];
        try {
            BufferedReader bufRdr = new BufferedReader(new FileReader(file));
            for (int level = 0; level < 8; level++) {
                for (int row = 0; row < 11; row++) {
                    String line = bufRdr.readLine();
                    String[] strArr = line.split(",");
                    System.arraycopy(strArr, 0, blocks[level][row], 0, 11);
                }
            }
        } catch (IOException io) {
            plugin.debug("Could not read csv file" + io.getMessage());
        }
        return blocks;
    }

    void buildLegacy(String[][][] s, Location location) {
        World world = location.getWorld();
        int startx = location.getBlockX();
        int resetx = location.getBlockX();
        int startz = location.getBlockZ();
        int resetz = location.getBlockZ();
        int level;
        int starty = location.getBlockY();
        for (level = 0; level < 8; level++) {
            for (int row = 0; row < 11; row++) {
                for (int col = 0; col < 11; col++) {
                    String tmp = s[level][row][col];
                    if (!tmp.equals("-")) {
                        Material material = Material.valueOf(tmp);
                        BlockData id = material.createBlockData();
                        TARDISBlockSetters.setBlock(world, startx, starty, startz, id);
                    }
                    startx += 1;
                }

                startx = resetx;
                startz += 1;
            }
            startz = resetz;
            starty++;
        }
    }
}
