/*
 * Copyright (C) 2014 eccentric_nz
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
import me.eccentric_nz.TARDIS.utility.TARDISFactionsChecker;
import me.eccentric_nz.TARDIS.utility.TARDISGriefPreventionChecker;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISTownyChecker;
import me.eccentric_nz.TARDIS.utility.TARDISWorldBorderChecker;
import me.eccentric_nz.TARDIS.utility.Version;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

/**
 * The telepathic password to the TARDIS was "the colour crimson, the number
 * eleven, the feeling of delight, and the smell of dust after rain".
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
     * Checks whether a location is allowed by other plugins. This checks
     * WorldGuard regions, Towny plots, WorldBorder borders and TARDIS areas.
     *
     * @param p the player who is travelling.
     * @param l the location to check.
     * @param message whether to display a message to the sender.
     * @return true or false depending on whether the player is allowed to
     * travel to the specified location
     */
    public boolean getRespect(Player p, Location l, boolean message) {
        boolean bool = true;
        if (plugin.getConfig().getBoolean("travel.per_world_perms")) {
            String perm = l.getWorld().getName();
            if (!p.hasPermission("tardis.travel." + perm)) {
                if (message) {
                    TARDISMessage.send(p, "TRAVEL_NO_PERM_WORLD", perm);
                }
                bool = false;
            }
        }
        if (!plugin.getConfig().getBoolean("travel.nether") && l.getWorld().getEnvironment().equals(Environment.NETHER)) {
            if (message) {
                TARDISMessage.send(p, "TRAVEL_DISABLED", "Nether");
            }
            bool = false;
        }
        if (!p.hasPermission("tardis.nether") && l.getWorld().getEnvironment().equals(Environment.NETHER)) {
            if (message) {
                TARDISMessage.send(p, "NO_PERM_TRAVEL", "Nether");
            }
            bool = false;
        }
        if (!plugin.getConfig().getBoolean("travel.the_end") && l.getWorld().getEnvironment().equals(Environment.THE_END)) {
            if (message) {
                TARDISMessage.send(p, "TRAVEL_DISABLED", "End");
            }
            bool = false;
        }
        if (!p.hasPermission("tardis.end") && l.getWorld().getEnvironment().equals(Environment.THE_END)) {
            if (message) {
                TARDISMessage.send(p, "NO_PERM_TRAVEL", "End");
            }
            bool = false;
        }
        if (plugin.isWorldGuardOnServer() && !plugin.getWorldGuardUtils().canLand(p, l)) {
            if (message) {
                TARDISMessage.send(p, "WORLDGUARD");
            }
            bool = false;
        }
        if (townyOnServer && !plugin.getConfig().getString("preferences.respect_towny").equals("none") && !tychk.checkTowny(p, l)) {
            if (message) {
                TARDISMessage.send(p, "TOWNY");
            }
            bool = false;
        }
        if (borderOnServer && plugin.getConfig().getBoolean("preferences.respect_worldborder") && !borderchk.isInBorder(l)) {
            if (message) {
                TARDISMessage.send(p, "WORLDBORDER");
            }
            bool = false;
        }
        if (factionsOnServer && plugin.getConfig().getBoolean("preferences.respect_factions") && !factionschk.isInFaction(p, l)) {
            if (message) {
                TARDISMessage.send(p, "FACTIONS");
            }
            bool = false;
        }
        if (griefPreventionOnServer && plugin.getConfig().getBoolean("preferences.respect_grief_prevention") && griefchk.isInClaim(p, l)) {
            if (message) {
                TARDISMessage.send(p, "GRIEFPREVENTION");
            }
            bool = false;
        }
        if (plugin.getTardisArea().areaCheckLocPlayer(p, l)) {
            if (message) {
                String area_perm = plugin.getTrackerKeeper().getPerm().get(p.getUniqueId());
                String area_name = "tardis.area." + plugin.getConfig().getString("creation.area");
                if (area_perm.equals(area_name)) {
                    TARDISMessage.send(p, "TARDIS_SET_HOME");
                } else {
                    TARDISMessage.send(p, "TRAVEL_NO_PERM", area_perm);
                }
            }
            plugin.getTrackerKeeper().getPerm().remove(p.getUniqueId());
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
            tychk = new TARDISTownyChecker(plugin, townyOnServer);
        }
    }

    /**
     * Checks if the WorldBorder plugin is available, and loads support if it
     * is.
     */
    public void loadWorldBorder() {
        if (plugin.getPM().getPlugin("WorldBorder") != null) {
            borderOnServer = true;
            borderchk = new TARDISWorldBorderChecker(plugin, borderOnServer);
        }
    }

    /**
     * Checks if the Factions plugin is available, and loads support if it is.
     */
    public void loadFactions() {
        if (plugin.getPM().getPlugin("Factions") != null) {
            Version version = new Version(plugin.getPM().getPlugin("Factions").getDescription().getVersion());
            Version min_version = new Version("2.0");
            if (version.compareTo(min_version) >= 0) {
                factionsOnServer = true;
                factionschk = new TARDISFactionsChecker(plugin);
            } else {
                plugin.getConsole().sendMessage(plugin.getPluginName() + "This version of TARDIS is not compatible with Factions " + version.toString() + ", please update to Factions 2.0 or higher.");
            }
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
            griefchk = new TARDISGriefPreventionChecker(plugin, griefPreventionOnServer);
        }
    }

    public TARDIS getPlugin() {
        return plugin;
    }

    public TARDISTownyChecker getTychk() {
        return tychk;
    }

    public TARDISWorldBorderChecker getBorderchk() {
        return borderchk;
    }

    public TARDISFactionsChecker getFactionschk() {
        return factionschk;
    }

    public TARDISGriefPreventionChecker getGriefchk() {
        return griefchk;
    }

    public boolean isTownyOnServer() {
        return townyOnServer;
    }

    public boolean isBorderOnServer() {
        return borderOnServer;
    }

    public boolean isFactionsOnServer() {
        return factionsOnServer;
    }

    public boolean isGriefPreventionOnServer() {
        return griefPreventionOnServer;
    }
}
