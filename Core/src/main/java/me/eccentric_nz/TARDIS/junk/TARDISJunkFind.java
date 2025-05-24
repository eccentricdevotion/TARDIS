/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.junk;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISCache;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import org.bukkit.command.CommandSender;

/**
 * @author eccentric_nz
 */
class TARDISJunkFind {

    private final TARDIS plugin;

    TARDISJunkFind(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean find(CommandSender sender) {
        // get current location
        if (!TARDISPermission.hasPermission(sender, "tardis.junk")) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "JUNK_NO_PERM");
            return true;
        }
        // get junk TARDIS id
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (rs.fromUUID("00000000-aaaa-bbbb-cccc-000000000000")) {
            // get current location
            Current current = TARDISCache.CURRENT.get(rs.getTardisId());
            if (current != null) {
                String world = TARDISAliasResolver.getWorldAlias(current.location().getWorld());
                if (!plugin.getPlanetsConfig().getBoolean("planets." + current.location().getWorld().getName() + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) {
                    world = plugin.getMVHelper().getAlias(current.location().getWorld());
                }
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "TARDIS_FIND", world + " at x: " + current.location().getBlockX() + " y: " + current.location().getBlockY() + " z: " + current.location().getBlockZ());
            } else {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "JUNK_NOT_FOUND");
            }
            return true;
        }
        return true;
    }
}
