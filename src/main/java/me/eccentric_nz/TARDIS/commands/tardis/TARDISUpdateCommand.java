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
package me.eccentric_nz.tardis.commands.tardis;

import com.google.common.collect.Sets;
import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.ars.TARDISARSMethods;
import me.eccentric_nz.tardis.blueprints.TARDISPermission;
import me.eccentric_nz.tardis.builders.TARDISTimeRotor;
import me.eccentric_nz.tardis.chatGUI.TARDISUpdateChatGUI;
import me.eccentric_nz.tardis.custommodeldata.TARDISMushroomBlockData;
import me.eccentric_nz.tardis.database.data.Farm;
import me.eccentric_nz.tardis.database.data.TARDIS;
import me.eccentric_nz.tardis.database.resultset.ResultSetARS;
import me.eccentric_nz.tardis.database.resultset.ResultSetFarming;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.enumeration.Updateable;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.messaging.TARDISUpdateLister;
import me.eccentric_nz.tardis.update.TARDISUpdateBlocks;
import me.eccentric_nz.tardis.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Door.Hinge;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

/**
 * @author eccentric_nz
 */
class TARDISUpdateCommand {

	private final TARDISPlugin plugin;
	private final Set<Updateable> mustGrowRoom = Sets.newHashSet(Updateable.FARM, Updateable.FUEL, Updateable.IGLOO, Updateable.SMELT, Updateable.STABLE, Updateable.STALL, Updateable.VAULT, Updateable.VILLAGE);

