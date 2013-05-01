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
import java.io.IOException;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;

/**
 * The Unified Intelligence Taskforce — formerly known as the United Nations
 * Intelligence Taskforce, and more usually called UNIT — was a military
 * organisation which operated under the auspices of the United Nations. Its
 * remit was to investigate and combat paranormal and extraterrestrial threats
 * to the Earth. UNIT was not the only alien defence organisation, but it was
 * the one with which the Doctor had the closest personal involvement.
 *
 * @author eccentric_nz
 */
public class TARDISMakeRoomCSV {

    private final TARDIS plugin;
    TARDISRoomSchematicReader reader;

    public TARDISMakeRoomCSV(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Loads CSV data into a 3D array for use by the room builder classes. If
     * the required files are not present in the TARDIS plugin folder, then they
     * are created. The WorldEdit schematics are first read and converted to CSV
     * format, then the CSV data is loaded into an array. This allows server
     * administrators to use their own schematic files.
     */
    public void loadCSV() {
        File userDir = new File(plugin.getDataFolder() + File.separator + "user_schematics");
        if (!userDir.exists()) {
            boolean result = userDir.mkdir();
            if (result) {
                userDir.setWritable(true);
                userDir.setExecutable(true);
                plugin.console.sendMessage(plugin.pluginName + "Created user_schematics directory.");
            }
        }
        // load room CSV files - create them if they don't exist
        reader = new TARDISRoomSchematicReader(plugin);
        String defaultbasepath = plugin.getDataFolder() + File.separator + "schematics" + File.separator;
        String userbasepath = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator;
        for (String r : plugin.getConfig().getConfigurationSection("rooms").getKeys(false)) {
            String basepath = (plugin.getConfig().getBoolean("rooms." + r + ".user")) ? userbasepath : defaultbasepath;
            String lower = r.toLowerCase(Locale.ENGLISH);
            File file = createFile(lower + ".csv", basepath);
            reader.readAndMakeRoomCSV(basepath + lower, r, false);
            short[] dimensions = plugin.room_dimensions.get(r);
            String[][][] schem = TARDISSchematic.schematic(file, dimensions[0], dimensions[1], dimensions[2]);
            plugin.room_schematics.put(r, schem);
            if (r.equals("PASSAGE") || r.equals("LONG")) {
                // repeat for EW
                File file_EW = createFile(lower + "_EW.csv", defaultbasepath);
                reader.readAndMakeRoomCSV(basepath + lower, r + "_EW", true);
                String[][][] schem_EW = TARDISSchematic.schematic(file_EW, dimensions[0], dimensions[1], dimensions[2]);
                plugin.room_schematics.put(r + "_EW", schem_EW);
            }
        }
    }

    /**
     * Tries to find the specified CSV file. If it doesn't exist, an empty file
     * is created.
     *
     * @param filename the file to find/create
     * @return a File
     */
    public File createFile(String filename, String folder) {
        File file = new File(folder, filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException io) {
                plugin.console.sendMessage(plugin.pluginName + filename + " could not be created! " + io.getMessage());
            }
        }
        return file;
    }
}
