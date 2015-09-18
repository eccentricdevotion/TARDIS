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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import me.eccentric_nz.TARDIS.ARS.TARDISARS;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;

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
     * Copies files for use by the TARDIS builder classes.
     */
    public void copyFiles() {
        // make directories if they don't exist
        File schematicDir = new File(plugin.getDataFolder() + File.separator + "schematics");
        if (!schematicDir.exists()) {
            boolean result = schematicDir.mkdir();
            if (result) {
                schematicDir.setWritable(true);
                schematicDir.setExecutable(true);
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Created schematics directory.");
            }
        }
        File userDir = new File(plugin.getDataFolder() + File.separator + "user_schematics");
        if (!userDir.exists()) {
            boolean useResult = userDir.mkdir();
            if (useResult) {
                userDir.setWritable(true);
                userDir.setExecutable(true);
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Created user_schematics directory.");
            }
        }
        // DELUXE, ELEVENTH, TWELFTH, ARS & REDSTONE schematics supplied by Lord_Rahl and killeratnight at mcnovus.net
        // The PYRAMID schematic supplied by airomis (player at thatsnotacreeper.com)
        // The MASTER's schematic supplied by macdja38 at pvpcraft.ca
        // load schematic files - copy the default files if they don't exist
        String basepath = plugin.getDataFolder() + File.separator + "schematics" + File.separator;
        String userbasepath = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator;
        for (SCHEMATIC ts : CONSOLES.getByNames().values()) {
            if (!ts.isCustom()) {
                String str = basepath + ts.getPermission() + ".tschm";
                copy(str, plugin.getResource(ts.getPermission() + ".tschm"), true, plugin.getPluginName());
            }
        }
        // copy default room files as well
        for (TARDISARS ta : TARDISARS.values()) {
            if (ta.getOffset() != 0) {
                String str = basepath + ta.getActualName().toLowerCase() + ".tschm";
                copy(str, plugin.getResource(ta.getActualName().toLowerCase() + ".tschm"), false, plugin.getPluginName());
            }
        }
        String zeronstr = basepath + "zero.tschm";
        copy(zeronstr, plugin.getResource("zero.tschm"), false, plugin.getPluginName());
        String junknstr = basepath + "junk.tschm";
        copy(junknstr, plugin.getResource("junk.tschm"), true, plugin.getPluginName());
        String tmpnstr = userbasepath + "template.tschm";
        copy(tmpnstr, plugin.getResource("template.tschm"), false, plugin.getPluginName());
    }

    /**
     * Copies the schematic file to the TARDIS plugin directory if it is not
     * present.
     *
     * @param filepath the path to the file to write to
     * @param in the input file to read from
     * @param overwrite whether to overwrite the file
     * @param pluginName the name of the plugin doing the copy
     * @return a File
     */
    public static File copy(String filepath, InputStream in, boolean overwrite, String pluginName) {
        File file = new File(filepath);
        if (overwrite || !file.exists()) {
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
                    System.err.println(pluginName + "Could not save the file (" + file.toString() + ").");
                } finally {
                    try {
                        out.close();
                    } catch (IOException e) {
                    }
                }
            } catch (FileNotFoundException e) {
                System.err.println(pluginName + "File not found.");
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

    /**
     * Copies the schematic file to the TARDIS plugin directory if it is not
     * present.
     *
     * @param filename the name of the file to copy
     * @return a File
     */
    public File copy(String filename) {
        String filepath = plugin.getDataFolder() + File.separator + filename;
        InputStream in = plugin.getResource(filename);
        return copy(filepath, in, false, plugin.getPluginName());
    }
}
