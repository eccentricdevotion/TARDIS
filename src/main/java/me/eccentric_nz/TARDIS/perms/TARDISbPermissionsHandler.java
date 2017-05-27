/*
 * Copyright (C) 2016 eccentric_nz
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;

/**
 * The Fourth Doctor was rather fond of bees. Romana compared K9's greeting to a
 * computer to the identification dance used by bees. When asked what bees were,
 * the Fourth Doctor replied "Insects! With stings in their tails".
 *
 * @author eccentric_nz
 */
public class TARDISbPermissionsHandler {

    private final TARDIS plugin;
    private File permissionsFile = null;
    LinkedHashMap<String, List<String>> permgroups = new LinkedHashMap<>();
    String group;

    public TARDISbPermissionsHandler(TARDIS plugin) {
        this.plugin = plugin;
        this.permissionsFile = new File(plugin.getDataFolder(), "permissions.txt");
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
        plugin.getServer().dispatchCommand(plugin.getConsole(), "world TARDIS_WORLD_" + player);
        int i = 0;
        for (Map.Entry<String, List<String>> entry : permgroups.entrySet()) {
            String grpstr = entry.getKey();
            List<String> perms = entry.getValue();
            plugin.getServer().dispatchCommand(plugin.getConsole(), "group " + grpstr);
            perms.forEach((p) -> {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "group addperm " + p);
            });
            if (i == 0) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "user " + player);
                plugin.getServer().dispatchCommand(plugin.getConsole(), "user setgroup " + grpstr);
            }
            i++;
        }
        plugin.getServer().dispatchCommand(plugin.getConsole(), "permissions save");
        plugin.getServer().dispatchCommand(plugin.getConsole(), "permissions reload");
    }
}
