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
package me.eccentric_nz.TARDIS.skaro;

import java.io.File;
import java.util.logging.Level;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.files.TARDISFileCopier;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.plugin.Plugin;

/**
 * The Time Vortex is the dimension through which all time travellers pass. The
 * Vortex was built by the Time Lords as a transdimensional spiral that
 * connected all points in space and time.
 *
 * @author eccentric_nz
 */
public class TARDISSkaro {

    private final TARDIS plugin;
    public World skaroWorld = null;

    public TARDISSkaro(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void createDalekWorld() {
        // check to see if TerrainControl is enabled
        if (!plugin.getPM().isPluginEnabled("TerrainControl")) {
            plugin.getServer().getLogger().log(Level.SEVERE, "TerrainControl plugin is not enabled! Please install TerrainControl and restart the server.");
            return;
        }
        // copy world config file to TerrainControl data folder
        Plugin tc = plugin.getPM().getPlugin("TerrainControl");
        try {
            File worlds_dir = new File(tc.getDataFolder(), "worlds" + File.separator + "Skaro");
            worlds_dir.mkdir();
            TARDISFileCopier.copy(worlds_dir.getAbsolutePath() + File.separator + "WorldConfig.ini", plugin.getResource("WorldConfig.ini"), true, plugin.getPluginName());
            // copy bo3 files to Skaro WorldObjects folder
            File objects_dir = new File(worlds_dir, File.separator + "WorldObjects");
            objects_dir.mkdir();
            String base_path = objects_dir.getAbsolutePath();
            TARDISFileCopier.copy(base_path + File.separator + "police_box.bo3", plugin.getResource("police_box.bo3"), true, plugin.getPluginName());
            if (plugin.getPM().isPluginEnabled("MultiWorld")) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mw create Skaro plugin:TerrainControl");
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mw load Skaro");
            } else if (plugin.isMVOnServer()) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv create Skaro NORMAL -g TerrainControl -t NORMAL");
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv modify set portalform none Skaro");
            } else {
                WorldCreator.name("Skaro").type(WorldType.NORMAL).environment(World.Environment.NORMAL).createWorld();
            }
            if (plugin.getPM().isPluginEnabled("My Worlds")) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "myworlds load Skaro:TerrainControl");
                plugin.getServer().dispatchCommand(plugin.getConsole(), "world config save");
            }
            // add world to config
            plugin.getConfig().set("worlds.Skaro", true);
            plugin.saveConfig();
        } catch (Exception e) {
            plugin.getServer().getLogger().log(Level.SEVERE, "Could not copy files to TerrainControl plugin data folder: {0}", e.getMessage());
        }
    }
}
