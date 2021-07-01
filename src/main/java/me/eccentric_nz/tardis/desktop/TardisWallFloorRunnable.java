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
package me.eccentric_nz.tardis.desktop;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.eccentric_nz.tardis.TardisConstants;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.api.event.TardisDesktopThemeEvent;
import me.eccentric_nz.tardis.builders.TardisInteriorPositioning;
import me.eccentric_nz.tardis.builders.TardisTipsData;
import me.eccentric_nz.tardis.custommodeldata.TardisMushroomBlockData;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.schematic.TardisSchematicGZip;
import me.eccentric_nz.tardis.utility.TardisBlockSetters;
import me.eccentric_nz.tardis.utility.TardisStaticLocationGetters;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

/**
 * There was also a safety mechanism for when TARDIS rooms were deleted, automatically relocating any living beings in
 * the deleted room, depositing them in the control room.
 *
 * @author eccentric_nz
 */
public class TardisWallFloorRunnable extends TardisThemeRunnable {

    private final TardisPlugin plugin;
    private final UUID uuid;
    private final TardisUpgradeData tud;
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
    private JsonArray arr;
    private Material wall_type;
    private Material floor_type;
    private Player player;

    public TardisWallFloorRunnable(TardisPlugin plugin, UUID uuid, TardisUpgradeData tud) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.tud = tud;
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
            JsonObject obj = TardisSchematicGZip.unzip(path);
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
            Tardis tardis = rs.getTardis();
            int slot = tardis.getTips();
            if (slot != -1) { // default world - use TIPS
                TardisInteriorPositioning tintpos = new TardisInteriorPositioning(plugin);
                TardisTipsData pos = tintpos.getTipsData(slot);
                startx = pos.getCentreX();
                startz = pos.getCentreZ();
            } else {
                int[] gsl = plugin.getLocationUtils().getStartLocation(tardis.getTardisId());
                startx = gsl[0];
                startz = gsl[2];
            }
            starty = TardisConstants.HIGHER.contains(tud.getSchematic().getPermission()) ? 65 : 64;
            world = TardisStaticLocationGetters.getWorld(tardis.getChunk());
            // wall/floor block prefs
            wall_type = Material.valueOf(tud.getWall());
            floor_type = Material.valueOf(tud.getFloor());
            // get input array
            arr = obj.get("input").getAsJsonArray();
            // set running
            running = true;
            player = plugin.getServer().getPlayer(uuid);
            plugin.getPluginManager().callEvent(new TardisDesktopThemeEvent(player, tardis, tud));
            // remove upgrade data
            plugin.getTrackerKeeper().getUpgrades().remove(uuid);
        }
        if (level == (h - 1) && row == (w - 1)) {
            // we're finished cancel the task
            plugin.getServer().getScheduler().cancelTask(taskID);
            taskID = 0;
            TardisMessage.send(player, "UPGRADE_FINISHED");
        } else {
            JsonArray floor = arr.get(level).getAsJsonArray();
            JsonArray r = (JsonArray) floor.get(row);
            // place a row of blocks
            for (int col = 0; col < c; col++) {
                JsonObject bb = r.get(col).getAsJsonObject();
                int x = startx + row;
                int y = starty + level;
                int z = startz + col;
                BlockData data = plugin.getServer().createBlockData(bb.get("data").getAsString());
                Material type = data.getMaterial();
                if (type.equals(Material.ORANGE_WOOL)) {
                    if (wall_type == Material.ORANGE_WOOL) {
                        data = plugin.getServer().createBlockData(TardisMushroomBlockData.MUSHROOM_STEM_DATA.get(46));
                    } else {
                        data = wall_type.createBlockData();
                    }
                    TardisBlockSetters.setBlock(world, x, y, z, data);
                }
                if (type.equals(Material.LIGHT_GRAY_WOOL)) {
                    type = floor_type;
                    TardisBlockSetters.setBlock(world, x, y, z, type);
                }
                if (type.equals(Material.BLUE_WOOL)) {
                    data = plugin.getServer().createBlockData(TardisMushroomBlockData.MUSHROOM_STEM_DATA.get(54));
                    TardisBlockSetters.setBlock(world, x, y, z, data);
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
