/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.builders;

import me.eccentric_nz.tardis.TardisConstants;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.chameleon.TardisChameleonCircuit;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.destroyers.TardisDeinstantPreset;
import me.eccentric_nz.tardis.enumeration.Adaption;
import me.eccentric_nz.tardis.enumeration.CardinalDirection;
import me.eccentric_nz.tardis.enumeration.Preset;
import me.eccentric_nz.tardis.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.tardis.junk.TardisJunkBuilder;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.planets.TardisBiome;
import me.eccentric_nz.tardis.utility.TardisSounds;
import me.eccentric_nz.tardis.utility.TardisStaticUtils;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * The Wibbly lever was a part of The Doctor's tardis console. The lever had at least two functions: opening and closing
 * doors and controlling implosions used to revert paradoxes in which the tardis had materialised within itself.
 *
 * @author eccentric_nz
 */
public class TardisPresetBuilderFactory {

    final List<Preset> no_block_under_door;
    private final TardisPlugin plugin;
    private final HashMap<CardinalDirection, BlockFace[]> face_map = new HashMap<>();
    private final List<Preset> notSubmarinePresets;

    public TardisPresetBuilderFactory(TardisPlugin plugin) {
        this.plugin = plugin;
        face_map.put(CardinalDirection.SOUTH, new BlockFace[]{BlockFace.SOUTH_WEST, BlockFace.SOUTH_SOUTH_WEST, BlockFace.SOUTH, BlockFace.SOUTH_SOUTH_EAST, BlockFace.SOUTH_EAST});
        face_map.put(CardinalDirection.EAST, new BlockFace[]{BlockFace.SOUTH_EAST, BlockFace.EAST_SOUTH_EAST, BlockFace.EAST, BlockFace.EAST_NORTH_EAST, BlockFace.NORTH_EAST});
        face_map.put(CardinalDirection.NORTH, new BlockFace[]{BlockFace.NORTH_EAST, BlockFace.NORTH_NORTH_EAST, BlockFace.NORTH, BlockFace.NORTH_NORTH_WEST, BlockFace.NORTH_WEST});
        face_map.put(CardinalDirection.WEST, new BlockFace[]{BlockFace.NORTH_WEST, BlockFace.WEST_NORTH_WEST, BlockFace.WEST, BlockFace.WEST_SOUTH_WEST, BlockFace.SOUTH_WEST});
        no_block_under_door = new ArrayList<>();
        no_block_under_door.add(Preset.ANGEL);
        no_block_under_door.add(Preset.DUCK);
        no_block_under_door.add(Preset.GAZEBO);
        no_block_under_door.add(Preset.HELIX);
        no_block_under_door.add(Preset.LIBRARY);
        no_block_under_door.add(Preset.PANDORICA);
        no_block_under_door.add(Preset.ROBOT);
        no_block_under_door.add(Preset.SWAMP);
        no_block_under_door.add(Preset.TORCH);
        no_block_under_door.add(Preset.WELL);
        notSubmarinePresets = new ArrayList<>();
        notSubmarinePresets.add(Preset.LAMP);
        notSubmarinePresets.add(Preset.MINESHAFT);
    }

