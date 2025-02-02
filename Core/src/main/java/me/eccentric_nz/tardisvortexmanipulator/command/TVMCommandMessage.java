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
package me.eccentric_nz.tardisvortexmanipulator.command;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

public class TVMCommandMessage {

    private final TARDIS plugin;

    public TVMCommandMessage(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean process(Player player, String[] args) {
        if (!TARDISPermission.hasPermission(player, "vm.message")) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_PERM_CMD");
            return true;
        }
        if (args.length < 3) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_USAGE");
            return false;
        }
        String first = args[1].toUpperCase(Locale.ROOT);
        try {
            MessageAction f = MessageAction.valueOf(first);
            switch (f) {
                case MSG -> {
                    OfflinePlayer ofp = plugin.getServer().getOfflinePlayer(args[2]);
                    if (ofp == null) {
                        plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "COULD_NOT_FIND_NAME");
                        return true;
                    }
                    String ofp_uuid = ofp.getUniqueId().toString();
                    // check they have a Vortex Manipulator
                    TVMResultSetManipulator rsofp = new TVMResultSetManipulator(plugin, ofp_uuid);
                    if (!rsofp.resultSet()) {
                        plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_NONE", args[2]);
                        return true;
                    }
                    StringBuilder sb = new StringBuilder();
                    for (int i = 3; i < args.length; i++) {
                        sb.append(args[i]).append(" ");
                    }
                    String message = sb.toString();
                    HashMap<String, Object> whereofp = new HashMap<>();
                    whereofp.put("uuid_to", ofp_uuid);
                    whereofp.put("uuid_from", player.getUniqueId().toString());
                    whereofp.put("message", message);
                    whereofp.put("date", System.currentTimeMillis());
                    plugin.getQueryFactory().doInsert("messages", whereofp);
                    plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_MSG_SENT");
                }
                case LIST -> {
                    String uuid = player.getUniqueId().toString();
                    if (args.length == 3) {
                        if (args[2].equalsIgnoreCase("out")) {
                            // list outbox
                            TVMResultSetOutbox rso = new TVMResultSetOutbox(plugin, uuid, 0, 10);
                            if (rso.resultSet()) {
                                TVMUtils.sendOutboxList(player, rso, 1);
                            } else {
                                plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_MSG_OUT");
                                return true;
                            }
                        } else {
                            // list inbox
                            TVMResultSetInbox rsi = new TVMResultSetInbox(plugin, uuid, 0, 10);
                            if (rsi.resultSet()) {
                                TVMUtils.sendInboxList(player, rsi, 1);
                            } else {
                                plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_MSG_IN");
                                return true;
                            }
                        }
                    }
                    if (args.length < 4) {
                        plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_MSG_PAGE");
                        return true;
                    }
                    int page = TARDISNumberParsers.parseInt(args[3]);
                    if (page == 0) {
                        plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_MSG_INVALID");
                        return true;
                    }
                    int start = (page * 10) - 10;
                    int limit = page * 10;
                    if (args[2].equalsIgnoreCase("out")) {
                        // outbox
                        TVMResultSetOutbox rso = new TVMResultSetOutbox(plugin, uuid, start, limit);
                        if (rso.resultSet()) {
                            TVMUtils.sendOutboxList(player, rso, page);
                        } else {
                            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_MSG_OUT");
                            return true;
                        }
                    } else {
                        // inbox
                        TVMResultSetInbox rsi = new TVMResultSetInbox(plugin, uuid, start, limit);
                        if (rsi.resultSet()) {
                            TVMUtils.sendInboxList(player, rsi, page);
                        } else {
                            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_MSG_IN");
                            return true;
                        }
                    }
                }
                case READ -> {
                    int read_id = TARDISNumberParsers.parseInt(args[2]);
                    if (read_id != 0) {
                        TVMResultSetMessageById rsm = new TVMResultSetMessageById(plugin, read_id);
                        if (rsm.resultSet()) {
                            TVMUtils.readMessage(player, rsm.getMessage());
                            // update read status
                            new TVMQueryFactory(plugin).setReadStatus(read_id);
                        } else {
                            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_MSG_ID");
                            return true;
                        }
                    }
                }
                case DELETE -> {
                    int delete_id = TARDISNumberParsers.parseInt(args[2]);
                    if (delete_id != 0) {
                        TVMResultSetMessageById rsm = new TVMResultSetMessageById(plugin, delete_id);
                        if (rsm.resultSet()) {
                            HashMap<String, Object> where = new HashMap<>();
                            where.put("message_id", delete_id);
                            plugin.getQueryFactory().doDelete("messages", where);
                            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_MSG_DELETED");
                        }
                    } else {
                        plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_MSG_ID");
                        return true;
                    }
                }
                default -> {
                    // clear
                    if (!args[2].toLowerCase(Locale.ROOT).equals("in") && !args[2].toLowerCase(Locale.ROOT).equals("out")) {
                        plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_MSG_CLEAR");
                        return true;
                    }
                    HashMap<String, Object> where = new HashMap<>();
                    String which = "Outbox";
                    if (args[2].equalsIgnoreCase("out")) {
                        where.put("uuid_from", player.getUniqueId().toString());
                    } else {
                        where.put("uuid_to", player.getUniqueId().toString());
                        which = "Inbox";
                    }
                    plugin.getQueryFactory().doDelete("messages", where);
                    plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_MSG_CLEARED", which);
                }



            }
        } catch (IllegalArgumentException e) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_USAGE");
            return false;
        }
        return true;
    }

    private enum MessageAction {
        MSG,
        LIST,
        READ,
        DELETE,
        CLEAR
    }
}
