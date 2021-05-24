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
package me.eccentric_nz.tardis.move;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.api.event.TARDISEnterEvent;
import me.eccentric_nz.tardis.api.event.TARDISExitEvent;
import me.eccentric_nz.tardis.chatGUI.TARDISUpdateChatGUI;
import me.eccentric_nz.tardis.database.resultset.ResultSetDoors;
import me.eccentric_nz.tardis.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.tardis.enumeration.COMPASS;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.mobfarming.TARDISPet;
import me.eccentric_nz.tardis.travel.TARDISDoorLocation;
import me.eccentric_nz.tardis.utility.TARDISItemRenamer;
import me.eccentric_nz.tardis.utility.TARDISSounds;
import me.eccentric_nz.tardis.utility.TARDISStaticLocationGetters;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISDoorListener {

	public final float[][] adjustYaw = new float[4][4];
	final TARDISPlugin plugin;
	private final int player_artron;

	public TARDISDoorListener(TARDISPlugin plugin) {
		this.plugin = plugin;
		player_artron = this.plugin.getArtronConfig().getInt("player");
		// yaw adjustments if inner and outer door directions are different
		adjustYaw[0][0] = 0;
		adjustYaw[0][1] = 90;
		adjustYaw[0][2] = 180;
		adjustYaw[0][3] = -90;
		adjustYaw[1][0] = -90;
		adjustYaw[1][1] = 0;
		adjustYaw[1][2] = 90;
		adjustYaw[1][3] = 180;
		adjustYaw[2][0] = 180;
		adjustYaw[2][1] = -90;
		adjustYaw[2][2] = 0;
		adjustYaw[2][3] = 90;
		adjustYaw[3][0] = 90;
		adjustYaw[3][1] = 180;
		adjustYaw[3][2] = -90;
		adjustYaw[3][3] = 0;
	}

	/**
	 * A method to teleport the player into and out of the TARDIS.
	 *
	 * @param p      the player to teleport
	 * @param l      the location to teleport to
	 * @param exit   whether the player is entering or exiting the TARDIS, if true they are exiting
	 * @param from   the world they are teleporting from
	 * @param quotes whether the player will receive a TARDIS quote message
	 * @param sound  an integer representing the sound to play
	 * @param m      whether to play the resource pack sound
	 */
	public void movePlayer(Player p, Location l, boolean exit, World from, boolean quotes, int sound, boolean m) {
		int i = TARDISConstants.RANDOM.nextInt(plugin.getGeneralKeeper().getQuotes().size());
		World to = l.getWorld();
		boolean allowFlight = p.getAllowFlight();
		boolean crossWorlds = (from != to);
		boolean isSurvival = checkSurvival(to);

		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			p.teleport(l);
			playDoorSound(p, sound, l, m);
		}, 5L);
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			p.teleport(l);
			if (p.getGameMode() == GameMode.CREATIVE || (allowFlight && crossWorlds && !isSurvival)) {
				p.setAllowFlight(true);
			}
			if (quotes) {
				if (TARDISConstants.RANDOM.nextInt(100) < 3) {
					TARDISUpdateChatGUI.sendJSON(plugin.getJsonKeeper().getEgg(), p);
				} else {
					p.sendMessage(plugin.getPluginName() + plugin.getGeneralKeeper().getQuotes().get(i));
				}
			}
			if (exit) {
				plugin.getPM().callEvent(new TARDISExitEvent(p, to));
				// give some artron energy to player
				HashMap<String, Object> where = new HashMap<>();
				UUID uuid = p.getUniqueId();
				where.put("uuid", uuid.toString());
				if (plugin.getTrackerKeeper().getHasTravelled().contains(uuid)) {
					plugin.getQueryFactory().alterEnergyLevel("player_prefs", player_artron, where, p);
					plugin.getTrackerKeeper().getHasTravelled().remove(uuid);
				}
				if (plugin.getTrackerKeeper().getSetTime().containsKey(uuid)) {
					setTemporalLocation(p, plugin.getTrackerKeeper().getSetTime().get(uuid));
					plugin.getTrackerKeeper().getSetTime().remove(uuid);
				}
				plugin.getTrackerKeeper().getEjecting().remove(uuid);
			} else {
				plugin.getPM().callEvent(new TARDISEnterEvent(p, from));
				if (p.isPlayerTimeRelative()) {
					setTemporalLocation(p, -1);
				}
				TARDISSounds.playTARDISHum(p);
			}
			// give a key
			giveKey(p);
		}, 10L);
	}

	/**
	 * Checks if the world the player is teleporting to is a SURVIVAL world.
	 *
	 * @param world the world to check
	 * @return true if the world is a SURVIVAL world, otherwise false
	 */
	public boolean checkSurvival(World world) {
		boolean bool = false;
		switch (plugin.getWorldManager()) {
			case MULTIVERSE -> bool = plugin.getMVHelper().isWorldSurvival(world);
			case NONE -> bool = Objects.requireNonNull(plugin.getPlanetsConfig().getString("planets." + world.getName() + ".gamemode")).equalsIgnoreCase("SURVIVAL");
		}
		return bool;
	}

	/**
	 * A method to transport player pets (tamed mobs) into and out of the tardis.
	 *
	 * @param pets   a list of the player's pets found nearby
	 * @param l      the location to teleport pets to
	 * @param player the player who owns the pets
	 * @param d      the direction of the police box
	 * @param enter  whether the pets are entering (true) or exiting (false)
	 */
	void movePets(List<TARDISPet> pets, Location l, Player player, COMPASS d, boolean enter) {
		Location pl = l.clone();
		World w = l.getWorld();
		// will need to adjust this depending on direction Police Box is facing
		if (enter) {
			pl.setZ(l.getZ() + 1);
		} else {
			switch (d) {
				case NORTH -> {
					pl.setX(l.getX() + 1);
					pl.setZ(l.getZ() + 1);
				}
				case WEST -> {
					pl.setX(l.getX() + 1);
					pl.setZ(l.getZ() - 1);
				}
				case SOUTH -> {
					pl.setX(l.getX() - 1);
					pl.setZ(l.getZ() - 1);
				}
				default -> {
					pl.setX(l.getX() - 1);
					pl.setZ(l.getZ() + 1);
				}
			}
		}
		for (TARDISPet pet : pets) {
			plugin.setTardisSpawn(true);
			assert w != null;
			LivingEntity ent = (LivingEntity) w.spawnEntity(pl, pet.getType());
			if (ent.isDead()) {
				ent.remove();
				plugin.debug("Entity is dead! Spawning again...");
				ent = (LivingEntity) w.spawnEntity(pl, pet.getType());
			}
			String pet_name = pet.getName();
			if (pet_name != null && !pet_name.isEmpty()) {
				ent.setCustomName(pet.getName());
			}
			ent.setHealth(pet.getHealth());
			((Tameable) ent).setTamed(true);
			((Tameable) ent).setOwner(player);
			switch (pet.getType()) {
				case WOLF:
					Wolf wolf = (Wolf) ent;
					wolf.setCollarColor(pet.getColour());
					wolf.setSitting(pet.getSitting());
					wolf.setAge(pet.getAge());
					if (pet.isBaby()) {
						wolf.setBaby();
					}
					break;
				case CAT:
					Cat cat = (Cat) ent;
					cat.setCollarColor(pet.getColour());
					cat.setCatType(pet.getCatType());
					cat.setSitting(pet.getSitting());
					cat.setAge(pet.getAge());
					if (pet.isBaby()) {
						cat.setBaby();
					}
					break;
				case PARROT:
					Parrot parrot = (Parrot) ent;
					parrot.setSitting(pet.getSitting());
					parrot.setAge(pet.getAge());
					if (pet.isBaby()) {
						parrot.setBaby();
					}
					parrot.setVariant(pet.getVariant());
					if (pet.isOnLeftShoulder()) {
						player.setShoulderEntityLeft(parrot);
					}
					if (pet.isOnRightShoulder()) {
						player.setShoulderEntityRight(parrot);
					}
					break;
				default:
					break;
			}
		}
		pets.clear();
	}

	/**
	 * A method to give the tardis key to a player if the server is using a multi-inventory plugin.
	 *
	 * @param player the player to give the key to
	 */
	private void giveKey(Player player) {
		if (plugin.getConfig().getBoolean("travel.give_key")) {
			String key;
			ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
			if (rsp.resultSet()) {
				key = (!rsp.getKey().isEmpty()) ? rsp.getKey() : plugin.getConfig().getString("preferences.key");
			} else {
				key = plugin.getConfig().getString("preferences.key");
			}
			assert key != null;
			if (!key.equals("AIR")) {
				PlayerInventory inv = player.getInventory();
				Material m = Material.valueOf(key);
				ItemStack oh = inv.getItemInOffHand();
				if (!inv.contains(m) && !oh.getType().equals(m)) {
					ItemStack is = new ItemStack(m, 1);
					TARDISItemRenamer ir = new TARDISItemRenamer(plugin, player, is);
					ir.setName("tardis Key", true);
					inv.addItem(is);
					player.updateInventory();
					TARDISMessage.send(player, "KEY_REMIND");
				}
			}
		}
	}

	/**
	 * Adjusts the direction the player is facing after a teleport.
	 *
	 * @param d1 the direction the first door is facing
	 * @param d2 the direction the second door is facing
	 * @return the angle needed to correct the yaw
	 */
	float adjustYaw(COMPASS d1, COMPASS d2) {
		return switch (d1) {
			case EAST -> adjustYaw[0][d2.ordinal()];
			case SOUTH -> adjustYaw[1][d2.ordinal()];
			case WEST -> adjustYaw[2][d2.ordinal()];
			default -> adjustYaw[3][d2.ordinal()];
		};
	}

	/**
	 * Get door location data for teleport entry and exit of the tardis.
	 *
	 * @param doorType a reference to the door_type field in the doors table
	 * @param id       the unique tardis identifier i the database
	 * @return an instance of the TARDISDoorLocation data class
	 */
	public TARDISDoorLocation getDoor(int doorType, int id) {
		TARDISDoorLocation tdl = new TARDISDoorLocation();
		// get door location
		HashMap<String, Object> wherei = new HashMap<>();
		wherei.put("door_type", doorType);
		wherei.put("tardis_id", id);
		ResultSetDoors rsd = new ResultSetDoors(plugin, wherei, false);
		if (rsd.resultSet()) {
			COMPASS d = rsd.getDoorDirection();
			tdl.setD(d);
			String doorLocStr = rsd.getDoorLocation();
			World cw = TARDISStaticLocationGetters.getWorld(doorLocStr);
			tdl.setW(cw);
			Location tmp_loc = TARDISStaticLocationGetters.getLocationFromDB(doorLocStr);
			assert tmp_loc != null;
			int getx = tmp_loc.getBlockX();
			int getz = tmp_loc.getBlockZ();
			switch (d) {
				case NORTH -> {
					// z -ve
					tmp_loc.setX(getx + 0.5);
					tmp_loc.setZ(getz - 0.5);
				}
				case EAST -> {
					// x +ve
					tmp_loc.setX(getx + 1.5);
					tmp_loc.setZ(getz + 0.5);
				}
				case SOUTH -> {
					// z +ve
					tmp_loc.setX(getx + 0.5);
					tmp_loc.setZ(getz + 1.5);
				}
				case WEST -> {
					// x -ve
					tmp_loc.setX(getx - 0.5);
					tmp_loc.setZ(getz + 0.5);
				}
			}
			tdl.setL(tmp_loc);
		}
		return tdl;
	}

	/**
	 * Plays a door sound when the iron door is clicked.
	 *
	 * @param p     a player to play the sound for
	 * @param sound the sound to play
	 * @param l     a location to play the sound at
	 * @param m     whether to play the tardis sound or a Minecraft substitute
	 */
	private void playDoorSound(Player p, int sound, Location l, boolean m) {
		switch (sound) {
			case 1:
				if (!m) {
					TARDISSounds.playTARDISSound(l, "tardis_door_open");
				} else {
					p.playSound(p.getLocation(), Sound.BLOCK_IRON_DOOR_OPEN, 1.0F, 1.0F);
				}
				break;
			case 2:
				if (!m) {
					TARDISSounds.playTARDISSound(l, "tardis_door_close");
				} else {
					p.playSound(p.getLocation(), Sound.BLOCK_IRON_DOOR_OPEN, 1.0F, 1.0F);
				}
				break;
			case 3:
				if (!m) {
					TARDISSounds.playTARDISSound(l, "tardis_enter");
				} else {
					p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
				}
				break;
			default:
				break;
		}
	}

	/**
	 * Set a player's time relative to the server time. Based on Essentials /ptime command.
	 *
	 * @param p the player to set the time for
	 * @param t the ticks to set the time to
	 */
	private void setTemporalLocation(Player p, long t) {
		if (p.isOnline()) {
			if (t != -1) {
				long time = p.getPlayerTime();
				time -= time % 24000L;
				time += 24000L + t;
				long calculatedtime = time - p.getWorld().getTime();
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
					p.setPlayerTime(calculatedtime, true);
					if (plugin.getConfig().getBoolean("allow.perception_filter")) {
						plugin.getFilter().addPerceptionFilter(p);
					}
					plugin.getTrackerKeeper().getTemporallyLocated().add(p.getUniqueId());
				}, 10L);
			} else {
				p.resetPlayerTime();
				boolean remove = true;
				Material m = Material.valueOf(plugin.getRecipesConfig().getString("shaped.Perception Filter.result"));
				for (ItemStack is : p.getInventory().getArmorContents()) {
					if (is != null && is.getType().equals(m)) {
						remove = false;
					}
				}
				if (remove && plugin.getTrackerKeeper().getTemporallyLocated().contains(p.getUniqueId())) {
					if (plugin.getConfig().getBoolean("allow.perception_filter")) {
						plugin.getFilter().removePerceptionFilter(p);
					}
					plugin.getTrackerKeeper().getTemporallyLocated().remove(p.getUniqueId());
				}
			}
		}
	}

	/**
	 * Remove player from the travellers table.
	 *
	 * @param u the UUID of the player to remove
	 */
	void removeTraveller(UUID u) {
		HashMap<String, Object> where = new HashMap<>();
		where.put("uuid", u.toString());
		plugin.getQueryFactory().doSyncDelete("travellers", where);
	}
}
