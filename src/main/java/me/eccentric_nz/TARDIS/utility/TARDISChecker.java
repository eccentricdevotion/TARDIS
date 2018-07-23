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
package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.ADVANCEMENT;
import org.bukkit.ChatColor;

import java.io.*;

/**
 * @author eccentric_nz
 */
public class TARDISChecker {

    private final TARDIS plugin;

    public TARDISChecker(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void checkMapsAndAdvancements() {
        // get server's main world folder
        // is there a worlds container?
        File container = plugin.getServer().getWorldContainer();
        String s_world = plugin.getServer().getWorlds().get(0).getName();
        String dataRoot = container.getAbsolutePath() + File.separator + s_world + File.separator + "data" + File.separator;
        for (int i = 1963; i < 1984; i++) {
            String map = "map_" + i + ".dat";
            File file = new File(dataRoot, map);
            if (!file.exists()) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + ChatColor.RED + String.format(plugin.getLanguage().getString("MAP_NOT_FOUND"), map));
                plugin.getConsole().sendMessage(plugin.getPluginName() + String.format(plugin.getLanguage().getString("MAP_COPYING"), map));
                copy(map, file);
            }
        }
        // check if directories exist
        String dataPacksRoot = container.getAbsolutePath() + File.separator + s_world + File.separator + "datapacks" + File.separator + "tardis" + File.separator + "data" + File.separator + "tardis" + File.separator + "advancements";
        File tardisDir = new File(dataPacksRoot);
        if (!tardisDir.exists()) {
            plugin.getConsole().sendMessage(plugin.getPluginName() + plugin.getLanguage().getString("ADVANCEMENT_DIRECTORIES"));
            tardisDir.mkdirs();
        }
        for (ADVANCEMENT advancement : ADVANCEMENT.values()) {
            String json = advancement.getConfigName() + ".json";
            File jfile = new File(dataPacksRoot, json);
            if (!jfile.exists()) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + ChatColor.RED + String.format(plugin.getLanguage().getString("ADVANCEMENT_NOT_FOUND"), json));
                plugin.getConsole().sendMessage(plugin.getPluginName() + String.format(plugin.getLanguage().getString("ADVANCEMENT_COPYING"), json));
                copy(json, jfile);
            }
        }
        String dataPacksMeta = container.getAbsolutePath() + File.separator + s_world + File.separator + "datapacks" + File.separator + "tardis";
        File mcmeta = new File(dataPacksMeta, "pack.mcmeta");
        if (!mcmeta.exists()) {
            plugin.getConsole().sendMessage(plugin.getPluginName() + ChatColor.RED + String.format(plugin.getLanguage().getString("ADVANCEMENT_NOT_FOUND"), "pack.mcmeta"));
            copy("pack.mcmeta", mcmeta);
        }
    }

    private void copy(String filename, File file) {
        InputStream in = null;
        try {
            in = plugin.getResource(filename);
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            try {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } catch (IOException io) {
                System.err.println(plugin.getPluginName() + "Could not save the file (" + file.toString() + ").");
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    System.err.println(plugin.getPluginName() + "Could not close the output stream.");
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println(plugin.getPluginName() + "File not found.");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.err.println(plugin.getPluginName() + "Could not close the input stream.");
                }
            }
        }
    }
}
