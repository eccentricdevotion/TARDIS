/*
 * Copyright (C) 2022 eccentric_nz
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
package me.eccentric_nz.TARDIS.perms;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISFloodgate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * The Sex Gas was an intelligent cloud of gas. It inhabited the body of Carys Fletcher to have sex with other humans,
 * killing them in the process. In its natural form, the sex gas was a thick pink-purple gas with high concentrations of
 * vorax and ceranium, which could move about at will. It could not survive in the atmosphere of Earth for very long and
 * would quickly become a pink powder.
 *
 * @author eccentric_nz
 */
public class TARDISPermissionsExHandler {

    private final TARDIS plugin;
    private final File permissionsFile;
    private final LinkedHashMap<String, List<String>> permgroups = new LinkedHashMap<>();
    private String group;

    public TARDISPermissionsExHandler(TARDIS plugin) {
        this.plugin = plugin;
        permissionsFile = new File(plugin.getDataFolder(), "permissions.txt");
    }

    public void addPerms(String player) {
        BufferedReader bufRdr = null;
        try {
            bufRdr = new BufferedReader(new FileReader(permissionsFile));
            String line;
            //read each line of text file
            while ((line = bufRdr.readLine()) != null) {
                if (line.charAt(0) == '#') {
                    group = line.substring(1).trim();
                    permgroups.put(group, new ArrayList<>());
                } else {
                    List<String> perms = permgroups.get(group);
                    perms.add(line.trim());
                }
            }
        } catch (IOException io) {
            plugin.debug("Could not read perms file. " + io.getMessage());
        } finally {
            if (bufRdr != null) {
                try {
                    bufRdr.close();
                } catch (IOException e) {
                    plugin.debug("Error closing perms reader! " + e.getMessage());
                }
            }
        }
        // get the default world
        String w = plugin.getServer().getWorlds().get(0).getName();
        // pex world <world> inherit <parentWorld> - make the TARDIS world inherit the main worlds permissions
        if (TARDISFloodgate.shouldReplacePrefix(player)) {
            plugin.getServer().dispatchCommand(plugin.getConsole(), "pex world " + TARDISFloodgate.getPlayerWorldName(player) + " inherit " + w);
        } else {
            plugin.getServer().dispatchCommand(plugin.getConsole(), "pex world " + "TARDIS_WORLD_" + player + " inherit " + w);
        }
        plugin.getServer().dispatchCommand(plugin.getConsole(), "pex reload");
    }
}
