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

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.VortexEffect;
import de.slikey.effectlib.util.ParticleEffect;
import java.io.File;
import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISMaterialisationData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISLocationGetters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

/**
 *
 * @author eccentric_nz
 */
public class TARDISJunkBuilder implements Runnable {

    private final TARDIS plugin;
    private final TARDISMaterialisationData tmd;
    public int task;
    private int i = 0;
    private final int sx, sy, sz;
    private final Location loc;
    private final Location mloc;
    World world;
    Biome biome;
    private final EffectManager effectManager;
    private final QueryFactory qf;

    public TARDISJunkBuilder(TARDIS plugin, TARDISMaterialisationData tmd) {
        this.plugin = plugin;
        this.tmd = tmd;
        this.loc = this.tmd.getLocation();
        this.mloc = this.loc.clone().add(0.0d, 0.05d, 0.0d);
        this.sx = this.loc.getBlockX() - 3;
        this.sy = this.loc.getBlockY();
        this.sz = this.loc.getBlockZ() - 2;
        this.world = this.loc.getWorld();
        this.biome = this.tmd.getBiome();
        this.effectManager = new EffectManager(this.plugin);
        this.qf = new QueryFactory(this.plugin);
    }

    @Override
    public void run() {
        if (!plugin.getTrackerKeeper().getDematerialising().contains(tmd.getTardisID())) {
            // get relative locations
            if (i < 10) {
                i++;
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
                                     * This block will be converted to a lever by
                                     * setBlock(), but remember it so we can use it as the handbrake!
                                     */
                                    String handbrakeloc = TARDISLocationGetters.makeLocationStr(world, x, y, z);
                                    qf.insertSyncControl(tmd.getTardisID(), 0, handbrakeloc, 0);
                                }
                                if (type.equals(Material.MONSTER_EGGS)) {
                                    // insert / update control
                                    qf.insertSyncControl(tmd.getTardisID(), 9, (new Location(world, x, y, z)).toString(), 0);
                                    // remember block
                                    postTerminalBlock = world.getBlockAt(x, y, z);
                                }
                                if (type.equals(Material.SPONGE)) {
                                    TARDISBlockSetters.setBlock(world, x, y, z, Material.AIR, data);
                                } else {
                                    TARDISBlockSetters.setBlock(world, x, y, z, type, data);
                                }
                            }
                        }
                    }
                    if (postTerminalBlock != null) {
                        postTerminalBlock.setType(Material.WALL_SIGN);
                        postTerminalBlock.setData((byte) 5, true);
                        if (postTerminalBlock.getType().equals(Material.WALL_SIGN)) {
                            Sign ts = (Sign) postTerminalBlock.getState();
                            ts.setLine(0, "");
                            ts.setLine(1, plugin.getSigns().getStringList("terminal").get(0));
                            ts.setLine(2, plugin.getSigns().getStringList("terminal").get(1));
                            ts.setLine(3, "");
                            ts.update();
                        }
                    }
                } else {
                    if (plugin.getConfig().getBoolean("junk.particles") && plugin.getPM().isPluginEnabled("EffectLib")) {
                        // just animate particles
                        VortexEffect vortexEffect = new VortexEffect(effectManager);
                        vortexEffect.particle = ParticleEffect.SPELL;
                        vortexEffect.radius = 3;
                        vortexEffect.circles = 10;
                        vortexEffect.helixes = 10;
                        vortexEffect.setLocation(loc);
                        vortexEffect.setTarget(mloc);
                        //vortexEffect.iterations = 5 * 20;
                        vortexEffect.start();
                    }
                }
            } else {
                plugin.getTrackerKeeper().getMaterialising().remove(Integer.valueOf(tmd.getTardisID()));
                plugin.getTrackerKeeper().getInVortex().remove(Integer.valueOf(tmd.getTardisID()));
                plugin.getServer().getScheduler().cancelTask(task);
                task = 0;
            }
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
