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
package me.eccentric_nz.TARDIS.builders;

import java.util.HashMap;
import java.util.Random;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonColumn;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;

/**
 * A police box is a telephone kiosk that can be used by members of the public
 * wishing to get help from the police. Early in the First Doctor's travels, the
 * TARDIS assumed the exterior shape of a police box during a five-month
 * stopover in 1963 London. Due a malfunction in its chameleon circuit, the
 * TARDIS became locked into that shape.
 *
 * @author eccentric_nz
 */
public class TARDISRebuildPreset {

    private final TARDIS plugin;
    private final TARDISConstants.COMPASS d;
    private final Location location;
    private final int tid;
    private final String p;
    private final int lamp;
    private final boolean sub;
    private final int cham_id;
    private final byte cham_data;
    private Block sponge;
    private final TARDISConstants.PRESET preset;
    private TARDISChameleonColumn column;
    private final byte[] colours;
    private final Random rand;
    private final byte random_colour;

    public TARDISRebuildPreset(TARDIS plugin, Location location, TARDISConstants.PRESET preset, int tid, TARDISConstants.COMPASS d, String p, boolean mal, int lamp, boolean sub, int cham_id, byte cham_data) {
        this.plugin = plugin;
        this.d = d;
        this.location = location;
        this.preset = preset;
        this.tid = tid;
        this.p = p;
        this.lamp = lamp;
        this.sub = sub;
        this.cham_id = cham_id;
        this.cham_data = cham_data;
        colours = new byte[]{0, 1, 2, 3, 4, 5, 6, 9, 10, 11, 12, 13, 14};
        rand = new Random();
        random_colour = colours[rand.nextInt(13)];
    }

