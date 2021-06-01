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
package me.eccentric_nz.tardis.move;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.blueprints.TARDISPermission;
import me.eccentric_nz.tardis.builders.TARDISEmergencyRelocation;
import me.eccentric_nz.tardis.control.TARDISPowerButton;
import me.eccentric_nz.tardis.database.data.TARDIS;
import me.eccentric_nz.tardis.database.resultset.*;
import me.eccentric_nz.tardis.enumeration.COMPASS;
import me.eccentric_nz.tardis.enumeration.PRESET;
import me.eccentric_nz.tardis.flight.TARDISTakeoff;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.mobfarming.TARDISFarmer;
import me.eccentric_nz.tardis.mobfarming.TARDISFollowerSpawner;
import me.eccentric_nz.tardis.mobfarming.TARDISPetsAndFollowers;
import me.eccentric_nz.tardis.travel.TARDISDoorLocation;
import me.eccentric_nz.tardis.utility.TARDISRedProtectChecker;
import me.eccentric_nz.tardis.utility.TARDISResourcePackChanger;
import me.eccentric_nz.tardis.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.tardis.utility.TARDISStaticUtils;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * During tardis operation, a distinctive grinding and whirring sound is usually heard. River Song once demonstrated a
 * tardis was capable of materialising silently, teasing the Doctor that the noise was actually caused by him leaving
 * the brakes on.
 *
 * @author eccentric_nz
 */
public class TARDISDoorWalkListener extends TARDISDoorListener implements Listener {

	public TARDISDoorWalkListener(TARDISPlugin plugin) {
		super(plugin);
	}

