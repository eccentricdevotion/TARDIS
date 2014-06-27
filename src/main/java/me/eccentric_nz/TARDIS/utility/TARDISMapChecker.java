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
package me.eccentric_nz.TARDIS.utility;

import java.io.File;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISMapChecker {

    private final TARDIS plugin;

    public TARDISMapChecker(final TARDIS plugin) {
        this.plugin = plugin;
    }

    public void checkMaps() {
        // get server's main world folder
        // is there a worlds container?
        File container = plugin.getServer().getWorldContainer();
        String s_world = plugin.getServer().getWorlds().get(0).getName();
        // check for MCPC+
        Pattern pat = Pattern.compile("MCPC", Pattern.DOTALL);
        Matcher mat = pat.matcher(plugin.getServer().getVersion());
        String server_world;
        if (mat.find()) {
            server_world = "data" + File.separator;
        } else {
            server_world = s_world + File.separator + "data" + File.separator;
        }
        String map = "map_1979.dat";
        String root = container.getAbsolutePath() + File.separator + server_world;
        File file = new File(root, map);
        if (!file.exists()) {
            plugin.getConsole().sendMessage(plugin.getPluginName() + ChatColor.RED + plugin.getLanguage().getString("MAPS_NOT_FOUND"));
            plugin.getConsole().sendMessage(plugin.getPluginName() + "Copying map files to the TARDIS folder...");
            plugin.getTardisCopier().copy(map);
            plugin.getTardisCopier().copy("map_1963.dat");
            plugin.getTardisCopier().copy("map_1964.dat");
            plugin.getTardisCopier().copy("map_1965.dat");
            plugin.getTardisCopier().copy("map_1966.dat");
            plugin.getTardisCopier().copy("map_1967.dat");
            plugin.getTardisCopier().copy("map_1968.dat");
            plugin.getTardisCopier().copy("map_1969.dat");
            plugin.getTardisCopier().copy("map_1970.dat");
            plugin.getTardisCopier().copy("map_1971.dat");
            plugin.getTardisCopier().copy("map_1972.dat");
            plugin.getTardisCopier().copy("map_1973.dat");
            plugin.getTardisCopier().copy("map_1974.dat");
            plugin.getTardisCopier().copy("map_1975.dat");
            plugin.getTardisCopier().copy("map_1976.dat");
            plugin.getTardisCopier().copy("map_1977.dat");
            plugin.getTardisCopier().copy("map_1978.dat");
            plugin.getTardisCopier().copy("map_1979.dat");
            plugin.getConsole().sendMessage(plugin.getPluginName() + "Please move the new map files to the main world [" + s_world + "] data folder.");
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    Set<OfflinePlayer> ops = plugin.getServer().getOperators();
                    for (OfflinePlayer olp : ops) {
                        if (olp.isOnline()) {
                            Player p = (Player) olp;
                            TARDISMessage.send(p, "MAPS_NOT_FOUND");
                        }
                    }
                }
            }, 200L);
        }
    }
}
