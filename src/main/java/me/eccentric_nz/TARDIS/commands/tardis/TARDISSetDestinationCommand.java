/*
 * Copyright (C) 2013 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.commands.tardis;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.travel.TARDISPluginRespect;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSetDestinationCommand {

    private final TARDIS plugin;

    public TARDISSetDestinationCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    public boolean doSetDestination(Player player, String[] args) {
        if (player.hasPermission("tardis.save")) {
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("owner", player.getName());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                player.sendMessage(plugin.pluginName + MESSAGE.NO_TARDIS);
                return false;
            }
            if (args.length < 2) {
                player.sendMessage(plugin.pluginName + "Too few command arguments!");
                return false;
            }
            if (!args[1].matches("[A-Za-z0-9_]{2,16}")) {
                player.sendMessage(plugin.pluginName + "The destination name must be between 2 and 16 characters and have no spaces!");
                return false;
            } else if (args[1].equalsIgnoreCase("hide") || args[1].equalsIgnoreCase("rebuild") || args[1].equalsIgnoreCase("home")) {
                player.sendMessage(plugin.pluginName + "That is a reserved destination name!");
                return false;
            } else {
                int id = rs.getTardis_id();
                TARDISCircuitChecker tcc = null;
                if (plugin.getConfig().getString("preferences.difficulty").equals("hard")) {
                    tcc = new TARDISCircuitChecker(plugin, id);
                    tcc.getCircuits();
                }
                if (tcc != null && !tcc.hasMemory()) {
                    player.sendMessage(plugin.pluginName + "The Memory Circuit is missing from the console!");
                    return true;
                }
                // check they are not in the tardis
                HashMap<String, Object> wherettrav = new HashMap<String, Object>();
                wherettrav.put("player", player.getName());
                wherettrav.put("tardis_id", id);
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wherettrav, false);
                if (rst.resultSet()) {
                    player.sendMessage(plugin.pluginName + "You cannot bring the Police Box here because you are inside a TARDIS!");
                    return true;
                }
                // get location player is looking at
                Block b = player.getTargetBlock(plugin.tardisCommand.transparent, 50);
                Location l = b.getLocation();
                if (!plugin.ta.areaCheckInExisting(l)) {
                    player.sendMessage(plugin.pluginName + "You cannot use /tardis setdest in a TARDIS area! Please use " + ChatColor.AQUA + "/tardistravel area [area name]");
                    return true;
                }
                String world = l.getWorld().getName();
                if (!plugin.getConfig().getBoolean("travel.include_default_world") && plugin.getConfig().getBoolean("creation.default_world") && world.equals(plugin.getConfig().getString("creation.default_world_name"))) {
                    player.sendMessage(plugin.pluginName + "The server admin will not allow you to set the TARDIS destination to this world!");
                    return true;
                }
                // check the world is not excluded
                if (!plugin.getConfig().getBoolean("worlds." + world)) {
                    player.sendMessage(plugin.pluginName + "You cannot bring the TARDIS Police Box to this world");
                    return true;
                }
                TARDISPluginRespect respect = new TARDISPluginRespect(plugin);
                if (!respect.getRespect(player, l, true)) {
                    return true;
                }
                if (player.hasPermission("tardis.exile") && plugin.getConfig().getBoolean("travel.exile")) {
                    String areaPerm = plugin.ta.getExileArea(player);
                    if (plugin.ta.areaCheckInExile(areaPerm, l)) {
                        player.sendMessage(plugin.pluginName + "You exile status does not allow you to save the TARDIS to this location!");
                        return false;
                    }
                }
                String dw = l.getWorld().getName();
                int dx = l.getBlockX();
                int dy = l.getBlockY() + 1;
                int dz = l.getBlockZ();
                QueryFactory qf = new QueryFactory(plugin);
                HashMap<String, Object> set = new HashMap<String, Object>();
                set.put("tardis_id", id);
                set.put("dest_name", args[1]);
                set.put("world", dw);
                set.put("x", dx);
                set.put("y", dy);
                set.put("z", dz);
                if (qf.doSyncInsert("destinations", set) < 0) {
                    return false;
                } else {
                    player.sendMessage(plugin.pluginName + "The destination '" + args[1] + "' was saved successfully.");
                    return true;
                }
            }
        } else {
            player.sendMessage(plugin.pluginName + MESSAGE.NO_PERMS);
            return false;
        }
    }
}
