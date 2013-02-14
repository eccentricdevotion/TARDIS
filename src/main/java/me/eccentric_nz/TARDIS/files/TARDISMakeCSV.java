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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;

/**
 * Cybermen are a "race" of cybernetically augmented humanoid. They vary greatly
 * in design, with different factions throughout time and space.
 *
 * @author eccentric_nz
 */
public class TARDISMakeCSV {

    private final TARDIS plugin;
    TARDISSchematicReader reader;

    public TARDISMakeCSV(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Loads CSV data into a 3D array for use by the TARDIS and room builder
     * classes. If the required files are not present in the TARDIS plugin
     * folder, then they are created. The WorldEdit schematics are first read
     * and converted to CSV format, then the CSV data is loaded into an array.
     * This allows server administrators to use their own schematic files.
     */
    public void loadCSV() {
        try {
            File schematicDir = new File(plugin.getDataFolder() + File.separator + "schematics");
            if (!schematicDir.exists()) {
                boolean result = schematicDir.mkdir();
                if (result) {
                    schematicDir.setWritable(true);
                    schematicDir.setExecutable(true);
                    plugin.console.sendMessage(plugin.pluginName + "Created schematics directory.");
                }
            }
            // load csv files - create them if they don't exist
            plugin.arboretumSchematicCSV = createFile(TARDISConstants.SCHEMATIC_ARBORETUM + ".csv");
            plugin.bakerSchematicCSV = createFile(TARDISConstants.SCHEMATIC_BAKER + ".csv");
            plugin.bedroomSchematicCSV = createFile(TARDISConstants.SCHEMATIC_BEDROOM + ".csv");
            plugin.biggerSchematicCSV = createFile(TARDISConstants.SCHEMATIC_BIGGER + ".csv");
            plugin.budgetSchematicCSV = createFile(TARDISConstants.SCHEMATIC_BUDGET + ".csv");
            plugin.deluxeSchematicCSV = createFile(TARDISConstants.SCHEMATIC_DELUXE + ".csv");
            plugin.eleventhSchematicCSV = createFile(TARDISConstants.SCHEMATIC_ELEVENTH + ".csv");
            plugin.emptySchematicCSV = createFile(TARDISConstants.SCHEMATIC_EMPTY + ".csv");
            plugin.gravitySchematicCSV = createFile(TARDISConstants.SCHEMATIC_ANTIGRAVITY + ".csv");
            plugin.antigravitySchematicCSV = createFile(TARDISConstants.SCHEMATIC_GRAVITY + ".csv");
            plugin.harmonySchematicCSV = createFile(TARDISConstants.SCHEMATIC_HARMONY + ".csv");
            plugin.kitchenSchematicCSV = createFile(TARDISConstants.SCHEMATIC_KITCHEN + ".csv");
            plugin.librarySchematicCSV = createFile(TARDISConstants.SCHEMATIC_LIBRARY + ".csv");
            plugin.passageSchematicCSV = createFile(TARDISConstants.SCHEMATIC_PASSAGE + ".csv");
            plugin.passageSchematicCSV_EW = createFile(TARDISConstants.SCHEMATIC_PASSAGE + "_EW.csv");
            plugin.poolSchematicCSV = createFile(TARDISConstants.SCHEMATIC_POOL + ".csv");
            plugin.vaultSchematicCSV = createFile(TARDISConstants.SCHEMATIC_VAULT + ".csv");
            plugin.woodSchematicCSV = createFile(TARDISConstants.SCHEMATIC_WOOD + ".csv");
            plugin.workshopSchematicCSV = createFile(TARDISConstants.SCHEMATIC_WORKSHOP + ".csv");
            reader = new TARDISSchematicReader(plugin);
            // load schematic files - copy the defaults if they don't exist
            String basepath = plugin.getDataFolder() + File.separator + "schematics" + File.separator;
            String bignstr = basepath + TARDISConstants.SCHEMATIC_BIGGER;
            String budnstr = basepath + TARDISConstants.SCHEMATIC_BUDGET;
            String delnstr = basepath + TARDISConstants.SCHEMATIC_DELUXE;
            String elenstr = basepath + TARDISConstants.SCHEMATIC_ELEVENTH;
            String arbornstr = basepath + TARDISConstants.SCHEMATIC_ARBORETUM;
            String baknstr = basepath + TARDISConstants.SCHEMATIC_BAKER;
            String bednstr = basepath + TARDISConstants.SCHEMATIC_BEDROOM;
            String empnstr = basepath + TARDISConstants.SCHEMATIC_EMPTY;
            String granstr = basepath + TARDISConstants.SCHEMATIC_GRAVITY;
            String antnstr = basepath + TARDISConstants.SCHEMATIC_ANTIGRAVITY;
            String harnstr = basepath + TARDISConstants.SCHEMATIC_HARMONY;
            String kitnstr = basepath + TARDISConstants.SCHEMATIC_KITCHEN;
            String libnstr = basepath + TARDISConstants.SCHEMATIC_LIBRARY;
            String passnstr = basepath + TARDISConstants.SCHEMATIC_PASSAGE;
            String poolnstr = basepath + TARDISConstants.SCHEMATIC_POOL;
            String vaunstr = basepath + TARDISConstants.SCHEMATIC_VAULT;
            String woonstr = basepath + TARDISConstants.SCHEMATIC_WOOD;
            String wornstr = basepath + TARDISConstants.SCHEMATIC_WORKSHOP;
            plugin.biggerSchematicFile = copy(bignstr, plugin.getResource(TARDISConstants.SCHEMATIC_BIGGER));
            plugin.budgetSchematicFile = copy(budnstr, plugin.getResource(TARDISConstants.SCHEMATIC_BUDGET));
            plugin.deluxeSchematicFile = copy(delnstr, plugin.getResource(TARDISConstants.SCHEMATIC_DELUXE));
            plugin.eleventhSchematicFile = copy(elenstr, plugin.getResource(TARDISConstants.SCHEMATIC_ELEVENTH));
            plugin.arboretumSchematicFile = copy(arbornstr, plugin.getResource(TARDISConstants.SCHEMATIC_ARBORETUM));
            plugin.bakerSchematicFile = copy(baknstr, plugin.getResource(TARDISConstants.SCHEMATIC_BAKER));
            plugin.bedroomSchematicFile = copy(bednstr, plugin.getResource(TARDISConstants.SCHEMATIC_BEDROOM));
            plugin.emptySchematicFile = copy(empnstr, plugin.getResource(TARDISConstants.SCHEMATIC_EMPTY));
            plugin.gravitySchematicFile = copy(granstr, plugin.getResource(TARDISConstants.SCHEMATIC_GRAVITY));
            plugin.antigravitySchematicFile = copy(antnstr, plugin.getResource(TARDISConstants.SCHEMATIC_ANTIGRAVITY));
            plugin.harmonySchematicFile = copy(harnstr, plugin.getResource(TARDISConstants.SCHEMATIC_HARMONY));
            plugin.kitchenSchematicFile = copy(kitnstr, plugin.getResource(TARDISConstants.SCHEMATIC_KITCHEN));
            plugin.librarySchematicFile = copy(libnstr, plugin.getResource(TARDISConstants.SCHEMATIC_LIBRARY));
            plugin.passageSchematicFile = copy(passnstr, plugin.getResource(TARDISConstants.SCHEMATIC_PASSAGE));
            plugin.poolSchematicFile = copy(poolnstr, plugin.getResource(TARDISConstants.SCHEMATIC_POOL));
            plugin.vaultSchematicFile = copy(vaunstr, plugin.getResource(TARDISConstants.SCHEMATIC_VAULT));
            plugin.woodSchematicFile = copy(woonstr, plugin.getResource(TARDISConstants.SCHEMATIC_WOOD));
            plugin.workshopSchematicFile = copy(wornstr, plugin.getResource(TARDISConstants.SCHEMATIC_WORKSHOP));
            // read the schematics and make the CSV files
            reader.readAndMakeCSV(arbornstr, TARDISConstants.SCHEMATIC.ARBORETUM, false);
            reader.readAndMakeCSV(baknstr, TARDISConstants.SCHEMATIC.BAKER, false);
            reader.readAndMakeCSV(bednstr, TARDISConstants.SCHEMATIC.BEDROOM, false);
            reader.readAndMakeCSV(bignstr, TARDISConstants.SCHEMATIC.BIGGER, false);
            reader.readAndMakeCSV(budnstr, TARDISConstants.SCHEMATIC.BUDGET, false);
            reader.readAndMakeCSV(delnstr, TARDISConstants.SCHEMATIC.DELUXE, false);
            reader.readAndMakeCSV(elenstr, TARDISConstants.SCHEMATIC.ELEVENTH, false);
            reader.readAndMakeCSV(empnstr, TARDISConstants.SCHEMATIC.EMPTY, false);
            reader.readAndMakeCSV(granstr, TARDISConstants.SCHEMATIC.GRAVITY, false);
            reader.readAndMakeCSV(antnstr, TARDISConstants.SCHEMATIC.ANTIGRAVITY, false);
            reader.readAndMakeCSV(harnstr, TARDISConstants.SCHEMATIC.HARMONY, false);
            reader.readAndMakeCSV(kitnstr, TARDISConstants.SCHEMATIC.KITCHEN, false);
            reader.readAndMakeCSV(libnstr, TARDISConstants.SCHEMATIC.LIBRARY, false);
            reader.readAndMakeCSV(passnstr, TARDISConstants.SCHEMATIC.PASSAGE, false);
            reader.readAndMakeCSV(passnstr, TARDISConstants.SCHEMATIC.PASSAGE, true);
            reader.readAndMakeCSV(poolnstr, TARDISConstants.SCHEMATIC.POOL, false);
            reader.readAndMakeCSV(vaunstr, TARDISConstants.SCHEMATIC.VAULT, false);
            reader.readAndMakeCSV(woonstr, TARDISConstants.SCHEMATIC.WOOD, false);
            reader.readAndMakeCSV(wornstr, TARDISConstants.SCHEMATIC.WORKSHOP, false);
            // load the schematic data from the csv files
            plugin.biggerschematic = TARDISSchematic.schematic(plugin.biggerSchematicCSV, plugin.biggerdimensions[0], plugin.biggerdimensions[1], plugin.biggerdimensions[2]);
            plugin.budgetschematic = TARDISSchematic.schematic(plugin.budgetSchematicCSV, plugin.budgetdimensions[0], plugin.budgetdimensions[1], plugin.budgetdimensions[2]);
            plugin.deluxeschematic = TARDISSchematic.schematic(plugin.deluxeSchematicCSV, plugin.deluxedimensions[0], plugin.deluxedimensions[1], plugin.deluxedimensions[2]);
            plugin.eleventhschematic = TARDISSchematic.schematic(plugin.eleventhSchematicCSV, plugin.eleventhdimensions[0], plugin.eleventhdimensions[1], plugin.eleventhdimensions[2]);
            plugin.bedroomschematic = TARDISSchematic.schematic(plugin.bedroomSchematicCSV, plugin.roomdimensions[0], plugin.roomdimensions[1], plugin.roomdimensions[2]);
            plugin.arboretumschematic = TARDISSchematic.schematic(plugin.arboretumSchematicCSV, plugin.arboretumdimensions[0], plugin.arboretumdimensions[1], plugin.arboretumdimensions[2]);
            plugin.bakerschematic = TARDISSchematic.schematic(plugin.bakerSchematicCSV, plugin.roomdimensions[0], plugin.roomdimensions[1], plugin.roomdimensions[2]);
            plugin.emptyschematic = TARDISSchematic.schematic(plugin.emptySchematicCSV, plugin.roomdimensions[0], plugin.roomdimensions[1], plugin.roomdimensions[2]);
            plugin.gravityschematic = TARDISSchematic.schematic(plugin.gravitySchematicCSV, plugin.gravitydimensions[0], plugin.gravitydimensions[1], plugin.gravitydimensions[2]);
            plugin.antigravityschematic = TARDISSchematic.schematic(plugin.antigravitySchematicCSV, plugin.antigravitydimensions[0], plugin.antigravitydimensions[1], plugin.antigravitydimensions[2]);
            plugin.harmonyschematic = TARDISSchematic.schematic(plugin.harmonySchematicCSV, plugin.roomdimensions[0], plugin.roomdimensions[1], plugin.roomdimensions[2]);
            plugin.kitchenschematic = TARDISSchematic.schematic(plugin.kitchenSchematicCSV, plugin.roomdimensions[0], plugin.roomdimensions[1], plugin.roomdimensions[2]);
            plugin.libraryschematic = TARDISSchematic.schematic(plugin.librarySchematicCSV, plugin.roomdimensions[0], plugin.roomdimensions[1], plugin.roomdimensions[2]);
            plugin.passageschematic = TARDISSchematic.schematic(plugin.passageSchematicCSV, plugin.passagedimensions[0], plugin.passagedimensions[1], plugin.passagedimensions[2]);
            plugin.passageschematic_EW = TARDISSchematic.schematic(plugin.passageSchematicCSV_EW, plugin.passagedimensions[0], plugin.passagedimensions[1], plugin.passagedimensions[2]);
            plugin.poolschematic = TARDISSchematic.schematic(plugin.poolSchematicCSV, plugin.pooldimensions[0], plugin.pooldimensions[1], plugin.pooldimensions[2]);
            plugin.vaultschematic = TARDISSchematic.schematic(plugin.vaultSchematicCSV, plugin.roomdimensions[0], plugin.roomdimensions[1], plugin.roomdimensions[2]);
            plugin.woodschematic = TARDISSchematic.schematic(plugin.woodSchematicCSV, plugin.roomdimensions[0], plugin.roomdimensions[1], plugin.roomdimensions[2]);
            plugin.workshopschematic = TARDISSchematic.schematic(plugin.workshopSchematicCSV, plugin.roomdimensions[0], plugin.roomdimensions[1], plugin.roomdimensions[2]);
        } catch (Exception e) {
            plugin.console.sendMessage(plugin.pluginName + "Failed to retrieve files from directory. Using defaults.");
        }
    }

    /**
     * Tries to find the specified CSV file. If it doesn't exist, an empty file
     * is created.
     */
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

    /**
     * Copies the schematic file to the TARDIS plugin directory if it is not
     * present.
     */
    public File copy(String filepath, InputStream in) {
        File file = new File(filepath);
        if (!file.exists()) {
            OutputStream out = null;
            try {
                out = new FileOutputStream(file);
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
