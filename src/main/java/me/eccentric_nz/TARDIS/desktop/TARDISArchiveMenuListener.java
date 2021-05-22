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
package me.eccentric_nz.tardis.desktop;

import me.eccentric_nz.tardis.TARDIS;
import me.eccentric_nz.tardis.control.TARDISThemeButton;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.enumeration.ConsoleSize;
import me.eccentric_nz.tardis.enumeration.Consoles;
import me.eccentric_nz.tardis.enumeration.Schematic;
import me.eccentric_nz.tardis.listeners.TARDISMenuListener;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.schematic.ArchiveUpdate;
import me.eccentric_nz.tardis.utility.TARDISNumberParsers;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * A control room's look could be changed over time. The process by which an operator could transform a control room was
 * fairly simple, once compared by the Fifth Doctor to changing a "desktop theme".
 *
 * @author eccentric_nz
 */
public class TARDISArchiveMenuListener extends TARDISMenuListener implements Listener {

	private final TARDIS plugin;

	public TARDISArchiveMenuListener(TARDIS plugin) {
		super(plugin);
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true)
	public void onThemeMenuClick(InventoryClickEvent event) {
		InventoryView view = event.getView();
		String name = view.getTitle();
		if (name.equals(ChatColor.DARK_RED + "tardis Archive")) {
			Player p = (Player) event.getWhoClicked();
			int slot = event.getRawSlot();
			if (slot >= 0 && slot < 27) {
				event.setCancelled(true);
				switch (slot) {
					case 17 -> {
						// back
						HashMap<String, Object> where = new HashMap<>();
						where.put("uuid", p.getUniqueId().toString());
						ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
						rs.resultSet();
						Tardis tardis = rs.getTardis();
						// return to Desktop Theme GUI
						close(p);
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TARDISThemeButton(plugin, p, tardis.getSchematic(), tardis.getArtron_level(), tardis.getTardis_id()).clickButton(), 2L);
					}
					case 18 -> {
						// size
						ItemStack iss = view.getItem(18);
						assert iss != null;
						ItemMeta ims = iss.getItemMeta();
						assert ims != null;
						List<String> lores = ims.getLore();
						String t;
						String b;
						int s;
						assert lores != null;
						int o = ConsoleSize.valueOf(lores.get(0)).ordinal();
						s = (o < 2) ? o + 1 : 0;
						t = ConsoleSize.values()[s].toString();
						b = ConsoleSize.values()[s].getBlocks();
						if (t != null) {
							ims.setLore(Arrays.asList(t, b, ChatColor.AQUA + "Click to change"));
							iss.setItemMeta(ims);
						}
					}
					case 19 ->
							// scan
							scan(p, view);
					case 20 ->
							// archive
							archive(p, view);
					case 22, 23, 24 -> {
						ItemStack template = view.getItem(slot);
						if (template != null) {
							UUID uuid = p.getUniqueId();
							TARDISUpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(uuid);
							ItemMeta im = template.getItemMeta();
							assert im != null;
							String size = im.getDisplayName().toLowerCase(Locale.ENGLISH);
							int upgrade = plugin.getArtronConfig().getInt("upgrades.template." + size);
							if (tud.getLevel() >= upgrade) {
								new ArchiveUpdate(plugin, uuid.toString(), im.getDisplayName()).setInUse();
								tud.setSchematic(Consoles.schematicFor(size));
								tud.setWall("ORANGE_WOOL");
								tud.setFloor("LIGHT_GRAY_WOOL");
								plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
									plugin.getTrackerKeeper().getUpgrades().put(uuid, tud);
									// process upgrade
									new TARDISThemeProcessor(plugin, uuid).changeDesktop();
								}, 10L);
								close(p);
							}
						}
					}
					case 26 ->
							// close
							close(p);
					default -> {
						// get Display name of selected archive
						ItemStack choice = view.getItem(slot);
						if (choice != null) {
							// remember the upgrade choice
							Schematic schm = Consoles.schematicFor("archive");
							UUID uuid = p.getUniqueId();
							TARDISUpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(uuid);
							ItemMeta im = choice.getItemMeta();
							assert im != null;
							List<String> lore = im.getLore();
							assert lore != null;
							if (lore.contains(ChatColor.GREEN + plugin.getLanguage().getString("CURRENT_CONSOLE"))) {
								TARDISMessage.send(p, "ARCHIVE_NOT_CURRENT");
								return;
							}
							int upgrade = plugin.getArtronConfig().getInt("upgrades.archive.tall");
							for (String l : lore) {
								if (l.startsWith("Cost")) {
									upgrade = TARDISNumberParsers.parseInt(l.replace("Cost: ", ""));
								}
							}
							if (tud.getLevel() >= upgrade) {
								new ArchiveUpdate(plugin, uuid.toString(), im.getDisplayName()).setInUse();
								tud.setSchematic(schm);
								tud.setWall("ORANGE_WOOL");
								tud.setFloor("LIGHT_GRAY_WOOL");
								plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
									plugin.getTrackerKeeper().getUpgrades().put(uuid, tud);
									// process upgrade
									new TARDISThemeProcessor(plugin, uuid).changeDesktop();
								}, 10L);
								close(p);
							}
						}
					}
				}
			} else {
				ClickType click = event.getClick();
				if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
					event.setCancelled(true);
				}
			}
		}
	}

	/**
	 * Closes the inventory.
	 *
	 * @param p the player using the GUI
	 */
	@Override
	public void close(Player p) {
		plugin.getTrackerKeeper().getUpgrades().remove(p.getUniqueId());
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, p::closeInventory, 1L);
	}

	/**
	 * Closes the inventory and scans the current console.
	 *
	 * @param p the player using the GUI
	 */
	private void scan(Player p, InventoryView view) {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			List<String> lore = getSizeLore(view);
			String size = lore.get(0);
			p.closeInventory();
			p.performCommand("tardis archive scan " + size);
		}, 1L);
	}

	/**
	 * Closes the inventory and archives the current console. A random name will be generated.
	 *
	 * @param p the player using the GUI
	 */
	private void archive(Player p, InventoryView view) {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			List<String> lore = getSizeLore(view);
			String size = lore.get(0);
			p.closeInventory();
			// generate random name
			String name = TARDISRandomArchiveName.getRandomName();
			p.performCommand("tardis archive add " + name + " " + size);
		}, 1L);
	}

	private List<String> getSizeLore(InventoryView view) {
		ItemStack is = view.getItem(18);
		assert is != null;
		ItemMeta im = is.getItemMeta();
		assert im != null;
		return im.getLore();
	}
}
