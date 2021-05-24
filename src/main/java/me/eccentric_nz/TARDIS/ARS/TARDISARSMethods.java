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
package me.eccentric_nz.tardis.ars;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.advanced.TARDISCircuitChecker;
import me.eccentric_nz.tardis.advanced.TARDISCircuitDamager;
import me.eccentric_nz.tardis.database.resultset.*;
import me.eccentric_nz.tardis.enumeration.Consoles;
import me.eccentric_nz.tardis.enumeration.Difficulty;
import me.eccentric_nz.tardis.enumeration.DiskCircuit;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.rooms.RoomRequiredLister;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * The architectural reconfiguration system is a component of the Doctor's tardis in the shape of a tree that, according
 * to the Eleventh Doctor, "reconstructs the particles according to your needs." It is basically "a machine that makes
 * machines," perhaps somewhat like a 3D printer. It is, according to Gregor Van Baalen's scanner, "more valuable than
 * the total sum of any currency.
 *
 * @author eccentric_nz
 */
public class TARDISARSMethods {

	final TARDISPlugin plugin;
	final HashMap<UUID, Integer> scroll_start = new HashMap<>();
	final HashMap<UUID, Integer> selected_slot = new HashMap<>();
	final HashMap<UUID, TARDISARSSaveData> save_map_data = new HashMap<>();
	final HashMap<UUID, TARDISARSMapData> map_data = new HashMap<>();
	final Set<String> consoleBlocks = Consoles.getBY_MATERIALS().keySet();
	final HashMap<UUID, Integer> ids = new HashMap<>();
	final List<UUID> hasLoadedMap = new ArrayList<>();
	private final String[] levels = new String[]{"Bottom level", "Main level", "Top level"};

	TARDISARSMethods(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Converts the JSON data stored in the database to a 3D array.
	 *
	 * @param js the JSON from the database
	 * @return a 3D array of Strings
	 */
	public static String[][][] getGridFromJSON(String js) {
		String[][][] grid = new String[3][9][9];
		JsonArray json = JsonParser.parseString(js).getAsJsonArray();
		for (int y = 0; y < 3; y++) {
			JsonArray jsonX = json.get(y).getAsJsonArray();
			for (int x = 0; x < 9; x++) {
				JsonArray jsonZ = jsonX.get(x).getAsJsonArray();
				for (int z = 0; z < 9; z++) {
					if (jsonZ.get(z).getAsString().equals("TNT")) {
						grid[y][x][z] = "STONE";
					} else {
						grid[y][x][z] = jsonZ.get(z).getAsString();
					}
				}
			}
		}
		return grid;
	}

	/**
	 * Saves the current ars data to the database.
	 *
	 * @param uuid the UUID of the player who is using the ars GUI
	 */
	private void saveAll(UUID uuid) {
		TARDISARSMapData md = map_data.get(uuid);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		JsonArray json = JsonParser.parseString(gson.toJson(md.getData())).getAsJsonArray();
		HashMap<String, Object> set = new HashMap<>();
		set.put("ars_x_east", md.getE());
		set.put("ars_z_south", md.getS());
		set.put("ars_y_layer", md.getY());
		set.put("json", json.toString());
		HashMap<String, Object> whereA = new HashMap<>();
		whereA.put("arsId", md.getId());
		plugin.getQueryFactory().doUpdate("ars", set, whereA);
	}

	/**
	 * Saves the current ars data to the database.
	 *
	 * @param uuid the UUID of the player who is using the ars GUI
	 */
	private void revert(UUID uuid) {
		TARDISARSSaveData sd = save_map_data.get(uuid);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		JsonArray json = JsonParser.parseString(gson.toJson(sd.getData())).getAsJsonArray();
		HashMap<String, Object> set = new HashMap<>();
		set.put("json", json.toString());
		HashMap<String, Object> whereA = new HashMap<>();
		whereA.put("ars_id", sd.getId());
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getQueryFactory().doUpdate("ars", set, whereA), 6L);
	}

