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
package me.eccentric_nz.tardis.listeners;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.advancement.TARDISBook;
import me.eccentric_nz.tardis.arch.TARDISArchPersister;
import me.eccentric_nz.tardis.blueprints.TARDISPermission;
import me.eccentric_nz.tardis.database.data.TARDIS;
import me.eccentric_nz.tardis.database.resultset.*;
import me.eccentric_nz.tardis.enumeration.Difficulty;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.utility.TARDISResourcePackChanger;
import me.eccentric_nz.tardis.utility.TARDISStaticLocationGetters;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;

/**
 * Tylos was a member of Varsh's group of Outlers on Alzarius. When Adric asked to join them, Tylos challenged him to
 * prove his worth by stealing some riverfruit.
 *
 * @author eccentric_nz
 */
public class TARDISJoinListener implements Listener {

	private final TARDISPlugin plugin;

	public TARDISJoinListener(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Listens for a player joining the server. If the player has tardis permissions (ie not a guest), then check
	 * whether they have achieved the building of a tardis. If not then insert an advancement record and give them the
	 * tardis book.
	 *
	 * @param event a player joining the server
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		if (plugin.getKitsConfig().getBoolean("give.join.enabled")) {
			if (TARDISPermission.hasPermission(player, "tardis.kit.join")) {
				// check if they have the tardis kit
				HashMap<String, Object> where = new HashMap<>();
				where.put("uuid", uuid);
				where.put("name", "joinkit");
				ResultSetAdvancements rsa = new ResultSetAdvancements(plugin, where, false);
				if (!rsa.resultSet()) {
					//add a record
					HashMap<String, Object> set = new HashMap<>();
					set.put("uuid", uuid);
					set.put("name", "joinkit");
					plugin.getQueryFactory().doInsert("achievements", set);
					// give the join kit
					String kit = plugin.getKitsConfig().getString("give.join.kit");
					plugin.getServer().dispatchCommand(plugin.getConsole(), "tardisgive " + player.getName() + " kit " + kit);
				}
			}
		}
		if (plugin.getConfig().getBoolean("allow.achievements")) {
			if (TARDISPermission.hasPermission(player, "tardis.book")) {
				// check if they have started building a tardis yet
				HashMap<String, Object> where = new HashMap<>();
				where.put("uuid", uuid);
				where.put("name", "tardis");
				ResultSetAdvancements rsa = new ResultSetAdvancements(plugin, where, false);
				if (!rsa.resultSet()) {
					//add a record
					HashMap<String, Object> set = new HashMap<>();
					set.put("uuid", uuid);
					set.put("name", "tardis");
					plugin.getQueryFactory().doInsert("achievements", set);
					TARDISBook book = new TARDISBook(plugin);
					// title, author, filename, player
					book.writeBook("Get transport", "Rassilon", "tardis", player);
				}
			}
		}
		if (!plugin.getDifficulty().equals(Difficulty.EASY) && ((plugin.getConfig().getBoolean("allow.player_difficulty") && TARDISPermission.hasPermission(player, "tardis.difficulty")) || (plugin.getConfig().getInt("travel.grace_period") > 0 && TARDISPermission.hasPermission(player, "tardis.create")))) {
			// check if they have t_count record - create one if not
			ResultSetCount rsc = new ResultSetCount(plugin, uuid);
			if (!rsc.resultSet()) {
				HashMap<String, Object> setc = new HashMap<>();
				setc.put("uuid", uuid);
				setc.put("player", player.getName());
				plugin.getQueryFactory().doInsert("t_count", setc);
			}
		}
		if (plugin.getConfig().getBoolean("allow.tp_switch") && TARDISPermission.hasPermission(player, "tardis.texture")) {
			// are they in the tardis?
			HashMap<String, Object> where = new HashMap<>();
			where.put("uuid", uuid);
			ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
			if (rst.resultSet()) {
				// is texture switching on?
				ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
				if (rsp.resultSet()) {
					if (rsp.isTextureOn()) {
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TARDISResourcePackChanger(plugin).changeRP(player, rsp.getTextureIn()), 50L);
					}
				}
			}
		}
		// load and remember the players Police Box chunk
		HashMap<String, Object> wherep = new HashMap<>();
		wherep.put("uuid", player.getUniqueId().toString());
		ResultSetTardis rs = new ResultSetTardis(plugin, wherep, "", false, 0);
		if (rs.resultSet()) {
			TARDIS tardis = rs.getTardis();
			int id = tardis.getTardisId();
			String owner = tardis.getOwner();
			String lastKnownName = tardis.getLastKnownName();
			HashMap<String, Object> wherecl = new HashMap<>();
			wherecl.put("tardis_id", id);
			ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
			if (rsc.resultSet()) {
				World w = rsc.getWorld();
				if (w != null) {
					Chunk chunk = w.getChunkAt(new Location(w, rsc.getX(), rsc.getY(), rsc.getZ()));
					while (!chunk.isLoaded()) {
						chunk.load();
					}
					chunk.setForceLoaded(true);
				}
			}
			long now;
			if (TARDISPermission.hasPermission(player, "tardis.prune.bypass")) {
				now = Long.MAX_VALUE;
			} else {
				now = System.currentTimeMillis();
			}
			HashMap<String, Object> set = new HashMap<>();
			set.put("last_use", now);
			set.put("monsters", 0);
			if (!lastKnownName.equals(player.getName())) {
				// update the player's name WG region as it may have changed
				if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
					World cw = TARDISStaticLocationGetters.getWorld(tardis.getChunk());
					// tardis region
					plugin.getWorldGuardUtils().updateRegionForNameChange(cw, owner, player.getUniqueId(), "tardis");
				}
				set.put("last_known_name", player.getName());
			}
			HashMap<String, Object> wherel = new HashMap<>();
			wherel.put("tardis_id", id);
			plugin.getQueryFactory().doUpdate("tardis", set, wherel);
		}
		// re-arch the player
		if (plugin.isDisguisesOnServer() && plugin.getConfig().getBoolean("arch.enabled")) {
			new TARDISArchPersister(plugin).reArch(player.getUniqueId());
		}
		// add to perception filter team
		if (plugin.getConfig().getBoolean("allow.perception_filter")) {
			plugin.getFilter().addPlayer(player);
		}
		// add to zero room occupants
		if (plugin.getConfig().getBoolean("allow.zero_room")) {
			if (player.getLocation().getWorld().getName().equals("TARDIS_Zero_Room")) {
				plugin.getTrackerKeeper().getZeroRoomOccupants().add(player.getUniqueId());
			}
		}
		// notify updates
		if (plugin.getConfig().getBoolean("preferences.notify_update") && plugin.isUpdateFound() && player.isOp()) {
			TARDISMessage.message(player, String.format(TARDISMessage.JENKINS_UPDATE_READY, plugin.getBuildNumber(), plugin.getUpdateNumber()));
		}
	}
}
