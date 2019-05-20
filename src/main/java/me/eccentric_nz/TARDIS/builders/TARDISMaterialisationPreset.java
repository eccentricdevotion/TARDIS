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
package me.eccentric_nz.TARDIS.builders;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonColumn;
import me.eccentric_nz.TARDIS.chameleon.TARDISConstructColumn;
import me.eccentric_nz.TARDIS.database.*;
import me.eccentric_nz.TARDIS.database.data.ReplacedBlock;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.ADAPTION;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import me.eccentric_nz.TARDIS.utility.*;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.*;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * A dematerialisation circuit was an essential part of a Type 40 TARDIS which enabled it to dematerialise from normal
 * space into the Time Vortex and rematerialise back from it.
 *
 * @author eccentric_nz
 */
class TARDISMaterialisationPreset implements Runnable {

    private final TARDIS plugin;
    private final BuildData bd;
    private final int loops;
    private final PRESET preset;
    private int task;
    private int i;
    private final BlockData cham_id;
    private final ADAPTION adapt;
    private final TARDISChameleonColumn column;
    private final TARDISChameleonColumn stained_column;
    private final TARDISChameleonColumn glass_column;
    private final Material random_colour;
    private final Material random_glass;
    private final ChatColor sign_colour;
    private Block handbrake;
    private BlockData h_data;
    private Block swampDoorBottom;
    private BlockData sdb_data;
    private Block swampDoorTop;
    private BlockData sdt_data;
    private Block swampSlab;
    private BlockData slab_data;

    /**
     * Runnable method to materialise the TARDIS Police Box. Tries to mimic the transparency of materialisation by
     * building the Police Box first with GLASS, then STAINED_GLASS, then the normal preset wall block.
     *
     * @param plugin  instance of the TARDIS plugin
     * @param bd      the Materialisation data
     * @param preset  the preset to construct
     * @param cham_id the chameleon block data for the police box
     * @param adapt   the chameleon circuit adaption setting
     * @param loops   the number of loops to run
     */
    public TARDISMaterialisationPreset(TARDIS plugin, BuildData bd, PRESET preset, BlockData cham_id, ADAPTION adapt, int loops) {
        this.plugin = plugin;
        this.bd = bd;
        this.loops = loops;
        i = 0;
        this.preset = preset;
        this.cham_id = cham_id;
        this.adapt = adapt;
        if (preset.equals(PRESET.ANGEL)) {
            plugin.getPresets().setR(TARDISConstants.RANDOM.nextInt(2));
        }
        if (this.preset.equals(PRESET.CONSTRUCT)) {
            column = new TARDISConstructColumn(plugin, bd.getTardisID(), "blueprintData", bd.getDirection()).getColumn();
            stained_column = new TARDISConstructColumn(plugin, bd.getTardisID(), "stainData", bd.getDirection()).getColumn();
            glass_column = new TARDISConstructColumn(plugin, bd.getTardisID(), "glassData", bd.getDirection()).getColumn();
        } else {
            column = plugin.getPresets().getColumn(preset, bd.getDirection());
            stained_column = plugin.getPresets().getStained(preset, bd.getDirection());
            glass_column = plugin.getPresets().getGlass(preset, bd.getDirection());
        }
        Material[] colours = new Material[]{Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.MAGENTA_WOOL, Material.LIGHT_BLUE_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL, Material.PINK_WOOL, Material.CYAN_WOOL, Material.PURPLE_WOOL, Material.BLUE_WOOL, Material.BROWN_WOOL, Material.GREEN_WOOL, Material.RED_WOOL};
        Material[] glassColours = new Material[]{Material.WHITE_STAINED_GLASS, Material.ORANGE_STAINED_GLASS, Material.MAGENTA_STAINED_GLASS, Material.LIGHT_BLUE_STAINED_GLASS, Material.YELLOW_STAINED_GLASS, Material.LIME_STAINED_GLASS, Material.PINK_STAINED_GLASS, Material.CYAN_STAINED_GLASS, Material.PURPLE_STAINED_GLASS, Material.BLUE_STAINED_GLASS, Material.BROWN_STAINED_GLASS, Material.GREEN_STAINED_GLASS, Material.RED_STAINED_GLASS};
        int r = TARDISConstants.RANDOM.nextInt(13);
        random_colour = colours[r];
        random_glass = glassColours[r];
        sign_colour = plugin.getUtils().getSignColour();
    }

