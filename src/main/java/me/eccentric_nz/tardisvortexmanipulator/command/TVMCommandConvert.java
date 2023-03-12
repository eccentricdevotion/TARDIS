package me.eccentric_nz.tardisvortexmanipulator.command;

import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisvortexmanipulator.database.Converter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TVMCommandConvert implements CommandExecutor {

    private final TARDISVortexManipulator plugin;
    private final int full;

    public TVMCommandConvert(TARDISVortexManipulator plugin) {
        this.plugin = plugin;
        full = this.plugin.getConfig().getInt("tachyon_use.max");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vmd")) {
            if (!sender.hasPermission("tardis.admin")) {
                sender.sendMessage(plugin.getPluginName() + "You don't have permission to use that command!");
                return true;
            }
            if (args.length < 1 || !args[0].equalsIgnoreCase("convert_database")) {
                return false;
            }
            try {
                plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Converter(plugin, sender));
                return true;
            } catch (Exception e) {
                sender.sendMessage("Database conversion failed! " + e.getMessage());
                return true;
            }
        }
        return false;
    }
}
