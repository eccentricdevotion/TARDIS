package me.eccentric_nz.tardisvortexmanipulator.command;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetManipulator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TVMCommandActivate {

    private final TARDIS plugin;

    public TVMCommandActivate(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean process(CommandSender sender, String[] args) {
        if (!TARDISPermission.hasPermission(sender, "tardis.admin")) {
            plugin.getMessenger().send(sender, TardisModule.VORTEX_MANIPULATOR, "VM_PERM_CMD");
            return true;
        }
        if (args.length < 2) {
            plugin.getMessenger().send(sender, TardisModule.VORTEX_MANIPULATOR, "VM_PLAYER");
            return true;
        }
        Player p = plugin.getServer().getPlayer(args[1]);
        if (p == null || !p.isOnline()) {
            plugin.getMessenger().send(sender, TardisModule.VORTEX_MANIPULATOR, "NOT_ONLINE");
            return true;
        }
        String uuid = p.getUniqueId().toString();
        // check for existing record
        TVMResultSetManipulator rs = new TVMResultSetManipulator(plugin, uuid);
        if (!rs.resultSet()) {
            HashMap<String, Object> set = new HashMap<>();
            set.put("uuid", uuid);
            plugin.getQueryFactory().doInsert("manipulator", set);
            plugin.getMessenger().send(sender, TardisModule.VORTEX_MANIPULATOR, "VM_ACTIVATED");
        } else {
            plugin.getMessenger().send(sender, TardisModule.VORTEX_MANIPULATOR, "VM_DONE");
        }
        return true;
    }
}