	/**
	 * Gets a 5x5 2D slice from a 3D array
	 *
	 * @param layer the level to to get
	 * @param x     the x position of the slice
	 * @param z     the z position of the slice
	 * @return a slice of the larger array
	 */
	private String[][] sliceGrid(String[][] layer, int x, int z) {
		String[][] slice = new String[5][5];
		int indexX = 0, indexZ = 0;
		for (int xx = x; xx < (x + 5); xx++) {
			for (int zz = z; zz < (z + 5); zz++) {
				slice[indexX][indexZ] = layer[xx][zz];
				indexZ++;
			}
			indexZ = 0;
			indexX++;
		}
		return slice;
	}

	/**
	 * Sets an ItemStack to the specified inventory slot updating the display name and removing any lore.
	 *
	 * @param view     the inventory to update
	 * @param slot     the slot number to update
	 * @param material the item (material) type to set the item stack to
	 * @param room     the room type associated with the block type
	 * @param uuid     the player using the GUI
	 * @param update   whether to update the grid display
	 */
	void setSlot(InventoryView view, int slot, Material material, String room, UUID uuid, boolean update) {
		ItemStack is = new ItemStack(material, 1);
		ItemMeta im = is.getItemMeta();
		assert im != null;
		im.setDisplayName(room);
		if (!room.equals("Empty slot")) {
			String config_path = TARDISARS.ARSFor(material.toString()).getConfigPath();
			List<String> lore = Collections.singletonList("Cost: " + plugin.getRoomsConfig().getInt("rooms." + config_path + ".cost"));
			im.setLore(lore);
		} else {
			im.setLore(null);
		}
		im.setCustomModelData(1);
		is.setItemMeta(im);
		view.setItem(slot, is);
		if (update) {
			updateGrid(uuid, slot, material.toString());
		}
	}

	/**
	 * Sets an ItemStack to the specified inventory slot.
	 *
	 * @param view   the inventory to update
	 * @param slot   the slot number to update
	 * @param is     the item stack to set
	 * @param uuid   the player using the GUI
	 * @param update whether to update the grid display
	 */
	void setSlot(InventoryView view, int slot, ItemStack is, UUID uuid, boolean update) {
		view.setItem(slot, is);
		String material = is.getType().toString();
		if (update) {
			updateGrid(uuid, slot, material);
		}
	}

	/**
	 * Get the coordinates of the clicked slot in relation to the ars map.
	 *
	 * @param slot the slot that was clicked
	 * @param md   an instance of the TARDISARSMapData class from which to retrieve the map offset
	 * @return an array of ints
	 */
	int[] getCoords(int slot, TARDISARSMapData md) {
		int[] coords = new int[2];
		if (slot <= 8) {
			coords[0] = (slot - 4) + md.getE();
			coords[1] = md.getS();
		}
		if (slot > 8 && slot <= 17) {
			coords[0] = (slot - 13) + md.getE();
			coords[1] = md.getS() + 1;
		}
		if (slot > 17 && slot <= 26) {
			coords[0] = (slot - 22) + md.getE();
			coords[1] = md.getS() + 2;
		}
		if (slot > 26 && slot <= 35) {
			coords[0] = (slot - 31) + md.getE();
			coords[1] = md.getS() + 3;
		}
		if (slot > 35 && slot <= 44) {
			coords[0] = (slot - 40) + md.getE();
			coords[1] = md.getS() + 4;
		}
		return coords;
	}

	/**
	 * Saves the current map to the TARDISARSMapData instance associated with the player using the GUI.
	 *
	 * @param uuid     the UUID of the player using the GUI
	 * @param slot     the slot that was clicked
	 * @param material the type id of the block in the slot
	 */
	private void updateGrid(UUID uuid, int slot, String material) {
		TARDISARSMapData md = map_data.get(uuid);
		String[][][] grid = md.getData();
		int yy = md.getY();
		int[] coords = getCoords(slot, md);
		int newX = coords[0];
		int newZ = coords[1];
		if (material.equals("SANDSTONE")) {
			if (yy < 2) {
				grid[yy + 1][newX][newZ] = material;
			}
		} else if (material.equals("MOSSY_COBBLESTONE")) {
			if (yy > 0) {
				grid[yy - 1][newX][newZ] = material;
			}
		}
		grid[yy][newX][newZ] = material;
		md.setData(grid);
		map_data.put(uuid, md);
	}

