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
package me.eccentric_nz.TARDIS.commands;

import java.util.ArrayList;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetAreas;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * A dimension is a property of space, extending in a given direction, which,
 * when combined with other dimensions of width and height and time, make up the
 * Universe.
 *
 * @author eccentric_nz
 */
public class TARDISAreaCommands implements CommandExecutor {

    private TARDIS plugin;

    public TARDISAreaCommands(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        // If the player typed /tardisarea then do the following...
        // check there is the right number of arguments
        if (cmd.getName().equalsIgnoreCase("tardisarea")) {
            if (args.length == 0) {
                return false;
            }
            if (player == null) {
                sender.sendMessage(plugin.pluginName + ChatColor.RED + " This command can only be run by a player");
                return false;
            }
            if (args[0].equals("start")) {
                // check name is unique and acceptable
                if (args.length < 2 || !args[1].matches("[A-Za-z0-9_]{2,16}")) {
                    sender.sendMessage(plugin.pluginName + "That doesn't appear to be a valid area name (it may be too long)" + ChatColor.GREEN + " /tardisarea start [area_name_goes_here]");
                    return false;
                }
                ResultSetAreas rsa = new ResultSetAreas(plugin, null, true);
                if (rsa.resultSet()) {
                    ArrayList<HashMap<String, String>> data = rsa.getData();
                    for (HashMap<String, String> map : data) {
                        if (map.get("area_name").equals(args[1])) {
                            sender.sendMessage(plugin.pluginName + "Area name already in use!");
                            return false;
                        }
                    }
                }
                plugin.trackName.put(player.getName(), args[1]);
                player.sendMessage(plugin.pluginName + "Click the area start block to save its position.");
                return true;
            }
            if (args[0].equals("end")) {
                if (!plugin.trackBlock.containsKey(player.getName())) {
                    player.sendMessage(plugin.pluginName + ChatColor.RED + " You haven't selected an area start block!");
                    return false;
                }
                plugin.trackEnd.put(player.getName(), "end");
                player.sendMessage(plugin.pluginName + "Click the area end block to complete the area.");
                return true;
            }
            if (args[0].equals("remove")) {
                if (args.length < 2) {
                    player.sendMessage(plugin.pluginName + "You need to supply an area name!");
                    return false;
                }
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("area_name", args[1]);
                QueryFactory qf = new QueryFactory(plugin);
                qf.doDelete("areas", where);
                player.sendMessage(plugin.pluginName + "Area [" + args[1] + "] deleted!");
                return true;
            }
            if (args[0].equals("show")) {
                if (args.length < 2) {
                    player.sendMessage(plugin.pluginName + "You need to supply an area name!");
                    return false;
                }
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("area_name", args[1]);
                ResultSetAreas rsa = new ResultSetAreas(plugin, where, false);
                if (!rsa.resultSet()) {
                    player.sendMessage(plugin.pluginName + "Could not find area [" + args[1] + "]! Did you type the name correctly?");
                    return false;
                }
                int mix = rsa.getMinx();
                int miz = rsa.getMinz();
                int max = rsa.getMaxx();
                int maz = rsa.getMaxz();
                World w = plugin.getServer().getWorld(rsa.getWorld());
                final Block b1 = w.getHighestBlockAt(mix, miz).getRelative(BlockFace.UP);
                b1.setTypeId(80);
                final Block b2 = w.getHighestBlockAt(mix, maz).getRelative(BlockFace.UP);
                b2.setTypeId(80);
                final Block b3 = w.getHighestBlockAt(max, miz).getRelative(BlockFace.UP);
                b3.setTypeId(80);
                final Block b4 = w.getHighestBlockAt(max, maz).getRelative(BlockFace.UP);
                b4.setTypeId(80);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        b1.setTypeId(0);
                        b2.setTypeId(0);
                        b3.setTypeId(0);
                        b4.setTypeId(0);
                    }
                }, 300L);
                return true;
            }
        }
        return false;
    }
}
