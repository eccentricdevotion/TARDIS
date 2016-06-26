/*
 * Copyright (C) 2014 eccentric_nz
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonColumn;
import me.eccentric_nz.TARDIS.chameleon.TARDISConstructColumn;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetBlocks;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.data.ReplacedBlock;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISJunkParticles;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.entity.Entity;
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
    private final BuildData bd;
    private final int loops;
    private final PRESET preset;
    public int task;
    private int i;
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
    private final List<Integer> doors = Arrays.asList(64, 71, 193, 194, 195, 196, 197);
    private Block handbrake;
    private byte h_data;

    /**
     * Runnable method to materialise the TARDIS Police Box. Tries to mimic the
     * transparency of materialisation by building the Police Box first with
     * GLASS, then STAINED_GLASS, then the normal preset wall block.
     *
     * @param plugin instance of the TARDIS plugin
     * @param bd the Materialisation data
     * @param preset the preset to construct
     * @param cham_id the chameleon block id for the police box
     * @param cham_data the chameleon block data for the police box
     * @param loops the number of loops to run
     */
    public TARDISMaterialisationPreset(TARDIS plugin, BuildData bd, PRESET preset, int cham_id, byte cham_data, int loops) {
        this.plugin = plugin;
        this.bd = bd;
        this.loops = loops;
        this.i = 0;
        this.preset = preset;
        this.cham_id = cham_id;
        this.cham_data = cham_data;
        rand = new Random();
        if (preset.equals(PRESET.ANGEL)) {
            plugin.getPresets().setR(rand.nextInt(2));
        }
        if (this.preset.equals(PRESET.CONSTRUCT)) {
            column = new TARDISConstructColumn(plugin, bd.getTardisID(), "blueprint", bd.getDirection()).getColumn();
            stained_column = new TARDISConstructColumn(plugin, bd.getTardisID(), "stain", bd.getDirection()).getColumn();
            glass_column = new TARDISConstructColumn(plugin, bd.getTardisID(), "glass", bd.getDirection()).getColumn();
        } else {
            column = plugin.getPresets().getColumn(preset, bd.getDirection());
            stained_column = plugin.getPresets().getStained(preset, bd.getDirection());
            glass_column = plugin.getPresets().getGlass(preset, bd.getDirection());
        }
        colours = new byte[]{0, 1, 2, 3, 4, 5, 6, 9, 10, 11, 12, 13, 14};
        random_colour = colours[rand.nextInt(13)];
        this.sign_colour = plugin.getUtils().getSignColour();
    }

    @Override
    public void run() {
        if (!plugin.getTrackerKeeper().getDematerialising().contains(bd.getTardisID())) {
            int[][] ids;
            byte[][] datas;
            // get relative locations
            int x = bd.getLocation().getBlockX(), plusx = bd.getLocation().getBlockX() + 1, minusx = bd.getLocation().getBlockX() - 1, y;
            if (preset.equals(PRESET.SUBMERGED)) {
                y = bd.getLocation().getBlockY() - 1;
            } else {
                y = bd.getLocation().getBlockY();
            }
            int z = bd.getLocation().getBlockZ(), plusz = bd.getLocation().getBlockZ() + 1, minusz = bd.getLocation().getBlockZ() - 1;
            World world = bd.getLocation().getWorld();
            int signx = 0, signz = 0;
            if (i < loops) {
                i++;
                if (preset.equals(PRESET.JUNK_MODE)) {
                    ids = column.getId();
                    datas = column.getData();
                } else {
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
                }
                QueryFactory qf = new QueryFactory(plugin);
                // rescue player?
                if (i == 10 && plugin.getTrackerKeeper().getRescue().containsKey(bd.getTardisID())) {
                    UUID playerUUID = plugin.getTrackerKeeper().getRescue().get(bd.getTardisID());
                    Player saved = plugin.getServer().getPlayer(playerUUID);
                    if (saved != null) {
                        TARDISDoorLocation idl = plugin.getGeneralKeeper().getDoorListener().getDoor(1, bd.getTardisID());
                        Location l = idl.getL();
                        plugin.getGeneralKeeper().getDoorListener().movePlayer(saved, l, false, world, false, 0, bd.useMinecartSounds());
                        TARDISSounds.playTARDISSound(saved, "tardis_land_fast");
                        // put player into travellers table
                        HashMap<String, Object> set = new HashMap<String, Object>();
                        set.put("tardis_id", bd.getTardisID());
                        set.put("uuid", playerUUID.toString());
                        qf.doInsert("travellers", set);
                    }
                    plugin.getTrackerKeeper().getRescue().remove(bd.getTardisID());
                }
                // first run - remember blocks
                if (i == 1) {
                    // if configured and it's a Whovian preset set biome
                    setBiome(world, x, z, bd.useTexture());
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("tardis_id", bd.getTardisID());
                    if (bd.isOutside()) {
                        if (!bd.useMinecartSounds()) {
                            String sound = (preset.equals(PRESET.JUNK_MODE)) ? "junk_land" : "tardis_land";
                            TARDISSounds.playTARDISSound(bd.getLocation(), sound);
                        } else {
                            world.playSound(bd.getLocation(), Sound.ENTITY_MINECART_INSIDE, 1.0F, 0.0F);
                        }
                    }
                    // get direction player is facing from yaw place block under door if block is in list of blocks an iron door cannot go on
                    switch (bd.getDirection()) {
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
                                    if (loops == 3) {
                                        TARDISBlockSetters.setBlock(world, xx, y, zz, rail.getTypeId(), rail.getData());
                                    } else {
                                        plugin.getBlockUtils().setBlockAndRemember(world, xx, y, zz, rail.getTypeId(), rail.getData(), bd.getTardisID());
                                    }
                                }
                            }
                            if (yy == 0 && n == 8 && !plugin.getPresetBuilder().no_block_under_door.contains(preset)) {
                                plugin.getBlockUtils().setUnderDoorBlock(world, xx, (y - 1), zz, bd.getTardisID(), true);
                            }
                            switch (colids[yy]) {
                                case 2:
                                case 3:
                                    int subi = (preset.equals(PRESET.SUBMERGED)) ? cham_id : colids[yy];
                                    byte subd = (preset.equals(PRESET.SUBMERGED)) ? cham_data : coldatas[yy];
                                    plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, subi, subd, bd.getTardisID());
                                    break;
                                case 35:
                                    int chai = (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) ? cham_id : colids[yy];
                                    byte chad = (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) ? cham_data : coldatas[yy];
                                    if (preset.equals(PRESET.PARTY) || (preset.equals(PRESET.FLOWER) && coldatas[yy] == 0)) {
                                        chad = random_colour;
                                    }
                                    plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, chai, chad, bd.getTardisID());
                                    break;
                                case 50: // lamps, glowstone and torches
                                case 89:
                                case 124:
                                    Material light;
                                    byte ld;
                                    if (bd.isSubmarine() && colids[yy] == 50) {
                                        light = Material.GLOWSTONE;
                                        ld = 0;
                                    } else {
                                        light = (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) ? bd.getLamp() : Material.getMaterial(colids[yy]);
                                        ld = coldatas[yy];
                                    }
                                    plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, light, ld, bd.getTardisID());
                                    break;
                                case 64: // wood, iron & trap doors, rails
                                case 66:
                                case 71:
                                case 96:
                                case 193:
                                case 194:
                                case 195:
                                case 196:
                                case 197:
                                    if (coldatas[yy] < 8 || colids[yy] == 96) {
                                        if (colids[yy] != 66) {
                                            // remember the door location
                                            String doorloc = world.getName() + ":" + xx + ":" + (y + yy) + ":" + zz;
                                            // should insert the door when tardis is first made, and then update location there after!
                                            HashMap<String, Object> whered = new HashMap<String, Object>();
                                            whered.put("door_type", 0);
                                            whered.put("tardis_id", bd.getTardisID());
                                            ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
                                            HashMap<String, Object> setd = new HashMap<String, Object>();
                                            setd.put("door_location", doorloc);
                                            setd.put("door_direction", bd.getDirection().toString());
                                            if (rsd.resultSet()) {
                                                HashMap<String, Object> whereid = new HashMap<String, Object>();
                                                whereid.put("door_id", rsd.getDoor_id());
                                                qf.doUpdate("doors", setd, whereid);
                                            } else {
                                                setd.put("tardis_id", bd.getTardisID());
                                                setd.put("door_type", 0);
                                                qf.doInsert("doors", setd);
                                            }
                                        }
                                    }
                                    if (yy == 0) {
                                        if (bd.isSubmarine() && plugin.isWorldGuardOnServer()) {
                                            int sy = y - 1;
                                            plugin.getBlockUtils().setBlockAndRemember(world, xx, sy, zz, 19, (byte) 0, bd.getTardisID());
                                            sponge = world.getBlockAt(xx, sy, zz);
                                            plugin.getWorldGuardUtils().sponge(sponge, true);
                                        } else if (!plugin.getPresetBuilder().no_block_under_door.contains(preset)) {
                                            plugin.getBlockUtils().setUnderDoorBlock(world, xx, (y - 1), zz, bd.getTardisID(), false);
                                        }
                                    }
                                    if (doors.contains(colids[yy]) && coldatas[yy] > 8) {
                                        plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], bd.getDirection().getUpperData(), bd.getTardisID());
                                    } else {
                                        plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], coldatas[yy], bd.getTardisID());
                                    }
                                    break;
                                case 63:
                                    if (preset.equals(PRESET.APPERTURE)) {
                                        plugin.getBlockUtils().setUnderDoorBlock(world, xx, (y - 1), zz, bd.getTardisID(), false);
                                    }
                                    break;
                                case 68: // sign - if there is one
                                    if (preset.equals(PRESET.JUNK_MODE)) {
                                        // add a sign
                                        TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                                        // remember its location
                                        String location = new Location(world, xx, (y + yy), zz).toString();
                                        saveJunkControl(location, "save_sign");
                                        // make it a save_sign
                                        Block sign = world.getBlockAt(xx, (y + yy), zz);
                                        if (sign.getType().equals(Material.WALL_SIGN)) {
                                            Sign s = (Sign) sign.getState();
                                            s.setLine(0, "TARDIS");
                                            s.setLine(1, plugin.getSigns().getStringList("saves").get(0));
                                            s.setLine(2, plugin.getSigns().getStringList("saves").get(1));
                                            s.setLine(3, "");
                                            s.update();
                                        }
                                    } else if (bd.shouldAddSign()) {
                                        TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                                        Block sign = world.getBlockAt(xx, (y + yy), zz);
                                        if (sign.getType().equals(Material.WALL_SIGN) || sign.getType().equals(Material.SIGN_POST)) {
                                            Sign s = (Sign) sign.getState();
                                            if (plugin.getConfig().getBoolean("police_box.name_tardis")) {
                                                HashMap<String, Object> wheret = new HashMap<String, Object>();
                                                wheret.put("tardis_id", bd.getTardisID());
                                                ResultSetTardis rst = new ResultSetTardis(plugin, wheret, "", false, 0);
                                                if (rst.resultSet()) {
                                                    Tardis tardis = rst.getTardis();
                                                    String player_name = plugin.getGeneralKeeper().getUUIDCache().getNameCache().get(tardis.getUuid());
                                                    if (player_name == null) {
                                                        // cache lookup failed, player may have disconnected
                                                        player_name = tardis.getOwner();
                                                    }
                                                    String owner;
                                                    if (preset.equals(PRESET.GRAVESTONE) || preset.equals(PRESET.PUNKED) || preset.equals(PRESET.ROBOT)) {
                                                        owner = (player_name.length() > 14) ? player_name.substring(0, 14) : player_name;
                                                    } else {
                                                        owner = (player_name.length() > 14) ? player_name.substring(0, 12) + "'s" : player_name + "'s";
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
                                    }
                                    break;
                                case 69:
                                    // remember this block and do at end
                                    if (preset.equals(PRESET.JUNK_MODE)) {
                                        // remember its location
                                        handbrake = world.getBlockAt(xx, (y + yy), zz);
                                        h_data = coldatas[yy];
                                    }
                                    break;
                                case 144:
                                    if (bd.isSubmarine()) {
                                        TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, 89, (byte) 0);
                                    } else {
                                        plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], coldatas[yy], bd.getTardisID());
                                        Skull skull = (Skull) world.getBlockAt(xx, (y + yy), zz).getState();
                                        skull.setRotation(plugin.getPresetBuilder().getSkullDirection(bd.getDirection()));
                                        skull.update();
                                    }
                                    break;
                                case 152:
                                    if (!bd.getLamp().equals(Material.REDSTONE_LAMP_OFF) && (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD))) {
                                        plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, cham_id, cham_data, bd.getTardisID());
                                    } else {
                                        plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], coldatas[yy], bd.getTardisID());
                                    }
                                    break;
                                default: // everything else
                                    if (change) {
                                        plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], coldatas[yy], bd.getTardisID());
                                    }
                                    break;
                            }
                        }
                    }
                }
                if (preset.equals(PRESET.JUNK_MODE) && plugin.getConfig().getBoolean("junk.particles")) {
                    // animate particles
                    for (Entity e : plugin.getUtils().getJunkTravellers(bd.getLocation())) {
                        if (e instanceof Player) {
                            Player p = (Player) e;
                            Location effectsLoc = bd.getLocation().clone().add(0.5d, 0, 0.5d);
                            TARDISJunkParticles.sendVortexParticles(effectsLoc, p);
                        }
                    }
                }
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
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, subi, subd);
                                break;
                            case 7:
                                if (preset.equals(PRESET.THEEND) && i == loops) {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, 7, (byte) 5);
                                    world.getBlockAt(xx, (y + yy + 1), zz).setType(Material.FIRE);
                                } else {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                                }
                                break;
                            case 35: // wool
                                int chai = (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) ? cham_id : colids[yy];
                                byte chad = (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) ? cham_data : coldatas[yy];
                                if (preset.equals(PRESET.PARTY) || (preset.equals(PRESET.FLOWER) && coldatas[yy] == 0)) {
                                    chad = random_colour;
                                }
                                if (bd.shouldUseCTM() && n == TARDISStaticUtils.getCol(bd.getDirection()) && yy == 1 && cham_id == 35 && (cham_data == (byte) 11 || cham_data == (byte) 3) && (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) && plugin.getConfig().getBoolean("police_box.set_biome")) {
                                    // set a quartz pillar block instead
                                    byte pillar = (bd.getDirection().equals(COMPASS.EAST) || bd.getDirection().equals(COMPASS.WEST)) ? (byte) 3 : (byte) 4;
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, 155, pillar);
                                } else {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chai, chad);
                                }
                                break;
                            case 38:
                                if (i == loops && preset.equals(PRESET.GRAVESTONE)) {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                                }
                                break;
                            case 50: // lamps, glowstone and torches
                            case 89:
                            case 124:
                                Material light;
                                byte ld;
                                if (bd.isSubmarine() && colids[yy] == 50) {
                                    light = Material.GLOWSTONE;
                                    ld = 0;
                                } else {
                                    light = (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) ? bd.getLamp() : Material.getMaterial(colids[yy]);
                                    ld = coldatas[yy];
                                }
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, light, ld);
                                break;
                            case 64:
                            case 71:
                            case 193:
                            case 194:
                            case 195:
                            case 196:
                            case 197:
                                // don't change the door
                                break;
                            case 69:
                                // remember this block and do at end
                                if (preset.equals(PRESET.JUNK_MODE)) {
                                    // remember its location
                                    handbrake = world.getBlockAt(xx, (y + yy), zz);
                                    h_data = coldatas[yy];
                                }
                                break;
                            case 87:
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                                if (preset.equals(PRESET.TORCH) && i == loops) {
                                    world.getBlockAt(xx, (y + yy + 1), zz).setType(Material.FIRE);
                                }
                                break;
                            case 90:
                                TARDISBlockSetters.setBlock(world, xx, (y + yy + 1), zz, 49, (byte) 0);
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
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
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, 95, chad);
                                } else {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                                }
                                break;
                            case 144:
                                if (bd.isSubmarine()) {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, 89, (byte) 0);
                                } else {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                                    Skull skull = (Skull) world.getBlockAt(xx, (y + yy), zz).getState();
                                    skull.setRotation(plugin.getPresetBuilder().getSkullDirection(bd.getDirection()));
                                    skull.update();
                                }
                                break;
                            case 152:
                                if (!bd.getLamp().equals(Material.REDSTONE_LAMP_OFF) && (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD))) {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, cham_id, cham_data);
                                } else {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                                }
                                break;
                            default: // everything else
                                if (change) {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                                }
                                break;
                        }
                    }
                }
            } else {
                if (preset.equals(PRESET.JUNK_MODE)) {
                    handbrake.setType(Material.LEVER);
                    handbrake.setData(h_data);
                    // remember its location
                    String location = handbrake.getLocation().toString();
                    saveJunkControl(location, "handbrake");
                    // set handbake to on ?
                }
                // just in case
                setBiome(world, x, z, bd.useTexture());
                // remove trackers
                plugin.getTrackerKeeper().getMaterialising().removeAll(Collections.singleton(bd.getTardisID()));
                plugin.getTrackerKeeper().getInVortex().removeAll(Collections.singleton(bd.getTardisID()));
                plugin.getServer().getScheduler().cancelTask(task);
                task = 0;
                // tardis has moved so remove HADS damage count
                if (plugin.getTrackerKeeper().getDamage().containsKey(bd.getTardisID())) {
                    plugin.getTrackerKeeper().getDamage().remove(bd.getTardisID());
                }
                if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(bd.getTardisID())) {
                    plugin.getTrackerKeeper().getDestinationVortex().remove(bd.getTardisID());
                }
                if (plugin.getTrackerKeeper().getMalfunction().containsKey(bd.getTardisID())) {
                    plugin.getTrackerKeeper().getMalfunction().remove(bd.getTardisID());
                }
                // message travellers in tardis
                if (loops > 3) {
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("tardis_id", bd.getTardisID());
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, where, true);
                    if (rst.resultSet()) {
                        List<UUID> travellers = rst.getData();
                        for (UUID s : travellers) {
                            Player p = plugin.getServer().getPlayer(s);
                            if (p != null) {
                                String message = (bd.isMalfunction()) ? "MALFUNCTION" : "HANDBRAKE_LEFT_CLICK";
                                TARDISMessage.send(p, message);
                                // TARDIS has travelled so add players to list so they can receive Artron on exit
                                if (!plugin.getTrackerKeeper().getHasTravelled().contains(s)) {
                                    plugin.getTrackerKeeper().getHasTravelled().add(s);
                                }
                            }
                        }
                    } else if (plugin.getTrackerKeeper().getJunkPlayers().containsKey(bd.getPlayer().getUniqueId())) {
                        TARDISMessage.send(bd.getPlayer().getPlayer(), "JUNK_HANDBRAKE_LEFT_CLICK");
                    }
                    // restore beacon up block if present
                    HashMap<String, Object> whereb = new HashMap<String, Object>();
                    whereb.put("tardis_id", bd.getTardisID());
                    whereb.put("police_box", 2);
                    ResultSetBlocks rs = new ResultSetBlocks(plugin, whereb, false);
                    if (rs.resultSet()) {
                        ReplacedBlock rb = rs.getReplacedBlock();
                        TARDISBlockSetters.setBlock(rb.getLocation(), rb.getBlock(), rb.getData());
                        HashMap<String, Object> whered = new HashMap<String, Object>();
                        whered.put("tardis_id", bd.getTardisID());
                        whered.put("police_box", 2);
                        new QueryFactory(plugin).doDelete("blocks", whered);
                    }
                }
            }
        }
    }

    public void setBiome(World world, int x, int z, boolean pp) {
        if (plugin.getConfig().getBoolean("police_box.set_biome") && (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD) || preset.equals(PRESET.PANDORICA)) && pp) {
            List<Chunk> chunks = new ArrayList<Chunk>();
            Chunk chunk = bd.getLocation().getChunk();
            chunks.add(chunk);
            // load the chunk
            final int cx = bd.getLocation().getBlockX() >> 4;
            final int cz = bd.getLocation().getBlockZ() >> 4;
            if (!world.loadChunk(cx, cz, false)) {
                world.loadChunk(cx, cz, true);
            }
            while (!chunk.isLoaded()) {
                world.loadChunk(chunk);
            }
            // set the biome
            for (int c = -1; c < 2; c++) {
                for (int r = -1; r < 2; r++) {
                    world.setBiome(x + c, z + r, Biome.DEEP_OCEAN);
                    if (TARDISConstants.NO_RAIN.contains(bd.getBiome())) {
                        // add an invisible roof
                        if (loops == 3) {
                            TARDISBlockSetters.setBlock(world, x + c, 255, z + r, Material.BARRIER, (byte) 0);
                        } else {
                            plugin.getBlockUtils().setBlockAndRemember(world, x + c, 255, z + r, Material.BARRIER, (byte) 0, bd.getTardisID());
                        }
                    }
                    Chunk tmp_chunk = world.getChunkAt(new Location(world, x + c, 64, z + r));
                    if (!chunks.contains(tmp_chunk)) {
                        chunks.add(tmp_chunk);
                    }
                }
            }
            // refresh the chunks
            for (Chunk c : chunks) {
                //world.refreshChunk(c.getX(), c.getZ());
                plugin.getTardisHelper().refreshChunk(c);
            }
        }
    }

    private void saveJunkControl(String location, String field) {
        // remember control location
        HashMap<String, Object> wherej = new HashMap<String, Object>();
        wherej.put("tardis_id", bd.getTardisID());
        HashMap<String, Object> setj = new HashMap<String, Object>();
        setj.put(field, location);
        new QueryFactory(plugin).doUpdate("junk", setj, wherej);
    }

    public void setTask(int task) {
        this.task = task;
    }
}
