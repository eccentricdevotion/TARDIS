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
package me.eccentric_nz.tardis.control;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.advanced.TARDISCircuitChecker;
import me.eccentric_nz.tardis.advanced.TARDISCircuitDamager;
import me.eccentric_nz.tardis.blueprints.TARDISPermission;
import me.eccentric_nz.tardis.database.resultset.ResultSetControls;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetNextLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.tardis.enumeration.COMPASS;
import me.eccentric_nz.tardis.enumeration.Difficulty;
import me.eccentric_nz.tardis.enumeration.DiskCircuit;
import me.eccentric_nz.tardis.enumeration.WorldManager;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.planets.TARDISAliasResolver;
import me.eccentric_nz.tardis.planets.TARDISBiome;
import me.eccentric_nz.tardis.rooms.TARDISExteriorRenderer;
import me.eccentric_nz.tardis.utility.TARDISSounds;
import me.eccentric_nz.tardis.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.tardis.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Scanner consists of a collection of thousands of instruments designed to gather information about the environment
 * outside a tardis. Chief among these is the visual signal, which is displayed on the Scanner Screen found in any of
 * the Control Rooms.
 *
 * @author eccentric_nz
 */
public class TARDISScanner {

	private final TARDISPlugin plugin;

	TARDISScanner(TARDISPlugin plugin) {
		this.plugin = plugin;
		List<Material> validBlocks = new ArrayList<>();
		validBlocks.add(Material.LEVER);
		validBlocks.add(Material.COMPARATOR);
		validBlocks.addAll(Tag.BUTTONS.getValues());
	}

