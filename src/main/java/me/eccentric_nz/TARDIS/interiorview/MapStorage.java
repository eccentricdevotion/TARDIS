/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.interiorview;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Bukkit;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

public class MapStorage {

    private final GsonBuilder builder;
    private final Gson gson;

    public MapStorage() {
        builder = new GsonBuilder();
        builder.registerTypeAdapter(Color.class, new ColorTypeAdapter());
        gson = builder.create();
    }

    public void store(int id, Color[][] data) {
        File folder = new File (TARDIS.plugin.getDataFolder(), "interior_views");
        if (!folder.exists()) {
            folder.mkdir();
            folder.setExecutable(true);
        }
        File file = new File (TARDIS.plugin.getDataFolder(), "interior_views" + File.separator + "view_" + id + ".json");
        try {
            FileWriter writer = new FileWriter(file);
            gson.toJson(data, writer);
            writer.close();
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error writing to mapId: {0}", id);
        }
    }
}
