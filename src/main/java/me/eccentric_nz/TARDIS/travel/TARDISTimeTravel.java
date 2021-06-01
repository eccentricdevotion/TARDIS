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
package me.eccentric_nz.tardis.travel;

import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.api.Parameters;
import me.eccentric_nz.tardis.blueprints.TARDISPermission;
import me.eccentric_nz.tardis.custommodeldata.TARDISMushroomBlockData;
import me.eccentric_nz.tardis.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.enumeration.COMPASS;
import me.eccentric_nz.tardis.enumeration.Flag;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.planets.TARDISAliasResolver;
import me.eccentric_nz.tardis.utility.TARDISBlockSetters;
import me.eccentric_nz.tardis.utility.TARDISMaterials;
import me.eccentric_nz.tardis.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.tardis.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * All things related to time travel.
 * <p>
 * All TARDISes built after a certain point, including the Type 40 the Doctor uses, have a mathematically modelled
 * duplicate of the Eye of harmony with all its attendant features.
 *
 * @author eccentric_nz
 */
public class TARDISTimeTravel {

	private final TARDISPlugin plugin;
	private final int attempts;
	private Location dest;

	public TARDISTimeTravel(TARDISPlugin plugin) {
		this.plugin = plugin;
		// add good materials
		attempts = plugin.getConfig().getInt("travel.random_attempts");
	}

	/**
	 * Checks if a random location is safe for the tardis Police Box to land at. The Police Box requires a clear 4 x 3 x
	 * 4 (d x w x h) area.
	 *
	 * @param startx a starting position in the x direction.
	 * @param starty a starting position in the y direction.
	 * @param startz a starting position in the z direction.
	 * @param resetx a copy of the starting x position to return to.
	 * @param resetz a copy of the starting z position to return to.
	 * @param w      the world the location check will take place in.
	 * @param d      the direction the Police Box is facing.
	 * @return the number of unsafe blocks
	 */
	public static int safeLocation(int startx, int starty, int startz, int resetx, int resetz, World w, COMPASS d) {
		int level, row, col, rowcount, colcount, count = 0;
		switch (d) {
			case EAST, WEST -> {
				rowcount = 3;
				colcount = 4;
			}
			default -> {
				rowcount = 4;
				colcount = 3;
			}
		}
		for (level = 0; level < 4; level++) {
			for (row = 0; row < rowcount; row++) {
				for (col = 0; col < colcount; col++) {
					Block block = w.getBlockAt(startx, starty, startz);
					if (level == 0) {
						// check for item frame
						for (Entity e : w.getNearbyEntities(block.getLocation(), 1.5d, 1.5d, 1.5d)) {
							if (e instanceof ItemFrame) {
								count++;
								break;
							}
						}
					}
					Material mat = block.getType();
					if (!TARDISConstants.GOOD_MATERIALS.contains(mat)) {
						// check for siege cube
						if (TARDISPlugin.plugin.getConfig().getBoolean("siege.enabled") &&
							mat.equals(Material.BROWN_MUSHROOM_BLOCK)) {
							MultipleFacing mf = (MultipleFacing) block.getBlockData();
							if (!mf.getAsString().equals(TARDISMushroomBlockData.BROWN_MUSHROOM_DATA.get(2))) {
								count++;
								break;
							}
						} else if (!(w.getName().equals("Siluria") && mat.equals(Material.BAMBOO))) {
							count++;
						}
					}
					startx += 1;
				}
				startx = resetx;
				startz += 1;
			}
			startz = resetz;
			starty += 1;
		}
		return count;
	}

	/**
	 * Gets the starting location for safe location checking.
	 *
	 * @param loc a location object to check.
	 * @param d   the direction the Police Box is facing.
	 * @return an array containing x and z coordinates
	 */
	public static int[] getStartLocation(Location loc, COMPASS d) {
		int[] startLocation = new int[4];
		switch (d) {
			case EAST -> {
				startLocation[0] = loc.getBlockX() - 2;
				startLocation[1] = startLocation[0];
				startLocation[2] = loc.getBlockZ() - 1;
				startLocation[3] = startLocation[2];
			}
			case SOUTH -> {
				startLocation[0] = loc.getBlockX() - 1;
				startLocation[1] = startLocation[0];
				startLocation[2] = loc.getBlockZ() - 2;
				startLocation[3] = startLocation[2];
			}
			default -> {
				startLocation[0] = loc.getBlockX() - 1;
				startLocation[1] = startLocation[0];
				startLocation[2] = loc.getBlockZ() - 1;
				startLocation[3] = startLocation[2];
			}
		}
		return startLocation;
	}

