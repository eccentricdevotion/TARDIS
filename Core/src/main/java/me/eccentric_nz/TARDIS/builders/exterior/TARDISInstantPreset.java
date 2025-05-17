/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.builders.exterior;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.chameleon.construct.TARDISConstructColumn;
import me.eccentric_nz.TARDIS.chameleon.utils.TARDISChameleonColumn;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetConstructSign;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.move.TARDISDoorListener;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISSponge;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import me.eccentric_nz.tardischunkgenerator.worldgen.TARDISChunkGenerator;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * A police box is a telephone kiosk that can be used by members of the public
 * wishing to get help from the police. Early in the First Doctor's travels, the
 * TARDIS assumed the exterior shape of a police box during a five-month
 * stopover in 1963 London. Due to a malfunction in its chameleon circuit, the
 * TARDIS became locked into that shape.
 *
 * @author eccentric_nz
 */
public class TARDISInstantPreset {

    private final TARDIS plugin;
    private final BuildData bd;
    private final BlockData chameleonBlockData;
    private final boolean rebuild;
    private final ChameleonPreset preset;
    private final Material random_colour;
    private final ChatColor sign_colour;
    private final List<ProblemBlock> do_at_end = new ArrayList<>();

    public TARDISInstantPreset(TARDIS plugin, BuildData bd, ChameleonPreset preset, BlockData chameleonBlockData, boolean rebuild) {
        this.plugin = plugin;
        this.bd = bd;
        this.preset = preset;
        this.chameleonBlockData = chameleonBlockData;
        this.rebuild = rebuild;
        Material[] colours = new Material[]{Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.MAGENTA_WOOL, Material.LIGHT_BLUE_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL, Material.PINK_WOOL, Material.CYAN_WOOL, Material.PURPLE_WOOL, Material.BLUE_WOOL, Material.BROWN_WOOL, Material.GREEN_WOOL, Material.RED_WOOL};
        random_colour = colours[TARDISConstants.RANDOM.nextInt(13)];
        sign_colour = plugin.getUtils().getSignColour();
    }

