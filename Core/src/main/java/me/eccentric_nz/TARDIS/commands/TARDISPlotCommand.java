package me.eccentric_nz.TARDIS.commands;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardischunkgenerator.worldgen.PlotGenerator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TARDISPlotCommand implements CommandExecutor, TabCompleter {

    private final TARDIS plugin;

    public TARDISPlotCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisplot")) {
            if (!(sender instanceof Player player)) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_PLAYER");
                return true;
            }
            if (args[0].equalsIgnoreCase("name")) {
                // are they in a plot world?
                if (!(player.getWorld().getGenerator() instanceof PlotGenerator)) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "PLOT_WORLD");
                    return true;
                }
                StringBuilder builder = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    builder.append(args[i]);
                    if (i < args.length - 1) {
                        builder.append(" ");
                    }
                }
                String name = builder.toString();
                if (name.length() < 2 || name.length() > 16) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "PLOT_BAD_NAME");
                    return true;
                }
                plugin.getTrackerKeeper().getPlotters().put(player.getUniqueId(), name);
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "PLOT_CLICK");
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            return List.of("name");
        }
        return null;
    }
}
