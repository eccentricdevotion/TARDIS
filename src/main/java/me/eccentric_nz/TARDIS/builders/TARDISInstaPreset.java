/*
 * Copyright (C) 2018 eccentric_nz
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

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonColumn;
import me.eccentric_nz.TARDIS.chameleon.TARDISConstructColumn;
import me.eccentric_nz.TARDIS.database.*;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * A police box is a telephone kiosk that can be used by members of the public wishing to get help from the police.
 * Early in the First Doctor's travels, the TARDIS assumed the exterior shape of a police box during a five-month
 * stopover in 1963 London. Due a malfunction in its chameleon circuit, the TARDIS became locked into that shape.
 *
 * @author eccentric_nz
 */
class TARDISInstaPreset {

    private final TARDIS plugin;
    private final BuildData bd;
    private final BlockData cham_id;
    private final boolean rebuild;
    private final PRESET preset;
    private TARDISChameleonColumn column;
    private final Material[] colours;
    private final Random rand;
    private final Material random_colour;
    private final ChatColor sign_colour;
    private final List<ProblemBlock> do_at_end = new ArrayList<>();
    private final List<Material> doors = Arrays.asList(Material.IRON_DOOR, Material.OAK_DOOR, Material.BIRCH_DOOR, Material.SPRUCE_DOOR, Material.JUNGLE_DOOR, Material.ACACIA_DOOR, Material.DARK_OAK_DOOR);

    public TARDISInstaPreset(TARDIS plugin, BuildData bd, PRESET preset, BlockData cham_id, boolean rebuild) {
        this.plugin = plugin;
        this.bd = bd;
        this.preset = preset;
        this.cham_id = cham_id;
        this.rebuild = rebuild;
        colours = new Material[]{Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.MAGENTA_WOOL, Material.LIGHT_BLUE_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL, Material.PINK_WOOL, Material.CYAN_WOOL, Material.PURPLE_WOOL, Material.BLUE_WOOL, Material.BROWN_WOOL, Material.GREEN_WOOL, Material.RED_WOOL};
        rand = new Random();
        random_colour = colours[rand.nextInt(13)];
        sign_colour = plugin.getUtils().getSignColour();
    }

