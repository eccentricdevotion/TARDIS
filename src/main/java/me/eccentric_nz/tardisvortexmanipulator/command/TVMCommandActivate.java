package me.eccentric_nz.tardisvortexmanipulator.command;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.MODULE;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetManipulator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TVMCommandActivate implements CommandExecutor {

    private final TARDIS plugin;

    public TVMCommandActivate(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vma")) {
            if (!sender.hasPermission("tardis.admin")) {
                TARDISMessage.send(sender, MODULE.VORTEX_MANIPULATOR, "VM_PERM_CMD");
                return true;
            }
            if (args.length < 1) {
                TARDISMessage.send(sender, MODULE.VORTEX_MANIPULATOR, "VM_PLAYER");
                return true;
            }
            Player p = plugin.getServer().getPlayer(args[0]);
            if (p == null || !p.isOnline()) {
                TARDISMessage.send(sender, MODULE.VORTEX_MANIPULATOR, "NOT_ONLINE");
                return true;
            }
            String uuid = p.getUniqueId().toString();
            // check for existing record
            TVMResultSetManipulator rs = new TVMResultSetManipulator(plugin, uuid);
            if (!rs.resultSet()) {
                HashMap<String, Object> set = new HashMap<>();
                set.put("uuid", uuid);
                plugin.getQueryFactory().doInsert("manipulator", set);
                TARDISMessage.send(sender, MODULE.VORTEX_MANIPULATOR, "VM_ACTIVATED");
            } else {
                TARDISMessage.send(sender, MODULE.VORTEX_MANIPULATOR, "VM_DONE");
            }
            return true;
        }
        return false;
    }
}
