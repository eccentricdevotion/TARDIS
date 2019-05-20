/*
 * Copyright (C) 2019 eccentric_nz
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

import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Archive;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.desktop.TARDISUpgradeData;
import me.eccentric_nz.TARDIS.schematic.ResultSetArchive;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

/**
 * There was also a safety mechanism for when TARDIS rooms were deleted, automatically relocating any living beings in
 * the deleted room, depositing them in the control room.
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
    private final QueryFactory qf;
    private Player player;
    private int taskID;
    private Archive archive;

    TARDISSiegeWallFloorRunnable(TARDIS plugin, UUID uuid, TARDISUpgradeData tud, boolean toSiege) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.tud = tud;
        this.toSiege = toSiege;
        qf = new QueryFactory(this.plugin);
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
                    plugin.getServer().getScheduler().cancelTask(taskID);
                    return;
                }
            }
            JSONObject obj;
            if (archive == null) {
                String directory = (tud.getSchematic().isCustom()) ? "user_schematics" : "schematics";
                String path = plugin.getDataFolder() + File.separator + directory + File.separator + tud.getSchematic().getPermission() + ".tschm";
                File file = new File(path);
                if (!file.exists()) {
                    plugin.debug("Could not find a schematic with that name!");
                    // cancel task
                    plugin.getServer().getScheduler().cancelTask(taskID);
                    return;
                }
                // get JSON
                obj = TARDISSchematicGZip.unzip(path);
            } else {
                obj = archive.getJSON();
            }
            // get dimensions
            JSONObject dimensions = (JSONObject) obj.get("dimensions");
            h = dimensions.getInt("height");
            w = dimensions.getInt("width");
            c = dimensions.getInt("length");
            // calculate startx, starty, startz
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("uuid", uuid.toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 0);
            if (!rs.resultSet()) {
                // abort and return energy
                HashMap<String, Object> wherea = new HashMap<>();
                wherea.put("uuid", uuid.toString());
                int amount = plugin.getArtronConfig().getInt("upgrades." + tud.getSchematic().getPermission());
                qf.alterEnergyLevel("tardis", amount, wherea, player);
            }
            Tardis tardis = rs.getTardis();
            int slot = tardis.getTIPS();
            int id = tardis.getTardis_id();
            if (slot != -1) { // default world - use TIPS
                TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
                TARDISTIPSData pos = tintpos.getTIPSData(slot);
                startx = pos.getCentreX();
                startz = pos.getCentreZ();
            } else {
                int gsl[] = plugin.getLocationUtils().getStartLocation(id);
                startx = gsl[0];
                startz = gsl[2];
            }
            starty = (tud.getSchematic().getPermission().equals("redstone")) ? 65 : 64;
            String[] split = tardis.getChunk().split(":");
            String w = (split[0].equals("TARDIS_TimeVortex") ? "tardis_time_vortex" : split[0].toLowerCase(Locale.ENGLISH));
            world = plugin.getServer().getWorld(w);
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
                // if we're setting the biome to sky, do it now
                if (plugin.getConfig().getBoolean("creation.sky_biome") && level == 0) {
                    world.setBiome(x, z, Biome.THE_VOID);
                }
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
