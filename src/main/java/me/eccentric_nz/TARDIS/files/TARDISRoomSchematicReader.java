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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import org.jnbt.ByteArrayTag;
import org.jnbt.CompoundTag;
import org.jnbt.NBTInputStream;
import org.jnbt.ShortTag;
import org.jnbt.Tag;

/**
 * The Abzorbaloff was an alien who disguised himself as a human under the alias
 * Victor Kennedy. He could absorb the bodies of his victims, along with their
 * memories and consciousness, into himself at a simple touch.
 *
 * @author eccentric_nz
 */
public class TARDISRoomSchematicReader {

    private static Tag getChildTag(Map<String, Tag> items, String key, Class<? extends Tag> expected) {
        Tag tag = items.get(key);
        return tag;
    }

    private TARDIS plugin;
    private HashMap<Integer, Integer> blockConversion = new HashMap<Integer, Integer>();
    private List<Byte> ignoreBlocks = Arrays.asList(new Byte[]{0, 14, 19, 52, 79});

    public TARDISRoomSchematicReader(TARDIS plugin) {
        this.plugin = plugin;
        blockConversion.put(1, 4); // stone -> cobblestone
        blockConversion.put(2, 3); // grass -> dirt
        blockConversion.put(18, 6); // leaves -> sapling
        blockConversion.put(31, 295); // long grass -> seeds
        blockConversion.put(55, 331); // redstone wire -> redstone dust
        blockConversion.put(59, 295); // crops -> seeds
        blockConversion.put(60, 3); // farmland -> dirt
        blockConversion.put(63, 323); // sign post -> sign
        blockConversion.put(68, 323); // wall sign -> sign
        blockConversion.put(76, 75); // restone torch on -> redstone torch off
        blockConversion.put(83, 338); // sugarcane block -> sugarcane item
        blockConversion.put(93, 356); // redstone repeater off -> redstone repeater item
        blockConversion.put(94, 356); // redstone repeater on -> redstone repeater item
        blockConversion.put(99, 39); // mushroom block -> brown mushroom
        blockConversion.put(100, 39); // mushroom block -> brown mushroom
        blockConversion.put(104, 361); // pumpkin stem -> pumpkin seed
        blockConversion.put(105, 362); // melon stem -> melon seed
        blockConversion.put(110, 3); // mycelium -> dirt
        blockConversion.put(115, 372); // netherwart -> netherwart item
        blockConversion.put(117, 379); // brewing stand -> brewing stand item
        blockConversion.put(118, 380); // cauldron -> cauldron item
        blockConversion.put(124, 123); // restone lamp on -> redstone lamp off
        blockConversion.put(127, 351); // cocoa plant -> cocoa seed
        blockConversion.put(140, 390); // flower pot -> flower pot item
        blockConversion.put(141, 391); // carrot plant -> carrot
        blockConversion.put(142, 392); // potato plant -> potato
        blockConversion.put(150, 149); // restone comparator on -> redstone comparator off
    }

