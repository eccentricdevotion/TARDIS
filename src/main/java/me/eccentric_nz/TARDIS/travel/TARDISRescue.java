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

import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Rescue Operational Security Module G723 (ROSM) was an artificial intelligence
 * built into a spacecraft working around the Cimmerian System. It was designed
 * by Professor Astrov to protect company interests and property, including
 * rescue of company employees from danger.
 *
 * @author eccentric_nz
 */
public class TARDISRescue {

    private final TARDIS plugin;

    public TARDISRescue(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Move the Police Box to a player's location, and potentially rescue the
     * player.
     *
     * @param player The Time Lord
     * @param saved The player to be rescued
     * @param id The TARDIS unique ID
     * @param tt an instance of the TARDISTimeTravel class
     * @param d the direction the Police Box is facing
     * @param rescue whether to rescue the player
     * @return true or false
     */
    public boolean rescue(Player player, UUID saved, int id, TARDISTimeTravel tt, COMPASS d, boolean rescue) {
        if (plugin.getServer().getPlayer(saved) == null) {
            TARDISMessage.send(player, plugin.getPluginName() + "That player is not online!");
            return (!rescue);
        }
        Player destPlayer = plugin.getServer().getPlayer(saved);
        Location player_loc = destPlayer.getLocation();
        if (!plugin.getTardisArea().areaCheckInExisting(player_loc)) {
            TARDISMessage.send(player, plugin.getPluginName() + "The player is in a TARDIS area! Please use " + ChatColor.AQUA + "/tardistravel area [area name]");
            return (!rescue);
        }
        if (!plugin.getPluginRespect().getRespect(player, player_loc, true)) {
            return (!rescue);
        }
        if (!plugin.getConfig().getBoolean("worlds." + player_loc.getWorld().getName())) {
            TARDISMessage.send(player, plugin.getPluginName() + "The server does not allow time travel to this world!");
            return (!rescue);
        }
        World w = player_loc.getWorld();
        int[] start_loc = tt.getStartLocation(player_loc, d);
        int move = (rescue) ? 0 : 3;
        int count = tt.safeLocation(start_loc[0] - move, player_loc.getBlockY(), start_loc[2], start_loc[1] - move, start_loc[3], w, d);
        if (count > 0) {
            TARDISMessage.send(player, plugin.getPluginName() + "The player's location would not be safe! Please tell the player to move!");
            return (!rescue);
        }
        HashMap<String, Object> set = new HashMap<String, Object>();
        set.put("world", player_loc.getWorld().getName());
        set.put("x", (player_loc.getBlockX() - move));
        set.put("y", player_loc.getBlockY());
        set.put("z", player_loc.getBlockZ());
        set.put("direction", d.toString());
        set.put("submarine", 0);
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        QueryFactory qf = new QueryFactory(plugin);
        qf.doUpdate("next", set, where);
        if (!rescue) {
            TARDISMessage.send(player, plugin.getPluginName() + "The player location was saved successfully. Please release the handbrake!");
        }
        plugin.getTrackerKeeper().getTrackHasDestination().put(id, plugin.getArtronConfig().getInt("travel"));
        if (rescue) {
            plugin.getTrackerKeeper().getTrackRescue().put(id, saved);
        }
        return true;
    }

    /**
     * Check whether a Time Lord can rescue a player, and then rescue them.
     *
     * @param player The Time Lord
     * @param saved The player to be rescued
     * @return true if rescue was successful
     */
    public boolean tryRescue(Player player, UUID saved) {
        if (player.hasPermission("tardis.timetravel") && !(player.hasPermission("tardis.exile") && plugin.getConfig().getBoolean("travel.exile"))) {
            TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
            // get tardis data
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NO_TARDIS.getText());
                return false;
            }
            int id = rs.getTardis_id();
            if (!rs.isHandbrake_on()) {
                TARDISMessage.send(player, plugin.getPluginName() + ChatColor.RED + MESSAGE.NOT_WHILE_TRAVELLING.getText());
                return false;
            }
            HashMap<String, Object> wheret = new HashMap<String, Object>();
            wheret.put("uuid", player.getUniqueId().toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            if (!rst.resultSet()) {
                TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NOT_IN_TARDIS.getText());
                return false;
            }
            int tardis_id = rst.getTardis_id();
            if (tardis_id != id) {
                TARDISMessage.send(player, plugin.getPluginName() + "You can only run this command if you are the Time Lord of " + ChatColor.LIGHT_PURPLE + "this" + ChatColor.RESET + " TARDIS!");
                return false;
            }
            int level = rs.getArtron_level();
            int travel = plugin.getArtronConfig().getInt("travel");
            if (level < travel) {
                TARDISMessage.send(player, plugin.getPluginName() + ChatColor.RED + MESSAGE.NOT_ENOUGH_ENERGY.getText());
                return false;
            }
            // get direction
            HashMap<String, Object> wherecl = new HashMap<String, Object>();
            wherecl.put("tardis_id", id);
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
            if (!rsc.resultSet()) {
                TARDISMessage.send(player, plugin.getPluginName() + ChatColor.RED + MESSAGE.NO_CURRENT.getText());
                return false;
            }
            return rescue(player, saved, id, tt, rsc.getDirection(), true);
        } else {
            return false;
        }
    }
}
