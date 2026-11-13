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
package me.eccentric_nz.TARDIS.builders.interior;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.customblocks.TARDISBlockDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.desktop.ChunkUtils;
import me.eccentric_nz.TARDIS.discovery.StructureVariant;
import me.eccentric_nz.TARDIS.enumeration.UseClay;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * The TARDIS was prone to a number of technical faults, ranging from depleted resources to malfunctioning controls to a
 * simple inability to arrive at the proper time or location. While the Doctor did not build the TARDIS from scratch, he
 * has substantially modified/rebuilt it.
 *
 * @author eccentric_nz
 */
class AbandonedFadeBuildRunnable implements Runnable {

    private final TARDIS plugin;
    private final StructureVariant variant;
    private final World world;
    private final int dbID;
    private final Player player;
    private final HashMap<String, Object> set = new HashMap<>();
    private final HashMap<String, Object> where = new HashMap<>();
    private boolean running;
    private TIPSData pos;
    private BlockData[][][] arr;
    private int task, h, w, d, level = 0, row = 0, sx, sy, sz, j = 2;
    private UseClay use_clay;
    private int counter = 0;
    private double div = 1.0d;
    private BossBar bb;

    /**
     * Builds the interior of an abandoned TARDIS.
     *
     * @param plugin  an instance of the TARDIS plugin main class.
     * @param variant the fade variant to grow
     * @param world   the world where the TARDIS is to be built.
     * @param dbID    the unique key of the record for this TARDIS in the database.
     * @param player  the player to show the progress bar to, may be null.
     */
    AbandonedFadeBuildRunnable(TARDIS plugin, StructureVariant variant, World world, int dbID, Player player) {
        this.plugin = plugin;
        this.variant = variant;
        this.world = world;
        this.dbID = dbID;
        this.player = player;
    }

