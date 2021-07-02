/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility.logging;

import me.eccentric_nz.TARDIS.TARDIS;
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
        coreProtectAPI.logPlacement("TARDIS", block.getLocation(), block.getType(), block.getBlockData());
    }

    public void logRemoval(Block block) {
        coreProtectAPI.logRemoval("TARDIS", block.getLocation(), block.getType(), block.getBlockData());
    }
}
