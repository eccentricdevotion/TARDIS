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
package me.eccentric_nz.TARDIS.commands.tardis;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.World;
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
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            String comps;
            int id;
            String[] data;
            if (!rs.resultSet()) {
                TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NO_TARDIS.getText());
                return false;
            } else {
                comps = rs.getCompanions();
                if (comps == null || comps.isEmpty()) {
                    TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.COMPANIONS_NONE.getText());
                    return true;
                }
                id = rs.getTardis_id();
                data = rs.getChunk().split(":");
            }
            if (args.length < 2) {
                TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.TOO_FEW_ARGS.getText());
                return false;
            }
            if (!args[1].matches("[A-Za-z0-9_]{2,16}")) {
                TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.PLAYER_NOT_VALID.getText());
                return true;
            } else {
                String newList = "";
                String message = "You removed " + ChatColor.GREEN + "ALL" + ChatColor.RESET + " your TARDIS companions.";
                if (!args[1].equals("all")) {
                    UUID oluuid = plugin.getServer().getOfflinePlayer(args[1]).getUniqueId();
                    if (oluuid == null) {
                        oluuid = plugin.getGeneralKeeper().getUUIDCache().getIdOptimistic(args[1]);
                        plugin.getGeneralKeeper().getUUIDCache().getId(args[1]);
                    }
                    if (oluuid != null) {
                        String[] split = comps.split(":");
                        StringBuilder buf = new StringBuilder();
                        if (split.length > 1) {
                            // recompile string without the specified player
                            for (String c : split) {
                                if (!c.equals(oluuid.toString())) {
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
                    } else {
                        TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.COULD_NOT_FIND_NAME.getText());
                        return true;
                    }
                }
                HashMap<String, Object> tid = new HashMap<String, Object>();
                HashMap<String, Object> set = new HashMap<String, Object>();
                tid.put("tardis_id", id);
                set.put("companions", newList);
                QueryFactory qf = new QueryFactory(plugin);
                qf.doUpdate("tardis", set, tid);
                // if using WorldGuard, remove them from the region membership
                if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                    World w = plugin.getServer().getWorld(data[0]);
                    if (w != null) {
                        plugin.getWorldGuardUtils().removeMemberFromRegion(w, player.getName(), args[1].toLowerCase(Locale.ENGLISH));
                    }
                }
                TARDISMessage.send(player, plugin.getPluginName() + message);
                return true;
            }
        } else {
            TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NO_PERMS.getText());
            return false;
        }
    }
}
