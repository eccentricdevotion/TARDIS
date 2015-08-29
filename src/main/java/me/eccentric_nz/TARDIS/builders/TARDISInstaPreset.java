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
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonColumn;
import me.eccentric_nz.TARDIS.chameleon.TARDISConstructColumn;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;

/**
 * A police box is a telephone kiosk that can be used by members of the public
 * wishing to get help from the police. Early in the First Doctor's travels, the
 * TARDIS assumed the exterior shape of a police box during a five-month
 * stopover in 1963 London. Due a malfunction in its chameleon circuit, the
 * TARDIS became locked into that shape.
 *
 * @author eccentric_nz
 */
public class TARDISInstaPreset {

    private final TARDIS plugin;
    private final TARDISMaterialisationData tmd;
    private final int lamp;
    private final int cham_id;
    private final byte cham_data;
    private final boolean rebuild;
    private final boolean minecart;
    private final boolean ctm;
    private final boolean add_sign;
    private Block sponge;
    private final PRESET preset;
    private TARDISChameleonColumn column;
    private final byte[] colours;
    private final Random rand;
    private final byte random_colour;
    private final ChatColor sign_colour;
    private final List<ProblemBlock> do_at_end = new ArrayList<ProblemBlock>();
    private final List<Integer> doors = Arrays.asList(64, 71, 193, 194, 195, 196, 197);

    public TARDISInstaPreset(TARDIS plugin, TARDISMaterialisationData tmd, PRESET preset, int lamp, int cham_id, byte cham_data, boolean rebuild, boolean minecart, boolean ctm, boolean add_sign) {
        this.plugin = plugin;
        this.tmd = tmd;
        this.preset = preset;
        this.lamp = lamp;
        this.cham_id = cham_id;
        this.cham_data = cham_data;
        this.rebuild = rebuild;
        this.minecart = minecart;
        this.ctm = ctm;
        this.add_sign = add_sign;
        colours = new byte[]{0, 1, 2, 3, 4, 5, 6, 9, 10, 11, 12, 13, 14};
        rand = new Random();
        random_colour = colours[rand.nextInt(13)];
        this.sign_colour = plugin.getUtils().getSignColour();
    }

