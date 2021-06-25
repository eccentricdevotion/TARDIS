package me.eccentric_nz.tardis.dynmap;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.api.TardisData;
import me.eccentric_nz.tardis.files.TardisFileCopier;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

public class TardisDynmap {

    private static final String INFO = "<div class=\"regioninfo\"><div class=\"infowindow\"><span style=\"font-weight:bold;\">Time Lord:</span> %owner%<br/><span style=\"font-weight:bold;\">Console type:</span> %console%<br/><span style=\"font-weight:bold;\">Chameleon circuit:</span> %chameleon%<br/><span style=\"font-weight:bold;\">Location:</span> %location%<br/><span style=\"font-weight:bold;\">Door:</span> %door%<br/><span style=\"font-weight:bold;\">Powered on:</span> %powered%<br/><span style=\"font-weight:bold;\">Siege mode:</span> %siege%<br/><span style=\"font-weight:bold;\">Occupants:</span> %occupants%</div></div>";
    private final TardisPlugin plugin;
    private Plugin dynmap;
    private DynmapAPI dynmapApi;
    private MarkerAPI markerApi;
    private boolean reload = false;
    private boolean stop;
    private Layer tardisLayer;

    public TardisDynmap(TardisPlugin plugin) {
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
        /*
         * Get dynmap
         */
        dynmap = plugin.getPluginManager().getPlugin("dynmap");
        /*
         * Get API
         */
        dynmapApi = (DynmapAPI) dynmap;
        plugin.getPluginManager().registerEvents(new TardisServerListener(), plugin);
        /*
         * If enabled, activate
         */
        if (dynmap.isEnabled()) {
            activate();
            checkIcon();
        }
    }

    private void activate() {
        /*
         * Now, get markers API
         */
        markerApi = dynmapApi.getMarkerAPI();
        if (markerApi == null) {
            Bukkit.getLogger().log(Level.WARNING, "Error loading Dynmap marker API!");
            return;
        }
        /*
         * Load configuration
         */
        if (reload) {
            if (tardisLayer != null) {
                if (tardisLayer.set != null) {
                    tardisLayer.set.deleteMarkerSet();
                    tardisLayer.set = null;
                }
                tardisLayer = null;
            }
        } else {
            reload = true;
        }
        /*
         * Now, add marker set for TardisApi
         */
        tardisLayer = new TardisLayer();
        /*
         * Set up update job - based on period
         */
        stop = false;
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new MarkerUpdate(), 5 * 20);
    }

    private void checkIcon() {
        String path = "plugins/dynmap/web/tiles/_markers_/tardis.png";
        File icon = new File(path);
        if (!icon.exists()) {
            TardisFileCopier.copy(path, plugin.getResource("tardis.png"), true);
            plugin.getServer().dispatchCommand(plugin.getConsole(), "dmarker addicon id:tardis newlabel:tardis file:plugins/dynmap/web/tiles/_markers_/tardis.png");
        }
    }

    private void updateMarkers() {
        tardisLayer.updateMarkerSet();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new MarkerUpdate(), 100L);
    }

    private String formatInfoWindow(String who, TardisData tardisData) {
        String window = INFO;
        window = window.replace("%owner%", who);
        window = window.replace("%console%", tardisData.getConsole());
        window = window.replace("%chameleon%", tardisData.getChameleon());
        String location = "x: " + tardisData.getLocation().getBlockX() + ", y: " + tardisData.getLocation().getBlockY() + ", z: " + tardisData.getLocation().getBlockZ();
        window = window.replace("%location%", location);
        window = window.replace("%powered%", tardisData.getPowered());
        window = window.replace("%door%", tardisData.getDoor());
        window = window.replace("%siege%", tardisData.getSiege());
        StringBuilder travellers = new StringBuilder();
        if (tardisData.getOccupants().size() > 0) {
            for (String occupant : tardisData.getOccupants()) {
                travellers.append(occupant).append("<br />");
            }
        } else {
            travellers = new StringBuilder("Empty");
        }
        window = window.replace("%occupants%", travellers.toString());
        return window;
    }

    private abstract class Layer {

        MarkerSet set;
        MarkerIcon defaultIcon;
        String labelFormat;
        Map<String, Marker> markers = new HashMap<>();

        Layer() {
            set = markerApi.getMarkerSet("tardis");
            if (set == null) {
                set = markerApi.createMarkerSet("tardis", "TARDISes", null, false);
            } else {
                set.setMarkerSetLabel("TARDISes");
            }
            if (set == null) {
                Bukkit.getLogger().log(Level.WARNING, "Error creating TARDIS marker set");
                return;
            }
            set.setLayerPriority(0);
            set.setHideByDefault(false);
            defaultIcon = markerApi.getMarkerIcon("tardis");
            labelFormat = "%name% (TARDIS)";
        }

        void cleanup() {
            if (set != null) {
                set.deleteMarkerSet();
                set = null;
            }
            markers.clear();
        }

        void updateMarkerSet() {
            Map<String, Marker> newMap = new HashMap<>();
            /*
             * Build new map
             */
            Map<String, TardisData> marks = getMarkers();
            marks.keySet().forEach((name) -> {
                Location location = marks.get(name).getLocation();
                String worldName = Objects.requireNonNull(location.getWorld()).getName();
                /*
                 * Get location
                 */
                String id = worldName + "/" + name;
                String label = labelFormat.replace("%name%", name);
                /*
                 * See if we already have marker
                 */
                Marker marker = markers.remove(id);
                if (marker == null) {
                    /*
                     * Not found? Need new one
                     */
                    marker = set.createMarker(id, label, worldName, location.getX(), location.getY(), location.getZ(), defaultIcon, false);
                } else {
                    /*
                     * Else, update position if needed
                     */
                    marker.setLocation(worldName, location.getX(), location.getY(), location.getZ());
                    marker.setLabel(label);
                    marker.setMarkerIcon(defaultIcon);
                }
                /*
                 * Build popup
                 */
                String description = formatInfoWindow(name, marks.get(name));
                /*
                 * Set popup
                 */
                marker.setDescription(description);
                /*
                 * Add to new map
                 */
                newMap.put(id, marker);
            });
            /*
             * Now, review old map - anything left is gone
             */
            markers.values().forEach(GenericMarker::deleteMarker);
            /*
             * And replace with new map
             */
            markers.clear();
            markers = newMap;
        }

        /*
         * Get current markers, by ID with location
         */
        public abstract Map<String, TardisData> getMarkers();
    }

    private class TardisServerListener implements Listener {

        @EventHandler
        public void onPluginEnable(PluginEnableEvent event) {
            Plugin plugin = event.getPlugin();
            String name = plugin.getDescription().getName();
            if (name.equals("dynmap")) {
                if (dynmap.isEnabled()) {
                    activate();
                }
            }
        }
    }

    private class TardisLayer extends Layer {

        TardisLayer() {
            super();
        }

        /*
         * Get current markers, by Time Lord with location
         */
        @Override
        public Map<String, TardisData> getMarkers() {
            HashMap<String, TardisData> map = new HashMap<>();
            HashMap<String, Integer> timeLordMap = plugin.getTardisApi().getTimeLordMap();
            timeLordMap.forEach((key, value) -> {
                TardisData data;
                try {
                    data = plugin.getTardisApi().getTardisMapData(value);
                    if (data.getLocation() != null) {
                        map.put(key, data);
                    }
                } catch (Exception ignored) {
                }
            });
            return map;
        }
    }

    private class MarkerUpdate implements Runnable {

        @Override
        public void run() {
            if (!stop) {
                updateMarkers();
            }
        }
    }
}
