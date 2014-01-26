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
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.tardischunkgenerator.TARDISChunkGenerator;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

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

    private final TARDIS plugin;
    private final List<String> directions = new ArrayList<String>();
    private final HashMap<String, Double> gravityDirection = new HashMap<String, Double>();

    public TARDISGravityCommands(TARDIS plugin) {
        this.plugin = plugin;
        directions.add("down");
        directions.add("up");
        directions.add("north");
        directions.add("west");
        directions.add("south");
        directions.add("east");
        directions.add("remove");
        gravityDirection.put("down", 0D);
        gravityDirection.put("up", 1D);
        gravityDirection.put("north", 2D);
        gravityDirection.put("west", 3D);
        gravityDirection.put("south", 4D);
        gravityDirection.put("east", 5D);
        gravityDirection.put("remove", 6D);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // If the player typed /tardisarea then do the following...
        // check there is the right number of arguments
        if (cmd.getName().equalsIgnoreCase("tardisgravity")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                sender.sendMessage(plugin.pluginName + ChatColor.RED + "This command can only be run by a player");
                return false;
            }
            if (!player.hasPermission("tardis.gravity")) {
                sender.sendMessage(plugin.pluginName + MESSAGE.NO_PERMS.getText());
                return true;
            }
            // check they are still in the TARDIS world
            World world = player.getLocation().getWorld();
            String name = world.getName();
            ChunkGenerator gen = world.getGenerator();
            boolean special = name.contains("TARDIS_TimeVortex") && (world.getWorldType().equals(WorldType.FLAT) || gen instanceof TARDISChunkGenerator);
            if (!name.equals("TARDIS_WORLD_" + player.getName()) && !special) {
                String mess_stub = (name.contains("TARDIS_WORLD_")) ? "your own" : "a";
                player.sendMessage(plugin.pluginName + "You must be in " + mess_stub + " TARDIS world to make a gravity well!");
                return true;
            }
            if (args.length < 1) {
                return false;
            }
            String dir = args[0].toLowerCase(Locale.ENGLISH);
            if (directions.contains(dir)) {
                Double[] values = new Double[3];
                values[0] = gravityDirection.get(dir);
                if (!dir.equals("remove") && !dir.equals("down")) {
                    if (args.length < 2) {
                        return false;
                    }
                    try {
                        values[1] = Double.parseDouble(args[1]);
                        if (values[1] > plugin.getConfig().getDouble("growth.gravity_max_distance")) {
                            player.sendMessage(plugin.pluginName + "That distance is too far!");
                            return true;
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage(plugin.pluginName + "Second argument must be a number!");
                        return false;
                    }
                } else {
                    values[1] = 0D;
                }
                if (args.length == 3) {
                    values[2] = plugin.utils.parseDouble(args[2]);
                    if (values[2] > plugin.getConfig().getDouble("growth.gravity_max_velocity")) {
                        player.sendMessage(plugin.pluginName + "That velocity is too fast!");
                        return true;
                    }
                } else {
                    values[2] = 0.5D;
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
