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
package me.eccentric_nz.TARDIS.commands.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 *
 * @author eccentric_nz
 */
public class TARDISTravelArea {

    private final TARDIS plugin;

    public TARDISTravelArea(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean action(Player player, String[] args, int id, ChameleonPreset preset) {
        HashMap<String, Object> wherea = new HashMap<>();
        wherea.put("area_name", args[1]);
        ResultSetAreas rsa = new ResultSetAreas(plugin, wherea, false, false);
        if (!rsa.resultSet()) {
            plugin.getMessenger().sendColouredCommand(player, "AREA_NOT_FOUND", "/tardis list areas", plugin);
            return true;
        }
        if ((!TARDISPermission.hasPermission(player, "tardis.area." + args[1]) && !TARDISPermission.hasPermission(player, "tardis.area.*")) || (!player.isPermissionSet("tardis.area." + args[1]) && !player.isPermissionSet("tardis.area.*"))) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_NO_AREA_PERM", args[1]);
            return true;
        }
        if (plugin.getConfig().getBoolean("difficulty.disks") && !plugin.getUtils().inGracePeriod(player, false)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ADV_AREA");
            return true;
        }
        // check whether this is a no invisibility area
        String invisibility = rsa.getArea().invisibility();
        if (invisibility.equals("DENY") && preset.equals(ChameleonPreset.INVISIBLE)) {
            // check preset
            plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_NO_INVISIBLE");
            return true;
        } else if (!invisibility.equals("ALLOW")) {
            // force preset
            plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_FORCE_PRESET", invisibility);
            HashMap<String, Object> wherei = new HashMap<>();
            wherei.put("tardis_id", id);
            HashMap<String, Object> seti = new HashMap<>();
            seti.put("chameleon_preset", invisibility);
            // set chameleon adaption to OFF
            seti.put("adapti_on", 0);
            plugin.getQueryFactory().doSyncUpdate("tardis", seti, wherei);
        }
        Location l;
        if (rsa.getArea().grid()) {
            l = plugin.getTardisArea().getNextSpot(rsa.getArea().areaName());
        } else {
            l = plugin.getTardisArea().getSemiRandomLocation(rsa.getArea().areaId());
        }
        if (l == null) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_MORE_SPOTS");
            return true;
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("world", l.getWorld().getName());
        set.put("x", l.getBlockX());
        set.put("y", l.getBlockY());
        set.put("z", l.getBlockZ());
        // should be setting direction of TARDIS
        if (!rsa.getArea().direction().isEmpty()) {
            set.put("direction", rsa.getArea().direction());
        } else {
            // get current direction
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
            if (!rsc.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
                return true;
            }
            set.put("direction", rsc.getCurrent().direction().forPreset().toString());
        }
        set.put("submarine", 0);
        HashMap<String, Object> tid = new HashMap<>();
        tid.put("tardis_id", id);
        plugin.getQueryFactory().doSyncUpdate("next", set, tid);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_APPROVED", args[1]);
        plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), TravelType.AREA));
        plugin.getTrackerKeeper().getRescue().remove(id);
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            new TARDISLand(plugin, id, player).exitVortex();
            plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.AREA, id));
        }
        return true;
    }
}
