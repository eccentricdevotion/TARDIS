/*
 * Copyright (C) 2023 eccentric_nz
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

import java.io.*;
import java.util.logging.Level;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Bukkit;

/**
 * Cybermen are a "race" of cybernetically augmented humanoid. They vary greatly
 * in design, with different factions throughout time and space.
 *
 * @author eccentric_nz
 */
public class TARDISFileCopier {

    private final TARDIS plugin;

    public TARDISFileCopier(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Copies a file to the TARDIS plugin directory if it is not present.
     *
     * @param filepath the path to the file to write to
     * @param in the input file to read from
     * @param overwrite whether to overwrite the file
     * @return a File
     */
    public static File copy(String filepath, InputStream in, boolean overwrite) {
        File file = new File(filepath);
        if (overwrite || !file.exists()) {
            try {
                OutputStream out = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int len;
                try {
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                } catch (IOException io) {
                    Bukkit.getLogger().log(Level.WARNING, "Copier: Could not save the file (" + file + ").");
                } finally {
                    try {
                        out.close();
                    } catch (IOException e) {
                        Bukkit.getLogger().log(Level.WARNING, "Copier: Could not close the output stream.");
                    }
                }
            } catch (FileNotFoundException e) {
                Bukkit.getLogger().log(Level.WARNING, "Copier: File not found: " + filepath);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        Bukkit.getLogger().log(Level.WARNING, "Copier: Could not close the input stream.");
                    }
                }
            }
        }
        return file;
    }

    /**
     * Copies files for use by the TARDIS builder classes.
     */
    public void copyRoomTemplateFile() {
        // make user_schematics directory if they doesn't exist
        File userDir = new File(plugin.getDataFolder() + File.separator + "user_schematics");
        if (!userDir.exists()) {
            boolean useResult = userDir.mkdir();
            if (useResult) {
                userDir.setWritable(true);
                userDir.setExecutable(true);
                Bukkit.getLogger().log(Level.INFO, "Created user_schematics directory.");
            }
        }
        // copy the template file if it doesn't exist
        String tmpnstr = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator + "template.tschm";
        copy(tmpnstr, plugin.getResource("rooms" + File.separator +"template.tschm"), true);
    }

    /**
     * Copies a file to the TARDIS plugin directory if it is not present.
     *
     * @param filename the name of the file to copy
     * @return a File
     */
    public File copy(String filename) {
        String filepath = plugin.getDataFolder() + File.separator + filename;
        InputStream in = plugin.getResource(filename);
        if (in != null) {
            return copy(filepath, in, false);
        } else {
            plugin.debug(filename);
            return null;
        }
    }
}
