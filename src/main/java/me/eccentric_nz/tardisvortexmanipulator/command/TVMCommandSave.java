package me.eccentric_nz.tardisvortexmanipulator.command;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.MODULE;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetSaves;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetWarpByName;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TVMCommandSave {

    private final TARDIS plugin;

    public TVMCommandSave(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean process(Player player, String[] args) {
        if (!TARDISPermission.hasPermission(player, "vm.teleport")) {
            TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_PERM_CMD");
            return true;
        }
        String uuid = player.getUniqueId().toString();
        if (args.length == 1) {
            // list saves
            TVMResultSetSaves rss = new TVMResultSetSaves(plugin, uuid, 0, 10);
            if (rss.resultSet()) {
                TVMUtils.sendSaveList(player, rss, 1);
            }
            return true;
        }
        if (args.length < 2) {
            TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_PAGE_NUM");
            return true;
        }
        try {
            int page = Integer.parseInt(args[1]);
            if (page <= 0) {
                TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_MSG_INVALID");
                return true;
            }
            int start = (page * 10) - 10;
            int limit = page * 10;
            TVMResultSetSaves rss = new TVMResultSetSaves(plugin, uuid, start, limit);
            if (rss.resultSet()) {
                TVMUtils.sendSaveList(player, rss, page);
            }
            return true;
        } catch (NumberFormatException e) {
            plugin.debug("Wasn't a page number...");
            // check for existing save
            TVMResultSetWarpByName rs = new TVMResultSetWarpByName(plugin, uuid, args[1]);
            if (rs.resultSet()) {
                TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_SAVE_EXISTS");
                return true;
            }
            Location l = player.getLocation();
            HashMap<String, Object> set = new HashMap<>();
            set.put("uuid", uuid);
            set.put("save_name", args[1]);
            set.put("world", l.getWorld().getName());
            set.put("x", l.getX());
            set.put("y", l.getY());
            set.put("z", l.getZ());
            set.put("yaw", l.getYaw());
            set.put("pitch", l.getPitch());
            plugin.getQueryFactory().doInsert("saves", set);
            TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_SAVE_ADDED", args[1]);
            return true;
        }
    }
}
