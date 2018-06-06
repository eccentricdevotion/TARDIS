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
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
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
public class TARDISSchematicPaster {

    private final TARDIS plugin;
    private final Player player;
    HashMap<Block, BlockData> postRedstoneTorches = new HashMap<>();
    HashMap<Block, TARDISBannerData> postStandingBanners = new HashMap<>();
    HashMap<Block, TARDISBannerData> postWallBanners = new HashMap<>();

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
                    Material m = Material.valueOf((String) col.get("type"));
                    BlockData data = plugin.getServer().createBlockData(col.getString("data"));
                    Block block = world.getBlockAt(x + w, y + h, z + l);
                    switch (m) {
                        case REDSTONE_TORCH:
                            postRedstoneTorches.put(block, data);
                            break;
                        case WHITE_BANNER:
                        case WHITE_WALL_BANNER:
                            JSONObject state = col.optJSONObject("banner");
                            if (state != null) {
                                TARDISBannerData tbd = new TARDISBannerData(m, data, state);
                                if (TARDISStaticUtils.isStandingBanner(m)) {
                                    postStandingBanners.put(block, tbd);
                                } else {
                                    postWallBanners.put(block, tbd);
                                }
                            }
                            break;
                        default:
//                            block.setType(m);
                            block.setData(data, true);
                            break;
                    }
                }
            }
        }
        postRedstoneTorches.entrySet().forEach((entry) -> {
            Block prtb = entry.getKey();
            BlockData ptdata = entry.getValue();
            prtb.setType(Material.REDSTONE_TORCH, true);
            prtb.setData(ptdata);
        });
        setBanners(postStandingBanners);
        setBanners(postWallBanners);
        return true;
    }
}