	public static List<Entity> getNearbyEntities(Location l, int radius) {
		int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
		List<Entity> radiusEntities = new ArrayList<>();
		for (int chX = -chunkRadius; chX <= chunkRadius; chX++) {
			for (int chZ = -chunkRadius; chZ <= chunkRadius; chZ++) {
				int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
				for (Entity e : new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities()) {
					if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock()) {
						radiusEntities.add(e);
					}
				}
			}
		}
		return radiusEntities;
	}

	public static TARDISScannerData scan(Player player, int id, BukkitScheduler bsched) {
		TARDISScannerData data = new TARDISScannerData();
		TARDISSounds.playTARDISSound(player.getLocation(), "tardis_scanner");
		Location scan_loc;
		String whereisit;
		COMPASS tardisDirection;
		HashMap<String, Object> wherenl = new HashMap<>();
		wherenl.put("tardis_id", id);
		if (TARDISPlugin.plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
			ResultSetNextLocation rsn = new ResultSetNextLocation(TARDISPlugin.plugin, wherenl);
			if (!rsn.resultSet()) {
				TARDISMessage.send(player, "NEXT_NOT_FOUND");
				return null;
			}
			scan_loc = new Location(rsn.getWorld(), rsn.getX(), rsn.getY(), rsn.getZ());
			tardisDirection = rsn.getDirection();
			whereisit = TARDISPlugin.plugin.getLanguage().getString("SCAN_NEXT");
		} else {
			ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(TARDISPlugin.plugin, wherenl);
			if (!rsc.resultSet()) {
				TARDISMessage.send(player, "CURRENT_NOT_FOUND");
				return null;
			}
			scan_loc = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
			tardisDirection = rsc.getDirection();
			whereisit = TARDISPlugin.plugin.getLanguage().getString("SCAN_CURRENT");
		}
		data.setScanLocation(scan_loc);
		data.setTardisDirection(tardisDirection);
		// record nearby entities
		HashMap<EntityType, Integer> scannedentities = new HashMap<>();
		List<String> playernames = new ArrayList<>();
		for (Entity k : getNearbyEntities(scan_loc, 16)) {
			EntityType et = k.getType();
			if (TARDISConstants.ENTITY_TYPES.contains(et)) {
				boolean visible = true;
				if (et.equals(EntityType.PLAYER)) {
					Player entPlayer = (Player) k;
					if (player.canSee(entPlayer)) {
						playernames.add(entPlayer.getName());
					} else {
						visible = false;
					}
				}
				if (TARDISPlugin.plugin.getPM().isPluginEnabled("TARDISWeepingAngels")) {
					if (et.equals(EntityType.SKELETON) || et.equals(EntityType.ZOMBIE) || et.equals(EntityType.ZOMBIFIED_PIGLIN)) {
						EntityEquipment ee = ((LivingEntity) k).getEquipment();
						if (ee.getHelmet() != null) {
							switch (ee.getHelmet().getType()) {
								case SLIME_BALL: // dalek
									et = EntityType.LLAMA_SPIT;
									break;
								case IRON_INGOT: // Cyberman
									et = EntityType.AREA_EFFECT_CLOUD;
									break;
								case SUGAR: // Empty Child
									et = EntityType.FALLING_BLOCK;
									break;
								case SNOWBALL: // Ice Warrior
									et = EntityType.ARROW;
									break;
								case FEATHER: // Silurian
									et = EntityType.BOAT;
									break;
								case POTATO: // Sontaran
									et = EntityType.FIREWORK;
									break;
								case BAKED_POTATO: // Strax
									et = EntityType.EGG;
									break;
								case BOOK: // Vashta Nerada
									et = EntityType.ENDER_CRYSTAL;
									break;
								case PAINTING: // Zygon
									et = EntityType.FISHING_HOOK;
									break;
								case STONE_BUTTON: // weeping angel
									et = EntityType.DRAGON_FIREBALL;
									break;
								default:
									break;
							}
						}
					}
					if (et.equals(EntityType.ENDERMAN) && k.getPassengers().size() > 0 && k.getPassengers().get(0) != null && k.getPassengers().get(0).getType().equals(EntityType.GUARDIAN)) {
						// silent
						et = EntityType.SPLASH_POTION;
					}
					if (et.equals(EntityType.ARMOR_STAND)) {
						EntityEquipment ee = ((ArmorStand) k).getEquipment();
						if (ee.getHelmet() != null) {
							switch (ee.getHelmet().getType()) {
								case YELLOW_DYE: // Judoon
									et = EntityType.SHULKER_BULLET;
									break;
								case BONE: // K9
									et = EntityType.EVOKER_FANGS;
									break;
								case ROTTEN_FLESH: // Ood
									et = EntityType.ITEM_FRAME;
									break;
								case GUNPOWDER: // Toclafane
									et = EntityType.DROPPED_ITEM;
									break;
								default:
									break;
							}
						}
					}
				}
				Integer entity_count = scannedentities.getOrDefault(et, 0);
				if (visible) {
					scannedentities.put(et, entity_count + 1);
				}
			}
		}
		if (TARDISPermission.hasPermission(player, "tardis.scanner.map")) {
			// is there a scanner map item frame?
			HashMap<String, Object> where = new HashMap<>();
			where.put("tardis_id", id);
			where.put("type", 37);
			ResultSetControls rs = new ResultSetControls(TARDISPlugin.plugin, where, true);
			if (rs.resultSet()) {
				// get the item frame
				Location mapFrame = TARDISStaticLocationGetters.getLocationFromBukkitString(rs.getLocation());
				if (mapFrame != null) {
					while (!mapFrame.getChunk().isLoaded()) {
						mapFrame.getChunk().load();
					}
				}
				ItemFrame itemFrame = null;
				for (Entity e : mapFrame.getWorld().getNearbyEntities(mapFrame, 1.0d, 1.0d, 1.0d)) {
					if (e instanceof ItemFrame) {
						itemFrame = (ItemFrame) e;
						break;
					}
				}
				if (itemFrame != null) {
					if (itemFrame.getItem().getType() == Material.FILLED_MAP) {
						new TARDISScannerMap(TARDISPlugin.plugin, scan_loc, itemFrame).setMap();
					}
				}
			}
		}
		long time = scan_loc.getWorld().getTime();
		data.setTime(time);
		String daynight = TARDISStaticUtils.getTime(time);
		// message the player
		TARDISMessage.send(player, "SCAN_RESULT", whereisit);
		String worldname;
		if (TARDISPlugin.plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) {
			worldname = TARDISPlugin.plugin.getMVHelper().getAlias(scan_loc.getWorld());
		} else {
			worldname = TARDISAliasResolver.getWorldAlias(scan_loc.getWorld());
		}
		TARDISMessage.send(player, "SCAN_WORLD", worldname);
		TARDISMessage.send(player, "SONIC_COORDS", scan_loc.getBlockX() + ":" + scan_loc.getBlockY() + ":" + scan_loc.getBlockZ());
		bsched.scheduleSyncDelayedTask(TARDISPlugin.plugin, () -> TARDISMessage.send(player, "SCAN_DIRECTION", tardisDirection.toString()), 20L);
		// get biome
		TARDISBiome tmb;
		if (whereisit.equals(TARDISPlugin.plugin.getLanguage().getString("SCAN_CURRENT"))) {
			// adjust for current location as it will always return DEEP_OCEAN if set_biome is true
			tmb = switch (tardisDirection) {
				case NORTH -> TARDISStaticUtils.getBiomeAt(scan_loc.getBlock().getRelative(BlockFace.SOUTH, 6).getLocation());
				case WEST -> TARDISStaticUtils.getBiomeAt(scan_loc.getBlock().getRelative(BlockFace.EAST, 6).getLocation());
				case SOUTH -> TARDISStaticUtils.getBiomeAt(scan_loc.getBlock().getRelative(BlockFace.NORTH, 6).getLocation());
				default -> TARDISStaticUtils.getBiomeAt(scan_loc.getBlock().getRelative(BlockFace.WEST, 6).getLocation());
			};
		} else {
			tmb = TARDISStaticUtils.getBiomeAt(scan_loc);
		}
		String biome = tmb.name();
		data.setScannedBiome(biome);
		bsched.scheduleSyncDelayedTask(TARDISPlugin.plugin, () -> TARDISMessage.send(player, "BIOME_TYPE", biome), 40L);
		bsched.scheduleSyncDelayedTask(TARDISPlugin.plugin, () -> TARDISMessage.send(player, "SCAN_TIME", daynight + " / " + time), 60L);
		// get weather
		String weather = switch (biome) {
			case "DESERT", "DESERT_HILLS", "DESERT_LAKES", "SAVANNA", "SAVANNA_PLATEAU", "SHATTERED_SAVANNA", "SHATTERED_SAVANNA_PLATEAU", "BADLANDS", "BADLANDS_PLATEAU", "ERODED_BADLANDS", "MODIFIED_BADLANDS_PLATEAU", "MODIFIED_WOODED_BADLANDS_PLATEAU", "WOODED_BADLANDS_PLATEAU" -> TARDISPlugin.plugin.getLanguage().getString("WEATHER_DRY");
			case "SNOWY_TUNDRA", "ICE_SPIKES", "FROZEN_OCEAN", "FROZEN_RIVER", "SNOWY_BEACH", "SNOWY_TAIGA", "SNOWY_MOUNTAINS", "SNOWY_TAIGA_HILLS", "SNOWY_TAIGA_MOUNTAINS" -> (scan_loc.getWorld().hasStorm()) ? TARDISPlugin.plugin.getLanguage().getString("WEATHER_SNOW") : TARDISPlugin.plugin.getLanguage().getString("WEATHER_COLD");
			default -> (scan_loc.getWorld().hasStorm()) ? TARDISPlugin.plugin.getLanguage().getString("WEATHER_RAIN") : TARDISPlugin.plugin.getLanguage().getString("WEATHER_CLEAR");
		};
		bsched.scheduleSyncDelayedTask(TARDISPlugin.plugin, () -> TARDISMessage.send(player, "SCAN_WEATHER", weather), 80L);
		bsched.scheduleSyncDelayedTask(TARDISPlugin.plugin, () -> TARDISMessage.send(player, "SCAN_HUMIDITY", String.format("%.2f", scan_loc.getBlock().getHumidity())), 100L);
		bsched.scheduleSyncDelayedTask(TARDISPlugin.plugin, () -> TARDISMessage.send(player, "SCAN_TEMP", String.format("%.2f", scan_loc.getBlock().getTemperature())), 120L);
		bsched.scheduleSyncDelayedTask(TARDISPlugin.plugin, () -> {
			if (scannedentities.size() > 0) {
				TARDISMessage.send(player, "SCAN_ENTS");
				for (Map.Entry<EntityType, Integer> entry : scannedentities.entrySet()) {
					String message = "";
					StringBuilder buf = new StringBuilder();
					if (entry.getKey().equals(EntityType.PLAYER) && playernames.size() > 0) {
						playernames.forEach((p) -> buf.append(", ").append(p));
						message = " (" + buf.substring(2) + ")";
					}
					switch (entry.getKey()) {
						case AREA_EFFECT_CLOUD:
							player.sendMessage("    Cyberman: " + entry.getValue());
							break;
						case LLAMA_SPIT:
							player.sendMessage("    Dalek: " + entry.getValue());
							break;
						case FALLING_BLOCK:
							player.sendMessage("    Empty Child: " + entry.getValue());
							break;
						case ARROW:
							player.sendMessage("    Ice Warrior: " + entry.getValue());
							break;
						case SHULKER_BULLET:
							player.sendMessage("    Judoon: " + entry.getValue());
							break;
						case EVOKER_FANGS:
							player.sendMessage("    K9: " + entry.getValue());
							break;
						case ITEM_FRAME:
							player.sendMessage("    Ood: " + entry.getValue());
							break;
						case SPLASH_POTION:
							player.sendMessage("    Silent: " + entry.getValue());
							break;
						case BOAT:
							player.sendMessage("    Silurian: " + entry.getValue());
							break;
						case FIREWORK:
							player.sendMessage("    Sontaran: " + entry.getValue());
							break;
						case EGG:
							player.sendMessage("    Strax: " + entry.getValue());
							break;
						case DROPPED_ITEM:
							player.sendMessage("    Toclafane: " + entry.getValue());
							break;
						case ENDER_CRYSTAL:
							player.sendMessage("    Vashta Nerada: " + entry.getValue());
							break;
						case DRAGON_FIREBALL:
							player.sendMessage("    Weeping Angel: " + entry.getValue());
							break;
						case FISHING_HOOK:
							player.sendMessage("    Zygon: " + entry.getValue());
							break;
						default:
							if (entry.getKey() != EntityType.ARMOR_STAND) {
								player.sendMessage("    " + entry.getKey() + ": " + entry.getValue() + message);
							}
							break;
					}
				}
				scannedentities.clear();
			} else {
				TARDISMessage.send(player, "SCAN_NONE");
			}
			// damage the circuit if configured
			if (TARDISPlugin.plugin.getConfig().getBoolean("circuits.damage") && !TARDISPlugin.plugin.getDifficulty().equals(Difficulty.EASY) && TARDISPlugin.plugin.getConfig().getInt("circuits.uses.scanner") > 0) {
				TARDISCircuitChecker tcc = new TARDISCircuitChecker(TARDISPlugin.plugin, id);
				tcc.getCircuits();
				// decrement uses
				int uses_left = tcc.getScannerUses();
				new TARDISCircuitDamager(TARDISPlugin.plugin, DiskCircuit.SCANNER, uses_left, id, player).damage();
			}
		}, 140L);
		return data;
	}

	public void scan(Player player, int id, String renderer, int level) {
		// get tardis from saved scanner location
		HashMap<String, Object> where = new HashMap<>();
		where.put("tardis_id", id);
		where.put("type", 33);
		ResultSetControls rs = new ResultSetControls(plugin, where, true);
		if (rs.resultSet()) {
			TARDISCircuitChecker tcc = null;
			if (!plugin.getDifficulty().equals(Difficulty.EASY) && !plugin.getUtils().inGracePeriod(player, false)) {
				tcc = new TARDISCircuitChecker(plugin, id);
				tcc.getCircuits();
			}
			if (tcc != null && !tcc.hasScanner()) {
				TARDISMessage.send(player, "SCAN_MISSING");
				return;
			}
			if (plugin.getTrackerKeeper().getHasRandomised().contains(id)) {
				TARDISMessage.send(player, "SCAN_NO_RANDOM");
				return;
			}
			BukkitScheduler bsched = plugin.getServer().getScheduler();
			TARDISScannerData data = scan(player, id, bsched);
			if (data != null) {
				boolean extrend = true;
				ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
				if (rsp.resultSet()) {
					extrend = rsp.isRendererOn();
				}
				if (!renderer.isEmpty() && extrend) {
					int required = plugin.getArtronConfig().getInt("render");
					if (level > required) {
						bsched.scheduleSyncDelayedTask(plugin, () -> {
							if (player.isOnline() && plugin.getUtils().inTARDISWorld(player)) {
								TARDISExteriorRenderer ter = new TARDISExteriorRenderer(plugin);
								ter.render(renderer, data.getScanLocation(), id, player, data.getTardisDirection(), data.getTime(), data.getScannedBiome());
							}
						}, 160L);
					} else {
						TARDISMessage.send(player, "ENERGY_NO_RENDER");
					}
				}
			}
		}
	}
}
