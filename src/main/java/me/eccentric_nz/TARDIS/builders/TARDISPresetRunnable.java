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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.TARDISConstants.COMPASS;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonColumn;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
//import org.getspout.spoutapi.SpoutManager;

/**
 * A dematerialisation circuit was an essential part of a Type 40 TARDIS which
 * enabled it to dematerialise from normal space into the Time Vortex and
 * rematerialise back from it.
 *
 * @author eccentric_nz
 */
public class TARDISPresetRunnable implements Runnable {

    private final TARDIS plugin;
    private final COMPASS d;
    private final int loops;
    private final Location location;
    private final TARDISConstants.PRESET preset;
    private final int tid;
    public int task;
    private int i;
    private final Player player;
    private final boolean mal;
    private final int lamp;
    private final boolean sub;
    private final int cham_id;
    private final byte cham_data;
    private Block sponge;
    private final TARDISChameleonColumn column;
    private final TARDISChameleonColumn ice_column;
    private final TARDISChameleonColumn glass_column;
    private final List<TARDISConstants.PRESET> no_block_under_door;

    /**
     * Runnable method to materialise the TARDIS Police Box. Tries to mimic the
     * transparency of materialisation by building the Police Box first with
     * GLASS, then ICE, then the normall wall block (BLUE WOOL or the chameleon
     * material).
     *
     * @param plugin instance of the TARDIS plugin
     * @param location the location to build the Police Box at
     * @param preset the preset to construct
     * @param tid the tardis_id this Police Box belongs to
     * @param d the COMPASS direction the Police Box is facing
     * @param player a player
     * @param mal a boolean determining whether there has been a TARDIS
     * malfunction
     * @param lamp a boolean determining whether there should be a lamp
     * @param sub a boolean determining whether the TARDIS is in submarine mode
     * @param cham_id the chameleon block id for the police box
     * @param cham_data the chameleon block data for the police box
     */
    public TARDISPresetRunnable(TARDIS plugin, Location location, TARDISConstants.PRESET preset, int tid, COMPASS d, Player player, boolean mal, int lamp, boolean sub, int cham_id, byte cham_data) {
        this.plugin = plugin;
        this.d = d;
        this.loops = 12;
        this.location = location;
        this.i = 0;
        this.tid = tid;
        this.preset = preset;
        this.player = player;
        this.mal = mal;
        this.lamp = lamp;
        this.sub = sub;
        this.cham_id = cham_id;
        this.cham_data = cham_data;
        column = plugin.presets.getColumn(preset, d);
        ice_column = plugin.presets.getIce(preset, d);
        glass_column = plugin.presets.getGlass(preset, d);
        no_block_under_door = new ArrayList<TARDISConstants.PRESET>();
        no_block_under_door.add(TARDISConstants.PRESET.RAISED);
        no_block_under_door.add(TARDISConstants.PRESET.SUBMERGED);
        no_block_under_door.add(TARDISConstants.PRESET.WELL);
    }

