package me.eccentric_nz.tardisvortexmanipulator.command;

import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.MODULE;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetManipulator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TVMCommandGive implements CommandExecutor {

    private final TARDIS plugin;
    private final int full;

    public TVMCommandGive(TARDIS plugin) {
        this.plugin = plugin;
        full = this.plugin.getConfig().getInt("tachyon_use.max");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vmg")) {
            if (!sender.hasPermission("tardis.admin")) {
                TARDISMessage.send(sender, MODULE.VORTEX_MANIPULATOR, "VM_PERM_CMD");
                return true;
            }
            if (args.length < 2) {
                TARDISMessage.send(sender, MODULE.VORTEX_MANIPULATOR, "VM_UUID");
                return true;
            }
            UUID uuid = UUID.fromString(args[0]);
            Player p = plugin.getServer().getPlayer(uuid);
            if (p == null || !p.isOnline()) {
                TARDISMessage.send(sender, MODULE.VORTEX_MANIPULATOR, "NOT_ONLINE");
                return true;
            }
            // check for existing record
            TVMResultSetManipulator rs = new TVMResultSetManipulator(plugin, args[0]);
            if (rs.resultSet()) {
                int tachyon_level = rs.getTachyonLevel();
                int amount;
                if (args[1].equalsIgnoreCase("full")) {
                    amount = full;
                } else if (args[1].equalsIgnoreCase("empty")) {
                    amount = 0;
                } else {
                    try {
                        amount = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        TARDISMessage.send(sender, MODULE.VORTEX_MANIPULATOR, "VM_LAST_ARG");
                        return true;
                    }
                    if (tachyon_level + amount > full) {
                        amount = full;
                    } else {
                        amount += tachyon_level;
                    }
                }
                HashMap<String, Object> set = new HashMap<>();
                set.put("tachyon_level", amount);
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", args[0]);
                plugin.getQueryFactory().doUpdate("manipulator", set, where);
                TARDISMessage.send(sender, MODULE.VORTEX_MANIPULATOR, "VM_TACHYON_SET", "" + amount);
            } else {
                TARDISMessage.send(sender, MODULE.VORTEX_MANIPULATOR, "VM_NONE", p.getName());
            }
            return true;
        }
        return false;
    }
}
