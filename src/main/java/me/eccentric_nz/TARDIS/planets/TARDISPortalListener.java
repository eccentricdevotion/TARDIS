package me.eccentric_nz.tardis.planets;

import me.eccentric_nz.tardis.TARDISPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

public class TARDISPortalListener implements Listener {

	private final TARDISPlugin plugin;

	public TARDISPortalListener(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true)
	public void onPortalCreate(PortalCreateEvent event) {
		String world = event.getWorld().getName();
		if (!plugin.getPlanetsConfig().getBoolean("planets." + world + ".allow_portals")) {
			event.setCancelled(true);
		}
	}
}
