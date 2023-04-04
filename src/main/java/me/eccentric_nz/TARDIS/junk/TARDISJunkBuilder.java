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
package me.eccentric_nz.TARDIS.junk;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Player;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;

/**
 * @author eccentric_nz
 */
public class TARDISJunkBuilder implements Runnable {

    private final TARDIS plugin;
    private final BuildData bd;
    private final int sx, sy, sz;
    private final Location loc;
    private final Location effectsLoc;
    private final World world;
    private int task;
    private int fryTask;
    private int i = 0;

    public TARDISJunkBuilder(TARDIS plugin, BuildData bd) {
        this.plugin = plugin;
        this.bd = bd;
        loc = this.bd.getLocation();
        effectsLoc = loc.clone().add(0.5d, 0, 0.5d);
        sx = loc.getBlockX() - 3;
        sy = loc.getBlockY();
        sz = loc.getBlockZ() - 2;
        world = loc.getWorld();
    }

    @Override
    public void run() {
        if (!plugin.getTrackerKeeper().getDematerialising().contains(bd.getTardisID())) {
            // get relative locations
            if (i < 24) {
                i++;
                if (i == 2) {
                    plugin.getUtils().getJunkTravellers(loc).forEach((e) -> {
                        if (e instanceof Player p) {
                            TARDISSounds.playTARDISSound(p, "junk_land", 5L);
                        }
                    });
                    fryTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TARDISJunkItsDangerousRunnable(plugin, loc), 0, 1L);
                }
                if (i == 1) {
                    // get wall and floor prefs
                    Material floor_type;
                    Material wall_type;
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, "00000000-aaaa-bbbb-cccc-000000000000");
                    if (rsp.resultSet()) {
                        floor_type = Material.valueOf(rsp.getFloor());
                        wall_type = Material.valueOf(rsp.getWall());
                    } else {
                        floor_type = Material.LIGHT_GRAY_WOOL;
                        wall_type = Material.ORANGE_WOOL;
                    }
                    // build TARDIS and remember BLOCKS
                    Material type;
                    BlockData data;
                    Block postTerminalBlock = null;
                    // get JSON
                    JsonObject obj = TARDISSchematicGZip.getObject(plugin, "consoles", "junk", false);
                    if (obj != null) {
                        // get dimensions
                        JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
                        int h = dimensions.get("height").getAsInt();
                        int w = dimensions.get("width").getAsInt();
                        int l = dimensions.get("length").getAsInt();
                        // get input array
                        JsonArray arr = obj.get("input").getAsJsonArray();
                        // loop like crazy
                        for (int level = 0; level < h; level++) {
                            JsonArray floor = arr.get(level).getAsJsonArray();
                            for (int row = 0; row < w; row++) {
                                JsonArray r = (JsonArray) floor.get(row);
                                for (int col = 0; col < l; col++) {
                                    JsonObject c = r.get(col).getAsJsonObject();
                                    int x = sx + row;
                                    int y = sy + level;
                                    int z = sz + col;
                                    data = plugin.getServer().createBlockData(c.get("data").getAsString());
                                    type = data.getMaterial();
                                    if (type.equals(Material.CAKE)) {
                                        /*
                                     * This block will be converted to a lever
                                     * by setBlockAndRemember(), but remember it
                                     * so we can use it as the handbrake!
                                         */
                                        String handbrakeloc = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                                        plugin.getQueryFactory().insertSyncControl(bd.getTardisID(), 0, handbrakeloc, 0);
                                    }
                                    if (type.equals(Material.STONE_BUTTON)) {
                                        // remember location 1
                                        String stone_button = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                                        plugin.getQueryFactory().insertSyncControl(bd.getTardisID(), 1, stone_button, 0);
                                    }
                                    if (type.equals(Material.OAK_BUTTON)) {
                                        // remember location 6
                                        String wood_button = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                                        plugin.getQueryFactory().insertSyncControl(bd.getTardisID(), 6, wood_button, 0);
                                    }
                                    if (type.equals(Material.REPEATER)) {
                                        // remember location 3
                                        String repeater = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                                        plugin.getQueryFactory().insertSyncControl(bd.getTardisID(), 2, repeater, 0);
                                    }
                                    if (type.equals(Material.COMPARATOR)) {
                                        // remember location 2
                                        String comparator = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                                        plugin.getQueryFactory().insertSyncControl(bd.getTardisID(), 3, comparator, 0);
                                    }
                                    if (TARDISMaterials.infested.contains(type)) {
                                        // insert / update control 9
                                        plugin.getQueryFactory().insertSyncControl(bd.getTardisID(), 9, (new Location(world, x, y, z)).toString(), 0);
                                        // remember block
                                        postTerminalBlock = world.getBlockAt(x, y, z);
                                    }
                                    if (type.equals(Material.TRIPWIRE_HOOK)) {
                                        // remember location 4
                                        String trip = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                                        plugin.getQueryFactory().insertSyncControl(bd.getTardisID(), 4, trip, 0);
                                    }
                                    switch (type) {
                                        case SPONGE, AIR -> TARDISBlockSetters.setBlock(world, x, y, z, Material.AIR);
                                        case CAKE -> {
                                            BlockData handbrake = Material.LEVER.createBlockData();
                                            Switch lever = (Switch) handbrake;
                                            lever.setAttachedFace(FaceAttachable.AttachedFace.FLOOR);
                                            lever.setFacing(BlockFace.SOUTH);
                                            TARDISBlockSetters.setBlockAndRemember(world, x, y, z, lever, bd.getTardisID());
                                        }
                                        case ORANGE_WOOL -> {
                                            BlockData stem;
                                            if (wall_type.equals(Material.ORANGE_WOOL)) {
                                                stem = TARDISConstants.BARRIER;
                                                TARDISDisplayItemUtils.set(TARDISDisplayItem.HEXAGON, world, x, y, z);
                                            } else {
                                                stem = wall_type.createBlockData();
                                            }
                                            TARDISBlockSetters.setBlockAndRemember(world, x, y, z, stem, bd.getTardisID());
                                        }
                                        case LIGHT_GRAY_WOOL -> TARDISBlockSetters.setBlockAndRemember(world, x, y, z, floor_type, bd.getTardisID());
                                        default -> TARDISBlockSetters.setBlockAndRemember(world, x, y, z, data, bd.getTardisID());
                                    }
                                }
                            }
                        }
                        if (postTerminalBlock != null) {
                            Directional sign = (Directional) Material.OAK_WALL_SIGN.createBlockData();
                            sign.setFacing(BlockFace.EAST);
                            postTerminalBlock.setBlockData(sign);
                            if (Tag.WALL_SIGNS.isTagged(postTerminalBlock.getType())) {
                                Sign ts = (Sign) postTerminalBlock.getState();
                                ts.setLine(0, plugin.getSigns().getStringList("junk").get(0));
                                ts.update();
                            }
                        }
                    }
                } else if (plugin.getConfig().getBoolean("junk.particles")) {
                    // just animate particles
                    plugin.getUtils().getJunkTravellers(loc).forEach((e) -> {
                        if (e instanceof Player p) {
                            TARDISParticles.sendVortexParticles(effectsLoc, p);
                        }
                    });
                }
            } else {
                plugin.getTrackerKeeper().getMaterialising().remove(bd.getTardisID());
                plugin.getTrackerKeeper().getInVortex().remove(bd.getTardisID());
                plugin.getServer().getScheduler().cancelTask(fryTask);
                plugin.getServer().getScheduler().cancelTask(task);
                task = 0;
                if (plugin.getConfig().getLong("junk.return") > 0) {
                    plugin.getGeneralKeeper().setJunkTime(System.currentTimeMillis());
                }
                plugin.getGeneralKeeper().setJunkTravelling(false);
                plugin.getGeneralKeeper().getJunkTravellers().clear();
                // update current location
                HashMap<String, Object> where = new HashMap<>();
                where.put("tardis_id", bd.getTardisID());
                HashMap<String, Object> set = new HashMap<>();
                set.put("world", loc.getWorld().getName());
                set.put("x", loc.getBlockX());
                set.put("y", sy);
                set.put("z", loc.getBlockZ());
                plugin.getQueryFactory().doUpdate("current", set, where);
                plugin.getGeneralKeeper().setJunkTime(System.currentTimeMillis());
            }
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
