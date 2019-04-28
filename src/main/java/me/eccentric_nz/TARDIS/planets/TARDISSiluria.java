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
 */
public class TARDISSiluria {

    private final TARDIS plugin;

    public TARDISSiluria(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void createSilurianUnderworld() {
        // check to see if TerrainControl or OpenTerrainGenerator is enabled
        if (!plugin.getPM().isPluginEnabled("TerrainControl") && !plugin.getPM().isPluginEnabled("OpenTerrainGenerator")) {
            plugin.getServer().getLogger().log(Level.SEVERE, "A custom terrain plugin is not enabled! Please install TerrainControl or OpenTerrainGenerator and restart the server.");
            return;
        }
        String which = (plugin.getPM().isPluginEnabled("TerrainControl")) ? "TerrainControl" : "OpenTerrainGenerator";
        Plugin tc = plugin.getPM().getPlugin(which);
        // copy world config files to TerrainControl / OpenTerrainGenerator data folder
        try {
            File worlds_dir = new File(tc.getDataFolder(), "worlds" + File.separator + "Siluria");
            worlds_dir.mkdirs();
            // copy WorldConfig.ini file to Siluria folder
            TARDISFileCopier.copy(worlds_dir.getAbsolutePath() + File.separator + "WorldConfig.ini", plugin.getResource("SiluriaWorldConfig.ini"), true);
            // copy Caves biome file to Siluria WorldBiomes folder
            File biomes_dir = new File(worlds_dir, File.separator + "WorldBiomes");
            biomes_dir.mkdirs();
            String biomes_base_path = biomes_dir.getAbsolutePath() + File.separator;
            TARDISFileCopier.copy(biomes_base_path + "Caves.bc", plugin.getResource("Caves.bc"), true);
            // copy bo3 files to Siluria WorldObjects folder
            File objects_dir = new File(worlds_dir, File.separator + "WorldObjects");
            objects_dir.mkdirs();
            String objects_base_path = objects_dir.getAbsolutePath() + File.separator;
            TARDISFileCopier.copy(objects_base_path + "siluriabuilding.bo3", plugin.getResource("siluriabuilding.bo3"), true);
            TARDISFileCopier.copy(objects_base_path + "siluriapath_EW.bo3", plugin.getResource("siluriapath_EW.bo3"), true);
            TARDISFileCopier.copy(objects_base_path + "siluriapath_NS.bo3", plugin.getResource("siluriapath_NS.bo3"), true);
            TARDISFileCopier.copy(objects_base_path + "siluriapath_cross.bo3", plugin.getResource("siluriapath_cross.bo3"), true);
            // copy nbt files to WorldObjects/NBT folder
            File nbt_dir = new File(objects_dir, File.separator + "SNBT");
            nbt_dir.mkdirs();
            String nbt_base_path = nbt_dir.getAbsolutePath() + File.separator;
            TARDISFileCopier.copy(nbt_base_path + "MobSpawner.nbt", plugin.getResource("SiluriaMobSpawner.nbt"), true);
            for (int l = 1; l < 16; l++) {
                TARDISFileCopier.copy(nbt_base_path + "Basic-" + l + ".nbt", plugin.getResource("Basic-" + l + ".nbt"), true);
                TARDISFileCopier.copy(nbt_base_path + "BasicE-" + l + ".nbt", plugin.getResource("BasicE-" + l + ".nbt"), true);
                TARDISFileCopier.copy(nbt_base_path + "Rare-" + l + ".nbt", plugin.getResource("Rare-" + l + ".nbt"), true);
                TARDISFileCopier.copy(nbt_base_path + "RareE-" + l + ".nbt", plugin.getResource("RareE-" + l + ".nbt"), true);
            }
//            if (plugin.getWorldManager().equals(WORLD_MANAGER.MULTIWORLD)) {
//                plugin.getServer().dispatchCommand(plugin.getConsole(), "mw create Siluria plugin:" + which);
//                plugin.getServer().dispatchCommand(plugin.getConsole(), "mw load Siluria");
//            } else if (plugin.getWorldManager().equals(WORLD_MANAGER.MULTIVERSE)) {
//                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv create Siluria NETHER -g " + which + " -t NORMAL");
//                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv modify set portalform none Siluria");
//            } else {
            WorldCreator.name("Siluria").type(WorldType.NORMAL).environment(Environment.NETHER).generator(which).createWorld();
//            }
//            if (plugin.getWorldManager().equals(WORLD_MANAGER.MYWORLDS)) {
//                plugin.getServer().dispatchCommand(plugin.getConsole(), "myworlds load Siluria:" + which);
//                plugin.getServer().dispatchCommand(plugin.getConsole(), "world config save");
//            }
            // add world to config
            plugin.getConfig().set("worlds.Siluria", true);
            plugin.saveConfig();
        } catch (CommandException e) {
            plugin.getServer().getLogger().log(Level.SEVERE, "Could not copy Siluria files to " + which + " plugin data folder: {0}", e.getMessage());
        }
    }
}
