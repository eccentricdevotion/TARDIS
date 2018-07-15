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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonCircuit;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.destroyers.TARDISDeinstaPreset;
import me.eccentric_nz.TARDIS.enumeration.ADAPTION;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.junk.TARDISJunkBuilder;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * The Wibbly lever was a part of The Doctor's TARDIS console. The lever had at least two functions: opening and closing
 * doors and controlling implosions used to revert paradoxes in which the TARDIS had materialised within itself.
 *
 * @author eccentric_nz
 */
public class TARDISPresetBuilderFactory {

    private final TARDIS plugin;
    private final HashMap<COMPASS, BlockFace[]> face_map = new HashMap<>();
    public final List<PRESET> no_block_under_door;
    private final List<PRESET> notSubmarinePresets;
    private final Random rand;

    public TARDISPresetBuilderFactory(TARDIS plugin) {
        this.plugin = plugin;
        rand = new Random();
        face_map.put(COMPASS.NORTH, new BlockFace[]{BlockFace.SOUTH_WEST, BlockFace.SOUTH_SOUTH_WEST, BlockFace.SOUTH, BlockFace.SOUTH_SOUTH_EAST, BlockFace.SOUTH_EAST});
        face_map.put(COMPASS.WEST, new BlockFace[]{BlockFace.SOUTH_EAST, BlockFace.EAST_SOUTH_EAST, BlockFace.EAST, BlockFace.EAST_NORTH_EAST, BlockFace.NORTH_EAST});
        face_map.put(COMPASS.SOUTH, new BlockFace[]{BlockFace.NORTH_EAST, BlockFace.NORTH_NORTH_EAST, BlockFace.NORTH, BlockFace.NORTH_NORTH_WEST, BlockFace.NORTH_WEST});
        face_map.put(COMPASS.EAST, new BlockFace[]{BlockFace.NORTH_WEST, BlockFace.WEST_NORTH_WEST, BlockFace.WEST, BlockFace.WEST_SOUTH_WEST, BlockFace.SOUTH_WEST});
        no_block_under_door = new ArrayList<>();
        no_block_under_door.add(PRESET.ANGEL);
        no_block_under_door.add(PRESET.DUCK);
        no_block_under_door.add(PRESET.GAZEBO);
        no_block_under_door.add(PRESET.HELIX);
        no_block_under_door.add(PRESET.LIBRARY);
        no_block_under_door.add(PRESET.PANDORICA);
        no_block_under_door.add(PRESET.ROBOT);
        no_block_under_door.add(PRESET.TORCH);
        no_block_under_door.add(PRESET.WELL);
        notSubmarinePresets = new ArrayList<>();
        notSubmarinePresets.add(PRESET.LAMP);
        notSubmarinePresets.add(PRESET.MINESHAFT);
    }