    @Override
    public void run() {
        if (!plugin.getTrackerKeeper().getDematerialising().contains(bd.getTardisID())) {
            BlockData[][] datas;
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
                    datas = column.getBlockData();
                } else {
                    // determine preset to use
                    switch (i % 3) {
                        case 2: // stained
                            datas = stained_column.getBlockData();
                            break;
                        case 1: // glass
                            datas = glass_column.getBlockData();
                            break;
                        default: // preset
                            datas = column.getBlockData();
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
                        HashMap<String, Object> set = new HashMap<>();
                        set.put("tardis_id", bd.getTardisID());
                        set.put("uuid", playerUUID.toString());
                        qf.doInsert("travellers", set);
                    }
                    plugin.getTrackerKeeper().getRescue().remove(bd.getTardisID());
                }
                // first run - remember blocks
                if (i == 1) {
                    // if configured and it's a Whovian preset set biome
                    setBiome(world, x, z, bd.useTexture(), true);
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
                        BlockData[] colData = datas[n];
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
                                if (rail.getType().equals(Material.RAIL) || rail.getType().equals(Material.POWERED_RAIL)) {
                                    change = false;
                                    if (loops == 3) {
                                        TARDISBlockSetters.setBlock(world, xx, y, zz, rail.getBlockData());
                                    } else {
                                        TARDISBlockSetters.setBlockAndRemember(world, xx, y, zz, rail.getBlockData(), bd.getTardisID());
                                    }
                                } else if (preset.equals(PRESET.SWAMP)) {
                                    swampSlab = rail;
                                    slab_data = column.getBlockData()[n][yy];
                                    change = false;
                                }
                            }
                            if (yy == 0 && n == 8 && !plugin.getPresetBuilder().no_block_under_door.contains(preset)) {
                                TARDISBlockSetters.setUnderDoorBlock(world, xx, (y - 1), zz, bd.getTardisID(), true);
                            }
                            Material mat = colData[yy].getMaterial();
                            switch (mat) {
                                case DIRT:
                                    BlockData subi = (preset.equals(PRESET.SUBMERGED)) ? cham_id : colData[yy];
                                    TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, subi, bd.getTardisID());
                                    break;
                                case WHITE_WOOL:
                                    Material flower = (preset.equals(PRESET.FLOWER)) ? random_colour : mat;
                                    TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, flower, bd.getTardisID());
                                    break;
                                case LIME_WOOL:
                                    Material party = (preset.equals(PRESET.PARTY)) ? random_colour : mat;
                                    TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, party, bd.getTardisID());
                                    break;
                                case BLUE_WOOL:
                                    BlockData old = ((preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) && adapt.equals(ADAPTION.BLOCK)) ? cham_id : colData[yy];
                                    TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, old, bd.getTardisID());
                                    break;
                                case TORCH: // lamps, glowstone and torches
                                case GLOWSTONE:
                                case REDSTONE_LAMP:
                                    BlockData light;
                                    if (bd.isSubmarine() && mat.equals(Material.TORCH)) {
                                        light = Material.GLOWSTONE.createBlockData();
                                    } else {
                                        light = (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) ? bd.getLamp().createBlockData() : colData[yy];
                                    }
                                    TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, light, bd.getTardisID());
                                    break;
                                case IRON_DOOR: // wood, iron & trap doors, rails
                                case RAIL:
                                case OAK_DOOR:
                                case BIRCH_DOOR:
                                case SPRUCE_DOOR:
                                case JUNGLE_DOOR:
                                case ACACIA_DOOR:
                                case DARK_OAK_DOOR:
                                case OAK_TRAPDOOR:
                                case BIRCH_TRAPDOOR:
                                case SPRUCE_TRAPDOOR:
                                case JUNGLE_TRAPDOOR:
                                case ACACIA_TRAPDOOR:
                                case DARK_OAK_TRAPDOOR:
                                    boolean door = false;
                                    if (Tag.DOORS.isTagged(mat)) {
                                        Bisected bisected = (Bisected) colData[yy];
                                        door = bisected.getHalf().equals(Bisected.Half.BOTTOM);
                                    }
                                    if (mat.equals(Material.OAK_TRAPDOOR) || mat.equals(Material.BIRCH_TRAPDOOR) || mat.equals(Material.SPRUCE_TRAPDOOR) || mat.equals(Material.JUNGLE_TRAPDOOR) || mat.equals(Material.ACACIA_TRAPDOOR) || mat.equals(Material.DARK_OAK_TRAPDOOR)) {
                                        door = true;
                                    }
                                    if (door) {
                                        // remember the door location
                                        String doorloc = world.getName() + ":" + xx + ":" + (y + yy) + ":" + zz;
                                        String doorStr = world.getBlockAt(xx, y + yy, zz).getLocation().toString();
                                        plugin.getGeneralKeeper().getProtectBlockMap().put(doorStr, bd.getTardisID());
                                        // should insert the door when tardis is first made, and then update location there after!
                                        HashMap<String, Object> whered = new HashMap<>();
                                        whered.put("door_type", 0);
                                        whered.put("tardis_id", bd.getTardisID());
                                        ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
                                        HashMap<String, Object> setd = new HashMap<>();
                                        setd.put("door_location", doorloc);
                                        setd.put("door_direction", bd.getDirection().toString());
                                        if (rsd.resultSet()) {
                                            HashMap<String, Object> whereid = new HashMap<>();
                                            whereid.put("door_id", rsd.getDoor_id());
                                            qf.doUpdate("doors", setd, whereid);
                                        } else {
                                            setd.put("tardis_id", bd.getTardisID());
                                            setd.put("door_type", 0);
                                            qf.doInsert("doors", setd);
                                        }
                                    } else {
                                        String doorStr = world.getBlockAt(xx, y + yy, zz).getLocation().toString();
                                        plugin.getGeneralKeeper().getProtectBlockMap().put(doorStr, bd.getTardisID());
                                    }
                                    if (yy == 0) {
                                        if (bd.isSubmarine() && plugin.isWorldGuardOnServer()) {
                                            int sy = y - 1;
                                            TARDISBlockSetters.setBlockAndRemember(world, xx, sy, zz, Material.SPONGE, bd.getTardisID());
                                            Block sponge = world.getBlockAt(xx, sy, zz);
                                            plugin.getWorldGuardUtils().sponge(sponge, true);
                                        } else if (!plugin.getPresetBuilder().no_block_under_door.contains(preset)) {
                                            TARDISBlockSetters.setUnderDoorBlock(world, xx, (y - 1), zz, bd.getTardisID(), false);
                                        }
                                    }
                                    if (preset.equals(PRESET.SWAMP) || preset.equals(PRESET.TOPSYTURVEY) || preset.equals(PRESET.JAIL)) {
                                        // do it at the end
                                        if (door) {
                                            swampDoorBottom = world.getBlockAt(xx, (y + yy), zz);
                                            sdb_data = colData[yy];
                                        } else {
                                            swampDoorTop = world.getBlockAt(xx, (y + yy), zz);
                                            sdt_data = colData[yy];
                                        }
                                    } else {
                                        TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, colData[yy], bd.getTardisID());
                                    }
                                    break;
                                case OAK_SIGN:
                                    if (preset.equals(PRESET.APPERTURE)) {
                                        TARDISBlockSetters.setUnderDoorBlock(world, xx, (y - 1), zz, bd.getTardisID(), false);
                                    }
                                    break;
                                case OAK_WALL_SIGN: // sign - if there is one
                                    if (preset.equals(PRESET.JUNK_MODE)) {
                                        // add a sign
                                        TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colData[yy]);
                                        // remember its location
                                        String location = new Location(world, xx, (y + yy), zz).toString();
                                        plugin.getGeneralKeeper().getProtectBlockMap().put(location, bd.getTardisID());
                                        saveJunkControl(location, "save_sign");
                                        // make it a save_sign
                                        Block sign = world.getBlockAt(xx, (y + yy), zz);
                                        if (sign.getType().equals(Material.OAK_WALL_SIGN)) {
                                            Sign s = (Sign) sign.getState();
                                            s.setLine(0, "TARDIS");
                                            s.setLine(1, plugin.getSigns().getStringList("saves").get(0));
                                            s.setLine(2, plugin.getSigns().getStringList("saves").get(1));
                                            s.setLine(3, "");
                                            s.update();
                                        }
                                    } else if (bd.shouldAddSign()) {
                                        TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colData[yy]);
                                        Block sign = world.getBlockAt(xx, (y + yy), zz);
                                        if (Tag.SIGNS.isTagged(sign.getType())) {
                                            Sign s = (Sign) sign.getState();
                                            plugin.getGeneralKeeper().getProtectBlockMap().put(sign.getLocation().toString(), bd.getTardisID());
                                            if (plugin.getConfig().getBoolean("police_box.name_tardis")) {
                                                HashMap<String, Object> wheret = new HashMap<>();
                                                wheret.put("tardis_id", bd.getTardisID());
                                                ResultSetTardis rst = new ResultSetTardis(plugin, wheret, "", false, 0);
                                                if (rst.resultSet()) {
                                                    Tardis tardis = rst.getTardis();
                                                    String player_name = TARDISStaticUtils.getNick(tardis.getUuid());
                                                    if (player_name == null) {
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
                                                case CONSTRUCT:
                                                    // get sign text from database
                                                    ResultSetConstructSign rscs = new ResultSetConstructSign(plugin, bd.getTardisID());
                                                    if (rscs.resultSet()) {
                                                        if (rscs.getLine1().isEmpty() && rscs.getLine2().isEmpty() && rscs.getLine3().isEmpty() && rscs.getLine4().isEmpty()) {
                                                            s.setLine(1, sign_colour + line1);
                                                            s.setLine(2, sign_colour + line2);
                                                        } else {
                                                            s.setLine(0, rscs.getLine1());
                                                            s.setLine(1, rscs.getLine2());
                                                            s.setLine(2, rscs.getLine3());
                                                            s.setLine(3, rscs.getLine4());
                                                        }
                                                    }
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
                                case LEVER:
                                    // remember this block and do at end
                                    if (preset.equals(PRESET.JUNK_MODE)) {
                                        // remember its location
                                        handbrake = world.getBlockAt(xx, (y + yy), zz);
                                        plugin.getGeneralKeeper().getProtectBlockMap().put(handbrake.getLocation().toString(), bd.getTardisID());
                                        h_data = colData[yy];
                                    }
                                    break;
                                case SKELETON_SKULL:
                                    if (bd.isSubmarine()) {
                                        TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, Material.GLOWSTONE);
                                    } else {
                                        Rotatable rotatable = (Rotatable) colData[yy];
                                        rotatable.setRotation(plugin.getPresetBuilder().getSkullDirection(bd.getDirection()));
                                        TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, rotatable, bd.getTardisID());
                                    }
                                    break;
                                case REDSTONE_BLOCK:
                                    if (!bd.getLamp().equals(Material.REDSTONE_LAMP) && (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD))) {
                                        TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, Material.BLUE_WOOL, bd.getTardisID());
                                    } else {
                                        TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, colData[yy], bd.getTardisID());
                                    }
                                    break;
                                case LIGHT_GRAY_TERRACOTTA:
                                    BlockData chai = (preset.equals(PRESET.FACTORY) && adapt.equals(ADAPTION.BIOME)) ? cham_id : colData[yy];
                                    TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, chai, bd.getTardisID());
                                    break;
                                default: // everything else
                                    if (change) {
                                        TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, colData[yy], bd.getTardisID());
                                    }
                                    break;
                            }
                        }
                    }
                }
                if (preset.equals(PRESET.JUNK_MODE) && plugin.getConfig().getBoolean("junk.particles")) {
                    // animate particles
                    plugin.getUtils().getJunkTravellers(bd.getLocation()).forEach((e) -> {
                        if (e instanceof Player) {
                            Player p = (Player) e;
                            Location effectsLoc = bd.getLocation().clone().add(0.5d, 0, 0.5d);
                            TARDISParticles.sendVortexParticles(effectsLoc, p);
                        }
                    });
                }
                // just change the walls
                int xx, zz;
                for (int n = 0; n < 9; n++) {
                    BlockData[] coldatas = datas[n];
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
                            if (rail.getType().equals(Material.RAIL) || rail.getType().equals(Material.POWERED_RAIL)) {
                                change = false;
                            }
                        }
                        Material mat = coldatas[yy].getMaterial();
                        switch (mat) {
                            case DIRT:
                                BlockData subi = (preset.equals(PRESET.SUBMERGED)) ? cham_id : coldatas[yy];
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, subi);
                                break;
                            case BEDROCK:
                                if (preset.equals(PRESET.THEEND) && i == loops) {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, coldatas[yy]);
                                    world.getBlockAt(xx, (y + yy + 1), zz).setBlockData(TARDISConstants.FIRE);
                                } else {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, coldatas[yy]);
                                }
                                break;
                            case ALLIUM:
                            case AZURE_BLUET:
                            case BLUE_ORCHID:
                            case DEAD_BUSH:
                            case FERN:
                            case LARGE_FERN:
                            case LILAC:
                            case ORANGE_TULIP:
                            case OXEYE_DAISY:
                            case PEONY:
                            case PINK_TULIP:
                            case POPPY:
                            case RED_TULIP:
                            case ROSE_BUSH:
                            case SUNFLOWER:
                            case TALL_GRASS:
                            case WHITE_TULIP:
                                if (i == loops && (preset.equals(PRESET.GRAVESTONE) || preset.equals(PRESET.MESA) || preset.equals(PRESET.PLAINS) || preset.equals(PRESET.TAIGA))) {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, coldatas[yy]);
                                } else {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, Material.AIR);
                                }
                                break;
                            case WHITE_WOOL:
                                Material flower = (preset.equals(PRESET.FLOWER)) ? random_colour : mat;
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, flower);
                                break;
                            case LIME_WOOL:
                                Material party = (preset.equals(PRESET.PARTY)) ? random_colour : mat;
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, party);
                                break;
                            case BLUE_WOOL:
                                if (adapt.equals(ADAPTION.OFF) && (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) && bd.shouldUseCTM() && n == TARDISStaticUtils.getCol(bd.getDirection()) && yy == 1 && plugin.getConfig().getBoolean("police_box.set_biome")) {
                                    // set an observer block instead
                                    Directional directional = (Directional) Material.OBSERVER.createBlockData();
                                    directional.setFacing(BlockFace.valueOf(bd.getDirection().toString()));
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, directional);
                                } else if (adapt.equals(ADAPTION.BLOCK) && (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD))) {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, cham_id);
                                } else {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, mat);
                                }
                                break;
                            case TORCH: // lamps, glowstone and torches
                            case GLOWSTONE:
                            case REDSTONE_LAMP:
                                Material light;
                                if (bd.isSubmarine() && mat.equals(Material.TORCH)) {
                                    light = Material.GLOWSTONE;
                                } else {
                                    light = (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) ? bd.getLamp() : mat;
                                }
                                BlockData lamp = light.createBlockData();
                                if (lamp instanceof Lightable) {
                                    Lightable lightable = (Lightable) lamp;
                                    lightable.setLit(true);
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, lightable);
                                } else {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, lamp);
                                }
                                break;
                            case IRON_DOOR: // wood, iron & trap doors
                            case OAK_DOOR:
                            case BIRCH_DOOR:
                            case SPRUCE_DOOR:
                            case JUNGLE_DOOR:
                            case ACACIA_DOOR:
                            case DARK_OAK_DOOR:
                            case OAK_TRAPDOOR:
                            case BIRCH_TRAPDOOR:
                            case SPRUCE_TRAPDOOR:
                            case JUNGLE_TRAPDOOR:
                            case ACACIA_TRAPDOOR:
                            case DARK_OAK_TRAPDOOR:
                                // don't change the door
                                break;
                            case LEVER:
                                // remember this block and do at end
                                if (preset.equals(PRESET.JUNK_MODE) || preset.equals(PRESET.TOILET)) {
                                    // remember its location
                                    handbrake = world.getBlockAt(xx, (y + yy), zz);
                                    h_data = coldatas[yy];
                                }
                                break;
                            case NETHERRACK:
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, coldatas[yy]);
                                if (preset.equals(PRESET.TORCH) && i == loops) {
                                    world.getBlockAt(xx, (y + yy + 1), zz).setBlockData(TARDISConstants.FIRE);
                                }
                                break;
                            case NETHER_PORTAL:
                                TARDISBlockSetters.setBlock(world, xx, (y + yy + 1), zz, Material.OBSIDIAN);
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, coldatas[yy]);
                                break;
                            case LIGHT_GRAY_STAINED_GLASS:
                                Material chag = (preset.equals(PRESET.FACTORY) && adapt.equals(ADAPTION.BIOME)) ? plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(cham_id.getMaterial()) : mat;
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chag);
                            case WHITE_STAINED_GLASS:
                                Material chaw = (preset.equals(PRESET.FLOWER)) ? random_glass : mat;
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chaw);
                                break;
                            case LIME_STAINED_GLASS:
                                Material chal = (preset.equals(PRESET.PARTY)) ? random_glass : mat;
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chal);
                                break;
                            case BLUE_STAINED_GLASS:
                                Material chad = ((preset.equals(PRESET.OLD) && adapt.equals(ADAPTION.BLOCK)) || preset.equals(PRESET.SUBMERGED)) ? plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(cham_id.getMaterial()) : mat;
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chad);
                                break;
                            case SKELETON_SKULL:
                                if (bd.isSubmarine()) {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, Material.GLOWSTONE);
                                } else {
                                    Rotatable rotatable = (Rotatable) coldatas[yy];
                                    rotatable.setRotation(plugin.getPresetBuilder().getSkullDirection(bd.getDirection()));
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, rotatable);
                                }
                                break;
                            case REDSTONE_BLOCK:
                                if (!bd.getLamp().equals(Material.REDSTONE_LAMP) && (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD))) {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, Material.BLUE_WOOL);
                                } else {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, coldatas[yy]);
                                }
                                break;
                            case LIGHT_GRAY_TERRACOTTA:
                                BlockData chai = (preset.equals(PRESET.FACTORY)) ? cham_id : coldatas[yy];
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chai);
                                break;
                            default: // everything else
                                if (change) {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, coldatas[yy]);
                                }
                                break;
                        }
                    }
                }
            } else {
                if ((preset.equals(PRESET.SWAMP) || preset.equals(PRESET.TOPSYTURVEY) || preset.equals(PRESET.JAIL)) && swampDoorBottom != null) {
                    TARDISBlockSetters.setBlockAndRemember(world, swampDoorBottom.getX(), swampDoorBottom.getY(), swampDoorBottom.getZ(), sdb_data, bd.getTardisID());
                    TARDISBlockSetters.setBlockAndRemember(world, swampDoorTop.getX(), swampDoorTop.getY(), swampDoorTop.getZ(), sdt_data, bd.getTardisID());
                    if (preset.equals(PRESET.SWAMP)) {
                        TARDISBlockSetters.setBlockAndRemember(world, swampSlab.getX(), swampSlab.getY(), swampSlab.getZ(), slab_data, bd.getTardisID());
                    }
                }
                if (preset.equals(PRESET.JUNK_MODE) || preset.equals(PRESET.TOILET)) {
                    handbrake.setBlockData(h_data);
                    // remember its location
                    String location = handbrake.getLocation().toString();
                    saveJunkControl(location, "handbrake");
                    // set handbrake to on ?
                }
                // just in case
                setBiome(world, x, z, bd.useTexture(), false);
                // remove trackers
                plugin.getTrackerKeeper().getMaterialising().removeAll(Collections.singleton(bd.getTardisID()));
                plugin.getTrackerKeeper().getInVortex().removeAll(Collections.singleton(bd.getTardisID()));
                plugin.getServer().getScheduler().cancelTask(task);
                task = 0;
                // tardis has moved so remove HADS damage count
                plugin.getTrackerKeeper().getDamage().remove(bd.getTardisID());
                plugin.getTrackerKeeper().getDestinationVortex().remove(bd.getTardisID());
                plugin.getTrackerKeeper().getMalfunction().remove(bd.getTardisID());
                if (plugin.getTrackerKeeper().getDidDematToVortex().contains(bd.getTardisID())) {
                    plugin.getTrackerKeeper().getDidDematToVortex().removeAll(Collections.singleton(bd.getTardisID()));
                }
                if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(bd.getTardisID())) {
                    int taskID = plugin.getTrackerKeeper().getDestinationVortex().get(bd.getTardisID());
                    plugin.getServer().getScheduler().cancelTask(taskID);
                }
                // message travellers in tardis
                if (loops > 3) {
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("tardis_id", bd.getTardisID());
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, where, true);
                    if (rst.resultSet()) {
                        List<UUID> travellers = rst.getData();
                        travellers.forEach((s) -> {
                            Player p = plugin.getServer().getPlayer(s);
                            if (p != null) {
                                String message = (bd.isMalfunction()) ? "MALFUNCTION" : "HANDBRAKE_LEFT_CLICK";
                                TARDISMessage.send(p, message);
                                // TARDIS has travelled so add players to list so they can receive Artron on exit
                                if (!plugin.getTrackerKeeper().getHasTravelled().contains(s)) {
                                    plugin.getTrackerKeeper().getHasTravelled().add(s);
                                }
                            }
                        });
                    } else if (plugin.getTrackerKeeper().getJunkPlayers().containsKey(bd.getPlayer().getUniqueId())) {
                        TARDISMessage.send(bd.getPlayer().getPlayer(), "JUNK_HANDBRAKE_LEFT_CLICK");
                    }
                    // restore beacon up block if present
                    HashMap<String, Object> whereb = new HashMap<>();
                    whereb.put("tardis_id", bd.getTardisID());
                    whereb.put("police_box", 2);
                    ResultSetBlocks rs = new ResultSetBlocks(plugin, whereb, false);
                    if (rs.resultSet()) {
                        ReplacedBlock rb = rs.getReplacedBlock();
                        TARDISBlockSetters.setBlock(rb.getLocation(), rb.getBlockData());
                        HashMap<String, Object> whered = new HashMap<>();
                        whered.put("tardis_id", bd.getTardisID());
                        whered.put("police_box", 2);
                        new QueryFactory(plugin).doDelete("blocks", whered);
                    }
                }
            }
        }
    }

    private void setBiome(World world, int x, int z, boolean pp, boolean umbrella) {
        if (plugin.getConfig().getBoolean("police_box.set_biome") && (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD) || preset.equals(PRESET.PANDORICA)) && pp) {
            List<Chunk> chunks = new ArrayList<>();
            Chunk chunk = bd.getLocation().getChunk();
            chunks.add(chunk);
            // load the chunk
            int cx = bd.getLocation().getBlockX() >> 4;
            int cz = bd.getLocation().getBlockZ() >> 4;
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
                    if (umbrella && TARDISConstants.NO_RAIN.contains(bd.getBiome())) {
                        // add an invisible roof
                        if (loops == 3) {
                            TARDISBlockSetters.setBlock(world, x + c, 255, z + r, Material.BARRIER);
                        } else {
                            TARDISBlockSetters.setBlockAndRemember(world, x + c, 255, z + r, Material.BARRIER, bd.getTardisID());
                        }
                    }
                    Chunk tmp_chunk = world.getChunkAt(new Location(world, x + c, 64, z + r));
                    if (!chunks.contains(tmp_chunk)) {
                        chunks.add(tmp_chunk);
                    }
                }
            }
            // refresh the chunks
            chunks.forEach((c) -> {
                plugin.getTardisHelper().refreshChunk(c);
            });
        }
    }

    private void saveJunkControl(String location, String field) {
        // remember control location
        HashMap<String, Object> wherej = new HashMap<>();
        wherej.put("tardis_id", bd.getTardisID());
        HashMap<String, Object> setj = new HashMap<>();
        setj.put(field, location);
        new QueryFactory(plugin).doUpdate("junk", setj, wherej);
    }

    public void setTask(int task) {
        this.task = task;
    }
}
