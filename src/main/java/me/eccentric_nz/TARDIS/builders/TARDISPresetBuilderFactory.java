/*
 * Copyright (C) 2023 eccentric_nz
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
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonCircuit;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.destroyers.TARDISDeinstantPreset;
import me.eccentric_nz.TARDIS.enumeration.Adaption;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.junk.TARDISJunkBuilder;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * The Wibbly lever was a part of The Doctor's TARDIS console. The lever had at least two functions: opening and closing
 * doors and controlling implosions used to revert paradoxes in which the TARDIS had materialised within itself.
 *
 * @author eccentric_nz
 */
public class TARDISPresetBuilderFactory {

    final List<ChameleonPreset> no_block_under_door;
    private final TARDIS plugin;
    private final HashMap<COMPASS, BlockFace[]> face_map = new HashMap<>();
    private final List<ChameleonPreset> notSubmarinePresets;

    public TARDISPresetBuilderFactory(TARDIS plugin) {
        this.plugin = plugin;
        face_map.put(COMPASS.SOUTH, new BlockFace[]{BlockFace.SOUTH_WEST, BlockFace.SOUTH_SOUTH_WEST, BlockFace.SOUTH, BlockFace.SOUTH_SOUTH_EAST, BlockFace.SOUTH_EAST});
        face_map.put(COMPASS.EAST, new BlockFace[]{BlockFace.SOUTH_EAST, BlockFace.EAST_SOUTH_EAST, BlockFace.EAST, BlockFace.EAST_NORTH_EAST, BlockFace.NORTH_EAST});
        face_map.put(COMPASS.NORTH, new BlockFace[]{BlockFace.NORTH_EAST, BlockFace.NORTH_NORTH_EAST, BlockFace.NORTH, BlockFace.NORTH_NORTH_WEST, BlockFace.NORTH_WEST});
        face_map.put(COMPASS.WEST, new BlockFace[]{BlockFace.NORTH_WEST, BlockFace.WEST_NORTH_WEST, BlockFace.WEST, BlockFace.WEST_SOUTH_WEST, BlockFace.SOUTH_WEST});
        no_block_under_door = new ArrayList<>();
        no_block_under_door.add(ChameleonPreset.ANGEL);
        no_block_under_door.add(ChameleonPreset.DUCK);
        no_block_under_door.add(ChameleonPreset.GAZEBO);
        no_block_under_door.add(ChameleonPreset.HELIX);
        no_block_under_door.add(ChameleonPreset.LIBRARY);
        no_block_under_door.add(ChameleonPreset.PANDORICA);
        no_block_under_door.add(ChameleonPreset.ROBOT);
        no_block_under_door.add(ChameleonPreset.SWAMP);
        no_block_under_door.add(ChameleonPreset.TORCH);
        no_block_under_door.add(ChameleonPreset.WELL);
        notSubmarinePresets = new ArrayList<>();
        notSubmarinePresets.add(ChameleonPreset.LAMP);
        notSubmarinePresets.add(ChameleonPreset.MINESHAFT);
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
            ChameleonPreset preset = tardis.getPreset();
            Biome biome;
            // keep the chunk this Police box is in loaded
            Chunk thisChunk = bd.getLocation().getChunk();
            while (!thisChunk.isLoaded()) {
                thisChunk.load();
            }
            if (bd.isRebuild()) {
                biome = bd.getLocation().getBlock().getRelative(getOppositeFace(bd.getDirection()), 2).getBiome();
            } else {
                biome = bd.getLocation().getBlock().getBiome();
                // disable force field
                if (plugin.getTrackerKeeper().getActiveForceFields().containsKey(tardis.getUuid())) {
                    plugin.getTrackerKeeper().getActiveForceFields().remove(tardis.getUuid());
                    TARDISMessage.send(bd.getPlayer().getPlayer(), "FORCE_FIELD", "OFF");
                }
            }
            if (tardis.getAdaption().equals(Adaption.BIOME)) {
                preset = adapt(biome, tardis.getAdaption());
            }
            ChameleonPreset demat = tardis.getDemat();
            Material chameleonMaterial = Material.LIGHT_GRAY_TERRACOTTA;
            if ((tardis.getAdaption().equals(Adaption.BIOME) && preset.equals(ChameleonPreset.FACTORY)) || tardis.getAdaption().equals(Adaption.BLOCK) || preset.equals(ChameleonPreset.SUBMERGED)) {
                Block chameleonBlock;
                // chameleon circuit is on - get block under TARDIS
                if (bd.getLocation().getBlock().getType() == Material.SNOW) {
                    chameleonBlock = bd.getLocation().getBlock();
                } else {
                    chameleonBlock = bd.getLocation().getBlock().getRelative(BlockFace.DOWN);
                }
                // determine chameleonMaterial
                TARDISChameleonCircuit tcc = new TARDISChameleonCircuit(plugin);
                chameleonMaterial = tcc.getChameleonBlock(chameleonBlock, bd.getPlayer());
            }
            boolean hidden = tardis.isHidden();
            // get submarine preferences
            if (bd.isSubmarine() && notSubmarinePresets.contains(preset)) {
                preset = ChameleonPreset.YELLOW;
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
            if (plugin.getConfig().getBoolean("police_box.keep_chunk_force_loaded")) {
                thisChunk.setForceLoaded(true);
            }
            if (bd.isRebuild()) {
                bd.setThrottle(SpaceTimeThrottle.REBUILD);
                // always destroy it first as the player may just be switching presets
                if (!hidden) {
                    TARDISDeinstantPreset deinsta = new TARDISDeinstantPreset(plugin);
                    deinsta.instaDestroyPreset(bd, false, demat);
                }
                plugin.getTrackerKeeper().getMaterialising().add(bd.getTardisID());
                int taskID;
                if (preset.usesItemFrame()) {
                    TARDISMaterialisePoliceBox frame = new TARDISMaterialisePoliceBox(plugin, bd, preset);
                    taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, frame, 10L, 20L);
                    frame.setTask(taskID);
                } else {
                    TARDISMaterialisePreset runnable = new TARDISMaterialisePreset(plugin, bd, preset, chameleonMaterial.createBlockData(), tardis.getAdaption());
                    taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
                    runnable.setTask(taskID);
                }
                TARDISSounds.playTARDISSound(bd.getLocation(), "tardis_land_fast");
                if (bd.getPlayer().getPlayer() != null && plugin.getUtils().inTARDISWorld(bd.getPlayer().getPlayer())) {
                    TARDISSounds.playTARDISSound(bd.getPlayer().getPlayer().getLocation(), "tardis_land_fast");
                }
            } else if (!preset.equals(ChameleonPreset.INVISIBLE)) {
                plugin.getTrackerKeeper().getMaterialising().add(bd.getTardisID());
                if (preset.equals(ChameleonPreset.JUNK)) {
                    TARDISJunkBuilder runnable = new TARDISJunkBuilder(plugin, bd);
                    int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
                    runnable.setTask(taskID);
                } else {
                    int taskID;
                    if (preset.usesItemFrame()) {
                        TARDISMaterialisePoliceBox frame = new TARDISMaterialisePoliceBox(plugin, bd, preset);
                        taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, frame, 10L, 20L);
                        frame.setTask(taskID);
                    } else {
                        TARDISMaterialisePreset runnable = new TARDISMaterialisePreset(plugin, bd, preset, chameleonMaterial.createBlockData(), tardis.getAdaption());
                        taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
                        runnable.setTask(taskID);
                    }
                }
            } else {
                Material material = chameleonMaterial;
                // delay by the usual time so handbrake message shows after materialisation sound
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    plugin.getTrackerKeeper().getMaterialising().add(bd.getTardisID());
                    new TARDISInstantPreset(plugin, bd, ChameleonPreset.INVISIBLE, material.createBlockData(), false).buildPreset();
                }, 375L);
            }
            // update demat so it knows about the current preset after it has changed
            HashMap<String, Object> whered = new HashMap<>();
            whered.put("tardis_id", bd.getTardisID());
            HashMap<String, Object> set = new HashMap<>();
            set.put("chameleon_demat", preset.toString());
            plugin.getQueryFactory().doUpdate("tardis", set, whered);
        }
    }

    private ChameleonPreset adapt(Biome biome, Adaption adaption) {
        if (adaption.equals(Adaption.BLOCK)) {
            return ChameleonPreset.ADAPTIVE;
        } else {
            try {
                return ChameleonPreset.valueOf(plugin.getAdaptiveConfig().getString(biome.toString()));
            } catch (IllegalArgumentException e) {
                return ChameleonPreset.FACTORY;
            }
        }
    }

    BlockFace getSkullDirection(COMPASS d) {
        BlockFace[] faces = face_map.get(d);
        return faces[TARDISConstants.RANDOM.nextInt(5)];
    }

    public BlockFace getOppositeFace(COMPASS d) {
        return switch (d) {
            case SOUTH -> BlockFace.NORTH;
            case WEST -> BlockFace.EAST;
            case NORTH -> BlockFace.SOUTH;
            default -> BlockFace.WEST;
        };
    }
}
