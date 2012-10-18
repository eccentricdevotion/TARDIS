package me.eccentric_nz.plugins.TARDIS;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

    public void main(String fileStr, Constants.SCHEMATIC s) {
        System.out.println(Constants.MY_PLUGIN_NAME + " Loading schematic: " + fileStr);
        try {
            File f = new File(fileStr);
            FileInputStream fis = new FileInputStream(f);
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
            }

            byte[] blocks = (byte[]) getChildTag(tagCollection, "Blocks", ByteArrayTag.class).getValue();
            byte[] data = (byte[]) getChildTag(tagCollection, "Data", ByteArrayTag.class).getValue();

            nbt.close();
            fis.close();
            int i = 0;
            String[] blockdata = new String[width*height*length];
            for (byte b : blocks) {
                blockdata[i] = b + ":" + data[i];
                i++;
            }
            List<String[]> lines = splitArray(blockdata, width);
            try {
                String csvFile = fileStr + ".csv";
                File file = new File(csvFile);
                BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
                for (String[] l : lines) {
                    String commas = "";
                    for (String bd : l) {
                        commas += bd+",";
                    }
                    String strCommas = commas.substring(0, (commas.length()-1));
                    bw.write(strCommas);
                    bw.newLine();
                }
                bw.close();
            } catch (IOException io) {
                System.err.println(Constants.MY_PLUGIN_NAME + " Could not save the time lords file!");
            }
        } catch (Exception e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " Schematic read error: " + e);
        }
    }

    public static List<String[]> splitArray(String[] array, int max) {
        int x = array.length / max;
        int lower = 0;
        int upper = 0;
        List<String[]> list = new ArrayList<String[]>();
        for (int i = 0; i < x; i++) {
            upper += max;
            list.add(Arrays.copyOfRange(array, lower, upper));
            lower = upper;
        }
        if (upper < array.length - 1) {
            lower = upper;
            upper = array.length;
            list.add(Arrays.copyOfRange(array, lower, upper));
        }
        return list;
    }
}