	/**
	 * Listens for player interaction with tardis doors. If the door is right-clicked with the tardis key (configurable)
	 * it will teleport the player either into or out of the tardis.
	 *
	 * @param event a player clicking a block
	 */
	@EventHandler(ignoreCancelled = true)
	public void onDoorInteract(PlayerInteractEvent event) {
		if (event.getHand() == null) {
			return;
		}
		Block block = event.getClickedBlock();
		if (block != null) {
			Material blockType = block.getType();
			// only proceed if they are clicking a door!
			if (Tag.DOORS.isTagged(blockType) || Tag.TRAPDOORS.isTagged(blockType)) {
				Player player = event.getPlayer();
				if (TARDISPermission.hasPermission(player, "tardis.enter")) {
					Action action = event.getAction();
					UUID playerUUID = player.getUniqueId();
					World playerWorld = player.getLocation().getWorld();
					Location block_loc = block.getLocation();
					String bw = Objects.requireNonNull(block_loc.getWorld()).getName();
					int bx = block_loc.getBlockX();
					int by = block_loc.getBlockY();
					int bz = block_loc.getBlockZ();
					if (!Tag.TRAPDOORS.isTagged(blockType)) {
						Bisected bisected = (Bisected) block.getBlockData();
						if (bisected.getHalf().equals(Bisected.Half.TOP)) {
							by = (by - 1);
							block = block.getRelative(BlockFace.DOWN);
						}
					}
					String doorloc = bw + ":" + bx + ":" + by + ":" + bz;
					HashMap<String, Object> where = new HashMap<>();
					where.put("door_location", doorloc);
					ResultSetDoors rsd = new ResultSetDoors(plugin, where, false);
					if (rsd.resultSet()) {
						event.setUseInteractedBlock(Event.Result.DENY);
						event.setUseItemInHand(Event.Result.DENY);
						event.setCancelled(true);
						if (event.getHand().equals(EquipmentSlot.OFF_HAND)) {
							return;
						}
						int id = rsd.getTardisId();
						if (plugin.getTrackerKeeper().getMaterialising().contains(id) ||
							plugin.getTrackerKeeper().getDematerialising().contains(id)) {
							TARDISMessage.send(player, "NOT_WHILE_MAT");
							return;
						}
						if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
							TARDISMessage.send(player, "LOST_IN_VORTEX");
							return;
						}
						COMPASS dd = rsd.getDoorDirection();
						int doorType = rsd.getDoorType();
						int endDoorType = switch (doorType) {
							case 0 -> // outside preset door
									1;
							case 2 -> // outside backdoor
									3;
							case 3 -> // inside backdoor
									2;
							default -> // 1, 4 tardis inside door, secondary inside door
									0;
						};
						ItemStack stack = player.getInventory().getItemInMainHand();
						Material material = stack.getType();
						// get key material
						ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, playerUUID.toString());
						String key;
						boolean hasPrefs = false;
						boolean willFarm = false;
						boolean canPowerUp = false;
						if (rsp.resultSet()) {
							hasPrefs = true;
							key = (!rsp.getKey().isEmpty()) ? rsp.getKey() : plugin.getConfig().getString("preferences.key");
							willFarm = rsp.isFarmOn();
							if (rsp.isAutoPowerupOn() && plugin.getConfig().getBoolean("allow.power_down")) {
								// check tardis is not abandoned
								HashMap<String, Object> tid = new HashMap<>();
								tid.put("tardis_id", id);
								ResultSetTardis rs = new ResultSetTardis(plugin, tid, "", false, 2);
								if (rs.resultSet()) {
									canPowerUp = !rs.getTardis().isAbandoned();
								}
							}
						} else {
							key = plugin.getConfig().getString("preferences.key");
						}
						boolean minecart = rsp.isMinecartOn();
						assert key != null;
						Material m = Material.getMaterial(key);
						if (action == Action.LEFT_CLICK_BLOCK) {
							if (stack.hasItemMeta() && Objects.requireNonNull(stack.getItemMeta()).hasDisplayName() &&
								stack.getItemMeta().getDisplayName().equals("tardis Remote Key")) {
								return;
							}
							// must be the owner
							ResultSetTardisID rs = new ResultSetTardisID(plugin);
							if (rs.fromUUID(playerUUID.toString())) {
								// must use key to lock / unlock door
								if (material.equals(m)) {
									if (rs.getTardisId() != id) {
										TARDISMessage.send(player, "DOOR_LOCK_UNLOCK");
										return;
									}
									int locked = (rsd.isLocked()) ? 0 : 1;
									String message = (rsd.isLocked()) ? plugin.getLanguage().getString("DOOR_UNLOCK") : plugin.getLanguage().getString("DOOR_DEADLOCK");
									HashMap<String, Object> setl = new HashMap<>();
									setl.put("locked", locked);
									HashMap<String, Object> wherel = new HashMap<>();
									wherel.put("tardis_id", rsd.getTardisId());
									// always lock / unlock both doors
									plugin.getQueryFactory().doUpdate("doors", setl, wherel);
									TARDISMessage.send(player, "DOOR_LOCK", message);
								} else if (material.isAir() &&
										   rs.getTardisId() != id) { // knock with hand if it's not their tardis
									// only outside the tardis
									if (doorType == 0) {
										// only if companion
										ResultSetCompanions rsc = new ResultSetCompanions(plugin, id);
										if (rsc.getCompanions().contains(playerUUID)) {
											// get Time Lord
											HashMap<String, Object> wherett = new HashMap<>();
											wherett.put("tardis_id", id);
											ResultSetTardis rstt = new ResultSetTardis(plugin, wherett, "", false, 2);
											if (rstt.resultSet()) {
												UUID tluuid = rstt.getTardis().getUuid();
												// only if Time Lord is inside
												HashMap<String, Object> wherev = new HashMap<>();
												wherev.put("uuid", tluuid.toString());
												ResultSetTravellers rsv = new ResultSetTravellers(plugin, wherev, false);
												if (rsv.resultSet()) {
													Player tl = plugin.getServer().getPlayer(tluuid);
													Sound knock = (blockType.equals(Material.IRON_DOOR)) ? Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR : Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR;
													assert tl != null;
													tl.getWorld().playSound(tl.getLocation(), knock, 3.0F, 3.0F);
												}
											}
										}
									}
								}
							}
						}
						if (action == Action.RIGHT_CLICK_BLOCK && !player.isSneaking()) {
							if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
								TARDISMessage.send(player, "SIEGE_NO_EXIT");
								return;
							}
							if (plugin.getTrackerKeeper().getInVortex().contains(id) ||
								plugin.getTrackerKeeper().getMaterialising().contains(id) ||
								plugin.getTrackerKeeper().getDematerialising().contains(id)) {
								TARDISMessage.send(player, "NOT_WHILE_MAT");
								return;
							}
							// handbrake must be on
							HashMap<String, Object> tid = new HashMap<>();
							tid.put("tardis_id", id);
							ResultSetTardis rs = new ResultSetTardis(plugin, tid, "", false, 2);
							if (rs.resultSet()) {
								if (!rs.getTardis().isHandbrakeOn()) {
									TARDISMessage.send(player, "HANDBRAKE_ENGAGE");
									return;
								}
								// must be Time Lord or companion
								ResultSetCompanions rsc = new ResultSetCompanions(plugin, id);
								if (rsc.getCompanions().contains(playerUUID) || rs.getTardis().isAbandoned()) {
									if (!rsd.isLocked()) {
										boolean isPoliceBox = (rs.getTardis().getPreset().isColoured());
										// toggle the door open/closed
										if (Tag.DOORS.isTagged(blockType) ||
											(blockType.equals(Material.OAK_TRAPDOOR) && isPoliceBox)) {
											if (doorType == 0 || doorType == 1) {
												boolean open = TARDISStaticUtils.isDoorOpen(block);
												if (!material.equals(m) && doorType == 0 && !open) {
													// must use key to open the outer door
													String[] split = Objects.requireNonNull(plugin.getRecipesConfig().getString("shaped.Sonic Screwdriver.result")).split(":");
													Material sonic = Material.valueOf(split[0]);
													if (!material.equals(sonic) ||
														!TARDISPermission.hasPermission(player, "tardis.sonic.admin")) {
														TARDISMessage.send(player, "NOT_KEY", key);
													}
													return;
												}
												if (open && rs.getTardis().isAbandoned()) {
													TARDISMessage.send(player, "ABANDONED_DOOR");
													return;
												}
												if (plugin.getTrackerKeeper().getHasClickedHandbrake().contains(id) &&
													doorType == 1) {
													plugin.getTrackerKeeper().getHasClickedHandbrake().removeAll(Collections.singleton(id));
													// toggle handbrake && dematerialise
													new TARDISTakeoff(plugin).run(id, player, rs.getTardis().getBeacon());
												}
												// toggle the door
												if (isPoliceBox) {
													new TARDISCustomModelDataChanger(plugin, block, player, id).toggleOuterDoor();
												} else {
													if (doorType == 1 ||
														!plugin.getPM().isPluginEnabled("RedProtect") ||
														TARDISRedProtectChecker.shouldToggleDoor(block)) {
														new TARDISDoorToggler(plugin, block, player, minecart, open, id).toggleDoors();
													} else {
														new TARDISInnerDoorOpener(plugin, playerUUID, id).openDoor();
													}
												}
											}
										} else if (Tag.TRAPDOORS.isTagged(blockType)) {
											TrapDoor door_data = (TrapDoor) block.getBlockData();
											door_data.setOpen(!door_data.isOpen());
										}
									} else if (rs.getTardis().getUuid() != playerUUID) {
										TARDISMessage.send(player, "DOOR_DEADLOCKED");
									} else {
										TARDISMessage.send(player, "DOOR_UNLOCK");
									}
								} else {
									TARDISMessage.send(player, "SIEGE_COMPANION");
								}
							}
						} else if (action == Action.RIGHT_CLICK_BLOCK && player.isSneaking()) {
							if (!material.equals(m) && doorType == 0) {
								// must use key to open and close the outer door
								TARDISMessage.send(player, "NOT_KEY", key);
								return;
							}
							if (rsd.isLocked()) {
								TARDISMessage.send(player, "DOOR_DEADLOCKED");
								return;
							}
							if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
								TARDISMessage.send(player, "SIEGE_NO_EXIT");
								return;
							}
							HashMap<String, Object> tid = new HashMap<>();
							tid.put("tardis_id", id);
							ResultSetTardis rs = new ResultSetTardis(plugin, tid, "", false, 2);
							if (rs.resultSet()) {
								TARDIS tardis = rs.getTardis();
								if (!tardis.isHandbrakeOn()) {
									TARDISMessage.send(player, "HANDBRAKE_ENGAGE");
									return;
								}
								int artron = tardis.getArtronLevel();
								int required = plugin.getArtronConfig().getInt("backdoor");
								UUID tlUUID = tardis.getUuid();
								PRESET preset = tardis.getPreset();
								float yaw = player.getLocation().getYaw();
								float pitch = player.getLocation().getPitch();
								String companions = tardis.getCompanions();
								boolean hb = tardis.isHandbrakeOn();
								boolean po = !tardis.isPowered() && !tardis.isAbandoned();
								HashMap<String, Object> wherecl = new HashMap<>();
								wherecl.put("tardis_id", tardis.getTardisId());
								ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
								if (!rsc.resultSet()) {
									// emergency tardis relocation
									new TARDISEmergencyRelocation(plugin).relocate(id, player);
									return;
								}
								COMPASS d_backup = rsc.getDirection();
								// get quotes player prefs
								boolean userQuotes = true;
								boolean userTP = false;
								if (hasPrefs) {
									userQuotes = rsp.isQuotesOn();
									userTP = rsp.isTextureOn();
								}
								// get players direction
								COMPASS pd = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(player, false));
								// get the other door direction
								COMPASS d;
								HashMap<String, Object> other = new HashMap<>();
								other.put("tardis_id", id);
								other.put("door_type", endDoorType);
								ResultSetDoors rse = new ResultSetDoors(plugin, other, false);
								if (rse.resultSet()) {
									d = rse.getDoorDirection();
								} else {
									d = d_backup;
								}
								switch (doorType) {
									case 1:
									case 4:
										// is the tardis materialising?
										if (plugin.getTrackerKeeper().getInVortex().contains(id) ||
											plugin.getTrackerKeeper().getMaterialising().contains(id) ||
											plugin.getTrackerKeeper().getDematerialising().contains(id)) {
											TARDISMessage.send(player, "LOST_IN_VORTEX");
											return;
										}
										// Can't SHIFT-click if INVISIBLE preset
										if (preset.equals(PRESET.INVISIBLE)) {
											TARDISMessage.send(player, "INVISIBILITY_SNEAK");
											return;
										}
										Location exitLoc;
										// player is in the tardis - always exit to current location
										Block door_bottom;
										Door door = (Door) block.getBlockData();
										door_bottom = (door.getHalf().equals(Bisected.Half.TOP)) ? block.getRelative(BlockFace.DOWN) : block;
										boolean opened = TARDISStaticUtils.isDoorOpen(door_bottom);
										if (opened && preset.hasDoor()) {
											exitLoc = TARDISStaticLocationGetters.getLocationFromDB(rse.getDoorLocation());
										} else {
											exitLoc = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ(), yaw, pitch);
										}
										if (hb && exitLoc != null) {
											// change the yaw if the door directions are different
											if (!dd.equals(d)) {
												yaw += adjustYaw(dd, d);
											}
											exitLoc.setYaw(yaw);
											// get location from database
											// make location safe ie. outside of the bluebox
											double ex = exitLoc.getX();
											double ez = exitLoc.getZ();
											if (opened) {
												exitLoc.setX(ex + 0.5);
												exitLoc.setZ(ez + 0.5);
											} else {
												switch (d) {
													case NORTH -> {
														exitLoc.setX(ex + 0.5);
														exitLoc.setZ(ez + 2.5);
													}
													case EAST -> {
														exitLoc.setX(ex - 1.5);
														exitLoc.setZ(ez + 0.5);
													}
													case SOUTH -> {
														exitLoc.setX(ex + 0.5);
														exitLoc.setZ(ez - 1.5);
													}
													case WEST -> {
														exitLoc.setX(ex + 2.5);
														exitLoc.setZ(ez + 0.5);
													}
												}
											}
											// exit tardis!
											movePlayer(player, exitLoc, true, playerWorld, userQuotes, 2, minecart);
											if (plugin.getConfig().getBoolean("allow.mob_farming") &&
												TARDISPermission.hasPermission(player, "tardis.farm")) {
												TARDISFarmer tf = new TARDISFarmer(plugin);
												TARDISPetsAndFollowers petsAndFollowers = tf.exitPets(player);
												if (petsAndFollowers != null) {
													if (petsAndFollowers.getPets().size() > 0) {
														plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> movePets(petsAndFollowers.getPets(), exitLoc, player, d, false), 10L);
													}
													if (petsAndFollowers.getFollowers().size() > 0) {
														new TARDISFollowerSpawner(plugin).spawn(petsAndFollowers.getFollowers(), exitLoc, player, d, false);
													}
												}
											}
											if (plugin.getConfig().getBoolean("allow.tp_switch") && userTP) {
												new TARDISResourcePackChanger(plugin).changeRP(player, rsp.getTextureOut());
											}
											// remove player from traveller table
											removeTraveller(playerUUID);
										} else {
											TARDISMessage.send(player, "LOST_IN_VORTEX");
										}
										break;
									case 0:
										// is the tardis materialising?
										if (plugin.getTrackerKeeper().getInVortex().contains(id) ||
											plugin.getTrackerKeeper().getMaterialising().contains(id) ||
											plugin.getTrackerKeeper().getDematerialising().contains(id)) {
											TARDISMessage.send(player, "LOST_IN_VORTEX");
											return;
										}
										boolean chkCompanion = false;
										if (!playerUUID.equals(tlUUID)) {
											if (companions != null && !companions.isEmpty()) {
												// is the player in the companion list
												if (companions.equalsIgnoreCase("everyone")) {
													chkCompanion = true;
												} else {
													String[] companionData = companions.split(":");
													for (String c : companionData) {
														if (c.equalsIgnoreCase(playerUUID.toString())) {
															chkCompanion = true;
															break;
														}
													}
												}
											}
										}
										if (playerUUID.equals(tlUUID) || chkCompanion ||
											TARDISPermission.hasPermission(player, "tardis.skeletonkey") ||
											tardis.isAbandoned()) {
											// get INNER tardis location
											TARDISDoorLocation idl = getDoor(1, id);
											Location tardis_loc = idl.getL();
											World cw = idl.getW();
											COMPASS innerD = idl.getD();
											// check for entities near the police box
											TARDISPetsAndFollowers petsAndFollowers = null;
											if (plugin.getConfig().getBoolean("allow.mob_farming") &&
												TARDISPermission.hasPermission(player, "tardis.farm") &&
												!plugin.getTrackerKeeper().getFarming().contains(playerUUID) &&
												willFarm) {
												plugin.getTrackerKeeper().getFarming().add(playerUUID);
												TARDISFarmer tf = new TARDISFarmer(plugin);
												assert playerWorld != null;
												petsAndFollowers = tf.farmAnimals(block_loc, d, id, player.getPlayer(), Objects.requireNonNull(tardis_loc.getWorld()).getName(), playerWorld.getName());
											}
											// if WorldGuard is on the server check for tardis region protection and add admin as member
											if (plugin.isWorldGuardOnServer() &&
												plugin.getConfig().getBoolean("preferences.use_worldguard") &&
												TARDISPermission.hasPermission(player, "tardis.skeletonkey")) {
												plugin.getWorldGuardUtils().addMemberToRegion(cw, tardis.getOwner(), player.getName());
											}
											// enter tardis!
											cw.getChunkAt(tardis_loc).load();
											tardis_loc.setPitch(pitch);
											// get inner door direction so we can adjust yaw if necessary
											if (!innerD.equals(pd)) {
												yaw += adjustYaw(pd, innerD);
											}
											tardis_loc.setYaw(yaw);
											movePlayer(player, tardis_loc, false, playerWorld, userQuotes, 1, minecart);
											if (petsAndFollowers != null) {
												if (petsAndFollowers.getPets().size() > 0) {
													movePets(petsAndFollowers.getPets(), tardis_loc, player, d, true);
												}
												if (petsAndFollowers.getFollowers().size() > 0) {
													new TARDISFollowerSpawner(plugin).spawn(petsAndFollowers.getFollowers(), tardis_loc, player, d, true);
												}
											}
											if (plugin.getConfig().getBoolean("allow.tp_switch") && userTP) {
												if (!rsp.getTextureIn().isEmpty()) {
													new TARDISResourcePackChanger(plugin).changeRP(player, rsp.getTextureIn());
												}
											}
											if (canPowerUp && po) {
												// power up the tardis
												plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TARDISPowerButton(plugin, id, player, tardis.getPreset(), false, tardis.isHidden(), tardis.isLightsOn(), player.getLocation(), artron, tardis.getSchematic().hasLanterns()).clickButton(), 20L);
											}
											// put player into travellers table
											// remove them first as they may have exited incorrectly and we only want them listed once
											removeTraveller(playerUUID);
											HashMap<String, Object> set = new HashMap<>();
											set.put("tardis_id", id);
											set.put("uuid", playerUUID.toString());
											plugin.getQueryFactory().doSyncInsert("travellers", set);
										} else {
											TARDISMessage.send(player, "SIEGE_COMPANION");
										}
										break;
									case 2:
										if (artron < required) {
											TARDISMessage.send(player, "NOT_ENOUGH_DOOR_ENERGY");
											return;
										}
										if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
											TARDISMessage.send(player, "SIEGE_NO_ENTER");
											return;
										}
										if (preset.equals(PRESET.JUNK_MODE)) {
											TARDISMessage.send(player, "JUNK_NO_ENTRY");
											return;
										}
										// always enter by the back door
										TARDISDoorLocation ibdl = getDoor(3, id);
										Location inner_loc = ibdl.getL();
										if (inner_loc == null) {
											TARDISMessage.send(player, "DOOR_BACK_IN");
											return;
										}
										COMPASS ibdd = ibdl.getD();
										COMPASS ipd = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(player, true));
										if (!ibdd.equals(ipd)) {
											yaw += adjustYaw(ipd, ibdd) + 180F;
										}
										inner_loc.setYaw(yaw);
										inner_loc.setPitch(pitch);
										movePlayer(player, inner_loc, false, playerWorld, userQuotes, 1, minecart);
										if (plugin.getConfig().getBoolean("allow.tp_switch") && userTP) {
											if (!rsp.getTextureIn().isEmpty()) {
												new TARDISResourcePackChanger(plugin).changeRP(player, rsp.getTextureIn());
											}
										}
										// put player into travellers table
										removeTraveller(playerUUID);
										HashMap<String, Object> set = new HashMap<>();
										set.put("tardis_id", id);
										set.put("uuid", playerUUID.toString());
										plugin.getQueryFactory().doSyncInsert("travellers", set);
										HashMap<String, Object> wheree = new HashMap<>();
										wheree.put("tardis_id", id);
										int cost = (-plugin.getArtronConfig().getInt("backdoor"));
										plugin.getQueryFactory().alterEnergyLevel("tardis", cost, wheree, player);
										break;
									case 3:
										if (artron < required) {
											TARDISMessage.send(player, "NOT_ENOUGH_DOOR_ENERGY");
											return;
										}
										if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
											TARDISMessage.send(player, "SIEGE_NO_EXIT");
											return;
										}
										// always exit to outer back door
										TARDISDoorLocation obdl = getDoor(2, id);
										Location outer_loc = obdl.getL();
										if (outer_loc == null) {
											TARDISMessage.send(player, "DOOR_BACK_OUT");
											return;
										}
										// backdoor is located in the end
										if (Objects.requireNonNull(outer_loc.getWorld()).getEnvironment().equals(Environment.THE_END)) {
											// check enabled
											if (!plugin.getConfig().getBoolean("travel.the_end")) {
												TARDISMessage.send(player, "ANCIENT", "End");
												return;
											}
											// check permission
											if (!TARDISPermission.hasPermission(player, "tardis.end")) {
												TARDISMessage.send(player, "NO_PERM_TRAVEL", "End");
												return;
											}
											// check traveled to
											if (plugin.getConfig().getBoolean("travel.allow_end_after_visit") &&
												!new ResultSetTravelledTo(plugin).resultSet(playerUUID.toString(), "THE_END")) {
												TARDISMessage.send(player, "TRAVEL_NOT_VISITED", "End");
												return;
											}
										}
										// backdoor located in the nether
										if (outer_loc.getWorld().getEnvironment().equals(Environment.NETHER)) {
											// check enabled
											if (!plugin.getConfig().getBoolean("travel.nether")) {
												TARDISMessage.send(player, "ANCIENT", "Nether");
												return;
											}
											// check permission
											if (!TARDISPermission.hasPermission(player, "tardis.nether")) {
												TARDISMessage.send(player, "NO_PERM_TRAVEL", "Nether");
												return;
											}
											// check traveled to
											if (plugin.getConfig().getBoolean("travel.allow_nether_after_visit") &&
												!new ResultSetTravelledTo(plugin).resultSet(playerUUID.toString(), "NETHER")) {
												TARDISMessage.send(player, "TRAVEL_NOT_VISITED", "Nether");
												return;
											}
										}
										COMPASS obdd = obdl.getD();
										COMPASS opd = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(player, false));
										if (!obdd.equals(opd)) {
											yaw += adjustYaw(opd, obdd);
										}
										outer_loc.setYaw(yaw);
										outer_loc.setPitch(pitch);
										movePlayer(player, outer_loc, true, playerWorld, userQuotes, 2, minecart);
										if (plugin.getConfig().getBoolean("allow.tp_switch") && userTP) {
											new TARDISResourcePackChanger(plugin).changeRP(player, rsp.getTextureOut());
										}
										// remove player from traveller table
										removeTraveller(playerUUID);
										// take energy
										HashMap<String, Object> wherea = new HashMap<>();
										wherea.put("tardis_id", id);
										int costa = (-plugin.getArtronConfig().getInt("backdoor"));
										plugin.getQueryFactory().alterEnergyLevel("tardis", costa, wherea, player);
										break;
									default:
										// do nothing
										break;
								}
							}
						}
					}
				}
			}
		}
	}
}
