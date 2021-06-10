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
package me.eccentric_nz.tardis.siegemode;

import com.google.gson.JsonObject;
import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.builders.TARDISInteriorPostioning;
import me.eccentric_nz.tardis.builders.TARDISTIPSData;
import me.eccentric_nz.tardis.database.data.Archive;
import me.eccentric_nz.tardis.database.data.TARDIS;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.desktop.TARDISUpgradeData;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.schematic.ResultSetArchive;
import me.eccentric_nz.tardis.schematic.TARDISSchematicGZip;
import me.eccentric_nz.tardis.utility.TARDISBlockSetters;
import me.eccentric_nz.tardis.utility.TARDISStaticLocationGetters;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

/**
 * There was also a safety mechanism for when tardis rooms were deleted, automatically relocating any living beings in
 * the deleted room, depositing them in the control room.
 *
 * @author eccentric_nz
 */
class TARDISSiegeWallFloorRunnable implements Runnable {

    private final TARDISPlugin plugin;
    private final UUID uuid;
    private final TARDISUpgradeData tud;
    private final boolean toSiege;
    private boolean running;
    private int level = 0;
    private int row = 0;
    private int h;
    private int w;
    private int c;
    private int startX;
    private int startY;
    private int startZ;
    private World world;
    private Material wallType;
    private Material floorType;
    private Material siegeWallType;
    private Material siegeFloorType;
    private Player player;
    private int taskId;
    private Archive archive;

    TARDISSiegeWallFloorRunnable(TARDISPlugin plugin, UUID uuid, TARDISUpgradeData tud, boolean toSiege) {
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
                    TARDISMessage.send(cp, "ARCHIVE_NOT_FOUND");
                    // cancel task
                    plugin.getServer().getScheduler().cancelTask(taskId);
                    return;
                }
            }
            JsonObject obj;
            if (archive == null) {
                String directory = (tud.getSchematic().isCustom()) ? "user_schematics" : "schematics";
                String path = plugin.getDataFolder() + File.separator + directory + File.separator + tud.getSchematic().getPermission() + ".tschm";
                File file = new File(path);
                if (!file.exists()) {
                    plugin.debug("Could not find a schematic with that name!");
                    // cancel task
                    plugin.getServer().getScheduler().cancelTask(taskId);
                    return;
                }
                // get JSON
                obj = TARDISSchematicGZip.unzip(path);
            } else {
                obj = archive.getJson();
            }
            // get dimensions
            assert obj != null;
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
            TARDIS tardis = rs.getTardis();
            int slot = tardis.getTIPS();
            int id = tardis.getTardisId();
            if (slot != -1) { // default world - use TIPS
                TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
                TARDISTIPSData pos = tintpos.getTIPSData(slot);
                startX = pos.getCentreX();
                startZ = pos.getCentreZ();
            } else {
                int[] gsl = plugin.getLocationUtils().getStartLocation(id);
                startX = gsl[0];
                startZ = gsl[2];
            }
            startY = TARDISConstants.HIGHER.contains(tud.getSchematic().getPermission()) ? 65 : 64;
            world = TARDISStaticLocationGetters.getWorld(tardis.getChunk());
            // wall/floor block prefs
            wallType = Material.valueOf(tud.getWall());
            floorType = Material.valueOf(tud.getFloor());
            // siege wall/floor block prefs
            siegeWallType = Material.valueOf(tud.getSiegeWall());
            siegeFloorType = Material.valueOf(tud.getSiegeFloor());
            // set running
            running = true;
            player = plugin.getServer().getPlayer(uuid);
            // remove upgrade data
            plugin.getTrackerKeeper().getUpgrades().remove(uuid);
        }
        if (level == (h - 1) && row == (w - 1)) {
            // we're finished cancel the task
            plugin.getServer().getScheduler().cancelTask(taskId);
            taskId = 0;
        } else {
            // place a row of blocks
            for (int col = 0; col < c; col++) {
                int x = startX + row;
                int y = startY + level;
                int z = startZ + col;
                Block bb = world.getBlockAt(x, y, z);
                Material compare_type = bb.getType();
                Material to_wall_type = (toSiege) ? wallType : siegeWallType;
                Material to_floor_type = (toSiege) ? floorType : siegeFloorType;
                Material type;
                if (compare_type.equals(to_wall_type)) {
                    type = (toSiege) ? siegeWallType : wallType;
                    TARDISBlockSetters.setBlock(world, x, y, z, type);
                }
                if (compare_type.equals(to_floor_type)) {
                    type = (toSiege) ? siegeFloorType : floorType;
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

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
