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
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
    private final COMPASS d;
    private final Location location;
    private final int tid;
    private final String uuid;
    private final boolean mal;
    private final int lamp;
    private final boolean sub;
    private final int cham_id;
    private final byte cham_data;
    private final boolean rebuild;
    private final boolean minecart;
    private Block sponge;
    private final PRESET preset;
    private TARDISChameleonColumn column;
    private final byte[] colours;
    private final Random rand;
    private final byte random_colour;
    private final ChatColor sign_colour;
    private final List<ProblemBlock> do_at_end = new ArrayList<ProblemBlock>();

    public TARDISInstaPreset(TARDIS plugin, Location location, PRESET preset, int tid, COMPASS d, String uuid, boolean mal, int lamp, boolean sub, int cham_id, byte cham_data, boolean rebuild, boolean minecart) {
        this.plugin = plugin;
        this.d = d;
        this.location = location;
        this.preset = preset;
        this.tid = tid;
        this.uuid = uuid;
        this.mal = mal;
        this.lamp = lamp;
        this.sub = sub;
        this.cham_id = cham_id;
        this.cham_data = cham_data;
        this.rebuild = rebuild;
        this.minecart = minecart;
        colours = new byte[]{0, 1, 2, 3, 4, 5, 6, 9, 10, 11, 12, 13, 14};
        rand = new Random();
        random_colour = colours[rand.nextInt(13)];
        this.sign_colour = getSignColour();
    }

    /**
     * Builds the TARDIS Preset.
     */
    public void buildPreset() {
        if (preset.equals(PRESET.ANGEL)) {
            plugin.getPresets().setR(rand.nextInt(2));
        }
        column = plugin.getPresets().getColumn(preset, d);
        int plusx, minusx, x, plusz, y, minusz, z, platform_id = plugin.getConfig().getInt("police_box.platform_id");
        byte platform_data = (byte) plugin.getConfig().getInt("police_box.platform_data");
        // get relative locations
        x = location.getBlockX();
        plusx = (location.getBlockX() + 1);
        minusx = (location.getBlockX() - 1);
        if (preset.equals(PRESET.SUBMERGED)) {
            y = location.getBlockY() - 1;
        } else {
            y = location.getBlockY();
        }
        z = (location.getBlockZ());
        plusz = (location.getBlockZ() + 1);
        minusz = (location.getBlockZ() - 1);
        final World world = location.getWorld();
        int signx = 0, signz = 0;
        QueryFactory qf = new QueryFactory(plugin);
        // if configured and it's a Police Box preset set biome
        if (plugin.getConfig().getBoolean("police_box.set_biome") && (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD))) {
            // load the chunk
            Chunk chunk = location.getChunk();
            while (!chunk.isLoaded()) {
                world.loadChunk(chunk);
            }
            // set the biome
            for (int c = -1; c < 2; c++) {
                for (int r = -1; r < 2; r++) {
                    world.setBiome(x + c, z + r, Biome.SKY);
                }
            }
            // refresh the chunk
            world.refreshChunk(chunk.getX(), chunk.getZ());
        }
        // rescue player?
        if (plugin.getTrackerKeeper().getTrackRescue().containsKey(tid)) {
            UUID playerUUID = plugin.getTrackerKeeper().getTrackRescue().get(tid);
            Player saved = plugin.getServer().getPlayer(playerUUID);
            if (saved != null) {
                TARDISDoorLocation idl = plugin.getGeneralKeeper().getDoorListener().getDoor(1, tid);
                Location l = idl.getL();
                plugin.getGeneralKeeper().getDoorListener().movePlayer(saved, l, false, world, false, 0, minecart);
                // put player into travellers table
                HashMap<String, Object> set = new HashMap<String, Object>();
                set.put("tardis_id", tid);
                set.put("uuid", playerUUID.toString());
                qf.doInsert("travellers", set);
            }
            plugin.getTrackerKeeper().getTrackRescue().remove(tid);
        }
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
                boolean change = true;
                if (yy == 0 && i == 9) {
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
                    case 7:
                        if (preset.equals(PRESET.THEEND)) {
                            plugin.getUtils().setBlockAndRemember(world, xx, (y + yy), zz, 7, (byte) 5, tid);
                            world.getBlockAt(xx, (y + yy + 1), zz).setType(Material.FIRE);
                        } else {
                            plugin.getUtils().setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], coldatas[yy], tid);
                        }
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
                        if (colids[yy] == 50) {
                            do_at_end.add(new ProblemBlock(new Location(world, xx, (y + yy), zz), light, ld));
                        } else {
                            plugin.getUtils().setBlockAndRemember(world, xx, (y + yy), zz, light, ld, tid);
                        }
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
                            // place block under door if block is in list of blocks an iron door cannot go on
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
                        }
                        if (colids[yy] == 66) {
                            do_at_end.add(new ProblemBlock(new Location(world, xx, (y + yy), zz), colids[yy], coldatas[yy]));
                        } else {
                            plugin.getUtils().setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], coldatas[yy], tid);
                        }
                        break;
                    case 63:
                        if (preset.equals(PRESET.APPERTURE)) {
                            plugin.getUtils().setUnderDoorBlock(world, xx, (y - 1), zz, platform_id, platform_data, tid);
                        }
                        break;
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
                        break;
                    case 87:
                        plugin.getUtils().setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], coldatas[yy], tid);
                        if (preset.equals(PRESET.TORCH)) {
                            world.getBlockAt(xx, (y + yy + 1), zz).setType(Material.FIRE);
                        }
                        break;
                    case 90:
                        plugin.getUtils().setBlockAndRemember(world, xx, (y + yy + 1), zz, 49, (byte) 0, tid);
                        plugin.getUtils().setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], coldatas[yy], tid);
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
                            if (colids[yy] == 69 || colids[yy] == 77 || colids[yy] == 143) {
                                do_at_end.add(new ProblemBlock(new Location(world, xx, (y + yy), zz), colids[yy], coldatas[yy]));
                            } else {
                                plugin.getUtils().setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], coldatas[yy], tid);
                            }
                        }
                        break;
                }
            }
        }
        for (ProblemBlock pb : do_at_end) {
            plugin.getUtils().setBlockAndRemember(pb.getL().getWorld(), pb.getL().getBlockX(), pb.getL().getBlockY(), pb.getL().getBlockZ(), pb.getId(), pb.getData(), tid);
        }
        if (!rebuild) {
            // message travellers in tardis
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("tardis_id", tid);
            ResultSetTravellers rst = new ResultSetTravellers(plugin, where, true);
            if (rst.resultSet()) {
                final List<UUID> travellers = rst.getData();
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        for (UUID s : travellers) {
                            Player trav = plugin.getServer().getPlayer(s);
                            if (trav != null) {
                                String message = (mal) ? "There was a malfunction and the emergency handbrake was engaged! Scan location before exit!" : "LEFT-click the handbrake to exit!";
                                TARDISMessage.send(trav, plugin.getPluginName() + message);
                            }
                        }
                    }
                }, 30L);
            }
        }
        plugin.getTrackerKeeper().getTrackMaterialising().remove(Integer.valueOf(tid));
        plugin.getTrackerKeeper().getTrackInVortex().remove(Integer.valueOf(tid));
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
