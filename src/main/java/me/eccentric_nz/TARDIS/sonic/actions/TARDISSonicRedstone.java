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
package me.eccentric_nz.TARDIS.sonic.actions;

import com.google.common.collect.Sets;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.custommodeldata.TARDISMushroomBlock;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.*;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Set;

public class TARDISSonicRedstone {

	private static final Set<Material> PISTON_NO_MOVE = Sets.newHashSet(Material.PISTON_HEAD, Material.ACACIA_SIGN, Material.ACACIA_WALL_SIGN, Material.ANVIL, Material.BARREL, Material.BARRIER, Material.BEACON, Material.BEDROCK, Material.BEDROCK, Material.BEE_NEST, Material.BEEHIVE, Material.BIRCH_SIGN, Material.BIRCH_WALL_SIGN, Material.BLACK_BANNER, Material.BLACK_WALL_BANNER, Material.BLAST_FURNACE, Material.BLUE_BANNER, Material.BLUE_WALL_BANNER, Material.BREWING_STAND, Material.BROWN_BANNER, Material.BROWN_WALL_BANNER, Material.CAMPFIRE, Material.CHEST, Material.CHIPPED_ANVIL, Material.COMMAND_BLOCK, Material.CRYING_OBSIDIAN, Material.CYAN_BANNER, Material.CYAN_WALL_BANNER, Material.DAMAGED_ANVIL, Material.DARK_OAK_SIGN, Material.DARK_OAK_WALL_SIGN, Material.DAYLIGHT_DETECTOR, Material.DISPENSER, Material.DROPPER, Material.ENCHANTING_TABLE, Material.END_GATEWAY, Material.END_PORTAL, Material.END_PORTAL_FRAME, Material.ENDER_CHEST, Material.FURNACE, Material.GRAY_BANNER, Material.GRAY_WALL_BANNER, Material.GREEN_BANNER, Material.GREEN_WALL_BANNER, Material.HOPPER, Material.JIGSAW, Material.JUKEBOX, Material.JUNGLE_SIGN, Material.JUNGLE_WALL_SIGN, Material.LECTERN, Material.LIGHT_BLUE_BANNER, Material.LIGHT_BLUE_WALL_BANNER, Material.LIGHT_GRAY_BANNER, Material.LIGHT_GRAY_WALL_BANNER, Material.LIME_BANNER, Material.LIME_WALL_BANNER, Material.LODESTONE, Material.MAGENTA_BANNER, Material.MAGENTA_WALL_BANNER, Material.NETHER_PORTAL, Material.OAK_SIGN, Material.OAK_WALL_SIGN, Material.OBSIDIAN, Material.ORANGE_BANNER, Material.ORANGE_WALL_BANNER, Material.PINK_BANNER, Material.PINK_WALL_BANNER, Material.PURPLE_BANNER, Material.PURPLE_WALL_BANNER, Material.RED_BANNER, Material.RED_WALL_BANNER, Material.SMOKER, Material.SOUL_CAMPFIRE, Material.SPAWNER, Material.SPRUCE_SIGN, Material.SPRUCE_WALL_SIGN, Material.TRAPPED_CHEST, Material.WHITE_BANNER, Material.WHITE_WALL_BANNER, Material.YELLOW_BANNER, Material.YELLOW_WALL_BANNER);

