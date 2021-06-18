/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.commands;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.TardisNumberParsers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Command /tardisgravity [arguments].
 * <p>
 * A dimension is a property of space, extending in a given direction, which, when combined with other dimensions of
 * width and height and time, make up the Universe.
 *
 * @author eccentric_nz
 */
public class TardisGravityCommands implements CommandExecutor {

    private final TardisPlugin plugin;
    private final List<String> directions = new ArrayList<>();
    private final HashMap<String, Double> gravityDirection = new HashMap<>();

    public TardisGravityCommands(TardisPlugin plugin) {
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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        // If the player typed /tardisgravity then do the following...
        if (cmd.getName().equalsIgnoreCase("tardisgravity")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                TardisMessage.send(sender, "CMD_PLAYER");
                return false;
            }
            if (!TardisPermission.hasPermission(player, "tardis.gravity")) {
                TardisMessage.send(sender, "NO_PERMS");
                return true;
            }
            if (!plugin.getConfig().getBoolean("allow.external_gravity")) {
                // check they are still in the TARDIS world
                if (!plugin.getUtils().inTARDISWorld(player)) {
                    String mess_stub = (Objects.requireNonNull(player.getLocation().getWorld()).getName().toUpperCase(Locale.ENGLISH).contains("TARDIS_WORLD_")) ? "GRAVITY_OWN_WORLD" : "GRAVITY_A_WORLD";
                    TardisMessage.send(player, mess_stub);
                    return true;
                }
            }
            // check there is the right number of arguments
            if (args.length < 1) {
                new TardisCommandHelper(plugin).getCommand("tardisgravity", sender);
                return true;
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
                            TardisMessage.send(player, "TOO_FAR");
                            return true;
                        }
                    } catch (NumberFormatException e) {
                        TardisMessage.send(player, "ARG_SEC_NUMBER");
                        return false;
                    }
                } else {
                    values[1] = 0D;
                }
                if (args.length == 3) {
                    values[2] = TardisNumberParsers.parseDouble(args[2]);
                    if (values[2] > plugin.getConfig().getDouble("growth.gravity_max_velocity")) {
                        TardisMessage.send(player, "GRAVITY_FAST");
                        return true;
                    }
                } else {
                    values[2] = 0.5D;
                }
                UUID uuid = player.getUniqueId();
                plugin.getTrackerKeeper().getGravity().put(uuid, values);
                String message = (dir.equals("remove")) ? "GRAVITY_CLICK_REMOVE" : "GRAVITY_CLICK_SAVE";
                TardisMessage.send(player, message);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getTrackerKeeper().getGravity().remove(uuid), 1200L);
                return true;
            }
        }
        return false;
    }
}
