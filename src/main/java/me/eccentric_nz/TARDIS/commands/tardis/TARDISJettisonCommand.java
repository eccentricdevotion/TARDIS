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
package me.eccentric_nz.TARDIS.commands.tardis;

import java.util.HashMap;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISJettisonCommand {

    private final TARDIS plugin;

    public TARDISJettisonCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean startJettison(Player player, String[] args) {
        if (player.hasPermission("tardis.room")) {
            if (args.length < 2) {
                player.sendMessage(plugin.getPluginName() + MESSAGE.TOO_FEW_ARGS.getText());
                return false;
            }
            String room = args[1].toUpperCase(Locale.ENGLISH);
            if (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) {
                player.sendMessage(plugin.getPluginName() + "Please make sure you have removed the gravity blocks with the " + ChatColor.AQUA + "/tardisgravity remove" + ChatColor.RESET + " command before proceeding!");
            }
            if (!plugin.getGeneralKeeper().getRoomArgs().contains(room)) {
                StringBuilder buf = new StringBuilder(args[1]);
                for (String rl : plugin.getGeneralKeeper().getRoomArgs()) {
                    buf.append(rl).append(", ");
                }
                String roomlist = buf.toString().substring(0, buf.length() - 2);
                player.sendMessage(plugin.getPluginName() + "That is not a valid room type! Try one of: " + roomlist);
                return true;
            }
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("owner", player.getName());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                player.sendMessage(plugin.getPluginName() + MESSAGE.NOT_A_TIMELORD.getText());
                return true;
            }
            int id = rs.getTardis_id();
            // check they are in the tardis
            HashMap<String, Object> wheret = new HashMap<String, Object>();
            wheret.put("player", player.getName());
            wheret.put("tardis_id", id);
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            if (!rst.resultSet()) {
                player.sendMessage(plugin.getPluginName() + MESSAGE.NOT_IN_TARDIS.getText());
                return true;
            }
            plugin.getTrackerKeeper().getTrackJettison().put(player.getName(), room);
            String seed = plugin.getArtronConfig().getString("jettison_seed");
            player.sendMessage(plugin.getPluginName() + "Place a " + seed + " block in front of the pressure plate leading to the " + room + ". Hit the " + seed + " with the TARDIS key to jettison the room!");
            return true;
        } else {
            player.sendMessage(plugin.getPluginName() + MESSAGE.NO_PERMS.getText());
            return false;
        }
    }
}
