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
import java.util.List;
import java.util.Random;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonColumn;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;

/**
 * A dematerialisation circuit was an essential part of a Type 40 TARDIS which
 * enabled it to dematerialise from normal space into the Time Vortex and
 * rematerialise back from it.
 *
 * @author eccentric_nz
 */
public class TARDISMaterialisationPreset implements Runnable {

    private final TARDIS plugin;
    private final COMPASS d;
    private final int loops;
    private final Location location;
    private final PRESET preset;
    private final int tid;
    public int task;
    private int i;
    private final Player player;
    private final boolean mal;
    private final int lamp;
    private final boolean sub;
    private final boolean minecart;
    private final int cham_id;
    private final byte cham_data;
    private Block sponge;
    private final TARDISChameleonColumn column;
    private final TARDISChameleonColumn stained_column;
    private final TARDISChameleonColumn glass_column;
    private final byte[] colours;
    private final Random rand;
    private final byte random_colour;
    private final ChatColor sign_colour;

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
     * @param minecart whether to play the minecart sound instead of the
     * resource pack sounds
     */
    public TARDISMaterialisationPreset(TARDIS plugin, Location location, PRESET preset, int tid, COMPASS d, Player player, boolean mal, int lamp, boolean sub, int cham_id, byte cham_data, boolean minecart) {
        this.plugin = plugin;
        this.d = d;
        this.loops = 18;
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
        this.minecart = minecart;
        rand = new Random();
        if (preset.equals(PRESET.ANGEL)) {
            plugin.getPresets().setR(rand.nextInt(2));
        }
        column = plugin.getPresets().getColumn(preset, d);
        stained_column = plugin.getPresets().getStained(preset, d);
        glass_column = plugin.getPresets().getGlass(preset, d);
        colours = new byte[]{0, 1, 2, 3, 4, 5, 6, 9, 10, 11, 12, 13, 14};
        random_colour = colours[rand.nextInt(13)];
        this.sign_colour = getSignColour();
    }

