package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

public class TARDISPortalListener implements Listener {

	private final TARDIS plugin;

	public TARDISPortalListener(TARDIS plugin) {
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
