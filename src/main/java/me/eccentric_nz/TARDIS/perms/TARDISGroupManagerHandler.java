/*
 * Copyright (C) 2021 eccentric_nz
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The Manager was a secret member of the rebels who pretended to be serving the Daleks in an alternate 22nd century
 * England. While pretending to be interrogating the Third Doctor, he whispered to him that he was on his side.
 *
 * @author eccentric_nz
 */
public class TARDISGroupManagerHandler {

    private final TARDIS plugin;
    private final File permissionsFile;
    private final LinkedHashMap<String, List<String>> permgroups = new LinkedHashMap<>();
    private String group;

    public TARDISGroupManagerHandler(TARDIS plugin) {
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
        plugin.getServer().dispatchCommand(plugin.getConsole(), "manselect TARDIS_WORLD_" + player);
        int i = 0;
        for (Map.Entry<String, List<String>> entry : permgroups.entrySet()) {
            String grpstr = entry.getKey();
            List<String> perms = entry.getValue();
            plugin.getServer().dispatchCommand(plugin.getConsole(), "mangadd " + grpstr);
            perms.forEach((p) -> plugin.getServer().dispatchCommand(plugin.getConsole(), "mangaddp " + grpstr + " " + p));
            if (i == 0) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "manuadd " + player + " " + grpstr);
            }
            i++;
        }
        plugin.getServer().dispatchCommand(plugin.getConsole(), "mansave");
    }
}
