/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.areas;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.database.data.Area;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Command /tardisarea [arguments].
 * <p>
 * A dimension is a property of space, extending in a given direction, which,
 * when combined with other dimensions of width and height and time, make up the
 * Universe.
 *
 * @author eccentric_nz
 */
public class TARDISAreaCommands implements CommandExecutor {

    private static final Pattern LETTERS_NUMBERS = Pattern.compile("[A-Za-z0-9_]{2,16}");
    private final TARDIS plugin;

    public TARDISAreaCommands(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!sender.hasPermission("tardis.admin")) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "NO_PERM_AREA");
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
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_PLAYER");
                return false;
            }
            String first = args[0].toLowerCase();
            switch (first) {
                case "start" -> {
                    // check name is unique and acceptable
                    if (args.length < 2 || !LETTERS_NUMBERS.matcher(args[1]).matches()) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_NAME_NOT_VALID");
                        return false;
                    }
                    ResultSetAreas rsa = new ResultSetAreas(plugin, null, false, true);
                    if (rsa.resultSet()) {
                        for (String s : rsa.getNames()) {
                            if (s.equals(args[1])) {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_IN_USE");
                                return false;
                            }
                        }
                    }
                    plugin.getTrackerKeeper().getArea().put(player.getUniqueId(), args[1]);
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_CLICK_START");
                    return true;
                }
                case "end" -> {
                    if (!plugin.getTrackerKeeper().getAreaStartBlock().containsKey(player.getUniqueId())) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_NO_START");
                        return false;
                    }
                    plugin.getTrackerKeeper().getAreaEndBlock().put(player.getUniqueId(), "end");
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_CLICK_END");
                    return true;
                }
                case "parking" -> {
                    if (args.length < 2) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_NEED");
                        return false;
                    }
                    if (args.length < 3) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_PARK");
                        return false;
                    }
                    int park;
                    try {
                        park = Integer.parseInt(args[2]);
                    } catch (NumberFormatException nfe) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_PARK");
                        return false;
                    }
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("area_name", args[1]);
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("parking_distance", park);
                    plugin.getQueryFactory().doUpdate("areas", set, where);
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_PARK_SET", args[1]);
                    return true;
                }
                case "remove" -> {
                    if (args.length < 2) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_NEED");
                        return false;
                    }
                    HashMap<String, Object> wherer = new HashMap<>();
                    wherer.put("area_name", args[1]);
                    plugin.getQueryFactory().doDelete("areas", wherer);
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_DELETE", args[1]);
                    return true;
                }
                case "show" -> {
                    if (args.length < 2) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_NEED");
                        return false;
                    }
                    HashMap<String, Object> wherea = new HashMap<>();
                    wherea.put("area_name", args[1]);
                    ResultSetAreas rsaShow = new ResultSetAreas(plugin, wherea, false, false);
                    if (!rsaShow.resultSet()) {
                        plugin.getMessenger().sendColouredCommand(player, "AREA_NOT_FOUND", "/tardis list areas", plugin);
                        return false;
                    }
                    if (!rsaShow.getArea().isGrid()) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_SHOW_NONGRID");
                        return true;
                    }
                    Area a = rsaShow.getArea();
                    int mix = a.getMinX();
                    int miz = a.getMinZ();
                    int max = a.getMaxX();
                    int maz = a.getMaxZ();
                    World w = TARDISAliasResolver.getWorldFromAlias(a.getWorld());
                    Set<Block> markers = new HashSet<>();
                    markers.add(w.getHighestBlockAt(mix, miz).getRelative(BlockFace.UP));
                    markers.add(w.getHighestBlockAt(mix, maz).getRelative(BlockFace.UP));
                    markers.add(w.getHighestBlockAt(max, miz).getRelative(BlockFace.UP));
                    markers.add(w.getHighestBlockAt(max, maz).getRelative(BlockFace.UP));
                    for (Block block : markers) {
                        player.sendBlockChange(block.getLocation(), TARDISConstants.SNOW_BLOCK);
                    }
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        for (Block block : markers) {
                            block.getState().update();
                        }
                    }, 300L);
                    return true;
                }
                case "yard" -> {
                    if (args.length < 2) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_NEED");
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
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_MATERIAL");
                            return true;
                        }
                        if (!fill.getMaterial().isBlock() || !dock.getMaterial().isBlock() || !fill.getMaterial().isSolid() || !dock.getMaterial().isSolid()) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_NOT_BLOCK");
                            return true;
                        }
                    }
                    HashMap<String, Object> yardWhere = new HashMap<>();
                    yardWhere.put("area_name", args[1]);
                    ResultSetAreas rsaYard = new ResultSetAreas(plugin, yardWhere, false, false);
                    if (!rsaYard.resultSet()) {
                        plugin.getMessenger().sendColouredCommand(player, "AREA_NOT_FOUND", "/tardis list areas", plugin);
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
                }
                case "invisibility" -> {
                    if (args.length < 2) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_NEED");
                        return false;
                    }
                    if (args.length < 3) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_INVISIBILITY_ARG");
                        return false;
                    }
                    HashMap<String, Object> invisWhere = new HashMap<>();
                    invisWhere.put("area_name", args[1]);
                    ResultSetAreas rsaInvis = new ResultSetAreas(plugin, invisWhere, false, false);
                    if (!rsaInvis.resultSet()) {
                        plugin.getMessenger().sendColouredCommand(player, "AREA_NOT_FOUND", "/tardis list areas", plugin);
                        return false;
                    }
                    String value = args[2].toUpperCase(Locale.ENGLISH);
                    if (!value.equals("ALLOW") && !value.equals("DENY")) {
                        try {
                            ChameleonPreset.valueOf(value);
                        } catch (IllegalArgumentException e) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_PRESET");
                            return false;
                        }
                    }
                    HashMap<String, Object> invisSet = new HashMap<>();
                    invisSet.put("invisibility", value);
                    HashMap<String, Object> whereInvis = new HashMap<>();
                    whereInvis.put("area_name", args[1]);
                    plugin.getQueryFactory().doUpdate("areas", invisSet, whereInvis);
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_INVISIBILITY_SET", args[1]);
                    return true;
                }
                case "direction" -> {
                    if (args.length < 2) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_NEED");
                        return false;
                    }
                    if (args.length < 3) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_DIRECTION_ARG");
                        return false;
                    }
                    HashMap<String, Object> dirWhere = new HashMap<>();
                    dirWhere.put("area_name", args[1]);
                    ResultSetAreas rsaDir = new ResultSetAreas(plugin, dirWhere, false, false);
                    if (!rsaDir.resultSet()) {
                        plugin.getMessenger().sendColouredCommand(player, "AREA_NOT_FOUND", "/tardis list areas", plugin);
                        return false;
                    }
                    String dir = args[2].toUpperCase(Locale.ENGLISH);
                    try {
                        COMPASS.valueOf(dir);
                    } catch (IllegalArgumentException e) {
                        if (dir.equals("NONE")) {
                            dir = "";
                        } else {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_COMPASS");
                            return false;
                        }
                    }
                    HashMap<String, Object> dirSet = new HashMap<>();
                    dirSet.put("direction", dir);
                    HashMap<String, Object> whereDir = new HashMap<>();
                    whereDir.put("area_name", args[1]);
                    plugin.getQueryFactory().doUpdate("areas", dirSet, whereDir);
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_DIRECTION_SET", args[1]);
                    return true;
                }
                case "create" -> {
                    // check name is unique and acceptable
                    if (args.length < 2 || !LETTERS_NUMBERS.matcher(args[1]).matches()) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_NAME_NOT_VALID");
                        return false;
                    }
                    ResultSetAreas rsa = new ResultSetAreas(plugin, null, false, true);
                    if (rsa.resultSet()) {
                        for (String s : rsa.getNames()) {
                            if (s.equals(args[1])) {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_IN_USE");
                                return false;
                            }
                        }
                    }
                    // add new area without min/max, grid = 0
                    HashMap<String, Object> create = new HashMap<>();
                    create.put("area_name", args[1]);
                    create.put("world", player.getLocation().getWorld().getName());
                    create.put("grid", 0);
                    plugin.getQueryFactory().doInsert("areas", create);
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_SAVED", args[1]);
                    return true;
                }
                case "add", "edit" -> {
                    // add location to specified area
                    if (args.length < 2) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_NEED");
                        return false;
                    }
                    // get area_id of specified area
                    HashMap<String, Object> wherea = new HashMap<>();
                    wherea.put("area_name", args[1]);
                    ResultSetAreas rsaId = new ResultSetAreas(plugin, wherea, false, false);
                    if (!rsaId.resultSet()) {
                        plugin.getMessenger().sendColouredCommand(player, "AREA_NOT_FOUND", "/tardis list areas", plugin);
                        return false;
                    }
                    if (rsaId.getArea().isGrid()) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_NOT_GRID");
                        return true;
                    }
                    if (first.equals("edit")) {
                        // open edit gui to allow removal of added locations
                        ItemStack[] locations = new TARDISEditAreasInventory(plugin, rsaId.getArea().getAreaId()).getLocations();
                        Inventory inventory = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Area Locations");
                        inventory.setContents(locations);
                        player.openInventory(inventory);
                    } else {
                        // get player's location
                        Location location = player.getLocation();
                        HashMap<String, Object> add = new HashMap<>();
                        add.put("area_id", rsaId.getArea().getAreaId());
                        add.put("world", location.getWorld().getName());
                        add.put("x", location.getBlockX());
                        add.put("y", location.getBlockY());
                        add.put("z", location.getBlockZ());
                        plugin.getQueryFactory().doInsert("area_locations", add);
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_ADD_LOCATION", args[1]);
                    }
                    return true;
                }
                default -> {
                    return false;
                }
            }
        }
        return false;
    }
}
