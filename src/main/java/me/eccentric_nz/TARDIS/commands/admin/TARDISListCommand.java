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
package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.messaging.TableGenerator;
import me.eccentric_nz.TARDIS.messaging.TableGenerator.Alignment;
import me.eccentric_nz.TARDIS.messaging.TableGenerator.Receiver;
import me.eccentric_nz.TARDIS.messaging.TableGeneratorCustomFont;
import me.eccentric_nz.TARDIS.messaging.TableGeneratorSmallChar;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TARDISListCommand {

	private final TARDIS plugin;

	TARDISListCommand(TARDIS plugin) {
		this.plugin = plugin;
	}

	boolean listStuff(CommandSender sender, String[] args) {
		if (args.length > 1 && (args[1].equalsIgnoreCase("save") || args[1].equalsIgnoreCase("portals") || args[1].equalsIgnoreCase("abandoned") || args[1].equalsIgnoreCase("preset_perms") || args[1].equalsIgnoreCase("perms") || args[1].equalsIgnoreCase("recipes") || args[1].equalsIgnoreCase("blueprints"))) {
			if (args[1].equalsIgnoreCase("save")) {
				ResultSetTardis rsl = new ResultSetTardis(plugin, new HashMap<>(), "", true, 1);
				if (rsl.resultSet()) {
					String file = plugin.getDataFolder() + File.separator + "TARDIS_list.txt";
					try {
						try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
							for (Tardis t : rsl.getData()) {
								HashMap<String, Object> wherecl = new HashMap<>();
								wherecl.put("tardis_id", t.getTardis_id());
								ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
								if (!rsc.resultSet()) {
									TARDISMessage.send(sender, "CURRENT_NOT_FOUND");
									return true;
								}
								String line = "ID: " + t.getTardis_id() + ", Time Lord: " + t.getOwner() + ", Location: " + rsc.getWorld().getName() + ":" + rsc.getX() + ":" + rsc.getY() + ":" + rsc.getZ();
								bw.write(line);
								bw.newLine();
							}
						}
					} catch (IOException e) {
						plugin.debug("Could not create and write to TARDIS_list.txt! " + e.getMessage());
					}
				}
				TARDISMessage.send(sender, "FILE_SAVED");
				return true;
			} else if (args[1].equalsIgnoreCase("portals")) {
				plugin.getTrackerKeeper().getPortals().forEach((key, value) -> sender.sendMessage("TARDIS id: " + value.getTardisId() + " has a portal open at: " + key.toString()));
				return true;
			} else if (args[1].equalsIgnoreCase("abandoned")) {
				new TARDISAbandonLister(plugin).list(sender);
				return true;
			} else if (args[1].equalsIgnoreCase("perms")) {
				new TARDISPermissionLister(plugin).listPerms(sender);
				return true;
			} else if (args[1].equalsIgnoreCase("recipes")) {
				new TARDISRecipesLister(plugin).listRecipes(sender);
				return true;
			} else if (args[1].equalsIgnoreCase("blueprints")) {
				new TARDISBlueprintsLister().listBlueprints(sender);
				return true;
			} else {
				// preset permissions
				new TARDISPresetPermissionLister().list(sender);
				return true;
			}
		} else {
			// get all tardis positions - max 18
			int start = 0, end = 18;
			if (args.length > 1) {
				int tmp = TARDISNumberParsers.parseInt(args[1]);
				start = (tmp * 18) - 18;
				end = tmp * 18;
			}
			String limit = start + ", " + end;
			ResultSetTardis rsl = new ResultSetTardis(plugin, new HashMap<>(), limit, true, 0);
			if (rsl.resultSet()) {
				TARDISMessage.send(sender, "TARDIS_LOCS");
				TableGenerator tg;
				if (TableGenerator.getSenderPrefs(sender)) {
					tg = new TableGeneratorCustomFont(Alignment.LEFT, Alignment.LEFT, Alignment.LEFT, Alignment.RIGHT, Alignment.RIGHT, Alignment.RIGHT);
				} else {
					tg = new TableGeneratorSmallChar(Alignment.LEFT, Alignment.LEFT, Alignment.LEFT, Alignment.RIGHT, Alignment.RIGHT, Alignment.RIGHT);
				}
				tg.addRow(ChatColor.GOLD + "" + ChatColor.UNDERLINE + "ID", ChatColor.GOLD + "" + ChatColor.UNDERLINE + "Time Lord", ChatColor.GOLD + "" + ChatColor.UNDERLINE + "World", ChatColor.GOLD + "" + ChatColor.UNDERLINE + "X", ChatColor.GOLD + "" + ChatColor.UNDERLINE + "Y", ChatColor.GOLD + "" + ChatColor.UNDERLINE + "Z");
				tg.addRow();
				for (Tardis t : rsl.getData()) {
					HashMap<String, Object> wherecl = new HashMap<>();
					wherecl.put("tardis_id", t.getTardis_id());
					ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
					if (!rsc.resultSet()) {
						TARDISMessage.send(sender, "CURRENT_NOT_FOUND");
						return true;
					}
					tg.addRow("" + t.getTardis_id(), t.getOwner(), rsc.getWorld().getName(), "" + rsc.getX(), "" + rsc.getY(), "" + rsc.getZ());
				}
				for (String line : tg.generate(sender instanceof Player ? Receiver.CLIENT : Receiver.CONSOLE, true, true)) {
					sender.sendMessage(line);
				}
				if (rsl.getData().size() > 18) {
					TARDISMessage.send(sender, "TARDIS_LOCS_INFO");
				}
			} else {
				TARDISMessage.send(sender, "TARDIS_LOCS_NONE");
			}
			return true;
		}
	}
}
