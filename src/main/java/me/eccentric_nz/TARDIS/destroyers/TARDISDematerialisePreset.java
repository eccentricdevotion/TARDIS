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
package me.eccentric_nz.tardis.destroyers;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.chameleon.TARDISChameleonColumn;
import me.eccentric_nz.tardis.chameleon.TARDISConstructColumn;
import me.eccentric_nz.tardis.database.resultset.ResultSetDoors;
import me.eccentric_nz.tardis.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.tardis.enumeration.PRESET;
import me.eccentric_nz.tardis.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.utility.TARDISBlockSetters;
import me.eccentric_nz.tardis.utility.TARDISParticles;
import me.eccentric_nz.tardis.utility.TARDISSounds;
import me.eccentric_nz.tardis.utility.TARDISStaticLocationGetters;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

/**
 * A dematerialisation circuit was an essential part of a Type 40 tardis which enabled it to dematerialise from normal
 * space into the Time Vortex and rematerialise back from it.
 *
 * @author eccentric_nz
 */
class TARDISDematerialisePreset implements Runnable {

	private final TARDISPlugin plugin;
	private final DestroyData dd;
	private final int loops;
	private final PRESET preset;
	private final BlockData cham_id;
	private final TARDISChameleonColumn column;
	private final TARDISChameleonColumn stained_column;
	private final TARDISChameleonColumn glass_column;
	private int task;
	private int i;
	private BlockData the_colour;
	private BlockData stain_colour;

	/**
	 * Runnable method to dematerialise the tardis Police Box. Tries to mimic the transparency of dematerialisation by
	 * building the Police Box first with GLASS, then STAINED_GLASS, then the normal preset wall block.
	 *
	 * @param plugin  instance of the tardis plugin
	 * @param dd      the DestroyData
	 * @param preset  the Chameleon preset currently in use by the tardis
	 * @param cham_id the chameleon block id for the police box
	 */
	TARDISDematerialisePreset(TARDISPlugin plugin, DestroyData dd, PRESET preset, BlockData cham_id) {
		this.plugin = plugin;
		this.dd = dd;
		loops = dd.getThrottle().getLoops();
		this.preset = preset;
		i = 0;
		this.cham_id = cham_id;
		if (this.preset.equals(PRESET.CONSTRUCT)) {
			column = new TARDISConstructColumn(plugin, dd.getTardisId(), "blueprintData", dd.getDirection()).getColumn();
			stained_column = new TARDISConstructColumn(plugin, dd.getTardisId(), "stainData", dd.getDirection()).getColumn();
			glass_column = new TARDISConstructColumn(plugin, dd.getTardisId(), "glassData", dd.getDirection()).getColumn();
		} else {
			column = plugin.getPresets().getColumn(preset, dd.getDirection());
			stained_column = plugin.getPresets().getStained(preset, dd.getDirection());
			glass_column = plugin.getPresets().getGlass(preset, dd.getDirection());
		}
	}

