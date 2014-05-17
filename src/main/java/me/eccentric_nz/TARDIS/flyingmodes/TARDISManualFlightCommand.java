package me.eccentric_nz.TARDIS.flyingmodes;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TARDISManualFlightCommand implements CommandExecutor {

    private final TARDIS plugin;

    public TARDISManualFlightCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("manualflight")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                sender.sendMessage(plugin.getPluginName() + "Command can only be used by a player!");
                return true;
            }
            // check number of arguments
            // 0
            if (args.length == 0) {
                long delay = plugin.getConfig().getLong("delay");
                // star a manual flight session
                sender.sendMessage(plugin.getPluginName() + "Starting manual flight session, click those repeaters!");
                TARDISManualFlightRunnable mfr = new TARDISManualFlightRunnable(plugin, player);
                int taskid = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, mfr, (delay / 2), delay);
                mfr.setTaskID(taskid);
                return true;
            }
            if (args.length == 1) {
                // set the repeater location
                int which = Integer.parseInt(args[0]);
                String option;
                switch (which) {
                    case 1:
                        option = "one";
                        break;
                    case 2:
                        option = "two";
                        break;
                    case 3:
                        option = "three";
                        break;
                    default:
                        option = "four";
                        break;
                }
                plugin.getTrackerKeeper().getCommand().put(player.getUniqueId(), option);
                player.sendMessage(plugin.getPluginName() + "Click the repeater...");
            }
            return true;
        }
        return false;
    }
}