	/**
	 * Retrieves a random location determined from the tardis repeater or terminal settings.
	 *
	 * @param p           a player object used to check permissions against.
	 * @param rx          the delay setting of the x-repeater, this determines the distance in the x direction.
	 * @param rz          the delay setting of the z-repeater, this determines the distance in the z direction.
	 * @param ry          the delay setting of the y-repeater, this determines the multiplier for both the x and z
	 *                    directions.
	 * @param d           the direction the tardis Police Box faces.
	 * @param e           the environment(s) the player has chosen (or is allowed) to travel to.
	 * @param this_world  the world the Police Box is currently in
	 * @param malfunction whether there should be a malfunction
	 * @param current     the current location of the tardis
	 * @return a random Location
	 */
	public Location randomDestination(Player p, int rx, int rz, int ry, COMPASS d, String e, World this_world, boolean malfunction, Location current) {
		int startx, starty, startz, resetx, resetz, listlen;
		World randworld;
		int count;
		// get max_radius from config
		int max = plugin.getConfig().getInt("travel.tp_radius");
		int quarter = (max + 4 - 1) / 4;
		int range = quarter + 1;
		int wherex = 0, highest = 252, wherez = 0;
		// get worlds
		Set<String> worldlist = Objects.requireNonNull(plugin.getPlanetsConfig().getConfigurationSection("planets")).getKeys(false);
		List<World> allowedWorlds = new ArrayList<>();

		if (e.equals("THIS") &&
			plugin.getPlanetsConfig().getBoolean("planets." + this_world.getName() + ".time_travel")) {
			allowedWorlds.add(this_world);
		} else {
			worldlist.forEach((o) -> {
				World ww = TARDISAliasResolver.getWorldFromAlias(o);
				if (ww != null) {
					String env = ww.getEnvironment().toString();
					// Catch all non-nether and non-end ENVIRONMENT types and assume they're normal
					if (!env.equals("NETHER") && !env.equals("THE_END")) {
						env = "NORMAL";
					}
					if (e.equalsIgnoreCase(env)) {
						if (plugin.getConfig().getBoolean("travel.include_default_world") ||
							!plugin.getConfig().getBoolean("creation.default_world")) {
							// malfunction only ever true for NETHER & THE_END worlds
							if (plugin.getPlanetsConfig().getBoolean("planets." + o + ".time_travel") || malfunction) {
								allowedWorlds.add(ww);
							}
						} else {
							if (!o.equals(plugin.getConfig().getString("creation.default_world_name"))) {
								if (plugin.getPlanetsConfig().getBoolean("planets." + o + ".time_travel") ||
									malfunction) {
									allowedWorlds.add(ww);
								}
							}
						}
					}
					// remove the world the Police Box is in
					if (this_world != null && (allowedWorlds.size() > 1 || !plugin.getPlanetsConfig().getBoolean(
							"planets." + this_world.getName() + ".time_travel"))) {
						allowedWorlds.remove(this_world);
					}
					// remove the world if the player doesn't have permission
					if (allowedWorlds.size() > 1 && plugin.getConfig().getBoolean("travel.per_world_perms") &&
						!TARDISPermission.hasPermission(p, "tardis.travel." + o)) {
						allowedWorlds.remove(ww);
					}
				}
			});
		}
		listlen = allowedWorlds.size();
		// random world
		randworld = allowedWorlds.get(TARDISConstants.RANDOM.nextInt(listlen));

		switch (randworld.getEnvironment()) {
			case NETHER:
				for (int n = 0; n < attempts; n++) {
					wherex = randomX(range, quarter, rx, ry, e, current);
					wherez = randomZ(range, quarter, rz, ry, e, current);
					if (safeNether(randworld, wherex, wherez, d, p)) {
						break;
					}
				}
				break;
			case THE_END:
				if (plugin.getPlanetsConfig().getBoolean("planets." + randworld.getName() + ".void")) {
					// any location will do!
					int voidx = randomX(range, quarter, rx, ry, e, current);
					int voidy = TARDISConstants.RANDOM.nextInt(240) + 5;
					int voidz = randomZ(range, quarter, rz, ry, e, current);
					return new Location(randworld, voidx, voidy, voidz);
				}
				for (int n = 0; n < attempts; n++) {
					wherex = TARDISConstants.RANDOM.nextInt(240);
					wherez = TARDISConstants.RANDOM.nextInt(240);
					wherex -= 120;
					wherez -= 120;
					// get the spawn point
					Location endSpawn = randworld.getSpawnLocation();
					highest = TARDISStaticLocationGetters.getHighestYIn3x3(randworld,
							endSpawn.getBlockX() + wherex, endSpawn.getBlockZ() + wherez);
					if (highest > 40) {
						Block currentBlock = randworld.getBlockAt(wherex, highest, wherez);
						Location chunk_loc = currentBlock.getLocation();
						if (plugin.getPluginRespect().getRespect(chunk_loc, new Parameters(p, Flag.getNoMessageFlags()))) {
							while (!randworld.getChunkAt(chunk_loc).isLoaded()) {
								randworld.getChunkAt(chunk_loc).load();
							}
							// get start location for checking there is enough space
							int[] gsl = getStartLocation(chunk_loc, d);
							startx = gsl[0];
							resetx = gsl[1];
							starty = chunk_loc.getBlockY() + 1;
							startz = gsl[2];
							resetz = gsl[3];
							count = safeLocation(startx, starty, startz, resetx, resetz, randworld, d);
						} else {
							count = 1;
						}
					} else {
						count = 1;
					}
					if (count == 0) {
						break;
					}
				}
				dest = (highest > 0) ? new Location(randworld, wherex, highest, wherez) : null;
				break;
			default:
				// Assume every non-nether/non-END world qualifies as NORMAL.
				if (plugin.getPlanetsConfig().getBoolean("planets." + randworld.getName() + ".false_nether")) {
					for (int n = 0; n < attempts; n++) {
						wherex = randomX(range, quarter, rx, ry, e, current);
						wherez = randomZ(range, quarter, rz, ry, e, current);
						if (safeNether(randworld, wherex, wherez, d, p)) {
							break;
						}
					}
				} else {
					if (plugin.getPlanetsConfig().getBoolean("planets." + randworld.getName() + ".void")) {
						// any location will do!
						int voidx = randomX(range, quarter, rx, ry, e, current);
						int voidy = TARDISConstants.RANDOM.nextInt(240) + 5;
						int voidz = randomZ(range, quarter, rz, ry, e, current);
						return new Location(randworld, voidx, voidy, voidz);
					}
					long timeout = System.currentTimeMillis() + (plugin.getConfig().getLong("travel.timeout") * 1000);
					while (true) {
						if (System.currentTimeMillis() < timeout) {
							// reset count
							count = 0;
							// randomX(Random TARDISConstants.RANDOM, int range, int quarter, int rx, int ry, int max)
							wherex = randomX(range, quarter, rx, ry, e, current);
							wherez = randomZ(range, quarter, rz, ry, e, current);
							highest = TARDISStaticLocationGetters.getHighestYIn3x3(randworld, wherex, wherez);
							if (highest > 3) {
								Block currentBlock = randworld.getBlockAt(wherex, highest, wherez);
								if ((currentBlock.getRelative(BlockFace.DOWN).getType().equals(Material.WATER)) &&
									!plugin.getConfig().getBoolean("travel.land_on_water")) {
									// check if submarine is on
									ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, p.getUniqueId().toString());
									if (rsp.resultSet()) {
										if (rsp.isSubmarineOn() &&
											TARDISStaticUtils.isOceanBiome(TARDISStaticUtils.getBiomeAt(currentBlock.getLocation()))) {
											// get submarine location
											TARDISMessage.send(p, "SUB_SEARCH");
											Location underwater = submarine(currentBlock, d);
											if (underwater != null) {
												// get TARDIS id
												HashMap<String, Object> wherep = new HashMap<>();
												wherep.put("uuid", p.getUniqueId().toString());
												ResultSetTravellers rst = new ResultSetTravellers(plugin, wherep, false);
												if (rst.resultSet()) {
													plugin.getTrackerKeeper().getSubmarine().add(rst.getTardisId());
												}
												return underwater;
											} else {
												count = 1;
											}
										} else if (!rsp.isSubmarineOn()) {
											count = 1;
										}
									} else {
										count = 1;
									}
								} else {
									if (TARDISConstants.GOOD_MATERIALS.contains(currentBlock.getType())) {
										currentBlock = currentBlock.getRelative(BlockFace.DOWN);
									}
									Location chunk_loc = currentBlock.getLocation();
									if (plugin.getPluginRespect().getRespect(chunk_loc, new Parameters(p, Flag.getNoMessageFlags()))) {
										while (!randworld.getChunkAt(chunk_loc).isLoaded()) {
											randworld.getChunkAt(chunk_loc).load();
										}
										// get start location for checking there is enough space
										int[] gsl = getStartLocation(chunk_loc, d);
										startx = gsl[0];
										resetx = gsl[1];
										starty = chunk_loc.getBlockY() + 1;
										startz = gsl[2];
										resetz = gsl[3];
										count = safeLocation(startx, starty, startz, resetx, resetz, randworld, d);
									} else {
										count = 1;
									}
								}
							} else {
								count = 1;
							}
							if (count == 0) {
								break;
							}
						} else {
							if (!plugin.getPluginRespect().getRespect(new Location(randworld, wherex, highest, wherez), new Parameters(p, Flag.getNoMessageFlags()))) {
								return null;
							} else {
								highest = plugin.getConfig().getInt("travel.timeout_height");
								break;
							}
						}
					}
					dest = new Location(randworld, wherex, highest, wherez);
				}
				break;
		}
		return dest;
	}

	/**
	 * Checks if a location is safe for the tardis Police Box to land at. Used for debugging purposes only. The Police
	 * Box requires a clear 4 x 3 x 4 (d x w x h) area.
	 *
	 * @param loc the location to test
	 * @param d   the direction the Police Box is facing.
	 */
	public void testSafeLocation(Location loc, COMPASS d) {
		World w = loc.getWorld();
		int starty = loc.getBlockY();
		int sx, sz;
		switch (d) {
			case EAST -> {
				sx = loc.getBlockX() - 2;
				sz = loc.getBlockZ() - 1;
			}
			case SOUTH -> {
				sx = loc.getBlockX() - 1;
				sz = loc.getBlockZ() - 2;
			}
			default -> {
				sx = loc.getBlockX() - 1;
				sz = loc.getBlockZ() - 1;
			}
		}
		int row, col;
		switch (d) {
			case EAST, WEST -> {
				row = 2;
				col = 3;
			}
			default -> {
				row = 3;
				col = 2;
			}
		}
		int r = row;
		int c = col;
		int startX = sx;
		int startZ = sz;
		assert w != null;
		TARDISBlockSetters.setBlock(w, startX, starty, startZ, Material.SNOW_BLOCK);
		TARDISBlockSetters.setBlock(w, startX, starty, startZ + row, Material.SNOW_BLOCK);
		TARDISBlockSetters.setBlock(w, startX + col, starty, startZ, Material.SNOW_BLOCK);
		TARDISBlockSetters.setBlock(w, startX + col, starty, startZ + row, Material.SNOW_BLOCK);
		TARDISBlockSetters.setBlock(w, startX, starty + 3, startZ, Material.SNOW_BLOCK);
		TARDISBlockSetters.setBlock(w, startX + col, starty + 3, startZ, Material.SNOW_BLOCK);
		TARDISBlockSetters.setBlock(w, startX, starty + 3, startZ + row, Material.SNOW_BLOCK);
		TARDISBlockSetters.setBlock(w, startX + col, starty + 3, startZ + row, Material.SNOW_BLOCK);
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			TARDISBlockSetters.setBlock(w, startX, starty, startZ, Material.AIR);
			TARDISBlockSetters.setBlock(w, startX, starty, startZ + r, Material.AIR);
			TARDISBlockSetters.setBlock(w, startX + c, starty, startZ, Material.AIR);
			TARDISBlockSetters.setBlock(w, startX + c, starty, startZ + r, Material.AIR);
			TARDISBlockSetters.setBlock(w, startX, starty + 3, startZ, Material.AIR);
			TARDISBlockSetters.setBlock(w, startX + c, starty + 3, startZ, Material.AIR);
			TARDISBlockSetters.setBlock(w, startX, starty + 3, startZ + r, Material.AIR);
			TARDISBlockSetters.setBlock(w, startX + c, starty + 3, startZ + r, Material.AIR);
		}, 300L);
	}

	/**
	 * Checks whether a NETHER location is safe to land at.
	 *
	 * @param nether a Nether world to search in.
	 * @param wherex an x co-ordinate.
	 * @param wherez a z co-ordinate.
	 * @param d      the direction the Police Box is facing.
	 * @param p      the player to check permissions for
	 * @return true or false
	 */
	boolean safeNether(World nether, int wherex, int wherez, COMPASS d, Player p) {
		boolean safe = false;
		int startx, starty, startz, resetx, resetz, count;
		int wherey = 100;
		Block startBlock = nether.getBlockAt(wherex, wherey, wherez);
		while (!startBlock.getType().isAir()) {
			startBlock = startBlock.getRelative(BlockFace.DOWN);
		}
		int air = 0;
		while (startBlock.getType().isAir() && startBlock.getLocation().getBlockY() > 30) {
			startBlock = startBlock.getRelative(BlockFace.DOWN);
			air++;
		}
		Material mat = startBlock.getType();
		if (air >= 4 && (plugin.getGeneralKeeper().getGoodNether().contains(mat) ||
						 plugin.getPlanetsConfig().getBoolean("planets." + nether.getName() + ".false_nether"))) {
			Location netherLocation = startBlock.getLocation();
			netherLocation.setY(netherLocation.getY() + 1);
			if (plugin.getPluginRespect().getRespect(netherLocation, new Parameters(p, Flag.getNoMessageFlags()))) {
				// get start location for checking there is enough space
				int[] gsl = getStartLocation(netherLocation, d);
				startx = gsl[0];
				resetx = gsl[1];
				starty = netherLocation.getBlockY();
				startz = gsl[2];
				resetz = gsl[3];
				count = safeLocation(startx, starty, startz, resetx, resetz, nether, d);
			} else {
				count = 1;
			}
			if (count == 0) {
				safe = true;
				dest = netherLocation;
			}
		}
		return safe;
	}

	/**
	 * Returns a random positive or negative x integer.
	 *
	 * @param range   the maximum the random number can be.
	 * @param quarter one fourth of the max_distance config option.
	 * @param rx      the delay of the x-repeater setting.
	 * @param ry      the delay of the y-repeater setting.
	 * @param e       a string to determine where to start the random search from
	 * @param l       the current tardis location
	 */
	private int randomX(int range, int quarter, int rx, int ry, String e, Location l) {
		int currentx = (e.equals("THIS")) ? l.getBlockX() : 0;
		int wherex;
		wherex = TARDISConstants.RANDOM.nextInt(range);
		// add the distance from the x and z repeaters
		wherex += (quarter * rx);
		// add chance of negative values
		if (TARDISConstants.RANDOM.nextInt(2) == 1) {
			wherex = -wherex;
		}
		// use multiplier based on position of third (y) repeater
		wherex *= ry;
		return wherex + currentx;
	}

	/**
	 * Returns a random positive or negative z integer.
	 *
	 * @param range   the maximum the random number can be.
	 * @param quarter one fourth of the max_distance config option.
	 * @param rz      the delay of the x-repeater setting.
	 * @param ry      the delay of the y-repeater setting.
	 * @param e       a string to determine where to start the random search from
	 * @param l       the current tardis location
	 */
	private int randomZ(int range, int quarter, int rz, int ry, String e, Location l) {
		int currentz = (e.equals("THIS")) ? l.getBlockZ() : 0;
		int wherez;
		wherez = TARDISConstants.RANDOM.nextInt(range);
		// add the distance from the x and z repeaters
		wherez += (quarter * rz);
		// add chance of negative values
		if (TARDISConstants.RANDOM.nextInt(2) == 1) {
			wherez = -wherez;
		}
		// use multiplier based on position of third (y) repeater
		wherez *= ry;
		return wherez + currentz;
	}

	public Location submarine(Block b, COMPASS d) {
		Block block = b;
		Material type;
		do {
			block = block.getRelative(BlockFace.DOWN);
			type = block.getType();
		} while (TARDISMaterials.submarine_blocks.contains(type));
		Location loc = block.getRelative(BlockFace.UP).getLocation();
		for (int n = 0; n < attempts; n++) {
			if (isSafeSubmarine(loc, d)) {
				return loc;
			} else {
				loc.setY(loc.getY() + 1);
			}
		}
		return (isSafeSubmarine(loc, d)) ? loc : null;
	}

	public boolean isSafeSubmarine(Location l, COMPASS d) {
		int[] s = getStartLocation(l, d);
		int level, row, col, rowcount, colcount, count = 0;
		int starty = l.getBlockY();
		switch (d) {
			case EAST, WEST -> {
				rowcount = 3;
				colcount = 4;
			}
			default -> {
				rowcount = 4;
				colcount = 3;
			}
		}
		for (level = 0; level < 4; level++) {
			for (row = 0; row < rowcount; row++) {
				for (col = 0; col < colcount; col++) {
					Material mat = Objects.requireNonNull(l.getWorld()).getBlockAt(s[0], starty, s[2]).getType();
					if (!TARDISConstants.GOOD_WATER.contains(mat)) {
						count++;
					}
					s[0] += 1;
				}
				s[0] = s[1];
				s[2] += 1;
			}
			s[2] = s[3];
			starty += 1;
		}
		return (count == 0);
	}
}
