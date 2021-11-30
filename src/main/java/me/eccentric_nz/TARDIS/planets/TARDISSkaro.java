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
package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISChecker;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

/**
 * The Time Vortex is the dimension through which all time travellers pass. The Vortex was built by the Time Lords as a
 * transdimensional spiral that connected all points in space and time.
 *
 * @author eccentric_nz
 */
public class TARDISSkaro {

    private final TARDIS plugin;

    public TARDISSkaro(TARDIS plugin) {
        this.plugin = plugin;
    }

    public World loadDalekWorld() {
        String s_world = plugin.getServer().getWorlds().get(0).getName();
        // copy datapack files
        if (!TARDISChecker.hasDimension("skaro")) {
            plugin.getServer().reloadData();
            // message console to restart server
            plugin.getLogger().log(Level.INFO, ChatColor.RED + "Skaro data pack has been installed, please restart the server to enable the world.");
            // get default server world
            // add world to config
            if (!plugin.getPlanetsConfig().getBoolean("planets." + s_world + "_tardis_skaro.time_travel")) {
                plugin.getPlanetsConfig().set("planets." + s_world + "_tardis_skaro.time_travel", true);
                plugin.savePlanetsConfig();
                // make sure TARDISWeepingAngels can re-disguise Daleks in the Skaro world
                Plugin twa = plugin.getPM().getPlugin("TARDISWeepingAngels");
                if (twa != null) {
                    twa.getConfig().set("daleks.worlds.Skaro", 500);
                    twa.saveConfig();
                }
            }
            return null;
        } else {
            return plugin.getServer().getWorld(s_world + "_tardis_skaro");
        }
    }
}
