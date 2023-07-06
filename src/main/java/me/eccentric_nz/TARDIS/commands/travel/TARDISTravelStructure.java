/*
 * Copyright (C) 2023 eccentric_nz
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

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.travel.TARDISStructureLocation;
import me.eccentric_nz.TARDIS.travel.TARDISStructureTravel;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISTravelStructure {

    private final TARDIS plugin;

    public TARDISTravelStructure(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean action(Player player, String[] args, int id) {
        if (!plugin.getConfig().getBoolean("allow.village_travel")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_NO_VILLAGE");
            return true;
        }
        if (!TARDISPermission.hasPermission(player, "tardis.timetravel.village")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_NO_PERM_VILLAGE");
            return true;
        }
        // find a village / nether fortress / end city
        TARDISStructureLocation randomVillage = new TARDISStructureTravel(plugin).getRandomVillage(player, id, args);
        if (randomVillage == null) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "VILLAGE_NOT_FOUND");
            return true;
        }
        Location village = randomVillage.getLocation();
        // check respect
        if (!plugin.getPluginRespect().getRespect(village, new Parameters(player, Flag.getDefaultFlags()))) {
            if (plugin.getConfig().getBoolean("travel.no_destination_malfunctions")) {
                plugin.getTrackerKeeper().getMalfunction().put(id, true);
            } else {
                return true;
            }
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("world", village.getWorld().getName());
        set.put("x", village.getBlockX());
        set.put("y", village.getBlockY());
        set.put("z", village.getBlockZ());
        set.put("submarine", 0);
        TravelType travelType;
        switch (village.getWorld().getEnvironment()) {
            case THE_END -> travelType = TravelType.VILLAGE_THE_END;
            case NETHER -> travelType = TravelType.VILLAGE_NETHER;
            default -> travelType = TravelType.VILLAGE_OVERWORLD;
        }
        HashMap<String, Object> tid = new HashMap<>();
        tid.put("tardis_id", id);
        plugin.getQueryFactory().doSyncUpdate("next", set, tid);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_LOADED", randomVillage.getWhich(), !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
        plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), travelType));
        plugin.getTrackerKeeper().getRescue().remove(id);
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            new TARDISLand(plugin, id, player).exitVortex();
            plugin.getPM().callEvent(new TARDISTravelEvent(player, null, travelType, id));
        }
        return true;
    }
}
