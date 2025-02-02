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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author eccentric_nz
 */
class TARDISRemoveCompanionCommand {

    private static final Pattern LETTERS_NUMBERS = Pattern.compile("[A-Za-z0-9_*.]{2,16}");
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
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
                return false;
            } else {
                Tardis tardis = rs.getTardis();
                comps = tardis.getCompanions();
                if (comps == null || comps.isEmpty()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "COMPANIONS_NONE");
                    return true;
                }
                id = tardis.getTardisId();
                data = tardis.getChunk();
                owner = tardis.getOwner();
            }
            if (args.length < 2) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                return false;
            }
            if (!LETTERS_NUMBERS.matcher(args[1]).matches()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "PLAYER_NOT_VALID");
            } else {
                String newList = "";
                String message = "COMPANIONS_REMOVE_ALL";
                if (!args[1].equals("all")) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                    if (offlinePlayer.getName() != null) {
                        UUID oluuid = offlinePlayer.getUniqueId();
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
                            if (!sb.isEmpty()) {
                                newList = sb.substring(0, sb.length() - 1);
                            }
                        }
                        message = "COMPANIONS_REMOVE_ONE";
                        if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                            World w = TARDISStaticLocationGetters.getWorldFromSplitString(data);
                            if (w != null) {
                                plugin.getWorldGuardUtils().removeMemberFromRegion(w, owner, oluuid);
                            }
                        }
                        plugin.getMessenger().send(player, TardisModule.TARDIS, message, args[1]);
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "COULD_NOT_FIND_NAME");
                        return true;
                    }
                } else {
                    // if using WorldGuard, remove them from the region membership
                    if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                        World w = TARDISStaticLocationGetters.getWorldFromSplitString(data);
                        if (w != null) {
                            if (args[1].equals("all")) {
                                plugin.getWorldGuardUtils().removeAllMembersFromRegion(w, owner, player.getUniqueId());
                                // set entry and exit flags to deny
                                plugin.getWorldGuardUtils().setEntryExitFlags(w.getName(), player.getName(), false);
                            }
                        }
                    }
                    plugin.getMessenger().send(player, TardisModule.TARDIS, message);
                }
                HashMap<String, Object> tid = new HashMap<>();
                HashMap<String, Object> set = new HashMap<>();
                tid.put("tardis_id", id);
                set.put("companions", newList);
                plugin.getQueryFactory().doUpdate("tardis", set, tid);
            }
            return true;
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return false;
        }
    }
}
