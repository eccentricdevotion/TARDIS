/*
 * Copyright (C) 2014 eccentric_nz
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
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command /tardisarea [arguments].
 *
 * A dimension is a property of space, extending in a given direction, which,
 * when combined with other dimensions of width and height and time, make up the
 * Universe.
 *
 * @author eccentric_nz
 */
public class TARDISAreaCommands implements CommandExecutor {

    private final TARDIS plugin;

    public TARDISAreaCommands(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("tardis.admin")) {
            TARDISMessage.send(sender, "NO_PERM_AREA");
            return true;
        }
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        // If the player typed /tardisarea then do the following...
        // check there is the right number of arguments
        if (cmd.getName().equalsIgnoreCase("tardisarea")) {
            if (args.length == 0) {
                new TARDISCommandHelper(plugin).getCommand("tardisarea", sender);
                return true;
            }
            if (player == null) {
                TARDISMessage.send(sender, "CMD_PLAYER");
                return false;
            }
            if (args[0].equals("start")) {
                // check name is unique and acceptable
                if (args.length < 2 || !args[1].matches("[A-Za-z0-9_]{2,16}")) {
                    TARDISMessage.send(player, "AREA_NAME_NOT_VALID");
                    return false;
                }
                ResultSetAreas rsa = new ResultSetAreas(plugin, null, true);
                if (rsa.resultSet()) {
                    ArrayList<HashMap<String, String>> data = rsa.getData();
                    for (HashMap<String, String> map : data) {
                        if (map.get("area_name").equals(args[1])) {
                            TARDISMessage.send(player, "AREA_IN_USE");
                            return false;
                        }
                    }
                }
                plugin.getTrackerKeeper().getArea().put(player.getUniqueId(), args[1]);
                TARDISMessage.send(player, "AREA_CLICK_START");
                return true;
            }
            if (args[0].equals("end")) {
                if (!plugin.getTrackerKeeper().getBlock().containsKey(player.getUniqueId())) {
                    TARDISMessage.send(player, "AREA_NO_START");
                    return false;
                }
                plugin.getTrackerKeeper().getEnd().put(player.getUniqueId(), "end");
                TARDISMessage.send(player, "AREA_CLICK_END");
                return true;
            }
            if (args[0].equals("parking")) {
                if (args.length < 2) {
                    TARDISMessage.send(player, "AREA_NEED");
                    return false;
                }
                if (args.length < 3) {
                    TARDISMessage.send(player, "AREA_PARK");
                    return false;
                }
                int park;
                try {
                    park = Integer.parseInt(args[2]);
                } catch (NumberFormatException nfe) {
                    TARDISMessage.send(player, "AREA_PARK");
                    return false;
                }
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("area_name", args[1]);
                HashMap<String, Object> set = new HashMap<String, Object>();
                set.put("parking_distance", park);
                QueryFactory qf = new QueryFactory(plugin);
                qf.doUpdate("areas", set, where);
                TARDISMessage.send(player, "AREA_PARK_SET", args[1]);
                return true;
            }
            if (args[0].equals("remove")) {
                if (args.length < 2) {
                    TARDISMessage.send(player, "AREA_NEED");
                    return false;
                }
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("area_name", args[1]);
                QueryFactory qf = new QueryFactory(plugin);
                qf.doDelete("areas", where);
                TARDISMessage.send(player, "AREA_DELETE", args[1]);
                return true;
            }
            if (args[0].equalsIgnoreCase("show")) {
                if (args.length < 2) {
                    TARDISMessage.send(player, "AREA_NEED");
                    return false;
                }
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("area_name", args[1]);
                ResultSetAreas rsa = new ResultSetAreas(plugin, where, false);
                if (!rsa.resultSet()) {
                    TARDISMessage.send(player, "AREA_NOT_FOUND", ChatColor.GREEN + "/tardis list areas" + ChatColor.RESET);
                    return false;
                }
                int mix = rsa.getMinX();
                int miz = rsa.getMinZ();
                int max = rsa.getMaxX();
                int maz = rsa.getMaxZ();
                World w = plugin.getServer().getWorld(rsa.getWorld());
                final Block b1 = w.getHighestBlockAt(mix, miz).getRelative(BlockFace.UP);
                b1.setType(Material.SNOW_BLOCK);
                final Block b2 = w.getHighestBlockAt(mix, maz).getRelative(BlockFace.UP);
                b2.setType(Material.SNOW_BLOCK);
                final Block b3 = w.getHighestBlockAt(max, miz).getRelative(BlockFace.UP);
                b3.setType(Material.SNOW_BLOCK);
                final Block b4 = w.getHighestBlockAt(max, maz).getRelative(BlockFace.UP);
                b4.setType(Material.SNOW_BLOCK);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new SetAir(b1, b2, b3, b4), 300L);
                return true;
            }
            if (args[0].equalsIgnoreCase("yard")) {
                if (args.length < 2) {
                    TARDISMessage.send(player, "AREA_NEED");
                    return false;
                }                // set some basic defaults
                Material fill = Material.COBBLESTONE;
                Material dock = Material.BRICK;
                if (args.length > 2) {
                    try {
                        fill = Material.valueOf(args[2].toUpperCase());
                        if (args.length > 3) {
                            dock = Material.valueOf(args[3].toUpperCase());
                        }
                    } catch (IllegalArgumentException e) {
                        TARDISMessage.send(player, "ARG_MATERIAL");
                        return true;
                    }
                    if (!fill.isBlock() || !dock.isBlock() || !fill.isSolid() || !dock.isSolid()) {
                        TARDISMessage.send(player, "ARG_NOT_BLOCK");
                        return true;
                    }
                }
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("area_name", args[1]);
                ResultSetAreas rsa = new ResultSetAreas(plugin, where, false);
                if (!rsa.resultSet()) {
                    TARDISMessage.send(player, "AREA_NOT_FOUND", ChatColor.GREEN + "/tardis list areas" + ChatColor.RESET);
                    return false;
                }
                int mix = rsa.getMinX();
                int miz = rsa.getMinZ();
                int max = rsa.getMaxX();
                int maz = rsa.getMaxZ();
                World w = plugin.getServer().getWorld(rsa.getWorld());
                for (int x = mix; x <= max; x++) {
                    for (int z = miz; z <= maz; z++) {
                        int y = w.getHighestBlockYAt(x, z) - 1;
                        if ((x - 2) % 5 == 0 && (z - 2) % 5 == 0) {
                            w.getBlockAt(x, y, z).setType(dock);
                        } else {
                            w.getBlockAt(x, y, z).setType(fill);
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    private static class SetAir implements Runnable {

        private final Block b1;
        private final Block b2;
        private final Block b3;
        private final Block b4;

        SetAir(Block b1, Block b2, Block b3, Block b4) {
            this.b1 = b1;
            this.b2 = b2;
            this.b3 = b3;
            this.b4 = b4;
        }

        @Override
        public void run() {
            b1.setType(Material.AIR);
            b2.setType(Material.AIR);
            b3.setType(Material.AIR);
            b4.setType(Material.AIR);
        }
    }
}