    /**
     * Builds the TARDIS Police Box.
     *
     * @param bd the TARDIS build data
     */
    public void buildPreset(BuildData bd) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", bd.getTardisId());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            Preset preset = tardis.getPreset();
            TardisBiome biome;
            // keep the chunk this Police box is in loaded
            Chunk thisChunk = bd.getLocation().getChunk();
            while (!thisChunk.isLoaded()) {
                thisChunk.load();
            }
            if (bd.isRebuild()) {
                biome = TardisStaticUtils.getBiomeAt(Objects.requireNonNull(bd.getLocation().getWorld()).getBlockAt(bd.getLocation()).getRelative(getOppositeFace(bd.getDirection()), 2).getLocation());
            } else {
                biome = TardisStaticUtils.getBiomeAt(bd.getLocation());
                // disable force field
                if (plugin.getTrackerKeeper().getActiveForceFields().containsKey(tardis.getUuid())) {
                    plugin.getTrackerKeeper().getActiveForceFields().remove(tardis.getUuid());
                    TardisMessage.send(bd.getPlayer().getPlayer(), "FORCE_FIELD", "OFF");
                }
            }
            if (tardis.getAdaption().equals(Adaption.BIOME)) {
                preset = adapt(biome, tardis.getAdaption());
            }
            Preset demat = tardis.getDemat();
            Material chameleonMaterial = Material.LIGHT_GRAY_TERRACOTTA;
            if ((tardis.getAdaption().equals(Adaption.BIOME) && preset.equals(Preset.FACTORY)) || tardis.getAdaption().equals(Adaption.BLOCK) || preset.equals(Preset.SUBMERGED)) {
                Block chameleonBlock;
                // chameleon circuit is on - get block under TARDIS
                if (bd.getLocation().getBlock().getType() == Material.SNOW) {
                    chameleonBlock = bd.getLocation().getBlock();
                } else {
                    chameleonBlock = bd.getLocation().getBlock().getRelative(BlockFace.DOWN);
                }
                // determine chameleonMaterial
                TardisChameleonCircuit tcc = new TardisChameleonCircuit(plugin);
                chameleonMaterial = tcc.getChameleonBlock(chameleonBlock, bd.getPlayer());
            }
            boolean hidden = tardis.isHidden();
            // get submarine preferences
            if (bd.isSubmarine() && notSubmarinePresets.contains(preset)) {
                preset = Preset.YELLOW;
                TardisMessage.send(bd.getPlayer().getPlayer(), "SUB_UNSUITED");
            }
            /*
             * We can always add the chunk, as List.remove() only removes the
             * first occurrence - and we want the chunk to remain loaded if there
             * are other Police Boxes in it.
             */
            while (!thisChunk.isLoaded()) {
                thisChunk.load();
            }
            thisChunk.setForceLoaded(true);
            if (bd.isRebuild()) {
                bd.setThrottle(SpaceTimeThrottle.REBUILD);
                // always destroy it first as the player may just be switching presets
                if (!hidden) {
                    TardisDeinstantPreset deinsta = new TardisDeinstantPreset(plugin);
                    deinsta.instaDestroyPreset(bd, false, demat);
                }
                plugin.getTrackerKeeper().getMaterialising().add(bd.getTardisId());
                int taskID;
                if (preset.usesItemFrame()) {
                    TardisMaterialisePoliceBox frame = new TardisMaterialisePoliceBox(plugin, bd, preset);
                    taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, frame, 10L, 20L);
                    frame.setTask(taskID);
                } else {
                    TardisMaterialisePreset runnable = new TardisMaterialisePreset(plugin, bd, preset, chameleonMaterial.createBlockData(), tardis.getAdaption());
                    taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
                    runnable.setTask(taskID);
                }
                TardisSounds.playTARDISSound(bd.getLocation(), "tardis_land_fast");
                if (bd.getPlayer().getPlayer() != null && plugin.getUtils().inTARDISWorld(bd.getPlayer().getPlayer())) {
                    TardisSounds.playTARDISSound(bd.getPlayer().getPlayer().getLocation(), "tardis_land_fast");
                }
            } else if (!preset.equals(Preset.INVISIBLE)) {
                plugin.getTrackerKeeper().getMaterialising().add(bd.getTardisId());
                if (preset.equals(Preset.JUNK)) {
                    TardisJunkBuilder runnable = new TardisJunkBuilder(plugin, bd);
                    int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
                    runnable.setTask(taskID);
                } else {
                    int taskID;
                    if (preset.usesItemFrame()) {
                        TardisMaterialisePoliceBox frame = new TardisMaterialisePoliceBox(plugin, bd, preset);
                        taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, frame, 10L, 20L);
                        frame.setTask(taskID);
                    } else {
                        TardisMaterialisePreset runnable = new TardisMaterialisePreset(plugin, bd, preset, chameleonMaterial.createBlockData(), tardis.getAdaption());
                        taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
                        runnable.setTask(taskID);
                    }
                }
            } else {
                Material material = chameleonMaterial;
                // delay by the usual time so handbrake message shows after materialisation sound
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    plugin.getTrackerKeeper().getMaterialising().add(bd.getTardisId());
                    new TardisInstantPreset(plugin, bd, Preset.INVISIBLE, material.createBlockData(), false).buildPreset();
                }, 375L);
            }
            // update demat so it knows about the current preset after it has changed
            HashMap<String, Object> whered = new HashMap<>();
            whered.put("tardis_id", bd.getTardisId());
            HashMap<String, Object> set = new HashMap<>();
            set.put("chameleon_demat", preset.toString());
            plugin.getQueryFactory().doUpdate("tardis", set, whered);
        }
    }

    private Preset adapt(TardisBiome biome, Adaption adaption) {
        if (adaption.equals(Adaption.BLOCK)) {
            return Preset.ADAPTIVE;
        } else {
            return switch (biome.name()) {
                case "BEACH", "FROZEN_RIVER", "RIVER", "SNOWY_BEACH" -> Preset.BOAT;
                case "COLD_OCEAN", "DEEP_COLD_OCEAN", "DEEP_LUKEWARM_OCEAN", "DEEP_OCEAN", "DEEP_WARM_OCEAN", "FROZEN_OCEAN", "LUKEWARM_OCEAN", "OCEAN", "WARM_OCEAN" -> Preset.YELLOW;
                case "DESERT", "DESERT_HILLS", "DESERT_LAKES" -> Preset.DESERT;
                case "GRAVELLY_MOUNTAINS", "MODIFIED_GRAVELLY_MOUNTAINS", "MOUNTAINS", "SNOWY_MOUNTAINS", "WOODED_MOUNTAINS" -> Preset.EXTREME_HILLS;
                case "BIRCH_FOREST", "BIRCH_FOREST_HILLS", "FOREST", "TALL_BIRCH_FOREST", "TALL_BIRCH_HILLS" -> Preset.FOREST;
                case "NETHER_WASTES", "SOUL_SAND_VALLEY", "CRIMSON_FOREST", "WARPED_FOREST", "BASALT_DELTAS" -> Preset.NETHER;
                case "SNOWY_TUNDRA", "DEEP_FROZEN_OCEAN" -> Preset.ICE_FLATS;
                case "ICE_SPIKES" -> Preset.ICE_SPIKES;
                case "JUNGLE", "JUNGLE_EDGE", "JUNGLE_HILLS", "MODIFIED_JUNGLE", "MODIFIED_JUNGLE_EDGE" -> Preset.JUNGLE;
                case "BADLANDS", "BADLANDS_PLATEAU", "ERODED_BADLANDS", "MODIFIED_BADLANDS_PLATEAU", "MODIFIED_WOODED_BADLANDS_PLATEAU", "WOODED_BADLANDS_PLATEAU" -> Preset.MESA;
                case "MUSHROOM_FIELDS", "MUSHROOM_FIELD_SHORE" -> Preset.SHROOM;
                case "PLAINS", "SUNFLOWER_PLAINS" -> Preset.PLAINS;
                case "DARK_FOREST", "DARK_FOREST_HILLS" -> Preset.ROOFED_FOREST;
                case "SAVANNA", "SHATTERED_SAVANNA", "SAVANNA_PLATEAU", "SHATTERED_SAVANNA_PLATEAU" -> Preset.SAVANNA;
                case "SWAMP", "SWAMP_HILLS" -> Preset.SWAMP;
                case "END_BARRENS", "END_HIGHLANDS", "END_MIDLANDS", "SMALL_END_ISLANDS", "THE_END" -> Preset.THEEND;
                case "GIANT_SPRUCE_TAIGA", "GIANT_SPRUCE_TAIGA_HILLS", "GIANT_TREE_TAIGA", "GIANT_TREE_TAIGA_HILLS", "TAIGA", "TAIGA_HILLS", "TAIGA_MOUNTAINS" -> Preset.TAIGA;
                case "SNOWY_TAIGA", "SNOWY_TAIGA_HILLS", "SNOWY_TAIGA_MOUNTAINS" -> Preset.COLD_TAIGA;
                default -> Preset.FACTORY;
            };
        }
    }

    BlockFace getSkullDirection(CardinalDirection d) {
        BlockFace[] faces = face_map.get(d);
        return faces[TardisConstants.RANDOM.nextInt(5)];
    }

    public BlockFace getOppositeFace(CardinalDirection d) {
        return switch (d) {
            case SOUTH -> BlockFace.NORTH;
            case WEST -> BlockFace.EAST;
            case NORTH -> BlockFace.SOUTH;
            default -> BlockFace.WEST;
        };
    }
}
