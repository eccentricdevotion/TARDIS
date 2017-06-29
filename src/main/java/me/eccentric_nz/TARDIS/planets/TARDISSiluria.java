/*
 * Copyright (C) 2017 eccentric_nz
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

import java.io.File;
import java.util.logging.Level;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.files.TARDISFileCopier;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.CommandException;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSiluria {

    private final TARDIS plugin;

    public TARDISSiluria(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void createSilurianUnderworld() {
        // check to see if TerrainControl is enabled
        if (!plugin.getPM().isPluginEnabled("TerrainControl")) {
            plugin.getServer().getLogger().log(Level.SEVERE, "TerrainControl plugin is not enabled! Please install TerrainControl and restart the server.");
            return;
        }
        // copy world config file to TerrainControl data folder
        Plugin tc = plugin.getPM().getPlugin("TerrainControl");
        try {
            File worlds_dir = new File(tc.getDataFolder(), "worlds" + File.separator + "Siluria");
            worlds_dir.mkdirs();
            // copy WorldConfig.ini file to Siluria folder
            TARDISFileCopier.copy(worlds_dir.getAbsolutePath() + File.separator + "WorldConfig.ini", plugin.getResource("SiluriaWorldConfig.ini"), true, plugin.getPluginName());
            // copy Caves biome file to Siluria WorldBiomes folder
            File biomes_dir = new File(worlds_dir, File.separator + "WorldBiomes");
            biomes_dir.mkdirs();
            String biomes_base_path = biomes_dir.getAbsolutePath() + File.separator;
            TARDISFileCopier.copy(biomes_base_path + "Caves.bc", plugin.getResource("Caves.bc"), true, plugin.getPluginName());
            // copy bo3 files to Siluria WorldObjects folder
            File objects_dir = new File(worlds_dir, File.separator + "WorldObjects");
            objects_dir.mkdirs();
            String objects_base_path = objects_dir.getAbsolutePath() + File.separator;
            TARDISFileCopier.copy(objects_base_path + "siluriabuilding.bo3", plugin.getResource("siluriabuilding.bo3"), true, plugin.getPluginName());
            TARDISFileCopier.copy(objects_base_path + "siluriapath_EW.bo3", plugin.getResource("siluriapath_EW.bo3"), true, plugin.getPluginName());
            TARDISFileCopier.copy(objects_base_path + "siluriapath_NS.bo3", plugin.getResource("siluriapath_NS.bo3"), true, plugin.getPluginName());
            TARDISFileCopier.copy(objects_base_path + "siluriapath_cross.bo3", plugin.getResource("siluriapath_cross.bo3"), true, plugin.getPluginName());
            // copy nbt files to WorldObjects/NBT folder
            File nbt_dir = new File(objects_dir, File.separator + "SNBT");
            nbt_dir.mkdirs();
            String nbt_base_path = nbt_dir.getAbsolutePath() + File.separator;
            TARDISFileCopier.copy(nbt_base_path + "MobSpawner.nbt", plugin.getResource("SiluriaMobSpawner.nbt"), true, plugin.getPluginName());
            for (int l = 1; l < 16; l++) {
                TARDISFileCopier.copy(nbt_base_path + "Basic-" + l + ".nbt", plugin.getResource("Basic-" + l + ".nbt"), true, plugin.getPluginName());
                TARDISFileCopier.copy(nbt_base_path + "BasicE-" + l + ".nbt", plugin.getResource("BasicE-" + l + ".nbt"), true, plugin.getPluginName());
                TARDISFileCopier.copy(nbt_base_path + "Rare-" + l + ".nbt", plugin.getResource("Rare-" + l + ".nbt"), true, plugin.getPluginName());
                TARDISFileCopier.copy(nbt_base_path + "RareE-" + l + ".nbt", plugin.getResource("RareE-" + l + ".nbt"), true, plugin.getPluginName());
            }
            if (plugin.getPM().isPluginEnabled("MultiWorld")) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mw create Siluria plugin:TerrainControl");
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mw load Siluria");
            } else if (plugin.isMVOnServer()) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv create Siluria NETHER -g TerrainControl -t NORMAL");
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv modify set portalform none Siluria");
            } else {
                WorldCreator.name("Siluria").type(WorldType.NORMAL).environment(World.Environment.NETHER).createWorld();
            }
            if (plugin.getPM().isPluginEnabled("My Worlds")) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "myworlds load Siluria:TerrainControl");
                plugin.getServer().dispatchCommand(plugin.getConsole(), "world config save");
            }
            // add world to config
            plugin.getConfig().set("worlds.Siluria", true);
            plugin.saveConfig();
            // make sure TARDISWeepingAngels can re-disguise Silurians in the Siluria world
            Plugin twa = plugin.getPM().getPlugin("TARDISWeepingAngels");
            if (twa != null) {
                twa.getConfig().set("silurians.worlds.Siluria", 500);
                twa.saveConfig();
            }
        } catch (CommandException e) {
            plugin.getServer().getLogger().log(Level.SEVERE, "Could not copy Siluria files to TerrainControl plugin data folder: {0}", e.getMessage());
        }
    }
}