	TARDISUpdateCommand(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	boolean startUpdate(Player player, String[] args) {
		if (TARDISPermission.hasPermission(player, "tardis.update")) {
			if (args.length == 1) {
				return new TARDISUpdateChatGUI(plugin).showInterface(player, args);
			} else if (args.length < 2) {
				TARDISMessage.send(player, "TOO_FEW_ARGS");
				return false;
			}
			if (args[1].equalsIgnoreCase("list")) {
				for (Updateable u : Updateable.values()) {
					System.out.println(u.getName() + " valid blocks:");
					for (Material m : u.getMaterialChoice().getChoices()) {
						String s = m.toString();
						if (s.equals("SPAWNER")) {
							System.out.println("   ANY BLOCK");
						} else {
							System.out.println("   " + s);
						}
					}
				}
				return true;
			}
			HashMap<String, Object> where = new HashMap<>();
			where.put("uuid", player.getUniqueId().toString());
			ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
			if (!rs.resultSet()) {
				TARDISMessage.send(player, "NOT_A_TIMELORD");
				return false;
			}
			TARDIS tardis = rs.getTardis();
			int ownerid = tardis.getTardisId();
			String tardis_block = TARDISStringUtils.toScoredUppercase(args[1]);
			Updateable updateable;
			try {
				updateable = Updateable.valueOf(tardis_block);
			} catch (IllegalArgumentException e) {
				new TARDISUpdateLister(player).list();
				return true;
			}
			if (args.length == 3 && args[2].equalsIgnoreCase("blocks")) {
				TARDISUpdateBlocks.showOptions(player, updateable);
				return true;
			}
			if (updateable.equals(Updateable.SIEGE) && !plugin.getConfig().getBoolean("siege.enabled")) {
				TARDISMessage.send(player, "SIEGE_DISABLED");
				return true;
			}
			if (updateable.equals(Updateable.BEACON) && !tardis.isPowered()) {
				TARDISMessage.send(player, "UPDATE_BEACON");
				return true;
			}
			if (updateable.equals(Updateable.HINGE)) {
				Block block = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 10);
				if (block.getType().equals(Material.IRON_DOOR)) {
					Door door = (Door) block.getBlockData();
					if (args.length == 3) {
						Hinge setHinge = Hinge.valueOf(args[2].toUpperCase(Locale.ENGLISH));
						door.setHinge(setHinge);
					} else {
						Hinge hinge = door.getHinge();
						if (hinge.equals(Hinge.LEFT)) {
							door.setHinge(Hinge.RIGHT);
						} else {
							door.setHinge(Hinge.LEFT);
						}
					}
					block.setBlockData(door);
				}
				return true;
			}
			if (updateable.equals(Updateable.ADVANCED) && !TARDISPermission.hasPermission(player, "tardis.advanced")) {
				TARDISMessage.send(player, "NO_PERM_ADV");
				return true;
			}
			if (updateable.equals(Updateable.STORAGE)) {
				// update note block if it's not MUSHROOM_STEM
				Block block = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 10);
				if (block.getType().equals(Material.NOTE_BLOCK)) {
					BlockData mushroom = plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(51));
					block.setBlockData(mushroom, true);
				}
			}
			if (updateable.equals(Updateable.FORCEFIELD) && !TARDISPermission.hasPermission(player, "tardis.forcefield")) {
				TARDISMessage.send(player, "NO_PERM_FF");
				return true;
			}
			if (updateable.equals(Updateable.STORAGE) && !TARDISPermission.hasPermission(player, "tardis.storage")) {
				TARDISMessage.send(player, "NO_PERM_DISK");
				return true;
			}
			if (updateable.equals(Updateable.BACKDOOR) && !TARDISPermission.hasPermission(player, "tardis.backdoor")) {
				TARDISMessage.send(player, "NO_PERM_BACKDOOR");
				return true;
			}
			if (updateable.equals(Updateable.TEMPORAL) && !TARDISPermission.hasPermission(player, "tardis.temporal")) {
				TARDISMessage.send(player, "NO_PERM_TEMPORAL");
				return true;
			}
			boolean hasFarm = false;
			boolean hasIgloo = false;
			boolean hasSmelt = false;
			boolean hasStable = false;
			boolean hasStall = false;
			boolean hasVault = false;
			boolean hasVillage = false;
			// check ARS for room type
			if (mustGrowRoom.contains(updateable)) {
				HashMap<String, Object> wherea = new HashMap<>();
				wherea.put("tardis_id", ownerid);
				ResultSetARS rsa = new ResultSetARS(plugin, wherea);
				if (rsa.resultSet()) {
					// check for rooms
					String[][][] json = TARDISARSMethods.getGridFromJSON(rsa.getJson());
					for (String[][] level : json) {
						for (String[] row : level) {
							for (String col : row) {
								if (col.equals("DIRT")) {
									hasFarm = true;
								}
								if (col.equals("PACKED_ICE")) {
									hasIgloo = true;
								}
								if (col.equals("CHEST")) {
									hasSmelt = true;
								}
								if (col.equals("HAY_BLOCK")) {
									hasStable = true;
								}
								if (col.equals("NETHER_WART_BLOCK")) {
									hasStall = true;
								}
								if (col.equals("DISPENSER")) {
									hasVault = true;
								}
								if (col.equals("OAK_LOG")) {
									hasVillage = true;
								}
							}
						}
					}
				}
			}
			if (updateable.equals(Updateable.VAULT)) {
				if (!TARDISPermission.hasPermission(player, "tardis.vault")) {
					TARDISMessage.send(player, "UPDATE_NO_PERM", "Vault room drop chest");
					return true;
				}
				// must grow room first
				if (!hasVault) {
					TARDISMessage.send(player, "UPDATE_ROOM", tardis_block);
					return true;
				}
			}
			if (updateable.equals(Updateable.FUEL) || updateable.equals(Updateable.SMELT)) {
				if (!TARDISPermission.hasPermission(player, "tardis.room.smelter")) {
					TARDISMessage.send(player, "UPDATE_NO_PERM", "Smelter room drop chest");
					return true;
				}
				// must grow room first
				if (!hasSmelt) {
					TARDISMessage.send(player, "UPDATE_ROOM", tardis_block);
					return true;
				}
			}
			if (updateable.equals(Updateable.FARM) || updateable.equals(Updateable.IGLOO) || updateable.equals(Updateable.STABLE) || updateable.equals(Updateable.STALL) || updateable.equals(Updateable.VILLAGE)) {
				if (!TARDISPermission.hasPermission(player, "tardis.farm")) {
					TARDISMessage.send(player, "UPDATE_NO_PERM", tardis_block);
					return true;
				}
				// must grow a room first
				ResultSetFarming rsf = new ResultSetFarming(plugin, ownerid);
				if (rsf.resultSet()) {
					Farm farming = rsf.getFarming();
					if (updateable.equals(Updateable.FARM) && farming.getFarm().isEmpty() && !hasFarm) {
						TARDISMessage.send(player, "UPDATE_ROOM", tardis_block);
						return true;
					}
					if (updateable.equals(Updateable.IGLOO) && farming.getIgloo().isEmpty() && !hasIgloo) {
						TARDISMessage.send(player, "UPDATE_ROOM", tardis_block);
						return true;
					}
					if (updateable.equals(Updateable.STABLE) && farming.getStable().isEmpty() && !hasStable) {
						TARDISMessage.send(player, "UPDATE_ROOM", tardis_block);
						return true;
					}
					if (updateable.equals(Updateable.STALL) && farming.getStall().isEmpty() && !hasStall) {
						TARDISMessage.send(player, "UPDATE_ROOM", tardis_block);
						return true;
					}
					if (updateable.equals(Updateable.VILLAGE) && farming.getVillage().isEmpty() && !hasVillage) {
						TARDISMessage.send(player, "UPDATE_ROOM", tardis_block);
						return true;
					}
				}
			}
			if (updateable.equals(Updateable.RAIL) && tardis.getRail().isEmpty()) {
				TARDISMessage.send(player, "UPDATE_ROOM", tardis_block);
				return true;
			}
			if (updateable.equals(Updateable.ZERO) && tardis.getZero().isEmpty()) {
				TARDISMessage.send(player, "UPDATE_ZERO");
				return true;
			}
			if (updateable.equals(Updateable.ARS)) {
				if (!TARDISPermission.hasPermission(player, "tardis.architectural")) {
					TARDISMessage.send(player, "NO_PERM_ARS");
					return true;
				}
				if (!plugin.getUtils().canGrowRooms(tardis.getChunk())) {
					TARDISMessage.send(player, "ARS_OWN_WORLD");
					return true;
				}
			}
			if (updateable.equals(Updateable.WEATHER)) {
				if (!TARDISPermission.hasPermission(player, "tardis.weather.clear") && !TARDISPermission.hasPermission(player, "tardis.weather.rain") && !TARDISPermission.hasPermission(player, "tardis.weather.thunder")) {
					TARDISMessage.send(player, "NO_PERMS");
					return true;
				}
			}
			if (!updateable.equals(Updateable.BACKDOOR)) {
				HashMap<String, Object> wheret = new HashMap<>();
				wheret.put("uuid", player.getUniqueId().toString());
				ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
				if (!rst.resultSet()) {
					TARDISMessage.send(player, "NOT_IN_TARDIS");
					return false;
				}
				int thisid = rst.getTardisId();
				if (thisid != ownerid) {
					TARDISMessage.send(player, "CMD_ONLY_TL");
					return false;
				}
			}
			if (updateable.equals(Updateable.ROTOR) && args.length == 3 && args[2].equalsIgnoreCase("unlock")) {
				// get Time Rotor frame location
				ItemFrame itemFrame = TARDISTimeRotor.getItemFrame(tardis.getRotor());
				if (itemFrame != null) {
					TARDISTimeRotor.unlockRotor(itemFrame);
					// also need to remove the item frame protection
					plugin.getGeneralKeeper().getTimeRotors().remove(itemFrame.getUniqueId());
					// and block protection
					Block block = itemFrame.getLocation().getBlock();
					String location = block.getLocation().toString();
					plugin.getGeneralKeeper().getProtectBlockMap().remove(location);
					String under = block.getRelative(BlockFace.DOWN).getLocation().toString();
					plugin.getGeneralKeeper().getProtectBlockMap().remove(under);
					TARDISMessage.send(player, "ROTOR_UNFIXED");
				}
				return true;
			}
			plugin.getTrackerKeeper().getPlayers().put(player.getUniqueId(), tardis_block);
			TARDISMessage.send(player, "UPDATE_CLICK", tardis_block);
			if (updateable.equals(Updateable.DIRECTION)) {
				TARDISMessage.send(player, "HOOK_REMIND");
			}
			return true;
		} else {
			TARDISMessage.send(player, "NO_PERMS");
			return false;
		}
	}
}
