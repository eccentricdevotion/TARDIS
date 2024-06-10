/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.chameleon.construct.TARDISConstructColumn;
import me.eccentric_nz.TARDIS.chameleon.utils.TARDISChameleonColumn;
import me.eccentric_nz.TARDIS.chameleon.utils.TARDISStainedGlassLookup;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.data.ReplacedBlock;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.Adaption;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.move.TARDISDoorListener;
import me.eccentric_nz.TARDIS.particles.TARDISParticles;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISSponge;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import me.eccentric_nz.tardischunkgenerator.worldgen.TARDISChunkGenerator;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * A dematerialisation circuit was an essential part of a Type 40 TARDIS which enabled it to dematerialise from normal
 * space into the Time Vortex and rematerialise back from it.
 *
 * @author eccentric_nz
 */
class TARDISMaterialisePreset implements Runnable {

    private final TARDIS plugin;
    private final BuildData bd;
    private final int loops;
    private final ChameleonPreset preset;
    private final BlockData data;
    private final Adaption adapt;
    private final TARDISChameleonColumn column;
    private final TARDISChameleonColumn stained_column;
    private final TARDISChameleonColumn glass_column;
    private final Material random_colour;
    private final Material random_glass;
    private final ChatColor sign_colour;
    private int task;
    private int i;
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
     * @param plugin instance of the TARDIS plugin
     * @param bd     the Materialisation data
     * @param preset the preset to construct
     * @param data   the chameleon block data for the police box
     * @param adapt  the chameleon circuit adaption setting
     */
    TARDISMaterialisePreset(TARDIS plugin, BuildData bd, ChameleonPreset preset, BlockData data, Adaption adapt) {
        this.plugin = plugin;
        this.bd = bd;
        loops = this.bd.getThrottle().getLoops();
        i = 0;
        this.preset = preset;
        this.data = data;
        this.adapt = adapt;
        if (preset.equals(ChameleonPreset.ANGEL)) {
            plugin.getPresets().setR(TARDISConstants.RANDOM.nextInt(2));
        }
        if (this.preset.equals(ChameleonPreset.CONSTRUCT)) {
            column = new TARDISConstructColumn(plugin, bd.getTardisID(), "blueprintData", bd.getDirection().forPreset()).getColumn();
            stained_column = new TARDISConstructColumn(plugin, bd.getTardisID(), "stainData", bd.getDirection().forPreset()).getColumn();
            glass_column = new TARDISConstructColumn(plugin, bd.getTardisID(), "glassData", bd.getDirection().forPreset()).getColumn();
        } else {
            column = plugin.getPresets().getColumn(preset, bd.getDirection().forPreset());
            stained_column = plugin.getPresets().getStained(preset, bd.getDirection().forPreset());
            glass_column = plugin.getPresets().getGlass(preset, bd.getDirection().forPreset());
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
            if (column == null || stained_column == null || glass_column == null) {
                plugin.getServer().getScheduler().cancelTask(task);
                task = 0;
                plugin.getMessenger().sendColouredCommand(bd.getPlayer().getPlayer(), "INVALID_CONSTRUCT", "/tardistravel stop", plugin);
                // remove trackers
                plugin.getTrackerKeeper().getMaterialising().removeAll(Collections.singleton(bd.getTardisID()));
                plugin.getTrackerKeeper().getDematerialising().removeAll(Collections.singleton(bd.getTardisID()));
                plugin.getTrackerKeeper().getInVortex().removeAll(Collections.singleton(bd.getTardisID()));
                plugin.getTrackerKeeper().getDestinationVortex().remove(bd.getTardisID());
                return;
            }
            BlockData[][] datas;
            // get relative locations
            int x = bd.getLocation().getBlockX(), plusx = bd.getLocation().getBlockX() + 1, minusx = bd.getLocation().getBlockX() - 1, y;
            if (preset.equals(ChameleonPreset.SUBMERGED)) {
                y = bd.getLocation().getBlockY() - 1;
            } else {
                y = bd.getLocation().getBlockY();
            }
            int z = bd.getLocation().getBlockZ(), plusz = bd.getLocation().getBlockZ() + 1, minusz = bd.getLocation().getBlockZ() - 1;
            World world = bd.getLocation().getWorld();
            int signx = 0, signz = 0;
            boolean hasDodgyDoor = preset.equals(ChameleonPreset.SWAMP) || preset.equals(ChameleonPreset.TOPSYTURVEY) || preset.equals(ChameleonPreset.JAIL);
            boolean isJunkOrToilet = preset.equals(ChameleonPreset.JUNK_MODE) || preset.equals(ChameleonPreset.TOILET);
            if (i < loops) {
                i++;
                if (preset.equals(ChameleonPreset.JUNK_MODE)) {
                    datas = column.getBlockData();
                } else {
                    // determine preset to use
                    datas = switch (i % 3) {
                        case 2 -> stained_column.getBlockData(); // stained
                        case 1 -> glass_column.getBlockData(); // glass
                        default -> column.getBlockData(); // preset
                    };
                }
                // rescue player?
                if (i == bd.getThrottle().getRescue() && plugin.getTrackerKeeper().getRescue().containsKey(bd.getTardisID())) {
                    UUID playerUUID = plugin.getTrackerKeeper().getRescue().get(bd.getTardisID());
                    Player saved = plugin.getServer().getPlayer(playerUUID);
                    if (saved != null) {
                        TARDISDoorLocation idl = TARDISDoorListener.getDoor(1, bd.getTardisID());
                        Location l = idl.getL();
                        plugin.getGeneralKeeper().getDoorListener().movePlayer(saved, l, false, world, false, 0, bd.useMinecartSounds(), false);
                        TARDISSounds.playTARDISSound(saved, "tardis_land_fast", 5L);
                        // put player into travellers table
                        HashMap<String, Object> set = new HashMap<>();
                        set.put("tardis_id", bd.getTardisID());
                        set.put("uuid", playerUUID.toString());
                        plugin.getQueryFactory().doInsert("travellers", set);
                    }
                    plugin.getTrackerKeeper().getRescue().remove(bd.getTardisID());
                }
                // first run - remember blocks
                boolean isAdaptive = preset.equals(ChameleonPreset.ADAPTIVE);
                boolean isAdaptiveFactory = preset.equals(ChameleonPreset.FACTORY) && adapt.equals(Adaption.BIOME);
                boolean isJunk = preset.equals(ChameleonPreset.JUNK_MODE) || preset.equals(ChameleonPreset.JUNK);
                if (i == 1) {
                    if (bd.isOutside()) {
                        if (!bd.useMinecartSounds()) {
                            String sound;
                            if (preset.equals(ChameleonPreset.JUNK_MODE)) {
                                sound = "junk_land";
                            } else {
                                sound = switch (bd.getThrottle()) {
                                    case WARP, RAPID, FASTER ->
                                            "tardis_land_" + bd.getThrottle().toString().toLowerCase();
                                    default -> "tardis_land";
                                };
                            }
                            TARDISSounds.playTARDISSound(bd.getLocation(), sound);
                        } else {
                            world.playSound(bd.getLocation(), Sound.ENTITY_MINECART_INSIDE, 1.0F, 0.0F);
                        }
                    }
                    // get direction player is facing from yaw place block under door if block is in list of blocks an iron door cannot go on
                    switch (bd.getDirection().forPreset()) {
                        case SOUTH -> {
                            // if (yaw >= 315 || yaw < 45)
                            signx = x;
                            signz = (minusz - 1);
                        }
                        case EAST -> {
                            // if (yaw >= 225 && yaw < 315)
                            signx = (minusx - 1);
                            signz = z;
                        }
                        case NORTH -> {
                            // if (yaw >= 135 && yaw < 225)
                            signx = x;
                            signz = (plusz + 1);
                        }
                        // WEST
                        default -> {
                            // if (yaw >= 45 && yaw < 135)
                            signx = (plusx + 1);
                            signz = z;
                        }
                    }
                    int xx, zz;
                    for (int n = 0; n < 10; n++) {
                        BlockData[] colData = datas[n];
                        switch (n) {
                            case 0 -> {
                                xx = minusx;
                                zz = minusz;
                            }
                            case 1 -> {
                                xx = x;
                                zz = minusz;
                            }
                            case 2 -> {
                                xx = plusx;
                                zz = minusz;
                            }
                            case 3 -> {
                                xx = plusx;
                                zz = z;
                            }
                            case 4 -> {
                                xx = plusx;
                                zz = plusz;
                            }
                            case 5 -> {
                                xx = x;
                                zz = plusz;
                            }
                            case 6 -> {
                                xx = minusx;
                                zz = plusz;
                            }
                            case 7 -> {
                                xx = minusx;
                                zz = z;
                            }
                            case 8 -> {
                                xx = x;
                                zz = z;
                            }
                            default -> {
                                xx = signx;
                                zz = signz;
                            }
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
                                } else if (preset.equals(ChameleonPreset.SWAMP)) {
                                    swampSlab = rail;
                                    slab_data = column.getBlockData()[n][yy];
                                    change = false;
                                } else if (preset.equals(ChameleonPreset.SUBMERGED)) {
                                    change = false;
                                }
                                if (world.getEnvironment().equals(World.Environment.NETHER) || world.getEnvironment().equals(World.Environment.THE_END) || world.getGenerator() instanceof TARDISChunkGenerator) {
                                    TARDISBlockSetters.setUnderDoorBlock(world, xx, (y - 1), zz, bd.getTardisID(), false);
                                }
                            }
                            if (yy == 0 && n == 8 && !plugin.getPresetBuilder().no_block_under_door.contains(preset)) {
                                TARDISBlockSetters.setUnderDoorBlock(world, xx, (y - 1), zz, bd.getTardisID(), true);
                            }
                            if (preset.equals(ChameleonPreset.DUCK) && yy == 2 && n == 9) {
                                swampDoorBottom = world.getBlockAt(xx, (y + yy), zz);
                            }
                            Material mat = colData[yy].getMaterial();
                            switch (mat) {
                                case DIRT, GRASS_BLOCK -> {
                                    BlockData subi = (preset.equals(ChameleonPreset.SUBMERGED)) ? data : colData[yy];
                                    TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, subi, bd.getTardisID());
                                }
                                case WHITE_WOOL -> {
                                    Material flower = (preset.equals(ChameleonPreset.FLOWER)) ? random_colour : mat;
                                    TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, flower, bd.getTardisID());
                                }
                                case LIME_WOOL -> {
                                    Material party = (preset.equals(ChameleonPreset.PARTY)) ? random_colour : mat;
                                    TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, party, bd.getTardisID());
                                }
                                case BLUE_WOOL -> {
                                    BlockData old = (isAdaptive && adapt.equals(Adaption.BLOCK)) ? data : colData[yy];
                                    TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, old, bd.getTardisID());
                                }
                                case TORCH, GLOWSTONE, REDSTONE_LAMP -> { // lamps, glowstone and torches
                                    BlockData light;
                                    if (bd.isSubmarine() && mat.equals(Material.TORCH)) {
                                        light = Material.GLOWSTONE.createBlockData();
                                    } else {
                                        light = colData[yy];
                                    }
                                    TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, light, bd.getTardisID());
                                }
                                case IRON_DOOR, RAIL, ACACIA_DOOR, ACACIA_TRAPDOOR, BAMBOO_DOOR, BAMBOO_TRAPDOOR,
                                     BIRCH_DOOR, BIRCH_TRAPDOOR, CHERRY_DOOR, CHERRY_TRAPDOOR, CRIMSON_DOOR,
                                     CRIMSON_TRAPDOOR, DARK_OAK_DOOR, DARK_OAK_TRAPDOOR, JUNGLE_DOOR, JUNGLE_TRAPDOOR,
                                     MANGROVE_DOOR, MANGROVE_TRAPDOOR, OAK_DOOR, OAK_TRAPDOOR, SPRUCE_DOOR,
                                     SPRUCE_TRAPDOOR, WARPED_DOOR,
                                     WARPED_TRAPDOOR -> { // wood, iron & trap doors, rails
                                    boolean door = false;
                                    if (Tag.DOORS.isTagged(mat)) {
                                        Bisected bisected = (Bisected) colData[yy];
                                        door = bisected.getHalf().equals(Bisected.Half.BOTTOM);
                                    }
                                    if (Tag.TRAPDOORS.isTagged(mat)) {
                                        door = true;
                                    }
                                    Block doorBlock = world.getBlockAt(xx, y + yy, zz);
                                    if (door) {
                                        // remember the door location
                                        saveDoorLocation(world, xx, y, yy, zz);
                                        // add under door block as well
                                        String under = doorBlock.getRelative(BlockFace.DOWN).getLocation().toString();
                                        plugin.getGeneralKeeper().getProtectBlockMap().put(under, bd.getTardisID());
                                        TARDISBlockSetters.rememberBlock(world, xx, (y - 1), zz, bd.getTardisID());
                                    } else {
                                        String doorStr = doorBlock.getLocation().toString();
                                        plugin.getGeneralKeeper().getProtectBlockMap().put(doorStr, bd.getTardisID());
                                    }
                                    if (yy == 0) {
                                        if (bd.isSubmarine() && plugin.isWorldGuardOnServer()) {
                                            int sy = y - 1;
                                            TARDISBlockSetters.setBlockAndRemember(world, xx, sy, zz, Material.SPONGE, bd.getTardisID());
                                            Block sponge = world.getBlockAt(xx, sy, zz);
                                            TARDISSponge.removeWater(sponge);
                                        } else if (!plugin.getPresetBuilder().no_block_under_door.contains(preset)) {
                                            TARDISBlockSetters.setUnderDoorBlock(world, xx, (y - 1), zz, bd.getTardisID(), false);
                                        }
                                    }
                                    if (hasDodgyDoor) {
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
                                }
                                case ACACIA_SIGN, BIRCH_SIGN, CRIMSON_SIGN, DARK_OAK_SIGN, JUNGLE_SIGN, MANGROVE_SIGN,
                                     OAK_SIGN, SPRUCE_SIGN, WARPED_SIGN -> {
                                    if (preset.equals(ChameleonPreset.APPERTURE)) {
                                        TARDISBlockSetters.setUnderDoorBlock(world, xx, (y - 1), zz, bd.getTardisID(), false);
                                    }
                                }
                                case ACACIA_WALL_SIGN, BIRCH_WALL_SIGN, CRIMSON_WALL_SIGN, DARK_OAK_WALL_SIGN,
                                     JUNGLE_WALL_SIGN, MANGROVE_WALL_SIGN, OAK_WALL_SIGN, SPRUCE_WALL_SIGN,
                                     WARPED_WALL_SIGN -> {
                                    // sign - if there is one
                                    if (preset.equals(ChameleonPreset.JUNK_MODE)) {
                                        // add a sign
                                        TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colData[yy]);
                                        // remember its location
                                        String location = new Location(world, xx, (y + yy), zz).toString();
                                        plugin.getGeneralKeeper().getProtectBlockMap().put(location, bd.getTardisID());
                                        saveJunkControl(location, "save_sign");
                                        // make it a save_sign
                                        Block sign = world.getBlockAt(xx, (y + yy), zz);
                                        if (Tag.WALL_SIGNS.isTagged(sign.getType())) {
                                            Sign s = (Sign) sign.getState();
                                            SignSide front = s.getSide(Side.FRONT);
                                            front.setLine(0, "TARDIS");
                                            front.setLine(1, plugin.getSigns().getStringList("saves").get(0));
                                            front.setLine(2, plugin.getSigns().getStringList("saves").get(1));
                                            front.setLine(3, "");
                                            s.update();
                                        }
                                    } else if (bd.shouldAddSign()) {
                                        TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colData[yy]);
                                        Block sign = world.getBlockAt(xx, (y + yy), zz);
                                        if (Tag.SIGNS.isTagged(sign.getType())) {
                                            Sign s = (Sign) sign.getState();
                                            SignSide front = s.getSide(Side.FRONT);
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
                                                    if (preset.equals(ChameleonPreset.GRAVESTONE) || preset.equals(ChameleonPreset.PUNKED) || preset.equals(ChameleonPreset.ROBOT)) {
                                                        owner = (player_name.length() > 14) ? player_name.substring(0, 14) : player_name;
                                                    } else {
                                                        owner = (player_name.length() > 14) ? player_name.substring(0, 12) + "'s" : player_name + "'s";
                                                    }
                                                    switch (preset) {
                                                        case GRAVESTONE -> front.setLine(3, owner);
                                                        case ANGEL, JAIL -> front.setLine(2, owner);
                                                        default -> front.setLine(0, owner);
                                                    }
                                                }
                                            }
                                            String line1;
                                            String line2;
                                            if (preset.equals(ChameleonPreset.CUSTOM)) {
                                                line1 = plugin.getPresets().custom.getFirstLine();
                                                line2 = plugin.getPresets().custom.getSecondLine();
                                            } else {
                                                line1 = preset.getFirstLine();
                                                line2 = preset.getSecondLine();
                                            }
                                            switch (preset) {
                                                case ANGEL -> {
                                                    front.setLine(0, sign_colour + line1);
                                                    front.setLine(1, sign_colour + line2);
                                                    front.setLine(3, sign_colour + "TARDIS");
                                                }
                                                case APPERTURE -> {
                                                    front.setLine(1, sign_colour + line1);
                                                    front.setLine(2, sign_colour + line2);
                                                    front.setLine(3, sign_colour + "LAB");
                                                }
                                                case JAIL -> {
                                                    front.setLine(0, sign_colour + line1);
                                                    front.setLine(1, sign_colour + line2);
                                                    front.setLine(3, sign_colour + "CAPTURE");
                                                }
                                                case THEEND -> {
                                                    front.setLine(1, sign_colour + line1);
                                                    front.setLine(2, sign_colour + line2);
                                                    front.setLine(3, sign_colour + "HOT ROD");
                                                }
                                                case CONSTRUCT -> {
                                                    // get sign text from database
                                                    ResultSetConstructSign rscs = new ResultSetConstructSign(plugin, bd.getTardisID());
                                                    if (rscs.resultSet()) {
                                                        if (rscs.getLine1().isEmpty() && rscs.getLine2().isEmpty() && rscs.getLine3().isEmpty() && rscs.getLine4().isEmpty()) {
                                                            front.setLine(1, sign_colour + line1);
                                                            front.setLine(2, sign_colour + line2);
                                                        } else {
                                                            front.setLine(0, rscs.getLine1());
                                                            front.setLine(1, rscs.getLine2());
                                                            front.setLine(2, rscs.getLine3());
                                                            front.setLine(3, rscs.getLine4());
                                                        }
                                                    }
                                                }
                                                default -> {
                                                    front.setLine(1, sign_colour + line1);
                                                    front.setLine(2, sign_colour + line2);
                                                }
                                            }
                                            s.update();
                                        }
                                    }
                                }
                                case LEVER -> {
                                    // remember this block and do at end
                                    if (preset.equals(ChameleonPreset.JUNK_MODE)) {
                                        // remember its location
                                        handbrake = world.getBlockAt(xx, (y + yy), zz);
                                        plugin.getGeneralKeeper().getProtectBlockMap().put(handbrake.getLocation().toString(), bd.getTardisID());
                                        h_data = colData[yy];
                                    }
                                }
                                case SKELETON_SKULL -> {
                                    if (bd.isSubmarine()) {
                                        TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, Material.GLOWSTONE);
                                    } else {
                                        Rotatable rotatable = (Rotatable) colData[yy];
                                        rotatable.setRotation(plugin.getPresetBuilder().getSkullDirection(bd.getDirection().forPreset()));
                                        TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, rotatable, bd.getTardisID());
                                    }
                                }
                                case LIGHT_GRAY_TERRACOTTA -> {
                                    BlockData chai = isAdaptiveFactory ? data : colData[yy];
                                    TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, chai, bd.getTardisID());
                                }
                                default -> { // everything else
                                    if (change) {
                                        TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, colData[yy], bd.getTardisID());
                                    }
                                }
                            }
                        }
                    }
                }
                if (preset.equals(ChameleonPreset.JUNK_MODE) && plugin.getConfig().getBoolean("junk.particles")) {
                    // animate particles
                    plugin.getUtils().getJunkTravellers(bd.getLocation()).forEach((e) -> {
                        if (e instanceof Player p) {
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
                        case 0 -> {
                            xx = minusx;
                            zz = minusz;
                        }
                        case 1 -> {
                            xx = x;
                            zz = minusz;
                        }
                        case 2 -> {
                            xx = plusx;
                            zz = minusz;
                        }
                        case 3 -> {
                            xx = plusx;
                            zz = z;
                        }
                        case 4 -> {
                            xx = plusx;
                            zz = plusz;
                        }
                        case 5 -> {
                            xx = x;
                            zz = plusz;
                        }
                        case 6 -> {
                            xx = minusx;
                            zz = plusz;
                        }
                        case 7 -> {
                            xx = minusx;
                            zz = z;
                        }
                        case 8 -> {
                            xx = x;
                            zz = z;
                        }
                        default -> {
                            xx = signx;
                            zz = signz;
                        }
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
                            case DIRT, GRASS_BLOCK -> {
                                BlockData subi = (preset.equals(ChameleonPreset.SUBMERGED)) ? data : coldatas[yy];
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, subi);
                            }
                            case BEDROCK -> {
                                if (preset.equals(ChameleonPreset.THEEND) && i == loops) {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, coldatas[yy]);
                                    world.getBlockAt(xx, (y + yy + 1), zz).setBlockData(TARDISConstants.FIRE);
                                } else {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, coldatas[yy]);
                                }
                            }
                            case ACACIA_SAPLING, ALLIUM, AZURE_BLUET, BAMBOO_SAPLING, BEETROOTS, BIRCH_SAPLING,
                                 BLUE_ORCHID, CARROTS, CORNFLOWER, CRIMSON_FUNGUS, CRIMSON_ROOTS, DANDELION,
                                 DARK_OAK_SAPLING, DEAD_BUSH, FERN, SHORT_GRASS, JUNGLE_SAPLING, LARGE_FERN, LILAC,
                                 LILY_OF_THE_VALLEY, OAK_SAPLING, ORANGE_TULIP, OXEYE_DAISY, PEONY, PINK_TULIP, POPPY,
                                 POTATOES, RED_TULIP, ROSE_BUSH, SPRUCE_SAPLING, SUGAR_CANE, SUNFLOWER,
                                 SWEET_BERRY_BUSH, TALL_GRASS, WARPED_FUNGUS, WARPED_ROOTS, WHEAT, WHITE_TULIP,
                                 WITHER_ROSE -> {
                                if (i == loops && (preset.equals(ChameleonPreset.GRAVESTONE) || preset.equals(ChameleonPreset.MESA) || preset.equals(ChameleonPreset.PLAINS) || preset.equals(ChameleonPreset.TAIGA) || preset.equals(ChameleonPreset.CONSTRUCT) || preset.equals(ChameleonPreset.CUSTOM))) {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, coldatas[yy]);
                                } else {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, Material.AIR);
                                }
                            }
                            case WHITE_WOOL -> {
                                Material flower = (preset.equals(ChameleonPreset.FLOWER)) ? random_colour : mat;
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, flower);
                            }
                            case LIME_WOOL -> {
                                Material party = (preset.equals(ChameleonPreset.PARTY)) ? random_colour : mat;
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, party);
                            }
                            case BLUE_WOOL -> {
                                if (adapt.equals(Adaption.BLOCK) && (preset.equals(ChameleonPreset.ADAPTIVE) || preset.equals(ChameleonPreset.SUBMERGED))) {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, data);
                                } else {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, mat);
                                }
                            }
                            case TORCH, GLOWSTONE, REDSTONE_LAMP -> { // lamps, glowstone and torches
                                Material light;
                                if (bd.isSubmarine() && mat.equals(Material.TORCH)) {
                                    light = Material.GLOWSTONE;
                                } else {
                                    light = mat;
                                }
                                BlockData lamp = light.createBlockData();
                                if (lamp instanceof Lightable lightable) {
                                    lightable.setLit(true);
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, lightable);
                                } else {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, lamp);
                                }
                            }
                            case IRON_DOOR, ACACIA_DOOR, ACACIA_TRAPDOOR, BAMBOO_DOOR, BAMBOO_TRAPDOOR, BIRCH_DOOR,
                                 BIRCH_TRAPDOOR, CHERRY_DOOR, CHERRY_TRAPDOOR, CRIMSON_DOOR, CRIMSON_TRAPDOOR,
                                 DARK_OAK_DOOR, DARK_OAK_TRAPDOOR, JUNGLE_DOOR, JUNGLE_TRAPDOOR, MANGROVE_DOOR,
                                 MANGROVE_TRAPDOOR, OAK_DOOR, OAK_TRAPDOOR, SPRUCE_DOOR, SPRUCE_TRAPDOOR, WARPED_DOOR,
                                 WARPED_TRAPDOOR -> {
                                // don't change the door
                            }
                            case LEVER -> {
                                // remember this block and do at end
                                if (isJunkOrToilet) {
                                    // remember its location
                                    handbrake = world.getBlockAt(xx, (y + yy), zz);
                                    h_data = coldatas[yy];
                                }
                            }
                            case NETHERRACK -> {
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, coldatas[yy]);
                                if (preset.equals(ChameleonPreset.TORCH) && i == loops) {
                                    world.getBlockAt(xx, (y + yy + 1), zz).setBlockData(TARDISConstants.FIRE);
                                }
                            }
                            case NETHER_PORTAL -> {
                                TARDISBlockSetters.setBlock(world, xx, (y + yy + 1), zz, Material.OBSIDIAN);
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, coldatas[yy]);
                            }
                            case LIGHT_GRAY_STAINED_GLASS -> {
                                Material chag = isAdaptiveFactory ? TARDISStainedGlassLookup.stainedGlassFromMaterial(world, data.getMaterial()) : mat;
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chag);
                            }
                            case WHITE_STAINED_GLASS -> {
                                Material chaw = (preset.equals(ChameleonPreset.FLOWER)) ? random_glass : mat;
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chaw);
                            }
                            case LIME_STAINED_GLASS -> {
                                Material chal = (preset.equals(ChameleonPreset.PARTY)) ? random_glass : mat;
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chal);
                            }
                            case BLUE_STAINED_GLASS -> {
                                Material chad = ((preset.equals(ChameleonPreset.ADAPTIVE) && adapt.equals(Adaption.BLOCK)) || preset.equals(ChameleonPreset.SUBMERGED)) ? TARDISStainedGlassLookup.stainedGlassFromMaterial(world, data.getMaterial()) : mat;
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chad);
                            }
                            case SKELETON_SKULL -> {
                                if (bd.isSubmarine()) {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, Material.GLOWSTONE);
                                } else {
                                    Rotatable rotatable = (Rotatable) coldatas[yy];
                                    rotatable.setRotation(plugin.getPresetBuilder().getSkullDirection(bd.getDirection().forPreset()));
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, rotatable);
                                }
                            }
                            case LIGHT_GRAY_TERRACOTTA -> {
                                BlockData chai = (preset.equals(ChameleonPreset.FACTORY)) ? data : coldatas[yy];
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chai);
                            }
                            // everything else
                            default -> {
                                if (change) {
                                    if (isJunk && mat.equals(Material.ORANGE_WOOL) && i == loops) {
                                        TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, TARDISConstants.BARRIER);
                                        TARDISDisplayItemUtils.set(TARDISDisplayItem.HEXAGON, world, xx, (y + yy), zz);
                                    } else {
                                        TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, coldatas[yy]);
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (preset.equals(ChameleonPreset.DUCK) && swampDoorBottom != null) {
                    swampDoorBottom.setBlockData(column.getBlockData()[9][2]);
                }
                if ((hasDodgyDoor) && swampDoorBottom != null) {
                    TARDISBlockSetters.setBlockAndRemember(world, swampDoorBottom.getX(), swampDoorBottom.getY(), swampDoorBottom.getZ(), sdb_data, bd.getTardisID());
                    TARDISBlockSetters.setBlockAndRemember(world, swampDoorTop.getX(), swampDoorTop.getY(), swampDoorTop.getZ(), sdt_data, bd.getTardisID());
                    if (preset.equals(ChameleonPreset.SWAMP)) {
                        TARDISBlockSetters.setBlockAndRemember(world, swampSlab.getX(), swampSlab.getY(), swampSlab.getZ(), slab_data, bd.getTardisID());
                    }
                }
                if (isJunkOrToilet) {
                    handbrake.setBlockData(h_data);
                    // remember its location
                    String location = handbrake.getLocation().toString();
                    saveJunkControl(location, "handbrake");
                    // set handbrake to on ?
                }
                // remove trackers
                plugin.getTrackerKeeper().getMaterialising().removeAll(Collections.singleton(bd.getTardisID()));
                plugin.getTrackerKeeper().getInVortex().removeAll(Collections.singleton(bd.getTardisID()));
                plugin.getServer().getScheduler().cancelTask(task);
                task = 0;
                plugin.getTrackerKeeper().getMalfunction().remove(bd.getTardisID());
                if (plugin.getTrackerKeeper().getDidDematToVortex().contains(bd.getTardisID())) {
                    plugin.getTrackerKeeper().getDidDematToVortex().removeAll(Collections.singleton(bd.getTardisID()));
                }
                if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(bd.getTardisID())) {
                    int taskID = plugin.getTrackerKeeper().getDestinationVortex().get(bd.getTardisID());
                    plugin.getServer().getScheduler().cancelTask(taskID);
                    plugin.getTrackerKeeper().getDestinationVortex().remove(bd.getTardisID());
                }
                if (!bd.isRebuild() && bd.getPlayer() != null) {
                    plugin.getTrackerKeeper().getActiveForceFields().remove(bd.getPlayer().getPlayer().getUniqueId());
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
                                plugin.getMessenger().sendStatus(p, message);
                                // TARDIS has travelled so add players to list so they can receive Artron on exit
                                plugin.getTrackerKeeper().getHasTravelled().add(s);
                            }
                        });
                    } else if (plugin.getTrackerKeeper().getJunkPlayers().containsKey(bd.getPlayer().getUniqueId())) {
                        plugin.getMessenger().send(bd.getPlayer().getPlayer(), TardisModule.TARDIS, "JUNK_HANDBRAKE_LEFT_CLICK");
                    }
                    // restore beacon up block if present
                    HashMap<String, Object> whereb = new HashMap<>();
                    whereb.put("tardis_id", bd.getTardisID());
                    whereb.put("police_box", 2);
                    ResultSetBlocks rs = new ResultSetBlocks(plugin, whereb, false);
                    rs.resultSetAsync((hasResult, resultSetBlocks) -> {
                        if (hasResult) {
                            ReplacedBlock rb = resultSetBlocks.getReplacedBlock();
                            TARDISBlockSetters.setBlock(rb.getLocation(), rb.getBlockData());
                            HashMap<String, Object> whered = new HashMap<>();
                            whered.put("tardis_id", bd.getTardisID());
                            whered.put("police_box", 2);
                            plugin.getQueryFactory().doDelete("blocks", whered);
                        }
                    });
                    // tardis has moved so remove HADS damage count
                    plugin.getTrackerKeeper().getHadsDamage().remove(bd.getTardisID());
                    // update demat field in database
                    TARDISBuilderUtility.updateChameleonDemat(preset.toString(), bd.getTardisID());
                }
            }
        }
    }

    private void saveJunkControl(String location, String field) {
        // remember control location
        HashMap<String, Object> wherej = new HashMap<>();
        wherej.put("tardis_id", bd.getTardisID());
        HashMap<String, Object> setj = new HashMap<>();
        setj.put(field, location);
        plugin.getQueryFactory().doUpdate("junk", setj, wherej);
    }

    private void saveDoorLocation(World world, int xx, int y, int yy, int zz) {
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
        setd.put("door_direction", bd.getDirection().forPreset().toString());
        if (rsd.resultSet()) {
            HashMap<String, Object> whereid = new HashMap<>();
            whereid.put("door_id", rsd.getDoor_id());
            plugin.getQueryFactory().doUpdate("doors", setd, whereid);
        } else {
            setd.put("tardis_id", bd.getTardisID());
            setd.put("door_type", 0);
            plugin.getQueryFactory().doInsert("doors", setd);
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
