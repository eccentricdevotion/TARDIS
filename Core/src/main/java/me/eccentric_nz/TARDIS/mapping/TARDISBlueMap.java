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

import com.flowpowered.math.vector.Vector3d;
import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.markers.MarkerSet;
import de.bluecolored.bluemap.api.markers.POIMarker;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.TARDISData;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class TARDISBlueMap implements TARDISMapper {

    private final TARDIS plugin;

    public TARDISBlueMap(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void disable() {
    }

    @Override
    public void enable() {
        Plugin bluemap = plugin.getPM().getPlugin("BlueMap");
        // get API
        plugin.getPM().registerEvents(new TARDISServerListener("BlueMap", this), plugin);
        // if enabled, activate
        if (bluemap != null && bluemap.isEnabled()) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this::activate, 300L);
        }
    }

    @Override
    public void activate() {
        BlueMapAPI.getInstance().ifPresent(api -> {
            long period = (plugin.getConfig().getLong("mapping.update_period") * 20) / 3;
            // start a repeating task to update markers
            updateMarkerSet(period);
        });
    }

    @Override
    public void updateMarkerSet(long period) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new BlueMapMarkerUpdate(), period);
    }

    private class BlueMapMarkerUpdate implements Runnable {
        ArrayList<World> worldsToDo = null;
        List<TARDISData> toDo = null;
        int tardisIndex = 0;
        World curWorld = null;

        @Override
        public void run() {
            // prime world list
            if (worldsToDo == null) {
                worldsToDo = new ArrayList<>();
                // only get worlds that are enabled for time travel
                for (String planet : plugin.getPlanetsConfig().getConfigurationSection("planets").getKeys(false)) {
                    if (plugin.getPlanetsConfig().getBoolean("planets." + planet + ".time_travel")) {
                        World world = plugin.getServer().getWorld(planet);
                        if (world != null) {
                            worldsToDo.add(world);
                        }
                    }
                }
            }
            while (toDo == null) {
                if (worldsToDo.isEmpty()) {
                    // Schedule next run
                    long delay = plugin.getConfig().getLong("mapping.update_period", 30) * 20L;
                    updateMarkerSet(delay);
                    break;
                } else {
                    // get next world
                    curWorld = worldsToDo.remove(0);
                    BlueMapAPI.getInstance().ifPresent(api -> api.getWorld(curWorld).ifPresent(w -> {
                        // get TARDISes
                        TARDISGetter getter = new TARDISGetter(plugin, curWorld);
                        getter.resultSetAsync(results -> {
                            MarkerSet markerSet = new MarkerSet("TARDIS");
                            toDo = results;
                            tardisIndex = 0;
                            if (toDo != null && toDo.isEmpty()) {
                                toDo = null;
                            } else {
                                // process up to limit per tick
                                int limit = plugin.getConfig().getInt("mapping.updates_per_tick", 10);
                                for (int cnt = 0; cnt < limit; cnt++) {
                                    if (tardisIndex >= toDo.size()) {
                                        toDo = null;
                                        break;
                                    }
                                    // get next TARDIS
                                    TARDISData data = toDo.get(tardisIndex);
                                    tardisIndex++;
                                    // get marker id
                                    String id = curWorld.getName() + "/" + data.getOwner();
                                    Location loc = data.getLocation();
                                    String label = String.format("%s (TARDIS)", data.getOwner());
                                    String desc = formatInfoWindow(data);
                                    POIMarker marker = new POIMarker(label, new Vector3d(loc.getX(), loc.getY(), loc.getZ()));
                                    marker.setIcon("https://raw.githubusercontent.com/eccentricdevotion/TARDIS/master/Core/src/main/resources/tardis.png", 0, 0);
                                    marker.setDetail(desc);
                                    markerSet.put(id, marker);
                                }
                            }
                            // put markerset back
                            for (BlueMapMap map : w.getMaps()) {
                                map.getMarkerSets().put("TARDIS", markerSet);
                            }
                        });
                    }));
                }
            }
        }
    }
}
