/*
 * Copyright (C) 2018 eccentric_nz
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

import me.eccentric_nz.TARDIS.ARS.TARDISARS;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;

import java.io.*;
import java.util.Locale;

/**
 * Cybermen are a "race" of cybernetically augmented humanoid. They vary greatly in design, with different factions
 * throughout time and space.
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
        // DELUXE, ELEVENTH, TWELFTH, ARS & REDSTONE schematics designed by Lord_Rahl and killeratnight at mcnovus.net
        // The CORAL schematic designed by vistaero
        // The PYRAMID schematic designed by airomis (player at thatsnotacreeper.com)
        // The ENDER schematic designed by ToppanaFIN (player at thatsnotacreeper.com)
        // The MASTER's schematic designed by ShadowAssociate
        // load schematic files - copy the default files if they don't exist
        String basepath = plugin.getDataFolder() + File.separator + "schematics" + File.separator;
        String userbasepath = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator;
        CONSOLES.getBY_NAMES().values().forEach((ts) -> {
            if (!ts.isCustom()) {
                String str = basepath + ts.getPermission() + ".tschm";
                copy(str, plugin.getResource(ts.getPermission() + ".tschm"), true);
            }
        });
        // copy default room files as well
        for (TARDISARS ta : TARDISARS.values()) {
            if (ta.getOffset() != 0) {
                String str = basepath + ta.getActualName().toLowerCase(Locale.ENGLISH) + ".tschm";
                copy(str, plugin.getResource(ta.getActualName().toLowerCase(Locale.ENGLISH) + ".tschm"), true);
            }
        }
        String zeronstr = basepath + "zero.tschm";
        copy(zeronstr, plugin.getResource("zero.tschm"), true);
        String junknstr = basepath + "junk.tschm";
        copy(junknstr, plugin.getResource("junk.tschm"), true);
        String tmpnstr = userbasepath + "template.tschm";
        copy(tmpnstr, plugin.getResource("template.tschm"), true);
        String gallifreynstr = basepath + "gallifrey.tschm";
        copy(gallifreynstr, plugin.getResource("gallifrey.tschm"), true);
        String skaronstr = basepath + "dalek_large.tschm";
        copy(skaronstr, plugin.getResource("dalek_large.tschm"), true);
        String daleknstr = basepath + "dalek_small.tschm";
        copy(daleknstr, plugin.getResource("dalek_small.tschm"), true);
        String sillnstr = basepath + "siluria_large.tschm";
        copy(sillnstr, plugin.getResource("siluria_large.tschm"), true);
        String silcnstr = basepath + "siluria_cross.tschm";
        copy(silcnstr, plugin.getResource("siluria_cross.tschm"), true);
        String silnsnstr = basepath + "siluria_north_south.tschm";
        copy(silnsnstr, plugin.getResource("siluria_north_south.tschm"), true);
        String silewnstr = basepath + "siluria_east_west.tschm";
        copy(silewnstr, plugin.getResource("siluria_east_west.tschm"), true);
    }

    /**
     * Copies the schematic file to the TARDIS plugin directory if it is not present.
     *
     * @param filepath  the path to the file to write to
     * @param in        the input file to read from
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
                    System.err.println("[TARDIS] Copier: Could not save the file (" + file.toString() + ").");
                } finally {
                    try {
                        out.close();
                    } catch (IOException e) {
                        System.err.println("[TARDIS] Copier: Could not close the output stream.");
                    }
                }
            } catch (FileNotFoundException e) {
                System.err.println("[TARDIS] Copier: File not found: " + filepath);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        System.err.println("[TARDIS] Copier: Could not close the input stream.");
                    }
                }
            }
        }
        return file;
    }

    /**
     * Copies the schematic file to the TARDIS plugin directory if it is not present.
     *
     * @param filename the name of the file to copy
     * @return a File
     */
    public File copy(String filename) {
        String filepath = plugin.getDataFolder() + File.separator + filename;
        InputStream in = plugin.getResource(filename);
        return copy(filepath, in, false);
    }
}
