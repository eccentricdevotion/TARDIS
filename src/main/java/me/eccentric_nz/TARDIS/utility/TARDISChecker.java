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

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author eccentric_nz
 */
public class TARDISChecker {

    private final TARDIS plugin;

    public TARDISChecker(TARDIS plugin) {
        this.plugin = plugin;
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

    public void checkAdvancements() {
        // get server's main world folder
        // is there a world container?
        File container = plugin.getServer().getWorldContainer();
        String s_world = plugin.getServer().getWorlds().get(0).getName();
        // check if directories exist
        String dataPacksRoot = container.getAbsolutePath() + File.separator + s_world + File.separator + "datapacks" + File.separator + "tardis" + File.separator + "data" + File.separator + "tardis" + File.separator + "advancements";
        File tardisDir = new File(dataPacksRoot);
        if (!tardisDir.exists()) {
            TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.WARNING, plugin.getLanguage().getString("ADVANCEMENT_DIRECTORIES"));
            tardisDir.mkdirs();
        }
        String dataPacksMeta = container.getAbsolutePath() + File.separator + s_world + File.separator + "datapacks" + File.separator + "tardis";
        File mcmeta = new File(dataPacksMeta, "pack.mcmeta");
        if (!mcmeta.exists()) {
            TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.WARNING, String.format(plugin.getLanguage().getString("ADVANCEMENT_NOT_FOUND"), "pack.mcmeta"));
            TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.WARNING, String.format(plugin.getLanguage().getString("ADVANCEMENT_COPYING"), "pack.mcmeta"));
            copy("pack.mcmeta", mcmeta);
            write(dataPacksRoot, false);
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
                                    // update json files
                                    write(dataPacksRoot, true);
                                }
                            }
                        }
                    }
                }
            } catch (IOException ignored) {
            }
        }
    }

    private void write(String dataPacksRoot, boolean overwrite) {
        for (Advancement advancement : Advancement.values()) {
            String json = advancement.getConfigName() + ".json";
            File jfile = new File(dataPacksRoot, json);
            if (!jfile.exists() || overwrite) {
                if (!overwrite) {
                    TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.WARNING, String.format(plugin.getLanguage().getString("ADVANCEMENT_NOT_FOUND"), json));
                }
                TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.WARNING, String.format(plugin.getLanguage().getString("ADVANCEMENT_COPYING"), json));
                copy(json, jfile);
            }
        }
    }
}
