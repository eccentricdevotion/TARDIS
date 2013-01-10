/*
 * Copyright (C) 2012 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.builders;

import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
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
public class TARDISBuilderPoliceBox {

    private final TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISBuilderPoliceBox(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Builds the TARDIS Police Box.
     *
     * @param id the unique key of the record for this TARDIS in the database.
     * @param l the location where the Police Box should be built.
     * @param d the direction the Police Box is built in.
     * @param c boolean determining whether to engage the chameleon circuit.
     * @param p an instance of the player who owns the TARDIS.
     * @param rebuild boolean determining whether the Police Box blocks should
     * be remembered in the database for protection purposes.
     */
    public void buildPoliceBox(int id, Location l, TARDISConstants.COMPASS d, boolean c, Player p, boolean rebuild) {
        int plusx, minusx, x, plusz, minusz, z, wall_block = 35;
        byte sd = 0, norm = 0, grey = 8, chameleonData = 11;
        if (c) {
            Block chameleonBlock;
            // chameleon circuit is on - get block under TARDIS
            if (l.getBlock().getType() == Material.SNOW) {
                chameleonBlock = l.getBlock();
            } else {
                chameleonBlock = l.getBlock().getRelative(BlockFace.DOWN);
            }
            int chameleonType = chameleonBlock.getTypeId();
            // determine wall_block
            if (TARDISConstants.CHAMELEON_BLOCKS_VALID.contains((Integer) chameleonType)) {
                wall_block = chameleonType;
                chameleonData = chameleonBlock.getData();
            }
            if (TARDISConstants.CHAMELEON_BLOCKS_BAD.contains((Integer) chameleonType)) {
                p.sendMessage(plugin.pluginName + "Bummer, the TARDIS could not engage the Chameleon Circuit!");
            }
            if (TARDISConstants.CHAMELEON_BLOCKS_CHANGE.contains((Integer) chameleonType)) {
                wall_block = swapId(chameleonType);
                switch (chameleonType) {
                    case 22:
                        chameleonData = 11;
                        break;
                    case 41:
                        chameleonData = 4;
                        break;
                    case 42:
                        chameleonData = 8;
                        break;
                    case 57:
                        chameleonData = 3;
                        break;
                    case 133:
                        chameleonData = 5;
                        break;
                    case 134:
                        chameleonData = 1;
                        break;
                    case 135:
                        chameleonData = 2;
                        break;
                    case 136:
                        chameleonData = 3;
                        break;
                    default:
                        chameleonData = chameleonBlock.getData();
                        break;
                }
            }

            if (TARDISConstants.CHAMELEON_BLOCKS_NEXT.contains((Integer) chameleonType)) {
                List<BlockFace> surrounding = Arrays.asList(new BlockFace[]{BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST});
                // try the surrounding blocks
                for (BlockFace bf : surrounding) {
                    Block surroundblock = chameleonBlock.getRelative(bf);
                    int eid = surroundblock.getTypeId();
                    if (TARDISConstants.CHAMELEON_BLOCKS_VALID.contains((Integer) eid)) {
                        wall_block = eid;
                        chameleonData = surroundblock.getData();
                        break;
                    }
                    if (TARDISConstants.CHAMELEON_BLOCKS_CHANGE.contains((Integer) eid)) {
                        wall_block = swapId(eid);
                        switch (eid) {
                            case 134:
                                chameleonData = 1;
                                break;
                            case 135:
                                chameleonData = 2;
                                break;
                            case 136:
                                chameleonData = 3;
                                break;
                            default:
                                chameleonData = chameleonBlock.getData();
                                break;
                        }
                        break;
                    }
                }
            }
        }
        byte mds = chameleonData, mdw = chameleonData, mdn = chameleonData, mde = chameleonData, bds = chameleonData, bdw = chameleonData, bdn = chameleonData, bde = chameleonData;
        final World world;
        // expand placed blocks to a police box
        double lowX = l.getX();
        double lowY = l.getY();
        double lowZ = l.getZ();
        l.setX(lowX + 0.5);
        l.setY(lowY + 2);
        l.setZ(lowZ + 0.5);
        // get relative locations
        x = l.getBlockX();
        plusx = (l.getBlockX() + 1);
        minusx = (l.getBlockX() - 1);
        final int y = l.getBlockY();
        final int plusy = (l.getBlockY() + 1), minusy = (l.getBlockY() - 1), down2y = (l.getBlockY() - 2), down3y = (l.getBlockY() - 3);
        z = (l.getBlockZ());
        plusz = (l.getBlockZ() + 1);
        minusz = (l.getBlockZ() - 1);
        world = l.getWorld();
        int south = wall_block, west = wall_block, north = wall_block, east = wall_block, signx = 0, signz = 0;
        String doorloc = "";

        QueryFactory qf = new QueryFactory(plugin);
        HashMap<String, Object> ps = new HashMap<String, Object>();
        ps.put("tardis_id", id);

        // get direction player is facing from yaw place block under door if block is in list of blocks an iron door cannot go on
        switch (d) {
            case SOUTH:
                //if (yaw >= 315 || yaw < 45)
                plugin.utils.setBlockCheck(world, x, down3y, minusz, 35, grey, id); // door is here if player facing south
                ps.put("location", world.getBlockAt(x, down3y, minusz).getLocation().toString());
                doorloc = world.getName() + ":" + x + ":" + down2y + ":" + minusz;
                sd = 2;
                signx = x;
                signz = (minusz - 1);
                south = 71;
                mds = 8;
                bds = 1;
                break;
            case EAST:
                //if (yaw >= 225 && yaw < 315)
                plugin.utils.setBlockCheck(world, minusx, down3y, z, 35, grey, id); // door is here if player facing east
                ps.put("location", world.getBlockAt(minusx, down3y, z).getLocation().toString());
                doorloc = world.getName() + ":" + minusx + ":" + down2y + ":" + z;
                sd = 4;
                signx = (minusx - 1);
                signz = z;
                east = 71;
                mde = 8;
                bde = 0;
                break;
            case NORTH:
                //if (yaw >= 135 && yaw < 225)
                plugin.utils.setBlockCheck(world, x, down3y, plusz, 35, grey, id); // door is here if player facing north
                ps.put("location", world.getBlockAt(x, down3y, plusz).getLocation().toString());
                doorloc = world.getName() + ":" + x + ":" + down2y + ":" + plusz;
                sd = 3;
                signx = x;
                signz = (plusz + 1);
                north = 71;
                mdn = 8;
                bdn = 3;
                break;
            case WEST:
                //if (yaw >= 45 && yaw < 135)
                plugin.utils.setBlockCheck(world, plusx, down3y, z, 35, grey, id); // door is here if player facing west
                ps.put("location", world.getBlockAt(plusx, down3y, z).getLocation().toString());
                doorloc = world.getName() + ":" + plusx + ":" + down2y + ":" + z;
                sd = 5;
                signx = (plusx + 1);
                signz = z;
                west = 71;
                mdw = 8;
                bdw = 2;
                break;
        }
        qf.doInsert("blocks", ps);
        // should insert the door when tardis is first made, and then update location there after!
        HashMap<String, Object> whered = new HashMap<String, Object>();
        whered.put("door_type", 0);
        whered.put("tardis_id", id);
        ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
        HashMap<String, Object> setd = new HashMap<String, Object>();
        setd.put("door_location", doorloc);
        if (rsd.resultSet()) {
            HashMap<String, Object> whereid = new HashMap<String, Object>();
            whereid.put("door_id", rsd.getDoor_id());
            qf.doUpdate("doors", setd, whereid);
        } else {
            setd.put("tardis_id", id);
            setd.put("door_type", 0);
            qf.doInsert("doors", setd);
        }

        // bottom layer corners
        plugin.utils.setBlockAndRemember(world, plusx, down2y, plusz, wall_block, chameleonData, id, rebuild);
        plugin.utils.setBlockAndRemember(world, minusx, down2y, plusz, wall_block, chameleonData, id, rebuild);
        plugin.utils.setBlockAndRemember(world, minusx, down2y, minusz, wall_block, chameleonData, id, rebuild);
        plugin.utils.setBlockAndRemember(world, plusx, down2y, minusz, wall_block, chameleonData, id, rebuild);
        // middle layer corners
        plugin.utils.setBlockAndRemember(world, plusx, minusy, plusz, wall_block, chameleonData, id, rebuild);
        plugin.utils.setBlockAndRemember(world, minusx, minusy, plusz, wall_block, chameleonData, id, rebuild);
        plugin.utils.setBlockAndRemember(world, minusx, minusy, minusz, wall_block, chameleonData, id, rebuild);
        plugin.utils.setBlockAndRemember(world, plusx, minusy, minusz, wall_block, chameleonData, id, rebuild);
        // top layer
        switch (wall_block) {
            case 18:
                plugin.utils.setBlockAndRemember(world, x, y, z, 17, chameleonData, id, rebuild);
                break;
            case 46:
                plugin.utils.setBlockAndRemember(world, x, y, z, 35, (byte) 14, id, rebuild);
                break;
            case 79:
                plugin.utils.setBlockAndRemember(world, x, y, z, 35, (byte) 3, id, rebuild);
                break;
            case 89:
                plugin.utils.setBlockAndRemember(world, x, y, z, 35, (byte) 4, id, rebuild);
                break;
            default:
                plugin.utils.setBlockAndRemember(world, x, y, z, wall_block, chameleonData, id, rebuild);
                break;
        }
        plugin.utils.setBlockAndRemember(world, plusx, y, z, wall_block, chameleonData, id, rebuild); // east
        plugin.utils.setBlockAndRemember(world, plusx, y, plusz, wall_block, chameleonData, id, rebuild);
        plugin.utils.setBlockAndRemember(world, x, y, plusz, wall_block, chameleonData, id, rebuild); // south
        plugin.utils.setBlockAndRemember(world, minusx, y, plusz, wall_block, chameleonData, id, rebuild);
        plugin.utils.setBlockAndRemember(world, minusx, y, z, wall_block, chameleonData, id, rebuild); // west
        plugin.utils.setBlockAndRemember(world, minusx, y, minusz, wall_block, chameleonData, id, rebuild);
        plugin.utils.setBlockAndRemember(world, x, y, minusz, wall_block, chameleonData, id, rebuild); // north
        plugin.utils.setBlockAndRemember(world, plusx, y, minusz, wall_block, chameleonData, id, rebuild);
        // set sign
        plugin.utils.setBlock(world, signx, y, signz, 68, sd);
        Block sign = world.getBlockAt(signx, y, signz);
        if (sign.getType().equals(Material.WALL_SIGN)) {
            Sign s = (Sign) sign.getState();
            if (plugin.getConfig().getBoolean("name_tardis")) {
                HashMap<String, Object> wheret = new HashMap<String, Object>();
                wheret.put("tardis_id", id);
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
            s.setLine(1, ChatColor.WHITE + "POLICE");
            s.setLine(2, ChatColor.WHITE + "BOX");
            s.update();
        }
        // put torch on top
        if (wall_block == 79) {
            plugin.utils.setBlockAndRemember(world, x, plusy, z, 76, (byte) 5, id, rebuild);
        } else {
            plugin.utils.setBlockAndRemember(world, x, plusy, z, 50, (byte) 5, id, rebuild);
        }
        // remove the IRON & LAPIS blocks
        plugin.utils.setBlock(world, x, minusy, z, 0, norm);
        plugin.utils.setBlock(world, x, down2y, z, 0, norm);
        // bottom layer with door bottom
        plugin.utils.setBlockAndRemember(world, plusx, down2y, z, west, bdw, id, rebuild);
        plugin.utils.setBlockAndRemember(world, x, down2y, plusz, north, bdn, id, rebuild);
        plugin.utils.setBlockAndRemember(world, minusx, down2y, z, east, bde, id, rebuild);
        plugin.utils.setBlockAndRemember(world, x, down2y, minusz, south, bds, id, rebuild);
        // middle layer with door top
        plugin.utils.setBlockAndRemember(world, plusx, minusy, z, west, mdw, id, rebuild);
        plugin.utils.setBlockAndRemember(world, x, minusy, plusz, north, mdn, id, rebuild);
        plugin.utils.setBlockAndRemember(world, minusx, minusy, z, east, mde, id, rebuild);
        plugin.utils.setBlockAndRemember(world, x, minusy, minusz, south, mds, id, rebuild);
        // add platform if configured and necessary
        if (plugin.getConfig().getBoolean("platform")) {
            // check if user has platform pref
            HashMap<String, Object> wherep = new HashMap<String, Object>();
            wherep.put("player", p.getName());
            ResultSetPlayerPrefs pp = new ResultSetPlayerPrefs(plugin, wherep);
            boolean userPlatform;
            if (pp.resultSet()) {
                userPlatform = pp.getPlatform_on();
            } else {
                userPlatform = true;
            }
            if (userPlatform) {
                List<Block> platform_blocks = null;
                switch (d) {
                    case SOUTH:
                        platform_blocks = Arrays.asList(world.getBlockAt(x - 1, down3y, minusz - 1), world.getBlockAt(x, down3y, minusz - 1), world.getBlockAt(x + 1, down3y, minusz - 1), world.getBlockAt(x - 1, down3y, minusz - 2), world.getBlockAt(x, down3y, minusz - 2), world.getBlockAt(x + 1, down3y, minusz - 2));
                        break;
                    case EAST:
                        platform_blocks = Arrays.asList(world.getBlockAt(minusx - 1, down3y, z - 1), world.getBlockAt(minusx - 1, down3y, z), world.getBlockAt(minusx - 1, down3y, z + 1), world.getBlockAt(minusx - 2, down3y, z - 1), world.getBlockAt(minusx - 2, down3y, z), world.getBlockAt(minusx - 2, down3y, z + 1));
                        break;
                    case NORTH:
                        platform_blocks = Arrays.asList(world.getBlockAt(x + 1, down3y, plusz + 1), world.getBlockAt(x, down3y, plusz + 1), world.getBlockAt(x - 1, down3y, plusz + 1), world.getBlockAt(x + 1, down3y, plusz + 2), world.getBlockAt(x, down3y, plusz + 2), world.getBlockAt(x - 1, down3y, plusz + 2));
                        break;
                    case WEST:
                        platform_blocks = Arrays.asList(world.getBlockAt(plusx + 1, down3y, z + 1), world.getBlockAt(plusx + 1, down3y, z), world.getBlockAt(plusx + 1, down3y, z - 1), world.getBlockAt(plusx + 2, down3y, z + 1), world.getBlockAt(plusx + 2, down3y, z), world.getBlockAt(plusx + 2, down3y, z - 1));
                        break;
                }
                StringBuilder sb = new StringBuilder();
                for (Block pb : platform_blocks) {
                    Material mat = pb.getType();
                    if (mat == Material.AIR || mat == Material.STATIONARY_WATER || mat == Material.WATER || mat == Material.VINE || mat == Material.RED_MUSHROOM || mat == Material.BROWN_MUSHROOM || mat == Material.LONG_GRASS || mat == Material.SAPLING || mat == Material.DEAD_BUSH || mat == Material.RED_ROSE || mat == Material.YELLOW_FLOWER || mat == Material.SNOW) {
                        plugin.utils.setBlockAndRemember(world, pb.getX(), pb.getY(), pb.getZ(), 35, grey, id, rebuild);
                        String p_tmp = world.getName() + ":" + pb.getX() + ":" + pb.getY() + ":" + pb.getZ() + ":" + mat.toString();
                        sb.append(p_tmp).append("~");
                    }
                }
                String recall = sb.toString();
                String platform_recall = "";
                if (recall.length() > 0) {
                    platform_recall = recall.substring(0, recall.length() - 1);
                }
                HashMap<String, Object> setf = new HashMap<String, Object>();
                setf.put("platform", platform_recall);
                HashMap<String, Object> wheref = new HashMap<String, Object>();
                wheref.put("tardis_id", id);
                qf.doUpdate("tardis", setf, wheref);
            }
        }
    }

    public int swapId(int id) {
        int swappedId = TARDISConstants.CHAMELEON_BLOCKS_CHANGE_HASH.get(id);
        return swappedId;
    }
}