    /**
     * Builds the TARDIS Preset.
     */
    public void buildPreset() {
        if (preset.equals(PRESET.ANGEL)) {
            plugin.getPresets().setR(rand.nextInt(2));
        }
        if (preset.equals(PRESET.CONSTRUCT)) {
            column = new TARDISConstructColumn(plugin, tmd.getTardisID(), "blueprint", tmd.getDirection()).getColumn();
        } else {
            column = plugin.getPresets().getColumn(preset, tmd.getDirection());
        }
        int plusx, minusx, x, plusz, y, minusz, z, platform_id = plugin.getConfig().getInt("police_box.platform_id");
        byte platform_data = (byte) plugin.getConfig().getInt("police_box.platform_data");
        // get relative locations
        x = tmd.getLocation().getBlockX();
        plusx = (tmd.getLocation().getBlockX() + 1);
        minusx = (tmd.getLocation().getBlockX() - 1);
        if (preset.equals(PRESET.SUBMERGED)) {
            y = tmd.getLocation().getBlockY() - 1;
        } else {
            y = tmd.getLocation().getBlockY();
        }
        z = (tmd.getLocation().getBlockZ());
        plusz = (tmd.getLocation().getBlockZ() + 1);
        minusz = (tmd.getLocation().getBlockZ() - 1);
        final World world = tmd.getLocation().getWorld();
        int signx = 0, signz = 0;
        QueryFactory qf = new QueryFactory(plugin);
        // if configured and it's a Whovian preset set biome
        if (plugin.getConfig().getBoolean("police_box.set_biome") && (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD) || preset.equals(PRESET.PANDORICA))) {
            List<Chunk> chunks = new ArrayList<Chunk>();
            Chunk chunk = tmd.getLocation().getChunk();
            chunks.add(chunk);
            // load the chunk
            while (!chunk.isLoaded()) {
                world.loadChunk(chunk);
            }
            // set the biome
            for (int c = -1; c < 2; c++) {
                for (int r = -1; r < 2; r++) {
                    world.setBiome(x + c, z + r, Biome.DEEP_OCEAN);
                    // TODO check re-adding umbrella if rebuilding
                    if (TARDISConstants.NO_RAIN.contains(tmd.getBiome())) {
                        // add a glass roof
                        plugin.getBlockUtils().setBlockAndRemember(world, x + c, 255, z + r, 20, (byte) 0, tmd.getTardisID());
                    }
                    Chunk tmp_chunk = world.getChunkAt(new Location(world, x + c, 64, z + r));
                    if (!chunks.contains(tmp_chunk)) {
                        chunks.add(tmp_chunk);
                    }
                }
            }
            // refresh the chunks
            for (Chunk c : chunks) {
                world.refreshChunk(c.getX(), c.getZ());
            }
        }
        // rescue player?
        if (plugin.getTrackerKeeper().getRescue().containsKey(tmd.getTardisID())) {
            UUID playerUUID = plugin.getTrackerKeeper().getRescue().get(tmd.getTardisID());
            Player saved = plugin.getServer().getPlayer(playerUUID);
            if (saved != null) {
                TARDISDoorLocation idl = plugin.getGeneralKeeper().getDoorListener().getDoor(1, tmd.getTardisID());
                Location l = idl.getL();
                plugin.getGeneralKeeper().getDoorListener().movePlayer(saved, l, false, world, false, 0, minecart);
                // put player into travellers table
                HashMap<String, Object> set = new HashMap<String, Object>();
                set.put("tardis_id", tmd.getTardisID());
                set.put("uuid", playerUUID.toString());
                qf.doInsert("travellers", set);
            }
            plugin.getTrackerKeeper().getRescue().remove(tmd.getTardisID());
        }
        switch (tmd.getDirection()) {
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
        int[][] ids = column.getId();
        byte[][] datas = column.getData();
        for (int i = 0; i < 10; i++) {
            int[] colids = ids[i];
            byte[] coldatas = datas[i];
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
                    if (rail.getType().equals(Material.RAILS) || rail.getType().equals(Material.POWERED_RAIL)) {
                        change = false;
                        plugin.getBlockUtils().setBlockAndRemember(world, xx, y, zz, rail.getTypeId(), rail.getData(), tmd.getTardisID());
                    }
                }
                if (yy == 0 && i == 8 && !plugin.getPresetBuilder().no_block_under_door.contains(preset)) {
                    plugin.getBlockUtils().setUnderDoorBlock(world, xx, (y - 1), zz, platform_id, platform_data, tmd.getTardisID(), true);
                }
                // update door location if invisible
                if (yy == 0 && (i == 1 || i == 3 || i == 5 || i == 7) && preset.equals(PRESET.INVISIBLE) && colids[yy] == 0) {
                    String invisible_door = world.getName() + ":" + xx + ":" + y + ":" + zz;
                    processDoor(invisible_door, qf);
                }
                switch (colids[yy]) {
                    case 2:
                    case 3:
                        int subi = (preset.equals(PRESET.SUBMERGED)) ? cham_id : colids[yy];
                        byte subd = (preset.equals(PRESET.SUBMERGED)) ? cham_data : coldatas[yy];
                        plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, subi, subd, tmd.getTardisID());
                        break;
                    case 7:
                        if (preset.equals(PRESET.THEEND)) {
                            plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, 7, (byte) 5, tmd.getTardisID());
                            world.getBlockAt(xx, (y + yy + 1), zz).setType(Material.FIRE);
                        } else {
                            plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], coldatas[yy], tmd.getTardisID());
                        }
                        break;
                    case 35:
                        int chai = (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) ? cham_id : colids[yy];
                        byte chad = (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) ? cham_data : coldatas[yy];
                        if (preset.equals(PRESET.PARTY) || (preset.equals(PRESET.FLOWER) && coldatas[yy] == 0)) {
                            chad = random_colour;
                        }
                        if (ctm && i == TARDISStaticUtils.getCol(tmd.getDirection()) && yy == 1 && cham_id == 35 && (cham_data == (byte) 11 || cham_data == (byte) 3) && (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) && plugin.getConfig().getBoolean("police_box.set_biome")) {
                            // set a quartz pillar block instead
                            byte pillar = (tmd.getDirection().equals(COMPASS.EAST) || tmd.getDirection().equals(COMPASS.WEST)) ? (byte) 3 : (byte) 4;
                            plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, 155, pillar, tmd.getTardisID());
                        } else {
                            plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, chai, chad, tmd.getTardisID());
                        }
                        break;
                    case 50: // lamps, glowstone and torches
                    case 89:
                    case 124:
                        int light;
                        byte ld;
                        if (tmd.isSubmarine() && colids[yy] == 50) {
                            light = 89;
                            ld = 0;
                        } else {
                            light = (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) ? lamp : colids[yy];
                            ld = coldatas[yy];
                        }
                        if (colids[yy] == 50) {
                            do_at_end.add(new ProblemBlock(new Location(world, xx, (y + yy), zz), light, ld));
                        } else {
                            plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, light, ld, tmd.getTardisID());
                        }
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
                                processDoor(doorloc, qf);
                            }
                            // place block under door if block is in list of blocks an iron door cannot go on
                            if (yy == 0) {
                                if (tmd.isSubmarine() && plugin.isWorldGuardOnServer()) {
                                    int sy = y - 1;
                                    plugin.getBlockUtils().setBlockAndRemember(world, xx, sy, zz, 19, (byte) 0, tmd.getTardisID());
                                    sponge = world.getBlockAt(xx, sy, zz);
                                    plugin.getWorldGuardUtils().sponge(sponge, true);
                                } else if (!plugin.getPresetBuilder().no_block_under_door.contains(preset)) {
                                    plugin.getBlockUtils().setUnderDoorBlock(world, xx, (y - 1), zz, platform_id, platform_data, tmd.getTardisID(), false);
                                }
                            }
                        }
                        if (doors.contains(colids[yy]) && coldatas[yy] > 8) {
                            plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], tmd.getDirection().getUpperData(), tmd.getTardisID());
                        } else {
                            if (colids[yy] == 66) {
                                do_at_end.add(new ProblemBlock(new Location(world, xx, (y + yy), zz), colids[yy], coldatas[yy]));
                            } else {
                                plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], coldatas[yy], tmd.getTardisID());
                            }
                        }
                        break;
                    case 63:
                        if (preset.equals(PRESET.APPERTURE)) {
                            plugin.getBlockUtils().setUnderDoorBlock(world, xx, (y - 1), zz, platform_id, platform_data, tmd.getTardisID(), false);
                        }
                        break;
                    case 68: // sign - if there is one
                        if (add_sign) {
                            TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                            Block sign = world.getBlockAt(xx, (y + yy), zz);
                            if (sign.getType().equals(Material.WALL_SIGN) || sign.getType().equals(Material.SIGN_POST)) {
                                Sign s = (Sign) sign.getState();
                                if (plugin.getConfig().getBoolean("police_box.name_tardis")) {
                                    HashMap<String, Object> wheret = new HashMap<String, Object>();
                                    wheret.put("tardis_id", tmd.getTardisID());
                                    ResultSetTardis rst = new ResultSetTardis(plugin, wheret, "", false);
                                    if (rst.resultSet()) {
                                        String player_name = plugin.getGeneralKeeper().getUUIDCache().getNameCache().get(rst.getUuid());
                                        if (player_name == null) {
                                            // cache lookup failed, player may have disconnected
                                            player_name = rst.getOwner();
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
                                    default:
                                        s.setLine(1, sign_colour + line1);
                                        s.setLine(2, sign_colour + line2);
                                        break;
                                }
                                s.update();
                            }
                        }
                        break;
                    case 87:
                        plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], coldatas[yy], tmd.getTardisID());
                        if (preset.equals(PRESET.TORCH)) {
                            world.getBlockAt(xx, (y + yy + 1), zz).setType(Material.FIRE);
                        }
                        break;
                    case 90:
                        plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy + 1), zz, 49, (byte) 0, tmd.getTardisID());
                        plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], coldatas[yy], tmd.getTardisID());
                        break;
                    case 144:
                        if (tmd.isSubmarine()) {
                            TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, 89, (byte) 0);
                        } else {
                            plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], coldatas[yy], tmd.getTardisID());
                            Skull skull = (Skull) world.getBlockAt(xx, (y + yy), zz).getState();
                            skull.setRotation(plugin.getPresetBuilder().getSkullDirection(tmd.getDirection()));
                            skull.update();
                        }
                        break;
                    case 152:
                        if (lamp != 123 && (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD))) {
                            plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, cham_id, cham_data, tmd.getTardisID());
                        } else {
                            plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], coldatas[yy], tmd.getTardisID());
                        }
                        break;
                    default: // everything else
                        if (change) {
                            if (colids[yy] == 69 || colids[yy] == 77 || colids[yy] == 143) {
                                do_at_end.add(new ProblemBlock(new Location(world, xx, (y + yy), zz), colids[yy], coldatas[yy]));
                            } else {
                                plugin.getBlockUtils().setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], coldatas[yy], tmd.getTardisID());
                            }
                        }
                        break;
                }
            }
        }
        for (ProblemBlock pb : do_at_end) {
            plugin.getBlockUtils().setBlockAndRemember(pb.getL().getWorld(), pb.getL().getBlockX(), pb.getL().getBlockY(), pb.getL().getBlockZ(), pb.getId(), pb.getData(), tmd.getTardisID());
        }
        if (!rebuild) {
            // message travellers in tardis
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("tardis_id", tmd.getTardisID());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, where, true);
            if (rst.resultSet()) {
                final List<UUID> travellers = rst.getData();
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        for (UUID s : travellers) {
                            Player trav = plugin.getServer().getPlayer(s);
                            if (trav != null) {
                                String message = (tmd.isMalfunction()) ? "MALFUNCTIO." : "HANDBRAKE_LEFT_CLICK";
                                TARDISMessage.send(trav, message);
                                // TARDIS has travelled so add players to list so they can receive Artron on exit
                                if (!plugin.getTrackerKeeper().getHasTravelled().contains(s)) {
                                    plugin.getTrackerKeeper().getHasTravelled().add(s);
                                }
                            }
                        }
                    }
                }, 30L);
            }
        }
        plugin.getTrackerKeeper().getMaterialising().remove(Integer.valueOf(tmd.getTardisID()));
        plugin.getTrackerKeeper().getInVortex().remove(Integer.valueOf(tmd.getTardisID()));
    }

    private void processDoor(String doorloc, QueryFactory qf) {
        // should insert the door when tardis is first made, and then update the location there after!
        HashMap<String, Object> whered = new HashMap<String, Object>();
        whered.put("door_type", 0);
        whered.put("tardis_id", tmd.getTardisID());
        ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
        HashMap<String, Object> setd = new HashMap<String, Object>();
        setd.put("door_location", doorloc);
        setd.put("door_direction", tmd.getDirection().toString());
        if (rsd.resultSet()) {
            HashMap<String, Object> whereid = new HashMap<String, Object>();
            whereid.put("door_id", rsd.getDoor_id());
            qf.doUpdate("doors", setd, whereid);
        } else {
            setd.put("tardis_id", tmd.getTardisID());
            setd.put("door_type", 0);
            qf.doInsert("doors", setd);
        }
    }

    private class ProblemBlock {

        Location l;
        int id;
        byte data;

        public ProblemBlock(Location l, int id, byte data) {
            this.l = l;
            this.id = id;
            this.data = data;
        }

        public Location getL() {
            return l;
        }

        public int getId() {
            return id;
        }

        public byte getData() {
            return data;
        }
    }
}
