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

import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.chameleon.TARDISChameleonColumn;
import me.eccentric_nz.tardis.chameleon.TARDISConstructColumn;
import me.eccentric_nz.tardis.custommodeldata.TARDISMushroomBlockData;
import me.eccentric_nz.tardis.database.data.ReplacedBlock;
import me.eccentric_nz.tardis.database.data.TARDIS;
import me.eccentric_nz.tardis.database.resultset.*;
import me.eccentric_nz.tardis.enumeration.Adaption;
import me.eccentric_nz.tardis.enumeration.PRESET;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.travel.TARDISDoorLocation;
import me.eccentric_nz.tardis.utility.TARDISBlockSetters;
import me.eccentric_nz.tardis.utility.TARDISParticles;
import me.eccentric_nz.tardis.utility.TARDISSounds;
import me.eccentric_nz.tardis.utility.TARDISStaticUtils;
import me.eccentric_nz.tardischunkgenerator.TARDISChunkGenerator;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.*;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * A dematerialisation circuit was an essential part of a Type 40 tardis which enabled it to dematerialise from normal
 * space into the Time Vortex and rematerialise back from it.
 *
 * @author eccentric_nz
 */
class TARDISMaterialisePreset implements Runnable {

	private final TARDISPlugin plugin;
	private final BuildData bd;
	private final int loops;
	private final PRESET preset;
	private final BlockData data;
	private final Adaption adapt;
	private final TARDISChameleonColumn column;
	private final TARDISChameleonColumn stained_column;
	private final TARDISChameleonColumn glass_column;
	private final Material random_colour;
	private final Material random_glass;
	private final ChatColor sign_colour;
	private int task;
	private int i;
	private Block handbrake;
	private BlockData h_data;
	private Block swampDoorBottom;
	private BlockData sdb_data;
	private Block swampDoorTop;
	private BlockData sdt_data;
	private Block swampSlab;
	private BlockData slab_data;

	/**
	 * Runnable method to materialise the TARDIS Police Box. Tries to mimic the transparency of materialisation by
	 * building the Police Box first with GLASS, then STAINED_GLASS, then the normal preset wall block.
	 *
	 * @param plugin instance of the TARDIS plugin
	 * @param bd     the Materialisation data
	 * @param preset the preset to construct
	 * @param data   the chameleon block data for the police box
	 * @param adapt  the chameleon circuit adaption setting
	 */
	TARDISMaterialisePreset(TARDISPlugin plugin, BuildData bd, PRESET preset, BlockData data, Adaption adapt) {
		this.plugin = plugin;
		this.bd = bd;
		loops = this.bd.getThrottle().getLoops();
		i = 0;
		this.preset = preset;
		this.data = data;
		this.adapt = adapt;
		if (preset.equals(PRESET.ANGEL)) {
			plugin.getPresets().setR(TARDISConstants.RANDOM.nextInt(2));
		}
		if (this.preset.equals(PRESET.CONSTRUCT)) {
			column = new TARDISConstructColumn(plugin, bd.getTardisId(), "blueprintData", bd.getDirection()).getColumn();
			stained_column = new TARDISConstructColumn(plugin, bd.getTardisId(), "stainData", bd.getDirection()).getColumn();
			glass_column = new TARDISConstructColumn(plugin, bd.getTardisId(), "glassData", bd.getDirection()).getColumn();
		} else {
			column = plugin.getPresets().getColumn(preset, bd.getDirection());
			stained_column = plugin.getPresets().getStained(preset, bd.getDirection());
			glass_column = plugin.getPresets().getGlass(preset, bd.getDirection());
		}
		Material[] colours = new Material[]{Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.MAGENTA_WOOL, Material.LIGHT_BLUE_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL, Material.PINK_WOOL, Material.CYAN_WOOL, Material.PURPLE_WOOL, Material.BLUE_WOOL, Material.BROWN_WOOL, Material.GREEN_WOOL, Material.RED_WOOL};
		Material[] glassColours = new Material[]{Material.WHITE_STAINED_GLASS, Material.ORANGE_STAINED_GLASS, Material.MAGENTA_STAINED_GLASS, Material.LIGHT_BLUE_STAINED_GLASS, Material.YELLOW_STAINED_GLASS, Material.LIME_STAINED_GLASS, Material.PINK_STAINED_GLASS, Material.CYAN_STAINED_GLASS, Material.PURPLE_STAINED_GLASS, Material.BLUE_STAINED_GLASS, Material.BROWN_STAINED_GLASS, Material.GREEN_STAINED_GLASS, Material.RED_STAINED_GLASS};
		int r = TARDISConstants.RANDOM.nextInt(13);
		random_colour = colours[r];
		random_glass = glassColours[r];
		sign_colour = plugin.getUtils().getSignColour();
	}

