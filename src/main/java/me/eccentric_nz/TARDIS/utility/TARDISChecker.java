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
package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Advancement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;

/**
 * @author eccentric_nz
 */
public class TARDISChecker {

    private final TARDIS plugin;

    public TARDISChecker(TARDIS plugin) {
        this.plugin = plugin;
    }

    public static boolean hasDimension(String dimension) {
        boolean exists = true;
        File container = TARDIS.plugin.getServer().getWorldContainer();
        String s_world = TARDIS.plugin.getServer().getWorlds().get(0).getName();
        String dataPacksRoot = container.getAbsolutePath() + File.separator + s_world + File.separator + "datapacks" + File.separator;
        // check if directories exist
        String dimensionRoot = dataPacksRoot + dimension + File.separator + "data" + File.separator + "tardis" + File.separator;
        File dimensionDir = new File(dimensionRoot + "dimension");
        File dimensionTypeDir = new File(dimensionRoot + "dimension_type");
        File worldGenDir = new File(dimensionRoot + "worldgen");
        if (!dimensionDir.exists()) {
            dimensionDir.mkdirs();
        }
        if (!dimensionTypeDir.exists()) {
            dimensionTypeDir.mkdirs();
        }
        if (worldGenDir.exists()) {
            deleteDirectoryAndContents(worldGenDir.toPath());
        }
        // copy files to directory
        File dimFile = new File(dimensionDir, dimension + ".json");
        if (!dimFile.exists()) {
            exists = false;
            TARDISChecker.copy(dimension + "_d.json", dimFile);
        }
        File dimTypeFile = new File(dimensionTypeDir, dimension + ".json");
        if (!dimTypeFile.exists()) {
            exists = false;
            TARDISChecker.copy(dimension + "_dt.json", dimTypeFile);
        }
        String dataPacksMeta = dataPacksRoot + dimension;
        File mcmeta = new File(dataPacksMeta, "pack.mcmeta");
        if (!mcmeta.exists()) {
            exists = false;
            copy("pack_" + dimension + ".mcmeta", mcmeta);
        }
        return exists;
    }

    public static void updateDimension(String dimension) {
        File container = TARDIS.plugin.getServer().getWorldContainer();
        String s_world = TARDIS.plugin.getServer().getWorlds().get(0).getName();
        String dataPacksRoot = container.getAbsolutePath() + File.separator + s_world + File.separator + "datapacks" + File.separator;
        // check if directories exist
        String dimensionRoot = dataPacksRoot + dimension + File.separator + "data" + File.separator + "tardis" + File.separator;
        File dimensionDir = new File(dimensionRoot + "dimension");
        File worldGenDir = new File(dimensionRoot + "worldgen");
        if (worldGenDir.exists()) {
            deleteDirectoryAndContents(worldGenDir.toPath());
        }
        if (dimensionDir.exists()) {
            File dimensionTypeDir = new File(dimensionRoot + "dimension_type");
            // overwrite files
            File dimFile = new File(dimensionDir, dimension + ".json");
            TARDISChecker.copy(dimension + "_d.json", dimFile);
            File dimTypeFile = new File(dimensionTypeDir, dimension + ".json");
            TARDISChecker.copy(dimension + "_dt.json", dimTypeFile);
            File metaFile = new File(dataPacksRoot + dimension, "pack.mcmeta");
            TARDISChecker.copy("pack_" + dimension + ".mcmeta", metaFile);
        }
    }

    public static void copy(String filename, File file) {
        InputStream in = null;
        try {
            in = TARDIS.plugin.getResource(filename);
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            try {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } catch (IOException io) {
                Bukkit.getLogger().log(Level.SEVERE, "Checker: Could not save the file (" + file + ").");
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    Bukkit.getLogger().log(Level.SEVERE, "Checker: Could not close the output stream.");
                }
            }
        } catch (FileNotFoundException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Checker: File not found: " + filename);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Bukkit.getLogger().log(Level.SEVERE, "Checker: Could not close the input stream.");
                }
            }
        }
    }

    private static void deleteDirectoryAndContents(Path path) {
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            TARDIS.plugin.debug("Could not delete datapack worldgen directory! " + e.getMessage());
        }
    }

    public void checkAdvancements() {
        // get server's main world folder
        // is there a worlds container?
        File container = plugin.getServer().getWorldContainer();
        String s_world = plugin.getServer().getWorlds().get(0).getName();
        // check if directories exist
        String dataPacksRoot = container.getAbsolutePath() + File.separator + s_world + File.separator + "datapacks" + File.separator + "tardis" + File.separator + "data" + File.separator + "tardis" + File.separator + "advancements";
        File tardisDir = new File(dataPacksRoot);
        if (!tardisDir.exists()) {
            Bukkit.getLogger().log(Level.INFO, plugin.getLanguage().getString("ADVANCEMENT_DIRECTORIES"));
            tardisDir.mkdirs();
        }
        for (Advancement advancement : Advancement.values()) {
            String json = advancement.getConfigName() + ".json";
            File jfile = new File(dataPacksRoot, json);
            if (!jfile.exists()) {
                Bukkit.getLogger().log(Level.INFO, ChatColor.RED + String.format(plugin.getLanguage().getString("ADVANCEMENT_NOT_FOUND"), json));
                Bukkit.getLogger().log(Level.INFO, String.format(plugin.getLanguage().getString("ADVANCEMENT_COPYING"), json));
                copy(json, jfile);
            }
        }
        String dataPacksMeta = container.getAbsolutePath() + File.separator + s_world + File.separator + "datapacks" + File.separator + "tardis";
        File mcmeta = new File(dataPacksMeta, "pack.mcmeta");
        if (!mcmeta.exists()) {
            Bukkit.getLogger().log(Level.INFO, ChatColor.RED + String.format(plugin.getLanguage().getString("ADVANCEMENT_NOT_FOUND"), "pack.mcmeta"));
            Bukkit.getLogger().log(Level.INFO, String.format(plugin.getLanguage().getString("ADVANCEMENT_COPYING"), "pack.mcmeta"));
            copy("pack.mcmeta", mcmeta);
        }
    }
}
