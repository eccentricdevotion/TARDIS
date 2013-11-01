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
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonCircuit;
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
    List<Integer> plat_blocks = Arrays.asList(new Integer[]{0, 6, 9, 8, 31, 32, 37, 38, 39, 40, 78, 106, 3019, 3020});

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
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            TARDISConstants.PRESET preset = rs.getPreset();
            int cham_id = rs.getChameleon_id();
            byte cham_data = rs.getChameleon_data();
            if (c && (preset.equals(TARDISConstants.PRESET.NEW) || preset.equals(TARDISConstants.PRESET.OLD))) {
                Block chameleonBlock;
                // chameleon circuit is on - get block under TARDIS
                if (l.getBlock().getType() == Material.SNOW) {
                    chameleonBlock = l.getBlock();
                } else {
                    chameleonBlock = l.getBlock().getRelative(BlockFace.DOWN);
                }
                // determine cham_id
                TARDISChameleonCircuit tcc = new TARDISChameleonCircuit(plugin);
                int[] b_data = tcc.getChameleonBlock(chameleonBlock, p, false);
                cham_id = b_data[0];
                cham_data = (byte) b_data[1];
            }
            // get lamp and submarine preferences
            int lamp = plugin.getConfig().getInt("tardis_lamp");
            boolean sub = false;
            HashMap<String, Object> wherepp = new HashMap<String, Object>();
            wherepp.put("player", p.getName());
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
            if (rsp.resultSet()) {
                lamp = rsp.getLamp();
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
                TARDISRebuildPreset trp = new TARDISRebuildPreset(plugin, l, preset, id, d, p.getName(), mal, lamp, sub, cham_id, cham_data);
                trp.rebuildPreset();
//            TARDISPoliceBoxRebuilder rebuilder = new TARDISPoliceBoxRebuilder(plugin, l, cham_id, cham_data, id, d, lamp, plain, sub);
//            rebuilder.rebuildPoliceBox();
            } else {
                if (plugin.getConfig().getBoolean("materialise")) {
                    plugin.tardisMaterialising.add(id);
//                TARDISMaterialisationRunnable runnable = new TARDISMaterialisationRunnable(plugin, l, cham_id, cham_data, id, d, p, mal, lamp, plain, sub);
                    TARDISPresetRunnable runnable = new TARDISPresetRunnable(plugin, l, preset, id, d, p, mal, lamp, sub, cham_id, cham_data);
                    int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
                    runnable.setTask(taskID);
                } else {
                    plugin.tardisMaterialising.add(id);
                    //TARDISInstaPoliceBox insta = new TARDISInstaPoliceBox(plugin, l, cham_id, cham_data, id, d, p.getName(), mal, lamp, plain, sub);
                    //insta.buildPoliceBox();
                    TARDISInstaPreset insta = new TARDISInstaPreset(plugin, l, preset, id, d, p.getName(), mal, lamp, sub, cham_id, cham_data);
                    insta.buildPreset();
                }
            }
            // update demat so it knows about the current preset after it has changed
            HashMap<String, Object> whered = new HashMap<String, Object>();
            whered.put("tardis_id", id);
            HashMap<String, Object> set = new HashMap<String, Object>();
            set.put("chameleon_demat", preset.toString());
            new QueryFactory(plugin).doUpdate("tardis", set, whered);
        }
    }

    @SuppressWarnings("deprecation")
    public void addPlatform(Location l, boolean rebuild, TARDISConstants.COMPASS d, String p, int id) {
        int plusx, minusx, x, y, plusz, minusz, z;
        int platform_id = plugin.getConfig().getInt("platform_id");
        byte platform_data = (byte) plugin.getConfig().getInt("platform_data");
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
                    int matint = pb.getTypeId();
                    if (plat_blocks.contains(matint)) {
                        if (rebuild) {
                            plugin.utils.setBlockAndRemember(world, pb.getX(), pb.getY(), pb.getZ(), platform_id, platform_data, id);
                        } else {
                            plugin.utils.setBlock(world, pb.getX(), pb.getY(), pb.getZ(), platform_id, platform_data);
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
