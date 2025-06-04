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
package me.eccentric_nz.TARDIS.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * Rescue Operational Security Module G723 (ROSM) is an artificial intelligence built into a spacecraft working around
 * the Cimmerian System. It was designed by Professor Astrov to protect company interests and property, including rescue
 * of company employees from danger.
 *
 * @author eccentric_nz
 */
public class TARDISRescue {

    private final TARDIS plugin;

    public TARDISRescue(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Move the Police Box to a player's location, and potentially rescue the player.
     *
     * @param player  The Time Lord
     * @param saved   The player to be rescued
     * @param id      The TARDIS unique ID
     * @param d       the direction the Police Box is facing
     * @param rescue  whether to rescue the player
     * @param request whether this is a request to travel to a player
     * @return true or false
     */
    public boolean rescue(Player player, UUID saved, int id, COMPASS d, boolean rescue, boolean request) {
        if (plugin.getServer().getPlayer(saved) == null) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ONLINE");
            return false;
        }
        Location player_loc = plugin.getServer().getPlayer(saved).getLocation();
        if (plugin.getTardisArea().isInExistingArea(player_loc)) {
            plugin.getMessenger().sendColouredCommand(player, "PLAYER_IN_AREA", "/tardistravel area [area name]", plugin);
            return false;
        }
        if (!request && !plugin.getPluginRespect().getRespect(player_loc, new Parameters(player, Flag.getDefaultFlags()))) {
            return false;
        }
        if (!plugin.getPlanetsConfig().getBoolean("planets." + player_loc.getWorld().getName() + ".time_travel")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_WORLD_TRAVEL");
            return false;
        }
        World w = player_loc.getWorld();
        int[] start_loc = TARDISTimeTravel.getStartLocation(player_loc, d);
        int move = (rescue) ? 0 : 3;
        int count = TARDISTimeTravel.safeLocation(start_loc[0] - move, player_loc.getBlockY(), start_loc[2], start_loc[1] - move, start_loc[3], w, d);
        if (count > 0) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "RESCUE_NOT_SAFE");
            return false;
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("world", player_loc.getWorld().getName());
        set.put("x", (player_loc.getBlockX() - move));
        set.put("y", player_loc.getBlockY());
        set.put("z", player_loc.getBlockZ());
        set.put("direction", d.toString());
        set.put("submarine", 0);
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        plugin.getQueryFactory().doSyncUpdate("next", set, where);
        TravelType travelType = (rescue) ? TravelType.RESCUE : TravelType.PLAYER;
        if (!rescue) {
            plugin.getMessenger().send(player, "RESCUE_SET", !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
            if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                new TARDISLand(plugin, id, player).exitVortex();
                plugin.getPM().callEvent(new TARDISTravelEvent(player, null, travelType, id));
            }
        }
        plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), travelType));
        if (rescue) {
            plugin.getTrackerKeeper().getRescue().put(id, saved);
        }
        return true;
    }

    /**
     * Check whether a Time Lord can rescue a player, and then rescue them.
     *
     * @param player  The Time Lord
     * @param saved   The player to be rescued
     * @param request whether this is travel to player request
     * @return rescue data determining if the rescue was successful and the TARDIS id
     */
    public RescueData tryRescue(Player player, UUID saved, boolean request) {
        if (TARDISPermission.hasPermission(player, "tardis.timetravel") && !(TARDISPermission.hasPermission(player, "tardis.exile") && plugin.getConfig().getBoolean("travel.exile"))) {
            // get tardis data
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
                return new RescueData(false, 0);
            }
            Tardis tardis = rs.getTardis();
            int id = tardis.getTardisId();
            if (!tardis.isHandbrakeOn() && !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_TRAVELLING");
                return new RescueData(false, 0);
            }
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("uuid", player.getUniqueId().toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            if (!rst.resultSet() && !plugin.getTrackerKeeper().getTelepathicRescue().containsKey(saved)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_IN_TARDIS");
                return new RescueData(false, 0);
            }
            int tardis_id = rst.getTardis_id();
            if (tardis_id != id && !plugin.getTrackerKeeper().getTelepathicRescue().containsKey(saved)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CMD_ONLY_TL");
                return new RescueData(false, 0);
            }
            int level = tardis.getArtronLevel();
            int travel = plugin.getArtronConfig().getInt("travel");
            if (level < travel) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ENOUGH_ENERGY");
                return new RescueData(false, 0);
            }
            // get direction
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
            if (!rsc.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
                return new RescueData(false, 0);
            }
            return new RescueData(rescue(player, saved, id, rsc.getCurrent().direction(), !request, request), id);
        } else {
            return new RescueData(false, 0);
        }
    }

    public static class RescueData {

        private final boolean success;
        private final int tardis_id;

        RescueData(boolean success, int id) {
            this.success = success;
            tardis_id = id;
        }

        public boolean success() {
            return success;
        }

        public int getTardis_id() {
            return tardis_id;
        }
    }
}
