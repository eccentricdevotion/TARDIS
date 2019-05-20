/*
 * Copyright (C) 2019 eccentric_nz
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
import me.eccentric_nz.TARDIS.utility.*;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.WorldBorder;

/**
 * The telepathic password to the TARDIS was "the colour crimson, the number eleven, the feeling of delight, and the
 * smell of dust after rain".
 *
 * @author eccentric_nz
 */
public class TARDISPluginRespect {

    private final TARDIS plugin;
    private TARDISTownyChecker tychk;
    private TARDISWorldBorderChecker borderchk;
    private TARDISFactionsChecker factionschk;
    private TARDISGriefPreventionChecker griefchk;
    private boolean townyOnServer = false;
    private boolean borderOnServer = false;
    private boolean factionsOnServer = false;
    private boolean griefPreventionOnServer = false;

    public TARDISPluginRespect(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Checks whether a location is allowed by other plugins. This checks WorldGuard regions, Towny plots, WorldBorder
     * borders and TARDIS areas.
     *
     * @param l    the location to check.
     * @param flag a list of flags to check (including whether to message the player).
     * @return true or false depending on whether the player is allowed to travel to the specified location
     */
    public boolean getRespect(Location l, Parameters flag) {
        boolean bool = true;
        if (plugin.getConfig().getBoolean("travel.per_world_perms")) {
            String perm = l.getWorld().getName();
            if (!flag.getPlayer().hasPermission("tardis.travel." + perm)) {
                if (flag.messagePlayer()) {
                    TARDISMessage.send(flag.getPlayer(), "TRAVEL_NO_PERM_WORLD", perm);
                }
                bool = false;
            }
        }
        if (flag.permsNether() && !plugin.getConfig().getBoolean("travel.nether") && l.getWorld().getEnvironment().equals(Environment.NETHER)) {
            if (flag.messagePlayer()) {
                TARDISMessage.send(flag.getPlayer(), "TRAVEL_DISABLED", "Nether");
            }
            bool = false;
        }
        if (flag.permsNether() && !flag.getPlayer().hasPermission("tardis.nether") && l.getWorld().getEnvironment().equals(Environment.NETHER)) {
            if (flag.messagePlayer()) {
                TARDISMessage.send(flag.getPlayer(), "NO_PERM_TRAVEL", "Nether");
            }
            bool = false;
        }
        if (flag.permsTheEnd() && !plugin.getConfig().getBoolean("travel.the_end") && l.getWorld().getEnvironment().equals(Environment.THE_END)) {
            if (flag.messagePlayer()) {
                TARDISMessage.send(flag.getPlayer(), "TRAVEL_DISABLED", "End");
            }
            bool = false;
        }
        if (flag.permsTheEnd() && !flag.getPlayer().hasPermission("tardis.end") && l.getWorld().getEnvironment().equals(Environment.THE_END)) {
            if (flag.messagePlayer()) {
                TARDISMessage.send(flag.getPlayer(), "NO_PERM_TRAVEL", "End");
            }
            bool = false;
        }
        if (flag.respectWorldguard() && plugin.isWorldGuardOnServer() && !plugin.getWorldGuardUtils().canLand(flag.getPlayer(), l)) {
            if (flag.messagePlayer()) {
                TARDISMessage.send(flag.getPlayer(), "WORLDGUARD");
            }
            bool = false;
        }
        if (flag.respectTowny() && townyOnServer && !plugin.getConfig().getString("preferences.respect_towny").equals("none") && !tychk.checkTowny(flag.getPlayer(), l)) {
            if (flag.messagePlayer()) {
                TARDISMessage.send(flag.getPlayer(), "TOWNY");
            }
            bool = false;
        }
        if (flag.repectWorldBorder()) {
            if (plugin.isHelperOnServer()) {
                WorldBorder wb = l.getWorld().getWorldBorder();
                if (!wb.isInside(l)) {
                    if (flag.messagePlayer()) {
                        TARDISMessage.send(flag.getPlayer(), "WORLDBORDER");
                    }
                    bool = false;
                }
            }
            if (borderOnServer && plugin.getConfig().getBoolean("preferences.respect_worldborder") && !borderchk.isInBorder(l)) {
                if (flag.messagePlayer()) {
                    TARDISMessage.send(flag.getPlayer(), "WORLDBORDER");
                }
                bool = false;
            }
        }
        if (flag.respectFactions() && factionsOnServer && plugin.getConfig().getBoolean("preferences.respect_factions") && !factionschk.isInFaction(flag.getPlayer(), l)) {
            if (flag.messagePlayer()) {
                TARDISMessage.send(flag.getPlayer(), "FACTIONS");
            }
            bool = false;
        }
        if (flag.respectGreifPrevention() && griefPreventionOnServer && plugin.getConfig().getBoolean("preferences.respect_grief_prevention") && griefchk.isInClaim(flag.getPlayer(), l)) {
            if (flag.messagePlayer()) {
                TARDISMessage.send(flag.getPlayer(), "GRIEFPREVENTION");
            }
            bool = false;
        }
        if (flag.permsArea() && plugin.getTardisArea().areaCheckLocPlayer(flag.getPlayer(), l)) {
            if (flag.messagePlayer()) {
                String area_perm = plugin.getTrackerKeeper().getPerm().get(flag.getPlayer().getUniqueId());
                String area_name = "tardis.area." + plugin.getConfig().getString("creation.area");
                if (area_perm.equals(area_name)) {
                    TARDISMessage.send(flag.getPlayer(), "TARDIS_SET_HOME");
                } else {
                    TARDISMessage.send(flag.getPlayer(), "TRAVEL_NO_PERM", area_perm);
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
     * Checks if the WorldBorder plugin is available, and loads support if it is.
     */
    public void loadWorldBorder() {
        if (plugin.getPM().getPlugin("WorldBorder") != null) {
            borderOnServer = true;
            borderchk = new TARDISWorldBorderChecker(plugin);
        }
    }

    /**
     * Checks if the Factions plugin is available, and loads support if it is.
     */
    public void loadFactions() {
        if (plugin.getPM().getPlugin("Factions") != null) {
            factionsOnServer = true;
            factionschk = new TARDISFactionsChecker();
        }
    }

    /**
     * Checks if the GriefPrevention plugin is available, and loads support if it is.
     */
    public void loadGriefPrevention() {
        if (plugin.getPM().getPlugin("GriefPrevention") != null) {
            plugin.debug("Hooking into GriefPrevention!");
            griefPreventionOnServer = true;
            griefchk = new TARDISGriefPreventionChecker(plugin);
        }
    }

    public TARDIS getPlugin() {
        return plugin;
    }

    TARDISTownyChecker getTychk() {
        return tychk;
    }

    TARDISWorldBorderChecker getBorderchk() {
        return borderchk;
    }

    TARDISFactionsChecker getFactionschk() {
        return factionschk;
    }

    TARDISGriefPreventionChecker getGriefchk() {
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
}
