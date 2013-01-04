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
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.jnbt.ByteArrayTag;
import org.jnbt.CompoundTag;
import org.jnbt.NBTInputStream;
import org.jnbt.ShortTag;
import org.jnbt.Tag;

public class TARDISSchematicReader {

    private TARDIS plugin;

    public TARDISSchematicReader(TARDIS plugin) {
        this.plugin = plugin;
    }

    private static Tag getChildTag(Map<String, Tag> items, String key, Class<? extends Tag> expected) {
        Tag tag = items.get(key);
        return tag;
    }

    public void readAndMakeCSV(String fileStr, TARDISConstants.SCHEMATIC s, boolean rotate) {
        plugin.console.sendMessage(plugin.pluginName + "Loading schematic: " + fileStr);
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
            switch (s) {
                case BUDGET:
                    plugin.budgetdimensions[0] = height;
                    plugin.budgetdimensions[1] = width;
                    plugin.budgetdimensions[2] = length;
                    break;
                case BIGGER:
                    plugin.biggerdimensions[0] = height;
                    plugin.biggerdimensions[1] = width;
                    plugin.biggerdimensions[2] = length;
                    break;
                case DELUXE:
                    plugin.deluxedimensions[0] = height;
                    plugin.deluxedimensions[1] = width;
                    plugin.deluxedimensions[2] = length;
                    break;
                case PASSAGE:
                    plugin.passagedimensions[0] = height;
                    plugin.passagedimensions[1] = width;
                    plugin.passagedimensions[2] = length;
                    break;
                case ARBORETUM:
                    plugin.arboretumdimensions[0] = height;
                    plugin.arboretumdimensions[1] = width;
                    plugin.arboretumdimensions[2] = length;
                    break;
                case POOL:
                    plugin.pooldimensions[0] = height;
                    plugin.pooldimensions[1] = width;
                    plugin.pooldimensions[2] = length;
                    break;
                default:
                    plugin.roomdimensions[0] = height;
                    plugin.roomdimensions[1] = width;
                    plugin.roomdimensions[2] = length;
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
                if (rotate) {
                    strarr = rotateCCW(strarr);
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
    }

    private static String[][] rotateCW(String[][] mat) {
        final int M = mat.length;
        final int N = mat[0].length;
        String[][] ret = new String[N][M];
        for (int r = 0; r < M; r++) {
            for (int c = 0; c < N; c++) {
                ret[c][M - 1 - r] = mat[r][c];
            }
        }
        return ret;
    }

    private static String[][] rotateCCW(String[][] mat) {
        final int M = mat.length;
        final int N = mat[0].length;
        String[][] ret = new String[M][N];
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < (M / 2); c++) {
                ret[c][r] = mat[N - c - 1][r];
            }
        }
        return ret;
    }
}