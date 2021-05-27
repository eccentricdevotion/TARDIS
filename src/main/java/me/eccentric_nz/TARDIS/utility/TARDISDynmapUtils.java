package me.eccentric_nz.tardis.utility;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.api.TARDISData;
import me.eccentric_nz.tardis.api.TARDISAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

import static org.bukkit.Bukkit.getServer;

public class TARDISDynmapUtils {

	private final String INFO = "<div class=\"regioninfo\"><div class=\"infowindow\"><span style=\"font-weight:bold;\">Time Lord:</span> %owner%<br/><span style=\"font-weight:bold;\">Console type:</span> %console%<br/><span style=\"font-weight:bold;\">Chameleon circuit:</span> %chameleon%<br/><span style=\"font-weight:bold;\">Location:</span> %location%<br/><span style=\"font-weight:bold;\">Door:</span> %door%<br/><span style=\"font-weight:bold;\">Powered on:</span> %powered%<br/><span style=\"font-weight:bold;\">Siege mode:</span> %siege%<br/><span style=\"font-weight:bold;\">Occupants:</span> %occupants%</div></div>";
	private DynmapAPI api;
	private MarkerAPI markerAPI;
	private TARDISAPI tardisAPI;
	private boolean reload = false;
	private boolean stop;
	private Layer tardisLayer;

	public void load() {
		/*
		 * Gets Marker API
		 */
		markerAPI = api.getMarkerAPI();
		if (markerAPI == null) {
			Bukkit.getLogger().log(Level.WARNING, "Error loading Dynmap marker API!");
			return;
		}
		/*
		 * Gets TARDIS API
		 */
		tardisAPI = TARDISPlugin.plugin.getTardisAPI();

		/*
		 * Loads configuration
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
		 * Adds marker set for TardisAPI
		 */
		if (tardisAPI != null) {
			tardisLayer = new TARDISLayer();
		}
		/*
		 * Sets up update job - based on period
		 */
		stop = false;
		getServer().getScheduler().scheduleSyncDelayedTask(TARDISPlugin.plugin, new MarkerUpdate(), 5 * 20);
	}

	private void updateMarkers() {
		if (tardisAPI != null) {
			tardisLayer.updateMarkerSet();
		}
		getServer().getScheduler().scheduleSyncDelayedTask(TARDISPlugin.plugin, new MarkerUpdate(), 100L);
	}

	private String formatInfoWindow(String who, TARDISData data, Marker m) {
		String window = INFO;
		window = window.replace("%owner%", who);
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

	private abstract class Layer {

		MarkerSet set;
		MarkerIcon defIcon;
		String labelFormat;
		Map<String, Marker> markers = new HashMap<>();

		public Layer() {
			set = markerAPI.getMarkerSet("tardis");
			if (set == null) {
				set = markerAPI.createMarkerSet("tardis", "TARDISes", null, false);
			} else {
				set.setMarkerSetLabel("TARDISes");
			}
			if (set == null) {
				Bukkit.getLogger().log(Level.WARNING, "Error creating TARDIS marker set");
				return;
			}
			set.setLayerPriority(0);
			set.setHideByDefault(false);
			defIcon = markerAPI.getMarkerIcon("tardis");
			labelFormat = "%name% (TARDIS)";
		}

		void cleanUp() {
			if (set != null) {
				set.deleteMarkerSet();
				set = null;
			}
			markers.clear();
		}

		void updateMarkerSet() {
			Map<String, Marker> newMap = new HashMap<>();
			/*
			 * Builds new map
			 */
			Map<String, TARDISData> marks = getMarkers();
			marks.keySet().forEach((name) -> {
				Location loc = marks.get(name).getLocation();
				String wName = Objects.requireNonNull(loc.getWorld()).getName();
				/*
				 * Gets location
				 */
				String id = wName + "/" + name;
				String label = labelFormat.replace("%name%", name);
				/*
				 * Sees if we already have marker
				 */
				Marker m = markers.remove(id);
				if (m == null) {
					/*
					 * Not found? Needs new one
					 */
					m = set.createMarker(id, label, wName, loc.getX(), loc.getY(), loc.getZ(), defIcon, false);
				} else {
					/*
					 * Else, updates position if needed
					 */
					m.setLocation(wName, loc.getX(), loc.getY(), loc.getZ());
					m.setLabel(label);
					m.setMarkerIcon(defIcon);
				}
				/*
				 * Builds popup
				 */
				String desc = formatInfoWindow(name, marks.get(name), m);
				/*
				 * Sets popup
				 */
				m.setDescription(desc);
				/*
				 * Adds to new map
				 */
				newMap.put(id, m);
			});
			/*
			 * Reviews old map - anything left is gone
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
		public abstract Map<String, TARDISData> getMarkers();
	}

	private class TARDISLayer extends Layer {

		public TARDISLayer() {
			super();
		}

		/*
		 * Get current markers, by timelord with location
		 */
		@Override
		public Map<String, TARDISData> getMarkers() {
			HashMap<String, TARDISData> map = new HashMap<>();
			if (tardisAPI != null) {
				HashMap<String, Integer> tl = tardisAPI.getTimelordMap();
				tl.forEach((key, value) -> {
					TARDISData data;
					try {
						data = tardisAPI.getTARDISMapData(value);
						if (data.getLocation() != null) {
							map.put(key, data);
						}
					} catch (Exception ignored) {
					}
				});
			}
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
