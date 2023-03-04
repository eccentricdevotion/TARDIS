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
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class InteriorView {

    private final TARDIS plugin;
    List<Integer> mapIDsNotToRender = new ArrayList<>();

    public InteriorView(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void load() {
        File folder = new File(plugin.getDataFolder() + "/interior_views");
        if (!folder.exists()) {
            folder.mkdir();
            folder.setExecutable(true);
        }
        Gson gson = new Gson();
        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                int mapId = Integer.parseInt(file.getName().split("_")[1].split(Pattern.quote("."))[0]);
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    Color[][] colours = gson.fromJson(br, Color[][].class);
                    MapView mapView = Bukkit.getMap(mapId);
                    mapView.setTrackingPosition(false);
                    for (MapRenderer renderer : mapView.getRenderers()) {
                        mapView.removeRenderer(renderer);
                    }
                    mapView.addRenderer(new MapRenderer() {
                        @Override
                        public void render(@NotNull MapView mapViewNew, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
                            if (!mapIDsNotToRender.contains(mapId)) {
                                mapIDsNotToRender.add(mapId);
                                for (int x = 0; x < 128; x++) {
                                    for (int y = 0; y < 128; y++) {
                                        mapCanvas.setPixelColor(x, y, colours[x][y]);
                                    }
                                }
                            }
                        }
                    });
                } catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
                    Bukkit.getLogger().log(Level.WARNING, "Could not render map with id: {0}", mapId);
                }
            }
        }
    }
}
