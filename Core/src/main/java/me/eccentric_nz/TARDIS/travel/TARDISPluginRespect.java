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
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravelledTo;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.protection.TARDISGriefPreventionChecker;
import me.eccentric_nz.TARDIS.utility.protection.TARDISTownyChecker;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.WorldBorder;

/**
 * The telepathic password to the TARDIS was "the colour crimson, the number
 * eleven, the feeling of delight, and the smell of dust after rain".
 *
 * @author eccentric_nz
 */
public class TARDISPluginRespect {

    private final TARDIS plugin;
    private TARDISTownyChecker tychk;
    private TARDISGriefPreventionChecker griefchk;
    private boolean townyOnServer = false;
    private boolean griefPreventionOnServer = false;

    public TARDISPluginRespect(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Checks whether a location is allowed by other plugins. This checks
     * WorldGuard regions, Towny plots, WorldBorder borders and TARDIS areas.
     *
     * @param location the location to check.
     * @param flag a list of flags to check (including whether to message the
     * player).
     * @return true or false depending on whether the player is allowed to
     * travel to the specified location
     */
    public boolean getRespect(Location location, Parameters flag) {
        boolean bool = true;
        if (plugin.getConfig().getBoolean("allow.admin_bypass") && flag.getPlayer() != null && flag.getPlayer().hasPermission("tardis.admin")) {
            return true;
        }
        if (plugin.getConfig().getBoolean("travel.per_world_perms")) {
            String perm = location.getWorld().getName();
            if (!TARDISPermission.hasPermission(flag.getPlayer(), "tardis.travel." + perm)) {
                if (flag.messagePlayer()) {
                    plugin.getMessenger().send(flag.getPlayer(), TardisModule.TARDIS, "TRAVEL_NO_PERM_WORLD", perm);
                }
                bool = false;
            }
        }
        // nether travel
        if (flag.permsNether() && location.getWorld().getEnvironment().equals(Environment.NETHER)) {
            // check if nether enabled
            if (!plugin.getConfig().getBoolean("travel.nether")) {
                if (flag.messagePlayer()) {
                    plugin.getMessenger().send(flag.getPlayer(), TardisModule.TARDIS, "TRAVEL_DISABLED", "Nether");
                }
                bool = false;
            }
            // check permission
            if (!TARDISPermission.hasPermission(flag.getPlayer(), "tardis.nether")) {
                if (flag.messagePlayer()) {
                    plugin.getMessenger().send(flag.getPlayer(), TardisModule.TARDIS, "NO_PERM_TRAVEL", "Nether");
                }
                bool = false;
            }
            // check if player has to visit first
            if (plugin.getConfig().getBoolean("travel.allow_nether_after_visit") && !new ResultSetTravelledTo(plugin).resultSet(flag.getPlayer().getUniqueId().toString(), "NETHER")) {
                if (flag.messagePlayer()) {
                    plugin.getMessenger().send(flag.getPlayer(), TardisModule.TARDIS, "TRAVEL_NOT_VISITED", "Nether");
                }
                bool = false;
            }
        }
        // end travel
        if (flag.permsTheEnd() && location.getWorld().getEnvironment().equals(Environment.THE_END)) {
            // check if end enabled
            if (!plugin.getConfig().getBoolean("travel.the_end")) {
                if (flag.messagePlayer()) {
                    plugin.getMessenger().send(flag.getPlayer(), TardisModule.TARDIS, "TRAVEL_DISABLED", "End");
                }
                bool = false;
            }
            // check permission
            if (!TARDISPermission.hasPermission(flag.getPlayer(), "tardis.end")) {
                if (flag.messagePlayer()) {
                    plugin.getMessenger().send(flag.getPlayer(), TardisModule.TARDIS, "NO_PERM_TRAVEL", "End");
                }
                bool = false;
            }
            // check if player has to visit first
            if (plugin.getConfig().getBoolean("travel.allow_end_after_visit") && !new ResultSetTravelledTo(plugin).resultSet(flag.getPlayer().getUniqueId().toString(), "THE_END")) {
                if (flag.messagePlayer()) {
                    plugin.getMessenger().send(flag.getPlayer(), TardisModule.TARDIS, "TRAVEL_NOT_VISITED", "End");
                }
                bool = false;
            }
        }
        if (flag.respectWorldguard() && plugin.isWorldGuardOnServer() && !plugin.getWorldGuardUtils().canLand(flag.getPlayer(), location)) {
            if (flag.messagePlayer()) {
                plugin.getMessenger().send(flag.getPlayer(), TardisModule.TARDIS, "WORLDGUARD");
            }
            bool = false;
        }
        if (flag.respectTowny() && townyOnServer && !plugin.getConfig().getString("preferences.respect_towny").equals("none") && !tychk.checkTowny(flag.getPlayer(), location)) {
            if (flag.messagePlayer()) {
                plugin.getMessenger().send(flag.getPlayer(), TardisModule.TARDIS, "TOWNY");
            }
            bool = false;
        }
        if (flag.repectWorldBorder()) {
                WorldBorder wb = location.getWorld().getWorldBorder();
                if (!wb.isInside(location)) {
                    if (flag.messagePlayer()) {
                        plugin.getMessenger().send(flag.getPlayer(), TardisModule.TARDIS, "WORLDBORDER");
                    }
                    bool = false;
                }
        }
        if (flag.respectGreifPrevention() && griefPreventionOnServer && plugin.getConfig().getBoolean("preferences.respect_grief_prevention") && griefchk.isInClaim(flag.getPlayer(), location)) {
            if (flag.messagePlayer()) {
                plugin.getMessenger().send(flag.getPlayer(), TardisModule.TARDIS, "GRIEFPREVENTION");
            }
            bool = false;
        }
        if (flag.permsArea() && plugin.getTardisArea().areaCheckLocPlayer(flag.getPlayer(), location)) {
            if (flag.messagePlayer()) {
                String area_perm = plugin.getTrackerKeeper().getPerm().get(flag.getPlayer().getUniqueId());
                String area_name = "tardis.area." + plugin.getConfig().getString("creation.area");
                if (area_perm.equals(area_name)) {
                    plugin.getMessenger().sendColouredCommand(flag.getPlayer(), "TARDIS_SET_HOME", "/tardis home", plugin);
                } else {
                    plugin.getMessenger().send(flag.getPlayer(), TardisModule.TARDIS, "TRAVEL_NO_PERM", area_perm);
                }
            }
            plugin.getTrackerKeeper().getPerm().remove(flag.getPlayer().getUniqueId());
            bool = false;
        }
        return bool;
    }

    /**
     * Checks if the Towny plugin is available, and loads support if it is.
     */
    public void loadTowny() {
        if (plugin.getPM().getPlugin("Towny") != null) {
            townyOnServer = true;
            tychk = new TARDISTownyChecker(plugin);
        }
    }

    /**
     * Checks if the GriefPrevention plugin is available, and loads support if
     * it is.
     */
    public void loadGriefPrevention() {
        if (plugin.getPM().getPlugin("GriefPrevention") != null) {
            plugin.debug("Hooking into GriefPrevention!");
            griefPreventionOnServer = true;
            griefchk = new TARDISGriefPreventionChecker(plugin);
        }
    }

    TARDISTownyChecker getTychk() {
        return tychk;
    }

    TARDISGriefPreventionChecker getGriefchk() {
        return griefchk;
    }

    boolean isTownyOnServer() {
        return townyOnServer;
    }

    boolean isGriefPreventionOnServer() {
        return griefPreventionOnServer;
    }
}
