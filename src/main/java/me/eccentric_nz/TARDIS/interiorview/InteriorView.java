package me.eccentric_nz.TARDIS.interiorview;

import com.google.gson.Gson;
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
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class InteriorView {

    private final TARDIS plugin;
    List<Integer> mapIDsNotToRender = new ArrayList<>();

    public InteriorView(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void load() {
        File folder = new File(plugin.getDataFolder() + "/interior_views");
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
