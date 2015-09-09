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
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonCircuit;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.destroyers.TARDISDeinstaPreset;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.junk.TARDISJunkBuilder;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * The Wibbly lever was a part of The Doctor's TARDIS console. The lever had at
 * least two functions: opening and closing doors and controlling implosions
 * used to revert paradoxes in which the TARDIS had materialised within itself.
 *
 * @author eccentric_nz
 */
public class TARDISPresetBuilderFactory {

    private final TARDIS plugin;
    HashMap<COMPASS, BlockFace[]> face_map = new HashMap<COMPASS, BlockFace[]>();
    public final List<PRESET> no_block_under_door;
    public final List<PRESET> notSubmarinePresets;
    Random rand;

    public TARDISPresetBuilderFactory(TARDIS plugin) {
        this.plugin = plugin;
        this.rand = new Random();
        face_map.put(COMPASS.NORTH, new BlockFace[]{BlockFace.SOUTH_WEST, BlockFace.SOUTH_SOUTH_WEST, BlockFace.SOUTH, BlockFace.SOUTH_SOUTH_EAST, BlockFace.SOUTH_EAST});
        face_map.put(COMPASS.WEST, new BlockFace[]{BlockFace.SOUTH_EAST, BlockFace.EAST_SOUTH_EAST, BlockFace.EAST, BlockFace.EAST_NORTH_EAST, BlockFace.NORTH_EAST});
        face_map.put(COMPASS.SOUTH, new BlockFace[]{BlockFace.NORTH_EAST, BlockFace.NORTH_NORTH_EAST, BlockFace.NORTH, BlockFace.NORTH_NORTH_WEST, BlockFace.NORTH_WEST});
        face_map.put(COMPASS.EAST, new BlockFace[]{BlockFace.NORTH_WEST, BlockFace.WEST_NORTH_WEST, BlockFace.WEST, BlockFace.WEST_SOUTH_WEST, BlockFace.SOUTH_WEST});
        no_block_under_door = new ArrayList<PRESET>();
        no_block_under_door.add(PRESET.ANGEL);
        no_block_under_door.add(PRESET.DUCK);
        no_block_under_door.add(PRESET.GAZEBO);
        no_block_under_door.add(PRESET.HELIX);
        no_block_under_door.add(PRESET.LIBRARY);
        no_block_under_door.add(PRESET.PANDORICA);
        no_block_under_door.add(PRESET.ROBOT);
        no_block_under_door.add(PRESET.TORCH);
        no_block_under_door.add(PRESET.WELL);
        notSubmarinePresets = new ArrayList<PRESET>();
        notSubmarinePresets.add(PRESET.LAMP);
        notSubmarinePresets.add(PRESET.MINESHAFT);
    }

