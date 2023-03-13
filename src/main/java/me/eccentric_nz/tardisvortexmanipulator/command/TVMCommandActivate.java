package me.eccentric_nz.tardisvortexmanipulator.command;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
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
                sender.sendMessage(plugin.getPluginName() + "You don't have permission to use that command!");
                return true;
            }
            if (args.length < 1) {
                sender.sendMessage(plugin.getPluginName() + "You need to specify a player name!");
                return true;
            }
            Player p = plugin.getServer().getPlayer(args[0]);
            if (p == null || !p.isOnline()) {
                sender.sendMessage(plugin.getPluginName() + "Could not find player! Are they online?");
                return true;
            }
            String uuid = p.getUniqueId().toString();
            // check for existing record
            TVMResultSetManipulator rs = new TVMResultSetManipulator(plugin, uuid);
            if (!rs.resultSet()) {
                HashMap<String, Object> set = new HashMap<>();
                set.put("uuid", uuid);
                plugin.getQueryFactory().doInsert("manipulator", set);
                sender.sendMessage(plugin.getPluginName() + "Vortex Manipulator activated!");
            } else {
                sender.sendMessage(plugin.getPluginName() + "The Vortex Manipulator is already activated!");
            }
            return true;
        }
        return false;
    }
}
