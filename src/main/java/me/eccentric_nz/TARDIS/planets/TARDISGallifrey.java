/*
 * Copyright (C) 2018 eccentric_nz
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
import me.eccentric_nz.TARDIS.files.TARDISFileCopier;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.CommandException;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.logging.Level;

/**
 * @author eccentric_nz
 * <p>
 * Gallifrey is the homeworld of the Time Lords. It is believed to have been destroyed in the Last Great Time War but
 * was later discovered to be frozen in a pocket universe by the first thirteen incarnations of the Doctor, surviving
 * the Time War. The literal translation of Gallifrey is "They that walk in the shadows"
 */
public class TARDISGallifrey {

    private final TARDIS plugin;

    public TARDISGallifrey(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void createTimeLordWorld() {
        // check to see if TerrainControl or OpenTerrainGenerator is enabled
        if (!plugin.getPM().isPluginEnabled("TerrainControl") && !plugin.getPM().isPluginEnabled("OpenTerrainGenerator")) {
            plugin.getServer().getLogger().log(Level.SEVERE, "A custom terrain plugin is not enabled! Please install TerrainControl or OpenTerrainGenerator and restart the server.");
            return;
        }
        String which = (plugin.getPM().isPluginEnabled("TerrainControl")) ? "TerrainControl" : "OpenTerrainGenerator";
        Plugin tc = plugin.getPM().getPlugin(which);
        // copy world config files to TerrainControl / OpenTerrainGenerator data folder
        try {
            File worlds_dir = new File(tc.getDataFolder(), "worlds" + File.separator + "Gallifrey");
            worlds_dir.mkdirs();
            // copy WorldConfig.ini file to Gallifrey folder
            TARDISFileCopier.copy(worlds_dir.getAbsolutePath() + File.separator + "WorldConfig.ini", plugin.getResource("GallifreyWorldConfig.ini"), true, plugin.getPluginName());
            // copy Caves biome file to Gallifrey WorldBiomes folder
            File biomes_dir = new File(worlds_dir, File.separator + "WorldBiomes");
            biomes_dir.mkdirs();
            String biomes_base_path = biomes_dir.getAbsolutePath() + File.separator;
            TARDISFileCopier.copy(biomes_base_path + "Desert M.bc", plugin.getResource("GallifreyDesert M.bc"), true, plugin.getPluginName());
            TARDISFileCopier.copy(biomes_base_path + "Desert.bc", plugin.getResource("GallifreyDesert.bc"), true, plugin.getPluginName());
            TARDISFileCopier.copy(biomes_base_path + "DesertHills.bc", plugin.getResource("GallifreyDesertHills.bc"), true, plugin.getPluginName());
            TARDISFileCopier.copy(biomes_base_path + "Gallifrey.bc", plugin.getResource("Gallifrey.bc"), true, plugin.getPluginName());
            // copy bo3 files to Gallifrey WorldObjects folder
            File objects_dir = new File(worlds_dir, File.separator + "WorldObjects");
            objects_dir.mkdirs();
            // copy nbt files to WorldObjects/NBT folder
            File nbt_dir = new File(objects_dir, File.separator + "GNBT");
            nbt_dir.mkdirs();
            String nbt_base_path = nbt_dir.getAbsolutePath() + File.separator;
            for (int l = 1; l < 16; l++) {
                TARDISFileCopier.copy(nbt_base_path + "Basic-" + l + ".nbt", plugin.getResource("Basic-" + l + ".nbt"), true, plugin.getPluginName());
                TARDISFileCopier.copy(nbt_base_path + "BasicE-" + l + ".nbt", plugin.getResource("BasicE-" + l + ".nbt"), true, plugin.getPluginName());
                TARDISFileCopier.copy(nbt_base_path + "Rare-" + l + ".nbt", plugin.getResource("Rare-" + l + ".nbt"), true, plugin.getPluginName());
                TARDISFileCopier.copy(nbt_base_path + "RareE-" + l + ".nbt", plugin.getResource("RareE-" + l + ".nbt"), true, plugin.getPluginName());
            }
            if (plugin.getPM().isPluginEnabled("MultiWorld")) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mw create Gallifrey plugin:" + which);
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mw load Gallifrey");
            } else if (plugin.isMVOnServer()) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv create Gallifrey NORMAL -g " + which + " -t NORMAL");
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv modify set portalform none Gallifrey");
            } else {
                WorldCreator.name("Gallifrey").type(WorldType.NORMAL).environment(Environment.NORMAL).generator(which).createWorld();
            }
            if (plugin.getPM().isPluginEnabled("My_Worlds")) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "myworlds load Gallifrey:" + which);
                plugin.getServer().dispatchCommand(plugin.getConsole(), "world config save");
            }
            // add world to config
            plugin.getConfig().set("worlds.Gallifrey", true);
            plugin.saveConfig();
        } catch (CommandException e) {
            plugin.getServer().getLogger().log(Level.SEVERE, "Could not copy Gallifrey files to " + which + " plugin data folder: {0}", e.getMessage());
        }
    }
}
