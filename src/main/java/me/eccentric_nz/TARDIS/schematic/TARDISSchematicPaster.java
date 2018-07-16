/*
 * Copyright (C) 2018 eccentric_nz
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
import me.eccentric_nz.TARDIS.utility.TARDISBannerData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

import static me.eccentric_nz.TARDIS.schematic.TARDISBannerSetter.setBanners;

/**
 * @author eccentric_nz
 */
class TARDISSchematicPaster {

    private final TARDIS plugin;
    private final Player player;
    private final HashMap<Block, BlockData> postRedstoneTorches = new HashMap<>();
    private final HashMap<Block, TARDISBannerData> postBanners = new HashMap<>();

    public TARDISSchematicPaster(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public boolean paste() {
        UUID uuid = player.getUniqueId();
        if (!plugin.getTrackerKeeper().getPastes().containsKey(uuid)) {
            player.sendMessage(plugin.getPluginName() + "No schematic loaded! " + ChatColor.GREEN + "/ts load [name]");
            return true;
        }
        JSONObject obj = plugin.getTrackerKeeper().getPastes().get(uuid);
        // get dimensions
        JSONObject d = (JSONObject) obj.get("dimensions");
        int hei = d.getInt("height");
        int wid = d.getInt("width");
        int len = d.getInt("length");
        // get start location
        JSONObject r = (JSONObject) obj.get("relative");
        int rx = r.getInt("x");
        int ry = r.getInt("y");
        int rz = r.getInt("z");
        int x = player.getLocation().getBlockX() - rx;
        int y = player.getLocation().getBlockY() - ry;
        int z = player.getLocation().getBlockZ() - rz;
        World world = player.getWorld();
        // get input array
        JSONArray arr = (JSONArray) obj.get("input");
        // loop like crazy
        for (int h = 0; h < hei; h++) {
            JSONArray level = (JSONArray) arr.get(h);
            for (int w = 0; w < wid; w++) {
                JSONArray row = (JSONArray) level.get(w);
                for (int l = 0; l < len; l++) {
                    JSONObject col = (JSONObject) row.get(l);
                    BlockData data = plugin.getServer().createBlockData(col.getString("data"));
                    Block block = world.getBlockAt(x + w, y + h, z + l);
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
                }
            }
        }
        postRedstoneTorches.forEach((prtb, ptdata) -> {
            prtb.setType(Material.REDSTONE_TORCH, true);
            prtb.setBlockData(ptdata);
        });
        setBanners(postBanners);
        return true;
    }
}