    /**
     * Builds the TARDIS Police Box.
     *
     * @param bd the TARDIS build data
     */
    public void buildPreset(BuildData bd) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", bd.getTardisID());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            PRESET preset = tardis.getPreset();
            Biome biome;
            // keep the chunk this Police box is in loaded
            Chunk thisChunk = bd.getLocation().getChunk();
            while (!thisChunk.isLoaded()) {
                thisChunk.load();
            }
            if (bd.isRebuild()) {
                biome = bd.getLocation().getWorld().getBlockAt(bd.getLocation()).getRelative(getOppositeFace(bd.getDirection()), 2).getBiome();
            } else {
                biome = bd.getLocation().getWorld().getBiome(bd.getLocation().getBlockX(), bd.getLocation().getBlockZ());
            }
            bd.setBiome(biome);
            if (plugin.getConfig().getBoolean("police_box.set_biome") && !bd.isRebuild()) {
                // remember the current biome (unless rebuilding)
                new QueryFactory(plugin).saveBiome(tardis.getTardis_id(), biome.toString());
            }
            if (tardis.getAdaption().equals(ADAPTION.BIOME)) {
                preset = adapt(biome, tardis.getAdaption());
            }
            PRESET demat = tardis.getDemat();
            Material cham_id = Material.LIGHT_GRAY_TERRACOTTA;
            if ((tardis.getAdaption().equals(ADAPTION.BIOME) && preset.equals(PRESET.FACTORY)) || tardis.getAdaption().equals(ADAPTION.BLOCK) || preset.equals(PRESET.SUBMERGED)) {
                Block chameleonBlock;
                // chameleon circuit is on - get block under TARDIS
                if (bd.getLocation().getBlock().getType() == Material.SNOW) {
                    chameleonBlock = bd.getLocation().getBlock();
                } else {
                    chameleonBlock = bd.getLocation().getBlock().getRelative(BlockFace.DOWN);
                }
                // determine cham_id
                TARDISChameleonCircuit tcc = new TARDISChameleonCircuit(plugin);
                cham_id = tcc.getChameleonBlock(chameleonBlock, bd.getPlayer());
            }
            boolean hidden = tardis.isHidden();
            // get submarine preferences
            if (bd.isSubmarine() && notSubmarinePresets.contains(preset)) {
                preset = PRESET.YELLOW;
                TARDISMessage.send(bd.getPlayer().getPlayer(), "SUB_UNSUITED");
            }
            /*
             * We can always add the chunk, as List.remove() only removes the
             * first occurrence - and we want the chunk to remain loaded if there
             * are other Police Boxes in it.
             */
            while (!thisChunk.isLoaded()) {
                thisChunk.load();
            }
            plugin.getGeneralKeeper().getTardisChunkList().add(thisChunk);
            if (bd.isRebuild()) {
                // always destroy it first as the player may just be switching presets
                if (!hidden) {
                    TARDISDeinstaPreset deinsta = new TARDISDeinstaPreset(plugin);
                    deinsta.instaDestroyPreset(bd, false, demat);
                }
                plugin.getTrackerKeeper().getMaterialising().add(bd.getTardisID());
                TARDISMaterialisationPreset runnable = new TARDISMaterialisationPreset(plugin, bd, preset, cham_id.createBlockData(), tardis.getAdaption(), 3);
                int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
                runnable.setTask(taskID);
                TARDISSounds.playTARDISSound(bd.getLocation(), "tardis_land_fast");
                if (plugin.getUtils().inTARDISWorld(bd.getPlayer().getPlayer())) {
                    TARDISSounds.playTARDISSound(bd.getPlayer().getPlayer().getLocation(), "tardis_land_fast");
                }
            } else if (!preset.equals(PRESET.INVISIBLE)) {
                plugin.getTrackerKeeper().getMaterialising().add(bd.getTardisID());
                if (preset.equals(PRESET.JUNK)) {
                    TARDISJunkBuilder runnable = new TARDISJunkBuilder(plugin, bd);
                    int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
                    runnable.setTask(taskID);
                } else {
                    TARDISMaterialisationPreset runnable = new TARDISMaterialisationPreset(plugin, bd, preset, cham_id.createBlockData(), tardis.getAdaption(), 18);
                    int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
                    runnable.setTask(taskID);
                }
            } else {
                Material id = cham_id;
                // delay by the usual time so handbrake message shows after materialisation sound
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    plugin.getTrackerKeeper().getMaterialising().add(bd.getTardisID());
                    new TARDISInstaPreset(plugin, bd, PRESET.INVISIBLE, id.createBlockData(), false).buildPreset();
                }, 375L);
            }
            // update demat so it knows about the current preset after it has changed
            HashMap<String, Object> whered = new HashMap<>();
            whered.put("tardis_id", bd.getTardisID());
            HashMap<String, Object> set = new HashMap<>();
            set.put("chameleon_demat", preset.toString());
            new QueryFactory(plugin).doUpdate("tardis", set, whered);
        }
    }

    private PRESET adapt(Biome biome, ADAPTION adaption) {
        if (adaption.equals(ADAPTION.BLOCK)) {
            return PRESET.OLD;
        } else {
            switch (biome) {
                case BEACH:
                case FROZEN_RIVER:
                case RIVER:
                case SNOWY_BEACH:
                    return PRESET.BOAT;
                case COLD_OCEAN:
                case DEEP_COLD_OCEAN:
                case DEEP_LUKEWARM_OCEAN:
                case DEEP_OCEAN:
                case DEEP_WARM_OCEAN:
                case FROZEN_OCEAN:
                case LUKEWARM_OCEAN:
                case OCEAN:
                case WARM_OCEAN:
                    return PRESET.YELLOW;
                case DESERT:
                case DESERT_HILLS:
                case DESERT_LAKES:
                    return PRESET.DESERT;
                case GRAVELLY_MOUNTAINS:
                case MODIFIED_GRAVELLY_MOUNTAINS:
                case MOUNTAINS:
                case SNOWY_MOUNTAINS:
                case WOODED_MOUNTAINS:
                    return PRESET.EXTREME_HILLS;
                case BIRCH_FOREST:
                case BIRCH_FOREST_HILLS:
                case FOREST:
                case TALL_BIRCH_FOREST:
                case TALL_BIRCH_HILLS:
                    return PRESET.FOREST;
                case NETHER:
                    return PRESET.NETHER;
                case SNOWY_TUNDRA:
                case DEEP_FROZEN_OCEAN:
                    return PRESET.ICE_FLATS;
                case ICE_SPIKES:
                    return PRESET.ICE_SPIKES;
                case JUNGLE:
                case JUNGLE_EDGE:
                case JUNGLE_HILLS:
                case MODIFIED_JUNGLE:
                case MODIFIED_JUNGLE_EDGE:
                    return PRESET.JUNGLE;
                case BADLANDS:
                case BADLANDS_PLATEAU:
                case ERODED_BADLANDS:
                case MODIFIED_BADLANDS_PLATEAU:
                case MODIFIED_WOODED_BADLANDS_PLATEAU:
                case WOODED_BADLANDS_PLATEAU:
                    return PRESET.MESA;
                case MUSHROOM_FIELDS:
                case MUSHROOM_FIELD_SHORE:
                    return PRESET.SHROOM;
                case PLAINS:
                case SUNFLOWER_PLAINS:
                    return PRESET.PLAINS;
                case DARK_FOREST:
                case DARK_FOREST_HILLS:
                    return PRESET.ROOFED_FOREST;
                case SAVANNA:
                case SHATTERED_SAVANNA:
                case SAVANNA_PLATEAU:
                case SHATTERED_SAVANNA_PLATEAU:
                    return PRESET.SAVANNA;
                case SWAMP:
                case SWAMP_HILLS:
                    return PRESET.SWAMP;
                case END_BARRENS:
                case END_HIGHLANDS:
                case END_MIDLANDS:
                case SMALL_END_ISLANDS:
                case THE_END:
                    return PRESET.THEEND;
                case GIANT_SPRUCE_TAIGA:
                case GIANT_SPRUCE_TAIGA_HILLS:
                case GIANT_TREE_TAIGA:
                case GIANT_TREE_TAIGA_HILLS:
                case TAIGA:
                case TAIGA_HILLS:
                case TAIGA_MOUNTAINS:
                    return PRESET.TAIGA;
                case SNOWY_TAIGA:
                case SNOWY_TAIGA_HILLS:
                case SNOWY_TAIGA_MOUNTAINS:
                    return PRESET.COLD_TAIGA;
                default:
                    return PRESET.FACTORY;
            }
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
