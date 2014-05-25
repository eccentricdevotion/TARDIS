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
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISAddCompanionCommand {

    private final TARDIS plugin;

    public TARDISAddCompanionCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean doAdd(Player player, String[] args) {
        if (player.hasPermission("tardis.add")) {
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            String comps;
            int id;
            String[] data;
            if (!rs.resultSet()) {
                TARDISMessage.send(player, "NO_TARDIS");
                return true;
            } else {
                id = rs.getTardis_id();
                comps = rs.getCompanions();
                data = rs.getChunk().split(":");
            }
            if (args.length < 2) {
                TARDISMessage.send(player, "TOO_FEW_ARGS");
                return false;
            }
            if (!args[1].matches("[A-Za-z0-9_]{2,16}")) {
                TARDISMessage.send(player, "PLAYER_NOT_VALID");
                return true;
            } else {
                UUID oluuid = plugin.getServer().getOfflinePlayer(args[1]).getUniqueId();
                if (oluuid == null) {
                    oluuid = plugin.getGeneralKeeper().getUUIDCache().getIdOptimistic(args[1]);
                    plugin.getGeneralKeeper().getUUIDCache().getId(args[1]);
                }
                if (oluuid != null) {
                    QueryFactory qf = new QueryFactory(plugin);
                    HashMap<String, Object> tid = new HashMap<String, Object>();
                    HashMap<String, Object> set = new HashMap<String, Object>();
                    tid.put("tardis_id", id);
                    if (comps != null && !comps.isEmpty()) {
                        // add to the list
                        String newList = comps + ":" + oluuid.toString();
                        set.put("companions", newList);
                    } else {
                        // make a list
                        set.put("companions", oluuid.toString());
                    }
                    qf.doUpdate("tardis", set, tid);
                    TARDISMessage.send(player, "COMPANIONS_ADD", ChatColor.GREEN + args[1] + ChatColor.RESET);
                    // are we doing an achievement?
                    if (plugin.getAchievementConfig().getBoolean("friends.enabled")) {
                        TARDISAchievementFactory taf = new TARDISAchievementFactory(plugin, player, "friends", 1);
                        taf.doAchievement(1);
                    }
                    // if using WorldGuard, add them to the region membership
                    if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                        World w = plugin.getServer().getWorld(data[0]);
                        if (w != null) {
                            plugin.getWorldGuardUtils().addMemberToRegion(w, player.getName(), args[1].toLowerCase(Locale.ENGLISH));
                        }
                    }
                    return true;
                } else {
                    TARDISMessage.send(player, "COULD_NOT_FIND_NAME");
                    return true;
                }
            }
        } else {
            TARDISMessage.send(player, "NO_PERMS");
            return true;
        }
    }
}
