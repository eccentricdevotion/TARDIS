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
package me.eccentric_nz.tardis.junk;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisID;
import me.eccentric_nz.tardis.enumeration.WorldManager;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.planets.TardisAliasResolver;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TardisJunkFind {

    private final TardisPlugin plugin;

    TardisJunkFind(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean find(CommandSender sender) {
        // get current location
        if (!TardisPermission.hasPermission(sender, "tardis.junk")) {
            TardisMessage.send(sender, "JUNK_NO_PERM");
            return true;
        }
        // get junk tardis id
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (rs.fromUUID("00000000-aaaa-bbbb-cccc-000000000000")) {
            // get current location
            HashMap<String, Object> wherec = new HashMap<>();
            wherec.put("tardis_id", rs.getTardisId());
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
            if (rsc.resultSet()) {
                String world = TardisAliasResolver.getWorldAlias(rsc.getWorld());
                if (plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) {
                    world = plugin.getMVHelper().getAlias(rsc.getWorld());
                }
                TardisMessage.send(sender, "TARDIS_FIND", world + " at x: " + rsc.getX() + " y: " + rsc.getY() + " z: " + rsc.getZ());
            } else {
                TardisMessage.send(sender, "JUNK_NOT_FOUND");
            }
            return true;
        }
        return true;
    }
}
