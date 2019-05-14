/*
 * Copyright (C) 2018 eccentric_nz
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
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.companionGUI.TARDISCompanionAddInventory;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.ADVANCEMENT;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISAddCompanionCommand {

    private final TARDIS plugin;

    TARDISAddCompanionCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean doAddGUI(Player player) {
        if (player.hasPermission("tardis.add")) {
            ItemStack[] items = new TARDISCompanionAddInventory(plugin, player.getUniqueId()).getPlayers();
            Inventory presetinv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Add Companion");
            presetinv.setContents(items);
            player.openInventory(presetinv);
        } else {
            TARDISMessage.send(player, "NO_PERMS");
        }
        return true;
    }

    boolean doAdd(Player player, String[] args) {
        if (player.hasPermission("tardis.add")) {
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            String comps;
            int id;
            String[] data;
            String owner;
            if (!rs.resultSet()) {
                TARDISMessage.send(player, "NO_TARDIS");
                return true;
            } else {
                Tardis tardis = rs.getTardis();
                id = tardis.getTardis_id();
                comps = tardis.getCompanions();
                data = tardis.getChunk().split(":");
                owner = tardis.getOwner();
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
                if (oluuid != null) {
                    QueryFactory qf = new QueryFactory(plugin);
                    HashMap<String, Object> tid = new HashMap<>();
                    HashMap<String, Object> set = new HashMap<>();
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
                        TARDISAchievementFactory taf = new TARDISAchievementFactory(plugin, player, ADVANCEMENT.FRIENDS, 1);
                        taf.doAchievement(1);
                    }
                    // if using WorldGuard, add them to the region membership
                    if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                        String world = (data[0].equals("TARDIS_TimeVortex") ? "tardis_time_vortex" : data[0].toLowerCase(Locale.ENGLISH));
                        World w = plugin.getServer().getWorld(world);
                        if (w != null) {
                            plugin.getWorldGuardUtils().addMemberToRegion(w, owner, args[1].toLowerCase(Locale.ENGLISH));
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
