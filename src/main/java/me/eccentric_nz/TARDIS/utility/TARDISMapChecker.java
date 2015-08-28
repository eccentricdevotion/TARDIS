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
        String root = container.getAbsolutePath() + File.separator + server_world;
        boolean opwarn = false;
        for (int i = 1963; i < 1983; i++) {
            String map = "map_" + i + ".dat";
            File file = new File(root, map);
            if (!file.exists()) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + ChatColor.RED + String.format(plugin.getLanguage().getString("MAP_NOT_FOUND"), map));
                plugin.getConsole().sendMessage(plugin.getPluginName() + String.format(plugin.getLanguage().getString("MAP_COPYING"), map));
                plugin.getTardisCopier().copy(map);
                plugin.getConsole().sendMessage(plugin.getPluginName() + String.format(plugin.getLanguage().getString("MAP_WORLD"), s_world));
                opwarn = true;
            }
        }
        if (opwarn) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    Set<OfflinePlayer> ops = plugin.getServer().getOperators();
                    for (OfflinePlayer olp : ops) {
                        if (olp.isOnline()) {
                            Player p = olp.getPlayer();
                            TARDISMessage.send(p, "MAPS_NOT_FOUND");
                        }
                    }
                }
            }, 200L);
        }
    }
}
