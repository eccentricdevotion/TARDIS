/*
 * Copyright (C) 2014 eccentric_nz
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

import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.desktop.*;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * There was also a safety mechanism for when TARDIS rooms were deleted,
 * automatically relocating any living beings in the deleted room, depositing
 * them in the control room.
 *
 * @author eccentric_nz
 */
public class TARDISSiegeWallFloorRunnable implements Runnable {

    private final TARDIS plugin;
    private final UUID uuid;
    private final TARDISUpgradeData tud;
    private final boolean toSiege;
    private boolean running;
    int id, slot, level = 0, row = 0, h, w, c, startx, starty, startz, resetx, resetz, j = 2;
    World world;
    JSONArray arr;
    Material wall_type, floor_type, siege_wall_type, siege_floor_type;
    byte wall_data, floor_data, siege_wall_data, siege_floor_data;
    QueryFactory qf;
    boolean own_world;
    Player player;
    int taskID;

    public TARDISSiegeWallFloorRunnable(TARDIS plugin, UUID uuid, TARDISUpgradeData tud, boolean toSiege) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.tud = tud;
        this.toSiege = toSiege;
        this.qf = new QueryFactory(this.plugin);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void run() {
        // initialise
        if (!running) {
            String directory = (tud.getSchematic().isCustom()) ? "user_schematics" : "schematics";
            String path = plugin.getDataFolder() + File.separator + directory + File.separator + tud.getSchematic().getPermission() + ".tschm";
            File file = new File(path);
            if (!file.exists()) {
                plugin.debug(plugin.getPluginName() + "Could not find a schematic with that name!");
                return;
            }
            // get JSON
            JSONObject obj = TARDISSchematicGZip.unzip(path);
            // get dimensions
            JSONObject dimensions = (JSONObject) obj.get("dimensions");
            h = dimensions.getInt("height");
            w = dimensions.getInt("width");
            c = dimensions.getInt("length");
            // calculate startx, starty, startz
            HashMap<String, Object> wheret = new HashMap<String, Object>();
            wheret.put("uuid", uuid.toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false);
            if (!rs.resultSet()) {
                // abort and return energy
                HashMap<String, Object> wherea = new HashMap<String, Object>();
                wherea.put("uuid", uuid.toString());
                int amount = plugin.getArtronConfig().getInt("upgrades." + tud.getSchematic().getPermission());
                qf.alterEnergyLevel("tardis", amount, wherea, player);
            }
            slot = rs.getTIPS();
            id = rs.getTardis_id();
            if (slot != -1) { // default world - use TIPS
                TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
                TARDISTIPSData pos = tintpos.getTIPSData(slot);
                startx = pos.getCentreX();
                resetx = pos.getCentreX();
                startz = pos.getCentreZ();
                resetz = pos.getCentreZ();
            } else {
                int gsl[] = plugin.getLocationUtils().getStartLocation(rs.getTardis_id());
                startx = gsl[0];
                resetx = gsl[1];
                startz = gsl[2];
                resetz = gsl[3];
            }
            starty = (tud.getSchematic().getPermission().equals("redstone")) ? 65 : 64;
            String[] split = rs.getChunk().split(":");
            world = plugin.getServer().getWorld(split[0]);
            own_world = plugin.getConfig().getBoolean("creation.create_worlds");
            // wall/floor block prefs
            String wall[] = tud.getWall().split(":");
            String floor[] = tud.getFloor().split(":");
            wall_type = Material.valueOf(wall[0]);
            floor_type = Material.valueOf(floor[0]);
            wall_data = TARDISNumberParsers.parseByte(wall[1]);
            floor_data = TARDISNumberParsers.parseByte(floor[1]);
            // siege wall/floor block prefs
            String siege_wall[] = tud.getSiegeWall().split(":");
            String siege_floor[] = tud.getSiegeFloor().split(":");
            siege_wall_type = Material.valueOf(siege_wall[0]);
            siege_floor_type = Material.valueOf(siege_floor[0]);
            siege_wall_data = TARDISNumberParsers.parseByte(siege_wall[1]);
            siege_floor_data = TARDISNumberParsers.parseByte(siege_floor[1]);
            // get input array
            arr = (JSONArray) obj.get("input");
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
                // if we're setting the biome to sky, do it now
                if (plugin.getConfig().getBoolean("creation.sky_biome") && level == 0) {
                    world.setBiome(x, z, Biome.SKY);
                }
                Block bb = world.getBlockAt(x, y, z);
                Material compare_type = bb.getType();
                byte compare_data = bb.getData();
                Material to_wall_type = (toSiege) ? wall_type : siege_wall_type;
                byte to_wall_data = (toSiege) ? wall_data : siege_wall_data;
                Material to_floor_type = (toSiege) ? floor_type : siege_floor_type;
                byte to_floor_data = (toSiege) ? floor_data : siege_floor_data;
                Material type;
                byte data;
                if (compare_type.equals(to_wall_type) && compare_data == to_wall_data) {
                    type = (toSiege) ? siege_wall_type : wall_type;
                    data = (toSiege) ? siege_wall_data : wall_data;
                    TARDISBlockSetters.setBlock(world, x, y, z, type, data);
                }
                if (compare_type.equals(to_floor_type) && compare_data == to_floor_data) {
                    type = (toSiege) ? siege_floor_type : floor_type;
                    data = (toSiege) ? siege_floor_data : floor_data;
                    TARDISBlockSetters.setBlock(world, x, y, z, type, data);
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