	@Override
	public void run() {
		if (!plugin.getTrackerKeeper().getDematerialising().contains(bd.getTardisId())) {
			if (column == null || stained_column == null || glass_column == null) {
				plugin.getServer().getScheduler().cancelTask(task);
				task = 0;
				TARDISMessage.send(bd.getPlayer().getPlayer(), "INVALID_CONSTRUCT");
			}
			BlockData[][] datas;
			// get relative locations
			int x = bd.getLocation().getBlockX(), plusx = bd.getLocation().getBlockX() + 1, minusx = bd.getLocation().getBlockX() - 1, y;
			if (preset.equals(PRESET.SUBMERGED)) {
				y = bd.getLocation().getBlockY() - 1;
			} else {
				y = bd.getLocation().getBlockY();
			}
			int z = bd.getLocation().getBlockZ(), plusz = bd.getLocation().getBlockZ() + 1, minusz = bd.getLocation().getBlockZ() - 1;
			World world = bd.getLocation().getWorld();
			int signx = 0, signz = 0;
			boolean hasDodgyDoor = preset.equals(PRESET.SWAMP) || preset.equals(PRESET.TOPSYTURVEY) || preset.equals(PRESET.JAIL);
			boolean isJunkOrToilet = preset.equals(PRESET.JUNK_MODE) || preset.equals(PRESET.TOILET);
			if (i < loops) {
				i++;
				if (preset.equals(PRESET.JUNK_MODE)) {
					datas = column.getBlockData();
				} else {
					// determine preset to use
					datas = switch (i % 3) {
						case 2 -> // stained
								stained_column.getBlockData();
						case 1 -> // glass
								glass_column.getBlockData();
						default -> // preset
								column.getBlockData();
					};
				}
				// rescue player?
				if (i == 10 && plugin.getTrackerKeeper().getRescue().containsKey(bd.getTardisId())) {
					UUID playerUUID = plugin.getTrackerKeeper().getRescue().get(bd.getTardisId());
					Player saved = plugin.getServer().getPlayer(playerUUID);
					if (saved != null) {
						TARDISDoorLocation idl = plugin.getGeneralKeeper().getDoorListener().getDoor(1, bd.getTardisId());
						Location l = idl.getL();
						plugin.getGeneralKeeper().getDoorListener().movePlayer(saved, l, false, world, false, 0, bd.useMinecartSounds());
						TARDISSounds.playTARDISSound(saved, "tardis_land_fast", 5L);
						// put player into travellers table
						HashMap<String, Object> set = new HashMap<>();
						set.put("tardis_id", bd.getTardisId());
						set.put("uuid", playerUUID.toString());
						plugin.getQueryFactory().doInsert("travellers", set);
					}
					plugin.getTrackerKeeper().getRescue().remove(bd.getTardisId());
				}
				// first run - remember blocks
				boolean isPoliceBox = preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD);
				boolean isAdaptiveFactory = preset.equals(PRESET.FACTORY) && adapt.equals(Adaption.BIOME);
				boolean isJunk = preset.equals(PRESET.JUNK_MODE) || preset.equals(PRESET.JUNK);
				if (i == 1) {
					// if configured and it's a Whovian preset set biome
					setBiome(bd.useTexture(), true);
					if (bd.isOutside()) {
						if (!bd.useMinecartSounds()) {
							String sound;
							if (preset.equals(PRESET.JUNK_MODE)) {
								sound = "junk_land";
							} else {
								sound = switch (bd.getThrottle()) {
									case WARP, RAPID, FASTER -> "tardis_land_" + bd.getThrottle().toString().toLowerCase();
									default -> "tardis_land";
								};
							}
							TARDISSounds.playTARDISSound(bd.getLocation(), sound);
						} else {
							world.playSound(bd.getLocation(), Sound.ENTITY_MINECART_INSIDE, 1.0F, 0.0F);
						}
					}
					// get direction player is facing from yaw place block under door if block is in list of blocks an iron door cannot go on
					switch (bd.getDirection()) {
						case SOUTH -> {
							// if (yaw >= 315 || yaw < 45)
							signx = x;
							signz = (minusz - 1);
						}
						case EAST -> {
							// if (yaw >= 225 && yaw < 315)
							signx = (minusx - 1);
							signz = z;
						}
						case NORTH -> {
							// if (yaw >= 135 && yaw < 225)
							signx = x;
							signz = (plusz + 1);
						}
						case WEST -> {
							// if (yaw >= 45 && yaw < 135)
							signx = (plusx + 1);
							signz = z;
						}
					}
					int xx, zz;
					for (int n = 0; n < 10; n++) {
						BlockData[] colData = datas[n];
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
							case 8 -> {
								xx = x;
								zz = z;
							}
							default -> {
								xx = signx;
								zz = signz;
							}
						}
						for (int yy = 0; yy < 4; yy++) {
							boolean change = true;
							if (yy == 0 && n == 9) {
								Block rail = world.getBlockAt(xx, y, zz);
								if (rail.getType().equals(Material.RAIL) || rail.getType().equals(Material.POWERED_RAIL)) {
									change = false;
									if (loops == 3) {
										TARDISBlockSetters.setBlock(world, xx, y, zz, rail.getBlockData());
									} else {
										TARDISBlockSetters.setBlockAndRemember(world, xx, y, zz, rail.getBlockData(), bd.getTardisId());
									}
								} else if (preset.equals(PRESET.SWAMP)) {
									swampSlab = rail;
									slab_data = column.getBlockData()[n][yy];
									change = false;
								} else if (preset.equals(PRESET.SUBMERGED)) {
									change = false;
								}
								if (world.getEnvironment().equals(World.Environment.NETHER) || world.getEnvironment().equals(World.Environment.THE_END) || world.getGenerator() instanceof TARDISChunkGenerator) {
									TARDISBlockSetters.setUnderDoorBlock(world, xx, (y - 1), zz, bd.getTardisId(), false);
								}
							}
							if (yy == 0 && n == 8 && !plugin.getPresetBuilder().no_block_under_door.contains(preset)) {
								TARDISBlockSetters.setUnderDoorBlock(world, xx, (y - 1), zz, bd.getTardisId(), true);
							}
							Material mat = colData[yy].getMaterial();
							switch (mat) {
								case DIRT:
								case GRASS_BLOCK:
									BlockData subi = (preset.equals(PRESET.SUBMERGED)) ? data : colData[yy];
									TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, subi, bd.getTardisId());
									break;
								case WHITE_WOOL:
									Material flower = (preset.equals(PRESET.FLOWER)) ? random_colour : mat;
									TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, flower, bd.getTardisId());
									break;
								case LIME_WOOL:
									Material party = (preset.equals(PRESET.PARTY)) ? random_colour : mat;
									TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, party, bd.getTardisId());
									break;
								case BLUE_WOOL:
									BlockData old = (isPoliceBox && adapt.equals(Adaption.BLOCK)) ? data : colData[yy];
									TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, old, bd.getTardisId());
									break;
								case TORCH: // lamps, glowstone and torches
								case GLOWSTONE:
								case REDSTONE_LAMP:
									BlockData light;
									if (bd.isSubmarine() && mat.equals(Material.TORCH)) {
										light = Material.GLOWSTONE.createBlockData();
									} else {
										light = colData[yy];
									}
									TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, light, bd.getTardisId());
									break;
								case IRON_DOOR: // wood, iron & trap doors, rails
								case RAIL:
								case ACACIA_DOOR:
								case ACACIA_TRAPDOOR:
								case BIRCH_DOOR:
								case BIRCH_TRAPDOOR:
								case CRIMSON_DOOR:
								case CRIMSON_TRAPDOOR:
								case DARK_OAK_DOOR:
								case DARK_OAK_TRAPDOOR:
								case JUNGLE_DOOR:
								case JUNGLE_TRAPDOOR:
								case OAK_DOOR:
								case OAK_TRAPDOOR:
								case SPRUCE_DOOR:
								case SPRUCE_TRAPDOOR:
								case WARPED_DOOR:
								case WARPED_TRAPDOOR:
									boolean door = false;
									if (Tag.DOORS.isTagged(mat)) {
										Bisected bisected = (Bisected) colData[yy];
										door = bisected.getHalf().equals(Bisected.Half.BOTTOM);
									}
									if (Tag.TRAPDOORS.isTagged(mat)) {
										door = true;
									}
									if (door) {
										// remember the door location
										saveDoorLocation(world, xx, y, yy, zz);
									} else {
										String doorStr = world.getBlockAt(xx, y + yy, zz).getLocation().toString();
										plugin.getGeneralKeeper().getProtectBlockMap().put(doorStr, bd.getTardisId());
									}
									if (yy == 0) {
										if (bd.isSubmarine() && plugin.isWorldGuardOnServer()) {
											int sy = y - 1;
											TARDISBlockSetters.setBlockAndRemember(world, xx, sy, zz, Material.SPONGE, bd.getTardisId());
											Block sponge = world.getBlockAt(xx, sy, zz);
											plugin.getWorldGuardUtils().sponge(sponge, true);
										} else if (!plugin.getPresetBuilder().no_block_under_door.contains(preset)) {
											TARDISBlockSetters.setUnderDoorBlock(world, xx, (y - 1), zz, bd.getTardisId(), false);
										}
									}
									if (hasDodgyDoor) {
										// do it at the end
										if (door) {
											swampDoorBottom = world.getBlockAt(xx, (y + yy), zz);
											sdb_data = colData[yy];
										} else {
											swampDoorTop = world.getBlockAt(xx, (y + yy), zz);
											sdt_data = colData[yy];
										}
									} else {
										TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, colData[yy], bd.getTardisId());
									}
									break;
								case ACACIA_SIGN:
								case BIRCH_SIGN:
								case CRIMSON_SIGN:
								case DARK_OAK_SIGN:
								case JUNGLE_SIGN:
								case OAK_SIGN:
								case SPRUCE_SIGN:
								case WARPED_SIGN:
									if (preset.equals(PRESET.APPERTURE)) {
										TARDISBlockSetters.setUnderDoorBlock(world, xx, (y - 1), zz, bd.getTardisId(), false);
									}
									break;
								case ACACIA_WALL_SIGN:
								case BIRCH_WALL_SIGN:
								case CRIMSON_WALL_SIGN:
								case DARK_OAK_WALL_SIGN:
								case JUNGLE_WALL_SIGN:
								case OAK_WALL_SIGN:
								case SPRUCE_WALL_SIGN:
								case WARPED_WALL_SIGN:
									// sign - if there is one
									if (preset.equals(PRESET.JUNK_MODE)) {
										// add a sign
										TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colData[yy]);
										// remember its location
										String location = new Location(world, xx, (y + yy), zz).toString();
										plugin.getGeneralKeeper().getProtectBlockMap().put(location, bd.getTardisId());
										saveJunkControl(location, "save_sign");
										// make it a save_sign
										Block sign = world.getBlockAt(xx, (y + yy), zz);
										if (Tag.WALL_SIGNS.isTagged(sign.getType())) {
											Sign s = (Sign) sign.getState();
											s.setLine(0, "TARDIS");
											s.setLine(1, plugin.getSigns().getStringList("saves").get(0));
											s.setLine(2, plugin.getSigns().getStringList("saves").get(1));
											s.setLine(3, "");
											s.update();
										}
									} else if (bd.shouldAddSign()) {
										TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colData[yy]);
										Block sign = world.getBlockAt(xx, (y + yy), zz);
										if (Tag.SIGNS.isTagged(sign.getType())) {
											Sign s = (Sign) sign.getState();
											plugin.getGeneralKeeper().getProtectBlockMap().put(sign.getLocation().toString(), bd.getTardisId());
											if (plugin.getConfig().getBoolean("police_box.name_tardis")) {
												HashMap<String, Object> wheret = new HashMap<>();
												wheret.put("tardis_id", bd.getTardisId());
												ResultSetTardis rst = new ResultSetTardis(plugin, wheret, "", false, 0);
												if (rst.resultSet()) {
													TARDIS tardis = rst.getTardis();
													String player_name = TARDISStaticUtils.getNick(tardis.getUuid());
													if (player_name == null) {
														player_name = tardis.getOwner();
													}
													String owner;
													if (preset.equals(PRESET.GRAVESTONE) || preset.equals(PRESET.PUNKED) || preset.equals(PRESET.ROBOT)) {
														owner = (player_name.length() > 14) ? player_name.substring(0, 14) : player_name;
													} else {
														owner = (player_name.length() > 14) ? player_name.substring(0, 12) + "'s" : player_name + "'s";
													}
													switch (preset) {
														case GRAVESTONE -> s.setLine(3, owner);
														case ANGEL, JAIL -> s.setLine(2, owner);
														default -> s.setLine(0, owner);
													}
												}
											}
											String line1;
											String line2;
											if (preset.equals(PRESET.CUSTOM)) {
												line1 = plugin.getPresets().custom.getFirstLine();
												line2 = plugin.getPresets().custom.getSecondLine();
											} else {
												line1 = preset.getFirstLine();
												line2 = preset.getSecondLine();
											}
											switch (preset) {
												case ANGEL:
													s.setLine(0, sign_colour + line1);
													s.setLine(1, sign_colour + line2);
													s.setLine(3, sign_colour + "TARDIS");
													break;
												case APPERTURE:
													s.setLine(1, sign_colour + line1);
													s.setLine(2, sign_colour + line2);
													s.setLine(3, sign_colour + "LAB");
													break;
												case JAIL:
													s.setLine(0, sign_colour + line1);
													s.setLine(1, sign_colour + line2);
													s.setLine(3, sign_colour + "CAPTURE");
													break;
												case THEEND:
													s.setLine(1, sign_colour + line1);
													s.setLine(2, sign_colour + line2);
													s.setLine(3, sign_colour + "HOT ROD");
													break;
												case CONSTRUCT:
													// get sign text from database
													ResultSetConstructSign rscs = new ResultSetConstructSign(plugin, bd.getTardisId());
													if (rscs.resultSet()) {
														if (rscs.getLine1().isEmpty() && rscs.getLine2().isEmpty() && rscs.getLine3().isEmpty() && rscs.getLine4().isEmpty()) {
															s.setLine(1, sign_colour + line1);
															s.setLine(2, sign_colour + line2);
														} else {
															s.setLine(0, rscs.getLine1());
															s.setLine(1, rscs.getLine2());
															s.setLine(2, rscs.getLine3());
															s.setLine(3, rscs.getLine4());
														}
													}
													break;
												case CUSTOM:
													break;
												default:
													s.setLine(1, sign_colour + line1);
													s.setLine(2, sign_colour + line2);
													break;
											}
											s.update();
										}
									}
									break;
								case LEVER:
									// remember this block and do at end
									if (preset.equals(PRESET.JUNK_MODE)) {
										// remember its location
										handbrake = world.getBlockAt(xx, (y + yy), zz);
										plugin.getGeneralKeeper().getProtectBlockMap().put(handbrake.getLocation().toString(), bd.getTardisId());
										h_data = colData[yy];
									}
									break;
								case SKELETON_SKULL:
									if (bd.isSubmarine()) {
										TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, Material.GLOWSTONE);
									} else {
										Rotatable rotatable = (Rotatable) colData[yy];
										rotatable.setRotation(plugin.getPresetBuilder().getSkullDirection(bd.getDirection()));
										TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, rotatable, bd.getTardisId());
									}
									break;
								case LIGHT_GRAY_TERRACOTTA:
									BlockData chai = isAdaptiveFactory ? data : colData[yy];
									TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, chai, bd.getTardisId());
									break;
								default: // everything else
									if (change) {
										if (isJunk && mat.equals(Material.ORANGE_WOOL)) {
											TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(46)), bd.getTardisId());
										} else {
											TARDISBlockSetters.setBlockAndRemember(world, xx, (y + yy), zz, colData[yy], bd.getTardisId());
										}
									}
									break;
							}
						}
					}
				}
				if (preset.equals(PRESET.JUNK_MODE) && plugin.getConfig().getBoolean("junk.particles")) {
					// animate particles
					plugin.getUtils().getJunkTravellers(bd.getLocation()).forEach((e) -> {
						if (e instanceof Player p) {
							Location effectsLoc = bd.getLocation().clone().add(0.5d, 0, 0.5d);
							TARDISParticles.sendVortexParticles(effectsLoc, p);
						}
					});
				}
				// just change the walls
				int xx, zz;
				for (int n = 0; n < 9; n++) {
					BlockData[] coldatas = datas[n];
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
						case 8 -> {
							xx = x;
							zz = z;
						}
						default -> {
							xx = signx;
							zz = signz;
						}
					}
					for (int yy = 0; yy < 4; yy++) {
						boolean change = true;
						if (yy == 0 && n == 9) {
							Block rail = world.getBlockAt(xx, y, zz);
							if (rail.getType().equals(Material.RAIL) || rail.getType().equals(Material.POWERED_RAIL)) {
								change = false;
							}
						}
						Material mat = coldatas[yy].getMaterial();
						switch (mat) {
							case DIRT:
							case GRASS_BLOCK:
								BlockData subi = (preset.equals(PRESET.SUBMERGED)) ? data : coldatas[yy];
								TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, subi);
								break;
							case BEDROCK:
								if (preset.equals(PRESET.THEEND) && i == loops) {
									TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, coldatas[yy]);
									world.getBlockAt(xx, (y + yy + 1), zz).setBlockData(TARDISConstants.FIRE);
								} else {
									TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, coldatas[yy]);
								}
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
								if (i == loops && (preset.equals(PRESET.GRAVESTONE) || preset.equals(PRESET.MESA) || preset.equals(PRESET.PLAINS) || preset.equals(PRESET.TAIGA) || preset.equals(PRESET.CONSTRUCT) || preset.equals(PRESET.CUSTOM))) {
									TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, coldatas[yy]);
								} else {
									TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, Material.AIR);
								}
								break;
							case WHITE_WOOL:
								Material flower = (preset.equals(PRESET.FLOWER)) ? random_colour : mat;
								TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, flower);
								break;
							case LIME_WOOL:
								Material party = (preset.equals(PRESET.PARTY)) ? random_colour : mat;
								TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, party);
								break;
							case BLUE_WOOL:
								if (adapt.equals(Adaption.OFF) && isPoliceBox && bd.shouldUseCTM() && n == TARDISStaticUtils.getCol(bd.getDirection()) && yy == 1 && plugin.getConfig().getBoolean("police_box.set_biome")) {
									// set an observer block instead
									Directional directional = (Directional) Material.OBSERVER.createBlockData();
									directional.setFacing(BlockFace.valueOf(bd.getDirection().toString()));
									TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, directional);
								} else if (adapt.equals(Adaption.BLOCK) && (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD) || preset.equals(PRESET.SUBMERGED))) {
									TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, data);
								} else {
									TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, mat);
								}
								break;
							case TORCH: // lamps, glowstone and torches
							case GLOWSTONE:
							case REDSTONE_LAMP:
								Material light;
								if (bd.isSubmarine() && mat.equals(Material.TORCH)) {
									light = Material.GLOWSTONE;
								} else {
									light = mat;
								}
								BlockData lamp = light.createBlockData();
								if (lamp instanceof Lightable lightable) {
									lightable.setLit(true);
									TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, lightable);
								} else {
									TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, lamp);
								}
								break;
							case IRON_DOOR: // wood, iron & trap doors
							case ACACIA_DOOR:
							case ACACIA_TRAPDOOR:
							case BIRCH_DOOR:
							case BIRCH_TRAPDOOR:
							case CRIMSON_DOOR:
							case CRIMSON_TRAPDOOR:
							case DARK_OAK_DOOR:
							case DARK_OAK_TRAPDOOR:
							case JUNGLE_DOOR:
							case JUNGLE_TRAPDOOR:
							case OAK_DOOR:
							case OAK_TRAPDOOR:
							case SPRUCE_DOOR:
							case SPRUCE_TRAPDOOR:
							case WARPED_DOOR:
							case WARPED_TRAPDOOR:
								if (preset.isColoured() || preset.equals(PRESET.PANDORICA)) {
									TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, coldatas[yy]);
									// remember the door location
									saveDoorLocation(world, xx, y, yy, zz);
								}
								// else don't change the door
								break;
							case LEVER:
								// remember this block and do at end
								if (isJunkOrToilet) {
									// remember its location
									handbrake = world.getBlockAt(xx, (y + yy), zz);
									h_data = coldatas[yy];
								}
								break;
							case NETHERRACK:
								TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, coldatas[yy]);
								if (preset.equals(PRESET.TORCH) && i == loops) {
									world.getBlockAt(xx, (y + yy + 1), zz).setBlockData(TARDISConstants.FIRE);
								}
								break;
							case NETHER_PORTAL:
								TARDISBlockSetters.setBlock(world, xx, (y + yy + 1), zz, Material.OBSIDIAN);
								TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, coldatas[yy]);
								break;
							case LIGHT_GRAY_STAINED_GLASS:
								Material chag = isAdaptiveFactory ? plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(data.getMaterial()) : mat;
								TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chag);
							case WHITE_STAINED_GLASS:
								Material chaw = (preset.equals(PRESET.FLOWER)) ? random_glass : mat;
								TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chaw);
								break;
							case LIME_STAINED_GLASS:
								Material chal = (preset.equals(PRESET.PARTY)) ? random_glass : mat;
								TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chal);
								break;
							case BLUE_STAINED_GLASS:
								Material chad = ((preset.equals(PRESET.OLD) && adapt.equals(Adaption.BLOCK)) || preset.equals(PRESET.SUBMERGED)) ? plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(data.getMaterial()) : mat;
								TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chad);
								break;
							case SKELETON_SKULL:
								if (bd.isSubmarine()) {
									TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, Material.GLOWSTONE);
								} else {
									Rotatable rotatable = (Rotatable) coldatas[yy];
									rotatable.setRotation(plugin.getPresetBuilder().getSkullDirection(bd.getDirection()));
									TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, rotatable);
								}
								break;
							case LIGHT_GRAY_TERRACOTTA:
								BlockData chai = (preset.equals(PRESET.FACTORY)) ? data : coldatas[yy];
								TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chai);
								break;
							default: // everything else
								if (change) {
									if (isJunk && mat.equals(Material.ORANGE_WOOL)) {
										TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(46)));
									} else {
										TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, coldatas[yy]);
									}
								}
								break;
						}
					}
				}
			} else {
				if ((hasDodgyDoor) && swampDoorBottom != null) {
					TARDISBlockSetters.setBlockAndRemember(world, swampDoorBottom.getX(), swampDoorBottom.getY(), swampDoorBottom.getZ(), sdb_data, bd.getTardisId());
					TARDISBlockSetters.setBlockAndRemember(world, swampDoorTop.getX(), swampDoorTop.getY(), swampDoorTop.getZ(), sdt_data, bd.getTardisId());
					if (preset.equals(PRESET.SWAMP)) {
						TARDISBlockSetters.setBlockAndRemember(world, swampSlab.getX(), swampSlab.getY(), swampSlab.getZ(), slab_data, bd.getTardisId());
					}
				}
				if (isJunkOrToilet) {
					handbrake.setBlockData(h_data);
					// remember its location
					String location = handbrake.getLocation().toString();
					saveJunkControl(location, "handbrake");
					// set handbrake to on ?
				}
				// just in case
				setBiome(bd.useTexture(), false);
				// remove trackers
				plugin.getTrackerKeeper().getMaterialising().removeAll(Collections.singleton(bd.getTardisId()));
				plugin.getTrackerKeeper().getInVortex().removeAll(Collections.singleton(bd.getTardisId()));
				plugin.getServer().getScheduler().cancelTask(task);
				task = 0;
				plugin.getTrackerKeeper().getMalfunction().remove(bd.getTardisId());
				if (plugin.getTrackerKeeper().getDidDematToVortex().contains(bd.getTardisId())) {
					plugin.getTrackerKeeper().getDidDematToVortex().removeAll(Collections.singleton(bd.getTardisId()));
				}
				if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(bd.getTardisId())) {
					int taskID = plugin.getTrackerKeeper().getDestinationVortex().get(bd.getTardisId());
					plugin.getServer().getScheduler().cancelTask(taskID);
					plugin.getTrackerKeeper().getDestinationVortex().remove(bd.getTardisId());
				}
				if (!bd.isRebuild() && bd.getPlayer() != null) {
					plugin.getTrackerKeeper().getActiveForceFields().remove(bd.getPlayer().getPlayer().getUniqueId());
				}
				// message travellers in tardis
				if (loops > 3) {
					HashMap<String, Object> where = new HashMap<>();
					where.put("tardis_id", bd.getTardisId());
					ResultSetTravellers rst = new ResultSetTravellers(plugin, where, true);
					if (rst.resultSet()) {
						List<UUID> travellers = rst.getData();
						travellers.forEach((s) -> {
							Player p = plugin.getServer().getPlayer(s);
							if (p != null) {
								String message = (bd.isMalfunction()) ? "MALFUNCTION" : "HANDBRAKE_LEFT_CLICK";
								TARDISMessage.send(p, message);
								// TARDIS has travelled so add players to list so they can receive Artron on exit
								plugin.getTrackerKeeper().getHasTravelled().add(s);
							}
						});
					} else if (plugin.getTrackerKeeper().getJunkPlayers().containsKey(bd.getPlayer().getUniqueId())) {
						TARDISMessage.send(bd.getPlayer().getPlayer(), "JUNK_HANDBRAKE_LEFT_CLICK");
					}
					// restore beacon up block if present
					HashMap<String, Object> whereb = new HashMap<>();
					whereb.put("tardis_id", bd.getTardisId());
					whereb.put("police_box", 2);
					ResultSetBlocks rs = new ResultSetBlocks(plugin, whereb, false);
					if (rs.resultSet()) {
						ReplacedBlock rb = rs.getReplacedBlock();
						TARDISBlockSetters.setBlock(rb.getLocation(), rb.getBlockData());
						HashMap<String, Object> whered = new HashMap<>();
						whered.put("tardis_id", bd.getTardisId());
						whered.put("police_box", 2);
						plugin.getQueryFactory().doDelete("blocks", whered);
					}
					// tardis has moved so remove HADS damage count
					plugin.getTrackerKeeper().getDamage().remove(bd.getTardisId());
				}
			}
		}
	}

	private void setBiome(boolean pp, boolean umbrella) {
		if (plugin.getConfig().getBoolean("police_box.set_biome") && (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) && pp) {
			BiomeSetter.setBiome(bd, umbrella, loops);
		}
	}

	private void saveJunkControl(String location, String field) {
		// remember control location
		HashMap<String, Object> wherej = new HashMap<>();
		wherej.put("tardis_id", bd.getTardisId());
		HashMap<String, Object> setj = new HashMap<>();
		setj.put(field, location);
		plugin.getQueryFactory().doUpdate("junk", setj, wherej);
	}

	private void saveDoorLocation(World world, int xx, int y, int yy, int zz) {
		// remember the door location
		String doorloc = world.getName() + ":" + xx + ":" + (y + yy) + ":" + zz;
		String doorStr = world.getBlockAt(xx, y + yy, zz).getLocation().toString();
		plugin.getGeneralKeeper().getProtectBlockMap().put(doorStr, bd.getTardisId());
		// should insert the door when tardis is first made, and then update location there after!
		HashMap<String, Object> whered = new HashMap<>();
		whered.put("door_type", 0);
		whered.put("tardis_id", bd.getTardisId());
		ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
		HashMap<String, Object> setd = new HashMap<>();
		setd.put("door_location", doorloc);
		setd.put("door_direction", bd.getDirection().toString());
		if (rsd.resultSet()) {
			HashMap<String, Object> whereid = new HashMap<>();
			whereid.put("door_id", rsd.getDoorId());
			plugin.getQueryFactory().doUpdate("doors", setd, whereid);
		} else {
			setd.put("tardis_id", bd.getTardisId());
			setd.put("door_type", 0);
			plugin.getQueryFactory().doInsert("doors", setd);
		}
	}

	public void setTask(int task) {
		this.task = task;
	}
}
