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
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;

/**
 *
 * @author eccentric_nz
 */
public class TARDISMakeCSV {

    private final TARDIS plugin;
    TARDISSchematicReader reader;

    public TARDISMakeCSV(TARDIS plugin) {
        this.plugin = plugin;
    }

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
            plugin.budgetSchematicCSV = createFile(TARDISConstants.SCHEMATIC_BUDGET + ".csv");
            plugin.biggerSchematicCSV = createFile(TARDISConstants.SCHEMATIC_BIGGER + ".csv");
            plugin.deluxeSchematicCSV = createFile(TARDISConstants.SCHEMATIC_DELUXE + ".csv");
            plugin.arboretumSchematicCSV = createFile(TARDISConstants.SCHEMATIC_ARBORETUM + ".csv");
            plugin.bedroomSchematicCSV = createFile(TARDISConstants.SCHEMATIC_BEDROOM + ".csv");
            plugin.kitchenSchematicCSV = createFile(TARDISConstants.SCHEMATIC_KITCHEN + ".csv");
            plugin.librarySchematicCSV = createFile(TARDISConstants.SCHEMATIC_LIBRARY + ".csv");
            plugin.passageSchematicCSV = createFile(TARDISConstants.SCHEMATIC_PASSAGE + ".csv");
            plugin.poolSchematicCSV = createFile(TARDISConstants.SCHEMATIC_POOL + ".csv");
            plugin.vaultSchematicCSV = createFile(TARDISConstants.SCHEMATIC_VAULT + ".csv");
            plugin.emptySchematicCSV = createFile(TARDISConstants.SCHEMATIC_EMPTY + ".csv");
            reader = new TARDISSchematicReader(plugin);
            // load schematic files - copy the defaults if they don't exist
            String basepath = plugin.getDataFolder() + File.separator + "schematics" + File.separator;
            String budnstr = basepath + TARDISConstants.SCHEMATIC_BUDGET;
            plugin.budgetSchematicFile = copy(budnstr, plugin.getResource(TARDISConstants.SCHEMATIC_BUDGET));
            String bignstr = basepath + TARDISConstants.SCHEMATIC_BIGGER;
            plugin.biggerSchematicFile = copy(bignstr, plugin.getResource(TARDISConstants.SCHEMATIC_BIGGER));
            String delnstr = basepath + TARDISConstants.SCHEMATIC_DELUXE;
            plugin.deluxeSchematicFile = copy(delnstr, plugin.getResource(TARDISConstants.SCHEMATIC_DELUXE));
            String arbornstr = basepath + TARDISConstants.SCHEMATIC_ARBORETUM;
            plugin.arboretumSchematicFile = copy(arbornstr, plugin.getResource(TARDISConstants.SCHEMATIC_ARBORETUM));
            String bednstr = basepath + TARDISConstants.SCHEMATIC_BEDROOM;
            plugin.bedroomSchematicFile = copy(bednstr, plugin.getResource(TARDISConstants.SCHEMATIC_BEDROOM));
            String kitnstr = basepath + TARDISConstants.SCHEMATIC_KITCHEN;
            plugin.kitchenSchematicFile = copy(kitnstr, plugin.getResource(TARDISConstants.SCHEMATIC_KITCHEN));
            String libnstr = basepath + TARDISConstants.SCHEMATIC_LIBRARY;
            plugin.librarySchematicFile = copy(libnstr, plugin.getResource(TARDISConstants.SCHEMATIC_LIBRARY));
            String passnstr = basepath + TARDISConstants.SCHEMATIC_PASSAGE;
            plugin.passageSchematicFile = copy(passnstr, plugin.getResource(TARDISConstants.SCHEMATIC_PASSAGE));
            String poolnstr = basepath + TARDISConstants.SCHEMATIC_POOL;
            plugin.poolSchematicFile = copy(poolnstr, plugin.getResource(TARDISConstants.SCHEMATIC_POOL));
            String vaunstr = basepath + TARDISConstants.SCHEMATIC_VAULT;
            plugin.vaultSchematicFile = copy(vaunstr, plugin.getResource(TARDISConstants.SCHEMATIC_VAULT));
            String empnstr = basepath + TARDISConstants.SCHEMATIC_EMPTY;
            plugin.emptySchematicFile = copy(empnstr, plugin.getResource(TARDISConstants.SCHEMATIC_EMPTY));
            // read the schematics
            reader.readAndMakeCSV(budnstr, TARDISConstants.SCHEMATIC.BUDGET);
            reader.readAndMakeCSV(bignstr, TARDISConstants.SCHEMATIC.BIGGER);
            reader.readAndMakeCSV(delnstr, TARDISConstants.SCHEMATIC.DELUXE);
            reader.readAndMakeCSV(arbornstr, TARDISConstants.SCHEMATIC.ARBORETUM);
            reader.readAndMakeCSV(bednstr, TARDISConstants.SCHEMATIC.BEDROOM);
            reader.readAndMakeCSV(kitnstr, TARDISConstants.SCHEMATIC.KITCHEN);
            reader.readAndMakeCSV(libnstr, TARDISConstants.SCHEMATIC.LIBRARY);
            reader.readAndMakeCSV(passnstr, TARDISConstants.SCHEMATIC.PASSAGE);
            reader.readAndMakeCSV(poolnstr, TARDISConstants.SCHEMATIC.POOL);
            reader.readAndMakeCSV(vaunstr, TARDISConstants.SCHEMATIC.VAULT);
            reader.readAndMakeCSV(empnstr, TARDISConstants.SCHEMATIC.EMPTY);
            // load the schematic data into the csv files
            plugin.budgetschematic = TARDISSchematic.schematic(plugin.budgetSchematicCSV, plugin.budgetdimensions[0], plugin.budgetdimensions[1], plugin.budgetdimensions[2]);
            plugin.biggerschematic = TARDISSchematic.schematic(plugin.biggerSchematicCSV, plugin.biggerdimensions[0], plugin.biggerdimensions[1], plugin.biggerdimensions[2]);
            plugin.deluxeschematic = TARDISSchematic.schematic(plugin.deluxeSchematicCSV, plugin.deluxedimensions[0], plugin.deluxedimensions[1], plugin.deluxedimensions[2]);
            plugin.arboretumschematic = TARDISSchematic.schematic(plugin.arboretumSchematicCSV, plugin.roomdimensions[0], plugin.roomdimensions[1], plugin.roomdimensions[2]);
            plugin.bedroomschematic = TARDISSchematic.schematic(plugin.bedroomSchematicCSV, plugin.roomdimensions[0], plugin.roomdimensions[1], plugin.roomdimensions[2]);
            plugin.kitchenschematic = TARDISSchematic.schematic(plugin.kitchenSchematicCSV, plugin.roomdimensions[0], plugin.roomdimensions[1], plugin.roomdimensions[2]);
            plugin.libraryschematic = TARDISSchematic.schematic(plugin.librarySchematicCSV, plugin.roomdimensions[0], plugin.roomdimensions[1], plugin.roomdimensions[2]);
            plugin.passageschematic = TARDISSchematic.schematic(plugin.passageSchematicCSV, plugin.passagedimensions[0], plugin.passagedimensions[1], plugin.passagedimensions[2]);
            plugin.poolschematic = TARDISSchematic.schematic(plugin.poolSchematicCSV, plugin.roomdimensions[0], plugin.roomdimensions[1], plugin.roomdimensions[2]);
            plugin.vaultschematic = TARDISSchematic.schematic(plugin.vaultSchematicCSV, plugin.roomdimensions[0], plugin.roomdimensions[1], plugin.roomdimensions[2]);
            plugin.emptyschematic = TARDISSchematic.schematic(plugin.emptySchematicCSV, plugin.roomdimensions[0], plugin.roomdimensions[1], plugin.roomdimensions[2]);
        } catch (Exception e) {
            plugin.console.sendMessage(plugin.pluginName + "failed to retrieve files from directory. Using defaults.");
        }
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