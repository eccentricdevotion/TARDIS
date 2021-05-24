/*
 * Copyright (C) 2020 eccentric_nz
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

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.chameleon.TARDISChameleonCircuit;
import me.eccentric_nz.tardis.database.data.TARDIS;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.destroyers.TARDISDeinstantPreset;
import me.eccentric_nz.tardis.enumeration.Adaption;
import me.eccentric_nz.tardis.enumeration.COMPASS;
import me.eccentric_nz.tardis.enumeration.PRESET;
import me.eccentric_nz.tardis.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.tardis.junk.TARDISJunkBuilder;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.planets.TARDISBiome;
import me.eccentric_nz.tardis.utility.TARDISSounds;
import me.eccentric_nz.tardis.utility.TARDISStaticUtils;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The Wibbly lever was a part of The Doctor's tardis console. The lever had at least two functions: opening and closing
 * doors and controlling implosions used to revert paradoxes in which the tardis had materialised within itself.
 *
 * @author eccentric_nz
 */
public class TARDISPresetBuilderFactory {

	final List<PRESET> no_block_under_door;
	private final TARDISPlugin plugin;
	private final HashMap<COMPASS, BlockFace[]> face_map = new HashMap<>();
	private final List<PRESET> notSubmarinePresets;

	public TARDISPresetBuilderFactory(TARDISPlugin plugin) {
		this.plugin = plugin;
		face_map.put(COMPASS.SOUTH, new BlockFace[]{BlockFace.SOUTH_WEST, BlockFace.SOUTH_SOUTH_WEST, BlockFace.SOUTH, BlockFace.SOUTH_SOUTH_EAST, BlockFace.SOUTH_EAST});
		face_map.put(COMPASS.EAST, new BlockFace[]{BlockFace.SOUTH_EAST, BlockFace.EAST_SOUTH_EAST, BlockFace.EAST, BlockFace.EAST_NORTH_EAST, BlockFace.NORTH_EAST});
		face_map.put(COMPASS.NORTH, new BlockFace[]{BlockFace.NORTH_EAST, BlockFace.NORTH_NORTH_EAST, BlockFace.NORTH, BlockFace.NORTH_NORTH_WEST, BlockFace.NORTH_WEST});
		face_map.put(COMPASS.WEST, new BlockFace[]{BlockFace.NORTH_WEST, BlockFace.WEST_NORTH_WEST, BlockFace.WEST, BlockFace.WEST_SOUTH_WEST, BlockFace.SOUTH_WEST});
		no_block_under_door = new ArrayList<>();
		no_block_under_door.add(PRESET.ANGEL);
		no_block_under_door.add(PRESET.DUCK);
		no_block_under_door.add(PRESET.GAZEBO);
		no_block_under_door.add(PRESET.HELIX);
		no_block_under_door.add(PRESET.LIBRARY);
		no_block_under_door.add(PRESET.PANDORICA);
		no_block_under_door.add(PRESET.ROBOT);
		no_block_under_door.add(PRESET.SWAMP);
		no_block_under_door.add(PRESET.TORCH);
		no_block_under_door.add(PRESET.WELL);
		notSubmarinePresets = new ArrayList<>();
		notSubmarinePresets.add(PRESET.LAMP);
		notSubmarinePresets.add(PRESET.MINESHAFT);
	}

