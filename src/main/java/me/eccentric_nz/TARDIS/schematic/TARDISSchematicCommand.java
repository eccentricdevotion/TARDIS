/*
 * Copyright (C) 2020 eccentric_nz
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

import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;

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
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisschematic")) {
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
                return new TARDISSchematicPaster(plugin, player).paste();
            }
            if (args.length < 2) {
                TARDISMessage.send(player, "TOO_FEW_ARGS");
                return true;
            }
            if (!args[0].equalsIgnoreCase("load") && !args[0].equalsIgnoreCase("save")) {
                TARDISMessage.send(player, "SCHM_NAME");
                return true;
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
                String chk_w = plugin.getTrackerKeeper().getStartLocation().get(uuid).getWorld().getName();
                if (!w.getName().equals(chk_w)) {
                    TARDISMessage.send(player, "SCHM_WORLD!");
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
                JSONObject relative = new JSONObject();
                int px = player.getLocation().getBlockX() - minx;
                int py = player.getLocation().getBlockY() - miny;
                int pz = player.getLocation().getBlockZ() - minz;
                relative.put("x", px);
                relative.put("y", py);
                relative.put("z", pz);
                // create a JSON object for dimensions
                JSONObject dimensions = new JSONObject();
                int width = (maxx - minx) + 1;
                int height = (maxy - miny) + 1;
                int length = (maxz - minz) + 1;
                dimensions.put("width", width);
                dimensions.put("height", height);
                dimensions.put("length", length);
                if (width != length) {
                    TARDISMessage.send(player, "SCHM_SQUARE");
                    return true;
                }
                if ((width % 16 != 0 || length % 16 != 0) && !args[1].equals("zero") && !args[1].equals("junk")) {
                    TARDISMessage.send(player, "SCHM_MULTIPLE");
                    return true;
                }
                JSONArray paintings = new JSONArray();
                List<Entity> entities = new ArrayList<>();
                // create JSON arrays for block data
                JSONArray levels = new JSONArray();
                // loop through the blocks inside this cube
                for (int l = miny; l <= maxy; l++) {
                    JSONArray rows = new JSONArray();
                    for (int r = minx; r <= maxx; r++) {
                        JSONArray columns = new JSONArray();
                        for (int c = minz; c <= maxz; c++) {
                            JSONObject obj = new JSONObject();
                            Block b = w.getBlockAt(r, l, c);
                            // check for paintings
                            Location bLocation = b.getLocation();
                            for (Entity entity : bLocation.getWorld().getNearbyEntities(bLocation, 1.25, 1.25, 1.25)) {
                                if (entity instanceof Painting) {
                                    Location ploc = entity.getLocation();
                                    if (!entities.contains(entity)) {
                                        JSONObject painting = new JSONObject();
                                        JSONObject loc = new JSONObject();
                                        loc.put("x", ploc.getBlockX() - minx);
                                        loc.put("y", ploc.getBlockY() - miny);
                                        loc.put("z", ploc.getBlockZ() - minz);
                                        painting.put("rel_location", loc);
                                        painting.put("art", ((Painting) entity).getArt().toString());
                                        painting.put("facing", entity.getFacing().toString());
                                        paintings.put(painting);
                                        entities.add(entity);
                                    }
                                }
                            }
                            String blockData = b.getBlockData().getAsString();
                            obj.put("data", blockData);
                            // banners
                            if (TARDISStaticUtils.isBanner(b.getType())) {
                                JSONObject state = new JSONObject();
                                Banner banner = (Banner) b.getState();
                                JSONArray patterns = new JSONArray();
                                if (banner.numberOfPatterns() > 0) {
                                    banner.getPatterns().forEach((p) -> {
                                        JSONObject pattern = new JSONObject();
                                        pattern.put("pattern", p.getPattern().toString());
                                        pattern.put("pattern_colour", p.getColor().toString());
                                        patterns.put(pattern);
                                    });
                                }
                                state.put("patterns", patterns);
                                obj.put("banner", state);
                            }
                            columns.put(obj);
                        }
                        rows.put(columns);
                    }
                    levels.put(rows);
                }
                JSONObject schematic = new JSONObject();
                schematic.put("relative", relative);
                schematic.put("dimensions", dimensions);
                schematic.put("input", levels);
                if (paintings.length() > 0) {
                    schematic.put("paintings", paintings);
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
                JSONObject sch = TARDISSchematicGZip.unzip(instr);
                plugin.getTrackerKeeper().getPastes().put(uuid, sch);
                TARDISMessage.send(player, "SCHM_LOADED", ChatColor.GREEN + "/ts paste" + ChatColor.RESET);
                return true;
            }
        }
        return false;
    }
}
