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

import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.data.Area;
import me.eccentric_nz.tardis.database.resultset.ResultSetAreas;
import me.eccentric_nz.tardis.enumeration.COMPASS;
import me.eccentric_nz.tardis.enumeration.PRESET;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.planets.TARDISAliasResolver;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Locale;

/**
 * Command /tardisarea [arguments].
 * <p>
 * A dimension is a property of space, extending in a given direction, which, when combined with other dimensions of
 * width and height and time, make up the Universe.
 *
 * @author eccentric_nz
 */
public class TARDISAreaCommands implements CommandExecutor {

    public static final BlockData SNOW = Material.SNOW_BLOCK.createBlockData();
    private final TARDISPlugin plugin;

    public TARDISAreaCommands(TARDISPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
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
            switch (args[0].toLowerCase()) {
                case "start":
                    // check name is unique and acceptable
                    if (args.length < 2 || !args[1].matches("[A-Za-z0-9_]{2,16}")) {
                        TARDISMessage.send(player, "AREA_NAME_NOT_VALID");
                        return false;
                    }
                    ResultSetAreas rsa = new ResultSetAreas(plugin, null, false, true);
                    if (rsa.resultSet()) {
                        for (String s : rsa.getNames()) {
                            if (s.equals(args[1])) {
                                TARDISMessage.send(player, "AREA_IN_USE");
                                return false;
                            }
                        }
                    }
                    plugin.getTrackerKeeper().getArea().put(player.getUniqueId(), args[1]);
                    TARDISMessage.send(player, "AREA_CLICK_START");
                    return true;
                case "end":
                    if (!plugin.getTrackerKeeper().getBlock().containsKey(player.getUniqueId())) {
                        TARDISMessage.send(player, "AREA_NO_START");
                        return false;
                    }
                    plugin.getTrackerKeeper().getEnd().put(player.getUniqueId(), "end");
                    TARDISMessage.send(player, "AREA_CLICK_END");
                    return true;
                case "parking":
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
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("area_name", args[1]);
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("parking_distance", park);
                    plugin.getQueryFactory().doUpdate("areas", set, where);
                    TARDISMessage.send(player, "AREA_PARK_SET", args[1]);
                    return true;
                case "remove":
                    if (args.length < 2) {
                        TARDISMessage.send(player, "AREA_NEED");
                        return false;
                    }
                    HashMap<String, Object> wherer = new HashMap<>();
                    wherer.put("area_name", args[1]);
                    plugin.getQueryFactory().doDelete("areas", wherer);
                    TARDISMessage.send(player, "AREA_DELETE", args[1]);
                    return true;
                case "show":
                    if (args.length < 2) {
                        TARDISMessage.send(player, "AREA_NEED");
                        return false;
                    }
                    HashMap<String, Object> wherea = new HashMap<>();
                    wherea.put("area_name", args[1]);
                    ResultSetAreas rsaShow = new ResultSetAreas(plugin, wherea, false, false);
                    if (!rsaShow.resultSet()) {
                        TARDISMessage.send(player, "AREA_NOT_FOUND", ChatColor.GREEN + "/tardis list areas" + ChatColor.RESET);
                        return false;
                    }
                    Area a = rsaShow.getArea();
                    int mix = a.getMinX();
                    int miz = a.getMinZ();
                    int max = a.getMaxX();
                    int maz = a.getMaxZ();
                    World w = TARDISAliasResolver.getWorldFromAlias(a.getWorld());
                    Block b1 = w.getHighestBlockAt(mix, miz).getRelative(BlockFace.UP);
                    b1.setBlockData(SNOW);
                    Block b2 = w.getHighestBlockAt(mix, maz).getRelative(BlockFace.UP);
                    b2.setBlockData(SNOW);
                    Block b3 = w.getHighestBlockAt(max, miz).getRelative(BlockFace.UP);
                    b3.setBlockData(SNOW);
                    Block b4 = w.getHighestBlockAt(max, maz).getRelative(BlockFace.UP);
                    b4.setBlockData(SNOW);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new SetAir(b1, b2, b3, b4), 300L);
                    return true;
                case "yard":
                    if (args.length < 2) {
                        TARDISMessage.send(player, "AREA_NEED");
                        return false;
                    }
                    // set some basic defaults
                    BlockData fill = Material.COBBLESTONE.createBlockData();
                    BlockData dock = Material.STONE_BRICKS.createBlockData();
                    if (args.length > 2) {
                        try {
                            fill = Material.valueOf(args[2].toUpperCase(Locale.ENGLISH)).createBlockData();
                            if (args.length > 3) {
                                dock = Material.valueOf(args[3].toUpperCase(Locale.ENGLISH)).createBlockData();
                            }
                        } catch (IllegalArgumentException e) {
                            TARDISMessage.send(player, "ARG_MATERIAL");
                            return true;
                        }
                        if (!fill.getMaterial().isBlock() || !dock.getMaterial().isBlock() || !fill.getMaterial().isSolid() || !dock.getMaterial().isSolid()) {
                            TARDISMessage.send(player, "ARG_NOT_BLOCK");
                            return true;
                        }
                    }
                    HashMap<String, Object> yardWhere = new HashMap<>();
                    yardWhere.put("area_name", args[1]);
                    ResultSetAreas rsaYard = new ResultSetAreas(plugin, yardWhere, false, false);
                    if (!rsaYard.resultSet()) {
                        TARDISMessage.send(player, "AREA_NOT_FOUND", ChatColor.GREEN + "/tardis list areas" + ChatColor.RESET);
                        return false;
                    }
                    Area yardArea = rsaYard.getArea();
                    int yardMinX = yardArea.getMinX();
                    int yardMinZ = yardArea.getMinZ();
                    int yardMaxX = yardArea.getMaxX();
                    int yardMaxZ = yardArea.getMaxZ();
                    World yardWorld = TARDISAliasResolver.getWorldFromAlias(yardArea.getWorld());
                    for (int x = yardMinX; x <= yardMaxX; x++) {
                        for (int z = yardMinZ; z <= yardMaxZ; z++) {
                            int y = yardWorld.getHighestBlockYAt(x, z);
                            if ((x - 2) % 5 == 0 && (z - 2) % 5 == 0) {
                                yardWorld.getBlockAt(x, y, z).setBlockData(dock);
                            } else {
                                yardWorld.getBlockAt(x, y, z).setBlockData(fill);
                            }
                        }
                    }
                    return true;
                case "invisibility":
                    if (args.length < 2) {
                        TARDISMessage.send(player, "AREA_NEED");
                        return false;
                    }
                    if (args.length < 3) {
                        TARDISMessage.send(player, "AREA_INVISIBILITY_ARG");
                        return false;
                    }
                    HashMap<String, Object> invisWhere = new HashMap<>();
                    invisWhere.put("area_name", args[1]);
                    ResultSetAreas rsaInvis = new ResultSetAreas(plugin, invisWhere, false, false);
                    if (!rsaInvis.resultSet()) {
                        TARDISMessage.send(player, "AREA_NOT_FOUND", ChatColor.GREEN + "/tardis list areas" + ChatColor.RESET);
                        return false;
                    }
                    String value = args[2].toUpperCase(Locale.ENGLISH);
                    if (!value.equals("ALLOW") && !value.equals("DENY")) {
                        try {
                            PRESET.valueOf(value);
                        } catch (IllegalArgumentException e) {
                            TARDISMessage.send(player, "ARG_PRESET");
                            return false;
                        }
                    }
                    HashMap<String, Object> invisSet = new HashMap<>();
                    invisSet.put("invisibility", value);
                    HashMap<String, Object> whereInvis = new HashMap<>();
                    whereInvis.put("area_name", args[1]);
                    plugin.getQueryFactory().doUpdate("areas", invisSet, whereInvis);
                    TARDISMessage.send(player, "AREA_INVISIBILITY_SET", args[1]);
                    return true;
                case "direction":
                    if (args.length < 2) {
                        TARDISMessage.send(player, "AREA_NEED");
                        return false;
                    }
                    if (args.length < 3) {
                        TARDISMessage.send(player, "AREA_DIRECTION_ARG");
                        return false;
                    }
                    HashMap<String, Object> dirWhere = new HashMap<>();
                    dirWhere.put("area_name", args[1]);
                    ResultSetAreas rsaDir = new ResultSetAreas(plugin, dirWhere, false, false);
                    if (!rsaDir.resultSet()) {
                        TARDISMessage.send(player, "AREA_NOT_FOUND", ChatColor.GREEN + "/tardis list areas" + ChatColor.RESET);
                        return false;
                    }
                    String dir = args[2].toUpperCase(Locale.ENGLISH);
                    try {
                        COMPASS.valueOf(dir);
                    } catch (IllegalArgumentException e) {
                        if (dir.equals("NONE")) {
                            dir = "";
                        } else {
                            TARDISMessage.send(player, "ARG_DIRECTION");
                            return false;
                        }
                    }
                    HashMap<String, Object> dirSet = new HashMap<>();
                    dirSet.put("direction", dir);
                    HashMap<String, Object> whereDir = new HashMap<>();
                    whereDir.put("area_name", args[1]);
                    plugin.getQueryFactory().doUpdate("areas", dirSet, whereDir);
                    TARDISMessage.send(player, "AREA_DIRECTION_SET", args[1]);
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    private static class SetAir implements Runnable {

        private final Block b1;
        private final Block b2;
        private final Block b3;
        private final Block b4;
        private final BlockData air = TARDISConstants.AIR;

        SetAir(Block b1, Block b2, Block b3, Block b4) {
            this.b1 = b1;
            this.b2 = b2;
            this.b3 = b3;
            this.b4 = b4;
        }

        @Override
        public void run() {
            b1.setBlockData(air);
            b2.setBlockData(air);
            b3.setBlockData(air);
            b4.setBlockData(air);
        }
    }
}
