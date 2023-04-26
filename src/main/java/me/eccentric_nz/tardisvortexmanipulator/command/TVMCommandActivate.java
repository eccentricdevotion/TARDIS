package me.eccentric_nz.tardisvortexmanipulator.command;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetManipulator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TVMCommandActivate {

    private final TARDIS plugin;

    public TVMCommandActivate(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean process(CommandSender sender, String[] args) {
        if (!TARDISPermission.hasPermission(sender, "tardis.admin")) {
            TARDISMessage.send(sender, TardisModule.VORTEX_MANIPULATOR, "VM_PERM_CMD");
            return true;
        }
        if (args.length < 2) {
            TARDISMessage.send(sender, TardisModule.VORTEX_MANIPULATOR, "VM_PLAYER");
            return true;
        }
        Player p = plugin.getServer().getPlayer(args[1]);
        if (p == null || !p.isOnline()) {
            TARDISMessage.send(sender, TardisModule.VORTEX_MANIPULATOR, "NOT_ONLINE");
            return true;
        }
        String uuid = p.getUniqueId().toString();
        // check for existing record
        TVMResultSetManipulator rs = new TVMResultSetManipulator(plugin, uuid);
        if (!rs.resultSet()) {
            HashMap<String, Object> set = new HashMap<>();
            set.put("uuid", uuid);
            plugin.getQueryFactory().doInsert("manipulator", set);
            TARDISMessage.send(sender, TardisModule.VORTEX_MANIPULATOR, "VM_ACTIVATED");
        } else {
            TARDISMessage.send(sender, TardisModule.VORTEX_MANIPULATOR, "VM_DONE");
        }
        return true;
    }
}
