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
package me.eccentric_nz.tardis.travel;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.api.Parameters;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravelledTo;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.*;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.WorldBorder;

import java.util.Objects;

/**
 * The telepathic password to the tardis was "the colour crimson, the number eleven, the feeling of delight, and the
 * smell of dust after rain".
 *
 * @author eccentric_nz
 */
public class TardisPluginRespect {

    private final TardisPlugin plugin;
    private TardisTownyChecker tychk;
    private TardisWorldBorderChecker borderchk;
    private TardisGriefPreventionChecker griefchk;
    private boolean townyOnServer = false;
    private boolean borderOnServer = false;
    private boolean factionsOnServer = false;
    private boolean griefPreventionOnServer = false;
    private boolean redProtectOnServer = false;

    public TardisPluginRespect(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Checks whether a location is allowed by other plugins. This checks WorldGuard regions, Towny plots, WorldBorder
     * borders and tardis areas.
     *
     * @param location the location to check.
     * @param flag     a list of flags to check (including whether to message the player).
     * @return true or false depending on whether the player is allowed to travel to the specified location
     */
    public boolean getRespect(Location location, Parameters flag) {
        boolean bool = true;
        if (plugin.getConfig().getBoolean("allow.admin_bypass") && flag.getPlayer() != null && flag.getPlayer().hasPermission("tardis.admin")) {
            return true;
        }
        if (plugin.getConfig().getBoolean("travel.per_world_perms")) {
            String perm = Objects.requireNonNull(location.getWorld()).getName();
            if (!TardisPermission.hasPermission(flag.getPlayer(), "tardis.travel." + perm)) {
                if (flag.messagePlayer()) {
                    TardisMessage.send(flag.getPlayer(), "TRAVEL_NO_PERM_WORLD", perm);
                }
                bool = false;
            }
        }
        // nether travel
        if (flag.permsNether() && Objects.requireNonNull(location.getWorld()).getEnvironment().equals(Environment.NETHER)) {
            // check if nether enabled
            if (!plugin.getConfig().getBoolean("travel.nether")) {
                if (flag.messagePlayer()) {
                    TardisMessage.send(flag.getPlayer(), "TRAVEL_DISABLED", "Nether");
                }
                bool = false;
            }
            // check permission
            if (!TardisPermission.hasPermission(flag.getPlayer(), "tardis.nether")) {
                if (flag.messagePlayer()) {
                    TardisMessage.send(flag.getPlayer(), "NO_PERM_TRAVEL", "Nether");
                }
                bool = false;
            }
            // check if player has to visit first
            if (plugin.getConfig().getBoolean("travel.allow_nether_after_visit") && !new ResultSetTravelledTo(plugin).resultSet(flag.getPlayer().getUniqueId().toString(), "NETHER")) {
                if (flag.messagePlayer()) {
                    TardisMessage.send(flag.getPlayer(), "TRAVEL_NOT_VISITED", "Nether");
                }
                bool = false;
            }
        }
        // end travel
        if (flag.permsTheEnd() && Objects.requireNonNull(location.getWorld()).getEnvironment().equals(Environment.THE_END)) {
            // check if end enabled
            if (!plugin.getConfig().getBoolean("travel.the_end")) {
                if (flag.messagePlayer()) {
                    TardisMessage.send(flag.getPlayer(), "TRAVEL_DISABLED", "End");
                }
                bool = false;
            }
            // check permission
            if (!TardisPermission.hasPermission(flag.getPlayer(), "tardis.end")) {
                if (flag.messagePlayer()) {
                    TardisMessage.send(flag.getPlayer(), "NO_PERM_TRAVEL", "End");
                }
                bool = false;
            }
            // check if player has to visit first
            if (plugin.getConfig().getBoolean("travel.allow_end_after_visit") && !new ResultSetTravelledTo(plugin).resultSet(flag.getPlayer().getUniqueId().toString(), "THE_END")) {
                if (flag.messagePlayer()) {
                    TardisMessage.send(flag.getPlayer(), "TRAVEL_NOT_VISITED", "End");
                }
                bool = false;
            }
        }
        if (flag.respectWorldGuard() && plugin.isWorldGuardOnServer() && !plugin.getWorldGuardUtils().canLand(flag.getPlayer(), location)) {
            if (flag.messagePlayer()) {
                TardisMessage.send(flag.getPlayer(), "WORLDGUARD");
            }
            bool = false;
        }
        if (flag.respectTowny() && townyOnServer && !Objects.equals(plugin.getConfig().getString("preferences.respect_towny"), "none") && !tychk.checkTowny(flag.getPlayer(), location)) {
            if (flag.messagePlayer()) {
                TardisMessage.send(flag.getPlayer(), "TOWNY");
            }
            bool = false;
        }
        if (flag.respectWorldBorder()) {
            if (!borderOnServer) {
                WorldBorder wb = Objects.requireNonNull(location.getWorld()).getWorldBorder();
                if (!wb.isInside(location)) {
                    if (flag.messagePlayer()) {
                        TardisMessage.send(flag.getPlayer(), "WORLDBORDER");
                    }
                    bool = false;
                }
            }
            if (borderOnServer && plugin.getConfig().getBoolean("preferences.respect_worldborder") && !borderchk.isInBorder(location)) {
                if (flag.messagePlayer()) {
                    TardisMessage.send(flag.getPlayer(), "WORLDBORDER");
                }
                bool = false;
            }
        }
        if (flag.respectFactions() && factionsOnServer && plugin.getConfig().getBoolean("preferences.respect_factions") && !TardisFactionsChecker.isInFaction(flag.getPlayer(), location)) {
            if (flag.messagePlayer()) {
                TardisMessage.send(flag.getPlayer(), "FACTIONS");
            }
            bool = false;
        }
        if (flag.respectGriefPrevention() && griefPreventionOnServer && plugin.getConfig().getBoolean("preferences.respect_grief_prevention") && griefchk.isInClaim(flag.getPlayer(), location)) {
            if (flag.messagePlayer()) {
                TardisMessage.send(flag.getPlayer(), "GRIEFPREVENTION");
            }
            bool = false;
        }
        if (flag.respectRedProtect() && redProtectOnServer && plugin.getConfig().getBoolean("preferences.respect_red_protect") && !TardisRedProtectChecker.canBuild(flag.getPlayer(), location)) {
            if (flag.messagePlayer()) {
                TardisMessage.send(flag.getPlayer(), "REDPROTECT");
            }
            bool = false;
        }
        if (flag.permsArea() && plugin.getTardisArea().areaCheckLocPlayer(flag.getPlayer(), location)) {
            if (flag.messagePlayer()) {
                String areaPerm = plugin.getTrackerKeeper().getPerm().get(flag.getPlayer().getUniqueId());
                String areaName = "tardis.area." + plugin.getConfig().getString("creation.area");
                if (areaPerm.equals(areaName)) {
                    TardisMessage.send(flag.getPlayer(), "TARDIS_SET_HOME");
                } else {
                    TardisMessage.send(flag.getPlayer(), "TRAVEL_NO_PERM", areaPerm);
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
            tychk = new TardisTownyChecker(plugin);
        }
    }

    /**
     * Checks if the WorldBorder plugin is available, and loads support if it is.
     */
    public void loadWorldBorder() {
        if (plugin.getPM().getPlugin("WorldBorder") != null) {
            borderOnServer = true;
            borderchk = new TardisWorldBorderChecker(plugin);
        }
    }

    /**
     * Checks if the Factions plugin is available, and loads support if it is.
     */
    public void loadFactions() {
        if (plugin.getPM().getPlugin("Factions") != null) {
            factionsOnServer = true;
        }
    }

    /**
     * Checks if the GriefPrevention plugin is available, and loads support if it is.
     */
    public void loadGriefPrevention() {
        if (plugin.getPM().getPlugin("GriefPrevention") != null) {
            plugin.debug("Hooking into GriefPrevention!");
            griefPreventionOnServer = true;
            griefchk = new TardisGriefPreventionChecker(plugin);
        }
    }

    /**
     * Checks if the GriefPrevention plugin is available, and loads support if it is.
     */
    public void loadRedProtect() {
        if (plugin.getPM().getPlugin("RedProtect") != null) {
            plugin.debug("Hooking into RedProtect!");
            redProtectOnServer = true;
        }
    }

    TardisTownyChecker getTychk() {
        return tychk;
    }

    TardisWorldBorderChecker getBorderchk() {
        return borderchk;
    }

    TardisGriefPreventionChecker getGriefchk() {
        return griefchk;
    }

    boolean isTownyOnServer() {
        return townyOnServer;
    }

    boolean isBorderOnServer() {
        return borderOnServer;
    }

    boolean isFactionsOnServer() {
        return factionsOnServer;
    }

    boolean isGriefPreventionOnServer() {
        return griefPreventionOnServer;
    }

    boolean isRedProtectOnServer() {
        return redProtectOnServer;
    }
}
