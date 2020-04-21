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
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.rooms.TARDISPainting;
import me.eccentric_nz.TARDIS.utility.TARDISBannerData;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

import static me.eccentric_nz.TARDIS.schematic.TARDISBannerSetter.setBanners;

/**
 * @author eccentric_nz
 */
class TARDISSchematicPaster implements Runnable {

    private final TARDIS plugin;
    private final Player player;
    private final HashMap<Block, BlockData> postRedstoneTorches = new HashMap<>();
    private final HashMap<Block, TARDISBannerData> postBanners = new HashMap<>();
    private int task, l, r, h, w, d, rx, ry, rz, x, y, z;
    private int counter = 0;
    private double div = 1.0d;
    private World world;
    private UUID uuid;
    private JSONObject obj;
    private JSONArray arr;
    private boolean running = false;
    private BossBar bb;

    TARDISSchematicPaster(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        l = 0;
        r = 0;
    }

    @Override
    public void run() {
        // initialise
        if (!running) {
            uuid = player.getUniqueId();
            if (!plugin.getTrackerKeeper().getPastes().containsKey(uuid)) {
                player.sendMessage(plugin.getPluginName() + "No schematic loaded! " + ChatColor.GREEN + "/ts load [name]");
                plugin.getServer().getScheduler().cancelTask(task);
                task = -1;
                return;
            }
            obj = plugin.getTrackerKeeper().getPastes().get(uuid);
            // get dimensions
            JSONObject dimensions = (JSONObject) obj.get("dimensions");
            h = dimensions.getInt("height") - 1;
            w = dimensions.getInt("width");
            d = dimensions.getInt("length") - 1;
            div = (h + 1.0d) * w * (d + 1.0d);
            // get start location
            JSONObject r = (JSONObject) obj.get("relative");
            rx = r.getInt("x");
            ry = r.getInt("y");
            rz = r.getInt("z");
            x = player.getLocation().getBlockX() - rx;
            y = player.getLocation().getBlockY() - ry;
            z = player.getLocation().getBlockZ() - rz;
            world = player.getWorld();
            // get input array
            arr = (JSONArray) obj.get("input");
            bb = Bukkit.createBossBar("TARDIS Schematic Paste Progress", BarColor.WHITE, BarStyle.SOLID, TARDISConstants.EMPTY_ARRAY);
            bb.setProgress(0);
            bb.addPlayer(player);
            bb.setVisible(true);
            running = true;
        }
        if (l == h && r == w - 1) {
            postRedstoneTorches.forEach((prtb, ptdata) -> prtb.setBlockData(ptdata));
            setBanners(postBanners);
            // paintings
            if (obj.has("paintings")) {
                JSONArray paintings = (JSONArray) obj.get("paintings");
                for (int i = 0; i < paintings.length(); i++) {
                    JSONObject painting = paintings.getJSONObject(i);
                    JSONObject rel = painting.getJSONObject("rel_location");
                    int px = rel.getInt("x");
                    int py = rel.getInt("y");
                    int pz = rel.getInt("z");
                    Art art = Art.valueOf(painting.getString("art"));
                    BlockFace facing = BlockFace.valueOf(painting.getString("facing"));
                    Location pl = TARDISPainting.calculatePosition(art, facing, new Location(world, x + px, y + py, z + pz));
                    try {
                        Painting ent = (Painting) world.spawnEntity(pl, EntityType.PAINTING);
                        ent.teleport(pl);
                        ent.setFacingDirection(facing, true);
                        ent.setArt(art, true);
                    } catch (IllegalArgumentException e) {
                        plugin.debug("Invalid painting location!");
                    }
                }
            }
            plugin.getServer().getScheduler().cancelTask(task);
            task = -1;
            bb.setProgress(1);
            bb.setVisible(false);
            bb.removeAll();
        }
        // paste a column
        JSONArray level = (JSONArray) arr.get(l);
        JSONArray row = (JSONArray) level.get(r);
        for (int c = 0; c <= d; c++) {
            counter++;
            JSONObject col = (JSONObject) row.get(c);
            BlockData data = plugin.getServer().createBlockData(col.getString("data"));
            Block block = world.getBlockAt(x + r, y + l, z + c);
            switch (data.getMaterial()) {
                case REDSTONE_TORCH:
                    postRedstoneTorches.put(block, data);
                    break;
                case BLACK_BANNER:
                case BLACK_WALL_BANNER:
                case BLUE_BANNER:
                case BLUE_WALL_BANNER:
                case BROWN_BANNER:
                case BROWN_WALL_BANNER:
                case CYAN_BANNER:
                case CYAN_WALL_BANNER:
                case GRAY_BANNER:
                case GRAY_WALL_BANNER:
                case GREEN_BANNER:
                case GREEN_WALL_BANNER:
                case LIGHT_BLUE_BANNER:
                case LIGHT_BLUE_WALL_BANNER:
                case LIGHT_GRAY_BANNER:
                case LIGHT_GRAY_WALL_BANNER:
                case LIME_BANNER:
                case LIME_WALL_BANNER:
                case MAGENTA_BANNER:
                case MAGENTA_WALL_BANNER:
                case ORANGE_BANNER:
                case ORANGE_WALL_BANNER:
                case PINK_BANNER:
                case PINK_WALL_BANNER:
                case PURPLE_BANNER:
                case PURPLE_WALL_BANNER:
                case RED_BANNER:
                case RED_WALL_BANNER:
                case WHITE_BANNER:
                case WHITE_WALL_BANNER:
                case YELLOW_BANNER:
                case YELLOW_WALL_BANNER:
                    JSONObject state = col.optJSONObject("banner");
                    if (state != null) {
                        TARDISBannerData tbd = new TARDISBannerData(data, state);
                        postBanners.put(block, tbd);
                    }
                    break;
                default:
                    block.setBlockData(data, true);
                    break;
            }
            double progress = counter / div;
            bb.setProgress(progress);
            if (c == d && r < w) {
                r++;
            }
            if (c == d && r == w && l < h) {
                r = 0;
                l++;
            }
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
