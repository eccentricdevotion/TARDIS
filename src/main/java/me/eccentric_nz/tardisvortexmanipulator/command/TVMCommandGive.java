package me.eccentric_nz.tardisvortexmanipulator.command;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetManipulator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TVMCommandGive {

    private final TARDIS plugin;
    private final int full;

    public TVMCommandGive(TARDIS plugin) {
        this.plugin = plugin;
        full = this.plugin.getVortexConfig().getInt("tachyon_use.max");
    }

    public boolean process(CommandSender sender, String[] args) {
        if (!TARDISPermission.hasPermission(sender, "tardis.admin")) {
            TARDISMessage.send(sender, TardisModule.VORTEX_MANIPULATOR, "VM_PERM_CMD");
            return true;
        }
        if (args.length < 3) {
            TARDISMessage.send(sender, TardisModule.VORTEX_MANIPULATOR, "VM_UUID");
            return true;
        }
        Player p = plugin.getServer().getPlayer(args[1]);
        if (p == null || !p.isOnline()) {
            TARDISMessage.send(sender, TardisModule.VORTEX_MANIPULATOR, "NOT_ONLINE");
            return true;
        }
        UUID uuid = p.getUniqueId();
        // check for existing record
        TVMResultSetManipulator rs = new TVMResultSetManipulator(plugin, uuid.toString());
        if (rs.resultSet()) {
            int tachyon_level = rs.getTachyonLevel();
            int amount;
            if (args[2].equalsIgnoreCase("full")) {
                amount = full;
            } else if (args[2].equalsIgnoreCase("empty")) {
                amount = 0;
            } else {
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    TARDISMessage.send(sender, TardisModule.VORTEX_MANIPULATOR, "VM_LAST_ARG");
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
            where.put("uuid", uuid.toString());
            plugin.getQueryFactory().doUpdate("manipulator", set, where);
            TARDISMessage.send(sender, TardisModule.VORTEX_MANIPULATOR, "VM_TACHYON_SET", "" + amount);
        } else {
            TARDISMessage.send(sender, TardisModule.VORTEX_MANIPULATOR, "VM_NONE", p.getName());
        }
        return true;
    }
}
