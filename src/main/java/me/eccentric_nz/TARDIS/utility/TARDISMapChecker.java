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
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.files.TARDISMakeTardisCSV;
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
        String server_world = s_world + File.separator + "data" + File.separator;
        String map = "map_1973.dat";
        String root = container.getAbsolutePath() + File.separator + server_world;
        File file = new File(root, map);
        if (!file.exists()) {
            String map1 = "map_1963.dat";
            String map2 = "map_1964.dat";
            String map3 = "map_1965.dat";
            String map4 = "map_1967.dat";
            String map5 = "map_1968.dat";
            String map6 = "map_1969.dat";
            String map7 = "map_1970.dat";
            String map8 = "map_1971.dat";
            String map9 = "map_1972.dat";
            String map10 = "map_1973.dat";
            String map11 = "map_1974.dat";
            String map12 = "map_1975.dat";
            String map13 = "map_1976.dat";
            String map14 = "map_1977.dat";
            String map15 = "map_1978.dat";
            plugin.getConsole().sendMessage(plugin.getPluginName() + ChatColor.RED + "Could not find TARDIS map files, some recipes will not work!");
            plugin.getConsole().sendMessage(plugin.getPluginName() + "Copying map files to the TARDIS folder...");
            TARDISMakeTardisCSV copier = new TARDISMakeTardisCSV(plugin);
            copier.copy(plugin.getDataFolder() + File.separator + map, plugin.getResource(map));
            copier.copy(plugin.getDataFolder() + File.separator + map1, plugin.getResource(map1));
            copier.copy(plugin.getDataFolder() + File.separator + map2, plugin.getResource(map2));
            copier.copy(plugin.getDataFolder() + File.separator + map3, plugin.getResource(map3));
            copier.copy(plugin.getDataFolder() + File.separator + map4, plugin.getResource(map4));
            copier.copy(plugin.getDataFolder() + File.separator + map5, plugin.getResource(map5));
            copier.copy(plugin.getDataFolder() + File.separator + map6, plugin.getResource(map6));
            copier.copy(plugin.getDataFolder() + File.separator + map7, plugin.getResource(map7));
            copier.copy(plugin.getDataFolder() + File.separator + map8, plugin.getResource(map8));
            copier.copy(plugin.getDataFolder() + File.separator + map9, plugin.getResource(map9));
            copier.copy(plugin.getDataFolder() + File.separator + map10, plugin.getResource(map10));
            copier.copy(plugin.getDataFolder() + File.separator + map11, plugin.getResource(map11));
            copier.copy(plugin.getDataFolder() + File.separator + map12, plugin.getResource(map12));
            copier.copy(plugin.getDataFolder() + File.separator + map13, plugin.getResource(map13));
            copier.copy(plugin.getDataFolder() + File.separator + map14, plugin.getResource(map14));
            copier.copy(plugin.getDataFolder() + File.separator + map15, plugin.getResource(map15));
            plugin.getConsole().sendMessage(plugin.getPluginName() + "Please move the new map files to the main world [" + s_world + "] data folder.");
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    Set<OfflinePlayer> ops = plugin.getServer().getOperators();
                    for (OfflinePlayer olp : ops) {
                        if (olp.isOnline()) {
                            Player p = (Player) olp;
                            p.sendMessage(plugin.getPluginName() + ChatColor.RED + "Could not find TARDIS map files, some recipes will not work!");
                        }
                    }
                }
            }, 200L);
        }
    }
}
