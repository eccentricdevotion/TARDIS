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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
        int plusx, minusx, x, y, plusz, minusz, z, wall_block = 35;
        byte grey = 8, chameleonData = 11;
        if (c) {
            Block chameleonBlock;
            // chameleon circuit is on - get block under TARDIS
            if (l.getBlock().getType() == Material.SNOW) {
                chameleonBlock = l.getBlock();
            } else {
                chameleonBlock = l.getBlock().getRelative(BlockFace.DOWN);
            }
            // get chameleon_id/data if set
            HashMap<String, Object> wherec = new HashMap<String, Object>();
            wherec.put("tardis_id", id);
            ResultSetTardis rsc = new ResultSetTardis(plugin, wherec, "", false);
            rsc.resultSet();
            int c_id = rsc.getChameleon_id();
            byte c_data = rsc.getChameleon_data();
//            int chameleonType;
            if (c_id != 35 && c_data != (byte) 11) {
                wall_block = c_id;
                chameleonData = c_data;
            } else {
                // determine wall_block
                TARDISChameleonCircuit tcc = new TARDISChameleonCircuit(plugin);
                int[] b_data = tcc.getChameleonBlock(chameleonBlock, p, false);
                wall_block = b_data[0];
                chameleonData = (byte) b_data[1];
//                chameleonType = chameleonBlock.getTypeId();
//                if (TARDISConstants.CHAMELEON_BLOCKS_VALID.contains((Integer) chameleonType)) {
//                    wall_block = chameleonType;
//                    chameleonData = chameleonBlock.getData();
//                }
//                if (TARDISConstants.CHAMELEON_BLOCKS_BAD.contains((Integer) chameleonType)) {
//                    p.sendMessage(plugin.pluginName + "Bummer, the TARDIS could not engage the Chameleon Circuit!");
//                }
//                if (TARDISConstants.CHAMELEON_BLOCKS_CHANGE.contains((Integer) chameleonType)) {
//                    wall_block = swapId(chameleonType);
//                    switch (chameleonType) {
//                        case 22:
//                            chameleonData = 11;
//                            break;
//                        case 41:
//                            chameleonData = 4;
//                            break;
//                        case 42:
//                            chameleonData = 8;
//                            break;
//                        case 57:
//                            chameleonData = 3;
//                            break;
//                        case 133:
//                            chameleonData = 5;
//                            break;
//                        case 134:
//                            chameleonData = 1;
//                            break;
//                        case 135:
//                            chameleonData = 2;
//                            break;
//                        case 136:
//                            chameleonData = 3;
//                            break;
//                        default:
//                            chameleonData = chameleonBlock.getData();
//                            break;
//                    }
//                }
//                if (TARDISConstants.CHAMELEON_BLOCKS_NEXT.contains((Integer) chameleonType)) {
//                    List<BlockFace> surrounding = Arrays.asList(new BlockFace[]{BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST});
//                    // try the surrounding blocks
//                    for (BlockFace bf : surrounding) {
//                        Block surroundblock = chameleonBlock.getRelative(bf);
//                        int eid = surroundblock.getTypeId();
//                        if (TARDISConstants.CHAMELEON_BLOCKS_VALID.contains((Integer) eid)) {
//                            wall_block = eid;
//                            chameleonData = surroundblock.getData();
//                            break;
//                        }
//                        if (TARDISConstants.CHAMELEON_BLOCKS_CHANGE.contains((Integer) eid)) {
//                            wall_block = swapId(eid);
//                            switch (eid) {
//                                case 134:
//                                    chameleonData = 1;
//                                    break;
//                                case 135:
//                                    chameleonData = 2;
//                                    break;
//                                case 136:
//                                    chameleonData = 3;
//                                    break;
//                                default:
//                                    chameleonData = chameleonBlock.getData();
//                                    break;
//                            }
//                            break;
//                        }
//                    }
//                }
            }
        }
        // keep the chunk this Police box is in loaded
        Chunk thisChunk = l.getChunk();
        while (!thisChunk.isLoaded()) {
            thisChunk.load();
        }
        /*
         * We can always add the chunk, as List.remove() only removes the first
         * occurence - and we want the chunk to remain loaded if there are other
         * Police Boxes in it.
         */
        plugin.tardisChunkList.add(thisChunk);
        if (rebuild) {
            TARDISPoliceBoxRebuilder rebuilder = new TARDISPoliceBoxRebuilder(plugin, l, wall_block, chameleonData, id, d);
            rebuilder.rebuildPoliceBox();
        } else {
            if (plugin.getConfig().getBoolean("materialise")) {
                plugin.tardisMaterialising.add(id);
                TARDISMaterialisationRunnable runnable = new TARDISMaterialisationRunnable(plugin, l, wall_block, chameleonData, id, d, p);
                int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
                runnable.setTask(taskID);
            } else {
                plugin.tardisMaterialising.add(id);
                TARDISInstaPoliceBox insta = new TARDISInstaPoliceBox(plugin, l, wall_block, chameleonData, id, d);
                insta.buildPoliceBox();
            }
        }

        // add platform if configured and necessary
        World world = l.getWorld();
        x = l.getBlockX();
        plusx = (l.getBlockX() + 1);
        minusx = (l.getBlockX() - 1);
        if (plugin.getConfig().getBoolean("materialise") && rebuild == false) {
            y = (l.getBlockY() - 1);
        } else {
            y = (l.getBlockY() - 3);
        }
        z = (l.getBlockZ());
        plusz = (l.getBlockZ() + 1);
        minusz = (l.getBlockZ() - 1);
        QueryFactory qf = new QueryFactory(plugin);
        if (plugin.getConfig().getBoolean("platform")) {
            // check if user has platform pref
            HashMap<String, Object> wherep = new HashMap<String, Object>();
            wherep.put("player", p.getName());
            ResultSetPlayerPrefs pp = new ResultSetPlayerPrefs(plugin, wherep);
            boolean userPlatform;
            if (pp.resultSet()) {
                userPlatform = pp.isPlatform_on();
            } else {
                userPlatform = true;
            }
            if (userPlatform) {
                List<Block> platform_blocks = null;
                switch (d) {
                    case SOUTH:
                        platform_blocks = Arrays.asList(world.getBlockAt(x - 1, y, minusz - 1), world.getBlockAt(x, y, minusz - 1), world.getBlockAt(x + 1, y, minusz - 1), world.getBlockAt(x - 1, y, minusz - 2), world.getBlockAt(x, y, minusz - 2), world.getBlockAt(x + 1, y, minusz - 2));
                        break;
                    case EAST:
                        platform_blocks = Arrays.asList(world.getBlockAt(minusx - 1, y, z - 1), world.getBlockAt(minusx - 1, y, z), world.getBlockAt(minusx - 1, y, z + 1), world.getBlockAt(minusx - 2, y, z - 1), world.getBlockAt(minusx - 2, y, z), world.getBlockAt(minusx - 2, y, z + 1));
                        break;
                    case NORTH:
                        platform_blocks = Arrays.asList(world.getBlockAt(x + 1, y, plusz + 1), world.getBlockAt(x, y, plusz + 1), world.getBlockAt(x - 1, y, plusz + 1), world.getBlockAt(x + 1, y, plusz + 2), world.getBlockAt(x, y, plusz + 2), world.getBlockAt(x - 1, y, plusz + 2));
                        break;
                    case WEST:
                        platform_blocks = Arrays.asList(world.getBlockAt(plusx + 1, y, z + 1), world.getBlockAt(plusx + 1, y, z), world.getBlockAt(plusx + 1, y, z - 1), world.getBlockAt(plusx + 2, y, z + 1), world.getBlockAt(plusx + 2, y, z), world.getBlockAt(plusx + 2, y, z - 1));
                        break;
                }
                StringBuilder sb = new StringBuilder();
                for (Block pb : platform_blocks) {
                    Material mat = pb.getType();
                    if (mat == Material.AIR || mat == Material.STATIONARY_WATER || mat == Material.WATER || mat == Material.VINE || mat == Material.RED_MUSHROOM || mat == Material.BROWN_MUSHROOM || mat == Material.LONG_GRASS || mat == Material.SAPLING || mat == Material.DEAD_BUSH || mat == Material.RED_ROSE || mat == Material.YELLOW_FLOWER || mat == Material.SNOW) {
                        if (rebuild) {
                            plugin.utils.setBlockAndRemember(world, pb.getX(), pb.getY(), pb.getZ(), 35, grey, id);
                        } else {
                            plugin.utils.setBlock(world, pb.getX(), pb.getY(), pb.getZ(), 35, grey);
                        }
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
}
