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
package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISPresetListener extends TARDISMenuListener implements Listener {

	private final TARDIS plugin;

	public TARDISPresetListener(TARDIS plugin) {
		super(plugin);
		this.plugin = plugin;
	}

	/**
	 * Listens for player clicking inside an inventory. If the inventory is a TARDIS GUI, then the click is processed
	 * accordingly.
	 *
	 * @param event a player clicking an inventory slot
	 */
	@EventHandler(ignoreCancelled = true)
	public void onChameleonPresetClick(InventoryClickEvent event) {
		InventoryView view = event.getView();
		String name = view.getTitle();
		if (name.equals(ChatColor.DARK_RED + "Chameleon Presets")) {
			event.setCancelled(true);
			int slot = event.getRawSlot();
			Player player = (Player) event.getWhoClicked();
			if (slot >= 0 && slot < 54) {
				ItemStack is = view.getItem(slot);
				if (is != null) {
					// get the TARDIS the player is in
					HashMap<String, Object> wheres = new HashMap<>();
					wheres.put("uuid", player.getUniqueId().toString());
					ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
					if (rst.resultSet()) {
						int id = rst.getTardis_id();
						HashMap<String, Object> where = new HashMap<>();
						where.put("tardis_id", id);
						ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
						if (rs.resultSet()) {
							Tardis tardis = rs.getTardis();
							String chameleon = "";
							// set the Chameleon Circuit sign(s)
							HashMap<String, Object> whereh = new HashMap<>();
							whereh.put("tardis_id", id);
							whereh.put("type", 31);
							ResultSetControls rsc = new ResultSetControls(plugin, whereh, true);
							boolean hasChameleonSign = false;
							if (rsc.resultSet()) {
								hasChameleonSign = true;
								for (HashMap<String, String> map : rsc.getData()) {
									chameleon = map.get("location");
								}
							}
							String last_line = TARDISStaticUtils.getLastLine(chameleon);
							String preset = tardis.getPreset().toString();
							HashMap<String, Object> set = new HashMap<>();
							HashMap<String, Object> wherec = new HashMap<>();
							wherec.put("tardis_id", id);
							TARDISChameleonFrame tcf = new TARDISChameleonFrame(plugin);
							switch (slot) {
								case 0:
									// new Police Box
									if (!last_line.equals("NEW")) {
										set.put("chameleon_preset", "NEW");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "NEW", player);
										tcf.updateChameleonFrame(id, PRESET.NEW);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "New Police Box");
									break;
								case 1:
									// jungle temple
									if (!last_line.equals("JUNGLE")) {
										set.put("chameleon_preset", "JUNGLE");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "JUNGLE", player);
										tcf.updateChameleonFrame(id, PRESET.JUNGLE);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Jungle Temple");
									break;
								case 2:
									// nether fortress
									if (!last_line.equals("NETHER")) {
										set.put("chameleon_preset", "NETHER");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "NETHER", player);
										tcf.updateChameleonFrame(id, PRESET.NETHER);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Nether Fortress");
									break;
								case 3:
									// old police box
									if (!last_line.equals("OLD")) {
										set.put("chameleon_preset", "OLD");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "OLD", player);
										tcf.updateChameleonFrame(id, PRESET.OLD);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Old Police Box");
									break;
								case 4:
									// swamp
									if (!last_line.equals("SWAMP")) {
										set.put("chameleon_preset", "SWAMP");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "SWAMP", player);
										tcf.updateChameleonFrame(id, PRESET.SWAMP);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Swamp Hut");
									break;
								case 5:
									// party tent
									if (!last_line.equals("PARTY")) {
										set.put("chameleon_preset", "PARTY");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "PARTY", player);
										tcf.updateChameleonFrame(id, PRESET.PARTY);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Party Tent");
									break;
								case 6:
									// village house
									if (!last_line.equals("VILLAGE")) {
										set.put("chameleon_preset", "VILLAGE");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "VILLAGE", player);
										tcf.updateChameleonFrame(id, PRESET.VILLAGE);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Village House");
									break;
								case 7:
									// yellow submarine
									if (!last_line.equals("YELLOW")) {
										set.put("chameleon_preset", "YELLOW");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "YELLOW", player);
										tcf.updateChameleonFrame(id, PRESET.YELLOW);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Yellow Submarine");
									break;
								case 8:
									// telephone
									if (!last_line.equals("TELEPHONE")) {
										set.put("chameleon_preset", "TELEPHONE");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "TELEPHONE", player);
										tcf.updateChameleonFrame(id, PRESET.TELEPHONE);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Red Telephone Box");
									break;
								case 9:
									// weeping angel
									if (!last_line.equals("ANGEL")) {
										set.put("chameleon_preset", "ANGEL");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "ANGEL", player);
										tcf.updateChameleonFrame(id, PRESET.ANGEL);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Weeping Angel");
									break;
								case 10:
									// submerged
									if (!last_line.equals("SUBMERGED")) {
										set.put("chameleon_preset", "SUBMERGED");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "SUBMERGED", player);
										tcf.updateChameleonFrame(id, PRESET.SUBMERGED);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Submerged");
									break;
								case 11:
									// flower
									if (!last_line.equals("FLOWER")) {
										set.put("chameleon_preset", "FLOWER");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "FLOWER", player);
										tcf.updateChameleonFrame(id, PRESET.FLOWER);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Daisy Flower");
									break;
								case 12:
									// stone brick column
									if (!last_line.equals("STONE")) {
										set.put("chameleon_preset", "STONE");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "STONE", player);
										tcf.updateChameleonFrame(id, PRESET.STONE);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Stone Brick Column");
									break;
								case 13:
									// chalice
									if (!last_line.equals("CHALICE")) {
										set.put("chameleon_preset", "CHALICE");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "CHALICE", player);
										tcf.updateChameleonFrame(id, PRESET.CHALICE);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Quartz Chalice");
									break;
								case 14:
									// desert temple
									if (!last_line.equals("DESERT")) {
										set.put("chameleon_preset", "DESERT");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "DESERT", player);
										tcf.updateChameleonFrame(id, PRESET.DESERT);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Desert Temple");
									break;
								case 15:
									// mossy well
									if (!last_line.equals("WELL")) {
										set.put("chameleon_preset", "WELL");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "WELL", player);
										tcf.updateChameleonFrame(id, PRESET.WELL);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Mossy Well");
									break;
								case 16:
									// windmill
									if (!last_line.equals("WINDMILL")) {
										set.put("chameleon_preset", "WINDMILL");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "WINDMILL", player);
										tcf.updateChameleonFrame(id, PRESET.WINDMILL);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Windmill");
									break;
								case 17:
									// Rubber Duck
									if (!last_line.equals("DUCK")) {
										set.put("chameleon_preset", "DUCK");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "DUCK", player);
										tcf.updateChameleonFrame(id, PRESET.DUCK);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Rubber Duck");
									break;
								case 18:
									// Mineshaft
									if (!last_line.equals("MINESHAFT")) {
										set.put("chameleon_preset", "MINESHAFT");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "MINESHAFT", player);
										tcf.updateChameleonFrame(id, PRESET.MINESHAFT);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Mineshaft");
									break;
								case 19:
									// Creepy
									if (!last_line.equals("CREEPY")) {
										set.put("chameleon_preset", "CREEPY");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "CREEPY", player);
										tcf.updateChameleonFrame(id, PRESET.CREEPY);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Creepy");
									break;
								case 20:
									// Peanut Butter Jar
									if (!last_line.equals("PEANUT")) {
										set.put("chameleon_preset", "PEANUT");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "PEANUT", player);
										tcf.updateChameleonFrame(id, PRESET.PEANUT);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Peanut Butter Jar");
									break;
								case 21:
									// Lamp Post
									if (!last_line.equals("LAMP")) {
										set.put("chameleon_preset", "LAMP");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "LAMP", player);
										tcf.updateChameleonFrame(id, PRESET.LAMP);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Lamp Post");
									break;
								case 22:
									// Candy Cane
									if (!last_line.equals("CANDY")) {
										set.put("chameleon_preset", "CANDY");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "CANDY", player);
										tcf.updateChameleonFrame(id, PRESET.CANDY);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Candy Cane");
									break;
								case 23:
									// Toilet
									if (!last_line.equals("TOILET")) {
										set.put("chameleon_preset", "TOILET");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "TOILET", player);
										tcf.updateChameleonFrame(id, PRESET.TOILET);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Water Closet");
									break;
								case 24:
									// Robot
									if (!last_line.equals("ROBOT")) {
										set.put("chameleon_preset", "ROBOT");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "ROBOT", player);
										tcf.updateChameleonFrame(id, PRESET.ROBOT);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Robot");
									break;
								case 25:
									// Flaming Torch
									if (!last_line.equals("TORCH")) {
										set.put("chameleon_preset", "TORCH");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "TORCH", player);
										tcf.updateChameleonFrame(id, PRESET.TORCH);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Flaming Torch");
									break;
								case 26:
									// Pine Tree
									if (!last_line.equals("PINE")) {
										set.put("chameleon_preset", "PINE");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "PINE", player);
										tcf.updateChameleonFrame(id, PRESET.PINE);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Pine Tree");
									break;
								case 27:
									// Steam Punked
									if (!last_line.equals("PUNKED")) {
										set.put("chameleon_preset", "PUNKED");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "PUNKED", player);
										tcf.updateChameleonFrame(id, PRESET.PUNKED);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Steam Punked");
									break;
								case 28:
									// Nether Portal
									if (!last_line.equals("PORTAL")) {
										set.put("chameleon_preset", "PORTAL");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "PORTAL", player);
										tcf.updateChameleonFrame(id, PRESET.PORTAL);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Nether Portal");
									break;
								case 29:
									// Cake
									if (!last_line.equals("CAKE")) {
										set.put("chameleon_preset", "CAKE");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "CAKE", player);
										tcf.updateChameleonFrame(id, PRESET.CAKE);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Birthday Cake");
									break;
								case 30:
									// Gravestone
									if (!last_line.equals("GRAVESTONE")) {
										set.put("chameleon_preset", "GRAVESTONE");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "GRAVESTONE", player);
										tcf.updateChameleonFrame(id, PRESET.GRAVESTONE);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Gravestone");
									break;
								case 31:
									// Topsy-turvey
									if (!last_line.equals("TOPSYTURVEY")) {
										set.put("chameleon_preset", "TOPSYTURVEY");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "TOPSYTURVEY", player);
										tcf.updateChameleonFrame(id, PRESET.TOPSYTURVEY);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Topsy-turvey");
									break;
								case 32:
									// Mushroom
									if (!last_line.equals("SHROOM")) {
										set.put("chameleon_preset", "SHROOM");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "SHROOM", player);
										tcf.updateChameleonFrame(id, PRESET.SHROOM);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Mushroom");
									break;
								case 33:
									// Random Fence
									if (!last_line.equals("FENCE")) {
										set.put("chameleon_preset", "FENCE");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "FENCE", player);
										tcf.updateChameleonFrame(id, PRESET.FENCE);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Random Fence");
									break;
								case 34:
									// Gazebo
									if (!last_line.equals("GAZEBO")) {
										set.put("chameleon_preset", "GAZEBO");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "GAZEBO", player);
										tcf.updateChameleonFrame(id, PRESET.GAZEBO);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Gazebo");
									break;
								case 35:
									// Apperture Science
									if (!last_line.equals("APPERTURE")) {
										set.put("chameleon_preset", "APPERTURE");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "APPERTURE", player);
										tcf.updateChameleonFrame(id, PRESET.APPERTURE);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Apperture Science");
									break;
								case 36:
									// Lighthouse
									if (!last_line.equals("LIGHTHOUSE")) {
										set.put("chameleon_preset", "LIGHTHOUSE");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "LIGHTHOUSE", player);
										tcf.updateChameleonFrame(id, PRESET.LIGHTHOUSE);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Tiny Lighthouse");
									break;
								case 37:
									// Library
									if (!last_line.equals("LIBRARY")) {
										set.put("chameleon_preset", "LIBRARY");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "LIBRARY", player);
										tcf.updateChameleonFrame(id, PRESET.LIBRARY);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Library");
									break;
								case 38:
									// Snowman
									if (!last_line.equals("SNOWMAN")) {
										set.put("chameleon_preset", "SNOWMAN");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "SNOWMAN", player);
										tcf.updateChameleonFrame(id, PRESET.SNOWMAN);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Snowman");
									break;
								case 39:
									// Jail Cell
									if (!last_line.equals("JAIL")) {
										set.put("chameleon_preset", "JAIL");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "JAIL", player);
										tcf.updateChameleonFrame(id, PRESET.JAIL);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Jail Cell");
									break;
								case 40:
									// Pandorica
									if (!last_line.equals("PANDORICA")) {
										set.put("chameleon_preset", "PANDORICA");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "PANDORICA", player);
										tcf.updateChameleonFrame(id, PRESET.PANDORICA);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Pandorica");
									break;
								case 41:
									// double helix
									if (!last_line.equals("HELIX")) {
										set.put("chameleon_preset", "HELIX");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "HELIX", player);
										tcf.updateChameleonFrame(id, PRESET.HELIX);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Double Helix");
									break;
								case 42:
									// Prismarine
									if (!last_line.equals("PRISMARINE")) {
										set.put("chameleon_preset", "PRISMARINE");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "PRISMARINE", player);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Guardian Temple");
									tcf.updateChameleonFrame(id, PRESET.PRISMARINE);
									break;
								case 43:
									// Chorus
									if (!last_line.equals("CHORUS")) {
										set.put("chameleon_preset", "CHORUS");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "CHORUS", player);
										tcf.updateChameleonFrame(id, PRESET.CHORUS);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Chorus Flower");
									break;
								case 44:
									// Andesite
									if (!last_line.equals("ANDESITE")) {
										set.put("chameleon_preset", "ANDESITE");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "ANDESITE", player);
										tcf.updateChameleonFrame(id, PRESET.ANDESITE);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Andesite Box");
									break;
								case 45:
									// Diorite
									if (!last_line.equals("DIORITE")) {
										set.put("chameleon_preset", "DIORITE");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "DIORITE", player);
										tcf.updateChameleonFrame(id, PRESET.DIORITE);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Diorite Box");
									break;
								case 46:
									// Granite
									if (!last_line.equals("GRANITE")) {
										set.put("chameleon_preset", "GRANITE");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "GRANITE", player);
										tcf.updateChameleonFrame(id, PRESET.GRANITE);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Granite Box");
									break;
								case 48:
									// custom
									if (!last_line.equals("CUSTOM")) {
										set.put("chameleon_preset", "CUSTOM");
										updateChameleonSign(hasChameleonSign, rsc.getData(), "CUSTOM", player);
										tcf.updateChameleonFrame(id, PRESET.CUSTOM);
									}
									TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Server's Custom");
									break;
								case 51:
									// return to Chameleon Circuit GUI
									close(player);
									plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
										ItemStack[] stacks = new TARDISChameleonInventory(plugin, tardis.getAdaption(), tardis.getPreset()).getMenu();
										Inventory gui = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Chameleon Circuit");
										gui.setContents(stacks);
										player.openInventory(gui);
									}, 2L);
									break;
								case 52:
									// go to page two (coloured police boxes)
									close(player);
									plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
										ItemStack[] boxes = new TARDISPoliceBoxInventory(plugin, player).getBoxes();
										Inventory gui = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Chameleon Police Boxes");
										gui.setContents(boxes);
										player.openInventory(gui);
									}, 2L);
									break;
								default:
									close(player);
									break;
							}
							if (set.size() > 0) {
								set.put("adapti_on", 0);
								set.put("chameleon_demat", preset);
								plugin.getQueryFactory().doUpdate("tardis", set, wherec);
							}
						}
					}
				}
			}
		}
	}

	private void updateChameleonSign(boolean update, ArrayList<HashMap<String, String>> map, String preset, Player player) {
		if (update) {
			for (HashMap<String, String> entry : map) {
				TARDISStaticUtils.setSign(entry.get("location"), 3, preset, player);
			}
		}
	}
}