    /**
     * Builds the TARDIS Preset.
     */
    public void rebuildPreset() {
        if (preset.equals(TARDISConstants.PRESET.ANGEL)) {
            plugin.presets.setR(rand.nextInt(2));
        }
        column = plugin.presets.getColumn(preset, d);
        int plusx, minusx, x, plusz, y, undery, minusz, z, platform_id = plugin.getConfig().getInt("platform_id");
        byte platform_data = (byte) plugin.getConfig().getInt("platform_data");
        // get relative locations
        x = location.getBlockX();
        plusx = (location.getBlockX() + 1);
        minusx = (location.getBlockX() - 1);
        if (preset.equals(TARDISConstants.PRESET.SUBMERGED)) {
            y = location.getBlockY() - 1;
            undery = (location.getBlockY() - 2);
        } else {
            y = location.getBlockY();
            undery = (location.getBlockY() - 1);
        }
        z = (location.getBlockZ());
        plusz = (location.getBlockZ() + 1);
        minusz = (location.getBlockZ() - 1);
        final World world = location.getWorld();
        int signx = 0, signz = 0;
        // platform
        plugin.buildPB.addPlatform(location, false, d, p, tid);
        QueryFactory qf = new QueryFactory(plugin);
        HashMap<String, Object> ps = new HashMap<String, Object>();
        ps.put("tardis_id", tid);
        String loc = "";
        // get direction player is facing from yaw
        // place block under door if block is in list of blocks an iron door cannot go on
        switch (d) {
            case SOUTH:
                //if (yaw >= 315 || yaw < 45)
                if (sub) {
                    plugin.utils.setBlockCheck(world, x, undery, minusz, 19, (byte) 0, tid, true); // door is here if player facing south
                    sponge = world.getBlockAt(x, undery, minusz);
                } else if (!plugin.buildPB.no_block_under_door.contains(preset)) {
                    plugin.utils.setBlockCheck(world, x, undery, minusz, platform_id, platform_data, tid, false); // door is here if player facing south
                }
                loc = world.getBlockAt(x, undery, minusz).getLocation().toString();
                ps.put("location", loc);
                signx = x;
                signz = (minusz - 1);
                break;
            case EAST:
                //if (yaw >= 225 && yaw < 315)
                if (sub) {
                    plugin.utils.setBlockCheck(world, minusx, undery, z, 19, (byte) 0, tid, true); // door is here if player facing east
                    sponge = world.getBlockAt(minusx, undery, z);
                } else if (!plugin.buildPB.no_block_under_door.contains(preset)) {
                    plugin.utils.setBlockCheck(world, minusx, undery, z, platform_id, platform_data, tid, false); // door is here if player facing east
                }
                loc = world.getBlockAt(minusx, undery, z).getLocation().toString();
                ps.put("location", loc);
                signx = (minusx - 1);
                signz = z;
                break;
            case NORTH:
                //if (yaw >= 135 && yaw < 225)
                if (sub) {
                    plugin.utils.setBlockCheck(world, x, undery, plusz, 19, (byte) 0, tid, true); // door is here if player facing north
                    sponge = world.getBlockAt(x, undery, plusz);
                } else if (!plugin.buildPB.no_block_under_door.contains(preset)) {
                    plugin.utils.setBlockCheck(world, x, undery, plusz, platform_id, platform_data, tid, false); // door is here if player facing north
                }
                loc = world.getBlockAt(x, undery, plusz).getLocation().toString();
                ps.put("location", loc);
                signx = x;
                signz = (plusz + 1);
                break;
            case WEST:
                //if (yaw >= 45 && yaw < 135)
                if (sub) {
                    plugin.utils.setBlockCheck(world, plusx, undery, z, 19, (byte) 0, tid, true); // door is here if player facing west
                    sponge = world.getBlockAt(plusx, undery, z);
                } else if (!plugin.buildPB.no_block_under_door.contains(preset)) {
                    plugin.utils.setBlockCheck(world, plusx, undery, z, platform_id, platform_data, tid, false); // door is here if player facing west
                }
                loc = world.getBlockAt(plusx, undery, z).getLocation().toString();
                ps.put("location", loc);
                signx = (plusx + 1);
                signz = z;
                break;
        }
        ps.put("police_box", 1);
        qf.doInsert("blocks", ps);
        if (!loc.isEmpty()) {
            plugin.protectBlockMap.put(loc, tid);
        }
        int xx, zz;
        int[][] ids = column.getId();
        byte[][] data = column.getData();
        for (int i = 0; i < 10; i++) {
            int[] colids = ids[i];
            byte[] coldatas = data[i];
            switch (i) {
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
                case 8:
                    xx = x;
                    zz = z;
                    break;
                default:
                    xx = signx;
                    zz = signz;
                    break;
            }
            for (int yy = 0; yy < 4; yy++) {
                switch (colids[yy]) {
                    case 2:
                    case 3:
                        int subi = (preset.equals(TARDISConstants.PRESET.SUBMERGED)) ? cham_id : colids[yy];
                        byte subd = (preset.equals(TARDISConstants.PRESET.SUBMERGED)) ? cham_data : coldatas[yy];
                        if (preset.equals(TARDISConstants.PRESET.SUBMERGED) && yy == 0) {
                            plugin.utils.setBlockAndRemember(world, xx, (y + yy), zz, subi, subd, tid);
                        }
                        plugin.utils.setBlock(world, xx, (y + yy), zz, subi, subd);
                        break;
                    case 35: // wool
                        int chai = (preset.equals(TARDISConstants.PRESET.NEW) || preset.equals(TARDISConstants.PRESET.OLD)) ? cham_id : colids[yy];
                        byte chad = (preset.equals(TARDISConstants.PRESET.NEW) || preset.equals(TARDISConstants.PRESET.OLD)) ? cham_data : coldatas[yy];
                        if (preset.equals(TARDISConstants.PRESET.PARTY) || (preset.equals(TARDISConstants.PRESET.FLOWER) && coldatas[yy] == 0)) {
                            chad = random_colour;
                        }
                        plugin.utils.setBlock(world, xx, (y + yy), zz, chai, chad);
                        break;
                    case 50: // lamps, glowstone and torches
                    case 89:
                    case 124:
                        int light = (preset.equals(TARDISConstants.PRESET.NEW) || preset.equals(TARDISConstants.PRESET.OLD)) ? lamp : colids[yy];
                        plugin.utils.setBlock(world, xx, (y + yy), zz, light, coldatas[yy]);
                        break;
                    case 64: // wood, iron & trap doors
                    case 71:
                    case 96:
                        if (coldatas[yy] < 8) {
                            // remember the door location
                            String doorloc = world.getName() + ":" + xx + ":" + (y + yy) + ":" + zz;
                            // should insert the door when tardis is first made, and then update location there after!
                            HashMap<String, Object> whered = new HashMap<String, Object>();
                            whered.put("door_type", 0);
                            whered.put("tardis_id", tid);
                            ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
                            HashMap<String, Object> setd = new HashMap<String, Object>();
                            setd.put("door_location", doorloc);
                            if (rsd.resultSet()) {
                                HashMap<String, Object> whereid = new HashMap<String, Object>();
                                whereid.put("door_id", rsd.getDoor_id());
                                qf.doUpdate("doors", setd, whereid);
                            } else {
                                setd.put("tardis_id", tid);
                                setd.put("door_type", 0);
                                setd.put("door_direction", d.toString());
                                qf.doInsert("doors", setd);
                            }
                        }
                        if (preset.equals(TARDISConstants.PRESET.SUBMERGED) && yy == 0) {
                            plugin.utils.setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], coldatas[yy], tid);
                        } else {
                            plugin.utils.setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                        }
                        break;
                    case 68: // sign - if there is one
                        plugin.utils.setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                        Block sign = world.getBlockAt(xx, (y + yy), zz);
                        if (sign.getType().equals(Material.WALL_SIGN)) {
                            Sign s = (Sign) sign.getState();
                            if (plugin.getConfig().getBoolean("name_tardis")) {
                                HashMap<String, Object> wheret = new HashMap<String, Object>();
                                wheret.put("tardis_id", tid);
                                ResultSetTardis rst = new ResultSetTardis(plugin, wheret, "", false);
                                if (rst.resultSet()) {
                                    String owner = rst.getOwner().substring(0, 12) + "'s";
                                    switch (preset) {
                                        case GRAVESTONE:
                                            s.setLine(3, owner);
                                            break;
                                        case ANGEL:
                                            s.setLine(2, owner);
                                            break;
                                        default:
                                            s.setLine(0, owner);
                                            break;
                                    }
                                }
                            }
                            String line1;
                            String line2;
                            switch (preset) {
                                case STONE:
                                    line1 = "STONE BRICK";
                                    line2 = "COLUMN";
                                    break;
                                case PARTY:
                                    line1 = "PARTY";
                                    line2 = "TENT";
                                    break;
                                case VILLAGE:
                                    line1 = "VILLAGE";
                                    line2 = "HOUSE";
                                    break;
                                case YELLOW:
                                    line1 = "YELLOW";
                                    line2 = "SUBMARINE";
                                    break;
                                case TELEPHONE:
                                    line1 = "TELEPHONE";
                                    line2 = "BOX";
                                    break;
                                case WINDMILL:
                                    line1 = "VERY SMALL";
                                    line2 = "WINDMILL";
                                    break;
                                case SWAMP:
                                    line1 = "SIGN ABOVE";
                                    line2 = "THE DOOR";
                                    break;
                                case ANGEL:
                                    line1 = "WEEPING";
                                    line2 = "ANGELS HAVE";
                                    break;
                                case CAKE:
                                    line1 = "CAKE AND";
                                    line2 = "EAT IT TOO";
                                    break;
                                case TOPSYTURVEY:
                                    line1 = "Topsy-turvey";
                                    line2 = "BOX O' MARVEL";
                                    break;
                                case GRAVESTONE:
                                    line1 = "HERE";
                                    line2 = "LIES";
                                    break;
                                case SHROOM:
                                    line1 = "TRIPPY";
                                    line2 = "SPACE SHROOM";
                                    break;
                                case CUSTOM:
                                    line1 = plugin.presets.custom.getLine_one();
                                    line2 = plugin.presets.custom.getLine_two();
                                    break;
                                default:
                                    line1 = "POLICE";
                                    line2 = "BOX";
                                    break;
                            }
                            if (preset.equals(TARDISConstants.PRESET.ANGEL)) {
                                s.setLine(0, ChatColor.WHITE + line1);
                                s.setLine(1, ChatColor.WHITE + line2);
                                s.setLine(3, ChatColor.WHITE + "TARDIS");
                            } else {
                                s.setLine(1, ChatColor.WHITE + line1);
                                s.setLine(2, ChatColor.WHITE + line2);
                            }
                            s.update();
                        }
                        break;
                    case 144:
                        plugin.utils.setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                        Skull skull = (Skull) world.getBlockAt(xx, (y + yy), zz).getState();
                        skull.setRotation(plugin.buildPB.getSkullDirection(d));
                        skull.update();
                        break;
                    case 152:
                        if (lamp != 123 && (preset.equals(TARDISConstants.PRESET.NEW) || preset.equals(TARDISConstants.PRESET.OLD))) {
                            plugin.utils.setBlock(world, xx, (y + yy), zz, cham_id, cham_data);
                        } else {
                            plugin.utils.setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                        }
                        break;
                    default: // everything else
                        if (preset.equals(TARDISConstants.PRESET.SUBMERGED) && yy == 0) {
                            plugin.utils.setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], coldatas[yy], tid);
                        } else {
                            plugin.utils.setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                        }
                        break;
                }
            }
        }
        if (sub && plugin.worldGuardOnServer) {
            plugin.wgutils.sponge(sponge, true);
        }
        plugin.tardisMaterialising.remove(Integer.valueOf(tid));
    }
}
