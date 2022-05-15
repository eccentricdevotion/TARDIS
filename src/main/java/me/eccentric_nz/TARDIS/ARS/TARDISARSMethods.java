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
package me.eccentric_nz.TARDIS.ARS;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.commands.sudo.TARDISSudoTracker;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.rooms.RoomRequiredLister;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * The architectural reconfiguration system is a component of the Doctor's TARDIS in the shape of a tree that, according
 * to the Eleventh Doctor, "reconstructs the particles according to your needs." It is basically "a machine that makes
 * machines," perhaps somewhat like a 3D printer. It is, according to Gregor Van Baalen's scanner, "more valuable than
 * the total sum of any currency.
 *
 * @author eccentric_nz
 */
public class TARDISARSMethods {

    final TARDIS plugin;
    final HashMap<UUID, Integer> scroll_start = new HashMap<>();
    final HashMap<UUID, Integer> selected_slot = new HashMap<>();
    final HashMap<UUID, TARDISARSSaveData> save_map_data = new HashMap<>();
    final HashMap<UUID, TARDISARSMapData> map_data = new HashMap<>();
    final Set<String> consoleBlocks = Consoles.getBY_MATERIALS().keySet();
    final HashMap<UUID, Integer> ids = new HashMap<>();
    final List<UUID> hasLoadedMap = new ArrayList<>();
    private final String[] levels = new String[]{"Bottom level", "Main level", "Top level"};

