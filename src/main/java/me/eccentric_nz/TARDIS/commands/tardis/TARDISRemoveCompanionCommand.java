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
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISRemoveCompanionCommand {

    private final TARDIS plugin;

    public TARDISRemoveCompanionCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean doRemoveCompanion(Player player, String[] args) {
        if (player.hasPermission("tardis.add")) {
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("owner", player.getName());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            String comps;
            int id;
            String[] data;
            if (!rs.resultSet()) {
                player.sendMessage(plugin.pluginName + TARDISConstants.NO_TARDIS);
                return false;
            } else {
                comps = rs.getCompanions();
                if (comps == null || comps.isEmpty()) {
                    player.sendMessage(plugin.pluginName + "You have not added any TARDIS companions yet!");
                    return true;
                }
                id = rs.getTardis_id();
                data = rs.getChunk().split(":");
            }
            if (args.length < 2) {
                player.sendMessage(plugin.pluginName + "Too few command arguments!");
                return false;
            }
            if (!args[1].matches("[A-Za-z0-9_]{2,16}")) {
                player.sendMessage(plugin.pluginName + "That doesn't appear to be a valid username");
                return false;
            } else {
                String newList = "";
                String message = "You removed " + ChatColor.GREEN + "ALL" + ChatColor.RESET + " your TARDIS companions.";
                if (!args[1].equals("all")) {
                    String[] split = comps.split(":");
                    StringBuilder buf = new StringBuilder();
                    if (split.length > 1) {
                        // recompile string without the specified player
                        for (String c : split) {
                            if (!c.equals(args[1].toLowerCase(Locale.ENGLISH))) {
                                // add to new string
                                buf.append(c).append(":");
                            }
                        }
                        // remove trailing colon
                        if (buf.length() > 0) {
                            newList = buf.toString().substring(0, buf.length() - 1);
                        }
                    }
                    message = "You removed " + ChatColor.GREEN + args[1] + ChatColor.RESET + " as a TARDIS companion.";
                }
                HashMap<String, Object> tid = new HashMap<String, Object>();
                HashMap<String, Object> set = new HashMap<String, Object>();
                tid.put("tardis_id", id);
                set.put("companions", newList);
                QueryFactory qf = new QueryFactory(plugin);
                qf.doUpdate("tardis", set, tid);
                // if using WorldGuard, add them to the region membership
                if (plugin.worldGuardOnServer && plugin.getConfig().getBoolean("use_worldguard")) {

                    plugin.getServer().dispatchCommand(plugin.console, "rg removemember tardis_" + player.getName() + " " + args[1].toLowerCase(Locale.ENGLISH) + " -w " + data[0]);
                }
                player.sendMessage(plugin.pluginName + message);
                return true;
            }
        } else {
            player.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
            return false;
        }
    }
}
