package me.eccentric_nz.TARDIS.commands;

import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TARDISPrefsCommands implements CommandExecutor {

    TARDISDatabase service = TARDISDatabase.getInstance();
    private final TARDIS plugin;
    private List<String> firstArgs = new ArrayList<String>();

    public TARDISPrefsCommands(TARDIS plugin) {
        this.plugin = plugin;
        firstArgs.add("sfx");
        firstArgs.add("platform");
        firstArgs.add("quotes");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        // If the player typed /tardisprefs then do the following...
        // check there is the right number of arguments
        if (cmd.getName().equalsIgnoreCase("tardisprefs")) {
            if (args.length == 0) {
                return false;
            }
            if (player == null) {
                sender.sendMessage(plugin.pluginName + ChatColor.RED + " This command can only be run by a player");
                return false;
            }
            String pref = args[0].toLowerCase();
            if (firstArgs.contains(pref)) {
                if (player.hasPermission("tardis.timetravel")) {
                    if (args.length < 2 || (!args[1].equalsIgnoreCase("on") && !args[1].equalsIgnoreCase("off"))) {
                        sender.sendMessage(plugin.pluginName + "You need to specify if " + pref + " should be on or off!");
                        return false;
                    }
                    // get the players preferences
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("player", player.getName());
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, where);
                    QueryFactory qf = new QueryFactory(plugin);
                    HashMap<String, Object> set = new HashMap<String, Object>();
                    if (!rsp.resultSet()) {
                        set.put("player", player.getName());
                        qf.doInsert("player_prefs", set);
                    }
                    HashMap<String, Object> setp = new HashMap<String, Object>();
                    HashMap<String, Object> wherep = new HashMap<String, Object>();
                    wherep.put("player", player.getName());
                    if (args[1].equalsIgnoreCase("on")) {
                        setp.put(pref + "_on", 1);
                        sender.sendMessage(plugin.pluginName + pref + " were turned ON!");
                    }
                    if (args[1].equalsIgnoreCase("off")) {
                        setp.put(pref + "_on", 0);
                        sender.sendMessage(plugin.pluginName + pref + " were turned OFF.");
                    }
                    qf.doUpdate("player_prefs", setp, wherep);
                    return true;
                } else {
                    sender.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                    return false;
                }
            }
        }
        return false;
    }
}