	/**
	 * Sets the lore of the ItemStack in the specified slot.
	 *
	 * @param view the inventory to update
	 * @param slot the slot to update
	 * @param str  the lore to set
	 */
	void setLore(InventoryView view, int slot, String str) {
		List<String> lore = (str != null) ? Collections.singletonList(str) : null;
		ItemStack is = view.getItem(slot);
		assert is != null;
		ItemMeta im = is.getItemMeta();
		assert im != null;
		im.setLore(lore);
		is.setItemMeta(im);
	}

	/**
	 * Switches the indicator block for the map level.
	 *
	 * @param view the inventory to update
	 * @param slot the slot to update
	 * @param uuid the UUID of the player using the GUI
	 */
	void switchLevel(InventoryView view, int slot, UUID uuid) {
		TARDISARSMapData md = map_data.get(uuid);
		for (int i = 27; i < 30; i++) {
			Material material = Material.WHITE_WOOL;
			if (i == slot) {
				material = Material.YELLOW_WOOL;
				md.setY(i - 27);
				map_data.put(uuid, md);
			}
			ItemStack is = new ItemStack(material, 1);
			ItemMeta im = is.getItemMeta();
			assert im != null;
			im.setDisplayName(levels[i - 27]);
			im.setCustomModelData(i - 26);
			is.setItemMeta(im);
			setSlot(view, i, is, uuid, false);
		}
	}

