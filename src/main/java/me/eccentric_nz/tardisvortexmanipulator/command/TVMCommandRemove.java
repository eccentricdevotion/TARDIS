package me.eccentric_nz.tardisvortexmanipulator.command;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetWarpByName;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TVMCommandRemove {

    private final TARDIS plugin;

    public TVMCommandRemove(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean process(Player player, String[] args) {
        if (!TARDISPermission.hasPermission(player, "vm.teleport")) {
            TARDISMessage.send(player, TardisModule.VORTEX_MANIPULATOR, "VM_PERM_CMD");
            return true;
        }
        if (args.length < 2) {
            TARDISMessage.send(player, TardisModule.VORTEX_MANIPULATOR, "VM_SAVE_NAME");
            return true;
        }
        String uuid = player.getUniqueId().toString();
        // check for existing save
        TVMResultSetWarpByName rs = new TVMResultSetWarpByName(plugin, uuid, args[1]);
        if (rs.resultSet()) {
            TARDISMessage.send(player, TardisModule.VORTEX_MANIPULATOR, "VM_SAVE_NONE");
            return true;
        }
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        where.put("save_name", args[1]);
        plugin.getQueryFactory().doDelete("saves", where);
        TARDISMessage.send(player, TardisModule.VORTEX_MANIPULATOR, "VM_SAVE_REMOVED", args[1]);
        return true;
    }
}
