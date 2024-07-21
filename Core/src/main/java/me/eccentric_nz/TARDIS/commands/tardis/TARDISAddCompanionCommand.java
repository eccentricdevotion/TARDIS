/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.companionGUI.TARDISCompanionAddInventory;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.Advancement;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author eccentric_nz
 */
class TARDISAddCompanionCommand {

    private static final Pattern LETTERS_NUMBERS = Pattern.compile("[A-Za-z0-9_*.]{2,16}");
    private final TARDIS plugin;

    TARDISAddCompanionCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean doAddGUI(Player player) {
        if (TARDISPermission.hasPermission(player, "tardis.add")) {
            ItemStack[] items = new TARDISCompanionAddInventory(plugin, player).getPlayers();
            Inventory presetinv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Add Companion");
            presetinv.setContents(items);
            player.openInventory(presetinv);
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
        }
        return true;
    }

    boolean doAdd(Player player, String[] args) {
        if (TARDISPermission.hasPermission(player, "tardis.add")) {
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            String comps;
            String data;
            int id;
            String owner;
            if (!rs.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
                return true;
            } else {
                Tardis tardis = rs.getTardis();
                id = tardis.getTardisId();
                comps = tardis.getCompanions();
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
                boolean addAll = (args[1].equalsIgnoreCase("everyone") || args[1].equalsIgnoreCase("all"));
                HashMap<String, Object> tid = new HashMap<>();
                HashMap<String, Object> set = new HashMap<>();
                OfflinePlayer companion = null;
                if (addAll) {
                    tid.put("tardis_id", id);
                    set.put("companions", "everyone");
                } else {
                    // get player from name
                    companion = Bukkit.getOfflinePlayer(args[1]);
                    if (companion.getName() != null) {
                        UUID oluuid = companion.getUniqueId();
                        tid.put("tardis_id", id);
                        if (comps != null && !comps.isEmpty() && !comps.equalsIgnoreCase("everyone")) {
                            // add to the list
                            String newList = comps + ":" + oluuid;
                            set.put("companions", newList);
                        } else {
                            // make a list
                            set.put("companions", oluuid.toString());
                        }
                        // are we doing an achievement?
                        if (plugin.getAchievementConfig().getBoolean("friends.enabled")) {
                            TARDISAchievementFactory taf = new TARDISAchievementFactory(plugin, player, Advancement.FRIENDS, 1);
                            taf.doAchievement(1);
                        }
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "COULD_NOT_FIND_NAME");
                        return true;
                    }
                }
                // if using WorldGuard, add them to the region membership
                if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                    World w = TARDISStaticLocationGetters.getWorldFromSplitString(data);
                    if (w != null) {
                        if (addAll) {
                            // remove all members
                            plugin.getWorldGuardUtils().removeAllMembersFromRegion(w, player.getName(), player.getUniqueId());
                            // set entry and exit flags to allow
                            plugin.getWorldGuardUtils().setEntryExitFlags(w.getName(), player.getName(), true);
                        } else {
                            plugin.getWorldGuardUtils().addMemberToRegion(w, owner, companion.getUniqueId());
                            // set entry and exit flags to deny
                            plugin.getWorldGuardUtils().setEntryExitFlags(w.getName(), player.getName(), false);
                        }
                    }
                }
                plugin.getQueryFactory().doUpdate("tardis", set, tid);
                if (addAll) {
                    plugin.getMessenger().sendInsertedColour(player, "COMPANIONS_ADD", "everyone", plugin);
                    plugin.getMessenger().sendColouredCommand(player, "COMPANIONS_EVERYONE", "/tardis remove all", plugin);
                } else {
                    plugin.getMessenger().sendInsertedColour(player, "COMPANIONS_ADD", args[1], plugin);
                }
            }
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
        }
        return true;
    }
}
