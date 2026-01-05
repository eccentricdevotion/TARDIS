/*
 * Copyright (C) 2026 eccentric_nz
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
import me.eccentric_nz.TARDIS.commands.admin.DeleteTARDISCommand;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.destroyers.DestroyData;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.Desktops;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

/**
 * @author eccentric_nz
 */
class TARDISJunkDelete {

    private final TARDIS plugin;

    TARDISJunkDelete(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean delete(CommandSender sender) {
        if (!sender.hasPermission("tardis.admin")) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_ADMIN");
            return true;
        }
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (rs.fromUUID("00000000-aaaa-bbbb-cccc-000000000000")) {
            int id = rs.getTardisId();
            // get the current location
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
            if (!rsc.resultSet()) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
                return true;
            }
            Current current = rsc.getCurrent();
            // destroy junk TARDIS
            DestroyData dd = new DestroyData();
            dd.setDirection(COMPASS.SOUTH);
            dd.setLocation(current.location());
            dd.setHide(false);
            dd.setOutside(false);
            dd.setSubmarine(current.submarine());
            dd.setTardisID(id);
            dd.setThrottle(SpaceTimeThrottle.JUNK);
            plugin.getPresetDestroyer().destroyPreset(dd);
            // destroy the vortex TARDIS
            World cw = plugin.getServer().getWorld(plugin.getConfig().getString("creation.default_world_name"));
            // give the TARDIS time to remove itself as it's not hidden
            if (cw != null) {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    plugin.getInteriorDestroyer().destroyInner(Desktops.schematicFor("junk"), id, cw, -999);
                    DeleteTARDISCommand.cleanDatabase(id);
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "JUNK_DELETED");
                }, 20L);
            }
        }
        return true;
    }
}
