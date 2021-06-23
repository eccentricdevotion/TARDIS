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
import me.eccentric_nz.tardis.commands.admin.TardisDeleteCommand;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisId;
import me.eccentric_nz.tardis.destroyers.DestroyData;
import me.eccentric_nz.tardis.enumeration.CardinalDirection;
import me.eccentric_nz.tardis.enumeration.Consoles;
import me.eccentric_nz.tardis.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author eccentric_nz
 */
class TardisJunkDeleter {

    private final TardisPlugin plugin;

    TardisJunkDeleter(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean delete(CommandSender sender) {
        if (!sender.hasPermission("tardis.admin")) {
            TardisMessage.send(sender, "CMD_ADMIN");
            return true;
        }
        ResultSetTardisId rs = new ResultSetTardisId(plugin);
        if (rs.fromUUID("00000000-aaaa-bbbb-cccc-000000000000")) {
            int id = rs.getTardisId();
            // get the current location
            Location bb_loc = null;
            HashMap<String, Object> wherecl = new HashMap<>();
            wherecl.put("tardis_id", id);
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
            if (rsc.resultSet()) {
                bb_loc = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
            }
            if (bb_loc == null) {
                TardisMessage.send(sender, "CURRENT_NOT_FOUND");
                return true;
            }
            // destroy junk tardis
            DestroyData dd = new DestroyData();
            dd.setDirection(CardinalDirection.SOUTH);
            dd.setLocation(bb_loc);
            dd.setHide(false);
            dd.setOutside(false);
            dd.setSubmarine(rsc.isSubmarine());
            dd.setTardisId(id);
            dd.setThrottle(SpaceTimeThrottle.JUNK);
            plugin.getPresetDestroyer().destroyPreset(dd);
            // destroy the vortex tardis
            World cw = plugin.getServer().getWorld(Objects.requireNonNull(plugin.getConfig().getString("creation.default_world_name")));
            // give the tardis time to remove itself as it's not hidden
            if (cw != null) {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    plugin.getInteriorDestroyer().destroyInner(Consoles.schematicFor("junk"), id, cw, -999);
                    TardisDeleteCommand.cleanDatabase(id);
                    TardisMessage.send(sender, "JUNK_DELETED");
                }, 20L);
            }
        }
        return true;
    }
}
