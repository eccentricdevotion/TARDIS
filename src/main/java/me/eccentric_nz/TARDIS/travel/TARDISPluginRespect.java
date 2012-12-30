/*
 * Copyright (C) 2012 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
public class TARDISPluginRespect {

    private TARDIS plugin;

    public TARDISPluginRespect(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Checks whether a location is allowed by other plugins. This checks
     * WorldGuard regions, Towny plots, WorldBorder borders and TARDIS areas.
     *
     * @param p the player who is travelling.
     * @param l the location to check.
     */
    public boolean getRespect(Player p, Location l, boolean message) {
        boolean bool = true;
        if (plugin.worldGuardOnServer && plugin.getConfig().getBoolean("respect_worldguard") && plugin.wgchk.cantBuild(p, l)) {
            if (message) {
                p.sendMessage(plugin.pluginName + "That location is protected by WorldGuard!");
            }
            bool = false;
        }
        if (plugin.townyOnServer && plugin.getConfig().getBoolean("respect_towny") && !plugin.tychk.isWilderness(p, l)) {
            if (message) {
                p.sendMessage(plugin.pluginName + "That location is protected by Towny!");
            }
            bool = false;
        }
        if (plugin.borderOnServer && plugin.getConfig().getBoolean("respect_worldborder") && !plugin.borderchk.isInBorder(l)) {
            if (message) {
                p.sendMessage(plugin.pluginName + "That location is outside the World Border!");
            }
            bool = false;
        }
        if (plugin.ta.areaCheckLocPlayer(p, l)) {
            if (message) {
                p.sendMessage(plugin.pluginName + "You do not have permission [" + plugin.trackPerm.get(p.getName()) + "] to bring the TARDIS to this location!");
            }
            plugin.trackPerm.remove(p.getName());
            bool = false;
        }
        return bool;
    }
}