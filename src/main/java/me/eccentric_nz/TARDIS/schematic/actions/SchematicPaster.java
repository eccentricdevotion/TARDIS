/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.schematic.actions;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.schematic.setters.*;
import me.eccentric_nz.TARDIS.utility.TARDISBannerData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class SchematicPaster implements Runnable {

    private final TARDIS plugin;
    private final Player player;
    private final boolean air;
    private final HashMap<Block, BlockData> postRedstoneTorches = new HashMap<>();
    private final HashMap<Block, BlockData> postRedstoneDust = new HashMap<>();
    private final HashMap<Block, BlockData> postPistons = new HashMap<>();
    private final HashMap<Block, TARDISBannerData> postBanners = new HashMap<>();
    private final HashMap<Block, BlockData> sidratFenceBlocks = new HashMap<>();
    private int task, l, r, h, w, d, x, y, z;
    private int counter = 0;
    private double div = 1.0d;
    private World world;
    private JsonObject obj;
    private JsonArray arr;
    private boolean running = false;
    private BossBar bb;

    public SchematicPaster(TARDIS plugin, Player player, boolean air) {
        this.plugin = plugin;
        this.player = player;
        this.air = air;
        l = 0;
        r = 0;
    }

    @Override
    public void run() {
        // initialise
        if (!running) {
            UUID uuid = player.getUniqueId();
            if (!plugin.getTrackerKeeper().getPastes().containsKey(uuid)) {
                plugin.getMessenger().message(player, TardisModule.TARDIS, "No schematic loaded! /ts load [console|room|structure|user] [name]");
                plugin.getServer().getScheduler().cancelTask(task);
                task = -1;
                return;
            }
            obj = plugin.getTrackerKeeper().getPastes().get(uuid);
            // get dimensions
            JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
            h = dimensions.get("height").getAsInt() - 1;
            w = dimensions.get("width").getAsInt();
            d = dimensions.get("length").getAsInt() - 1;
            div = (h + 1.0d) * w * (d + 1.0d);
            // get start location
            JsonObject r = obj.get("relative").getAsJsonObject();
            int rx = r.get("x").getAsInt();
            int ry = r.get("y").getAsInt();
            int rz = r.get("z").getAsInt();
            x = player.getLocation().getBlockX() - rx;
            y = player.getLocation().getBlockY() - ry;
            z = player.getLocation().getBlockZ() - rz;
            world = player.getWorld();
            // get input array
            arr = obj.get("input").getAsJsonArray();
            bb = Bukkit.createBossBar("TARDIS Schematic Paste Progress", BarColor.WHITE, BarStyle.SOLID, TARDISConstants.EMPTY_ARRAY);
            bb.setProgress(0);
            bb.addPlayer(player);
            bb.setVisible(true);
            running = true;
        }
        if (l == h && r == w - 1) {
            for (Map.Entry<Block, BlockData> map : postRedstoneTorches.entrySet()) {
                map.getKey().setBlockData(map.getValue());
                if (TARDIS.plugin.getBlockLogger().isLogging()) {
                    TARDIS.plugin.getBlockLogger().logPlacement(map.getKey());
                }
            }
            for (Map.Entry<Block, BlockData> map : postRedstoneDust.entrySet()) {
                map.getKey().setBlockData(map.getValue());
                if (TARDIS.plugin.getBlockLogger().isLogging()) {
                    TARDIS.plugin.getBlockLogger().logPlacement(map.getKey());
                }
            }
            for (Map.Entry<Block, BlockData> map : postPistons.entrySet()) {
                map.getKey().setBlockData(map.getValue());
                if (TARDIS.plugin.getBlockLogger().isLogging()) {
                    TARDIS.plugin.getBlockLogger().logPlacement(map.getKey());
                }
            }
            BannerSetter.setBanners(postBanners);
            SIDRATFenceSetter.update(sidratFenceBlocks);
            // mannequins
            if (obj.has("mannequins")) {
                JsonArray mannequins = obj.get("mannequins").getAsJsonArray();
                MannequinSetter.setMannequins(mannequins, world, x, y, z);
            }
            // armour stands
            if (obj.has("armour_stands")) {
                JsonArray stands = obj.get("armour_stands").getAsJsonArray();
                ArmourStandSetter.setStands(stands, world, x, y, z);
            }
            // paintings
            if (obj.has("paintings")) {
                JsonArray paintings = (JsonArray) obj.get("paintings");
                PaintingSetter.setArt(paintings, world, x, y, z);
            }
            Location start = new Location(world, x, y, z);
            if (obj.has("item_frames")) {
                JsonArray frames = obj.get("item_frames").getAsJsonArray();
                ItemFrameSetter.curate(frames, start, -1);
            }
            if (obj.has("item_displays")) {
                JsonArray displays = obj.get("item_displays").getAsJsonArray();
                for (int i = 0; i < displays.size(); i++) {
                    ItemDisplaySetter.fakeBlock(displays.get(i).getAsJsonObject(), start, -1);
                }
            }
            plugin.getServer().getScheduler().cancelTask(task);
            task = -1;
            bb.setProgress(1);
            bb.setVisible(false);
            bb.removeAll();
        }
        // paste a column
        JsonArray level = (JsonArray) arr.get(l);
        JsonArray row = (JsonArray) level.get(r);
        for (int c = 0; c <= d; c++) {
            counter++;
            JsonObject col = row.get(c).getAsJsonObject();
            BlockData data = plugin.getServer().createBlockData(col.get("data").getAsString());
            Block block = world.getBlockAt(x + r, y + l, z + c);
            if (!block.getType().isAir() && plugin.getBlockLogger().isLogging()) {
                plugin.getBlockLogger().logRemoval(block);
            }
            switch (data.getMaterial()) {
                case AIR, CAVE_AIR, VOID_AIR -> {
                    if (air) {
                        block.setBlockData(data, true);
                    }
                }
                case REDSTONE_TORCH -> postRedstoneTorches.put(block, data);
                case REDSTONE -> postRedstoneDust.put(block, data);
                case STICKY_PISTON -> postPistons.put(block, data);
                case PLAYER_HEAD, PLAYER_WALL_HEAD -> {
                    block.setBlockData(data, true);
                    JsonObject head = col.has("head") ? col.get("head").getAsJsonObject() : null;
                    if (head != null) {
                        if (head.has("uuid")) {
                            UUID uuid = UUID.fromString(head.get("uuid").getAsString());
                            if (uuid != null) {
                                HeadSetter.textureSkull(plugin, uuid, head, block);
                            }
                        }
                    }
                }
                case DECORATED_POT -> {
                    block.setBlockData(data, true);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        if (col.has("pot")) {
                            JsonObject pot = col.get("pot").getAsJsonObject();
                            PotSetter.decorate(plugin, pot, block);
                        }
                    }, 1L);
                }
                case OAK_SHELF -> {
                    block.setBlockData(data, true);
                    if (plugin.getBlockLogger().isLogging()) {
                        plugin.getBlockLogger().logPlacement(block);
                    }
                    if (col.has("items")) {
                        ShelfSetter.stock(block, col.get("items").getAsJsonArray());
                    }
                }
                case PALE_OAK_FENCE -> {
                    block.setBlockData(data, false);
                    sidratFenceBlocks.put(block, data);
                }
                default -> {
                    if (Tag.BANNERS.isTagged(data.getMaterial())) {
                        JsonObject state = col.has("banner") ? col.get("banner").getAsJsonObject() : null;
                        if (state != null) {
                            TARDISBannerData tbd = new TARDISBannerData(data, state);
                            postBanners.put(block, tbd);
                        }
                    } else if (Tag.ALL_SIGNS.isTagged(data.getMaterial())) {
                        JsonObject state = col.has("sign") ? col.get("sign").getAsJsonObject() : null;
                        if (state != null) {
                            block.setBlockData(data, true);
                            SignSetter.setSign(block, state, null, 0);
                        }
                    } else {
                        block.setBlockData(data, true);
                        if (plugin.getBlockLogger().isLogging()) {
                            plugin.getBlockLogger().logPlacement(block);
                        }
                    }
                }
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
