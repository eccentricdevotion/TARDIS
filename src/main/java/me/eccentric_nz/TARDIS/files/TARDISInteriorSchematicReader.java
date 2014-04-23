/*
 * Copyright (C) 2014 eccentric_nz
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
import java.util.List;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import org.jnbt.ByteArrayTag;
import org.jnbt.CompoundTag;
import org.jnbt.NBTInputStream;
import org.jnbt.ShortTag;
import org.jnbt.Tag;

/**
 * The Ood are a humanoid species with coleoid tentacles on the lower portions
 * of their faces. They have no vocal cords and instead communicate by
 * telepathy.
 *
 * @author eccentric_nz
 */
public class TARDISInteriorSchematicReader {

    private static Tag getChildTag(Map<String, Tag> items, String key, Class<? extends Tag> expected) {
        Tag tag = items.get(key);
        return tag;
    }
    private final TARDIS plugin;

    public TARDISInteriorSchematicReader(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Reads a WorldEdit schematic file and writes the data to a CSV file. The
     * dimensions of the schematics are also stored for use by the TARDIS and
     * room builders.
     *
     * @param fileStr the path to the schematic file
     * @param s the schematic name
     */
    public void readAndMakeInteriorCSV(String fileStr, SCHEMATIC s) {
        plugin.debug("Loading schematic: " + fileStr);
        FileInputStream fis = null;
        try {
            File f = new File(fileStr);
            fis = new FileInputStream(f);
            NBTInputStream nbt = new NBTInputStream(fis);
            CompoundTag backuptag = (CompoundTag) nbt.readTag();
            Map<String, Tag> tagCollection = backuptag.getValue();

            short width = (Short) getChildTag(tagCollection, "Width", ShortTag.class).getValue();
            short height = (Short) getChildTag(tagCollection, "Height", ShortTag.class).getValue();
            short length = (Short) getChildTag(tagCollection, "Length", ShortTag.class).getValue();
            short[] shorty = new short[3];
            shorty[0] = height;
            shorty[1] = width;
            shorty[2] = length;
            switch (s) {
                case ARS:
                    plugin.getBuildKeeper().setARSDimensions(shorty);
                    break;
                case BUDGET:
                    plugin.getBuildKeeper().setBudgetDimensions(shorty);
                    break;
                case BIGGER:
                    plugin.getBuildKeeper().setBiggerDimensions(shorty);
                    break;
                case DELUXE:
                    plugin.getBuildKeeper().setDeluxeDimensions(shorty);
                    break;
                case ELEVENTH:
                    plugin.getBuildKeeper().setEleventhDimensions(shorty);
                    break;
                case REDSTONE:
                    plugin.getBuildKeeper().setRedstoneDimensions(shorty);
                    break;
                case STEAMPUNK:
                    plugin.getBuildKeeper().setSteampunkDimensions(shorty);
                    break;
                case PLANK:
                    plugin.getBuildKeeper().setPlankDimensions(shorty);
                    break;
                case TOM:
                    plugin.getBuildKeeper().setTomDimensions(shorty);
                    break;
                case CUSTOM:
                    plugin.getBuildKeeper().setCustomDimensions(shorty);
                    break;
            }

            byte[] blocks = (byte[]) getChildTag(tagCollection, "Blocks", ByteArrayTag.class).getValue();
            byte[] data = (byte[]) getChildTag(tagCollection, "Data", ByteArrayTag.class).getValue();

            nbt.close();
            fis.close();
            int i = 0;
            String[] blockdata = new String[width * height * length];
            for (byte b : blocks) {
                blockdata[i] = b + ":" + data[i];
                i++;
            }
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
                layers.add(strarr);
            }
            try {
                String csvFile = fileStr + ".csv";
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
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Could not save the TARDIS csv file!");
            }
        } catch (IOException e) {
            plugin.getConsole().sendMessage(plugin.getPluginName() + "Schematic read error: " + e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
