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
import java.util.List;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command /tardisgravity [arguments].
 *
 * A dimension is a property of space, extending in a given direction, which,
 * when combined with other dimensions of width and height and time, make up the
 * Universe.
 *
 * @author eccentric_nz
 */
public class TARDISGravityCommands implements CommandExecutor {

    private TARDIS plugin;
    private List<String> directions = new ArrayList<String>();
    private HashMap<String, Integer> gravityDirection = new HashMap<String, Integer>();

    public TARDISGravityCommands(TARDIS plugin) {
        this.plugin = plugin;
        directions.add("down");
        directions.add("up");
        directions.add("north");
        directions.add("west");
        directions.add("south");
        directions.add("east");
        directions.add("remove");
        gravityDirection.put("down", 0);
        gravityDirection.put("up", 1);
        gravityDirection.put("north", 2);
        gravityDirection.put("west", 3);
        gravityDirection.put("south", 4);
        gravityDirection.put("east", 5);
        gravityDirection.put("remove", 6);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        // If the player typed /tardisarea then do the following...
        // check there is the right number of arguments
        if (cmd.getName().equalsIgnoreCase("tardisgravity")) {
            if (args.length < 1) {
                return false;
            }
            if (player == null) {
                sender.sendMessage(plugin.pluginName + ChatColor.RED + " This command can only be run by a player");
                return false;
            }
            String dir = args[0].toLowerCase(Locale.ENGLISH);
            if (directions.contains(dir)) {
                Integer[] values = new Integer[2];
                values[0] = gravityDirection.get(dir);
                if (!dir.equals("remove") && !dir.equals("down")) {
                    if (args.length < 2) {
                        return false;
                    }
                    try {
                        values[1] = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(plugin.pluginName + "Second argument must be a number!");
                        return false;
                    }
                } else {
                    values[1] = 0;
                }
                plugin.trackGravity.put(player.getName(), values);
                String message = (dir.equals("remove")) ? "remove it from the database" : "save its position";
                player.sendMessage(plugin.pluginName + "Click the wool block to " + message + ".");
                return true;
            }
        }
        return false;
    }
}
