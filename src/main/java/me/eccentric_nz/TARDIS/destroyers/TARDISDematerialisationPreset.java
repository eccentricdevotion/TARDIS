/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.destroyers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.TARDISConstants.COMPASS;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonColumn;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
//import org.getspout.spoutapi.SpoutManager;

/**
 * A dematerialisation circuit was an essential part of a Type 40 TARDIS which
 * enabled it to dematerialise from normal space into the Time Vortex and
 * rematerialise back from it.
 *
 * @author eccentric_nz
 */
public class TARDISDematerialisationPreset implements Runnable {

    private final TARDIS plugin;
    private final COMPASS d;
    private final int loops;
    private final Location location;
    private final TARDISConstants.PRESET preset;
    private final int tid;
    public int task;
    private int i;
    private final int lamp;
    private final int cham_id;
    private final byte cham_data;
    private final TARDISChameleonColumn column;
    private final TARDISChameleonColumn ice_column;
    private final TARDISChameleonColumn glass_column;
    private final List<BlockFace> faces = new ArrayList<BlockFace>();
    private byte the_colour;

    /**
     * Runnable method to materialise the TARDIS Police Box. Tries to mimic the
     * transparency of materialisation by building the Police Box first with
     * GLASS, then ICE, then the normall wall block (BLUE WOOL or the chameleon
     * material).
     *
     * @param plugin instance of the TARDIS plugin
     * @param location the location to build the Police Box at
     * @param preset the Chameleon preset currently in use by the TARDIS
     * @param lamp the id of the lamp block
     * @param tid the tardis_id this Police Box belongs to
     * @param d the COMPASS direction the Police Box is facing
     * @param cham_id the chameleon block id for the police box
     * @param cham_data the chameleon block data for the police box
     */
    public TARDISDematerialisationPreset(TARDIS plugin, Location location, TARDISConstants.PRESET preset, int lamp, int tid, COMPASS d, int cham_id, byte cham_data) {
        this.plugin = plugin;
        this.d = d;
        this.loops = 12;
        this.location = location;
        this.preset = preset;
        this.i = 0;
        this.tid = tid;
        this.lamp = lamp;
        this.cham_id = cham_id;
        this.cham_data = cham_data;
        column = plugin.presets.getColumn(preset, d);
        ice_column = plugin.presets.getIce(preset, d);
        glass_column = plugin.presets.getGlass(preset, d);
        this.faces.add(BlockFace.NORTH);
        this.faces.add(BlockFace.SOUTH);
        this.faces.add(BlockFace.EAST);
        this.faces.add(BlockFace.WEST);
    }