	/**
	 * Closes the inventory.
	 *
	 * @param p the player using the GUI
	 */
	void close(Player p) {
		UUID uuid = p.getUniqueId();
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			scroll_start.remove(uuid);
			selected_slot.remove(uuid);
			hasLoadedMap.remove(uuid);
			if (map_data.containsKey(uuid)) {
				if (playerIsOwner(p.getUniqueId().toString(), ids.get(uuid))) {
					saveAll(uuid);
					TARDISARSProcessor tap = new TARDISARSProcessor(plugin, ids.get(uuid));
					boolean changed = tap.compare3DArray(save_map_data.get(uuid).getData(), map_data.get(uuid).getData());
					if (changed && tap.checkCosts(tap.getChanged(), tap.getJettison())) {
						if (plugin.getConfig().getBoolean("growth.rooms_require_blocks")) {
							if (!hasCondensables(uuid.toString(), tap.getChanged(), ids.get(uuid))) {
								String message = (tap.getChanged().size() > 1) ? "ARS_CONDENSE_MULTIPLE" : "ARS_CONDENSE";
								TARDISMessage.send(p, message);
								if (tap.getChanged().size() == 1) {
									RoomRequiredLister.listCondensables(plugin, tap.getChanged().entrySet().iterator().next().getValue().toString(), p);
								}
								revert(uuid);
								p.closeInventory();
								return;
							}
						}
						TARDISMessage.send(p, "ARS_START");
						// do all jettisons first
						if (tap.getJettison().size() > 0) {
							TARDISMessage.send(p, "ROOM_JETT", String.format("%d", tap.getJettison().size()));
							long del = 5L;
							for (Map.Entry<TARDISARSJettison, ARS> map : tap.getJettison().entrySet()) {
								TARDISARSJettisonRunnable jr = new TARDISARSJettisonRunnable(plugin, map.getKey(), map.getValue(), ids.get(uuid), p);
								plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, jr, del);
								del += 5L;
							}
						}
						// one every 40 seconds at default room_speed
						long period = 2400L * (Math.round(20 / plugin.getConfig().getDouble("growth.room_speed")));
						long delay = 20L;
						for (Map.Entry<TARDISARSSlot, ARS> map : tap.getChanged().entrySet()) {
							TARDISARSRunnable ar = new TARDISARSRunnable(plugin, map.getKey(), map.getValue(), p, ids.get(uuid));
							plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, ar, delay);
							delay += period;
						}
						// damage the circuit if configured
						if (plugin.getConfig().getBoolean("circuits.damage") && !plugin.getDifficulty().equals(Difficulty.EASY) && plugin.getConfig().getInt("circuits.uses.ars") > 0) {
							// get the id of the tardis this player is in
							int id = plugin.getTardisAPI().getIdOfTARDISPlayerIsIn(uuid);
							TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
							tcc.getCircuits();
							// decrement uses
							int uses_left = tcc.getArsUses();
							new TARDISCircuitDamager(plugin, DiskCircuit.ARS, uses_left, id, p).damage();
						}
					} else {
						// reset map to the previous version
						revert(uuid);
						if (tap.getError().equals("ARS_LIMIT")) {
							TARDISMessage.send(p, tap.getError(), plugin.getConfig().getString("growth.ars_limit"));
						} else {
							TARDISMessage.send(p, tap.getError());
						}
					}
				} else {
					TARDISMessage.send(p, "ROOM_ONLY_TL");
					revert(uuid);
				}
				map_data.remove(uuid);
				save_map_data.remove(uuid);
				ids.remove(uuid);
			}
			p.closeInventory();
		}, 1L);
	}

	/**
	 * Loads the map from the database ready for use in the GUI.
	 *
	 * @param view the inventory to load the map into
	 * @param uuid the UUID of the player using the GUI
	 */
	void loadMap(InventoryView view, UUID uuid) {
		if (Objects.requireNonNull(Objects.requireNonNull(view.getItem(10)).getItemMeta()).hasLore()) {
			setLore(view, 10, plugin.getLanguage().getString("ARS_MAP_ERROR"));
			return;
		}
		setLore(view, 10, "Loading...");
		HashMap<String, Object> where = new HashMap<>();
		where.put("tardis_id", ids.get(uuid));
		ResultSetARS rs = new ResultSetARS(plugin, where);
		if (rs.resultSet()) {
			TARDISARSSaveData sd = new TARDISARSSaveData();
			TARDISARSMapData md = new TARDISARSMapData();
			String[][][] json = getGridFromJSON(rs.getJson());
			String[][][] json2 = getGridFromJSON(rs.getJson());
			sd.setData(json);
			sd.setId(rs.getId());
			md.setData(json2);
			md.setE(rs.getEast());
			md.setS(rs.getSouth());
			md.setY(rs.getLayer());
			md.setId(rs.getId());
			save_map_data.put(uuid, sd);
			map_data.put(uuid, md);
			setMap(rs.getLayer(), rs.getEast(), rs.getSouth(), uuid, view);
			saveAll(uuid);
			hasLoadedMap.add(uuid);
			setLore(view, 10, plugin.getLanguage().getString("ARS_MAP_LOADED"));
			switchLevel(view, (27 + rs.getLayer()), uuid);
		}
	}

	void setMap(int ul, int ue, int us, UUID uuid, InventoryView view) {
		TARDISARSMapData data = map_data.get(uuid);
		String[][][] grid = data.getData();
		String[][] layer = grid[ul];
		String[][] map = sliceGrid(layer, ue, us);
		int indexX = 0, indexZ = 0;
		for (int i = 4; i < 9; i++) {
			for (int j = 0; j < 5; j++) {
				int slot = i + (j * 9);
				Material material = Material.valueOf(map[indexX][indexZ]);
				String name = TARDISARS.ARSFor(map[indexX][indexZ]).getDescriptiveName();
				setSlot(view, slot, material, name, uuid, false);
				indexZ++;
			}
			indexZ = 0;
			indexX++;
		}
	}

	/**
	 * Move the map to a new position.
	 *
	 * @param uuid the UUID of the player using the GUI
	 * @param view the inventory to update
	 * @param slot the slot number to update
	 */
	void moveMap(UUID uuid, InventoryView view, int slot) {
		if (map_data.containsKey(uuid)) {
			TARDISARSMapData md = map_data.get(uuid);
			int ue, us;
			switch (slot) {
				case 1 -> {
					ue = md.getE();
					us = ((md.getS() + 1) < 5) ? md.getS() + 1 : md.getS();
				}
				case 9 -> {
					ue = ((md.getE() + 1) < 5) ? md.getE() + 1 : md.getE();
					us = md.getS();
				}
				case 11 -> {
					ue = ((md.getE() - 1) >= 0) ? md.getE() - 1 : md.getE();
					us = md.getS();
				}
				default -> {
					ue = md.getE();
					us = ((md.getS() - 1) >= 0) ? md.getS() - 1 : md.getS();
				}
			}
			setMap(md.getY(), ue, us, uuid, view);
			setLore(view, slot, null);
			md.setE(ue);
			md.setS(us);
			map_data.put(uuid, md);
		} else {
			setLore(view, slot, plugin.getLanguage().getString("ARS_LOAD"));
		}
	}

	/**
	 * Checks whether a player has condensed the required BLOCKS to grow the room (s).
	 *
	 * @param uuid the UUID of the player to check for
	 * @param map  a HashMap where the key is the changed room slot and the value is the ars room type
	 * @param id   the tardis id
	 * @return true or false
	 */
	private boolean hasCondensables(String uuid, HashMap<TARDISARSSlot, ARS> map, int id) {
		boolean hasRequired = true;
		String wall = "ORANGE_WOOL";
		String floor = "LIGHT_GRAY_WOOL";
		boolean hasPrefs = false;
		ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid);
		if (rsp.resultSet()) {
			hasPrefs = true;
			wall = rsp.getWall();
			floor = rsp.getFloor();
		}
		HashMap<String, Integer> item_counts = new HashMap<>();
		for (Map.Entry<TARDISARSSlot, ARS> rooms : map.entrySet()) {
			HashMap<String, Integer> roomBlocks = plugin.getBuildKeeper().getRoomBlockCounts().get(rooms.getValue().toString());
			for (Map.Entry<String, Integer> entry : roomBlocks.entrySet()) {
				String bid = entry.getKey();
				String bkey;
				if (hasPrefs && (bid.equals("ORANGE_WOOL") || bid.equals("LIGHT_GRAY_WOOL"))) {
					bkey = (bid.equals("ORANGE_WOOL")) ? wall : floor;
				} else {
					bkey = bid;
				}
				int tmp = Math.round((entry.getValue() / 100.0F) * plugin.getConfig().getInt("growth.rooms_condenser_percent"));
				int required = (tmp > 0) ? tmp : 1;
				if (item_counts.containsKey(bkey)) {
					item_counts.put(bkey, (item_counts.get(bkey) + required));
				} else {
					item_counts.put(bkey, required);
				}
			}
		}
		for (Map.Entry<String, Integer> blocks : item_counts.entrySet()) {
			HashMap<String, Object> whereC = new HashMap<>();
			whereC.put("tardis_id", id);
			whereC.put("block_data", blocks.getKey());
			ResultSetCondenser rsc = new ResultSetCondenser(plugin, whereC);
			if (rsc.resultSet()) {
				if (rsc.getBlockCount() < blocks.getValue()) {
					hasRequired = false;
				}
			} else {
				hasRequired = false;
			}
		}
		return hasRequired;
	}

	int getTardisId(String uuid) {
		int id = 0;
		HashMap<String, Object> where = new HashMap<>();
		where.put("uuid", uuid);
		ResultSetTravellers rs = new ResultSetTravellers(plugin, where, false);
		if (rs.resultSet()) {
			id = rs.getTardisId();
		}
		return id;
	}

	boolean hasRenderer(UUID uuid) {
		HashMap<String, Object> where = new HashMap<>();
		where.put("tardis_id", ids.get(uuid));
		ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
		if (rs.resultSet()) {
			return !rs.getTardis().getRenderer().isEmpty();
		}
		return false;
	}

	boolean checkSlotForConsole(InventoryView view, int slot, String uuid) {
		Material m = Objects.requireNonNull(view.getItem(slot)).getType();
		if (m.equals(Material.NETHER_BRICKS)) {
			// allow only if console is not MASTER
			HashMap<String, Object> where = new HashMap<>();
			where.put("uuid", uuid);
			ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
			if (rs.resultSet() && !rs.getTardis().getSchematic().getSeed().equals("NETHER_BRICKS")) {
				return false;
			}
		}
		if (m.equals(Material.NETHER_WART_BLOCK)) {
			// allow only if console is not CORAL
			HashMap<String, Object> where = new HashMap<>();
			where.put("uuid", uuid);
			ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
			if (rs.resultSet() && !rs.getTardis().getSchematic().getSeed().equals("NETHER_WART_BLOCK")) {
				return false;
			}
		}
		return (consoleBlocks.contains(m.toString()));
	}

	private boolean playerIsOwner(String uuid, int id) {
		HashMap<String, Object> where = new HashMap<>();
		where.put("tardis_id", id);
		where.put("uuid", uuid);
		ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
		return rs.resultSet();
	}
}