    @Override
    public void run() {
        if (!plugin.tardisDematerialising.contains(tid)) {
            int[][] ids;
            byte[][] datas;
            // get relative locations
            int x = location.getBlockX(), plusx = location.getBlockX() + 1, minusx = location.getBlockX() - 1;
            int y;
            int undery;
            if (preset.equals(TARDISConstants.PRESET.WELL)) {
                plugin.isWellPresetMaterialising.add("tid" + tid);
            }
            if (preset.equals(TARDISConstants.PRESET.SUBMERGED)) {
                y = location.getBlockY() - 1;
                undery = (location.getBlockY() - 2);
            } else {
                y = location.getBlockY();
                undery = (location.getBlockY() - 1);
            }
            int z = location.getBlockZ(), plusz = location.getBlockZ() + 1, minusz = location.getBlockZ() - 1;
            int platform_id = plugin.getConfig().getInt("platform_id");
            byte platform_data = (byte) plugin.getConfig().getInt("platform_data");
            World world = location.getWorld();
            int signx = 0, signz = 0;
            if (i < loops) {
                i++;
                // determine preset to use
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
                // rescue player?
                if (i == 10 && plugin.trackRescue.containsKey(tid)) {
                    String name = plugin.trackRescue.get(tid);
                    Player saved = plugin.getServer().getPlayer(name);
                    if (saved != null) {
                        TARDISDoorLocation idl = plugin.doorListener.getDoor(1, tid);
                        Location l = idl.getL();
                        plugin.doorListener.movePlayer(saved, l, false, world, false);
                        // put player into travellers table
                        HashMap<String, Object> set = new HashMap<String, Object>();
                        set.put("tardis_id", tid);
                        set.put("player", name);
                        QueryFactory qf = new QueryFactory(plugin);
                        qf.doInsert("travellers", set);
                    }
                    plugin.trackRescue.remove(tid);
                }
                // first run - remember blocks
                if (i == 1) {
                    plugin.buildPB.addPlatform(location, false, d, player.getName(), tid);
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("tardis_id", tid);
//                    if (plugin.pm.getPlugin("Spout") != null && SpoutManager.getPlayer(player).isSpoutCraftEnabled()) {
//                        SpoutManager.getSoundManager().playGlobalCustomSoundEffect(plugin, "https://dl.dropboxusercontent.com/u/53758864/tardis_land.mp3", false, location, 9, 75);
//                    } else {
                    try {
                        Class.forName("org.bukkit.Sound");
                        world.playSound(location, Sound.MINECART_INSIDE, 1, 0);
                    } catch (ClassNotFoundException e) {
                        world.playEffect(location, Effect.BLAZE_SHOOT, 0);
                    }
//                    }
                    QueryFactory qf = new QueryFactory(plugin);
                    HashMap<String, Object> ps = new HashMap<String, Object>();
                    ps.put("tardis_id", tid);
                    String loc = "";
                    // get direction player is facing from yaw place block under door if block is in list of blocks an iron door cannot go on
                    switch (d) {
                        case SOUTH:
                            //if (yaw >= 315 || yaw < 45)
                            if (sub) {
                                plugin.utils.setBlockCheck(world, x, undery, minusz, 19, (byte) 0, tid, true); // door is here if player facing south
                                sponge = world.getBlockAt(x, undery, minusz);
                            } else if (!no_block_under_door.contains(preset)) {
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
                            } else if (!no_block_under_door.contains(preset)) {
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
                            } else if (!no_block_under_door.contains(preset)) {
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
                            } else if (!no_block_under_door.contains(preset)) {
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
                    for (int n = 0; n < 10; n++) {
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
                                    plugin.utils.setBlockAndRemember(world, xx, (y + yy), zz, subi, subd, tid);
                                    break;
                                case 35:
                                    int chai = (preset.equals(TARDISConstants.PRESET.NEW) || preset.equals(TARDISConstants.PRESET.OLD)) ? cham_id : colids[yy];
                                    byte chad = (preset.equals(TARDISConstants.PRESET.NEW) || preset.equals(TARDISConstants.PRESET.OLD)) ? cham_data : coldatas[yy];
                                    plugin.utils.setBlockAndRemember(world, xx, (y + yy), zz, chai, chad, tid);
                                    break;
                                case 50: // lamps, glowstone and torches
                                case 89:
                                case 124:
                                    int light = (preset.equals(TARDISConstants.PRESET.NEW) || preset.equals(TARDISConstants.PRESET.OLD)) ? lamp : colids[yy];
                                    plugin.utils.setBlockAndRemember(world, xx, (y + yy), zz, light, coldatas[yy], tid);
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
                                        plugin.utils.setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], coldatas[yy], tid);
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
                                                String owner = rst.getOwner();
                                                if (owner.length() > 14) {
                                                    s.setLine(0, owner.substring(0, 12) + "'s");
                                                } else {
                                                    s.setLine(0, owner + "'s");
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
                                            case RAISED:
                                                line1 = "SIGN ABOVE";
                                                line2 = "THE DOOR";
                                                break;
                                            case SWAMP:
                                                line1 = "SWAMP";
                                                line2 = "HUT";
                                                break;
                                            default:
                                                line1 = "POLICE";
                                                line2 = "BOX";
                                                break;
                                        }
                                        s.setLine(1, ChatColor.WHITE + line1);
                                        s.setLine(2, ChatColor.WHITE + line2);
                                        s.update();
                                    }
                                    break;
                                default: // everything else
                                    if (preset.equals(TARDISConstants.PRESET.SUBMERGED) && yy == 0) {
                                        plugin.utils.setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], coldatas[yy], tid);
                                    } else {
                                        plugin.utils.setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], coldatas[yy], tid);
                                    }
                                    break;
                            }
                        }
                    }
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
                if (preset.equals(TARDISConstants.PRESET.WELL)) {
                    plugin.isWellPresetMaterialising.remove("tid" + tid);
                }
                // set sheild if submarine
                if (sub && plugin.worldGuardOnServer) {
                    plugin.wgutils.sponge(sponge, true);
                }
                plugin.tardisMaterialising.remove(Integer.valueOf(tid));
                plugin.getServer().getScheduler().cancelTask(task);
                task = 0;
                // tardis has moved so remove HADS damage count
                if (plugin.trackDamage.containsKey(Integer.valueOf(tid))) {
                    plugin.trackDamage.remove(Integer.valueOf(tid));
                }
                // message travellers in tardis
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("tardis_id", tid);
                ResultSetTravellers rst = new ResultSetTravellers(plugin, where, true);
                if (rst.resultSet()) {
                    List<String> travellers = rst.getData();
                    for (String s : travellers) {
                        Player p = plugin.getServer().getPlayer(s);
                        if (p != null) {
                            String message = (mal) ? "There was a malfunction and the emergency handbrake was engaged! Scan location before exit!" : "LEFT-click the handbrake to exit!";
                            p.sendMessage(plugin.pluginName + message);
                        }
                    }
                }
            }
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
