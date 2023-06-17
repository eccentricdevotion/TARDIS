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
package me.eccentric_nz.TARDIS.siegemode;

import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.database.data.Archive;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.desktop.TARDISUpgradeData;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.schematic.ResultSetArchive;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * There was also a safety mechanism for when TARDIS rooms were deleted,
 * automatically relocating any living beings in the deleted room, depositing
 * them in the control room.
 *
 * @author eccentric_nz
 */
class TARDISSiegeWallFloorRunnable implements Runnable {

    private final TARDIS plugin;
    private final UUID uuid;
    private final TARDISUpgradeData tud;
    private final boolean toSiege;
    private boolean running;
    private int level = 0;
    private int row = 0;
    private int h;
    private int w;
    private int c;
    private int startx;
    private int starty;
    private int startz;
    private World world;
    private Material wall_type;
    private Material floor_type;
    private Material siege_wall_type;
    private Material siege_floor_type;
    private Player player;
    private int taskID;
    private Archive archive;

    TARDISSiegeWallFloorRunnable(TARDIS plugin, UUID uuid, TARDISUpgradeData tud, boolean toSiege) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.tud = tud;
        this.toSiege = toSiege;
    }

    @Override

    public void run() {
        // initialise
        if (!running) {
            // get Archive if nescessary
            if (tud.getSchematic().getPermission().equals("archive")) {
                HashMap<String, Object> wherean = new HashMap<>();
                wherean.put("uuid", uuid.toString());
                wherean.put("use", 1);
                ResultSetArchive rs = new ResultSetArchive(plugin, wherean);
                if (rs.resultSet()) {
                    archive = rs.getArchive();
                } else {
                    // abort
                    Player cp = plugin.getServer().getPlayer(uuid);
                    plugin.getMessenger().send(cp, TardisModule.TARDIS, "ARCHIVE_NOT_FOUND");
                    // cancel task
                    plugin.getServer().getScheduler().cancelTask(taskID);
                    return;
                }
            }
            JsonObject obj;
            // get JSON
            if (archive == null) {
                obj = TARDISSchematicGZip.getObject(plugin, "consoles", tud.getSchematic().getPermission(), tud.getSchematic().isCustom());
                if (obj == null) {
                    // cancel task
                    plugin.getServer().getScheduler().cancelTask(taskID);
                    return;
                }
            } else {
                obj = archive.getJSON();
            }
            // get dimensions
            JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
            h = dimensions.get("height").getAsInt();
            w = dimensions.get("width").getAsInt();
            c = dimensions.get("length").getAsInt();
            // calculate startx, starty, startz
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("uuid", uuid.toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 0);
            if (!rs.resultSet()) {
                // abort and return energy
                HashMap<String, Object> wherea = new HashMap<>();
                wherea.put("uuid", uuid.toString());
                int amount = plugin.getArtronConfig().getInt("upgrades." + tud.getSchematic().getPermission());
                plugin.getQueryFactory().alterEnergyLevel("tardis", amount, wherea, player);
            }
            Tardis tardis = rs.getTardis();
            int slot = tardis.getTIPS();
            int id = tardis.getTardis_id();
            if (slot != -1000001) { // default world - use TIPS
                TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
                TARDISTIPSData pos = tintpos.getTIPSData(slot);
                startx = pos.getCentreX();
                startz = pos.getCentreZ();
            } else {
                int[] gsl = plugin.getLocationUtils().getStartLocation(id);
                startx = gsl[0];
                startz = gsl[2];
            }
            if (tud.getSchematic().getPermission().equals("mechanical")) {
                starty = 62;
            } else if (TARDISConstants.HIGHER.contains(tud.getSchematic().getPermission())) {
                starty = 65;
            } else {
                starty = 64;
            }
            world = TARDISStaticLocationGetters.getWorldFromSplitString(tardis.getChunk());
            // wall/floor block prefs
            wall_type = Material.valueOf(tud.getWall());
            floor_type = Material.valueOf(tud.getFloor());
            // siege wall/floor block prefs
            siege_wall_type = Material.valueOf(tud.getSiegeWall());
            siege_floor_type = Material.valueOf(tud.getSiegeFloor());
            // set running
            running = true;
            player = plugin.getServer().getPlayer(uuid);
            // remove upgrade data
            plugin.getTrackerKeeper().getUpgrades().remove(uuid);
        }
        if (level == (h - 1) && row == (w - 1)) {
            // we're finished cancel the task
            plugin.getServer().getScheduler().cancelTask(taskID);
            taskID = 0;
        } else {
            // place a row of blocks
            for (int col = 0; col < c; col++) {
                int x = startx + row;
                int y = starty + level;
                int z = startz + col;
                Block bb = world.getBlockAt(x, y, z);
                Material compare_type = bb.getType();
                Material to_wall_type = (toSiege) ? wall_type : siege_wall_type;
                Material to_floor_type = (toSiege) ? floor_type : siege_floor_type;
                Material type;
                if (compare_type.equals(to_wall_type)) {
                    type = (toSiege) ? siege_wall_type : wall_type;
                    TARDISBlockSetters.setBlock(world, x, y, z, type);
                }
                if (compare_type.equals(to_floor_type)) {
                    type = (toSiege) ? siege_floor_type : floor_type;
                    TARDISBlockSetters.setBlock(world, x, y, z, type);
                }
            }
            if (row < w) {
                row++;
            }
            if (row == w && level < h) {
                row = 0;
                level++;
            }
        }
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }
}