    @Override
    public void run() {
        if (!running) {
            // get JSON
            String[][][] template = new Template().load(plugin);
            BlockData[][][] base = BaseBlocks.set(template, variant);
            // get dimensions
            h = 15;
            w = 32;
            d = 31;
            div = (h + 1.0d) * w * (d + 1.0d);
            // calculate sx, sz
            TARDISInteriorPositioning positioning = new TARDISInteriorPositioning(plugin);
            int slot = positioning.getFreeSlot();
            pos = positioning.getTIPSData(slot);
            // save the slot
            set.put("tips", slot);
            sx = pos.getCentreX();
            sy = 64;
            sz = pos.getCentreZ();
            // get the correct chunk for ARS
            Location cl = new Location(world, sx, sy, sz);
            Chunk cars = world.getChunkAt(cl);
            String chun = world.getKey().asString() + ":" + cars.getX() + ":" + cars.getZ();
            set.put("chunk", chun);
            // get list of used chunks
            List<Chunk> chunkList = ChunkUtils.getConsoleChunks(world, cl.getChunk().getX(), cl.getChunk().getZ(), w, d);
            // update chunks list in DB
            chunkList.forEach((ch) -> {
                HashMap<String, Object> setc = new HashMap<>();
                setc.put("tardis_id", dbID);
                setc.put("world", world.getKey().asString());
                setc.put("x", ch.getX());
                setc.put("z", ch.getZ());
                plugin.getQueryFactory().doInsert("chunks", setc);
            });
            where.put("tardis_id", dbID);
            // determine 'use_clay' material
            try {
                use_clay = UseClay.valueOf(plugin.getConfig().getString("creation.use_clay"));
            } catch (IllegalArgumentException e) {
                use_clay = UseClay.WOOL;
            }
            // get input array
            arr = new FadeMutator(TARDISConstants.RANDOM.nextLong(), 7, variant).applyGradient(base, variant.gradient());
            if (player != null) {
                // start progress bar
                bb = Bukkit.createBossBar(TARDISConstants.GROWTH_STATES.getFirst(), BarColor.WHITE, BarStyle.SOLID, TARDISConstants.EMPTY_ARRAY);
                bb.setProgress(0);
                bb.addPlayer(player);
                bb.setVisible(true);
            }
            running = true;
        }
        if (level == h && row == w - 1) {
            if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                UUID randomUUID = UUID.randomUUID();
                plugin.getWorldGuardUtils().addWGProtection(randomUUID, randomUUID.toString(), pos, world);
            }
            // finished processing - update tardis table!
            plugin.getQueryFactory().doUpdate("tardis", set, where);
            plugin.getServer().getScheduler().cancelTask(task);
            task = -1;
            if (player != null) {
                bb.setProgress(1);
                bb.setVisible(false);
                bb.removeAll();
            }
        }
        BlockData[][] floor = arr[level];
        BlockData[] r = floor[row];
        // paste a column
        for (int col = 0; col <= d; col++) {
            counter++;
            int x = sx + row;
            int y = sy + level;
            int z = sz + col;
            BlockData data = r[col];
            Material type = data.getMaterial();
            if (type.equals(Material.NOTE_BLOCK)) {
                // remember the location of this Disk Storage
                String storage = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbID, 14, storage, 0);
                // set block data to correct BARRIER
                data = TARDISConstants.BARRIER;
                // spawn an item display entity
                TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.DISK_STORAGE, world, x, y, z);
            }
            if (type.equals(Material.BLUE_WOOL)) {
                switch (use_clay) {
                    case TERRACOTTA -> data = Material.BLUE_TERRACOTTA.createBlockData();
                    case CONCRETE -> data = Material.BLUE_CONCRETE.createBlockData();
                    default -> {
                        data = TARDISConstants.BARRIER;
                        // spawn an item display entity
                        TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.BLUE_BOX, world, x, y, z);
                    }
                }
            }
            if (type.equals(Material.CHEST)) {
                // remember the location of the condenser chest
                String condenser = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbID, 34, condenser, 0);
            }
            if (type.equals(Material.IRON_DOOR)) {
                Bisected bisected = (Bisected) data;
                if (bisected.getHalf().equals(Half.BOTTOM)) { // iron door bottom
                    HashMap<String, Object> setd = new HashMap<>();
                    String doorLocation = world.getKey().asString() + ":" + x + ":" + y + ":" + z;
                    setd.put("tardis_id", dbID);
                    setd.put("door_type", 1);
                    setd.put("door_location", doorLocation);
                    setd.put("door_direction", "SOUTH");
                    plugin.getQueryFactory().doInsert("doors", setd);
                }
            }
            if (type.equals(Material.STONE_BUTTON)) { // random button
                // remember the location of this button
                String button = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbID, 1, button, 0);
            }
            if (type.equals(Material.JUKEBOX)) {
                // remember the location of this Advanced Console
                String advanced = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbID, 15, advanced, 0);
                // set block data to correct BARRIER + Item Display
                data = TARDISConstants.BARRIER;
                // spawn an item display entity
                TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.ADVANCED_CONSOLE, world, x, y, z);
            }
            if (type.equals(Material.LEVER)) {
                String handbrakeLocation = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbID, 0, handbrakeLocation, 0);
                // create default json for ARS
                String[][][] empty = new String[3][9][9];
                for (int ars_y = 0; ars_y < 3; ars_y++) {
                    for (int ars_x = 0; ars_x < 9; ars_x++) {
                        for (int ars_z = 0; ars_z < 9; ars_z++) {
                            empty[ars_y][ars_x][ars_z] = "STONE";
                        }
                    }
                }
                String control = "ORANGE_POPLAR_LEAVES";
                empty[1][4][4] = control;
                // the 3 slots on the same level
                empty[1][4][5] = control;
                empty[1][5][4] = control;
                empty[1][5][5] = control;
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                JsonArray json = JsonParser.parseString(gson.toJson(empty)).getAsJsonArray();
                HashMap<String, Object> seta = new HashMap<>();
                seta.put("tardis_id", dbID);
                seta.put("json", json.toString());
                plugin.getQueryFactory().doInsert("ars", seta);
            }
            if (type.equals(Material.COMMAND_BLOCK)) {
                // command block - remember it to spawn the creeper on.
                String creeperLocation = world.getKey().asString() + ":" + (x + 0.5) + ":" + y + ":" + (z + 0.5);
                set.put("creeper", creeperLocation);
                data = Material.TUFF.createBlockData();
            }
            if (type.equals(Material.OAK_BUTTON)) {
                // oak button - remember it for the Artron Energy Capacitor.
                String woodButtonLocation = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbID, 6, woodButtonLocation, 0);
            }
            if (type.equals(Material.RED_MUSHROOM_BLOCK)) { // red mushroom block for repeaters
                // save repeater locations
                if (j < 6) {
                    String repeater = world.getKey().asString() + ":" + x + ":" + y + ":" + z;
                    data = Material.REPEATER.createBlockData();
                    Directional directional = (Directional) data;
                    switch (j) {
                        case 2 -> {
                            directional.setFacing(BlockFace.WEST);
                            plugin.getQueryFactory().insertSyncControl(dbID, 3, repeater, 0);
                        }
                        case 3 -> {
                            directional.setFacing(BlockFace.NORTH);
                            plugin.getQueryFactory().insertSyncControl(dbID, 2, repeater, 0);
                        }
                        case 4 -> {
                            directional.setFacing(BlockFace.SOUTH);
                            plugin.getQueryFactory().insertSyncControl(dbID, 5, repeater, 0);
                        }
                        default -> {
                            directional.setFacing(BlockFace.EAST);
                            plugin.getQueryFactory().insertSyncControl(dbID, 4, repeater, 0);
                        }
                    }
                    j++;
                }
            }
            TARDISBlockSetters.setBlock(world, x, y, z, data);
            if (player != null) {
                double progress = counter / div;
                if (progress > 1.0) {
                    progress = 1.0;
                }
                bb.setProgress(progress);
            }
            if (col == d && row < w) {
                row++;
            }
            if (col == d && row == w && level < h) {
                row = 0;
                level++;
                if (player != null) {
                    // set progress bar title
                    if (level == h) {
                        bb.setTitle(TARDISConstants.GROWTH_STATES.get(31));
                    } else {
                        bb.setTitle(TARDISConstants.GROWTH_STATES.get(level));
                    }
                }
            }
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
