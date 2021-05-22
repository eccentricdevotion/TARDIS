package me.eccentric_nz.tardis.utility.logging;

import me.eccentric_nz.tardis.TARDIS;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.block.Block;
import org.bukkit.plugin.PluginManager;

public class TARDISBlockLogger {

	private final TARDIS plugin;
	private CoreProtectAPI coreProtectAPI = null;
	private boolean logging = false;

	public TARDISBlockLogger(TARDIS plugin) {
		this.plugin = plugin;
	}

	public boolean isLogging() {
		return logging;
	}

	public void enableLogger() {
		PluginManager pm = plugin.getServer().getPluginManager();
		if (pm.isPluginEnabled("CoreProtect")) {
			CoreProtect cp = (CoreProtect) pm.getPlugin("CoreProtect");
			// Check that CoreProtect is loaded
			if (cp == null) {
				return;
			}
			// Check that the API is enabled
			CoreProtectAPI CoreProtect = cp.getAPI();
			if (!CoreProtect.isEnabled()) {
				return;
			}
			// Check that a compatible version of the API is loaded
			if (CoreProtect.APIVersion() < 6) {
				return;
			}
			plugin.getServer().getConsoleSender().sendMessage(plugin.getPluginName() + "Connecting to CoreProtect");
			coreProtectAPI = CoreProtect;
			logging = true;
		}
	}

	public void logPlacement(Block block) {
		coreProtectAPI.logPlacement("tardis", block.getLocation(), block.getType(), block.getBlockData());
	}

	public void logRemoval(Block block) {
		coreProtectAPI.logRemoval("tardis", block.getLocation(), block.getType(), block.getBlockData());
	}
}