	/**
	 * Builds the tardis Police Box.
	 *
	 * @param bd the tardis build data
	 */
	public void buildPreset(BuildData bd) {
		HashMap<String, Object> where = new HashMap<>();
		where.put("tardis_id", bd.getTardisId());
		ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
		if (rs.resultSet()) {
			TARDIS tardis = rs.getTardis();
			PRESET preset = tardis.getPreset();
			TARDISBiome biome;
			// keep the chunk this Police box is in loaded
			Chunk thisChunk = bd.getLocation().getChunk();
			while (!thisChunk.isLoaded()) {
				thisChunk.load();
			}
			if (bd.isRebuild()) {
				biome = TARDISStaticUtils.getBiomeAt(bd.getLocation().getWorld().getBlockAt(bd.getLocation()).getRelative(getOppositeFace(bd.getDirection()), 2).getLocation());
			} else {
				biome = TARDISStaticUtils.getBiomeAt(bd.getLocation());
				// disable force field
				if (plugin.getTrackerKeeper().getActiveForceFields().containsKey(tardis.getUuid())) {
					plugin.getTrackerKeeper().getActiveForceFields().remove(tardis.getUuid());
					TARDISMessage.send(bd.getPlayer().getPlayer(), "FORCE_FIELD", "OFF");
				}
			}
			bd.setTardisBiome(biome);
			if (plugin.getConfig().getBoolean("police_box.set_biome") && !bd.isRebuild()) {
				// remember the current biome (unless rebuilding)
				plugin.getQueryFactory().saveBiome(tardis.getTardisId(), biome.getKey().toString());
			}
			if (tardis.getAdaption().equals(Adaption.BIOME)) {
				preset = adapt(biome, tardis.getAdaption());
			}
			PRESET demat = tardis.getDemat();
			Material chameleonMaterial = Material.LIGHT_GRAY_TERRACOTTA;
			if ((tardis.getAdaption().equals(Adaption.BIOME) && preset.equals(PRESET.FACTORY)) || tardis.getAdaption().equals(Adaption.BLOCK) || preset.equals(PRESET.SUBMERGED)) {
				Block chameleonBlock;
				// chameleon circuit is on - get block under tardis
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
			thisChunk.setForceLoaded(true);
			if (bd.isRebuild()) {
				bd.setThrottle(SpaceTimeThrottle.REBUILD);
				// always destroy it first as the player may just be switching presets
				if (!hidden) {
					TARDISDeinstantPreset deinsta = new TARDISDeinstantPreset(plugin);
					deinsta.instaDestroyPreset(bd, false, demat);
				}
				plugin.getTrackerKeeper().getMaterialising().add(bd.getTardisId());
				int taskID;
				if (preset.isColoured()) {
					TARDISMaterialisePoliceBox frame = new TARDISMaterialisePoliceBox(plugin, bd, preset);
					taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, frame, 10L, 20L);
					frame.setTask(taskID);
				} else {
					TARDISMaterialisePreset runnable = new TARDISMaterialisePreset(plugin, bd, preset, chameleonMaterial.createBlockData(), tardis.getAdaption());
					taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
					runnable.setTask(taskID);
				}
				TARDISSounds.playTARDISSound(bd.getLocation(), "tardis_land_fast");
				if (plugin.getUtils().inTARDISWorld(bd.getPlayer().getPlayer())) {
					TARDISSounds.playTARDISSound(bd.getPlayer().getPlayer().getLocation(), "tardis_land_fast");
				}
			} else if (!preset.equals(PRESET.INVISIBLE)) {
				plugin.getTrackerKeeper().getMaterialising().add(bd.getTardisId());
				if (preset.equals(PRESET.JUNK)) {
					TARDISJunkBuilder runnable = new TARDISJunkBuilder(plugin, bd);
					int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
					runnable.setTask(taskID);
				} else {
					int taskID;
					if (preset.isColoured()) {
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
					plugin.getTrackerKeeper().getMaterialising().add(bd.getTardisId());
					new TARDISInstantPreset(plugin, bd, PRESET.INVISIBLE, material.createBlockData(), false).buildPreset();
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

	private PRESET adapt(TARDISBiome biome, Adaption adaption) {
		if (adaption.equals(Adaption.BLOCK)) {
			return PRESET.OLD;
		} else {
			return switch (biome.name()) {
				case "BEACH", "FROZEN_RIVER", "RIVER", "SNOWY_BEACH" -> PRESET.BOAT;
				case "COLD_OCEAN", "DEEP_COLD_OCEAN", "DEEP_LUKEWARM_OCEAN", "DEEP_OCEAN", "DEEP_WARM_OCEAN", "FROZEN_OCEAN", "LUKEWARM_OCEAN", "OCEAN", "WARM_OCEAN" -> PRESET.YELLOW;
				case "DESERT", "DESERT_HILLS", "DESERT_LAKES" -> PRESET.DESERT;
				case "GRAVELLY_MOUNTAINS", "MODIFIED_GRAVELLY_MOUNTAINS", "MOUNTAINS", "SNOWY_MOUNTAINS", "WOODED_MOUNTAINS" -> PRESET.EXTREME_HILLS;
				case "BIRCH_FOREST", "BIRCH_FOREST_HILLS", "FOREST", "TALL_BIRCH_FOREST", "TALL_BIRCH_HILLS" -> PRESET.FOREST;
				case "NETHER_WASTES", "SOUL_SAND_VALLEY", "CRIMSON_FOREST", "WARPED_FOREST", "BASALT_DELTAS" -> PRESET.NETHER;
				case "SNOWY_TUNDRA", "DEEP_FROZEN_OCEAN" -> PRESET.ICE_FLATS;
				case "ICE_SPIKES" -> PRESET.ICE_SPIKES;
				case "JUNGLE", "JUNGLE_EDGE", "JUNGLE_HILLS", "MODIFIED_JUNGLE", "MODIFIED_JUNGLE_EDGE" -> PRESET.JUNGLE;
				case "BADLANDS", "BADLANDS_PLATEAU", "ERODED_BADLANDS", "MODIFIED_BADLANDS_PLATEAU", "MODIFIED_WOODED_BADLANDS_PLATEAU", "WOODED_BADLANDS_PLATEAU" -> PRESET.MESA;
				case "MUSHROOM_FIELDS", "MUSHROOM_FIELD_SHORE" -> PRESET.SHROOM;
				case "PLAINS", "SUNFLOWER_PLAINS" -> PRESET.PLAINS;
				case "DARK_FOREST", "DARK_FOREST_HILLS" -> PRESET.ROOFED_FOREST;
				case "SAVANNA", "SHATTERED_SAVANNA", "SAVANNA_PLATEAU", "SHATTERED_SAVANNA_PLATEAU" -> PRESET.SAVANNA;
				case "SWAMP", "SWAMP_HILLS" -> PRESET.SWAMP;
				case "END_BARRENS", "END_HIGHLANDS", "END_MIDLANDS", "SMALL_END_ISLANDS", "THE_END" -> PRESET.THEEND;
				case "GIANT_SPRUCE_TAIGA", "GIANT_SPRUCE_TAIGA_HILLS", "GIANT_TREE_TAIGA", "GIANT_TREE_TAIGA_HILLS", "TAIGA", "TAIGA_HILLS", "TAIGA_MOUNTAINS" -> PRESET.TAIGA;
				case "SNOWY_TAIGA", "SNOWY_TAIGA_HILLS", "SNOWY_TAIGA_MOUNTAINS" -> PRESET.COLD_TAIGA;
				default -> PRESET.FACTORY;
			};
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
