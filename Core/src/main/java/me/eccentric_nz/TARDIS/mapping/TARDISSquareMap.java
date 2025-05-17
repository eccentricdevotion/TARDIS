/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.mapping;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.TARDISData;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.jpenilla.squaremap.api.*;
import xyz.jpenilla.squaremap.api.marker.Icon;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class TARDISSquareMap implements TARDISMapper {

    public static final Key TARDIS_ICON_KEY = Key.key("tardis_icon");
    private static final Key TARDIS_LAYER_KEY = Key.key("tardis");
    private final TARDIS plugin;
    private final Map<WorldIdentifier, SquaremapTask> tasks = new HashMap<>();

    public TARDISSquareMap(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void disable() {
        tasks.values().forEach(SquaremapTask::disable);
        tasks.clear();
    }

    @Override
    public void enable() {
        Plugin squaremap = plugin.getPM().getPlugin("squaremap");
        plugin.getPM().registerEvents(new TARDISServerListener("squaremap", this), plugin);
        // if enabled, activate
        if (squaremap != null && squaremap.isEnabled()) {
            activate();
        }
    }

    @Override
    public void activate() {
        try {
            BufferedImage image = ImageIO.read(new File(plugin.getDataFolder(), "tardis.png"));
            SquaremapProvider.get().iconRegistry().register(TARDIS_ICON_KEY, image);
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to register TARDIS icon", e);
        }
        updateMarkerSet(plugin.getConfig().getLong("mapping.update_period"));
    }

    @Override
    public void updateMarkerSet(long period) {
        for (MapWorld world : SquaremapProvider.get().mapWorlds()) {
            if (!plugin.getPlanetsConfig().getBoolean("planets." + world.identifier() + "time_travel")) {
                continue;
            }
            SimpleLayerProvider provider = SimpleLayerProvider.builder("TARDIS")
                    .showControls(true)
                    .defaultHidden(false)
                    .build();
            world.layerRegistry().register(TARDIS_LAYER_KEY, provider);
            SquaremapTask task = new SquaremapTask(world, provider, plugin);
            task.runTaskTimerAsynchronously(plugin, 0, 20L * period);
            tasks.put(world.identifier(), task);
        }
    }

    private static class SquaremapTask extends BukkitRunnable {

        private final MapWorld world;
        private final SimpleLayerProvider provider;
        private final TARDIS plugin;

        private boolean stop;

        public SquaremapTask(MapWorld world, SimpleLayerProvider provider, TARDIS plugin) {
            this.world = world;
            this.provider = provider;
            this.plugin = plugin;
        }

        @Override
        public void run() {
            if (stop) {
                cancel();
            }
            provider.clearMarkers();
            final World bukkitWorld = BukkitAdapter.bukkitWorld(world);
            TARDISGetter getter = new TARDISGetter(plugin, bukkitWorld);
            getter.resultSetAsync(results -> {
                for (TARDISData data : results) {
                    handle(data.owner(), data.location());
                }
            });
        }

        private void handle(String name, Location location) {
            Icon icon = Marker.icon(BukkitAdapter.point(location), TARDISSquareMap.TARDIS_ICON_KEY, 16);
            String label = String.format("%s (TARDIS)", name);
            icon.markerOptions(MarkerOptions.builder().hoverTooltip(label));
            String markerid = "tardis_" + name;
            provider.addMarker(Key.of(markerid), icon);
        }

        public void disable() {
            cancel();
            stop = true;
            provider.clearMarkers();
        }
    }
}
