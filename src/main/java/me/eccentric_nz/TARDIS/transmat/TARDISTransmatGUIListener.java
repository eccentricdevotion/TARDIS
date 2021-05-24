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
package me.eccentric_nz.tardis.transmat;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetTransmat;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.listeners.TARDISMenuListener;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

public class TARDISTransmatGUIListener extends TARDISMenuListener implements Listener {

	private final TARDISPlugin plugin;
	private final HashMap<UUID, String> selectedLocation = new HashMap<>();

	public TARDISTransmatGUIListener(TARDISPlugin plugin) {
		super(plugin);
		this.plugin = plugin;
	}

	/**
	 * Listens for player clicking inside an inventory. If the inventory is a tardis GUI, then the click is processed
	 * accordingly.
	 *
	 * @param event a player clicking an inventory slot
	 */
	@EventHandler(ignoreCancelled = true)
	public void onTransmatMenuClick(InventoryClickEvent event) {
		InventoryView view = event.getView();
		String name = view.getTitle();
		if (name.equals(ChatColor.DARK_RED + "tardis transmats")) {
			event.setCancelled(true);
			int slot = event.getRawSlot();
			Player player = (Player) event.getWhoClicked();
			if (slot >= 0 && slot < 54) {
				ItemStack is = view.getItem(slot);
				if (is != null) {
					// get the tardis the player is in
					HashMap<String, Object> wheres = new HashMap<>();
					wheres.put("uuid", player.getUniqueId().toString());
					ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
					if (rst.resultSet()) {
						int id = rst.getTardisId();
						switch (slot) {
							case 17:
								if (!selectedLocation.containsKey(player.getUniqueId())) {
									TARDISMessage.send(player, "TRANSMAT_SELECT");
								} else {
									// transmat to selected location
									ResultSetTransmat rsm = new ResultSetTransmat(plugin, id, selectedLocation.get(player.getUniqueId()));
									if (rsm.resultSet()) {
										TARDISMessage.send(player, "TRANSMAT");
										Location tp_loc = rsm.getLocation();
										tp_loc.setYaw(rsm.getYaw());
										tp_loc.setPitch(player.getLocation().getPitch());
										plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
											player.playSound(tp_loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
											player.teleport(tp_loc);
										}, 10L);
										close(player);
									}
								}
								break;
							case 35:
								if (!selectedLocation.containsKey(player.getUniqueId())) {
									TARDISMessage.send(player, "TRANSMAT_SELECT");
								} else {
									// delete
									ResultSetTransmat rsm = new ResultSetTransmat(plugin, id, selectedLocation.get(player.getUniqueId()));
									if (rsm.resultSet()) {
										HashMap<String, Object> wherer = new HashMap<>();
										wherer.put("transmat_id", rsm.getTransmatId());
										plugin.getQueryFactory().doDelete("transmats", wherer);
										TARDISMessage.send(player, "TRANSMAT_REMOVED");
										close(player);
									}
								}
								break;
							case 53:
								selectedLocation.remove(player.getUniqueId());
								//close
								close(player);
								break;
							default:
								// select
								ItemMeta im = is.getItemMeta();
								selectedLocation.put(player.getUniqueId(), im.getDisplayName());
								break;
						}
					}
				}
			}
		}
	}
}