    TARDISARSMethods(TARDIS plugin) {
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
            JsonArray jsonx = json.get(y).getAsJsonArray();
            for (int x = 0; x < 9; x++) {
                JsonArray jsonz = jsonx.get(x).getAsJsonArray();
                for (int z = 0; z < 9; z++) {
                    if (jsonz.get(z).getAsString().equals("TNT")) {
                        grid[y][x][z] = "STONE";
                    } else {
                        grid[y][x][z] = jsonz.get(z).getAsString();
                    }
                }
            }
        }
        return grid;
    }

    /**
     * Saves the current ARS data to the database.
     *
     * @param playerUUID the UUID of the player who is using the ARS GUI
     */
    private void saveAll(UUID playerUUID) {
        TARDISARSMapData md = map_data.get(playerUUID);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        JsonArray json = JsonParser.parseString(gson.toJson(md.getData())).getAsJsonArray();
        HashMap<String, Object> set = new HashMap<>();
        set.put("ars_x_east", md.getE());
        set.put("ars_z_south", md.getS());
        set.put("ars_y_layer", md.getY());
        set.put("json", json.toString());
        HashMap<String, Object> wherea = new HashMap<>();
        wherea.put("ars_id", md.getId());
        plugin.getQueryFactory().doUpdate("ars", set, wherea);
    }

    /**
     * Saves the current ARS data to the database.
     *
     * @param playerUUID the UUID of the player who is using the ARS GUI
     */
    private void revert(UUID playerUUID) {
        TARDISARSSaveData sd = save_map_data.get(playerUUID);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        JsonArray json = JsonParser.parseString(gson.toJson(sd.getData())).getAsJsonArray();
        HashMap<String, Object> set = new HashMap<>();
        set.put("json", json.toString());
        HashMap<String, Object> wherea = new HashMap<>();
        wherea.put("ars_id", sd.getId());
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getQueryFactory().doUpdate("ars", set, wherea), 6L);
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
        int indexx = 0, indexz = 0;
        for (int xx = x; xx < (x + 5); xx++) {
            for (int zz = z; zz < (z + 5); zz++) {
                slice[indexx][indexz] = layer[xx][zz];
                indexz++;
            }
            indexz = 0;
            indexx++;
        }
        return slice;
    }

    /**
     * Sets an ItemStack to the specified inventory slot updating the display name and setting lore if necessary.
     *
     * @param view       the inventory to update
     * @param slot       the slot number to update
     * @param material   the item (material) type to set the item stack to
     * @param room       the room type associated with the block type
     * @param playerUUID the player using the GUI
     * @param update     whether to update the grid display
     */
    void setSlot(InventoryView view, int slot, Material material, String room, UUID playerUUID, boolean update, boolean showPerms) {
        ItemStack is = new ItemStack(material, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(room);
        if (!room.equals("Empty slot")) {
            String config_path = TARDISARS.ARSFor(material.toString()).getConfigPath();
            List<String> lore = new ArrayList<>();
            lore.add("Cost: " + plugin.getRoomsConfig().getInt("rooms." + config_path + ".cost"));
            if (showPerms) {
                String roomName = TARDISARS.ARSFor(material.toString()).getConfigPath();
                Player player = plugin.getServer().getPlayer(playerUUID);
                if (player != null && !TARDISPermission.hasPermission(player, "tardis.room." + roomName.toLowerCase(Locale.ENGLISH))) {
                    lore.add(ChatColor.RED + plugin.getLanguage().getString("NO_PERM_CONSOLE"));
                }
            }
            im.setLore(lore);
        } else {
            im.setLore(null);
        }
        im.setCustomModelData(1);
        is.setItemMeta(im);
        view.setItem(slot, is);
        if (update) {
            updateGrid(playerUUID, slot, material.toString());
        }
    }

    /**
     * Sets an ItemStack to the specified inventory slot.
     *
     * @param view       the inventory to update
     * @param slot       the slot number to update
     * @param is         the item stack to set
     * @param playerUUID the player using the GUI
     * @param update     whether to update the grid display
     */
    void setSlot(InventoryView view, int slot, ItemStack is, UUID playerUUID, boolean update) {
        view.setItem(slot, is);
        String material = is.getType().toString();
        if (update) {
            updateGrid(playerUUID, slot, material);
        }
    }

    /**
     * Get the coordinates of the clicked slot in relation to the ARS map.
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
     * @param playerUUID the UUID of the player using the GUI
     * @param slot       the slot that was clicked
     * @param material   the type id of the block in the slot
     */
    private void updateGrid(UUID playerUUID, int slot, String material) {
        TARDISARSMapData md = map_data.get(playerUUID);
        String[][][] grid = md.getData();
        int yy = md.getY();
        int[] coords = getCoords(slot, md);
        int newx = coords[0];
        int newz = coords[1];
        if (material.equals("SANDSTONE")) {
            if (yy < 2) {
                grid[yy + 1][newx][newz] = material;
            }
        } else if (material.equals("MOSSY_COBBLESTONE")) {
            if (yy > 0) {
                grid[yy - 1][newx][newz] = material;
            }
        }
        grid[yy][newx][newz] = material;
        md.setData(grid);
        map_data.put(playerUUID, md);
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
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);
    }

    /**
     * Switches the indicator block for the map level.
     *
     * @param view       the inventory to update
     * @param slot       the slot to update
     * @param playerUUID the UUID of the player using the GUI
     */
    void switchLevel(InventoryView view, int slot, UUID playerUUID) {
        TARDISARSMapData md = map_data.get(playerUUID);
        for (int i = 27; i < 30; i++) {
            Material material = Material.WHITE_WOOL;
            if (i == slot) {
                material = Material.YELLOW_WOOL;
                md.setY(i - 27);
                map_data.put(playerUUID, md);
            }
            ItemStack is = new ItemStack(material, 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(levels[i - 27]);
            im.setCustomModelData(i - 26);
            is.setItemMeta(im);
            setSlot(view, i, is, playerUUID, false);
        }
    }

    /**
     * Closes the inventory.
     *
     * @param player the player using the GUI
     */
    void close(Player player) {
        UUID playerUUID = player.getUniqueId();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            scroll_start.remove(playerUUID);
            selected_slot.remove(playerUUID);
            hasLoadedMap.remove(playerUUID);
            if (map_data.containsKey(playerUUID)) {
                if (playerIsOwner(playerUUID, ids.get(playerUUID))) {
                    saveAll(playerUUID);
                    TARDISARSProcessor tap = new TARDISARSProcessor(plugin, ids.get(playerUUID));
                    boolean changed = tap.compare3DArray(save_map_data.get(playerUUID).getData(), map_data.get(playerUUID).getData());
                    if (changed && tap.checkCosts(tap.getChanged(), tap.getJettison())) {
                        if (plugin.getConfig().getBoolean("growth.rooms_require_blocks")) {
                            if (!TARDISSudoTracker.SUDOERS.containsKey(playerUUID) && !hasCondensables(playerUUID.toString(), tap.getChanged(), ids.get(playerUUID))) {
                                String message = (tap.getChanged().size() > 1) ? "ARS_CONDENSE_MULTIPLE" : "ARS_CONDENSE";
                                TARDISMessage.send(player, message);
                                if (tap.getChanged().size() == 1) {
                                    RoomRequiredLister.listCondensables(plugin, tap.getChanged().entrySet().iterator().next().getValue().toString(), player);
                                }
                                revert(playerUUID);
                                player.closeInventory();
                                return;
                            }
                        }
                        TARDISMessage.send(player, "ARS_START");
                        // do all jettisons first
                        if (tap.getJettison().size() > 0) {
                            TARDISMessage.send(player, "ROOM_JETT", String.format("%d", tap.getJettison().size()));
                            long del = 5L;
                            for (Map.Entry<TARDISARSJettison, ARS> map : tap.getJettison().entrySet()) {
                                TARDISARSJettisonRunnable jr = new TARDISARSJettisonRunnable(plugin, map.getKey(), map.getValue(), ids.get(playerUUID), player);
                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, jr, del);
                                del += 5L;
                            }
                        }
                        // one every 120 seconds at fastest room_speed - reduced by the delay factor
                        long period = (2400L / plugin.getConfig().getLong("growth.delay_factor")) * (Math.round(20 / plugin.getConfig().getDouble("growth.room_speed")));
                        long delay = 20L;
                        int tasksRemaining = tap.getChanged().size();
                        for (Map.Entry<TARDISARSSlot, ARS> map : tap.getChanged().entrySet()) {
                            tasksRemaining--;
                            TARDISARSRunnable ar = new TARDISARSRunnable(plugin, map.getKey(), map.getValue(), player, ids.get(playerUUID), tasksRemaining == 0);
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, ar, delay);
                            delay += period;
                        }
                        // damage the circuit if configured
                        if (plugin.getConfig().getBoolean("circuits.damage") && !plugin.getDifficulty().equals(Difficulty.EASY) && plugin.getConfig().getInt("circuits.uses.ars") > 0) {
                            // get the id of the TARDIS this player is in
                            int id = plugin.getTardisAPI().getIdOfTARDISPlayerIsIn(playerUUID);
                            TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
                            tcc.getCircuits();
                            // decrement uses
                            int uses_left = tcc.getArsUses();
                            new TARDISCircuitDamager(plugin, DiskCircuit.ARS, uses_left, id, player).damage();
                        }
                    } else {
                        // reset map to the previous version
                        revert(playerUUID);
                        if (tap.getError().equals("ARS_LIMIT")) {
                            TARDISMessage.send(player, tap.getError(), plugin.getConfig().getString("growth.ars_limit"));
                        } else {
                            TARDISMessage.send(player, tap.getError());
                        }
                    }
                } else {
                    TARDISMessage.send(player, "ROOM_ONLY_TL");
                    revert(playerUUID);
                }
                map_data.remove(playerUUID);
                save_map_data.remove(playerUUID);
                ids.remove(playerUUID);
            }
            player.closeInventory();
        }, 1L);
    }

    /**
     * Loads the map from the database ready for use in the GUI.
     *
     * @param view       the inventory to load the map into
     * @param playerUUID the UUID of the player using the GUI
     */
    void loadMap(InventoryView view, UUID playerUUID) {
        if (view.getItem(10).getItemMeta().hasLore()) {
            setLore(view, 10, plugin.getLanguage().getString("ARS_MAP_ERROR"));
            return;
        }
        setLore(view, 10, "Loading...");
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", ids.get(playerUUID));
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
            save_map_data.put(playerUUID, sd);
            map_data.put(playerUUID, md);
            setMap(rs.getLayer(), rs.getEast(), rs.getSouth(), playerUUID, view);
            saveAll(playerUUID);
            hasLoadedMap.add(playerUUID);
            setLore(view, 10, plugin.getLanguage().getString("ARS_MAP_LOADED"));
            switchLevel(view, (27 + rs.getLayer()), playerUUID);
        }
    }

    void setMap(int ul, int ue, int us, UUID playerUUID, InventoryView view) {
        TARDISARSMapData data = map_data.get(playerUUID);
        String[][][] grid = data.getData();
        String[][] layer = grid[ul];
        String[][] map = sliceGrid(layer, ue, us);
        int indexx = 0, indexz = 0;
        for (int i = 4; i < 9; i++) {
            for (int j = 0; j < 5; j++) {
                int slot = i + (j * 9);
                Material material = Material.valueOf(map[indexx][indexz]);
                String name = TARDISARS.ARSFor(map[indexx][indexz]).getDescriptiveName();
                setSlot(view, slot, material, name, playerUUID, false, false);
                indexz++;
            }
            indexz = 0;
            indexx++;
        }
    }

    /**
     * Move the map to a new position.
     *
     * @param playerUUID the UUID of the player using the GUI
     * @param view       the inventory to update
     * @param slot       the slot number to update
     */
    void moveMap(UUID playerUUID, InventoryView view, int slot) {
        if (map_data.containsKey(playerUUID)) {
            TARDISARSMapData md = map_data.get(playerUUID);
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
            setMap(md.getY(), ue, us, playerUUID, view);
            setLore(view, slot, null);
            md.setE(ue);
            md.setS(us);
            map_data.put(playerUUID, md);
        } else {
            setLore(view, slot, plugin.getLanguage().getString("ARS_LOAD"));
        }
    }

    /**
     * Checks whether a player has condensed the required BLOCKS to grow the room (s).
     *
     * @param uuid the UUID of the player to check for
     * @param map  a HashMap where the key is the changed room slot and the value is the ARS room type
     * @param id   the TARDIS id
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
            HashMap<String, Object> wherec = new HashMap<>();
            wherec.put("tardis_id", id);
            wherec.put("block_data", blocks.getKey());
            ResultSetCondenser rsc = new ResultSetCondenser(plugin, wherec);
            if (rsc.resultSet()) {
                if (rsc.getBlock_count() < blocks.getValue()) {
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
            id = rs.getTardis_id();
        }
        return id;
    }

    boolean hasRenderer(UUID playerUUID) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", ids.get(playerUUID));
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (rs.resultSet()) {
            return !rs.getTardis().getRenderer().isEmpty();
        }
        return false;
    }

    boolean checkSlotForConsole(InventoryView view, int slot, String uuid) {
        Material m = view.getItem(slot).getType();
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

    private boolean playerIsOwner(UUID uuid, int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("uuid", TARDISSudoTracker.SUDOERS.getOrDefault(uuid, uuid).toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        return rs.resultSet();
    }
}
