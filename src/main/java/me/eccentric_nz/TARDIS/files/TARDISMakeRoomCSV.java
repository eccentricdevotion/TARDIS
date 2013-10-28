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
import org.bukkit.ChatColor;

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
        for (String r : plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false)) {
            if (plugin.getRoomsConfig().getBoolean("rooms." + r + ".enabled")) {
                boolean user = plugin.getRoomsConfig().getBoolean("rooms." + r + ".user");
                String basepath = (user) ? userbasepath : defaultbasepath;
                String lower = r.toLowerCase(Locale.ENGLISH);
                File sch = new File(basepath + lower + ".schematic");
                if (sch.exists()) {
                    File file = createFile(lower + ".csv", basepath);
                    if (reader.readAndMakeRoomCSV(basepath + lower, r, false)) {
                        short[] dimensions = plugin.room_dimensions.get(r);
                        String[][][] schem = TARDISSchematic.schematic(file, dimensions[0], dimensions[1], dimensions[2]);
                        plugin.room_schematics.put(r, schem);
                    }
                } else {
                    plugin.console.sendMessage(plugin.pluginName + ChatColor.RED + lower + ".schematic was not found in 'user_schematics' and was disabled!");
                    plugin.getRoomsConfig().set("rooms." + r + ".enabled", false);
                }
            }
        }
    }

    /**
     * Tries to find the specified CSV file. If it doesn't exist, an empty file
     * is created.
     *
     * @param filename the file to find/create
     * @param folder the folder to look in
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
