/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.monitor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SnapshotLoader {

    final List<Integer> mapIDsNotToRender = new ArrayList<>();
    private final TARDIS plugin;
    private final Gson gson;

    public SnapshotLoader(TARDIS plugin) {
        this.plugin = plugin;
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Color.class, new ColorTypeAdapter());
        gson = builder.create();
    }

    public void load() {
        File folder = new File(plugin.getDataFolder() + "/monitor_snapshots");
        if (!folder.exists()) {
            folder.mkdir();
            folder.setExecutable(true);
        }
        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                if (!file.getName().startsWith(".")) { // not macos junk
                    // strip off extension
                    String[] split = file.getName().split(Pattern.quote("."));
                    String name = split[0];
                    // grab the number
                    String[] numSplit = name.split("_");
                    int mapId = Integer.parseInt(numSplit[1]);
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        Color[][] colours = gson.fromJson(br, Color[][].class);
                        MapView mapView = Bukkit.getMap(mapId);
                        if (mapView != null) {
                            mapView.setTrackingPosition(false);
                            for (MapRenderer renderer : mapView.getRenderers()) {
                                mapView.removeRenderer(renderer);
                            }
                            mapView.addRenderer(new MapRenderer() {
                                @Override
                                public void render(MapView mapViewNew, MapCanvas mapCanvas, Player player) {
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
                        } else {
                            // remove the file as map doesn't exist
                            file.delete();
                        }
                    } catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
                        plugin.getMessenger().message(plugin.getConsole(), TardisModule.WARNING, "Could not render map with id: " + mapId);
                    }
                }
            }
        }
    }
}
