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
package me.eccentric_nz.TARDIS.schematic;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TARDISSchematicCommand implements CommandExecutor {

    private final TARDIS plugin;

    public TARDISSchematicCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisschematic")) {
            if (args.length < 1) {
                TARDISMessage.send(sender, "TOO_FEW_ARGS");
                return true;
            }
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                TARDISMessage.send(sender, "CMD_PLAYER");
                return true;
            }
            if (!player.hasPermission("tardis.admin")) {
                TARDISMessage.send(sender, "CMD_ADMIN");
                return true;
            }
            UUID uuid = player.getUniqueId();
            if (args.length == 1 && args[0].equalsIgnoreCase("paste")) {
                TARDISSchematicPaster paster = new TARDISSchematicPaster(plugin, player);
                int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, paster, 1L, 3L);
                paster.setTask(task);
                return true;
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("pastecsv")) {
                Location eyeLocation = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 50).getLocation();
                String path = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator + "legacy_budget.csv";
                File csv = new File(path);
                if (csv.exists()) {
                    TARDISCSVPaster paster = new TARDISCSVPaster(plugin);
                    paster.buildLegacy(paster.arrayFromCSV(csv), eyeLocation);
                } else {
                    TARDISMessage.message(player, "Nice try, but it looks like you don't know what this command is for...");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("clear")) {
                // check they have selected start and end blocks
                if (!plugin.getTrackerKeeper().getStartLocation().containsKey(uuid)) {
                    TARDISMessage.send(player, "SCHM_NO_START");
                    return true;
                }
                if (!plugin.getTrackerKeeper().getEndLocation().containsKey(uuid)) {
                    TARDISMessage.send(player, "SCHM_NO_END");
                    return true;
                }
                // get the world
                World w = plugin.getTrackerKeeper().getStartLocation().get(uuid).getWorld();
                String chk_w = plugin.getTrackerKeeper().getEndLocation().get(uuid).getWorld().getName();
                if (!w.getName().equals(chk_w)) {
                    TARDISMessage.send(player, "SCHM_WORLD");
                    return true;
                }
                // get the raw coords
                int sx = plugin.getTrackerKeeper().getStartLocation().get(uuid).getBlockX();
                int sy = plugin.getTrackerKeeper().getStartLocation().get(uuid).getBlockY();
                int sz = plugin.getTrackerKeeper().getStartLocation().get(uuid).getBlockZ();
                int ex = plugin.getTrackerKeeper().getEndLocation().get(uuid).getBlockX();
                int ey = plugin.getTrackerKeeper().getEndLocation().get(uuid).getBlockY();
                int ez = plugin.getTrackerKeeper().getEndLocation().get(uuid).getBlockZ();
                // get the min & max coords
                int minx = Math.min(sx, ex);
                int maxx = Math.max(sx, ex);
                int miny = Math.min(sy, ey);
                int maxy = Math.max(sy, ey);
                int minz = Math.min(sz, ez);
                int maxz = Math.max(sz, ez);
                // loop through the blocks inside this cube
                for (int l = miny; l <= maxy; l++) {
                    for (int r = minx; r <= maxx; r++) {
                        for (int c = minz; c <= maxz; c++) {
                            Block b = w.getBlockAt(r, l, c);
                            b.setBlockData(TARDISConstants.AIR);
                        }
                    }
                }
                return true;
            }
            if (args.length < 2) {
                TARDISMessage.send(player, "TOO_FEW_ARGS");
                TARDISMessage.send(player, "SCHM_NAME");
                return true;
            }
            if (!args[0].equalsIgnoreCase("load") && !args[0].equalsIgnoreCase("save") && !args[0].equalsIgnoreCase("replace")) {
                return false;
            }
            if (args[0].equalsIgnoreCase("save")) {
                // check they have selected start and end blocks
                if (!plugin.getTrackerKeeper().getStartLocation().containsKey(uuid)) {
                    TARDISMessage.send(player, "SCHM_NO_START");
                    return true;
                }
                if (!plugin.getTrackerKeeper().getEndLocation().containsKey(uuid)) {
                    TARDISMessage.send(player, "SCHM_NO_END");
                    return true;
                }
                // get the world
                World w = plugin.getTrackerKeeper().getStartLocation().get(uuid).getWorld();
                String chk_w = plugin.getTrackerKeeper().getEndLocation().get(uuid).getWorld().getName();
                if (!w.getName().equals(chk_w)) {
                    TARDISMessage.send(player, "SCHM_WORLD");
                    return true;
                }
                // get the raw coords
                int sx = plugin.getTrackerKeeper().getStartLocation().get(uuid).getBlockX();
                int sy = plugin.getTrackerKeeper().getStartLocation().get(uuid).getBlockY();
                int sz = plugin.getTrackerKeeper().getStartLocation().get(uuid).getBlockZ();
                int ex = plugin.getTrackerKeeper().getEndLocation().get(uuid).getBlockX();
                int ey = plugin.getTrackerKeeper().getEndLocation().get(uuid).getBlockY();
                int ez = plugin.getTrackerKeeper().getEndLocation().get(uuid).getBlockZ();
                // get the min & max coords
                int minx = Math.min(sx, ex);
                int maxx = Math.max(sx, ex);
                int miny = Math.min(sy, ey);
                int maxy = Math.max(sy, ey);
                int minz = Math.min(sz, ez);
                int maxz = Math.max(sz, ez);
                // create a JSON object for relative position
                JsonObject relative = new JsonObject();
                int px = player.getLocation().getBlockX() - minx;
                int py = player.getLocation().getBlockY() - miny;
                int pz = player.getLocation().getBlockZ() - minz;
                relative.addProperty("x", px);
                relative.addProperty("y", py);
                relative.addProperty("z", pz);
                // create a JSON object for dimensions
                JsonObject dimensions = new JsonObject();
                int width = (maxx - minx) + 1;
                int height = (maxy - miny) + 1;
                int length = (maxz - minz) + 1;
                dimensions.addProperty("width", width);
                dimensions.addProperty("height", height);
                dimensions.addProperty("length", length);
                if (width != length) {
                    TARDISMessage.send(player, "SCHM_SQUARE");
                    return true;
                }
                if ((width % 16 != 0 || length % 16 != 0) && !args[1].equals("zero") && !args[1].equals("junk")) {
                    TARDISMessage.send(player, "SCHM_MULTIPLE");
                    return true;
                }
                JsonArray paintings = new JsonArray();
                List<Entity> entities = new ArrayList<>();
                // create JSON arrays for block data
                JsonArray levels = new JsonArray();
                // loop through the blocks inside this cube
                for (int l = miny; l <= maxy; l++) {
                    JsonArray rows = new JsonArray();
                    for (int r = minx; r <= maxx; r++) {
                        JsonArray columns = new JsonArray();
                        for (int c = minz; c <= maxz; c++) {
                            JsonObject obj = new JsonObject();
                            Block b = w.getBlockAt(r, l, c);
                            // check for paintings
                            Location bLocation = b.getLocation();
                            for (Entity entity : bLocation.getWorld().getNearbyEntities(bLocation, 1.25, 1.25, 1.25)) {
                                if (entity instanceof Painting art) {
                                    Location ploc = entity.getLocation();
                                    if (!entities.contains(entity)) {
                                        JsonObject painting = new JsonObject();
                                        JsonObject loc = new JsonObject();
                                        loc.addProperty("x", ploc.getBlockX() - minx);
                                        loc.addProperty("y", ploc.getBlockY() - miny);
                                        loc.addProperty("z", ploc.getBlockZ() - minz);
                                        painting.add("rel_location", loc);
                                        painting.addProperty("art", art.getArt().toString());
                                        painting.addProperty("facing", entity.getFacing().toString());
                                        paintings.add(painting);
                                        entities.add(entity);
                                    }
                                }
                            }
                            String blockData = b.getBlockData().getAsString();
                            obj.addProperty("data", blockData);
                            // banners
                            if (TARDISStaticUtils.isBanner(b.getType())) {
                                JsonObject state = new JsonObject();
                                Banner banner = (Banner) b.getState();
                                JsonArray patterns = new JsonArray();
                                if (banner.numberOfPatterns() > 0) {
                                    banner.getPatterns().forEach((p) -> {
                                        JsonObject pattern = new JsonObject();
                                        pattern.addProperty("pattern", p.getPattern().toString());
                                        pattern.addProperty("pattern_colour", p.getColor().toString());
                                        patterns.add(pattern);
                                    });
                                }
                                state.add("patterns", patterns);
                                obj.add("banner", state);
                            }
                            columns.add(obj);
                        }
                        rows.add(columns);
                    }
                    levels.add(rows);
                }
                JsonObject schematic = new JsonObject();
                schematic.add("relative", relative);
                schematic.add("dimensions", dimensions);
                schematic.add("input", levels);
                if (paintings.size() > 0) {
                    schematic.add("paintings", paintings);
                }
                String output = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator + args[1] + ".json";
                File file = new File(output);
                try {
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file), 16 * 1024)) {
                        bw.write(schematic.toString());
                    }
                    TARDISSchematicGZip.zip(output, plugin.getDataFolder() + File.separator + "user_schematics" + File.separator + args[1] + ".tschm");
                    file.delete();
                    TARDISMessage.send(player, "SCHM_SAVED", args[1]);
                } catch (IOException e) {
                    TARDISMessage.send(player, "SCHM_ERROR");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("load")) {
                String instr = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator + args[1] + ".tschm";
                File file = new File(instr);
                if (!file.exists()) {
                    TARDISMessage.send(player, "SCHM_NOT_VALID");
                    return true;
                }
                JsonObject sch = TARDISSchematicGZip.unzip(instr);
                plugin.getTrackerKeeper().getPastes().put(uuid, sch);
                TARDISMessage.send(player, "SCHM_LOADED", ChatColor.GREEN + "/ts paste" + ChatColor.RESET);
                return true;
            }
            if (args[0].equalsIgnoreCase("replace")) {
                if (args.length < 3) {
                    TARDISMessage.send(player, "TOO_FEW_ARGS");
                    return true;
                }
                // check they have selected start and end blocks
                if (!plugin.getTrackerKeeper().getStartLocation().containsKey(uuid)) {
                    TARDISMessage.send(player, "SCHM_NO_START");
                    return true;
                }
                if (!plugin.getTrackerKeeper().getEndLocation().containsKey(uuid)) {
                    TARDISMessage.send(player, "SCHM_NO_END");
                    return true;
                }
                // get the world
                World w = plugin.getTrackerKeeper().getStartLocation().get(uuid).getWorld();
                String chk_w = plugin.getTrackerKeeper().getEndLocation().get(uuid).getWorld().getName();
                if (!w.getName().equals(chk_w)) {
                    TARDISMessage.send(player, "SCHM_WORLD");
                    return true;
                }
                try {
                    Material from = Material.valueOf(args[1].toUpperCase());
                    Material to = Material.valueOf(args[2].toUpperCase());
                    // get the raw coords
                    int sx = plugin.getTrackerKeeper().getStartLocation().get(uuid).getBlockX();
                    int sy = plugin.getTrackerKeeper().getStartLocation().get(uuid).getBlockY();
                    int sz = plugin.getTrackerKeeper().getStartLocation().get(uuid).getBlockZ();
                    int ex = plugin.getTrackerKeeper().getEndLocation().get(uuid).getBlockX();
                    int ey = plugin.getTrackerKeeper().getEndLocation().get(uuid).getBlockY();
                    int ez = plugin.getTrackerKeeper().getEndLocation().get(uuid).getBlockZ();
                    // get the min & max coords
                    int minx = Math.min(sx, ex);
                    int maxx = Math.max(sx, ex);
                    int miny = Math.min(sy, ey);
                    int maxy = Math.max(sy, ey);
                    int minz = Math.min(sz, ez);
                    int maxz = Math.max(sz, ez);
                    // loop through the blocks inside this cube
                    for (int l = miny; l <= maxy; l++) {
                        for (int r = minx; r <= maxx; r++) {
                            for (int c = minz; c <= maxz; c++) {
                                Block b = w.getBlockAt(r, l, c);
                                if (b.getType().equals(from)) {
                                    BlockState state = b.getState();
                                    if (state instanceof BlockState) {
                                        plugin.getTardisHelper().removeTileEntity(state);
                                    }
                                    b.setType(to);
                                }
                            }
                        }
                    }
                } catch (IllegalArgumentException e) {
                    TARDISMessage.send(player, "ARG_MATERIAL");
                    return true;
                }
                return true;
            }
        }
        return false;
    }
}
