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
package me.eccentric_nz.TARDIS.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravelledTo;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISFactionsChecker;
import me.eccentric_nz.TARDIS.utility.TARDISRedProtectChecker;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

/**
 * The telepathic password to the TARDIS was "the colour crimson, the number eleven, the feeling of delight, and the
 * smell of dust after rain".
 *
 * @author eccentric_nz
 */
public class TARDISTravelRequest {

    private final TARDIS plugin;

    public TARDISTravelRequest(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Checks whether a location is allowed by other plugins. This checks WorldGuard regions, Towny plots, WorldBorder
     * borders and TARDIS areas.
     *
     * @param p  the player who is travelling.
     * @param to the player to request travel to.
     * @param l  the location to check.
     * @return true or false depending on whether the player is allowed to travel to the specified location
     */
    public boolean getRequest(Player p, Player to, Location l) {
        boolean bool = true;
        if (plugin.getConfig().getBoolean("travel.per_world_perms")) {
            String perm = l.getWorld().getName();
            if (!TARDISPermission.hasPermission(p, "tardis.travel." + perm)) {
                TARDISMessage.send(p, "TRAVEL_NO_PERM_WORLD", perm);
                bool = false;
            }
        }
        // nether travel
        if (l.getWorld().getEnvironment().equals(Environment.NETHER)) {
            // check if nether enabled
            if (!plugin.getConfig().getBoolean("travel.nether")) {
                TARDISMessage.send(p, "TRAVEL_DISABLED", "Nether");
                bool = false;
            }
            // check permission
            if (!TARDISPermission.hasPermission(p, "tardis.nether")) {
                TARDISMessage.send(p, "NO_PERM_TRAVEL", "Nether");
                bool = false;
            }
            // check if player has to visit first
            if (plugin.getConfig().getBoolean("travel.allow_nether_after_visit") && !new ResultSetTravelledTo(plugin).resultSet(p.getUniqueId().toString(), "NETHER")) {
                TARDISMessage.send(p, "TRAVEL_NOT_VISITED", "Nether");
                bool = false;
            }
        }
        // end travel
        if (l.getWorld().getEnvironment().equals(Environment.THE_END)) {
            // check if end enabled
            if (!plugin.getConfig().getBoolean("travel.the_end")) {
                TARDISMessage.send(p, "TRAVEL_DISABLED", "End");
                bool = false;
            }
            // check permission
            if (!TARDISPermission.hasPermission(p, "tardis.end")) {
                TARDISMessage.send(p, "NO_PERM_TRAVEL", "End");
                bool = false;
            }
            // check if player has to visit first
            if (plugin.getConfig().getBoolean("allow_end_after_visit") && !new ResultSetTravelledTo(plugin).resultSet(p.getUniqueId().toString(), "THE_END")) {
                TARDISMessage.send(p, "TRAVEL_NOT_VISITED", "End");
                bool = false;
            }
        }
        if (plugin.isWorldGuardOnServer() && !plugin.getWorldGuardUtils().canBuild(to, l)) {
            TARDISMessage.send(p, "WORLDGUARD");
            bool = false;
        }
        if (plugin.getPluginRespect().isTownyOnServer() && !plugin.getConfig().getString("preferences.respect_towny").equals("none") && !plugin.getPluginRespect().getTychk().checkTowny(to, l)) {
            TARDISMessage.send(p, "TOWNY");
            bool = false;
        }
        if (plugin.getPluginRespect().isBorderOnServer() && plugin.getConfig().getBoolean("preferences.respect_worldborder") && !plugin.getPluginRespect().getBorderchk().isInBorder(l)) {
            TARDISMessage.send(p, "WORLDBORDER");
            bool = false;
        }
        if (plugin.getPluginRespect().isFactionsOnServer() && plugin.getConfig().getBoolean("preferences.respect_factions") && !TARDISFactionsChecker.isInFaction(to, l)) {
            TARDISMessage.send(p, "FACTIONS");
            bool = false;
        }
        if (plugin.getPluginRespect().isGriefPreventionOnServer() && plugin.getConfig().getBoolean("preferences.respect_grief_prevention") && !plugin.getPluginRespect().getGriefchk().isInClaim(to, l)) {
            TARDISMessage.send(p, "GRIEFPREVENTION");
            bool = false;
        }
        if (plugin.getPluginRespect().isRedProtectOnServer() && plugin.getConfig().getBoolean("preferences.respect_red_protect") && !TARDISRedProtectChecker.canBuild(to, l)) {
            TARDISMessage.send(p, "REDPROTECT");
            bool = false;
        }

        if (plugin.getTardisArea().areaCheckLocPlayer(p, l)) {
            TARDISMessage.send(p, "TRAVEL_NO_PERM", plugin.getTrackerKeeper().getPerm().get(p.getUniqueId()));
            plugin.getTrackerKeeper().getPerm().remove(p.getUniqueId());
            bool = false;
        }
        return bool;
    }
}
