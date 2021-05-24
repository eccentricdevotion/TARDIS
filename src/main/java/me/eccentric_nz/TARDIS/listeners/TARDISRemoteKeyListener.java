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
package me.eccentric_nz.tardis.listeners;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.artron.TARDISPoliceBoxLampToggler;
import me.eccentric_nz.tardis.commands.tardis.TARDISHideCommand;
import me.eccentric_nz.tardis.commands.tardis.TARDISRebuildCommand;
import me.eccentric_nz.tardis.database.data.TARDIS;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetDoors;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.enumeration.PRESET;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.move.TARDISDoorToggler;
import me.eccentric_nz.tardis.utility.TARDISSounds;
import me.eccentric_nz.tardis.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.tardis.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;

/**
 * At one point, the Tenth Doctor installed a system that allowed him to lock the tardis remotely using a fob. As a
 * joke, the tardis roof light flashed and a alarm chirp sound was heard, similar to that used on vehicles on Earth.
 *
 * @author eccentric_nz
 */
public class TARDISRemoteKeyListener implements Listener {

	private final TARDISPlugin plugin;
	private final Material rkey;

	public TARDISRemoteKeyListener(TARDISPlugin plugin) {
		this.plugin = plugin;
		rkey = Material.valueOf(this.plugin.getRecipesConfig().getString("shaped.tardis Remote Key.result"));
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInteract(PlayerInteractEvent event) {
		if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
			return;
		}
		Action action = event.getAction();
		Player player = event.getPlayer();
		if ((!action.equals(Action.LEFT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_AIR)) || player.isSneaking()) {
			return;
		}
		// check item in hand
		ItemStack is = player.getInventory().getItemInMainHand();
		if (!is.getType().equals(rkey)) {
			return;
		}
		if (is.hasItemMeta() && Objects.requireNonNull(is.getItemMeta()).hasDisplayName() && is.getItemMeta().getDisplayName().equals("tardis Remote Key")) {
			String uuid = player.getUniqueId().toString();
			// has tardis?
			HashMap<String, Object> where = new HashMap<>();
			where.put("uuid", uuid);
			ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
			if (!rs.resultSet()) {
				return;
			}
			TARDIS tardis = rs.getTardis();
			int id = tardis.getTardisId();
			boolean powered = tardis.isPowered();
			PRESET preset = tardis.getPreset();
			if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
				TARDISMessage.send(player, "SIEGE_NO_CONTROL");
				return;
			}
			if (plugin.getTrackerKeeper().getDispersedTARDII().contains(id)) {
				TARDISMessage.send(player, "NOT_WHILE_DISPERSED");
				return;
			}
			boolean hidden = tardis.isHidden();
			if (action.equals(Action.LEFT_CLICK_AIR)) {
				// get the tardis current location
				HashMap<String, Object> wherec = new HashMap<>();
				wherec.put("tardis_id", id);
				ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
				if (!rsc.resultSet()) {
					return;
				}
				Location l = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
				HashMap<String, Object> whered = new HashMap<>();
				whered.put("tardis_id", id);
				ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
				if (rsd.resultSet()) {
					// toggle door lock
					int locked = (rsd.isLocked()) ? 0 : 1;
					HashMap<String, Object> setl = new HashMap<>();
					setl.put("locked", locked);
					HashMap<String, Object> wherel = new HashMap<>();
					wherel.put("tardis_id", id);
					// always lock / unlock both doors
					plugin.getQueryFactory().doUpdate("doors", setl, wherel);
					String message = (rsd.isLocked()) ? plugin.getLanguage().getString("DOOR_UNLOCK") : plugin.getLanguage().getString("DOOR_DEADLOCK");
					TARDISMessage.send(player, "DOOR_LOCK", message);
					TARDISPoliceBoxLampToggler tpblt = new TARDISPoliceBoxLampToggler(plugin);
					TARDISSounds.playTARDISSound(l, "tardis_lock");
					tpblt.toggleLamp(id, !powered);
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> tpblt.toggleLamp(id, powered), 6L);
				}
			} else if (preset.equals(PRESET.INVISIBLE)) {
				HashMap<String, Object> whered = new HashMap<>();
				whered.put("tardis_id", id);
				whered.put("door_type", 1);
				ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
				if (rsd.resultSet()) {
					// get inner door block
					Block block = Objects.requireNonNull(TARDISStaticLocationGetters.getLocationFromDB(rsd.getDoorLocation())).getBlock();
					boolean open = TARDISStaticUtils.isDoorOpen(block);
					// toggle door / portals
					new TARDISDoorToggler(plugin, block, player, false, open, id).toggleDoors();
					String message = (open) ? "DOOR_CLOSED" : "DOOR_OPENED";
					TARDISMessage.send(player, message);
				}
			} else {
				if (plugin.getTrackerKeeper().getRebuildCooldown().containsKey(player.getUniqueId())) {
					long now = System.currentTimeMillis();
					long cooldown = plugin.getConfig().getLong("police_box.rebuild_cooldown");
					long then = plugin.getTrackerKeeper().getRebuildCooldown().get(player.getUniqueId()) + cooldown;
					if (now < then) {
						TARDISMessage.send(player.getPlayer(), "COOLDOWN", String.format("%d", cooldown / 1000));
						return;
					}
				}
				if (!powered) {
					TARDISMessage.send(player, "POWER_DOWN");
					return;
				}
				if (hidden) {
					// rebuild
					new TARDISRebuildCommand(plugin).rebuildPreset(player);
				} else {
					// hide
					new TARDISHideCommand(plugin).hide(player);
				}
			}
		}
	}
}
