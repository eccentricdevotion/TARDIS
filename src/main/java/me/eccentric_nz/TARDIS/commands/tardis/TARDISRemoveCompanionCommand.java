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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISRemoveCompanionCommand {

    private final TARDIS plugin;

    TARDISRemoveCompanionCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean doRemoveCompanion(Player player, String[] args) {
        if (TARDISPermission.hasPermission(player, "tardis.add")) {
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            String comps;
            int id;
            String data;
            String owner;
            if (!rs.resultSet()) {
                TARDISMessage.send(player, "NO_TARDIS");
                return false;
            } else {
                Tardis tardis = rs.getTardis();
                comps = tardis.getCompanions();
                if (comps == null || comps.isEmpty()) {
                    TARDISMessage.send(player, "COMPANIONS_NONE");
                    return true;
                }
                id = tardis.getTardis_id();
                data = tardis.getChunk();
                owner = tardis.getOwner();
            }
            if (args.length < 2) {
                TARDISMessage.send(player, "TOO_FEW_ARGS");
                return false;
            }
            if (!args[1].matches("[A-Za-z0-9_*.]{2,16}")) {
                TARDISMessage.send(player, "PLAYER_NOT_VALID");
            } else {
                String newList = "";
                String message = "COMPANIONS_REMOVE_ALL";
                if (!args[1].equals("all")) {
                    UUID oluuid = plugin.getServer().getOfflinePlayer(args[1]).getUniqueId();
                    if (oluuid != null) {
                        String[] split = comps.split(":");
                        StringBuilder sb = new StringBuilder();
                        if (split.length > 1) {
                            // recompile string without the specified player
                            for (String c : split) {
                                if (!c.equals(oluuid.toString())) {
                                    // add to new string
                                    sb.append(c).append(":");
                                }
                            }
                            // remove trailing colon
                            if (sb.length() > 0) {
                                newList = sb.substring(0, sb.length() - 1);
                            }
                        }
                        message = "COMPANIONS_REMOVE_ONE";
                    } else {
                        TARDISMessage.send(player, "COULD_NOT_FIND_NAME");
                        return true;
                    }
                }
                // if using WorldGuard, remove them from the region membership
                if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                    World w = TARDISStaticLocationGetters.getWorld(data);
                    if (w != null) {
                        if (args[1].equals("all")) {
                            plugin.getWorldGuardUtils().removeAllMembersFromRegion(w, owner);
                            // set entry and exit flags to deny
                            plugin.getWorldGuardUtils().setEntryExitFlags(w.getName(), player.getName(), false);
                        } else {
                            plugin.getWorldGuardUtils().removeMemberFromRegion(w, owner, args[1].toLowerCase(Locale.ENGLISH));
                        }
                    }
                }
                HashMap<String, Object> tid = new HashMap<>();
                HashMap<String, Object> set = new HashMap<>();
                tid.put("tardis_id", id);
                set.put("companions", newList);
                plugin.getQueryFactory().doUpdate("tardis", set, tid);
                if (!args[1].equals("all")) {
                    TARDISMessage.send(player, message, args[1]);
                } else {
                    TARDISMessage.send(player, message);
                }
            }
            return true;
        } else {
            TARDISMessage.send(player, "NO_PERMS");
            return false;
        }
    }
}
