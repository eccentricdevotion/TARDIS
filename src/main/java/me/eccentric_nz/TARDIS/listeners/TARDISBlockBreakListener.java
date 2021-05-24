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

import me.eccentric_nz.tardis.TARDISBuilderInstanceKeeper;
import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.blueprints.TARDISPermission;
import me.eccentric_nz.tardis.database.resultset.ResultSetBlocks;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.planets.TARDISAliasResolver;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * The Silurians, also known as Earth Reptiles, Eocenes, Homo reptilia and Psionosauropodomorpha, are a species of Earth
 * reptile. Technologically advanced, they live alongside their aquatic cousins, the Sea Devils.
 *
 * @author eccentric_nz
 */
public class TARDISBlockBreakListener implements Listener {

	private final TARDISPlugin plugin;

	public TARDISBlockBreakListener(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Listens for the tardis Police Box sign being broken. If the sign is broken, then the tardis is destroyed, the
	 * database records removed and the tardis world deleted.
	 *
	 * @param event a player breaking a block
	 */
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onSignBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (player.getGameMode().equals(GameMode.CREATIVE)) {
			// prevent tardis block breakage
			Block b = event.getBlock();
			String l = b.getLocation().toString();
			HashMap<String, Object> where = new HashMap<>();
			where.put("location", l);
			where.put("police_box", 1);
			ResultSetBlocks rsb = new ResultSetBlocks(plugin, where, false);
			if (rsb.resultSet()) {
				TARDISMessage.send(player, "TARDIS_BREAK");
				event.setCancelled(true);
				return;
			}
		}
		if (plugin.getTrackerKeeper().getZeroRoomOccupants().contains(player.getUniqueId())) {
			event.setCancelled(true);
			TARDISMessage.send(player, "NOT_IN_ZERO");
			return;
		}
		Block block = event.getBlock();
		Material blockType = block.getType();
		if (player.getGameMode().equals(GameMode.CREATIVE) && TARDISBuilderInstanceKeeper.getPrecious().contains(blockType)) {
			HashMap<String, Object> where = new HashMap<>();
			where.put("uuid", player.getUniqueId().toString());
			ResultSetTravellers rs = new ResultSetTravellers(plugin, where, false);
			if (rs.resultSet()) {
				// remove protection
				HashMap<String, Object> wherep = new HashMap<>();
				String loc = block.getLocation().toString();
				wherep.put("location", loc);
				wherep.put("police_box", 0);
				plugin.getQueryFactory().doDelete("blocks", wherep);
				plugin.getGeneralKeeper().getProtectBlockMap().remove(loc);
			}
			return;
		}
		if (Tag.WALL_SIGNS.isTagged(blockType)) {
			// check the text on the sign
			Sign sign = (Sign) block.getState();
			String line0 = ChatColor.stripColor(sign.getLine(0));
			String line1 = ChatColor.stripColor(sign.getLine(1));
			String line2 = ChatColor.stripColor(sign.getLine(2));
			if (isPresetSign(line0, line1, line2)) {
				event.setCancelled(true);
				sign.update();
				if (TARDISPermission.hasPermission(player, "tardis.exterminate")) {
					UUID uuid = player.getUniqueId();
					// check it is their tardis
					plugin.getTrackerKeeper().getExterminate().put(uuid, block);
					long timeout = plugin.getConfig().getLong("police_box.confirm_timeout");
					TARDISMessage.send(player, "Q_DELETE", ChatColor.AQUA + "/tardis exterminate" + ChatColor.RESET, String.format("%d", timeout));
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getTrackerKeeper().getExterminate().remove(uuid), timeout * 20);
				} else {
					TARDISMessage.send(player, "NO_PERM_DELETE");
				}
			}
		}
		if (blockType == Material.BEACON) {
			Location loc = event.getBlock().getLocation();
			if (Objects.requireNonNull(loc.getWorld()).getName().startsWith("tardis")) {
				return;
			}
			String b = loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
			// check if it is a rift manipulator
			for (String r : Objects.requireNonNull(plugin.getConfig().getConfigurationSection("rechargers")).getKeys(false)) {
				if (r.startsWith("rift")) {
					// get the location
					World w = TARDISAliasResolver.getWorldFromAlias(plugin.getConfig().getString("rechargers." + r + ".world"));
					int x = plugin.getConfig().getInt("rechargers." + r + ".x");
					int y = plugin.getConfig().getInt("rechargers." + r + ".y");
					int z = plugin.getConfig().getInt("rechargers." + r + ".z");
					assert w != null;
					String l = w.getName() + "," + x + "," + y + "," + z;
					if (l.equals(b)) {
						if (Objects.equals(plugin.getConfig().getString("rechargers." + r + ".uuid"), player.getUniqueId().toString())) {
							plugin.getConfig().set("rechargers." + r, null);
							TARDISMessage.send(player, "RIFT_REMOVED");
							event.setCancelled(true);
							// drop Rift Manipulator
							event.getBlock().setBlockData(TARDISConstants.AIR);
							ItemStack rm = new ItemStack(Material.BEACON, 1);
							ItemMeta im = rm.getItemMeta();
							assert im != null;
							im.setDisplayName("Rift Manipulator");
							rm.setItemMeta(im);
							w.dropItem(loc, rm);
						} else {
							event.setCancelled(true);
							TARDISMessage.send(player, "RIFT_PLAYER");
						}
						break;
					}
				}
			}
		}
	}

	private boolean isPresetSign(String l0, String l1, String l2) {
		if (l0.equalsIgnoreCase("WEEPING") || l0.equalsIgnoreCase("$50,000")) {
			return (plugin.getGeneralKeeper().getSign_lookup().containsKey(l0) && l1.equals(plugin.getGeneralKeeper().getSign_lookup().get(l0)));
		} else {
			return (plugin.getGeneralKeeper().getSign_lookup().containsKey(l1) && l2.equals(plugin.getGeneralKeeper().getSign_lookup().get(l1)));
		}
	}
}