    /**
     * Builds the TARDIS Preset.
     */
    public void buildPreset() {
        if (preset.equals(PRESET.ANGEL)) {
            plugin.getPresets().setR(rand.nextInt(2));
        }
        if (preset.equals(PRESET.CONSTRUCT)) {
            column = new TARDISConstructColumn(plugin, bd.getTardisID(), "blueprint", bd.getDirection()).getColumn();
        } else {
            column = plugin.getPresets().getColumn(preset, bd.getDirection());
        }
        int plusx, minusx, x, plusz, y, minusz, z;
        // get relative locations
        x = bd.getLocation().getBlockX();
        plusx = (bd.getLocation().getBlockX() + 1);
        minusx = (bd.getLocation().getBlockX() - 1);
        if (preset.equals(PRESET.SUBMERGED)) {
            y = bd.getLocation().getBlockY() - 1;
        } else {
            y = bd.getLocation().getBlockY();
        }
        z = (bd.getLocation().getBlockZ());
        plusz = (bd.getLocation().getBlockZ() + 1);
        minusz = (bd.getLocation().getBlockZ() - 1);
        World world = bd.getLocation().getWorld();
        int signx = 0, signz = 0;
        QueryFactory qf = new QueryFactory(plugin);
        // if configured and it's a Whovian preset set biome
        if (plugin.getConfig().getBoolean("police_box.set_biome") && (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD) || preset.equals(PRESET.PANDORICA)) && bd.useTexture()) {
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
                    // TODO check re-adding umbrella if rebuilding
                    if (TARDISConstants.NO_RAIN.contains(bd.getBiome())) {
                        // add an invisible roof
                        plugin.getBlockUtils().setBlockAndRemember(world, x + c, 255, z + r, Material.BARRIER, bd.getTardisID());
                    }
                    Chunk tmp_chunk = world.getChunkAt(new Location(world, x + c, 64, z + r));
                    if (!chunks.contains(tmp_chunk)) {
                        chunks.add(tmp_chunk);
                    }
                }
            }
            // refresh the chunks
            chunks.forEach((c) -> {
                //world.refreshChunk(c.getX(), c.getZ());
                plugin.getTardisHelper().refreshChunk(c);
            });
        }
        // rescue player?
        if (plugin.getTrackerKeeper().getRescue().containsKey(bd.getTardisID())) {
            UUID playerUUID = plugin.getTrackerKeeper().getRescue().get(bd.getTardisID());
            Player saved = plugin.getServer().getPlayer(playerUUID);
            if (saved != null) {
                TARDISDoorLocation idl = plugin.getGeneralKeeper().getDoorListener().getDoor(1, bd.getTardisID());
                Location l = idl.getL();
                plugin.getGeneralKeeper().getDoorListener().movePlayer(saved, l, false, world, false, 0, bd.useMinecartSounds());
                // put player into travellers table
                HashMap<String, Object> set = new HashMap<>();
                set.put("tardis_id", bd.getTardisID());
                set.put("uuid", playerUUID.toString());
                qf.doInsert("travellers", set);
            }
            plugin.getTrackerKeeper().getRescue().remove(bd.getTardisID());
        }
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
        BlockData[][] data = column.getBlockData();
        for (int i = 0; i < 10; i++) {
            BlockData[] colData = data[i];
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
                boolean change = true;
                if (yy == 0 && i == 9) {
                    Block rail = world.getBlockAt(xx, y, zz);
                    if (rail.getType().equals(Material.RAIL) || rail.getType().equals(Material.POWERED_RAIL)) {
                        change = false;
                        plugin.getBlockUtils().setBlockAndRemember(world, xx, y, zz, rail.getBlockData(), bd.getTardisID());
                    }
                }
                if (yy == 0 && i == 8 && !plugin.getPresetBuilder().no_block_under_door.contains(preset)) {
                    plugin.getBlockUtils().setUnderDoorBlock(world, xx, (y - 1), zz, bd.getTardisID(), true);
                }
                Material mat = colData[yy].getMaterial();
                // update door location if invisible
                if (yy == 0 && (i == 1 || i == 3 || i == 5 || i == 7) && preset.equals(PRESET.INVISIBLE) && mat.equals(Material.AIR)) {
                    String invisible_door = world.getName() + ":" + xx + ":" + y + ":" + zz;
                    processDoor(invisible_door, qf);
                    // if tardis is in the air add under door
                    plugin.getBlockUtils().setUnderDoorBlock(world, xx, (y - 1), zz, bd.getTardisID(), true);
                }
                switch (mat) {
                    case GRASS_BLOCK:
                    case DIRT:
                        BlockData subi = (preset.equals(PRESET.SUBMERGED)) ? cham_id : colData[yy];
                        plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, subi, bd.getTardisID());
                        break;
                    case BEDROCK:
                        if (preset.equals(PRESET.THEEND)) {
                            plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, colData[yy], bd.getTardisID());
                            world.getBlockAt(xx, (y + yy + 1), zz).setBlockData(TARDISConstants.FIRE);
                        } else {
                            plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, colData[yy], bd.getTardisID());
                        }
                        break;
                    case WHITE_WOOL:
                    case ORANGE_WOOL:
                    case MAGENTA_WOOL:
                    case LIGHT_BLUE_WOOL:
                    case YELLOW_WOOL:
                    case LIME_WOOL:
                    case PINK_WOOL:
                    case GRAY_WOOL:
                    case LIGHT_GRAY_WOOL:
                    case CYAN_WOOL:
                    case PURPLE_WOOL:
                    case BLUE_WOOL:
                    case BROWN_WOOL:
                    case GREEN_WOOL:
                    case RED_WOOL:
                    case BLACK_WOOL:
                        if (preset.equals(PRESET.PARTY) || (preset.equals(PRESET.FLOWER) && mat.equals(Material.WHITE_WOOL))) {
                            plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, random_colour, bd.getTardisID());
                        }
                        if (bd.shouldUseCTM() && i == TARDISStaticUtils.getCol(bd.getDirection()) && yy == 1 && (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) && plugin.getConfig().getBoolean("police_box.set_biome")) {
                            // set a quartz pillar block instead
                            Orientable orientable = (Orientable) Material.QUARTZ_PILLAR.createBlockData();
                            Axis axis = (bd.getDirection().equals(COMPASS.EAST) || bd.getDirection().equals(COMPASS.WEST)) ? Axis.X : Axis.Z;
                            orientable.setAxis(axis);
                            plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, orientable, bd.getTardisID());
                        }
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
                        if (mat.equals(Material.TORCH)) {
                            do_at_end.add(new ProblemBlock(new Location(world, xx, (y + yy), zz), light));
                        } else {
                            plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, light, bd.getTardisID());
                        }
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
                            door = bisected.getHalf().equals(Half.BOTTOM);
                        }
                        if (mat.equals(Material.OAK_TRAPDOOR) || mat.equals(Material.BIRCH_TRAPDOOR) || mat.equals(Material.SPRUCE_TRAPDOOR) || mat.equals(Material.JUNGLE_TRAPDOOR) || mat.equals(Material.ACACIA_TRAPDOOR) || mat.equals(Material.DARK_OAK_TRAPDOOR)) {
                            door = true;
                        }
                        if (door) {
                            // remember the door location
                            String doorloc = world.getName() + ":" + xx + ":" + (y + yy) + ":" + zz;
                            String doorStr = world.getBlockAt(xx, y + yy, zz).getLocation().toString();
                            plugin.getGeneralKeeper().getProtectBlockMap().put(doorStr, bd.getTardisID());
                            processDoor(doorloc, qf);
                            // place block under door if block is in list of blocks an iron door cannot go on
                            if (yy == 0) {
                                if (bd.isSubmarine() && plugin.isWorldGuardOnServer()) {
                                    int sy = y - 1;
                                    plugin.getBlockUtils().setBlockAndRemember(world, xx, sy, zz, Material.SPONGE, bd.getTardisID());
                                    Block sponge = world.getBlockAt(xx, sy, zz);
                                    plugin.getWorldGuardUtils().sponge(sponge, true);
                                } else if (!plugin.getPresetBuilder().no_block_under_door.contains(preset)) {
                                    plugin.getBlockUtils().setUnderDoorBlock(world, xx, (y - 1), zz, bd.getTardisID(), false);
                                }
                            }
                        } else {
                            String doorStr = world.getBlockAt(xx, y + yy, zz).getLocation().toString();
                            plugin.getGeneralKeeper().getProtectBlockMap().put(doorStr, bd.getTardisID());
                        }
                        if (mat.equals(Material.RAIL)) {
                            do_at_end.add(new ProblemBlock(new Location(world, xx, (y + yy), zz), colData[yy]));
                        } else {
                            plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, colData[yy], bd.getTardisID());
                        }
                        break;
                    case SIGN:
                        if (preset.equals(PRESET.APPERTURE)) {
                            plugin.getBlockUtils().setUnderDoorBlock(world, xx, (y - 1), zz, bd.getTardisID(), false);
                        }
                        break;
                    case WALL_SIGN: // sign - if there is one
                        if (bd.shouldAddSign()) {
                            TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colData[yy]);
                            Block sign = world.getBlockAt(xx, (y + yy), zz);
                            plugin.getGeneralKeeper().getProtectBlockMap().put(sign.getLocation().toString(), bd.getTardisID());
                            if (sign.getType().equals(Material.WALL_SIGN) || sign.getType().equals(Material.SIGN)) {
                                Sign s = (Sign) sign.getState();
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
                                // TODO renable custom preset
//                                if (preset.equals(PRESET.CUSTOM)) {
//                                    line1 = plugin.getPresets().custom.getFirstLine();
//                                    line2 = plugin.getPresets().custom.getSecondLine();
//                                } else {
                                line1 = preset.getFirstLine();
                                line2 = preset.getSecondLine();
//                                }
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
                                    default:
                                        s.setLine(1, sign_colour + line1);
                                        s.setLine(2, sign_colour + line2);
                                        break;
                                }
                                s.update();
                            }
                        }
                        break;
                    case NETHERRACK:
                        plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, colData[yy], bd.getTardisID());
                        if (preset.equals(PRESET.TORCH)) {
                            world.getBlockAt(xx, (y + yy + 1), zz).setBlockData(TARDISConstants.FIRE);
                        }
                        break;
                    case NETHER_PORTAL:
                        plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy + 1), zz, Material.OBSIDIAN, bd.getTardisID());
                        plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, colData[yy], bd.getTardisID());
                        break;
                    case SKELETON_SKULL:
                        if (bd.isSubmarine()) {
                            TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, Material.GLOWSTONE);
                        } else {
                            Rotatable rotatable = (Rotatable) colData[yy];
                            rotatable.setRotation(plugin.getPresetBuilder().getSkullDirection(bd.getDirection()));
                            plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, rotatable, bd.getTardisID());
                        }
                        break;
                    case REDSTONE_BLOCK:
                        if (!bd.getLamp().equals(Material.REDSTONE_LAMP) && (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD))) {
                            plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, Material.BLUE_WOOL, bd.getTardisID());
                        } else {
                            plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, colData[yy], bd.getTardisID());
                        }
                        break;
                    case WHITE_TERRACOTTA:
                    case ORANGE_TERRACOTTA:
                    case MAGENTA_TERRACOTTA:
                    case LIGHT_BLUE_TERRACOTTA:
                    case YELLOW_TERRACOTTA:
                    case LIME_TERRACOTTA:
                    case PINK_TERRACOTTA:
                    case GRAY_TERRACOTTA:
                    case LIGHT_GRAY_TERRACOTTA:
                    case CYAN_TERRACOTTA:
                    case PURPLE_TERRACOTTA:
                    case BLUE_TERRACOTTA:
                    case BROWN_TERRACOTTA:
                    case GREEN_TERRACOTTA:
                    case RED_TERRACOTTA:
                    case BLACK_TERRACOTTA:
                        BlockData chai = (preset.equals(PRESET.FACTORY)) ? cham_id : colData[yy];
                        plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, chai, bd.getTardisID());
                        break;
                    default: // everything else
                        if (change) {
                            if (mat.equals(Material.LEVER) || mat.equals(Material.STONE_BUTTON) || mat.equals(Material.OAK_BUTTON)) {
                                do_at_end.add(new ProblemBlock(new Location(world, xx, (y + yy), zz), colData[yy]));
                            } else {
                                plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, colData[yy], bd.getTardisID());
                            }
                        }
                        break;
                }
            }
        }
        do_at_end.forEach((pb) -> plugin.getBlockUtils().setBlockAndRemember(pb.getL().getWorld(), pb.getL().getBlockX(), pb.getL().getBlockY(), pb.getL().getBlockZ(), pb.getData(), bd.getTardisID()));
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
                        TARDISMessage.send(trav, message);
                        // TARDIS has travelled so add players to list so they can receive Artron on exit
                        if (!plugin.getTrackerKeeper().getHasTravelled().contains(s)) {
                            plugin.getTrackerKeeper().getHasTravelled().add(s);
                        }
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
    }

    private void processDoor(String doorloc, QueryFactory qf) {
        // should insert the door when tardis is first made, and then update the location there after!
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
    }

    private static class ProblemBlock {

        final Location l;
        final BlockData data;

        ProblemBlock(Location l, BlockData data) {
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
