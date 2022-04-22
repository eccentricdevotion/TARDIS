package me.eccentric_nz.TARDIS.commands.config;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;

public class TARDISAutonomousAreaCommand {

    private final TARDIS plugin;

    public TARDISAutonomousAreaCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean processArea(CommandSender sender, String[] args) {
        if (args.length < 3) {
            TARDISMessage.send(sender, "TOO_FEW_ARGS");
            return true;
        }
        String area = args[1];
        // check the area exists
        HashMap<String, Object> where = new HashMap<>();
        where.put("area_name", area);
        ResultSetAreas rsa = new ResultSetAreas(plugin, where, false, true);
        if (!rsa.resultSet()) {
            TARDISMessage.send(sender, "AREA_NOT_FOUND", ChatColor.GREEN + "/tardis list areas" + ChatColor.RESET);
            return true;
        }
        List<String> autoAreas = plugin.getConfig().getStringList("autonomous_areas");
        if (args[2].equalsIgnoreCase("add")) {
            if (autoAreas.contains(area)) {
                TARDISMessage.send(sender, "AREA_ALREADY_ADDED", area);
                return true;
            }
            autoAreas.add(area);
        } else {
            // remove
            if (!autoAreas.contains(area)) {
                TARDISMessage.send(sender, "AREA_NOT_IN_LIST", area);
                return true;
            }
            autoAreas.remove(area);
        }
        plugin.getConfig().set("autonomous_areas", autoAreas);
        plugin.saveConfig();
        TARDISMessage.send(sender, "AREA_LIST_UPDATED");
        return true;
    }
}
