package me.eccentric_nz.tardisvortexmanipulator.command;

import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetManipulator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TVMCommandGive implements CommandExecutor {

    private final TARDISVortexManipulator plugin;
    private final int full;

    public TVMCommandGive(TARDISVortexManipulator plugin) {
        this.plugin = plugin;
        full = this.plugin.getConfig().getInt("tachyon_use.max");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vmg")) {
            if (!sender.hasPermission("tardis.admin")) {
                sender.sendMessage(plugin.getPluginName() + "You don't have permission to use that command!");
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(plugin.getPluginName() + "You need to specify a player uuid and amount!");
                return true;
            }
            UUID uuid = UUID.fromString(args[0]);
            Player p = plugin.getServer().getPlayer(uuid);
            if (p == null || !p.isOnline()) {
                sender.sendMessage(plugin.getPluginName() + "Could not find player! Are they online?");
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
                        sender.sendMessage(plugin.getPluginName() + "The last argument must be a number, 'full' or 'empty'");
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
                new TVMQueryFactory(plugin).doUpdate("manipulator", set, where);
                sender.sendMessage(plugin.getPluginName() + "Tachyon level set to " + amount);
            } else {
                sender.sendMessage(plugin.getPluginName() + "Player does not have a Vortex Manipulator!");
            }
            return true;
        }
        return false;
    }
}
