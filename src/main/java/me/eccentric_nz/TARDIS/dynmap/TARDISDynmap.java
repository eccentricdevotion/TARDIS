package me.eccentric_nz.tardis.dynmap;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.api.TARDISData;
import me.eccentric_nz.tardis.files.TARDISFileCopier;
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

public class TARDISDynmap {

	private static final String INFO = "<div class=\"regioninfo\"><div class=\"infowindow\"><span style=\"font-weight:bold;\">Time Lord:</span> %owner%<br/><span style=\"font-weight:bold;\">Console type:</span> %console%<br/><span style=\"font-weight:bold;\">Chameleon circuit:</span> %chameleon%<br/><span style=\"font-weight:bold;\">Location:</span> %location%<br/><span style=\"font-weight:bold;\">Door:</span> %door%<br/><span style=\"font-weight:bold;\">Powered on:</span> %powered%<br/><span style=\"font-weight:bold;\">Siege mode:</span> %siege%<br/><span style=\"font-weight:bold;\">Occupants:</span> %occupants%</div></div>";
	private final TARDISPlugin plugin;
	private Plugin dynmap;
	private DynmapAPI api;
	private MarkerAPI markerapi;
	private boolean reload = false;
	private boolean stop;
	private Layer tardisLayer;

	public TARDISDynmap(TARDISPlugin plugin) {
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
		 * Now, add marker set for TardisAPI
		 */
		tardisLayer = new TARDISLayer();
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
			TARDISFileCopier.copy(path, plugin.getResource("tardis.png"), true);
			plugin.getServer().dispatchCommand(plugin.getConsole(), "dmarker addicon id:tardis newlabel:tardis file:plugins/dynmap/web/tiles/_markers_/tardis.png");
		}
	}

	private void updateMarkers() {
		tardisLayer.updateMarkerSet();
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new MarkerUpdate(), 100L);
	}

	private String formatInfoWindow(String who, TARDISData data) {
		String window = INFO;
		window = window.replace("%owner%", who);
		window = window.replace("%console%", data.getConsole());
		window = window.replace("%chameleon%", data.getChameleon());
		String l = "x: " + data.getLocation().getBlockX() + ", y: " + data.getLocation().getBlockY() + ", z: " +
				   data.getLocation().getBlockZ();
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

	private abstract class Layer {

		MarkerSet set;
		MarkerIcon deficon;
		String labelfmt;
		Map<String, Marker> markers = new HashMap<>();

		Layer() {
			set = markerapi.getMarkerSet("tardis");
			if (set == null) {
				set = markerapi.createMarkerSet("tardis", "TARDISes", null, false);
			} else {
				set.setMarkerSetLabel("TARDISes");
			}
			if (set == null) {
				Bukkit.getLogger().log(Level.WARNING, "Error creating tardis marker set");
				return;
			}
			set.setLayerPriority(0);
			set.setHideByDefault(false);
			deficon = markerapi.getMarkerIcon("tardis");
			labelfmt = "%name% (TARDIS)";
		}

		void cleanup() {
			if (set != null) {
				set.deleteMarkerSet();
				set = null;
			}
			markers.clear();
		}

		void updateMarkerSet() {
			Map<String, Marker> newmap = new HashMap<>();
			/*
			 * Build new map
			 */
			Map<String, TARDISData> marks = getMarkers();
			marks.keySet().forEach((name) -> {
				Location loc = marks.get(name).getLocation();
				String wname = Objects.requireNonNull(loc.getWorld()).getName();
				/*
				 * Get location
				 */
				String id = wname + "/" + name;
				String label = labelfmt.replace("%name%", name);
				/*
				 * See if we already have marker
				 */
				Marker m = markers.remove(id);
				if (m == null) {
					/*
					 * Not found? Need new one
					 */
					m = set.createMarker(id, label, wname, loc.getX(), loc.getY(), loc.getZ(), deficon, false);
				} else {
					/*
					 * Else, update position if needed
					 */
					m.setLocation(wname, loc.getX(), loc.getY(), loc.getZ());
					m.setLabel(label);
					m.setMarkerIcon(deficon);
				}
				/*
				 * Build popup
				 */
				String desc = formatInfoWindow(name, marks.get(name));
				/*
				 * Set popup
				 */
				m.setDescription(desc);
				/*
				 * Add to new map
				 */
				newmap.put(id, m);
			});
			/*
			 * Now, review old map - anything left is gone
			 */
			markers.values().forEach(GenericMarker::deleteMarker);
			/*
			 * And replace with new map
			 */
			markers.clear();
			markers = newmap;
		}

		/*
		 * Get current markers, by ID with location
		 */
		public abstract Map<String, TARDISData> getMarkers();
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

	private class MarkerUpdate implements Runnable {

		@Override
		public void run() {
			if (!stop) {
				updateMarkers();
			}
		}
	}
}