	@Override
	public void run() {
		if (column == null || stained_column == null || glass_column == null) {
			plugin.getServer().getScheduler().cancelTask(task);
			task = 0;
			TARDISMessage.send(dd.getPlayer().getPlayer(), "INVALID_CONSTRUCT");
		}
		BlockData[][] data;
		// get relative locations
		int x = dd.getLocation().getBlockX(), plusx = dd.getLocation().getBlockX() + 1, minusx = dd.getLocation().getBlockX() - 1;
		int y;
		if (preset.equals(PRESET.SUBMERGED)) {
			y = dd.getLocation().getBlockY() - 1;
		} else {
			y = dd.getLocation().getBlockY();
		}
		int z = dd.getLocation().getBlockZ(), plusz = dd.getLocation().getBlockZ() + 1, minusz = dd.getLocation().getBlockZ() - 1;
		World world = dd.getLocation().getWorld();
		if (i < loops) {
			i++;
			// expand placed blocks to a police box
			data = switch (i % 3) {
				case 2 -> // stained
						stained_column.getBlockData();
				case 1 -> // glass
						glass_column.getBlockData();
				default -> // preset
						column.getBlockData();
			};
			// first run - play sound
			if (i == 1) {
				switch (preset) {
					case GRAVESTONE:
						// remove flower
						int flowerx;
						int flowery = (dd.getLocation().getBlockY() + 1);
						int flowerz;
						switch (dd.getDirection()) {
							case NORTH -> {
								flowerx = dd.getLocation().getBlockX();
								flowerz = dd.getLocation().getBlockZ() + 1;
							}
							case WEST -> {
								flowerx = dd.getLocation().getBlockX() + 1;
								flowerz = dd.getLocation().getBlockZ();
							}
							case SOUTH -> {
								flowerx = dd.getLocation().getBlockX();
								flowerz = dd.getLocation().getBlockZ() - 1;
							}
							default -> {
								flowerx = dd.getLocation().getBlockX() - 1;
								flowerz = dd.getLocation().getBlockZ();
							}
						}
						TARDISBlockSetters.setBlock(world, flowerx, flowery, flowerz, Material.AIR);
						break;
					case CAKE:
						plugin.getPresetDestroyer().destroyLamp(dd.getLocation(), preset);
						break;
					case JUNK_MODE:
						plugin.getPresetDestroyer().destroySign(dd.getLocation(), dd.getDirection(), preset);
						plugin.getPresetDestroyer().destroyHandbrake(dd.getLocation(), dd.getDirection());
						break;
					case SWAMP:
						plugin.getPresetDestroyer().destroySign(dd.getLocation(), dd.getDirection(), preset);
						break;
					case JAIL:
					case TOPSYTURVEY:
						// destroy door
						plugin.getPresetDestroyer().destroyDoor(dd.getTardisId());
					case MESA:
						// remove dead bushes
						int deadx;
						int bushx;
						int bushy = (dd.getLocation().getBlockY() + 3);
						int deadz;
						int bushz;
						switch (dd.getDirection()) {
							case NORTH -> {
								deadx = dd.getLocation().getBlockX() + 1;
								deadz = dd.getLocation().getBlockZ() + 1;
								bushx = dd.getLocation().getBlockX() - 1;
								bushz = dd.getLocation().getBlockZ();
							}
							case WEST -> {
								deadx = dd.getLocation().getBlockX() + 1;
								deadz = dd.getLocation().getBlockZ() - 1;
								bushx = dd.getLocation().getBlockX();
								bushz = dd.getLocation().getBlockZ() + 1;
							}
							case SOUTH -> {
								deadx = dd.getLocation().getBlockX() - 1;
								deadz = dd.getLocation().getBlockZ() - 1;
								bushx = dd.getLocation().getBlockX() + 1;
								bushz = dd.getLocation().getBlockZ();
							}
							default -> {
								deadx = dd.getLocation().getBlockX() - 1;
								deadz = dd.getLocation().getBlockZ() + 1;
								bushx = dd.getLocation().getBlockX();
								bushz = dd.getLocation().getBlockZ() - 1;
							}
						}
						TARDISBlockSetters.setBlock(world, deadx, bushy, deadz, Material.AIR);
						TARDISBlockSetters.setBlock(world, bushx, bushy, bushz, Material.AIR);
						break;
					default:
						break;
				}
				// only play the sound if the player is outside the tardis
				if (dd.isOutside()) {
					ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, dd.getPlayer().getUniqueId().toString());
					boolean minecart = false;
					SpaceTimeThrottle spaceTimeThrottle = SpaceTimeThrottle.NORMAL;
					if (rsp.resultSet()) {
						minecart = rsp.isMinecartOn();
						spaceTimeThrottle = SpaceTimeThrottle.getByDelay().get(rsp.getThrottle());
					}
					if (!minecart) {
						String sound;
						if (preset.equals(PRESET.JUNK_MODE)) {
							sound = "junk_takeoff";
						} else {
							sound = switch (spaceTimeThrottle) {
								case WARP, RAPID, FASTER -> "tardis_takeoff_" + spaceTimeThrottle.toString().toLowerCase();
								default -> // NORMAL
										"tardis_takeoff";
							};
						}
						TARDISSounds.playTARDISSound(dd.getLocation(), sound);
					} else {
						world.playSound(dd.getLocation(), Sound.ENTITY_MINECART_INSIDE, 1.0F, 0.0F);
					}
				}
				getColours(dd.getTardisId(), preset);
			} else if (preset.equals(PRESET.JUNK_MODE) && plugin.getConfig().getBoolean("junk.particles")) {
				// animate particles
				plugin.getUtils().getJunkTravellers(dd.getLocation()).forEach((e) -> {
					if (e instanceof Player p) {
						Location effectsLoc = dd.getLocation().clone().add(0.5d, 0, 0.5d);
						TARDISParticles.sendVortexParticles(effectsLoc, p);
					}
				});
			} else {
				// just change the walls
				int xx, zz;
				for (int n = 0; n < 9; n++) {
					BlockData[] colData = data[n];
					switch (n) {
						case 0 -> {
							xx = minusx;
							zz = minusz;
						}
						case 1 -> {
							xx = x;
							zz = minusz;
						}
						case 2 -> {
							xx = plusx;
							zz = minusz;
						}
						case 3 -> {
							xx = plusx;
							zz = z;
						}
						case 4 -> {
							xx = plusx;
							zz = plusz;
						}
						case 5 -> {
							xx = x;
							zz = plusz;
						}
						case 6 -> {
							xx = minusx;
							zz = plusz;
						}
						case 7 -> {
							xx = minusx;
							zz = z;
						}
						default -> {
							xx = x;
							zz = z;
						}
					}
					for (int yy = 0; yy < 4; yy++) {
						boolean change = true;
						Material mat = colData[yy].getMaterial();
						switch (mat) {
							case GRASS_BLOCK:
							case DIRT:
								BlockData subi = (preset.equals(PRESET.SUBMERGED)) ? cham_id : colData[yy];
								TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, subi);
								break;
							case WHITE_WOOL:
							case LIME_WOOL:
								BlockData chaw = (preset.equals(PRESET.FLOWER)) ? the_colour : colData[yy];
								TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chaw);
								break;
							case ACACIA_SAPLING:
							case ALLIUM:
							case AZURE_BLUET:
							case BAMBOO_SAPLING:
							case BEETROOTS:
							case BIRCH_SAPLING:
							case BLUE_ORCHID:
							case CARROTS:
							case CORNFLOWER:
							case CRIMSON_FUNGUS:
							case CRIMSON_ROOTS:
							case DANDELION:
							case DARK_OAK_SAPLING:
							case DEAD_BUSH:
							case FERN:
							case GRASS:
							case JUNGLE_SAPLING:
							case LARGE_FERN:
							case LILAC:
							case LILY_OF_THE_VALLEY:
							case OAK_SAPLING:
							case ORANGE_TULIP:
							case OXEYE_DAISY:
							case PEONY:
							case PINK_TULIP:
							case POPPY:
							case POTATOES:
							case RED_TULIP:
							case ROSE_BUSH:
							case SPRUCE_SAPLING:
							case SUGAR_CANE:
							case SUNFLOWER:
							case SWEET_BERRY_BUSH:
							case TALL_GRASS:
							case WARPED_FUNGUS:
							case WARPED_ROOTS:
							case WHEAT:
							case WHITE_TULIP:
							case WITHER_ROSE:
								break;
							case TORCH: // lamps, glowstone and torches
							case GLOWSTONE:
							case REDSTONE_LAMP:
								BlockData light;
								if (dd.isSubmarine() && mat.equals(Material.TORCH)) {
									light = Material.GLOWSTONE.createBlockData();
								} else {
									light = colData[yy];
								}
								TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, light);
								break;
							case ACACIA_DOOR: // wood, iron & trap doors
							case ACACIA_TRAPDOOR:
							case ACACIA_WALL_SIGN:
							case BIRCH_DOOR:
							case BIRCH_TRAPDOOR:
							case BIRCH_WALL_SIGN:
							case CRIMSON_DOOR:
							case CRIMSON_TRAPDOOR:
							case CRIMSON_WALL_SIGN:
							case DARK_OAK_DOOR:
							case DARK_OAK_TRAPDOOR:
							case DARK_OAK_WALL_SIGN:
							case IRON_DOOR:
							case JUNGLE_DOOR:
							case JUNGLE_TRAPDOOR:
							case JUNGLE_WALL_SIGN:
							case OAK_DOOR:
							case OAK_TRAPDOOR:
							case OAK_WALL_SIGN:
							case SPRUCE_DOOR:
							case SPRUCE_TRAPDOOR:
							case SPRUCE_WALL_SIGN:
							case WARPED_DOOR:
							case WARPED_TRAPDOOR:
							case WARPED_WALL_SIGN:
								if (preset.equals(PRESET.SWAMP) || preset.equals(PRESET.TOPSYTURVEY) || preset.equals(PRESET.JAIL)) {
									TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, Material.AIR);
								}
								break;
							case WHITE_STAINED_GLASS:
								BlockData chaf = (preset.equals(PRESET.FLOWER)) ? stain_colour : colData[yy];
								TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chaf);
								break;
							case LIME_STAINED_GLASS:
								BlockData chap = (preset.equals(PRESET.PARTY)) ? stain_colour : colData[yy];
								TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chap);
								break;
							case LIGHT_GRAY_STAINED_GLASS:
								BlockData cham = (preset.equals(PRESET.FACTORY)) ? plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(cham_id.getMaterial()).createBlockData() : colData[yy];
								TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, cham);
								break;
							case LIGHT_GRAY_TERRACOTTA:
								BlockData chai = (preset.equals(PRESET.FACTORY)) ? cham_id : colData[yy];
								TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chai);
								break;
							default: // everything else
								if (change) {
									TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colData[yy]);
								}
								break;
						}
					}
				}
			}
		} else {
			plugin.getServer().getScheduler().cancelTask(task);
			task = 0;
			new TARDISDeinstantPreset(plugin).instaDestroyPreset(dd, false, preset);
			if (preset.equals(PRESET.JUNK_MODE)) {
				// teleport player(s) to exit (tmd.getFromToLocation())
				getJunkTravellers().forEach((e) -> {
					if (e instanceof Player p) {
						Location relativeLoc = getRelativeLocation(p);
						p.teleport(relativeLoc);
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> p.teleport(relativeLoc), 2L);
					}
				});
			}
		}
	}

	private Location getRelativeLocation(Player p) {
		Location playerLoc = p.getLocation();
		double x = dd.getFromToLocation().getX() + (playerLoc.getX() - dd.getLocation().getX());
		double y = dd.getFromToLocation().getY() + (playerLoc.getY() - dd.getLocation().getY()) + 1.1d;
		double z = dd.getFromToLocation().getZ() + (playerLoc.getZ() - dd.getLocation().getZ());
		Location l = new Location(dd.getFromToLocation().getWorld(), x, y, z, playerLoc.getYaw(), playerLoc.getPitch());
		while (!l.getChunk().isLoaded()) {
			l.getChunk().load();
		}
		return l;
	}

	private List<Entity> getJunkTravellers() {
		// spawn an entity
		Entity orb = dd.getLocation().getWorld().spawnEntity(dd.getLocation(), EntityType.EXPERIENCE_ORB);
		List<Entity> ents = orb.getNearbyEntities(1.0, 1.0, 1.0);
		orb.remove();
		return ents;
	}

	private void getColours(int id, PRESET p) {
		HashMap<String, Object> where = new HashMap<>();
		where.put("tardis_id", id);
		where.put("door_type", 0);
		ResultSetDoors rs = new ResultSetDoors(plugin, where, false);
		if (rs.resultSet()) {
			try {
				Block b = TARDISStaticLocationGetters.getLocationFromDB(rs.getDoorLocation()).getBlock();
				if (p.equals(PRESET.FLOWER)) {
					the_colour = b.getRelative(BlockFace.UP, 3).getBlockData();
					String[] split = the_colour.getMaterial().toString().toLowerCase().split("_");
					String colour = (split.length > 2) ? split[0] + "_" + split[1] : split[0];
					stain_colour = plugin.getServer().createBlockData("minecraft:" + colour + "_stained_glass");
					return;
				} else {
					for (BlockFace f : plugin.getGeneralKeeper().getFaces()) {
						if (Tag.WOOL.isTagged(b.getRelative(f).getType())) {
							the_colour = b.getRelative(f).getBlockData();
							String[] split = the_colour.getMaterial().toString().toLowerCase().split("_");
							String colour = (split.length > 2) ? split[0] + "_" + split[1] : split[0];
							stain_colour = plugin.getServer().createBlockData("minecraft:" + colour + "_stained_glass");
							return;
						}
					}
				}
			} catch (Exception e) {
				the_colour = Material.BLUE_WOOL.createBlockData();
				stain_colour = Material.BLUE_STAINED_GLASS.createBlockData();
				return;
			}
		}
		the_colour = Material.BLUE_WOOL.createBlockData();
		stain_colour = Material.BLUE_STAINED_GLASS.createBlockData();
	}

	public void setTask(int task) {
		this.task = task;
	}
}
