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
package me.eccentric_nz.TARDIS.perms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;

/**
 * The Manager was a secret member of the rebels who pretended to be serving the
 * Daleks in an alternate 22nd century England. While pretending to be
 * interrogating the Third Doctor, he whispered to him that he was on his side.
 *
 * @author eccentric_nz
 */
public class TARDISGroupManagerHandler {

    private final TARDIS plugin;
    private File permissionsFile = null;
    List<String> perms = new ArrayList<String>();
    String group;

    public TARDISGroupManagerHandler(TARDIS plugin) {
        this.plugin = plugin;
        this.permissionsFile = new File(plugin.getDataFolder(), "permissions.txt");
    }

    public void addPerms(String player) {
        BufferedReader bufRdr = null;
        int i = 0;
        try {
            bufRdr = new BufferedReader(new FileReader(permissionsFile));
            String line;
            //read each line of text file
            while ((line = bufRdr.readLine()) != null) {
                if (i == 0) {
                    group = line.trim();
                } else {
                    perms.add(line);
                }
                i++;
            }
        } catch (IOException io) {
            plugin.debug("Could not read perms file. " + io.getMessage());
        } finally {
            if (bufRdr != null) {
                try {
                    bufRdr.close();
                } catch (Exception e) {
                    plugin.debug("Error closing perms reader! " + e.getMessage());
                }
            }
        }
        plugin.getServer().dispatchCommand(plugin.console, "manselect TARDIS_WORLD_" + player);
        plugin.getServer().dispatchCommand(plugin.console, "mangadd " + group);
        for (String p : perms) {
            plugin.getServer().dispatchCommand(plugin.console, "mangaddp " + group + " " + p);
        }
        plugin.getServer().dispatchCommand(plugin.console, "manuadd " + player + " " + group);
        plugin.getServer().dispatchCommand(plugin.console, "mansave");
    }
}
