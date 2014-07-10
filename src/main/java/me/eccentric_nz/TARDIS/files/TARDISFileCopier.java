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
import me.eccentric_nz.TARDIS.TARDIS;
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
        // TARDIS schematics supplied by Lord_Rahl and killeratnight at mcnovus.net
        // load schematic files - copy the default files if they don't exist
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
        String warnstr = basepath + SCHEMATIC.WAR.getFile();
        copy(arsnstr, plugin.getResource(SCHEMATIC.ARS.getFile()), true);
        copy(bignstr, plugin.getResource(SCHEMATIC.BIGGER.getFile()), true);
        copy(budnstr, plugin.getResource(SCHEMATIC.BUDGET.getFile()), true);
        copy(delnstr, plugin.getResource(SCHEMATIC.DELUXE.getFile()), true);
        copy(elenstr, plugin.getResource(SCHEMATIC.ELEVENTH.getFile()), true);
        copy(rednstr, plugin.getResource(SCHEMATIC.REDSTONE.getFile()), true);
        copy(stenstr, plugin.getResource(SCHEMATIC.STEAMPUNK.getFile()), true);
        copy(planstr, plugin.getResource(SCHEMATIC.PLANK.getFile()), true);
        copy(tomnstr, plugin.getResource(SCHEMATIC.TOM.getFile()), true);
        copy(warnstr, plugin.getResource(SCHEMATIC.WAR.getFile()), true);

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
        String laznstr = basepath + SCHEMATIC.LAZARUS.getFile();
        String libnstr = basepath + SCHEMATIC.LIBRARY.getFile();
        String musnstr = basepath + SCHEMATIC.MUSHROOM.getFile();
        String passnstr = basepath + SCHEMATIC.PASSAGE.getFile();
        String poolnstr = basepath + SCHEMATIC.POOL.getFile();
        String railnstr = basepath + SCHEMATIC.RAIL.getFile();
        String stbnstr = basepath + SCHEMATIC.STABLE.getFile();
        String tmpnstr = userbasepath + "template.tschm";
        String trenstr = basepath + SCHEMATIC.TRENZALORE.getFile();
        String vaunstr = basepath + SCHEMATIC.VAULT.getFile();
        String vilnstr = basepath + SCHEMATIC.VILLAGE.getFile();
        String woonstr = basepath + SCHEMATIC.WOOD.getFile();
        String wornstr = basepath + SCHEMATIC.WORKSHOP.getFile();
        String rennstr = basepath + SCHEMATIC.RENDERER.getFile();
        String zeronstr = basepath + SCHEMATIC.ZERO.getFile();
        copy(antnstr, plugin.getResource(SCHEMATIC.ANTIGRAVITY.getFile()), false);
        copy(arbornstr, plugin.getResource(SCHEMATIC.ARBORETUM.getFile()), false);
        copy(baknstr, plugin.getResource(SCHEMATIC.BAKER.getFile()), false);
        copy(bednstr, plugin.getResource(SCHEMATIC.BEDROOM.getFile()), false);
        copy(empnstr, plugin.getResource(SCHEMATIC.EMPTY.getFile()), false);
        copy(farnstr, plugin.getResource(SCHEMATIC.FARM.getFile()), false);
        copy(granstr, plugin.getResource(SCHEMATIC.GRAVITY.getFile()), false);
        copy(grenstr, plugin.getResource(SCHEMATIC.GREENHOUSE.getFile()), false);
        copy(harnstr, plugin.getResource(SCHEMATIC.HARMONY.getFile()), false);
        copy(kitnstr, plugin.getResource(SCHEMATIC.KITCHEN.getFile()), false);
        copy(laznstr, plugin.getResource(SCHEMATIC.LAZARUS.getFile()), false);
        copy(libnstr, plugin.getResource(SCHEMATIC.LIBRARY.getFile()), false);
        copy(musnstr, plugin.getResource(SCHEMATIC.MUSHROOM.getFile()), false);
        copy(passnstr, plugin.getResource(SCHEMATIC.PASSAGE.getFile()), false);
        copy(poolnstr, plugin.getResource(SCHEMATIC.POOL.getFile()), false);
        copy(railnstr, plugin.getResource(SCHEMATIC.RAIL.getFile()), false);
        copy(stbnstr, plugin.getResource(SCHEMATIC.STABLE.getFile()), false);
        copy(tmpnstr, plugin.getResource("template.tschm"), false);
        copy(trenstr, plugin.getResource(SCHEMATIC.TRENZALORE.getFile()), false);
        copy(vaunstr, plugin.getResource(SCHEMATIC.VAULT.getFile()), false);
        copy(vilnstr, plugin.getResource(SCHEMATIC.VILLAGE.getFile()), false);
        copy(woonstr, plugin.getResource(SCHEMATIC.WOOD.getFile()), false);
        copy(wornstr, plugin.getResource(SCHEMATIC.WORKSHOP.getFile()), false);
        copy(rennstr, plugin.getResource(SCHEMATIC.RENDERER.getFile()), false);
        copy(zeronstr, plugin.getResource(SCHEMATIC.ZERO.getFile()), false);

        // check custom schematic last
        File c_file = new File(userbasepath + SCHEMATIC.CUSTOM.getFile());
        if (plugin.getConfig().getBoolean("creation.custom_schematic") && !c_file.exists()) {
            plugin.getConsole().sendMessage(plugin.getPluginName() + "CUSTOM console is enabled in the config, but the schematic file was not found in 'user_schematics'!");
        }
    }

    /**
     * Copies the schematic file to the TARDIS plugin directory if it is not
     * present.
     *
     * @param filepath the path to the file to write to
     * @param in the input file to read from
     * @param overwrite whether to overwrite the file
     * @return a File
     */
    public File copy(String filepath, InputStream in, boolean overwrite) {
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
                    plugin.getConsole().sendMessage(plugin.getPluginName() + "Could not save the file (" + file.toString() + ").");
                } finally {
                    try {
                        out.close();
                    } catch (IOException e) {
                    }
                }
            } catch (FileNotFoundException e) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + "File not found.");
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
        return copy(filepath, in, false);
    }
}
