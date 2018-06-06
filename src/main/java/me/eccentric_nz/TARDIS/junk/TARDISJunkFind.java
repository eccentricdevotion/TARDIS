/*
 * Copyright (C) 2018 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardisID;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISJunkFind {

    private final TARDIS plugin;

    public TARDISJunkFind(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean find(CommandSender sender) {
        // get current location
        if (!sender.hasPermission("tardis.junk")) {
            TARDISMessage.send(sender, "JUNK_NO_PERM");
            return true;
        }
        // get junk TARDIS id
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (rs.fromUUID("00000000-aaaa-bbbb-cccc-000000000000")) {
            // get current location
            HashMap<String, Object> wherec = new HashMap<>();
            wherec.put("tardis_id", rs.getTardis_id());
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
            if (rsc.resultSet()) {
                String world = rsc.getWorld().getName();
                if (plugin.isMVOnServer()) {
                    world = plugin.getMVHelper().getAlias(rsc.getWorld());
                }
                TARDISMessage.send(sender, "TARDIS_FIND", world + " at x: " + rsc.getX() + " y: " + rsc.getY() + " z: " + rsc.getZ());
                return true;
            } else {
                TARDISMessage.send(sender, "JUNK_NOT_FOUND");
                return true;
            }
        }
        return true;
    }
}
