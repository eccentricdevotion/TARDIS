package me.eccentric_nz.TARDIS.dynmap;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.TARDISData;
import me.eccentric_nz.TARDIS.files.TARDISFileCopier;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class TARDISDynmap {

    private static final String INFO = """
            <div class="regioninfo">
                <div class="infowindow">
                    <span style="font-weight:bold;">Time Lord:</span> %owner%<br/>
                    <span style="font-weight:bold;">Console type:</span> %console%<br/>
                    <span style="font-weight:bold;">Chameleon circuit:</span> %chameleon%<br/>
                    <span style="font-weight:bold;">Location:</span> %location%<br/>
                    <span style="font-weight:bold;">Door:</span> %door%<br/>
                    <span style="font-weight:bold;">Powered on:</span> %powered%<br/>
                    <span style="font-weight:bold;">Siege mode:</span> %siege%<br/>
                    <span style="font-weight:bold;">Occupants:</span> %occupants%
                </div>
            </div>""";
    private final TARDIS plugin;
    private MarkerSet markerSet;
    private Map<String, Marker> markers = new HashMap<>();
    private Plugin dynmap;
    private DynmapAPI api;
    private MarkerAPI markerapi;
    private boolean reload = false;
    private boolean stop;
    private TARDISLayer tardisLayer;

    public TARDISDynmap(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void disable() {
        if (tardisLayer != null) {
            tardisLayer.cleanup();
            tardisLayer = null;
        }
        stop = true;
    }

    public void enable() {
        // get dynmap
        dynmap = plugin.getPM().getPlugin("dynmap");
        // get API
        api = (DynmapAPI) dynmap;
        plugin.getPM().registerEvents(new TARDISServerListener(), plugin);
        // if enabled, activate
        if (dynmap.isEnabled()) {
            activate();
            checkIcon();
        }
    }

    private void activate() {
        // get markers API
        markerapi = api.getMarkerAPI();
        if (markerapi == null) {
            plugin.debug("Error loading Dynmap marker API!");
            return;
        }
        // load configuration
        if (reload) {
            if (tardisLayer != null) {
                if (markerSet != null) {
                    markerSet.deleteMarkerSet();
                    markerSet = null;
                }
                tardisLayer = null;
            }
        } else {
            reload = true;
        }
        // add TARDIS marker set
        tardisLayer = new TARDISLayer();
        // potentially laggy on first run...
        tardisLayer.updateMarkerSet();
        // set up update job - based on period
        stop = false;
        // this marker update is limited by `plugin.getConfig().getInt("dynmap.updates_per_tick")`
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new TARDISMarkerUpdate(), (plugin.getConfig().getLong("dynmap.update_period") * 20) / 3);
    }

    private void checkIcon() {
        String path = "plugins/dynmap/web/tiles/_markers_/tardis.png";
        File icon = new File(path);
        if (!icon.exists()) {
            TARDISFileCopier.copy(path, plugin.getResource("tardis.png"), true);
            plugin.getServer().dispatchCommand(plugin.getConsole(), "dmarker addicon id:tardis newlabel:tardis file:plugins/dynmap/web/tiles/_markers_/tardis.png");
        }
    }

    private String formatInfoWindow(TARDISData data) {
        String window = INFO;
        window = window.replace("%owner%", data.getOwner());
        window = window.replace("%console%", data.getConsole());
        window = window.replace("%chameleon%", data.getChameleon());
        String l = "x: " + data.getLocation().getBlockX() + ", y: " + data.getLocation().getBlockY() + ", z: " + data.getLocation().getBlockZ();
        window = window.replace("%location%", l);
        window = window.replace("%powered%", data.getPowered());
        window = window.replace("%door%", data.getDoor());
        window = window.replace("%siege%", data.getSiege());
        StringBuilder travellers = new StringBuilder();
        if (data.getOccupants().size() > 0) {
            for (String o : data.getOccupants()) {
                travellers.append(o).append("<br />");
            }
        } else {
            travellers = new StringBuilder("Empty");
        }
        window = window.replace("%occupants%", travellers.toString());
        return window;
    }

    private class TARDISLayer {

        MarkerIcon markerIcon;
        String labelFormat;

        TARDISLayer() {
            markerSet = markerapi.getMarkerSet("tardis");
            if (markerSet == null) {
                markerSet = markerapi.createMarkerSet("tardis", "TARDISes", null, false);
            } else {
                markerSet.setMarkerSetLabel("TARDISes");
            }
            if (markerSet == null) {
                plugin.getLogger().log(Level.WARNING, "Error creating tardis marker set");
                return;
            }
            markerSet.setLayerPriority(0);
            markerSet.setHideByDefault(false);
            markerIcon = markerapi.getMarkerIcon("tardis");
            labelFormat = "%name% (TARDIS)";
        }

        void cleanup() {
            if (markerSet != null) {
                markerSet.deleteMarkerSet();
                markerSet = null;
            }
            markers.clear();
        }

        /**
         * Get current markers with relevant TARDIS data
         * only on first run - markers are updated thereafter
         */
        void updateMarkerSet() {
            Map<String, Marker> newMarkerMap = new HashMap<>();
            // build new map
            TARDISGetter getter = new TARDISGetter(plugin, null);
            getter.resultSetAsync(results -> results.forEach(value -> {
                Location loc = value.getLocation();
                World w = loc.getWorld();
                if (w != null) {
                    String world = w.getName();
                    // get location
                    String id = world + "/" + value.getOwner();
                    String label = labelFormat.replace("%name%", value.getOwner());
                    // see if we already have marker
                    Marker marker = markers.remove(id);
                    if (marker == null) {
                        // not found? make a new one
                        marker = markerSet.createMarker(id, label, world, loc.getX(), loc.getY(), loc.getZ(), markerIcon, false);
                    } else {
                        // else, update position
                        marker.setLocation(world, loc.getX(), loc.getY(), loc.getZ());
                        marker.setLabel(label);
                        marker.setMarkerIcon(markerIcon);
                    }
                    // build popup
                    String desc = formatInfoWindow(value);
                    // set popup
                    marker.setDescription(desc);
                    // add to new marker map
                    newMarkerMap.put(id, marker);
                }
            }));
            // review old map - anything left is gone
            markers.values().forEach(GenericMarker::deleteMarker);
            // replace with new map
            markers.clear();
            markers = newMarkerMap;
        }
    }

    private class TARDISServerListener implements Listener {

        @EventHandler
        public void onPluginEnable(PluginEnableEvent event) {
            Plugin p = event.getPlugin();
            String name = p.getDescription().getName();
            if (name.equals("dynmap")) {
                if (dynmap.isEnabled()) {
                    activate();
                }
            }
        }
    }

    private class TARDISMarkerUpdate implements Runnable {
        // build new map
        Map<String, Marker> newmap = new HashMap<>();
        ArrayList<World> worldsToDo = null;
        List<TARDISData> toDo = null;
        int tardisIndex = 0;
        World curWorld = null;

        @Override
        public void run() {
            if (stop || markerSet == null) {
                return;
            }
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
                    // review old map - anything left is gone
                    for (Marker oldm : markers.values()) {
                        oldm.deleteMarker();
                    }
                    // replace with new map
                    markers = newmap;
                    // Schedule next run
                    long delay = plugin.getConfig().getLong("dynmap.update_period", 30) * 20L;
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new TARDISMarkerUpdate(), delay);
                    break;
                } else {
                    // get next world
                    curWorld = worldsToDo.remove(0);
                    // get TARDISes
                    TARDISGetter getter = new TARDISGetter(plugin, curWorld);
                    getter.resultSetAsync(results -> {
                        toDo = results;
                        tardisIndex = 0;
                        if (toDo != null && toDo.isEmpty()) {
                            toDo = null;
                        } else {
                            // process up to limit per tick
                            int limit = plugin.getConfig().getInt("dynmap.updates_per_tick", 10);
                            for (int cnt = 0; cnt < limit; cnt++) {
                                if (tardisIndex >= toDo.size()) {
                                    toDo = null;
                                    break;
                                }
                                // get next TARDIS
                                TARDISData data = toDo.get(tardisIndex);
                                tardisIndex++;
                                Location loc = data.getLocation();
                                String world = loc.getWorld().getName();
                                // get marker id
                                String id = world + "/" + data.getOwner();
                                String label = String.format("%s (TARDIS)", data.getOwner());
                                MarkerIcon markerIcon = markerapi.getMarkerIcon("tardis");
                                // see if we already have a marker
                                Marker marker = markers.remove(id);
                                if (marker == null) {
                                    // not found? make new one
                                    marker = markerSet.createMarker(id, label, world, loc.getX(), loc.getY(), loc.getZ(), markerIcon, false);
                                } else {
                                    // update position if needed
                                    marker.setLocation(world, loc.getX(), loc.getY(), loc.getZ());
                                    marker.setLabel(label);
                                    marker.setMarkerIcon(markerIcon);
                                }
                                if (marker != null) { // should never be null when getting here as there is already a null check...
                                    // build popup
                                    String desc = formatInfoWindow(data);
                                    // set popup
                                    marker.setDescription(desc); // ... but apparently this line can throw a NPE - java.lang.NullPointerException: Cannot invoke "org.dynmap.markers.Marker.setDescription(String)" because "marker" is null
                                    // add to new marker map
                                    newmap.put(id, marker);
                                }
                            }
                        }
                    });
                }
            }
        }
    }
}
