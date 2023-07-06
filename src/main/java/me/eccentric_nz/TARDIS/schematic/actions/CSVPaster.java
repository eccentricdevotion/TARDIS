/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.schematic.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;

/**
 * @author eccentric_nz
 */
public class CSVPaster {

    private final TARDIS plugin;
    private final HashMap<String, Material> ID_LOOKUP = new HashMap<>();

    public CSVPaster(TARDIS plugin) {
        this.plugin = plugin;
        ID_LOOKUP.put("20", Material.GLASS);
        ID_LOOKUP.put("25", Material.NOTE_BLOCK);
        ID_LOOKUP.put("29:1", Material.STICKY_PISTON);
        ID_LOOKUP.put("35:0", Material.WHITE_WOOL);
        ID_LOOKUP.put("35:1", Material.ORANGE_WOOL);
        ID_LOOKUP.put("35:11", Material.BLUE_WOOL);
        ID_LOOKUP.put("35:7", Material.GRAY_WOOL);
        ID_LOOKUP.put("35:8", Material.LIGHT_GRAY_WOOL);
        ID_LOOKUP.put("50:5", Material.TORCH);
        ID_LOOKUP.put("54:~", Material.CHEST);
        ID_LOOKUP.put("55", Material.REDSTONE_WIRE);
        ID_LOOKUP.put("58", Material.CRAFTING_TABLE);
        ID_LOOKUP.put("61:~", Material.FURNACE);
        ID_LOOKUP.put("70", Material.STONE_PRESSURE_PLATE);
        ID_LOOKUP.put("71:8", Material.IRON_DOOR);
        ID_LOOKUP.put("71:~", Material.IRON_DOOR);
        ID_LOOKUP.put("76:5", Material.REDSTONE_TORCH);
        ID_LOOKUP.put("76:~", Material.REDSTONE_TORCH);
        ID_LOOKUP.put("77:~", Material.STONE_BUTTON);
        ID_LOOKUP.put("85", Material.OAK_FENCE);
        ID_LOOKUP.put("89", Material.GLOWSTONE);
        ID_LOOKUP.put("93:~", Material.REPEATER);
    }

    public String[][][] arrayFromCSV(File file) {
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
            bufRdr.close();
        } catch (IOException io) {
            plugin.debug("Could not read csv file" + io.getMessage());
        }
        return blocks;
    }

    public void buildLegacy(String[][][] s, Location location) {
        World world = location.getWorld();
        int startx = location.getBlockX();
        int resetx = location.getBlockX();
        int startz = location.getBlockZ();
        int resetz = location.getBlockZ();
        int level;
        int starty = location.getBlockY();
        Material material;
        for (level = 0; level < 8; level++) {
            for (int row = 0; row < 11; row++) {
                for (int col = 0; col < 11; col++) {
                    String tmp = s[level][row][col];
                    if (!tmp.equals("-") && !tmp.equals("0")) {
                        try {
                            material = Material.valueOf(tmp);
                        } catch (IllegalArgumentException e) {
                            material = ID_LOOKUP.get(tmp);
                        }
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
