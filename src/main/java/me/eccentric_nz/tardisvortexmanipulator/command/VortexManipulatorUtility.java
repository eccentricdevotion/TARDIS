package me.eccentric_nz.tardisvortexmanipulator.command;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Locale;

public class VortexManipulatorUtility {

    public static boolean checkPlayer(TARDIS plugin, Player player) {
        if (!TARDISPermission.hasPermission(player, "vm.teleport")) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_PERM_CMD");
            return false;
        }
        ItemStack is = player.getInventory().getItemInMainHand();
        return is.hasItemMeta()
                && is.getItemMeta().hasDisplayName()
                && ComponentUtils.endsWith(is.getItemMeta().displayName(), "Vortex Manipulator");
    }

    public static void message(TARDIS plugin, Player player, OfflinePlayer ofp, String message) {
        if (ofp == null) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "COULD_NOT_FIND_NAME");
            return;
        }
        String ofp_uuid = ofp.getUniqueId().toString();
        // check they have a Vortex Manipulator
        TVMResultSetManipulator rsofp = new TVMResultSetManipulator(plugin, ofp_uuid);
        if (!rsofp.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_NONE", ofp.getName());
            return;
        }
        HashMap<String, Object> whereofp = new HashMap<>();
        whereofp.put("uuid_to", ofp_uuid);
        whereofp.put("uuid_from", player.getUniqueId().toString());
        whereofp.put("message", message);
        whereofp.put("date", System.currentTimeMillis());
        plugin.getQueryFactory().doInsert("messages", whereofp);
        plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_MSG_SENT");
    }

    public static void basicList(TARDIS plugin, Player player, String box) {
        String uuid = player.getUniqueId().toString();
        if (box.equalsIgnoreCase("out")) {
            // list outbox
            TVMResultSetOutbox rso = new TVMResultSetOutbox(plugin, uuid, 0, 10);
            if (rso.resultSet()) {
                TVMUtils.sendOutboxList(player, rso, 1);
            } else {
                plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_MSG_OUT");
            }
        } else {
            // list inbox
            TVMResultSetInbox rsi = new TVMResultSetInbox(plugin, uuid, 0, 10);
            if (rsi.resultSet()) {
                TVMUtils.sendInboxList(player, rsi, 1);
            } else {
                plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_MSG_IN");
            }
        }
    }

    public static void pagedList(TARDIS plugin, Player player, String box, int page) {
        String uuid = player.getUniqueId().toString();
        if (page == 0) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_MSG_INVALID");
            return;
        }
        int start = (page * 10) - 10;
        int limit = page * 10;
        if (box.equalsIgnoreCase("out")) {
            // outbox
            TVMResultSetOutbox rso = new TVMResultSetOutbox(plugin, uuid, start, limit);
            if (rso.resultSet()) {
                TVMUtils.sendOutboxList(player, rso, page);
            } else {
                plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_MSG_OUT");
            }
        } else {
            // inbox
            TVMResultSetInbox rsi = new TVMResultSetInbox(plugin, uuid, start, limit);
            if (rsi.resultSet()) {
                TVMUtils.sendInboxList(player, rsi, page);
            } else {
                plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_MSG_IN");
            }
        }
    }

    public static void read(TARDIS plugin, Player player, int read_id) {
        if (read_id != 0) {
            TVMResultSetMessageById rsm = new TVMResultSetMessageById(plugin, read_id);
            if (rsm.resultSet()) {
                TVMUtils.readMessage(player, rsm.getMessage());
                // update read status
                new TVMQueryFactory(plugin).setReadStatus(read_id);
            } else {
                plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_MSG_ID");
            }
        }
    }

    public static void delete(TARDIS plugin, Player player, int delete_id) {
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
        }
    }

    public static void clear(TARDIS plugin, Player player, String box) {
        if (!box.toLowerCase(Locale.ROOT).equals("in") && !box.toLowerCase(Locale.ROOT).equals("out")) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_MSG_CLEAR");
            return;
        }
        HashMap<String, Object> where = new HashMap<>();
        String which = "Outbox";
        if (box.equalsIgnoreCase("out")) {
            where.put("uuid_from", player.getUniqueId().toString());
        } else {
            where.put("uuid_to", player.getUniqueId().toString());
            which = "Inbox";
        }
        plugin.getQueryFactory().doDelete("messages", where);
        plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_MSG_CLEARED", which);
    }
}
