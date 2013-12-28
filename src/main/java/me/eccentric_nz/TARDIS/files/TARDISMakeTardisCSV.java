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
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;

/**
 * Cybermen are a "race" of cybernetically augmented humanoid. They vary greatly
 * in design, with different factions throughout time and space.
 *
 * @author eccentric_nz
 */
public class TARDISMakeTardisCSV {

    private final TARDIS plugin;
    TARDISInteriorSchematicReader reader;

    public TARDISMakeTardisCSV(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Loads CSV data into a 3D array for use by the TARDIS builder classes. If
     * the required files are not present in the TARDIS plugin folder, then they
     * are created. The WorldEdit schematics are first read and converted to CSV
     * format, then the CSV data is loaded into an array. This allows server
     * administrators to use their own schematic files.
     */
    public void loadCSV() {
        // make directories if they don't exist
        File schematicDir = new File(plugin.getDataFolder() + File.separator + "schematics");
        if (!schematicDir.exists()) {
            boolean result = schematicDir.mkdir();
            if (result) {
                schematicDir.setWritable(true);
                schematicDir.setExecutable(true);
                plugin.console.sendMessage(plugin.pluginName + "Created schematics directory.");
            }
        }
        File userDir = new File(plugin.getDataFolder() + File.separator + "user_schematics");
        if (!userDir.exists()) {
            boolean useResult = userDir.mkdir();
            if (useResult) {
                userDir.setWritable(true);
                userDir.setExecutable(true);
                plugin.console.sendMessage(plugin.pluginName + "Created user_schematics directory.");
            }
        }
        // load tardisCSV files - create them if they don't exist
        // TARDIS schematics supplied by Lord_Rahl and killeratnight at mcnovus.net
        plugin.arsSchematicCSV = createFile(SCHEMATIC.ARS.getFile() + ".csv");
        plugin.biggerSchematicCSV = createFile(SCHEMATIC.BIGGER.getFile() + ".csv");
        plugin.budgetSchematicCSV = createFile(SCHEMATIC.BUDGET.getFile() + ".csv");
        plugin.deluxeSchematicCSV = createFile(SCHEMATIC.DELUXE.getFile() + ".csv");
        plugin.eleventhSchematicCSV = createFile(SCHEMATIC.ELEVENTH.getFile() + ".csv");
        plugin.redstoneSchematicCSV = createFile(SCHEMATIC.REDSTONE.getFile() + ".csv");
        plugin.steampunkSchematicCSV = createFile(SCHEMATIC.STEAMPUNK.getFile() + ".csv");
        plugin.plankSchematicCSV = createFile(SCHEMATIC.PLANK.getFile() + ".csv");
        plugin.tomSchematicCSV = createFile(SCHEMATIC.TOM.getFile() + ".csv");
        reader = new TARDISInteriorSchematicReader(plugin);
        // load schematic files - copy the defaults if they don't exist
        String basepath = plugin.getDataFolder() + File.separator + "schematics" + File.separator;
        String userbasepath = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator;
        String arsnstr = basepath + SCHEMATIC.ARS.getFile();
        String bignstr = basepath + SCHEMATIC.BIGGER.getFile();
        String budnstr = basepath + SCHEMATIC.BUDGET.getFile();
        String delnstr = basepath + SCHEMATIC.DELUXE.getFile();
        String elenstr = basepath + SCHEMATIC.ELEVENTH.getFile();
        String rednstr = basepath + SCHEMATIC.REDSTONE.getFile();
        String stenstr = basepath + SCHEMATIC.STEAMPUNK.getFile();
        String planstr = basepath + SCHEMATIC.PLANK.getFile();
        String tomnstr = basepath + SCHEMATIC.TOM.getFile();
        plugin.arsSchematicFile = copy(arsnstr, plugin.getResource(SCHEMATIC.ARS.getFile()));
        plugin.biggerSchematicFile = copy(bignstr, plugin.getResource(SCHEMATIC.BIGGER.getFile()));
        plugin.budgetSchematicFile = copy(budnstr, plugin.getResource(SCHEMATIC.BUDGET.getFile()));
        plugin.deluxeSchematicFile = copy(delnstr, plugin.getResource(SCHEMATIC.DELUXE.getFile()));
        plugin.eleventhSchematicFile = copy(elenstr, plugin.getResource(SCHEMATIC.ELEVENTH.getFile()));
        plugin.redstoneSchematicFile = copy(rednstr, plugin.getResource(SCHEMATIC.REDSTONE.getFile()));
        plugin.steampunkSchematicFile = copy(stenstr, plugin.getResource(SCHEMATIC.STEAMPUNK.getFile()));
        plugin.plankSchematicFile = copy(planstr, plugin.getResource(SCHEMATIC.PLANK.getFile()));
        plugin.tomSchematicFile = copy(tomnstr, plugin.getResource(SCHEMATIC.TOM.getFile()));

        // copy default room files as well
        String antnstr = basepath + SCHEMATIC.ANTIGRAVITY.getFile();
        String arbornstr = basepath + SCHEMATIC.ARBORETUM.getFile();
        String baknstr = basepath + SCHEMATIC.BAKER.getFile();
        String bednstr = basepath + SCHEMATIC.BEDROOM.getFile();
        String empnstr = basepath + SCHEMATIC.EMPTY.getFile();
        String farnstr = basepath + SCHEMATIC.FARM.getFile();
        String granstr = basepath + SCHEMATIC.GRAVITY.getFile();
        String grenstr = basepath + SCHEMATIC.GREENHOUSE.getFile();
        String harnstr = basepath + SCHEMATIC.HARMONY.getFile();
        String kitnstr = basepath + SCHEMATIC.KITCHEN.getFile();
        String libnstr = basepath + SCHEMATIC.LIBRARY.getFile();
        String musnstr = basepath + SCHEMATIC.MUSHROOM.getFile();
        String passnstr = basepath + SCHEMATIC.PASSAGE.getFile();
        String poolnstr = basepath + SCHEMATIC.POOL.getFile();
        String railnstr = basepath + SCHEMATIC.RAIL.getFile();
        String stbnstr = basepath + SCHEMATIC.STABLE.getFile();
        String tmpnstr = basepath + "template.schematic";
        String trenstr = basepath + SCHEMATIC.TRENZALORE.getFile();
        String vaunstr = basepath + SCHEMATIC.VAULT.getFile();
        String vilnstr = basepath + SCHEMATIC.VILLAGE.getFile();
        String woonstr = basepath + SCHEMATIC.WOOD.getFile();
        String wornstr = basepath + SCHEMATIC.WORKSHOP.getFile();
        String rennstr = basepath + SCHEMATIC.RENDERER.getFile();
        copy(antnstr, plugin.getResource(SCHEMATIC.ANTIGRAVITY.getFile()));
        copy(arbornstr, plugin.getResource(SCHEMATIC.ARBORETUM.getFile()));
        copy(baknstr, plugin.getResource(SCHEMATIC.BAKER.getFile()));
        copy(bednstr, plugin.getResource(SCHEMATIC.BEDROOM.getFile()));
        copy(empnstr, plugin.getResource(SCHEMATIC.EMPTY.getFile()));
        copy(farnstr, plugin.getResource(SCHEMATIC.FARM.getFile()));
        copy(granstr, plugin.getResource(SCHEMATIC.GRAVITY.getFile()));
        copy(grenstr, plugin.getResource(SCHEMATIC.GREENHOUSE.getFile()));
        copy(harnstr, plugin.getResource(SCHEMATIC.HARMONY.getFile()));
        copy(kitnstr, plugin.getResource(SCHEMATIC.KITCHEN.getFile()));
        copy(libnstr, plugin.getResource(SCHEMATIC.LIBRARY.getFile()));
        copy(musnstr, plugin.getResource(SCHEMATIC.MUSHROOM.getFile()));
        copy(passnstr, plugin.getResource(SCHEMATIC.PASSAGE.getFile()));
        copy(poolnstr, plugin.getResource(SCHEMATIC.POOL.getFile()));
        copy(railnstr, plugin.getResource(SCHEMATIC.RAIL.getFile()));
        copy(stbnstr, plugin.getResource(SCHEMATIC.STABLE.getFile()));
        copy(tmpnstr, plugin.getResource("template.schematic"));
        copy(trenstr, plugin.getResource(SCHEMATIC.TRENZALORE.getFile()));
        copy(vaunstr, plugin.getResource(SCHEMATIC.VAULT.getFile()));
        copy(vilnstr, plugin.getResource(SCHEMATIC.VILLAGE.getFile()));
        copy(woonstr, plugin.getResource(SCHEMATIC.WOOD.getFile()));
        copy(wornstr, plugin.getResource(SCHEMATIC.WORKSHOP.getFile()));
        copy(rennstr, plugin.getResource(SCHEMATIC.RENDERER.getFile()));

        // read the schematics and make the CSV files
        reader.readAndMakeInteriorCSV(arsnstr, SCHEMATIC.ARS);
        reader.readAndMakeInteriorCSV(bignstr, SCHEMATIC.BIGGER);
        reader.readAndMakeInteriorCSV(budnstr, SCHEMATIC.BUDGET);
        reader.readAndMakeInteriorCSV(delnstr, SCHEMATIC.DELUXE);
        reader.readAndMakeInteriorCSV(elenstr, SCHEMATIC.ELEVENTH);
        reader.readAndMakeInteriorCSV(rednstr, SCHEMATIC.REDSTONE);
        reader.readAndMakeInteriorCSV(stenstr, SCHEMATIC.STEAMPUNK);
        reader.readAndMakeInteriorCSV(planstr, SCHEMATIC.PLANK);
        reader.readAndMakeInteriorCSV(tomnstr, SCHEMATIC.TOM);
        // load the schematic data from the tardisCSV files
        plugin.arsschematic = TARDISSchematic.schematic(plugin.arsSchematicCSV, plugin.arsdimensions[0], plugin.arsdimensions[1], plugin.arsdimensions[2]);
        plugin.biggerschematic = TARDISSchematic.schematic(plugin.biggerSchematicCSV, plugin.biggerdimensions[0], plugin.biggerdimensions[1], plugin.biggerdimensions[2]);
        plugin.budgetschematic = TARDISSchematic.schematic(plugin.budgetSchematicCSV, plugin.budgetdimensions[0], plugin.budgetdimensions[1], plugin.budgetdimensions[2]);
        plugin.deluxeschematic = TARDISSchematic.schematic(plugin.deluxeSchematicCSV, plugin.deluxedimensions[0], plugin.deluxedimensions[1], plugin.deluxedimensions[2]);
        plugin.eleventhschematic = TARDISSchematic.schematic(plugin.eleventhSchematicCSV, plugin.eleventhdimensions[0], plugin.eleventhdimensions[1], plugin.eleventhdimensions[2]);
        plugin.redstoneschematic = TARDISSchematic.schematic(plugin.redstoneSchematicCSV, plugin.redstonedimensions[0], plugin.redstonedimensions[1], plugin.redstonedimensions[2]);
        plugin.steampunkschematic = TARDISSchematic.schematic(plugin.steampunkSchematicCSV, plugin.steampunkdimensions[0], plugin.steampunkdimensions[1], plugin.steampunkdimensions[2]);
        plugin.plankschematic = TARDISSchematic.schematic(plugin.plankSchematicCSV, plugin.plankdimensions[0], plugin.plankdimensions[1], plugin.plankdimensions[2]);
        plugin.tomschematic = TARDISSchematic.schematic(plugin.tomSchematicCSV, plugin.tomdimensions[0], plugin.tomdimensions[1], plugin.tomdimensions[2]);
        // do custom schematic last
        File c_file = new File(userbasepath + SCHEMATIC.CUSTOM);
        if (plugin.getConfig().getBoolean("creation.custom_schematic")) {
            if (c_file.exists()) {
                plugin.customSchematicCSV = createCustomFile(SCHEMATIC.CUSTOM.getFile() + ".csv");
                String cusnstr = userbasepath + SCHEMATIC.CUSTOM;
                reader.readAndMakeInteriorCSV(cusnstr, SCHEMATIC.CUSTOM);
                plugin.customschematic = TARDISSchematic.schematic(plugin.customSchematicCSV, plugin.customdimensions[0], plugin.customdimensions[1], plugin.customdimensions[2]);
            } else {
                plugin.console.sendMessage(plugin.pluginName + "CUSTOM console is enabled in the config, but the schematic file was not found in 'user_schematics'!");
            }
        }
    }

    /**
     * Tries to find the specified CSV file. If it doesn't exist, an empty file
     * is created.
     *
     * @param filename the file to search for/create
     * @return the File
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
     * Tries to find the specified CSV file. If it doesn't exist, an empty file
     * is created.
     *
     * @param filename the file to search for/create
     * @return the File
     */
    public File createCustomFile(String filename) {
        File file = new File(plugin.getDataFolder() + File.separator + "user_schematics" + File.separator, filename);
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
     *
     * @param filepath the path to the file to write to
     * @param in the input file to read from
     * @return a File
     */
    public File copy(String filepath, InputStream in) {
        File file = new File(filepath);
        // for now always delete recipes...
        if (file.exists() && filepath.contains("recipes")) {
            plugin.debug("Deleting recipes.yml so we have a fresh copy...");
            file.delete();
        }
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
                    try {
                        out.close();
                    } catch (IOException e) {
                    }
                }
            } catch (FileNotFoundException e) {
                plugin.console.sendMessage(plugin.pluginName + "File not found.");
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
        return file;
    }
}
