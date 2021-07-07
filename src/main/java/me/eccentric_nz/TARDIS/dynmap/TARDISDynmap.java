package me.eccentric_nz.TARDIS.dynmap;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.TARDISData;
import me.eccentric_nz.TARDIS.files.TARDISFileCopier;
import org.bukkit.Bukkit;
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

    private final TARDIS plugin;
    private MarkerSet markerSet;
    private Map<String, Marker> markers = new HashMap<>();
    private static final String INFO = """
            <div class=\"regioninfo\">
                <div class=\"infowindow\">
                    <span style=\"font-weight:bold;\">Time Lord:</span> %owner%<br/>
                    <span style=\"font-weight:bold;\">Console type:</span> %console%<br/>
                    <span style=\"font-weight:bold;\">Chameleon circuit:</span> %chameleon%<br/>
                    <span style=\"font-weight:bold;\">Location:</span> %location%<br/>
                    <span style=\"font-weight:bold;\">Door:</span> %door%<br/>
                    <span style=\"font-weight:bold;\">Powered on:</span> %powered%<br/>
                    <span style=\"font-weight:bold;\">Siege mode:</span> %siege%<br/>
                    <span style=\"font-weight:bold;\">Occupants:</span> %occupants%
                </div>
            </div>""";
    private Plugin dynmap;
    private DynmapAPI api;
    private MarkerAPI markerapi;
    private boolean reload = false;
    private boolean stop;
    private Layer tardisLayer;

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
        /*
         * Get dynmap
         */
        dynmap = plugin.getPM().getPlugin("dynmap");
        /*
         * Get API
         */
        api = (DynmapAPI) dynmap;
        plugin.getPM().registerEvents(new TARDISServerListener(), plugin);
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
        markerapi = api.getMarkerAPI();
        if (markerapi == null) {
            Bukkit.getLogger().log(Level.WARNING, "Error loading Dynmap marker API!");
            return;
        }
        /*
         * Load configuration
         */
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
        /*
         * Now, add marker set for TardisAPI
         */
        tardisLayer = new TARDISLayer();
        tardisLayer.updateMarkerSet(); // potentially laggy on first run...
        /*
         * Set up update job - based on period
         */
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

    private String formatInfoWindow(String who, TARDISData data) {
        String window = INFO;
        window = window.replace("%owner%", who);
        window = window.replace("%console%", data.getConsole());
        window = window.replace("%chameleon%", data.getChameleon());
        String l = "x: " + data.getLocation().getBlockX() + ", y: " + data.getLocation().getBlockY() + ", z: " + data.getLocation().getBlockZ();
        window = window.replace("%location%", l);
        window = window.replace("%powered%", data.getPowered());
        window = window.replace("%door%", data.getDoor());
        window = window.replace("%siege%", data.getSiege());
        String travellers = "";
        if (data.getOccupants().size() > 0) {
            for (String o : data.getOccupants()) {
                travellers += o + "<br />";
            }
        } else {
            travellers = "Empty";
        }
        window = window.replace("%occupants%", travellers);
        return window;
    }

    private abstract class Layer {

        MarkerIcon markerIcon;
        String labelFormat;

        Layer() {
            markerSet = markerapi.getMarkerSet("tardis");
            if (markerSet == null) {
                markerSet = markerapi.createMarkerSet("tardis", "TARDISes", null, false);
            } else {
                markerSet.setMarkerSetLabel("TARDISes");
            }
            if (markerSet == null) {
                Bukkit.getLogger().log(Level.WARNING, "Error creating tardis marker set");
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

        void updateMarkerSet() {
            Map<String, Marker> newMarkerMap = new HashMap<>();
            /*
             * Build new map
             */
            Map<String, TARDISData> marks = getMarkers();
            marks.forEach((name, value) -> {
                Location loc = value.getLocation();
                String world = loc.getWorld().getName();
                /*
                 * Get location
                 */
                String id = world + "/" + name;
                String label = labelFormat.replace("%name%", name);
                /*
                 * See if we already have marker
                 */
                Marker m = markers.remove(id);
                if (m == null) {
                    /*
                     * Not found? Need new one
                     */
                    m = markerSet.createMarker(id, label, world, loc.getX(), loc.getY(), loc.getZ(), markerIcon, false);
                } else {
                    /*
                     * Else, update position if needed
                     */
                    m.setLocation(world, loc.getX(), loc.getY(), loc.getZ());
                    m.setLabel(label);
                    m.setMarkerIcon(markerIcon);
                }
                /*
                 * Build popup
                 */
                String desc = formatInfoWindow(name, value);
                /*
                 * Set popup
                 */
                m.setDescription(desc);
                /*
                 * Add to new map
                 */
                newMarkerMap.put(id, m);
            });
            /*
             * Now, review old map - anything left is gone
             */
            markers.values().forEach(GenericMarker::deleteMarker);
            /*
             * And replace with new map
             */
            markers.clear();
            markers = newMarkerMap;
        }

        /*
         * Get current markers, by ID with location
         */
        abstract Map<String, TARDISData> getMarkers();
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

    private class TARDISLayer extends Layer {

        TARDISLayer() {
            super();
        }

        /*
         * Get current markers, by timelord with location
         */
        @Override
        public Map<String, TARDISData> getMarkers() {
            HashMap<String, TARDISData> map = new HashMap<>();
            HashMap<String, Integer> tl = plugin.getTardisAPI().getTimelordMap();
            tl.forEach((key, value) -> {
                TARDISData data;
                try {
                    data = plugin.getTardisAPI().getTARDISMapData(value);
                    if (data.getLocation() != null) {
                        map.put(key, data);
                    }
                } catch (Exception ignored) {
                }
            });
            return map;
        }
    }

    private class TARDISMarkerUpdate implements Runnable {
        Map<String, Marker> newmap = new HashMap<>(); /* Build new map */
        ArrayList<World> worldsToDo = null;
        List<TARDISData> toDo = null;
        int tardisIndex = 0;
        World curWorld = null;

        @Override
        public void run() {
            if (stop || markerSet == null) {
                return;
            }
            // If needed, prime world list
            if (worldsToDo == null) {
                worldsToDo = new ArrayList<>(plugin.getServer().getWorlds());
            }
            while (toDo == null) {
                if (worldsToDo.isEmpty()) {
                    // Now, review old map - anything left is gone
                    for (Marker oldm : markers.values()) {
                        oldm.deleteMarker();
                    }
                    // And replace with new map
                    markers = newmap;
                    // Schedule next run
                    long delay = plugin.getConfig().getLong("dynmap.update_period", 200);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new TARDISMarkerUpdate(), delay);
                    return;
                } else {
                    curWorld = worldsToDo.remove(0); // Get next world
                    toDo = getTARDISes(curWorld); // Get TARDISes
                    tardisIndex = 0;
                    if (toDo != null && toDo.isEmpty()) {
                        toDo = null;
                    }
                }
            }
            // Process up to limit per tick
            int limit = plugin.getConfig().getInt("dynmap.updates_per_tick", 10);
            for (int cnt = 0; cnt < limit; cnt++) {
                if (tardisIndex >= toDo.size()) {
                    toDo = null;
                    break;
                }
                // Get next TARDIS
                TARDISData t = toDo.get(tardisIndex);
                tardisIndex++;

                Location loc = t.getLocation();
                String world = loc.getWorld().getName();
                /*
                 * Get location
                 */
                String id = world + "/" + t.getOwner();
                String label = String.format("%s (TARDIS)", t.getOwner());
                MarkerIcon markerIcon = markerapi.getMarkerIcon("tardis");
                /*
                 * See if we already have a marker
                 */
                Marker m = markers.remove(id);
                if (m == null) {
                    /*
                     * Not found? Need new one
                     */
                    m = markerSet.createMarker(id, label, world, loc.getX(), loc.getY(), loc.getZ(), markerIcon, false);
                } else {
                    /*
                     * Else, update position if needed
                     */
                    m.setLocation(world, loc.getX(), loc.getY(), loc.getZ());
                    m.setLabel(label);
                    m.setMarkerIcon(markerIcon);
                }
                /*
                 * Build popup
                 */
                String desc = formatInfoWindow(t.getOwner(), t);
                /*
                 * Set popup
                 */
                m.setDescription(desc);
                /*
                 * Add to new map
                 */
                newmap.put(id, m);
            }
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 1);
        }
    }

    private List<TARDISData> getTARDISes(World world) {
        return new TARDISGetByWorld(plugin).getList(world);
    }
}
