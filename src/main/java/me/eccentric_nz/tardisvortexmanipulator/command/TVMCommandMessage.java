package me.eccentric_nz.tardisvortexmanipulator.command;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.MODULE;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TVMCommandMessage implements CommandExecutor {

    private final TARDIS plugin;

    public TVMCommandMessage(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vmm")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                TARDISMessage.send(sender, MODULE.VORTEX_MANIPULATOR, "CMD_PLAYER");
                return true;
            }
            if (!player.hasPermission("vm.message")) {
                TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_PERM_CMD");
                return true;
            }
            ItemStack is = player.getInventory().getItemInMainHand();
            if (is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals("Vortex Manipulator")) {
                if (args.length < 2) {
                    TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_USAGE");
                    return false;
                }
                String first = args[0].toLowerCase();
                try {
                    FIRST f = FIRST.valueOf(first);
                    switch (f) {
                        case msg -> {
                            OfflinePlayer ofp = plugin.getServer().getOfflinePlayer(args[1]);
                            if (ofp == null) {
                                TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "COULD_NOT_FIND_NAME");
                                return true;
                            }
                            String ofp_uuid = ofp.getUniqueId().toString();
                            // check they have a Vortex Manipulator
                            TVMResultSetManipulator rsofp = new TVMResultSetManipulator(plugin, ofp_uuid);
                            if (!rsofp.resultSet()) {
                                TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_NONE", args[1]);
                                return true;
                            }
                            StringBuilder sb = new StringBuilder();
                            for (int i = 2; i < args.length; i++) {
                                sb.append(args[i]).append(" ");
                            }
                            String message = sb.toString();
                            HashMap<String, Object> whereofp = new HashMap<>();
                            whereofp.put("uuid_to", ofp_uuid);
                            whereofp.put("uuid_from", player.getUniqueId().toString());
                            whereofp.put("message", message);
                            whereofp.put("date", System.currentTimeMillis());
                            plugin.getQueryFactory().doInsert("messages", whereofp);
                            TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_MSG_SENT");
                        }
                        case list -> {
                            String uuid = player.getUniqueId().toString();
                            if (args.length == 2) {
                                if (args[1].equalsIgnoreCase("out")) {
                                    // list outbox
                                    TVMResultSetOutbox rso = new TVMResultSetOutbox(plugin, uuid, 0, 10);
                                    if (rso.resultSet()) {
                                        TVMUtils.sendOutboxList(player, rso, 1);
                                    } else {
                                        TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_MSG_OUT");
                                        return true;
                                    }
                                } else {
                                    // list inbox
                                    TVMResultSetInbox rsi = new TVMResultSetInbox(plugin, uuid, 0, 10);
                                    if (rsi.resultSet()) {
                                        TVMUtils.sendInboxList(player, rsi, 1);
                                    } else {
                                        TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_MSG_IN");
                                        return true;
                                    }
                                }
                            }
                            if (args.length < 3) {
                                TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_MSG_PAGE");
                                return true;
                            }
                            int page = parseNum(args[2]);
                            if (page == -1) {
                                TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_MSG_INVALID");
                                return true;
                            }
                            int start = (page * 10) - 10;
                            int limit = page * 10;
                            if (args[1].equalsIgnoreCase("out")) {
                                // outbox
                                TVMResultSetOutbox rso = new TVMResultSetOutbox(plugin, uuid, start, limit);
                                if (rso.resultSet()) {
                                    TVMUtils.sendOutboxList(player, rso, page);
                                } else {
                                    TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_MSG_OUT");
                                    return true;
                                }
                            } else {
                                // inbox
                                TVMResultSetInbox rsi = new TVMResultSetInbox(plugin, uuid, start, limit);
                                if (rsi.resultSet()) {
                                    TVMUtils.sendInboxList(player, rsi, page);
                                } else {
                                    TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_MSG_IN");
                                    return true;
                                }
                            }
                        }
                        case read -> {
                            int read_id = parseNum(args[1]);
                            if (read_id != -1) {
                                TVMResultSetMessageById rsm = new TVMResultSetMessageById(plugin, read_id);
                                if (rsm.resultSet()) {
                                    TVMUtils.readMessage(player, rsm.getMessage());
                                    // update read status
                                    new TVMQueryFactory(plugin).setReadStatus(read_id);
                                } else {
                                    TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_MSG_ID");
                                    return true;
                                }
                            }
                        }
                        case delete -> {
                            int delete_id = parseNum(args[1]);
                            if (delete_id != -1) {
                                TVMResultSetMessageById rsm = new TVMResultSetMessageById(plugin, delete_id);
                                if (rsm.resultSet()) {
                                    HashMap<String, Object> where = new HashMap<>();
                                    where.put("message_id", delete_id);
                                    plugin.getQueryFactory().doDelete("messages", where);
                                    TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_MSG_DELETED");
                                }
                            } else {
                                TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_MSG_ID");
                                return true;
                            }
                        }
                        default -> {
                            // clear
                            if (!args[1].toLowerCase().equals("in") && !args[1].toLowerCase().equals("out")) {
                                TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_MSG_CLEAR");
                                return true;
                            }
                            HashMap<String, Object> where = new HashMap<>();
                            String which = "Outbox";
                            if (args[1].equalsIgnoreCase("out")) {
                                where.put("uuid_from", player.getUniqueId().toString());
                            } else {
                                where.put("uuid_to", player.getUniqueId().toString());
                                which = "Inbox";
                            }
                            plugin.getQueryFactory().doDelete("messages", where);
                            TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_MSG_CLEARED", which);
                        }
                    }
                } catch (IllegalArgumentException e) {
                    TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_USAGE");
                    return false;
                }
                return true;
            } else {
                TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_HAND");
                return true;
            }
        }
        return false;
    }

    private int parseNum(String s) {
        try {
            int i = Integer.parseInt(s);
            return i;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private enum FIRST {

        msg,
        list,
        read,
        delete,
        clear
    }
}
