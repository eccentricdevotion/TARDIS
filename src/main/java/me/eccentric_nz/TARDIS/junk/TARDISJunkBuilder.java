/*
 * Copyright (C) 2015 eccentric_nz
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

import java.io.File;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISMaterialisationData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISEffectLibHelper;
import me.eccentric_nz.TARDIS.utility.TARDISLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISJunkBuilder implements Runnable {

    private final TARDIS plugin;
    private final TARDISMaterialisationData tmd;
    public int task;
    public int fryTask;
    private int i = 0;
    private final int sx, sy, sz;
    private final Location loc;
    private final Location effectsLoc;
    World world;
    Biome biome;
    private final QueryFactory qf;

    public TARDISJunkBuilder(TARDIS plugin, TARDISMaterialisationData tmd) {
        this.plugin = plugin;
        this.tmd = tmd;
        this.loc = this.tmd.getLocation();
        this.effectsLoc = this.loc.clone().add(0.5d, 0, 0.5d);
        this.sx = this.loc.getBlockX() - 3;
        this.sy = this.loc.getBlockY();
        this.sz = this.loc.getBlockZ() - 2;
        this.world = this.loc.getWorld();
        this.biome = this.tmd.getBiome();
        this.qf = new QueryFactory(this.plugin);
    }

    @Override
    public void run() {
        if (!plugin.getTrackerKeeper().getDematerialising().contains(tmd.getTardisID())) {
            // get relative locations
            if (i < 24) {
                i++;
                if (i == 2) {
                    for (Entity e : getJunkTravellers()) {
                        if (e instanceof Player) {
                            Player p = (Player) e;
                            TARDISSounds.playTARDISSound(loc, p, "junk_land");
                        }
                    }
                    fryTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TARDISJunkItsDangerousRunnable(plugin, loc), 0, 1L);
                }
                if (i == 1) {
                    // build TARDIS and remember blocks
                    Material type;
                    byte data;
                    Block postTerminalBlock = null;
                    // get JSON
                    String path = plugin.getDataFolder() + File.separator + "schematics" + File.separator + "junk.tschm";
                    JSONObject obj = TARDISSchematicGZip.unzip(path);
                    // get dimensions
                    JSONObject dimensions = (JSONObject) obj.get("dimensions");
                    int h = dimensions.getInt("height");
                    int w = dimensions.getInt("width");
                    int l = dimensions.getInt("length");
                    // get input array
                    JSONArray arr = (JSONArray) obj.get("input");
                    // loop like crazy
                    for (int level = 0; level < h; level++) {
                        JSONArray floor = (JSONArray) arr.get(level);
                        for (int row = 0; row < w; row++) {
                            JSONArray r = (JSONArray) floor.get(row);
                            for (int col = 0; col < l; col++) {
                                JSONObject c = (JSONObject) r.get(col);
                                int x = sx + row;
                                int y = sy + level;
                                int z = sz + col;
                                // if we're setting the biome to sky, do it now
                                if (plugin.getConfig().getBoolean("creation.sky_biome") && level == 0) {
                                    world.setBiome(x, z, Biome.SKY);
                                }
                                type = Material.valueOf((String) c.get("type"));
                                data = c.getByte("data");
                                if (type.equals(Material.CAKE_BLOCK)) {
                                    /*
                                     * This block will be converted to a lever
                                     * by setBlockAndRemember(), but remember it
                                     * so we can use it as the handbrake!
                                     */
                                    String handbrakeloc = TARDISLocationGetters.makeLocationStr(world, x, y, z);
                                    qf.insertSyncControl(tmd.getTardisID(), 0, handbrakeloc, 0);
                                }
                                if (type.equals(Material.STONE_BUTTON)) {
                                    // remember location 1
                                    String stone_button = TARDISLocationGetters.makeLocationStr(world, x, y, z);
                                    qf.insertSyncControl(tmd.getTardisID(), 1, stone_button, 0);
                                }
                                if (type.equals(Material.WOOD_BUTTON)) {
                                    // remember location 6
                                    String wood_button = TARDISLocationGetters.makeLocationStr(world, x, y, z);
                                    qf.insertSyncControl(tmd.getTardisID(), 6, wood_button, 0);
                                }
                                if (type.equals(Material.DIODE_BLOCK_OFF)) {
                                    // remember location 3
                                    String repeater = TARDISLocationGetters.makeLocationStr(world, x, y, z);
                                    qf.insertSyncControl(tmd.getTardisID(), 2, repeater, 0);
                                }
                                if (type.equals(Material.REDSTONE_COMPARATOR_OFF)) {
                                    // remember location 2
                                    String comparator = TARDISLocationGetters.makeLocationStr(world, x, y, z);
                                    qf.insertSyncControl(tmd.getTardisID(), 3, comparator, 0);
                                }
                                if (type.equals(Material.MONSTER_EGGS)) {
                                    // insert / update control 9
                                    qf.insertSyncControl(tmd.getTardisID(), 9, (new Location(world, x, y, z)).toString(), 0);
                                    // remember block
                                    postTerminalBlock = world.getBlockAt(x, y, z);
                                }
                                if (type.equals(Material.TRIPWIRE_HOOK)) {
                                    // remember location 4
                                    String trip = TARDISLocationGetters.makeLocationStr(world, x, y, z);
                                    qf.insertSyncControl(tmd.getTardisID(), 4, trip, 0);
                                }
                                if (type.equals(Material.SPONGE) || type.equals(Material.AIR)) {
                                    TARDISBlockSetters.setBlock(world, x, y, z, Material.AIR, data);
                                } else if (type.equals(Material.CAKE_BLOCK)) {
                                    TARDISBlockSetters.setBlock(world, x, y, z, type, data);
                                } else {
                                    plugin.getBlockUtils().setBlockAndRemember(world, x, y, z, type, data, tmd.getTardisID());
                                }
                            }
                        }
                    }
                    if (postTerminalBlock != null) {
                        postTerminalBlock.setType(Material.WALL_SIGN);
                        postTerminalBlock.setData((byte) 5, true);
                        if (postTerminalBlock.getType().equals(Material.WALL_SIGN)) {
                            Sign ts = (Sign) postTerminalBlock.getState();
                            ts.setLine(0, plugin.getSigns().getStringList("junk").get(0));
                            ts.update();
                        }
                    }
                } else if (plugin.getConfig().getBoolean("junk.particles") && plugin.isEffectLibOnServer()) {
                    // just animate particles
                    TARDISEffectLibHelper.sendVortexParticles(effectsLoc);
                }
            } else {
                plugin.getTrackerKeeper().getMaterialising().remove(Integer.valueOf(tmd.getTardisID()));
                plugin.getTrackerKeeper().getInVortex().remove(Integer.valueOf(tmd.getTardisID()));
                plugin.getServer().getScheduler().cancelTask(fryTask);
                plugin.getServer().getScheduler().cancelTask(task);
                task = 0;
                if (plugin.getConfig().getLong("junk.return") > 0) {
                    plugin.getGeneralKeeper().setJunkTime(System.currentTimeMillis());
                }
                plugin.getGeneralKeeper().setJunkTravelling(false);
                plugin.getGeneralKeeper().getJunkTravellers().clear();
                // update current location
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("tardis_id", tmd.getTardisID());
                HashMap<String, Object> set = new HashMap<String, Object>();
                set.put("world", loc.getWorld().getName());
                set.put("x", loc.getBlockX());
                set.put("y", sy);
                set.put("z", loc.getBlockZ());
                qf.doUpdate("current", set, where);
            }
        }
    }

    private List<Entity> getJunkTravellers() {
        // spawn an entity
        Entity orb = loc.getWorld().spawnEntity(loc, EntityType.EXPERIENCE_ORB);
        List<Entity> ents = orb.getNearbyEntities(16.0d, 16.0d, 16.0d);
        orb.remove();
        return ents;
    }

    public void setTask(int task) {
        this.task = task;
    }
}
