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
package me.eccentric_nz.TARDIS.desktop;

import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISDesktopThemeEvent;
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
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
public class TARDISWallFloorRunnable extends TARDISThemeRunnable {

    private final TARDIS plugin;
    private final UUID uuid;
    private final TARDISUpgradeData tud;
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
    private JSONArray arr;
    private Material wall_type;
    private Material floor_type;
    private final QueryFactory qf;
    private Player player;

    public TARDISWallFloorRunnable(TARDIS plugin, UUID uuid, TARDISUpgradeData tud) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.tud = tud;
        qf = new QueryFactory(this.plugin);
    }

    @Override

    public void run() {
        // initialise
        if (!running) {
            String directory = (tud.getSchematic().isCustom()) ? "user_schematics" : "schematics";
            String path = plugin.getDataFolder() + File.separator + directory + File.separator + tud.getSchematic().getPermission() + ".tschm";
            File file = new File(path);
            if (!file.exists()) {
                plugin.debug("Could not find a schematic with that name!");
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
            if (slot != -1) { // default world - use TIPS
                TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
                TARDISTIPSData pos = tintpos.getTIPSData(slot);
                startx = pos.getCentreX();
                startz = pos.getCentreZ();
            } else {
                int gsl[] = plugin.getLocationUtils().getStartLocation(tardis.getTardis_id());
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
            // get input array
            arr = (JSONArray) obj.get("input");
            // set running
            running = true;
            player = plugin.getServer().getPlayer(uuid);
            plugin.getPM().callEvent(new TARDISDesktopThemeEvent(player, tardis, tud));
            // remove upgrade data
            plugin.getTrackerKeeper().getUpgrades().remove(uuid);
        }
        if (level == (h - 1) && row == (w - 1)) {
            // we're finished cancel the task
            plugin.getServer().getScheduler().cancelTask(taskID);
            taskID = 0;
            TARDISMessage.send(player, "UPGRADE_FINISHED");
        } else {
            JSONArray floor = (JSONArray) arr.get(level);
            JSONArray r = (JSONArray) floor.get(row);
            // place a row of blocks
            for (int col = 0; col < c; col++) {
                JSONObject bb = (JSONObject) r.get(col);
                int x = startx + row;
                int y = starty + level;
                int z = startz + col;
                // if we're setting the biome to sky, do it now
                if (plugin.getConfig().getBoolean("creation.sky_biome") && level == 0) {
                    world.setBiome(x, z, Biome.THE_VOID);
                }
                BlockData data = plugin.getServer().createBlockData(bb.getString("data"));
                Material type = data.getMaterial();
                if (type.equals(Material.ORANGE_WOOL)) {
                    type = wall_type;
                    TARDISBlockSetters.setBlock(world, x, y, z, type);
                }
                if (type.equals(Material.LIGHT_GRAY_WOOL)) {
                    type = floor_type;
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
}
