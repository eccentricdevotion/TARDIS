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
package me.eccentric_nz.tardis.commands.tardis;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.advancement.TardisAdvancementFactory;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.companiongui.TardisCompanionAddInventory;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.enumeration.Advancement;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.TardisStaticLocationGetters;
import me.eccentric_nz.tardis.utility.TardisStaticUtils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
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
class TardisAddCompanionCommand {

    private final TardisPlugin plugin;

    TardisAddCompanionCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    boolean doAddGui(Player player) {
        if (TardisPermission.hasPermission(player, "tardis.add")) {
            ItemStack[] items = new TardisCompanionAddInventory(plugin, player).getPlayers();
            Inventory presetinv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Add Companion");
            presetinv.setContents(items);
            player.openInventory(presetinv);
        } else {
            TardisMessage.send(player, "NO_PERMS");
        }
        return true;
    }

    boolean doAdd(Player player, String[] args) {
        if (TardisPermission.hasPermission(player, "tardis.add")) {
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            String comps;
            String data;
            int id;
            String owner;
            if (!rs.resultSet()) {
                TardisMessage.send(player, "NO_Tardis");
                return true;
            } else {
                Tardis tardis = rs.getTardis();
                id = tardis.getTardisId();
                comps = tardis.getCompanions();
                data = tardis.getChunk();
                owner = tardis.getOwner();
            }
            if (args.length < 2) {
                TardisMessage.send(player, "TOO_FEW_ARGS");
                return false;
            }
            if (!args[1].matches("[A-Za-z0-9_*.]{2,16}")) {
                TardisMessage.send(player, "PLAYER_NOT_VALID");
            } else {
                boolean addAll = (args[1].equalsIgnoreCase("everyone") || args[1].equalsIgnoreCase("all"));
                HashMap<String, Object> tid = new HashMap<>();
                HashMap<String, Object> set = new HashMap<>();
                if (addAll) {
                    tid.put("tardis_id", id);
                    set.put("companions", "everyone");
                } else {
                    // get player from name
                    OfflinePlayer offlinePlayer = TardisStaticUtils.getOfflinePlayer(args[1]);
                    if (offlinePlayer != null) {
                        UUID oluuid = offlinePlayer.getUniqueId();
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
                        if (plugin.getAdvancementConfig().getBoolean("friends.enabled")) {
                            TardisAdvancementFactory taf = new TardisAdvancementFactory(plugin, player, Advancement.FRIENDS, 1);
                            taf.doAdvancement(1);
                        }
                    } else {
                        TardisMessage.send(player, "COULD_NOT_FIND_NAME");
                        return true;
                    }
                }
                // if using WorldGuard, add them to the region membership
                if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                    World w = TardisStaticLocationGetters.getWorld(data);
                    if (w != null) {
                        if (addAll) {
                            // remove all members
                            plugin.getWorldGuardUtils().removeAllMembersFromRegion(w, player.getName(), player.getUniqueId());
                            // set entry and exit flags to allow
                            plugin.getWorldGuardUtils().setEntryExitFlags(w.getName(), player.getName(), true);
                        } else {
                            plugin.getWorldGuardUtils().addMemberToRegion(w, owner, args[1].toLowerCase(Locale.ENGLISH));
                            // set entry and exit flags to allow
                            plugin.getWorldGuardUtils().setEntryExitFlags(w.getName(), player.getName(), false);
                        }
                    }
                }
                plugin.getQueryFactory().doUpdate("tardis", set, tid);
                if (addAll) {
                    TardisMessage.send(player, "COMPANIONS_ADD", ChatColor.GREEN + "everyone" + ChatColor.RESET);
                    TardisMessage.send(player, "COMPANIONS_EVERYONE");
                } else {
                    TardisMessage.send(player, "COMPANIONS_ADD", ChatColor.GREEN + args[1] + ChatColor.RESET);
                }
            }
        } else {
            TardisMessage.send(player, "NO_PERMS");
        }
        return true;
    }
}