	public static void togglePoweredState(TARDIS plugin, Player player, Block block) {
		// not protected blocks - WorldGuard / GriefPrevention / Lockette / Towny
		if (TARDISSonicRespect.checkBlockRespect(plugin, player, block)) {
			TARDISSonicSound.playSonicSound(plugin, player, System.currentTimeMillis(), 600L, "sonic_short");
			Material blockType = block.getType();
			// do redstone activation
			switch (blockType) {
				case DETECTOR_RAIL:
				case POWERED_RAIL:
					RedstoneRail rail = (RedstoneRail) block.getBlockData();
					if (plugin.getGeneralKeeper().getSonicRails().contains(block.getLocation().toString())) {
						plugin.getGeneralKeeper().getSonicRails().remove(block.getLocation().toString());
						rail.setPowered(false);
					} else {
						plugin.getGeneralKeeper().getSonicRails().add(block.getLocation().toString());
						rail.setPowered(true);
					}
					block.setBlockData(rail, true);
					break;
				case IRON_DOOR:
					// get bottom door block
					Block tmp = block;
					Bisected bisected = (Bisected) block.getBlockData();
					if (bisected.getHalf().equals(Bisected.Half.TOP)) {
						tmp = block.getRelative(BlockFace.DOWN);
					}
					// not TARDIS doors!
					String doorloc = tmp.getLocation().getWorld().getName() + ":" + tmp.getLocation().getBlockX() + ":" + tmp.getLocation().getBlockY() + ":" + tmp.getLocation().getBlockZ();
					HashMap<String, Object> wheredoor = new HashMap<>();
					wheredoor.put("door_location", doorloc);
					ResultSetDoors rsd = new ResultSetDoors(plugin, wheredoor, false);
					if (rsd.resultSet()) {
						return;
					}
					if (!plugin.getTrackerKeeper().getSonicDoors().contains(player.getUniqueId())) {
						plugin.getTrackerKeeper().getSonicDoors().add(player.getUniqueId());
						Block door_bottom = tmp;
						Openable openable = (Openable) door_bottom.getBlockData();
						openable.setOpen(true);
						door_bottom.setBlockData(openable, true);
						// return the door to its previous state after 3 seconds
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							openable.setOpen(false);
							door_bottom.setBlockData(openable, true);
							plugin.getTrackerKeeper().getSonicDoors().remove(player.getUniqueId());
						}, 60L);
					}
					break;
				case IRON_TRAPDOOR:
					TrapDoor trapDoor = (TrapDoor) block.getBlockData();
					trapDoor.setOpen(!trapDoor.isOpen());
					block.setBlockData(trapDoor, true);
					break;
				case PISTON:
				case STICKY_PISTON:
					Piston piston = (Piston) block.getBlockData();
					// find the direction the piston is facing
					if (plugin.getGeneralKeeper().getSonicPistons().contains(block.getLocation().toString())) {
						plugin.getGeneralKeeper().getSonicPistons().remove(block.getLocation().toString());
						for (BlockFace f : plugin.getGeneralKeeper().getBlockFaces()) {
							if (block.getRelative(f).getType().isAir()) {
								// force a block update
								block.getRelative(f).setBlockData(TARDISConstants.VOID_AIR, true);
								block.getRelative(f).setBlockData(TARDISConstants.AIR, true);
								break;
							}
						}
					} else if (setExtension(plugin, block)) {
						piston.setExtended(true);
						block.setBlockData(piston, true);
						player.playSound(block.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1.0f, 1.0f);
					}
					break;
				case REDSTONE_LAMP:
					Lightable lightable = (Lightable) block.getBlockData();
					if (!lightable.isLit()) {
						plugin.getGeneralKeeper().getSonicLamps().add(block.getLocation().toString());
						for (BlockFace f : plugin.getGeneralKeeper().getBlockFaces()) {
							if (block.getRelative(f).getType().isAir()) {
								// force a block update
								block.getRelative(f).setBlockData(TARDISConstants.POWER, true);
								lightable.setLit(true);
								block.setBlockData(lightable, true);
								block.getRelative(f).setBlockData(TARDISConstants.AIR, true);
								break;
							}
						}
					} else if (plugin.getGeneralKeeper().getSonicLamps().contains(block.getLocation().toString())) {
						plugin.getGeneralKeeper().getSonicLamps().remove(block.getLocation().toString());
						lightable.setLit(false);
						block.setBlockData(lightable, true);
					}
					break;
				case REDSTONE_WIRE:
					RedstoneWire wire = (RedstoneWire) block.getBlockData();
					if (plugin.getGeneralKeeper().getSonicWires().contains(block.getLocation().toString())) {
						plugin.getGeneralKeeper().getSonicWires().remove(block.getLocation().toString());
						wire.setPower(0);
						plugin.getGeneralKeeper().getBlockFaces().forEach((f) -> {
							if (block.getRelative(f).getType().equals(Material.REDSTONE_WIRE)) {
								wire.setPower(0);
							}
						});
					} else {
						plugin.getGeneralKeeper().getSonicWires().add(block.getLocation().toString());
						wire.setPower(15);
						plugin.getGeneralKeeper().getBlockFaces().forEach((f) -> {
							if (block.getRelative(f).getType().equals(Material.REDSTONE_WIRE)) {
								wire.setPower(13);
							}
						});
					}
					block.setBlockData(wire, true);
					break;
				case MUSHROOM_STEM:
					// check the block is a chemistry lamp block
					MultipleFacing multipleFacing = (MultipleFacing) block.getBlockData();
					if (TARDISMushroomBlock.isChemistryStemOn(multipleFacing)) {
						multipleFacing = TARDISMushroomBlock.getChemistryStemOff(multipleFacing);
						// delete light source
						plugin.getTardisHelper().deleteLight(block.getLocation());
					} else if (TARDISMushroomBlock.isChemistryStemOff(multipleFacing)) {
						multipleFacing = TARDISMushroomBlock.getChemistryStemOn(multipleFacing);
						// create light source
						plugin.getTardisHelper().createLight(block.getLocation());
					}
					block.setBlockData(multipleFacing, true);
					break;
				default:
					break;
			}
		}
	}

	public static boolean setExtension(TARDIS plugin, Block b) {
		BlockFace face = ((Piston) b.getBlockData()).getFacing();
		Block l = b.getRelative(face);
		Material mat = l.getType();
		// check if there is a block there
		if (!PISTON_NO_MOVE.contains(mat)) {
			if (mat.isAir()) {
				extend(plugin, b, l);
				return true;
			} else {
				// check the block further on for AIR
				Block two = b.getRelative(face, 2);
				if (two.getType().isAir()) {
					two.setBlockData(mat.createBlockData());
					extend(plugin, b, l);
					return true;
				}
			}
		}
		return false;
	}

	private static void extend(TARDIS plugin, Block b, Block l) {
		plugin.getGeneralKeeper().getSonicPistons().add(b.getLocation().toString());
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			PistonHead pistonHead = (PistonHead) Material.PISTON_HEAD.createBlockData();
			if (b.getType().equals(Material.STICKY_PISTON)) {
				pistonHead.setType(TechnicalPiston.Type.STICKY);
			}
			Piston piston = (Piston) b.getBlockData();
			pistonHead.setFacing(piston.getFacing());
			l.setBlockData(pistonHead);
			piston.setExtended(true);
			b.setBlockData(piston, true);
		}, 3L);
	}
}
