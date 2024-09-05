/*
 * Copyright (C) 2024 eccentric_nz
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Advancement;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardischemistry.block.Painting;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author eccentric_nz
 */
public class TARDISChecker {

    private final TARDIS plugin;
    private final Set<String> broken = new HashSet<>();

    public TARDISChecker(TARDIS plugin) {
        this.plugin = plugin;
        broken.add("aorta.json");
        broken.add("chemistry.json");
        broken.add("lava.json");
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
                TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.SEVERE, "Checker: Could not save the file (" + file + ").");
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.SEVERE, "Checker: Could not close the output stream.");
                }
            }
        } catch (FileNotFoundException e) {
            TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.SEVERE, "Checker: File not found: " + filename);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.SEVERE, "Checker: Could not close the input stream.");
                }
            }
        }
    }

    public void checkDataPack() {
        // get server's main world folder
        // is there a world container?
        File container = plugin.getServer().getWorldContainer();
        String s_world = plugin.getServer().getWorlds().getFirst().getName();
        // check if directories exist
        String dataPacksTardis = container.getAbsolutePath() + File.separator + s_world + File.separator + "datapacks" + File.separator + "tardis" + File.separator + "data" + File.separator + "tardis" + File.separator;
        String dataPacksAdvancement = dataPacksTardis + "advancements";
        File tardisOldDir = new File(dataPacksAdvancement);
        boolean update = false;
        if (tardisOldDir.exists()) {
            // delete directory and files as they need updating
            try {
                FileUtils.deleteDirectory(tardisOldDir);
                update = true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        String dataPacksMinecraft = container.getAbsolutePath() + File.separator + s_world + File.separator + "datapacks" + File.separator + "tardis" + File.separator + "data" + File.separator + "minecraft" + File.separator + "tags" + File.separator + "painting_variant" + File.separator;
        File placeableDir = new File(dataPacksMinecraft);
        if (!placeableDir.exists()) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.WARNING, String.format(plugin.getLanguage().getString("DATAPACK_DIRECTORIES", "%s"), "placeable"));
            placeableDir.mkdirs();
        }
        dataPacksAdvancement = dataPacksTardis + "advancement";
        String dataPacksTrim = dataPacksTardis + "trim_pattern";
        String dataPacksPaintings = dataPacksTardis + "painting_variant";
        File advancementDir = new File(dataPacksAdvancement);
        if (!advancementDir.exists()) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.WARNING, String.format(plugin.getLanguage().getString("DATAPACK_DIRECTORIES", "%s"), "advancements"));
            advancementDir.mkdirs();
        }
        File trimsDir = new File(dataPacksTrim);
        if (!trimsDir.exists()) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.WARNING, String.format(plugin.getLanguage().getString("DATAPACK_DIRECTORIES", "%s"), "trim patterns"));
            trimsDir.mkdirs();
        }
        File paintingsDir = new File(dataPacksPaintings);
        if (!paintingsDir.exists()) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.WARNING, String.format(plugin.getLanguage().getString("DATAPACK_DIRECTORIES", "%s"), "painting variants"));
            paintingsDir.mkdirs();
        }
        String dataPacksMeta = container.getAbsolutePath() + File.separator + s_world + File.separator + "datapacks" + File.separator + "tardis";
        File mcmeta = new File(dataPacksMeta, "pack.mcmeta");
        if (!mcmeta.exists() || update) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.WARNING, String.format(plugin.getLanguage().getString("DATAPACK_NOT_FOUND", "%s"), "pack.mcmeta", "datapack"));
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.WARNING, String.format(plugin.getLanguage().getString("DATAPACK_COPYING", "%s"), "datapack", "pack.mcmeta"));
            copy("pack.mcmeta", mcmeta);
        } else {
            // update the format - 48 is the latest for 1.21
            // it's a json file, so load it and check the value
            Gson gson = new GsonBuilder().create();
            try {
                // convert JSON file to map
                Map<?, ?> map = gson.fromJson(new FileReader(mcmeta), Map.class);
                // loop map entries
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    if (entry.getKey().equals("pack") && entry.getValue() instanceof Map<?, ?> values) {
                        for (Map.Entry<?, ?> data : values.entrySet()) {
                            if (data.getKey().equals("pack_format")) {
                                Double d = (Double) data.getValue();
                                if (d < 48.0D) {
                                    Map<String, Map<String, Object>> mcmap = new HashMap<>();
                                    Map<String, Object> pack = new HashMap<>();
                                    pack.put("description", "Data pack for the TARDIS plugin");
                                    pack.put("pack_format", 48);
                                    mcmap.put("pack", pack);
                                    FileWriter writer = new FileWriter(mcmeta);
                                    gson.toJson(mcmap, writer);
                                    writer.close();
                                }
                            }
                        }
                    }
                }
            } catch (IOException ignored) {
            }
        }
        File placeable = new File(dataPacksMinecraft, "placeable.json");
        if (!placeable.exists()) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.WARNING, String.format(plugin.getLanguage().getString("DATAPACK_NOT_FOUND", "%s"), "placeable.json", "painting variant"));
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.WARNING, String.format(plugin.getLanguage().getString("DATAPACK_COPYING", "%s"), "painting variant", "placeable.json"));
            copy("painting_variant/placeable.json", placeable);
        }
        // update json files
        write(dataPacksTardis);
    }

    private void write(String rootFolder) {
        for (Advancement advancement : Advancement.values()) {
            String json = advancement.getConfigName() + ".json";
            File jfile = new File(rootFolder + "advancement", json);
            if (!jfile.exists()) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.WARNING, String.format(plugin.getLanguage().getString("DATAPACK_NOT_FOUND", "%s"), json, "advancement"));
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.WARNING, String.format(plugin.getLanguage().getString("DATAPACK_COPYING", "%s, %s"), "advancement", json));
                copy("advancement/" + json, jfile);
            }
        }
        for (Monster monster : Monster.values()) {
            if (monster.hasTrim()) {
                String json = monster.getPermission() + ".json";
                File jfile = new File(rootFolder + "trim_pattern", json);
                if (!jfile.exists()) {
                    plugin.getMessenger().message(plugin.getConsole(), TardisModule.WARNING, String.format(plugin.getLanguage().getString("DATAPACK_NOT_FOUND", "%s"), json, "trim pattern"));
                    plugin.getMessenger().message(plugin.getConsole(), TardisModule.WARNING, String.format(plugin.getLanguage().getString("DATAPACK_COPYING", "%s"), "trim pattern", json));
                    copy("trim_pattern/" + json, jfile);
                }
            }
        }
        for (Painting painting : Painting.values()) {
            String json = painting.getName() + ".json";
            File jfile = new File(rootFolder + "painting_variant", json);
            if (!jfile.exists()) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.WARNING, String.format(plugin.getLanguage().getString("DATAPACK_NOT_FOUND", "%s"), json, "painting variant"));
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.WARNING, String.format(plugin.getLanguage().getString("DATAPACK_COPYING", "%s"), "painting variant", json));
                copy("painting_variant/" + json, jfile);
            } else if (!plugin.getConfig().getBoolean("conversions.paintings") && broken.contains(json)) {
                // fix broken paintings
                copy("painting_variant/" + json, jfile);
            } else if (!plugin.getConfig().getBoolean("conversions.exploding") && painting == Painting.EXPLODING_TARDIS) {
                plugin.debug("Fixing exploding TARDIS painting");
                // fix exploding TARDIS
                copy("painting_variant/" + json, jfile);
                plugin.getConfig().set("conversions.exploding", true);
            }
            plugin.saveConfig();
        }
    }
}