    /**
     * Builds the TARDIS Police Box.
     *
     * @param tmd the TARDIS build data
     */
    public void buildPreset(TARDISMaterialisationData tmd) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", tmd.getTardisID());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            PRESET preset = rs.getPreset();
            Biome biome;
            if (tmd.isRebuild()) {
                biome = tmd.getLocation().getWorld().getBlockAt(tmd.getLocation()).getRelative(getOppositeFace(tmd.getDirection()), 2).getBiome();
            } else {
                biome = tmd.getLocation().getWorld().getBiome(tmd.getLocation().getBlockX(), tmd.getLocation().getBlockZ());
            }
            tmd.setBiome(biome);
            if (plugin.getConfig().getBoolean("police_box.set_biome") && !tmd.isRebuild()) {
                // remember the current biome (unless rebuilding)
                new QueryFactory(plugin).saveBiome(rs.getTardis_id(), biome.toString());
            }
            if (rs.isAdapti_on()) {
                preset = adapt(biome, preset);
            }
            PRESET demat = rs.getDemat();
            int cham_id = rs.getChameleon_id();
            byte cham_data = rs.getChameleon_data();
            if (tmd.isChameleon() && (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD) || preset.equals(PRESET.SUBMERGED))) {
                Block chameleonBlock;
                // chameleon circuit is on - get block under TARDIS
                if (tmd.getLocation().getBlock().getType() == Material.SNOW) {
                    chameleonBlock = tmd.getLocation().getBlock();
                } else {
                    chameleonBlock = tmd.getLocation().getBlock().getRelative(BlockFace.DOWN);
                }
                // determine cham_id
                TARDISChameleonCircuit tcc = new TARDISChameleonCircuit(plugin);
                int[] b_data = tcc.getChameleonBlock(chameleonBlock, tmd.getPlayer(), false);
                cham_id = b_data[0];
                cham_data = (byte) b_data[1];
            }
            // get lamp and submarine preferences
            int lamp = plugin.getConfig().getInt("police_box.tardis_lamp");
            boolean minecart = false;
            boolean ctm = false;
            boolean add_sign = true;
            boolean hidden = rs.isHidden();
            String uuid = (preset.equals(PRESET.JUNK)) ? "00000000-aaaa-bbbb-cccc-000000000000" : tmd.getPlayer().getUniqueId().toString();
            HashMap<String, Object> wherepp = new HashMap<String, Object>();
            wherepp.put("uuid", uuid);
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
            if (rsp.resultSet()) {
                lamp = rsp.getLamp();
                minecart = rsp.isMinecartOn();
                ctm = rsp.isCtmOn();
                add_sign = rsp.isSignOn();
            }
            if (tmd.isSubmarine() && notSubmarinePresets.contains(preset)) {
                preset = PRESET.YELLOW;
                TARDISMessage.send(tmd.getPlayer().getPlayer(), "SUB_UNSUITED");
            }
            // keep the chunk this Police box is in loaded
            Chunk thisChunk = tmd.getLocation().getChunk();
            while (!thisChunk.isLoaded()) {
                thisChunk.load();
            }
            /*
             * We can always add the chunk, as List.remove() only removes the
             * first occurence - and we want the chunk to remain loaded if there
             * are other Police Boxes in it.
             */
            plugin.getGeneralKeeper().getTardisChunkList().add(thisChunk);
            if (tmd.isRebuild()) {
                // always destroy it first as the player may just be switching presets
                if (!hidden) {
                    TARDISDeinstaPreset deinsta = new TARDISDeinstaPreset(plugin);
                    deinsta.instaDestroyPreset(tmd, false, demat);
                }
                plugin.getTrackerKeeper().getMaterialising().add(tmd.getTardisID());
                TARDISMaterialisationPreset runnable = new TARDISMaterialisationPreset(plugin, tmd, preset, lamp, cham_id, cham_data, minecart, ctm, add_sign, 3);
                int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
                runnable.setTask(taskID);
            } else if (plugin.getConfig().getBoolean("police_box.materialise") && !preset.equals(PRESET.INVISIBLE)) {
                plugin.getTrackerKeeper().getMaterialising().add(tmd.getTardisID());
                if (preset.equals(PRESET.JUNK)) {
                    TARDISJunkBuilder runnable = new TARDISJunkBuilder(plugin, tmd);
                    int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
                    runnable.setTask(taskID);
                } else {
                    TARDISMaterialisationPreset runnable = new TARDISMaterialisationPreset(plugin, tmd, preset, lamp, cham_id, cham_data, minecart, ctm, add_sign, 18);
                    int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
                    runnable.setTask(taskID);
                }
            } else {
                plugin.getTrackerKeeper().getMaterialising().add(tmd.getTardisID());
                TARDISInstaPreset insta = new TARDISInstaPreset(plugin, tmd, preset, lamp, cham_id, cham_data, false, minecart, ctm, add_sign);
                insta.buildPreset();
            }
            // update demat so it knows about the current preset after it has changed
            HashMap<String, Object> whered = new HashMap<String, Object>();
            whered.put("tardis_id", tmd.getTardisID());
            HashMap<String, Object> set = new HashMap<String, Object>();
            set.put("chameleon_demat", preset.toString());
            new QueryFactory(plugin).doUpdate("tardis", set, whered);
        }
    }

    public PRESET adapt(Biome biome, PRESET preset) {
        switch (biome) {
            case DEEP_OCEAN:
            case FROZEN_OCEAN:
            case OCEAN:
                return PRESET.YELLOW;
            case DESERT:
            case DESERT_HILLS:
                return PRESET.DESERT;
            case HELL:
                return PRESET.NETHER;
            case JUNGLE:
            case JUNGLE_HILLS:
                return PRESET.JUNGLE;
            case PLAINS:
                return PRESET.VILLAGE;
            case MUSHROOM_ISLAND:
            case MUSHROOM_SHORE:
                return PRESET.SHROOM;
            case SWAMPLAND:
                return PRESET.SWAMP;
            case SKY:
                return PRESET.THEEND;
            default:
                return preset;
        }
    }

    public BlockFace getSkullDirection(COMPASS d) {
        BlockFace[] faces = face_map.get(d);
        return faces[rand.nextInt(5)];
    }

    private BlockFace getOppositeFace(COMPASS d) {
        switch (d) {
            case SOUTH:
                return BlockFace.NORTH;
            case WEST:
                return BlockFace.EAST;
            case NORTH:
                return BlockFace.SOUTH;
            default:
                return BlockFace.WEST;
        }
    }
}