    @Override
    public void run() {
        int[][] ids;
        byte[][] datas;
        // get relative locations
        int x = location.getBlockX(), plusx = location.getBlockX() + 1, minusx = location.getBlockX() - 1;
        int y;
        plugin.isPresetMaterialising.add("tid" + tid);
        if (preset.equals(TARDISConstants.PRESET.SUBMERGED)) {
            y = location.getBlockY() - 1;
        } else {
            y = location.getBlockY();
        }
        int z = location.getBlockZ(), plusz = location.getBlockZ() + 1, minusz = location.getBlockZ() - 1;
        World world = location.getWorld();
        if (i < loops) {
            i++;
            // expand placed blocks to a police box
            switch (i % 3) {
                case 2: // ice
                    ids = ice_column.getId();
                    datas = ice_column.getData();
                    break;
                case 1: // glass
                    ids = glass_column.getId();
                    datas = glass_column.getData();
                    break;
                default: // preset
                    ids = column.getId();
                    datas = column.getData();
                    break;
            }
            // first run - play sound
            if (i == 1) {
//                if (plugin.pm.getPlugin("Spout") != null && SpoutManager.getPlayer(player).isSpoutCraftEnabled()) {
//                    SpoutManager.getSoundManager().playGlobalCustomSoundEffect(plugin, "https://dl.dropboxusercontent.com/u/53758864/tardis_takeoff.mp3", false, location, 9, 75);
//                } else {
                try {
                    Class.forName("org.bukkit.Sound");
                    world.playSound(location, Sound.MINECART_INSIDE, 1, 0);
                } catch (ClassNotFoundException e) {
                    world.playEffect(location, Effect.BLAZE_SHOOT, 0);
                }
//                }
                the_colour = getWoolColour(tid, preset);
            } else {
                // just change the walls
                int xx, zz;
                for (int n = 0; n < 9; n++) {
                    int[] colids = ids[n];
                    byte[] coldatas = datas[n];
                    switch (n) {
                        case 0:
                            xx = minusx;
                            zz = minusz;
                            break;
                        case 1:
                            xx = x;
                            zz = minusz;
                            break;
                        case 2:
                            xx = plusx;
                            zz = minusz;
                            break;
                        case 3:
                            xx = plusx;
                            zz = z;
                            break;
                        case 4:
                            xx = plusx;
                            zz = plusz;
                            break;
                        case 5:
                            xx = x;
                            zz = plusz;
                            break;
                        case 6:
                            xx = minusx;
                            zz = plusz;
                            break;
                        case 7:
                            xx = minusx;
                            zz = z;
                            break;
                        default:
                            xx = x;
                            zz = z;
                            break;
                    }
                    for (int yy = 0; yy < 4; yy++) {
                        switch (colids[yy]) {
                            case 2:
                            case 3:
                                int subi = (preset.equals(TARDISConstants.PRESET.SUBMERGED)) ? cham_id : colids[yy];
                                byte subd = (preset.equals(TARDISConstants.PRESET.SUBMERGED)) ? cham_data : coldatas[yy];
                                plugin.utils.setBlock(world, xx, (y + yy), zz, subi, subd);
                                break;
                            case 35: // wool
                                int chai = (preset.equals(TARDISConstants.PRESET.NEW) || preset.equals(TARDISConstants.PRESET.OLD)) ? cham_id : colids[yy];
                                byte chad = (preset.equals(TARDISConstants.PRESET.NEW) || preset.equals(TARDISConstants.PRESET.OLD)) ? cham_data : coldatas[yy];
                                if (preset.equals(TARDISConstants.PRESET.PARTY) || (preset.equals(TARDISConstants.PRESET.FLOWER) && coldatas[yy] == 0)) {
                                    chad = the_colour;
                                }
                                plugin.utils.setBlock(world, xx, (y + yy), zz, chai, chad);
                                break;
                            case 50: // lamps, glowstone and torches
                            case 89:
                            case 124:
                                int light = (preset.equals(TARDISConstants.PRESET.NEW) || preset.equals(TARDISConstants.PRESET.OLD)) ? lamp : colids[yy];
                                plugin.utils.setBlock(world, xx, (y + yy), zz, light, coldatas[yy]);
                                break;
                            default: // everything else
                                plugin.utils.setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                                break;
                        }
                    }
                }
            }
        } else {
            new TARDISDeinstaPreset(plugin).instaDestroyPreset(location, d, tid, false, preset);
            plugin.getServer().getScheduler().cancelTask(task);
            task = 0;
        }
    }

    private byte getWoolColour(int id, TARDISConstants.PRESET p) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        where.put("door_type", 0);
        ResultSetDoors rs = new ResultSetDoors(plugin, where, false);
        if (rs.resultSet()) {
            Block b = plugin.utils.getLocationFromDB(rs.getDoor_location(), 0.0F, 0.0F).getBlock();
            if (p.equals(TARDISConstants.PRESET.FLOWER)) {
                return b.getRelative(BlockFace.UP, 3).getData();
            } else {
                for (BlockFace f : faces) {
                    if (b.getRelative(f).getType().equals(Material.WOOL)) {
                        return b.getRelative(f).getData();
                    }
                }
            }
        }
        return (byte) 0;
    }

    public void setTask(int task) {
        this.task = task;
    }
}
