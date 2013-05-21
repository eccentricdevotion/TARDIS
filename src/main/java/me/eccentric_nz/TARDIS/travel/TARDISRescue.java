/*
 * Copyright (C) 2013 eccentric_nz
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
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISRescue {

    private final TARDIS plugin;

    public TARDISRescue(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean rescue(Player player, String saved, int id, TARDISTimetravel tt, TARDISConstants.COMPASS d, boolean rescue) {
        if (plugin.getServer().getPlayer(saved) == null) {
            player.sendMessage(plugin.pluginName + "That player is not online!");
            return true;
        }
        Player destPlayer = plugin.getServer().getPlayer(saved);
        Location player_loc = destPlayer.getLocation();
        if (!plugin.ta.areaCheckInExisting(player_loc)) {
            player.sendMessage(plugin.pluginName + "The player is in a TARDIS area! Please use " + ChatColor.AQUA + "/tardistravel area [area name]");
            return true;
        }
        World w = player_loc.getWorld();
        int[] start_loc = tt.getStartLocation(player_loc, d);
        int move = (rescue) ? 0 : 3;
        int count = tt.safeLocation(start_loc[0] - move, player_loc.getBlockY(), start_loc[2], start_loc[1] - move, start_loc[3], w, d);
        if (count > 0) {
            player.sendMessage(plugin.pluginName + "The player's location would not be safe! Please tell the player to move!");
            return true;
        }
        TARDISPluginRespect respect = new TARDISPluginRespect(plugin);
        if (!respect.getRespect(player, player_loc, true)) {
            return true;
        }
        if (!plugin.getConfig().getBoolean("worlds." + player_loc.getWorld().getName())) {
            player.sendMessage(plugin.pluginName + "The server does not allow time travel to this world!");
            return true;
        }
        String save_loc = player_loc.getWorld().getName() + ":" + (player_loc.getBlockX() - move) + ":" + player_loc.getBlockY() + ":" + player_loc.getBlockZ();
        HashMap<String, Object> set = new HashMap<String, Object>();
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        set.put("save", save_loc);
        QueryFactory qf = new QueryFactory(plugin);
        qf.doUpdate("tardis", set, where);
        player.sendMessage(plugin.pluginName + "The player location was saved succesfully. Please release the handbrake!");
        plugin.tardisHasDestination.put(id, plugin.getArtronConfig().getInt("travel"));
        if (rescue) {
            plugin.trackRescue.put(id, saved);
        }
        return true;
    }

    public boolean tryRescue(Player player, String saved) {
        if (player.hasPermission("tardis.timetravel") && !(player.hasPermission("tardis.exile") && plugin.getConfig().getBoolean("exile"))) {
            TARDISTimetravel tt = new TARDISTimetravel(plugin);
            // get tardis data
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("owner", player.getName());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                player.sendMessage(plugin.pluginName + "You are not a Timelord. You need to create a TARDIS before using this command!");
                return true;
            }
            int id = rs.getTardis_id();
            if (!rs.isHandbrake_on()) {
                player.sendMessage(plugin.pluginName + ChatColor.RED + "You cannot set a destination while the TARDIS is travelling!");
                return true;
            }
            HashMap<String, Object> wheret = new HashMap<String, Object>();
            wheret.put("player", player.getName());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            if (!rst.resultSet()) {
                player.sendMessage(plugin.pluginName + "You are not inside the TARDIS. You need to be to run this command!");
                return true;
            }
            int tardis_id = rst.getTardis_id();
            if (tardis_id != id) {
                player.sendMessage(plugin.pluginName + "You can only run this command if you are the Timelord of " + ChatColor.LIGHT_PURPLE + "this" + ChatColor.RESET + " TARDIS!");
                return true;
            }
            int level = rs.getArtron_level();
            int travel = plugin.getArtronConfig().getInt("travel");
            if (level < travel) {
                player.sendMessage(plugin.pluginName + ChatColor.RED + "The TARDIS does not have enough Artron Energy to make this trip!");
                return true;
            }
            TARDISConstants.COMPASS d = rs.getDirection();
            return rescue(player, saved, id, tt, d, true);
        } else {
            return false;
        }
    }
}