    /**
     * Builds the TARDIS Preset.
     */
    public void buildPreset() {
        if (preset.equals(ChameleonPreset.ANGEL)) {
            plugin.getPresets().setR(TARDISConstants.RANDOM.nextInt(2));
        }
        TARDISChameleonColumn column;
        if (preset.equals(ChameleonPreset.CONSTRUCT)) {
            column = new TARDISConstructColumn(plugin, bd.getTardisID(), "blueprintData", bd.getDirection().forPreset()).getColumn();
            if (column == null) {
                if (bd.getPlayer().getPlayer() != null) {
                    plugin.getMessenger().sendColouredCommand(bd.getPlayer().getPlayer(), "INVALID_CONSTRUCT", "/tardistravel stop", plugin);
                }
                // remove trackers
                plugin.getTrackerKeeper().getMaterialising().removeAll(Collections.singleton(bd.getTardisID()));
                plugin.getTrackerKeeper().getDematerialising().removeAll(Collections.singleton(bd.getTardisID()));
                plugin.getTrackerKeeper().getInVortex().removeAll(Collections.singleton(bd.getTardisID()));
                plugin.getTrackerKeeper().getDestinationVortex().remove(bd.getTardisID());
                return;
            }
        } else {
            column = plugin.getPresets().getColumn(preset, bd.getDirection().forPreset());
        }
        int plusx, minusx, x, plusz, y, minusz, z;
        // get relative locations
        x = bd.getLocation().getBlockX();
        plusx = (bd.getLocation().getBlockX() + 1);
        minusx = (bd.getLocation().getBlockX() - 1);
        if (preset.equals(ChameleonPreset.SUBMERGED)) {
            y = bd.getLocation().getBlockY() - 1;
        } else {
            y = bd.getLocation().getBlockY();
        }
        z = (bd.getLocation().getBlockZ());
        plusz = (bd.getLocation().getBlockZ() + 1);
        minusz = (bd.getLocation().getBlockZ() - 1);
        World world = bd.getLocation().getWorld();
        int signx, signz;
        // rescue player?
        if (plugin.getTrackerKeeper().getRescue().containsKey(bd.getTardisID())) {
            UUID playerUUID = plugin.getTrackerKeeper().getRescue().get(bd.getTardisID());
            Player saved = plugin.getServer().getPlayer(playerUUID);
            if (saved != null) {
                TARDISDoorLocation idl = TARDISDoorListener.getDoor(1, bd.getTardisID());
                Location l = idl.getL();
                plugin.getGeneralKeeper().getDoorListener().movePlayer(saved, l, false, world, false, 0, bd.useMinecartSounds(), false);
                // put player into travellers table
                HashMap<String, Object> set = new HashMap<>();
                set.put("tardis_id", bd.getTardisID());
                set.put("uuid", playerUUID.toString());
                plugin.getQueryFactory().doInsert("travellers", set);
            }
            plugin.getTrackerKeeper().getRescue().remove(bd.getTardisID());
        }
        switch (bd.getDirection().forPreset()) {
            case SOUTH -> {
                //if (yaw >= 315 || yaw < 45)
                signx = x;
                signz = (minusz - 1);
            }
            case EAST -> {
                //if (yaw >= 225 && yaw < 315)
                signx = (minusx - 1);
                signz = z;
            }
            case NORTH -> {
                //if (yaw >= 135 && yaw < 225)
                signx = x;
                signz = (plusz + 1);
            }
            // WEST
            default -> {
                //if (yaw >= 45 && yaw < 135)
                signx = (plusx + 1);
                signz = z;
            }
        }
        int xx, zz;
        BlockData[][] data = column.blockData();
        for (int i = 0; i < 10; i++) {
            BlockData[] colData = data[i];
            switch (i) {
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
                if (yy == 0 && i == 9) {
                    Block rail = world.getBlockAt(xx, y, zz);
                    if (rail.getType().equals(Material.RAIL) || rail.getType().equals(Material.POWERED_RAIL)) {
                        change = false;
                        TARDISBlockSetters.setBlockAndRemember(world, xx, y, zz, rail.getBlockData(), bd.getTardisID());
                    }
                    if (world.getEnvironment().equals(World.Environment.NETHER) || world.getEnvironment().equals(World.Environment.THE_END) || world.getGenerator() instanceof TARDISChunkGenerator) {
                        TARDISBlockSetters.setUnderDoorBlock(world, xx, (y - 1), zz, bd.getTardisID(), false);
                    }
                }
                if (yy == 0 && i == 8 && !plugin.getPresetBuilder().no_block_under_door.contains(preset)) {
                    TARDISBlockSetters.setUnderDoorBlock(world, xx, (y - 1), zz, bd.getTardisID(), true);
                }
                Material mat = colData[yy].getMaterial();
                // update door location if invisible
                if (yy == 0 && (i == 1 || i == 3 || i == 5 || i == 7) && preset.equals(ChameleonPreset.INVISIBLE) && mat.isAir()) {
                    String invisible_door = world.getName() + ":" + xx + ":" + y + ":" + zz;
                    processDoor(invisible_door);
                    // if tardis is in the air add under door
                    TARDISBlockSetters.setUnderDoorBlock(world, xx, (y - 1), zz, bd.getTardisID(), true);
                }
                if (Tag.STANDING_SIGNS.isTagged(mat) &&preset.equals(ChameleonPreset.APPERTURE)) {
                    TARDISBlockSetters.setUnderDoorBlock(world, xx, (y - 1), zz, bd.getTardisID(), false);
                } else if (Tag.WALL_SIGNS.isTagged(mat)) {
                    // sign - if there is one
                    if (preset.equals(ChameleonPreset.JUNK_MODE)) {
                        // add a sign
                        TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colData[yy]);
                        // remember its location
                        String location = new Location(world, xx, (y + yy), zz).toString();
                        plugin.getGeneralKeeper().getProtectBlockMap().put(location, bd.getTardisID());
                        TARDISMaterialisePreset.saveJunkControl(location, "save_sign", bd.getTardisID());
                        // make it a save_sign
                        Block sign = world.getBlockAt(xx, (y + yy), zz);
                        if (Tag.WALL_SIGNS.isTagged(sign.getType())) {
                            Sign s = (Sign) sign.getState();
                            SignSide front = s.getSide(Side.FRONT);
                            SignSide back = s.getSide(Side.BACK);
                            front.setLine(0, "TARDIS");
                            front.setLine(1, plugin.getSigns().getStringList("saves").getFirst());
                            front.setLine(2, plugin.getSigns().getStringList("saves").get(1));
                            front.setLine(3, "");
                            back.setLine(0, "TARDIS");
                            back.setLine(1, plugin.getSigns().getStringList("saves").getFirst());
                            back.setLine(2, plugin.getSigns().getStringList("saves").get(1));
                            back.setLine(3, "");
                            s.setWaxed(true);
                            s.update();
                        }
                    } else if (bd.shouldAddSign()) {
                        TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colData[yy]);
                        Block sign = world.getBlockAt(xx, (y + yy), zz);
                        plugin.getGeneralKeeper().getProtectBlockMap().put(sign.getLocation().toString(), bd.getTardisID());
                        if (Tag.SIGNS.isTagged(sign.getType())) {
                            Sign s = (Sign) sign.getState();
                            SignSide front = s.getSide(Side.FRONT);
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
                                    if (plugin.getServer().getPluginManager().getPlugin("Essentials") != null) {
                                        Essentials essentials = (Essentials) plugin.getServer().getPluginManager().getPlugin("Essentials");
                                        User user = essentials.getUser(tardis.getUuid());
                                        player_name = ChatColor.stripColor(user.getNick(false));
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
                            s.setWaxed(true);
                            s.update();
                        }
                    }
                } else if (Tag.DOORS.isTagged(mat) || Tag.TRAPDOORS.isTagged(mat) || mat == Material.RAIL || (Tag.BUTTONS.isTagged(mat) && preset.equals(ChameleonPreset.CONSTRUCT))) {
                    // wood, iron & trap doors, rails
                    boolean door = false;
                    if (Tag.DOORS.isTagged(mat)) {
                        Bisected bisected = (Bisected) colData[yy];
                        door = bisected.getHalf().equals(Half.BOTTOM);
                    }
                    if (Tag.TRAPDOORS.isTagged(mat) || Tag.BUTTONS.isTagged(mat)) {
                        door = true;
                    }
                    if (door) {
                        // remember the door location
                        String doorloc = world.getName() + ":" + xx + ":" + (y + yy) + ":" + zz;
                        Block doorBlock = world.getBlockAt(xx, y + yy, zz);
                        String doorStr = doorBlock.getLocation().toString();
                        plugin.getGeneralKeeper().getProtectBlockMap().put(doorStr, bd.getTardisID());
                        if (!Tag.BUTTONS.isTagged(mat)) {
                            // also remember the under door block
                            String under = doorBlock.getRelative(BlockFace.DOWN).getLocation().toString();
                            plugin.getGeneralKeeper().getProtectBlockMap().put(under, bd.getTardisID());
                            TARDISBlockSetters.rememberBlock(world, xx, (y - 1), zz, bd.getTardisID());
                        }
                        // save the door location in the database
                        processDoor(doorloc);
                        // place block under door if block is in list of blocks an iron door cannot go on
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
                    } else {
                        String doorStr = world.getBlockAt(xx, y + yy, zz).getLocation().toString();
                        plugin.getGeneralKeeper().getProtectBlockMap().put(doorStr, bd.getTardisID());
                    }
                    if (mat.equals(Material.RAIL)) {
                        do_at_end.add(new ProblemBlock(new Location(world, xx, (y + yy), zz), colData[yy]));
                    } else {
                        TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, colData[yy], bd.getTardisID());
                    }
                } else {
                    switch (mat) {
                        case GRASS_BLOCK, DIRT -> {
                            BlockData subi = (preset.equals(ChameleonPreset.SUBMERGED)) ? chameleonBlockData : colData[yy];
                            TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, subi, bd.getTardisID());
                        }
                        case BEDROCK -> {
                            if (preset.equals(ChameleonPreset.THEEND)) {
                                TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, colData[yy], bd.getTardisID());
                                world.getBlockAt(xx, (y + yy + 1), zz).setBlockData(TARDISConstants.FIRE);
                            } else {
                                TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, colData[yy], bd.getTardisID());
                            }
                        }
                        case WHITE_WOOL, ORANGE_WOOL, MAGENTA_WOOL, LIGHT_BLUE_WOOL, YELLOW_WOOL, LIME_WOOL, PINK_WOOL, GRAY_WOOL, LIGHT_GRAY_WOOL, CYAN_WOOL, PURPLE_WOOL, BLUE_WOOL, BROWN_WOOL, GREEN_WOOL, RED_WOOL, BLACK_WOOL -> {
                            if (preset.equals(ChameleonPreset.PARTY) || (preset.equals(ChameleonPreset.FLOWER) && mat.equals(Material.WHITE_WOOL))) {
                                TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, random_colour, bd.getTardisID());
                            }
                            if ((preset.equals(ChameleonPreset.JUNK_MODE) || preset.equals(ChameleonPreset.JUNK)) && mat.equals(Material.ORANGE_WOOL)) {
                                TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, TARDISConstants.BARRIER, bd.getTardisID());
                                TARDISDisplayItemUtils.set(TARDISDisplayItem.HEXAGON, world, xx, (y + yy), zz);
                            }
                        }
                        case TORCH, GLOWSTONE, REDSTONE_LAMP -> { // lamps, glowstone and torches
                            BlockData light;
                            if (bd.isSubmarine() && mat.equals(Material.TORCH)) {
                                light = Material.GLOWSTONE.createBlockData();
                            } else {
                                light = colData[yy];
                            }
                            if (mat.equals(Material.TORCH)) {
                                do_at_end.add(new ProblemBlock(new Location(world, xx, (y + yy), zz), light));
                            } else {
                                if (light instanceof Lightable lit) {
                                    lit.setLit(true);
                                    TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, lit, bd.getTardisID());
                                } else {
                                    TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, light, bd.getTardisID());
                                }
                            }
                        }
                        case NETHERRACK -> {
                            TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, colData[yy], bd.getTardisID());
                            if (preset.equals(ChameleonPreset.TORCH)) {
                                world.getBlockAt(xx, (y + yy + 1), zz).setBlockData(TARDISConstants.FIRE);
                            }
                        }
                        case NETHER_PORTAL -> {
                            TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy + 1), zz, Material.OBSIDIAN, bd.getTardisID());
                            TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, colData[yy], bd.getTardisID());
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
                        case WHITE_TERRACOTTA, ORANGE_TERRACOTTA, MAGENTA_TERRACOTTA, LIGHT_BLUE_TERRACOTTA,
                             YELLOW_TERRACOTTA, LIME_TERRACOTTA, PINK_TERRACOTTA, GRAY_TERRACOTTA,
                             LIGHT_GRAY_TERRACOTTA, CYAN_TERRACOTTA, PURPLE_TERRACOTTA, BLUE_TERRACOTTA,
                             BROWN_TERRACOTTA, GREEN_TERRACOTTA, RED_TERRACOTTA, BLACK_TERRACOTTA -> {
                            BlockData chai = (preset.equals(ChameleonPreset.FACTORY)) ? chameleonBlockData : colData[yy];
                            TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, chai, bd.getTardisID());
                        }
                        default -> { // everything else
                            if (change) {
                                if (mat.equals(Material.LEVER) || mat.equals(Material.STONE_BUTTON) || mat.equals(Material.OAK_BUTTON)) {
                                    do_at_end.add(new ProblemBlock(new Location(world, xx, (y + yy), zz), colData[yy]));
                                } else {
                                    TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, colData[yy], bd.getTardisID());
                                }
                            }
                        }
                    }
                }
            }
        }
        do_at_end.forEach((pb) -> TARDISBlockSetters.setBlockAndRemember(pb.getL().getWorld(), pb.getL().getBlockX(), pb.getL().getBlockY(), pb.getL().getBlockZ(), pb.getData(), bd.getTardisID()));
        if (!rebuild) {
            // message travellers in tardis
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", bd.getTardisID());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, where, true);
            if (rst.resultSet()) {
                List<UUID> travellers = rst.getData();
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> travellers.forEach((s) -> {
                    Player trav = plugin.getServer().getPlayer(s);
                    if (trav != null) {
                        String message = (bd.isMalfunction()) ? "MALFUNCTION" : "HANDBRAKE_LEFT_CLICK";
                        plugin.getMessenger().sendStatus(trav, message);
                        // TARDIS has travelled so add players to list so they can receive Artron on exit
                        plugin.getTrackerKeeper().getHasTravelled().add(s);
                    }
                }), 30L);
            }
        }
        plugin.getTrackerKeeper().getMaterialising().removeAll(Collections.singleton(bd.getTardisID()));
        plugin.getTrackerKeeper().getDematerialising().removeAll(Collections.singleton(bd.getTardisID()));
        plugin.getTrackerKeeper().getInVortex().removeAll(Collections.singleton(bd.getTardisID()));
        plugin.getTrackerKeeper().getDestinationVortex().remove(bd.getTardisID());
        if (plugin.getTrackerKeeper().getDidDematToVortex().contains(bd.getTardisID())) {
            plugin.getTrackerKeeper().getDidDematToVortex().removeAll(Collections.singleton(bd.getTardisID()));
        }
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(bd.getTardisID())) {
            int taskID = plugin.getTrackerKeeper().getDestinationVortex().get(bd.getTardisID());
            plugin.getServer().getScheduler().cancelTask(taskID);
        }
        if (!bd.isRebuild() && bd.getPlayer().getPlayer() != null) {
            plugin.getTrackerKeeper().getActiveForceFields().remove(bd.getPlayer().getPlayer().getUniqueId());
        }
    }

    private void processDoor(String doorloc) {
        // should insert the door when tardis is first made, and then update the location thereafter!
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

    public static class ProblemBlock {

        final Location l;
        final BlockData data;

        public ProblemBlock(Location l, BlockData data) {
            this.l = l;
            this.data = data;
        }

        Location getL() {
            return l;
        }

        BlockData getData() {
            return data;
        }
    }
}
