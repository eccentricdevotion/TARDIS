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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants.SCHEMATIC;

/**
 *
 * @author eccentric_nz
 */
public class TARDISMakeCSV {

    private final TARDIS plugin;
    TARDISSchematicReader reader;
    // HashMaps
    private HashMap<String, String> filenames = new HashMap<String, String>();
    private HashMap<String, String> filepaths = new HashMap<String, String>();
    private HashMap<String, File> csvfiles = new HashMap<String, File>();
    private HashMap<String, File> schematicfiles = new HashMap<String, File>();
//    public HashMap<String, HashMap<Integer, Short>> dimensions = new HashMap<String, HashMap<Integer, Short>>();
//    public HashMap<String, String[][][]> arrays = new HashMap<String, String[][][]>();
    public String[][][] budgetschematic;
    public String[][][] biggerschematic;
    public String[][][] deluxeschematic;
    public String[][][] arboretumschematic;
    public String[][][] bedroomschematic;
    public String[][][] kitchenschematic;
    public String[][][] libraryschematic;
    public String[][][] passageschematic;
    public String[][][] poolschematic;
    public String[][][] vaultschematic;
    public String[][][] emptyschematic;
    public short[] budgetdimensions;
    public short[] biggerdimensions;
    public short[] deluxedimensions;
    public short[] passagedimensions;
    public short[] roomdimensions;

    public TARDISMakeCSV(TARDIS plugin) {
        this.plugin = plugin;
        this.reader = new TARDISSchematicReader(TARDIS.plugin);
        // file names
        filenames.put("BUDGET", "budget.schematic");
        filenames.put("BIGGER", "bigger.schematic");
        filenames.put("DELUXE", "deluxe.schematic");
        filenames.put("ARBORETUM", "arboretum.schematic");
        filenames.put("BEDROOM", "bedroom.schematic");
        filenames.put("KITCHEN", "kitchen.schematic");
        filenames.put("LIBRARY", "library.schematic");
        filenames.put("PASSAGE", "passage.schematic");
        filenames.put("POOL", "pool.schematic");
        filenames.put("VAULT", "vault.schematic");
        filenames.put("EMPTY", "empty.schematic");
    }

    public void makeCSVs() {
        File schematicDir = new File(plugin.getDataFolder() + File.separator + "schematics");
        if (!schematicDir.exists()) {
            boolean result = schematicDir.mkdir();
            if (result) {
                schematicDir.setWritable(true);
                schematicDir.setExecutable(true);
                plugin.console.sendMessage(plugin.pluginName + "Created schematics directory.");
            }
        }
        for (SCHEMATIC s : SCHEMATIC.values()) {
            String key = s.toString();
            String filename = filenames.get(key);
            // load csv files - create them if they don't exist
            csvfiles.put(key, createFile(filename + ".csv"));
            // load schematic files - copy the defaults if they don't exist
            String filepath = plugin.getDataFolder() + File.separator + "schematics" + File.separator + filename;
            filepaths.put(key, filepath);
            schematicfiles.put(key, copy(filepath, plugin.getResource(filename)));
            // read the schematics
//            dimensions.put(key, reader.readAndReturnDimensions(filepath));
            // load the schematic data into the csv files
//            plugin.debug("read schematic and wrote csv for: " + filename);
//            arrays.put(key, TARDISSchematic.schematic(csvfiles.get(key), dimensions.get(key)));
//            plugin.debug("created array from: " + filename);
        }
        // now do it the slow way...
        // read the schematics
//        budgetdimensions = reader.readAndReturnDimensions(filepaths.get("BUDGET"));
//        biggerdimensions = reader.readAndReturnDimensions(filepaths.get("BIGGER"));
//        deluxedimensions = reader.readAndReturnDimensions(filepaths.get("DELUXE"));
//        roomdimensions = reader.readAndReturnDimensions(filepaths.get("ARBORETUM"));
//        reader.readAndReturnDimensions(filepaths.get("BEDROOM"));
//        reader.readAndReturnDimensions(filepaths.get("KITCHEN"));
//        reader.readAndReturnDimensions(filepaths.get("LIBRAY"));
//        passagedimensions = reader.readAndReturnDimensions(filepaths.get("PASSAGE"));
//        reader.readAndReturnDimensions(filepaths.get("POOL"));
//        reader.readAndReturnDimensions(filepaths.get("VAULT"));
//        reader.readAndReturnDimensions(filepaths.get("EMPTY"));
        // load the schematic data into the csv files
//        budgetschematic = TARDISSchematic.schematic(csvfiles.get("BUDGET"), budgetdimensions);
//        biggerschematic = TARDISSchematic.schematic(csvfiles.get("BIGGER"), biggerdimensions);
//        deluxeschematic = TARDISSchematic.schematic(csvfiles.get("DELUXE"), deluxedimensions);
//        arboretumschematic = TARDISSchematic.schematic(csvfiles.get("ARBORETUM"), roomdimensions);
//        bedroomschematic = TARDISSchematic.schematic(csvfiles.get("BEDROOM"), roomdimensions);
//        kitchenschematic = TARDISSchematic.schematic(csvfiles.get("KITCHEN"), roomdimensions);
//        libraryschematic = TARDISSchematic.schematic(csvfiles.get("LIBRARY"), roomdimensions);
//        passageschematic = TARDISSchematic.schematic(csvfiles.get("PASSAGE"), passagedimensions);
//        poolschematic = TARDISSchematic.schematic(csvfiles.get("POOL"), roomdimensions);
//        vaultschematic = TARDISSchematic.schematic(csvfiles.get("VAULT"), roomdimensions);
//        emptyschematic = TARDISSchematic.schematic(csvfiles.get("EMPTY"), roomdimensions);
    }

    public File createFile(String filename) {
        File file = new File(plugin.getDataFolder() + File.separator + "schematics" + File.separator, filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException io) {
                plugin.console.sendMessage(plugin.pluginName + filename + " could not be created! " + io.getMessage());
            }
        }
        return file;
    }

    public File copy(String filepath, InputStream in) {
        File file = new File(filepath);
        if (!file.exists()) {
            OutputStream out = null;
            try {
                out = new FileOutputStream(file, false);
                byte[] buf = new byte[1024];
                int len;
                try {
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                } catch (IOException io) {
                    plugin.console.sendMessage(plugin.pluginName + "Could not save the file (" + file.toString() + ").");
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (Exception e) {
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                plugin.console.sendMessage(plugin.pluginName + "File not found.");
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
        return file;
    }
}