    @Override
    public void run() {
        if (!plugin.getTrackerKeeper().getTrackDematerialising().contains(Integer.valueOf(tid))) {
            int[][] ids;
            byte[][] datas;
            // get relative locations
            int x = location.getBlockX(), plusx = location.getBlockX() + 1, minusx = location.getBlockX() - 1, y;
            if (preset.equals(PRESET.SUBMERGED)) {
                y = location.getBlockY() - 1;
            } else {
                y = location.getBlockY();
            }
            int z = location.getBlockZ(), plusz = location.getBlockZ() + 1, minusz = location.getBlockZ() - 1;
            int platform_id = plugin.getConfig().getInt("police_box.platform_id");
            byte platform_data = (byte) plugin.getConfig().getInt("police_box.platform_data");
            World world = location.getWorld();
            int signx = 0, signz = 0;
            if (i < loops) {
                i++;
                // determine preset to use
                switch (i % 3) {
                    case 2: // stained
                        ids = stained_column.getId();
                        datas = stained_column.getData();
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
                QueryFactory qf = new QueryFactory(plugin);
                // rescue player?
                if (i == 10 && plugin.getTrackerKeeper().getTrackRescue().containsKey(tid)) {
                    String name = plugin.getTrackerKeeper().getTrackRescue().get(tid);
                    Player saved = plugin.getServer().getPlayer(name);
                    if (saved != null) {
                        TARDISDoorLocation idl = plugin.getGeneralKeeper().getDoorListener().getDoor(1, tid);
                        Location l = idl.getL();
                        plugin.getGeneralKeeper().getDoorListener().movePlayer(saved, l, false, world, false, 0, minecart);
                        // put player into travellers table
                        HashMap<String, Object> set = new HashMap<String, Object>();
                        set.put("tardis_id", tid);
                        set.put("player", name);
                        qf.doInsert("travellers", set);
                    }
                    plugin.getTrackerKeeper().getTrackRescue().remove(tid);
                }
                // first run - remember blocks
                if (i == 1) {
                    plugin.getPresetBuilder().addPlatform(location, false, d, player.getName(), tid);
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("tardis_id", tid);
                    if (!minecart) {
                        plugin.getUtils().playTARDISSound(location, player, "tardis_land");
                    } else {
                        world.playSound(location, Sound.MINECART_INSIDE, 1.0F, 0.0F);
                    }
                    // get direction player is facing from yaw place block under door if block is in list of blocks an iron door cannot go on
                    switch (d) {
                        case SOUTH:
                            //if (yaw >= 315 || yaw < 45)
                            signx = x;
                            signz = (minusz - 1);
                            break;
                        case EAST:
                            //if (yaw >= 225 && yaw < 315)
                            signx = (minusx - 1);
                            signz = z;
                            break;
                        case NORTH:
                            //if (yaw >= 135 && yaw < 225)
                            signx = x;
                            signz = (plusz + 1);
                            break;
                        case WEST:
                            //if (yaw >= 45 && yaw < 135)
                            signx = (plusx + 1);
                            signz = z;
                            break;
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
                            boolean change = true;
                            if (yy == 0 && n == 9) {
                                Block rail = world.getBlockAt(xx, y, zz);
                                if (rail.getType().equals(Material.RAILS) || rail.getType().equals(Material.POWERED_RAIL)) {
                                    change = false;
                                    plugin.getUtils().setBlockAndRemember(world, xx, y, zz, rail.getTypeId(), rail.getData(), tid);
                                }
                            }
                            switch (colids[yy]) {
                                case 2:
                                case 3:
                                    int subi = (preset.equals(PRESET.SUBMERGED)) ? cham_id : colids[yy];
                                    byte subd = (preset.equals(PRESET.SUBMERGED)) ? cham_data : coldatas[yy];
                                    plugin.getUtils().setBlockAndRemember(world, xx, (y + yy), zz, subi, subd, tid);
                                    break;
                                case 35:
                                    int chai = (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) ? cham_id : colids[yy];
                                    byte chad = (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) ? cham_data : coldatas[yy];
                                    if (preset.equals(PRESET.PARTY) || (preset.equals(PRESET.FLOWER) && coldatas[yy] == 0)) {
                                        chad = random_colour;
                                    }
                                    plugin.getUtils().setBlockAndRemember(world, xx, (y + yy), zz, chai, chad, tid);
                                    break;
                                case 50: // lamps, glowstone and torches
                                case 89:
                                case 124:
                                    int light;
                                    byte ld;
                                    if (sub && colids[yy] == 50) {
                                        light = 89;
                                        ld = 0;
                                    } else {
                                        light = (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) ? lamp : colids[yy];
                                        ld = coldatas[yy];
                                    }
                                    plugin.getUtils().setBlockAndRemember(world, xx, (y + yy), zz, light, ld, tid);
                                    break;
                                case 64: // wood, iron & trap doors, rails
                                case 66:
                                case 71:
                                case 96:
                                    if (coldatas[yy] < 8 || colids[yy] == 96) {
                                        if (colids[yy] != 66) {
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
                                    }
                                    if (yy == 0) {
                                        if (sub && plugin.isWorldGuardOnServer()) {
                                            int sy = y - 1;
                                            plugin.getUtils().setBlockAndRemember(world, xx, sy, zz, 19, (byte) 0, tid);
                                            sponge = world.getBlockAt(xx, sy, zz);
                                            plugin.getWorldGuardUtils().sponge(sponge, true);
                                        } else if (!plugin.getPresetBuilder().no_block_under_door.contains(preset)) {
                                            plugin.getUtils().setUnderDoorBlock(world, xx, (y - 1), zz, platform_id, platform_data, tid);
                                        }
                                    }
                                    plugin.getUtils().setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], coldatas[yy], tid);
                                    break;
                                case 63:
                                    if (preset.equals(PRESET.APPERTURE)) {
                                        plugin.getUtils().setUnderDoorBlock(world, xx, (y - 1), zz, platform_id, platform_data, tid);
                                    }
                                case 68: // sign - if there is one
                                    plugin.getUtils().setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                                    Block sign = world.getBlockAt(xx, (y + yy), zz);
                                    if (sign.getType().equals(Material.WALL_SIGN) || sign.getType().equals(Material.SIGN_POST)) {
                                        Sign s = (Sign) sign.getState();
                                        if (plugin.getConfig().getBoolean("police_box.name_tardis")) {
                                            HashMap<String, Object> wheret = new HashMap<String, Object>();
                                            wheret.put("tardis_id", tid);
                                            ResultSetTardis rst = new ResultSetTardis(plugin, wheret, "", false);
                                            if (rst.resultSet()) {
                                                String owner;
                                                if (preset.equals(PRESET.GRAVESTONE) || preset.equals(PRESET.PUNKED) || preset.equals(PRESET.ROBOT)) {
                                                    owner = (rst.getOwner().length() > 14) ? rst.getOwner().substring(0, 14) : rst.getOwner();
                                                } else {
                                                    owner = (rst.getOwner().length() > 14) ? rst.getOwner().substring(0, 12) + "'s" : rst.getOwner() + "'s";
                                                }
                                                switch (preset) {
                                                    case GRAVESTONE:
                                                        s.setLine(3, owner);
                                                        break;
                                                    case ANGEL:
                                                    case JAIL:
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
                                        if (preset.equals(PRESET.CUSTOM)) {
                                            line1 = plugin.getPresets().custom.getFirstLine();
                                            line2 = plugin.getPresets().custom.getSecondLine();
                                        } else {
                                            line1 = preset.getFirstLine();
                                            line2 = preset.getSecondLine();
                                        }
                                        switch (preset) {
                                            case ANGEL:
                                                s.setLine(0, sign_colour + line1);
                                                s.setLine(1, sign_colour + line2);
                                                s.setLine(3, sign_colour + "TARDIS");
                                                break;
                                            case APPERTURE:
                                                s.setLine(1, sign_colour + line1);
                                                s.setLine(2, sign_colour + line2);
                                                s.setLine(3, sign_colour + "LAB");
                                                break;
                                            case JAIL:
                                                s.setLine(0, sign_colour + line1);
                                                s.setLine(1, sign_colour + line2);
                                                s.setLine(3, sign_colour + "CAPTURE");
                                                break;
                                            case THEEND:
                                                s.setLine(1, sign_colour + line1);
                                                s.setLine(2, sign_colour + line2);
                                                s.setLine(3, sign_colour + "HOT ROD");
                                                break;
                                            case CUSTOM:
                                                break;
                                            default:
                                                s.setLine(1, sign_colour + line1);
                                                s.setLine(2, sign_colour + line2);
                                                break;
                                        }
                                        s.update();
                                    }
                                    break;
                                case 144:
                                    if (sub) {
                                        plugin.getUtils().setBlock(world, xx, (y + yy), zz, 89, (byte) 0);
                                    } else {
                                        plugin.getUtils().setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], coldatas[yy], tid);
                                        Skull skull = (Skull) world.getBlockAt(xx, (y + yy), zz).getState();
                                        skull.setRotation(plugin.getPresetBuilder().getSkullDirection(d));
                                        skull.update();
                                    }
                                    break;
                                case 152:
                                    if (lamp != 123 && (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD))) {
                                        plugin.getUtils().setBlockAndRemember(world, xx, (y + yy), zz, cham_id, cham_data, tid);
                                    } else {
                                        plugin.getUtils().setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], coldatas[yy], tid);
                                    }
                                    break;
                                default: // everything else
                                    if (change) {
                                        plugin.getUtils().setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], coldatas[yy], tid);
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
                            boolean change = true;
                            if (yy == 0 && n == 9) {
                                Block rail = world.getBlockAt(xx, y, zz);
                                if (rail.getType().equals(Material.RAILS) || rail.getType().equals(Material.POWERED_RAIL)) {
                                    change = false;
                                }
                            }
                            switch (colids[yy]) {
                                case 2:
                                case 3:
                                    int subi = (preset.equals(PRESET.SUBMERGED)) ? cham_id : colids[yy];
                                    byte subd = (preset.equals(PRESET.SUBMERGED)) ? cham_data : coldatas[yy];
                                    plugin.getUtils().setBlock(world, xx, (y + yy), zz, subi, subd);
                                    break;
                                case 7:
                                    if (preset.equals(PRESET.THEEND) && i == loops) {
                                        plugin.getUtils().setBlock(world, xx, (y + yy), zz, 7, (byte) 5);
                                        world.getBlockAt(xx, (y + yy + 1), zz).setType(Material.FIRE);
                                    } else {
                                        plugin.getUtils().setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                                    }
                                    break;
                                case 35: // wool
                                    int chai = (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) ? cham_id : colids[yy];
                                    byte chad = (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) ? cham_data : coldatas[yy];
                                    if (preset.equals(PRESET.PARTY) || (preset.equals(PRESET.FLOWER) && coldatas[yy] == 0)) {
                                        chad = random_colour;
                                    }
                                    plugin.getUtils().setBlock(world, xx, (y + yy), zz, chai, chad);
                                    break;
                                case 38:
                                    if (i == loops && preset.equals(PRESET.GRAVESTONE)) {
                                        plugin.getUtils().setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                                    }
                                    break;
                                case 50: // lamps, glowstone and torches
                                case 89:
                                case 124:
                                    int light;
                                    byte ld;
                                    if (sub && colids[yy] == 50) {
                                        light = 89;
                                        ld = 0;
                                    } else {
                                        light = (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) ? lamp : colids[yy];
                                        ld = coldatas[yy];
                                    }
                                    plugin.getUtils().setBlock(world, xx, (y + yy), zz, light, ld);
                                    break;
                                case 87:
                                    plugin.getUtils().setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                                    if (preset.equals(PRESET.TORCH) && i == loops) {
                                        world.getBlockAt(xx, (y + yy + 1), zz).setType(Material.FIRE);
                                    }
                                    break;
                                case 90:
                                    plugin.getUtils().setBlock(world, xx, (y + yy + 1), zz, 49, (byte) 0);
                                    plugin.getUtils().setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                                    break;
                                case 95:
                                    if (coldatas[yy] == -1) {
                                        if (preset.equals(PRESET.PARTY) || (preset.equals(PRESET.FLOWER) && coldatas[yy] == 0)) {
                                            chad = random_colour;
                                        } else {
                                            // if it was a wool / stained glass / stained clay block get the data from that
                                            int[] finalids = column.getId()[n];
                                            byte[] finaldatas = column.getData()[n];
                                            if (finalids[yy] == 35 || finalids[yy] == 95 || finalids[yy] == 159 || finalids[yy] == 160 || finalids[yy] == 171) {
                                                if (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) {
                                                    chad = cham_data;
                                                } else {
                                                    chad = finaldatas[yy];
                                                }
                                            } else {
                                                chad = plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(cham_id);
                                            }
                                        }
                                        plugin.getUtils().setBlock(world, xx, (y + yy), zz, 95, chad);
                                    } else {
                                        plugin.getUtils().setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                                    }
                                    break;
                                case 144:
                                    if (sub) {
                                        plugin.getUtils().setBlock(world, xx, (y + yy), zz, 89, (byte) 0);
                                    } else {
                                        plugin.getUtils().setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                                        Skull skull = (Skull) world.getBlockAt(xx, (y + yy), zz).getState();
                                        skull.setRotation(plugin.getPresetBuilder().getSkullDirection(d));
                                        skull.update();
                                    }
                                    break;
                                case 152:
                                    if (lamp != 123 && (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD))) {
                                        plugin.getUtils().setBlock(world, xx, (y + yy), zz, cham_id, cham_data);
                                    } else {
                                        plugin.getUtils().setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                                    }
                                    break;
                                default: // everything else
                                    if (change) {
                                        plugin.getUtils().setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                                    }
                                    break;
                            }
                        }
                    }
                }
            } else {
                // set sheild if submarine
                plugin.getTrackerKeeper().getTrackMaterialising().remove(Integer.valueOf(tid));
                plugin.getTrackerKeeper().getTrackInVortex().remove(Integer.valueOf(tid));
                plugin.getServer().getScheduler().cancelTask(task);
                task = 0;
                // tardis has moved so remove HADS damage count
                if (plugin.getTrackerKeeper().getTrackDamage().containsKey(Integer.valueOf(tid))) {
                    plugin.getTrackerKeeper().getTrackDamage().remove(Integer.valueOf(tid));
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
                            TARDISMessage.send(p, plugin.getPluginName() + message);
                        }
                    }
                }
            }
        }
    }

    public void setTask(int task) {
        this.task = task;
    }

    private ChatColor getSignColour() {
        ChatColor colour;
        String cc = plugin.getConfig().getString("police_box.sign_colour");
        try {
            colour = ChatColor.valueOf(cc);
        } catch (IllegalArgumentException e) {
            colour = ChatColor.WHITE;
        }
        return colour;
    }
}