    /**
     * Reads a WorldEdit schematic file and writes the data to a CSV file. The
     * dimensions of the schematics are also stored for use by the room builder.
     *
     * @param fileStr the schematic file to read
     * @param s the schematic name
     * @param rotate whether to rotate the schematic 90 degrees
     * counter-clockwise
     * @return true or false depending on whether the room is square or not
     */
    public boolean readAndMakeRoomCSV(String fileStr, String s, boolean rotate) {
        HashMap<String, Integer> blockIDs = new HashMap<String, Integer>();
        boolean square = true;
        plugin.debug("Loading schematic: " + fileStr + ".schematic");
        FileInputStream fis = null;
        try {
            File f = new File(fileStr + ".schematic");
            fis = new FileInputStream(f);
            NBTInputStream nbt = new NBTInputStream(fis);
            CompoundTag backuptag = (CompoundTag) nbt.readTag();
            Map<String, Tag> tagCollection = backuptag.getValue();

            short width = (Short) getChildTag(tagCollection, "Width", ShortTag.class).getValue();
            short height = (Short) getChildTag(tagCollection, "Height", ShortTag.class).getValue();
            short length = (Short) getChildTag(tagCollection, "Length", ShortTag.class).getValue();

            // check the room is square - should never fail on plugin enable as schematics are checked when added
            if (width != length) {
                plugin.console.sendMessage(plugin.pluginName + "Load failed - schematic had unequal length sides!");
                square = false;
            } else {
                short[] dimensions = new short[3];
                dimensions[0] = height;
                dimensions[1] = width;
                dimensions[2] = length;
                plugin.room_dimensions.put(s, dimensions);

                byte[] blocks = (byte[]) getChildTag(tagCollection, "Blocks", ByteArrayTag.class).getValue();
                byte[] data = (byte[]) getChildTag(tagCollection, "Data", ByteArrayTag.class).getValue();

                nbt.close();
                fis.close();
                int i = 0;
                String[] blockdata = new String[width * height * length];
                int adjust = 256;
                for (byte b : blocks) {
                    if (!ignoreBlocks.contains(b)) {
                        Integer bid = (b < (byte) 0) ? b + adjust : b;
                        if (blockConversion.containsKey(bid)) {
                            bid = blockConversion.get(bid);
                        }
                        if (bid == 35 && (data[i] == 1 || data[i] == 8)) {
                            String bstr = bid + ":" + data[i];
                            if (blockIDs.containsKey(bstr)) {
                                Integer count = blockIDs.get(bstr) + 1;
                                blockIDs.put(bstr, count);
                            } else {
                                blockIDs.put(bstr, 1);
                            }
                        } else {
                            if (blockIDs.containsKey(bid.toString())) {
                                Integer count = blockIDs.get(bid.toString()) + 1;
                                blockIDs.put(bid.toString(), count);
                            } else {
                                blockIDs.put(bid.toString(), 1);
                            }
                        }
                    }
                    blockdata[i] = b + ":" + data[i];
                    i++;
                }
                plugin.roomBlockCounts.put(s, blockIDs);
                int j = 0;
                List<String[][]> layers = new ArrayList<String[][]>();
                for (int h = 0; h < height; h++) {
                    String[][] strarr = new String[width][length];
                    for (int w = 0; w < width; w++) {
                        for (int l = 0; l < length; l++) {
                            strarr[w][l] = blockdata[j];
                            j++;
                        }
                    }
                    if (rotate) {
                        strarr = rotateSquareCCW(strarr);
                    }
                    layers.add(strarr);
                }
                try {
                    String csvFile = (rotate) ? fileStr + "_EW.csv" : fileStr + ".csv";
                    File file = new File(csvFile);
                    BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
                    for (String[][] l : layers) {
                        for (String[] lines : l) {
                            StringBuilder buf = new StringBuilder();
                            for (String bd : lines) {
                                buf.append(bd).append(",");
                            }
                            String commas = buf.toString();
                            String strCommas = commas.substring(0, (commas.length() - 1));
                            bw.write(strCommas);
                            bw.newLine();
                        }
                    }
                    bw.close();

                } catch (IOException io) {
                    plugin.console.sendMessage(plugin.pluginName + "Could not save the time lords file!");
                }
            }
        } catch (IOException e) {
            plugin.console.sendMessage(plugin.pluginName + "Schematic read error: " + e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                }
            }
        }
        return square;
    }

    /**
     * Rotates a square 2D array 90 degrees counterclockwise. This is used for
     * the (non-symmetrical) TARDIS passage ways so that they are built
     * correctly in the EAST and WEST directions.
     */
    private String[][] rotateSquareCCW(String[][] mat) {
        int size = mat.length;
        String[][] out = new String[size][size];
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                out[r][c] = mat[c][size - r - 1];
            }
        }
        return out;
    }
}
