/*
 * Copyright (C) 2023 eccentric_nz
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.rooms.TARDISPainting;
import static me.eccentric_nz.TARDIS.schematic.TARDISBannerSetter.setBanners;
import me.eccentric_nz.TARDIS.schematic.TARDISHeadSetter;
import me.eccentric_nz.TARDIS.schematic.TARDISItemFrameSetter;
import me.eccentric_nz.TARDIS.utility.TARDISBannerData;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
public class SchematicPaster implements Runnable {

    private final TARDIS plugin;
    private final Player player;
    private final boolean air;
    private final HashMap<Block, BlockData> postRedstoneTorches = new HashMap<>();
    private final HashMap<Block, TARDISBannerData> postBanners = new HashMap<>();
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
                player.sendMessage(plugin.getPluginName() + "No schematic loaded! " + ChatColor.GREEN + "/ts load [name]");
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
            setBanners(postBanners);
            // paintings
            if (obj.has("paintings")) {
                JsonArray paintings = obj.get("paintings").getAsJsonArray();
                for (int i = 0; i < paintings.size(); i++) {
                    JsonObject painting = paintings.get(i).getAsJsonObject();
                    JsonObject rel = painting.get("rel_location").getAsJsonObject();
                    int px = rel.get("x").getAsInt();
                    int py = rel.get("y").getAsInt();
                    int pz = rel.get("z").getAsInt();
                    Art art = Art.valueOf(painting.get("art").getAsString());
                    BlockFace facing = BlockFace.valueOf(painting.get("facing").getAsString());
                    Location pl = TARDISPainting.calculatePosition(art, facing, new Location(world, x + px, y + py, z + pz));
                    try {
                        Painting ent = (Painting) world.spawnEntity(pl, EntityType.PAINTING);
                        ent.teleport(pl);
                        ent.setFacingDirection(facing, true);
                        ent.setArt(art, true);
                    } catch (IllegalArgumentException e) {
                        plugin.debug("Invalid painting location!" + pl);
                    }
                }
            }
            if (obj.has("item_frames")) {
                JsonArray frames = obj.get("item_frames").getAsJsonArray();
                Location start = new Location(world, x, y, z);
                for (int i = 0; i < frames.size(); i++) {
                    TARDISItemFrameSetter.curate(frames.get(i).getAsJsonObject(), start, -1);
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
                case BLACK_BANNER, BLACK_WALL_BANNER, BLUE_BANNER, BLUE_WALL_BANNER, BROWN_BANNER, BROWN_WALL_BANNER, CYAN_BANNER, CYAN_WALL_BANNER, GRAY_BANNER, GRAY_WALL_BANNER, GREEN_BANNER, GREEN_WALL_BANNER, LIGHT_BLUE_BANNER, LIGHT_BLUE_WALL_BANNER, LIGHT_GRAY_BANNER, LIGHT_GRAY_WALL_BANNER, LIME_BANNER, LIME_WALL_BANNER, MAGENTA_BANNER, MAGENTA_WALL_BANNER, ORANGE_BANNER, ORANGE_WALL_BANNER, PINK_BANNER, PINK_WALL_BANNER, PURPLE_BANNER, PURPLE_WALL_BANNER, RED_BANNER, RED_WALL_BANNER, WHITE_BANNER, WHITE_WALL_BANNER, YELLOW_BANNER, YELLOW_WALL_BANNER -> {
                    JsonObject state = col.has("banner") ? col.get("banner").getAsJsonObject() : null;
                    if (state != null) {
                        TARDISBannerData tbd = new TARDISBannerData(data, state);
                        postBanners.put(block, tbd);
                    }
                }
                case PLAYER_HEAD, PLAYER_WALL_HEAD -> {
                    block.setBlockData(data, true);
                    JsonObject head = col.has("head") ? col.get("head").getAsJsonObject() : null;
                    if (head != null) {
                        if (head.has("uuid")) {
                            UUID uuid = UUID.fromString(head.get("uuid").getAsString());
                            if (uuid != null) {
                                TARDISHeadSetter.textureSkull(plugin, uuid, head, block);
                            }
                        }
                    }
                }
                case ACACIA_SIGN, BAMBOO_SIGN, CHERRY_SIGN, BIRCH_SIGN, CRIMSON_SIGN, DARK_OAK_SIGN, JUNGLE_SIGN, MANGROVE_SIGN, OAK_SIGN, SPRUCE_SIGN, WARPED_SIGN,
                        ACACIA_WALL_SIGN, BAMBOO_WALL_SIGN, CHERRY_WALL_SIGN, BIRCH_WALL_SIGN, CRIMSON_WALL_SIGN, DARK_OAK_WALL_SIGN, JUNGLE_WALL_SIGN, MANGROVE_WALL_SIGN, OAK_WALL_SIGN, SPRUCE_WALL_SIGN, WARPED_WALL_SIGN,
                        ACACIA_HANGING_SIGN, BAMBOO_HANGING_SIGN, CHERRY_HANGING_SIGN, BIRCH_HANGING_SIGN, CRIMSON_HANGING_SIGN, DARK_OAK_HANGING_SIGN, JUNGLE_HANGING_SIGN, MANGROVE_HANGING_SIGN, OAK_HANGING_SIGN, SPRUCE_HANGING_SIGN, WARPED_HANGING_SIGN,
                        ACACIA_WALL_HANGING_SIGN, BAMBOO_WALL_HANGING_SIGN, CHERRY_WALL_HANGING_SIGN, BIRCH_WALL_HANGING_SIGN, CRIMSON_WALL_HANGING_SIGN, DARK_OAK_WALL_HANGING_SIGN, JUNGLE_WALL_HANGING_SIGN, MANGROVE_WALL_HANGING_SIGN, OAK_WALL_HANGING_SIGN, SPRUCE_WALL_HANGING_SIGN, WARPED_WALL_HANGING_SIGN-> {
                    block.setBlockData(data, true);
                    Sign sign = (Sign) block.getState();
                    JsonObject text = col.has("sign") ? col.get("sign").getAsJsonObject() : null;
                    if (text != null) {
                        sign.setLine(0, text.get("line0").getAsString());
                        sign.setLine(1, text.get("line1").getAsString());
                        sign.setLine(2, text.get("line2").getAsString());
                        sign.setLine(3, text.get("line3").getAsString());
                        sign.setGlowingText(text.get("glowing").getAsBoolean());
                        DyeColor colour = DyeColor.valueOf(text.get("colour").getAsString());
                        sign.setColor(colour);
                        sign.setEditable(text.get("editable").getAsBoolean());
                        sign.update();
                    }
                }
                default -> {
                    block.setBlockData(data, true);
                    if (plugin.getBlockLogger().isLogging()) {
                        plugin.getBlockLogger().logPlacement(block);
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
