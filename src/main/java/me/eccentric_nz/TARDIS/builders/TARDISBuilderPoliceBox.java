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

import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonCircuit;
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
 * The Wibbly lever was a part of The Doctor's TARDIS console. The lever had at
 * least two functions: opening and closing doors and controlling implosions
 * used to revert paradoxes in which the TARDIS had materialised within itself.
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
     * @param mal boolean determining whether a malfunction has occurred
     */
    public void buildPoliceBox(int id, Location l, TARDISConstants.COMPASS d, boolean c, Player p, boolean rebuild, boolean mal) {
        int wall_block = plugin.getConfig().getInt("wall_id");
        byte chameleonData = (byte) plugin.getConfig().getInt("wall_data");
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
            boolean bluewool = (c_id == wall_block && c_data == chameleonData);
            if (!bluewool) {
                wall_block = c_id;
                chameleonData = c_data;
            } else {
                // determine wall_block
                TARDISChameleonCircuit tcc = new TARDISChameleonCircuit(plugin);
                int[] b_data = tcc.getChameleonBlock(chameleonBlock, p, false);
                wall_block = b_data[0];
                chameleonData = (byte) b_data[1];
            }
        }
        // get sign and torch preferences
        int lamp = plugin.getConfig().getInt("tardis_lamp");
        boolean plain = plugin.getConfig().getBoolean("plain_on");
        boolean sub = false;
        HashMap<String, Object> wherepp = new HashMap<String, Object>();
        wherepp.put("player", p.getName());
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
        if (rsp.resultSet()) {
            lamp = rsp.getLamp();
            plain = rsp.isPlain_on();
            sub = (rsp.isSubmarine_on() && plugin.trackSubmarine.contains(Integer.valueOf(id)));
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
            TARDISPoliceBoxRebuilder rebuilder = new TARDISPoliceBoxRebuilder(plugin, l, wall_block, chameleonData, id, d, lamp, plain, sub);
            rebuilder.rebuildPoliceBox();
        } else {
            if (plugin.getConfig().getBoolean("materialise")) {
                plugin.tardisMaterialising.add(id);
                TARDISMaterialisationRunnable runnable = new TARDISMaterialisationRunnable(plugin, l, wall_block, chameleonData, id, d, p, mal, lamp, plain, sub);
                int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
                runnable.setTask(taskID);
            } else {
                plugin.tardisMaterialising.add(id);
                TARDISInstaPoliceBox insta = new TARDISInstaPoliceBox(plugin, l, wall_block, chameleonData, id, d, p.getName(), mal, lamp, plain, sub);
                insta.buildPoliceBox();
            }
        }
    }

    public void addPlatform(Location l, boolean rebuild, TARDISConstants.COMPASS d, String p, int id) {
        int plusx, minusx, x, y, plusz, minusz, z;
        byte grey = 8;
        int platform_id = plugin.getConfig().getInt("platform_ID");
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
            wherep.put("player", p);
            ResultSetPlayerPrefs pp = new ResultSetPlayerPrefs(plugin, wherep);
            boolean userPlatform;
            if (pp.resultSet()) {
                userPlatform = pp.isPlatform_on();
            } else {
                userPlatform = true;
            }
            if (userPlatform) {
                List<Block> platform_blocks;
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
                    default:
                        platform_blocks = Arrays.asList(world.getBlockAt(plusx + 1, y, z + 1), world.getBlockAt(plusx + 1, y, z), world.getBlockAt(plusx + 1, y, z - 1), world.getBlockAt(plusx + 2, y, z + 1), world.getBlockAt(plusx + 2, y, z), world.getBlockAt(plusx + 2, y, z - 1));
                        break;
                }
                StringBuilder sb = new StringBuilder();
                for (Block pb : platform_blocks) {
                    Material mat = pb.getType();
                    if (mat == Material.AIR || mat == Material.STATIONARY_WATER || mat == Material.WATER || mat == Material.VINE || mat == Material.RED_MUSHROOM || mat == Material.BROWN_MUSHROOM || mat == Material.LONG_GRASS || mat == Material.SAPLING || mat == Material.DEAD_BUSH || mat == Material.RED_ROSE || mat == Material.YELLOW_FLOWER || mat == Material.SNOW) {
                        if (rebuild) {
                            plugin.utils.setBlockAndRemember(world, pb.getX(), pb.getY(), pb.getZ(), platform_id, grey, id);
                        } else {
                            plugin.utils.setBlock(world, pb.getX(), pb.getY(), pb.getZ(), platform_id, grey);
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
