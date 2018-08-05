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
import me.eccentric_nz.TARDIS.enumeration.WORLD_MANAGER;
import me.eccentric_nz.TARDIS.files.TARDISFileCopier;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.CommandException;
import org.bukkit.plugin.Plugin;

import java.io.File;
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

    public void createDalekWorld() {
        // check to see if TerrainControl or OpenTerrainGenerator is enabled
        if (!plugin.getPM().isPluginEnabled("TerrainControl") && !plugin.getPM().isPluginEnabled("OpenTerrainGenerator")) {
            plugin.getServer().getLogger().log(Level.SEVERE, "A custom terrain plugin is not enabled! Please install TerrainControl or OpenTerrainGenerator and restart the server.");
            return;
        }
        String which = (plugin.getPM().isPluginEnabled("TerrainControl")) ? "TerrainControl" : "OpenTerrainGenerator";
        Plugin tc = plugin.getPM().getPlugin(which);
        // copy world config files to TerrainControl / OpenTerrainGenerator data folder
        try {
            File worlds_dir = new File(tc.getDataFolder(), "worlds" + File.separator + "Skaro");
            worlds_dir.mkdirs();
            // copy WorldConfig.ini file to Skaro folder
            TARDISFileCopier.copy(worlds_dir.getAbsolutePath() + File.separator + "WorldConfig.ini", plugin.getResource("WorldConfig.ini"), true, plugin.getPluginName());
            // copy biome files to Skaro WorldBiomes folder
            File biomes_dir = new File(worlds_dir, File.separator + "WorldBiomes");
            biomes_dir.mkdirs();
            String biomes_base_path = biomes_dir.getAbsolutePath() + File.separator;
            TARDISFileCopier.copy(biomes_base_path + "Desert.bc", plugin.getResource("Desert.bc"), true, plugin.getPluginName());
            TARDISFileCopier.copy(biomes_base_path + "DesertHills.bc", plugin.getResource("DesertHills.bc"), true, plugin.getPluginName());
            TARDISFileCopier.copy(biomes_base_path + "Mesa (Bryce).bc", plugin.getResource("Mesa (Bryce).bc"), true, plugin.getPluginName());
            TARDISFileCopier.copy(biomes_base_path + "Mesa Plateau F M.bc", plugin.getResource("Mesa Plateau F M.bc"), true, plugin.getPluginName());
            TARDISFileCopier.copy(biomes_base_path + "Mesa Plateau F.bc", plugin.getResource("Mesa Plateau F.bc"), true, plugin.getPluginName());
            TARDISFileCopier.copy(biomes_base_path + "Mesa Plateau M.bc", plugin.getResource("Mesa Plateau M.bc"), true, plugin.getPluginName());
            TARDISFileCopier.copy(biomes_base_path + "Mesa Plateau.bc", plugin.getResource("Mesa Plateau.bc"), true, plugin.getPluginName());
            TARDISFileCopier.copy(biomes_base_path + "Mesa.bc", plugin.getResource("Mesa.bc"), true, plugin.getPluginName());
            // copy bo3 files to Skaro WorldObjects folder
            File objects_dir = new File(worlds_dir, File.separator + "WorldObjects");
            objects_dir.mkdirs();
            String objects_base_path = objects_dir.getAbsolutePath() + File.separator;
            TARDISFileCopier.copy(objects_base_path + "dalek1.bo3", plugin.getResource("dalek1.bo3"), true, plugin.getPluginName());
            TARDISFileCopier.copy(objects_base_path + "dalek2.bo3", plugin.getResource("dalek2.bo3"), true, plugin.getPluginName());
            // copy nbt files to WorldObjects/NBT folder
            File nbt_dir = new File(objects_dir, File.separator + "NBT");
            nbt_dir.mkdirs();
            String nbt_base_path = nbt_dir.getAbsolutePath() + File.separator;
            TARDISFileCopier.copy(nbt_base_path + "MobSpawner.nbt", plugin.getResource("MobSpawner.nbt"), true, plugin.getPluginName());
            for (int l = 1; l < 16; l++) {
                TARDISFileCopier.copy(nbt_base_path + "Basic-" + l + ".nbt", plugin.getResource("Basic-" + l + ".nbt"), true, plugin.getPluginName());
                TARDISFileCopier.copy(nbt_base_path + "BasicE-" + l + ".nbt", plugin.getResource("BasicE-" + l + ".nbt"), true, plugin.getPluginName());
                TARDISFileCopier.copy(nbt_base_path + "Rare-" + l + ".nbt", plugin.getResource("Rare-" + l + ".nbt"), true, plugin.getPluginName());
                TARDISFileCopier.copy(nbt_base_path + "RareE-" + l + ".nbt", plugin.getResource("RareE-" + l + ".nbt"), true, plugin.getPluginName());
            }
            if (plugin.getWorldManager().equals(WORLD_MANAGER.MULTIWORLD)) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mw create Skaro plugin:" + which);
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mw load Skaro");
            } else if (plugin.getWorldManager().equals(WORLD_MANAGER.MULTIVERSE)) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv create Skaro NORMAL -g " + which + " -t NORMAL");
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv modify set portalform none Skaro");
            } else {
                WorldCreator.name("Skaro").type(WorldType.NORMAL).environment(Environment.NORMAL).generator(which).createWorld();
            }
            if (plugin.getWorldManager().equals(WORLD_MANAGER.MYWORLDS)) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "myworlds load Skaro:" + which);
                plugin.getServer().dispatchCommand(plugin.getConsole(), "world config save");
            }
            // add world to config
            plugin.getConfig().set("worlds.Skaro", true);
            plugin.saveConfig();
            // make sure TARDISWeepingAngels can re-disguise Daleks in the Skaro world
            Plugin twa = plugin.getPM().getPlugin("TARDISWeepingAngels");
            if (twa != null) {
                twa.getConfig().set("daleks.worlds.Skaro", 500);
                twa.saveConfig();
            }
        } catch (CommandException e) {
            plugin.getServer().getLogger().log(Level.SEVERE, "Could not copy Skaro files to " + which + " plugin data folder: {0}", e.getMessage());
        }
    }
}
