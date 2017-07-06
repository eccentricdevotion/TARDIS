/*
 * Copyright (C) 2016 eccentric_nz
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetARS;
import me.eccentric_nz.TARDIS.database.ResultSetCondenser;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;
import me.eccentric_nz.TARDIS.enumeration.DIFFICULTY;
import me.eccentric_nz.TARDIS.enumeration.DISK_CIRCUIT;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls.Pair;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * The architectural reconfiguration system is a component of the Doctor's
 * TARDIS in the shape of a tree that, according to the Eleventh Doctor,
 * "reconstructs the particles according to your needs." It is basically "a
 * machine that makes machines," perhaps somewhat like a 3D printer. It is,
 * according to Gregor Van Baalen's scanner, "more valuable than the total sum
 * of any currency.
 *
 * @author eccentric_nz
 */
public class TARDISARSMethods {

    public final TARDIS plugin;
    public final HashMap<UUID, Integer> scroll_start = new HashMap<>();
    public final HashMap<UUID, Integer> selected_slot = new HashMap<>();
    public final HashMap<UUID, TARDISARSSaveData> save_map_data = new HashMap<>();
    public final HashMap<UUID, TARDISARSMapData> map_data = new HashMap<>();
    public final String[] levels = new String[]{"Bottom level", "Main level", "Top level"};
    public final Set<String> consoleBlocks = CONSOLES.getBY_MATERIALS().keySet();
    public final HashMap<UUID, Integer> ids = new HashMap<>();
    public final List<UUID> hasLoadedMap = new ArrayList<>();

    public TARDISARSMethods(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Saves the current ARS data to the database.
     *
     * @param uuid the UUID of the player who is using the ARS GUI
     */
    public void saveAll(UUID uuid) {
        TARDISARSMapData md = map_data.get(uuid);
        JSONArray json = new JSONArray(md.getData());
        HashMap<String, Object> set = new HashMap<>();
        set.put("ars_x_east", md.getE());
        set.put("ars_z_south", md.getS());
        set.put("ars_y_layer", md.getY());
        set.put("json", json.toString());
        HashMap<String, Object> wherea = new HashMap<>();
        wherea.put("ars_id", md.getId());
        new QueryFactory(plugin).doUpdate("ars", set, wherea);
    }

    /**
     * Saves the current ARS data to the database.
     *
     * @param uuid the UUID of the player who is using the ARS GUI
     */
    public void revert(UUID uuid) {
        TARDISARSSaveData sd = save_map_data.get(uuid);
        JSONArray json = new JSONArray(sd.getData());
        final HashMap<String, Object> set = new HashMap<>();
        set.put("json", json.toString());
        final HashMap<String, Object> wherea = new HashMap<>();
        wherea.put("ars_id", sd.getId());
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            new QueryFactory(plugin).doUpdate("ars", set, wherea);
        }, 6L);
    }

    /**
     * Converts the JSON data stored in the database to a 3D array.
     *
     * @param js the JSON from the database
     * @return a 3D array of ints
     */
    public static int[][][] getGridFromJSON(String js) {
        int[][][] grid = new int[3][9][9];
        JSONArray json = new JSONArray(js);
        for (int y = 0; y < 3; y++) {
            JSONArray jsonx = json.getJSONArray(y);
            for (int x = 0; x < 9; x++) {
                JSONArray jsonz = jsonx.getJSONArray(x);
                for (int z = 0; z < 9; z++) {
                    if (jsonz.getInt(z) == 46) {
                        grid[y][x][z] = 1;
                    } else {
                        grid[y][x][z] = jsonz.getInt(z);
                    }
                }
            }
        }
        return grid;
    }

    /**
     * Gets a 5x5 2D slice from a 3D array
     *
     * @param layer the level to to get
     * @param x the x position of the slice
     * @param z the z position of the slice
     * @return a slice of the larger array
     */
    public int[][] sliceGrid(int[][] layer, int x, int z) {
        int[][] slice = new int[5][5];
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
     * Sets an ItemStack to the specified inventory slot updating the display
     * name and removing any lore.
     *
     * @param inv the inventory to update
     * @param slot the slot number to update
     * @param id the item id to set the item stack to
     * @param room the room type associated with the id
     * @param uuid the player using the GUI
     * @param update whether to update the grid display
     */
    @SuppressWarnings("deprecation")
    public void setSlot(Inventory inv, int slot, int id, String room, UUID uuid, boolean update) {
        ItemStack is = new ItemStack(id, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(room);
        if (!room.equals("Empty slot")) {
            String config_path = TARDISARS.ARSFor(room).getActualName();
            List<String> lore = Arrays.asList("Cost: " + plugin.getRoomsConfig().getInt("rooms." + config_path + ".cost"));
            im.setLore(lore);
        } else {
            im.setLore(null);
        }
        is.setItemMeta(im);
        inv.setItem(slot, is);
        if (update) {
            updateGrid(uuid, slot, id);
        }
    }

    /**
     * Sets an ItemStack to the specified inventory slot.
     *
     * @param inv the inventory to update
     * @param slot the slot number to update
     * @param is the item stack to set
     * @param uuid the player using the GUI
     * @param update whether to update the grid display
     */
    @SuppressWarnings("deprecation")
    public void setSlot(Inventory inv, int slot, ItemStack is, UUID uuid, boolean update) {
        inv.setItem(slot, is);
        int id = is.getTypeId();
        if (update) {
            updateGrid(uuid, slot, id);
        }
    }

    /**
     * Get the coordinates of the clicked slot in relation to the ARS map.
     *
     * @param slot the slot that was clicked
     * @param md an instance of the TARDISARSMapData class from which to
     * retrieve the map offset
     * @return an array of ints
     */
    public int[] getCoords(int slot, TARDISARSMapData md) {
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
     * Saves the current map to the TARDISARSMapData instance associated with
     * the player using the GUI.
     *
     * @param uuid the UUID of the player using the GUI
     * @param slot the slot that was clicked
     * @param id the type id of the block in the slot
     */
    public void updateGrid(UUID uuid, int slot, int id) {
        TARDISARSMapData md = map_data.get(uuid);
        int[][][] grid = md.getData();
        int yy = md.getY();
        int[] coords = getCoords(slot, md);
        int newx = coords[0];
        int newz = coords[1];
        if (id == 24) {
            if (yy < 2) {
                grid[yy + 1][newx][newz] = id;
            }
        } else if (id == 48) {
            if (yy > 0) {
                grid[yy - 1][newx][newz] = id;
            }
        }
        grid[yy][newx][newz] = id;
        md.setData(grid);
        map_data.put(uuid, md);
    }

    /**
     * Sets the lore of the ItemStack in the specified slot.
     *
     * @param inv the inventory to update
     * @param slot the slot to update
     * @param str the lore to set
     */
    public void setLore(Inventory inv, int slot, String str) {
        List<String> lore = (str != null) ? Arrays.asList(str) : null;
        ItemStack is = inv.getItem(slot);
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);
    }

    /**
     * Switches the indicator block for the map level.
     *
     * @param inv the inventory to update
     * @param slot the slot to update
     * @param uuid the UUID of the player using the GUI
     */
    public void switchLevel(Inventory inv, int slot, UUID uuid) {
        TARDISARSMapData md = map_data.get(uuid);
        for (int i = 27; i < 30; i++) {
            byte data = 0;
            if (i == slot) {
                data = 4;
                md.setY(i - 27);
                map_data.put(uuid, md);
            }
            ItemStack is = new ItemStack(Material.WOOL, 1, data);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(levels[i - 27]);
            is.setItemMeta(im);
            setSlot(inv, i, is, uuid, false);
        }
    }

    /**
     * Closes the inventory.
     *
     * @param p the player using the GUI
     */
    public void close(final Player p) {
        final UUID uuid = p.getUniqueId();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (scroll_start.containsKey(uuid)) {
                scroll_start.remove(uuid);
            }
            if (selected_slot.containsKey(uuid)) {
                selected_slot.remove(uuid);
            }
            if (hasLoadedMap.contains(uuid)) {
                hasLoadedMap.remove(uuid);
            }
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
                        if (plugin.getConfig().getBoolean("circuits.damage") && !plugin.getDifficulty().equals(DIFFICULTY.EASY) && plugin.getConfig().getInt("circuits.uses.ars") > 0) {
                            // get the id of the TARDIS this player is in
                            int id = plugin.getTardisAPI().getIdOfTARDISPlayerIsIn(uuid);
                            TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
                            tcc.getCircuits();
                            // decrement uses
                            int uses_left = tcc.getArsUses();
                            new TARDISCircuitDamager(plugin, DISK_CIRCUIT.ARS, uses_left, id, p).damage();
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
     * @param inv the inventory to load the map into
     * @param uuid the UUID of the player using the GUI
     */
    public void loadMap(Inventory inv, UUID uuid) {
        if (inv.getItem(10).getItemMeta().hasLore()) {
            setLore(inv, 10, "Map already loaded!");
            return;
        }
        setLore(inv, 10, "Loading...");
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", ids.get(uuid));
        ResultSetARS rs = new ResultSetARS(plugin, where);
        if (rs.resultSet()) {
            TARDISARSSaveData sd = new TARDISARSSaveData();
            TARDISARSMapData md = new TARDISARSMapData();
            int[][][] json = getGridFromJSON(rs.getJson());
            int[][][] json2 = getGridFromJSON(rs.getJson());
            sd.setData(json);
            sd.setId(rs.getId());
            md.setData(json2);
            md.setE(rs.getEast());
            md.setS(rs.getSouth());
            md.setY(rs.getLayer());
            md.setId(rs.getId());
            save_map_data.put(uuid, sd);
            map_data.put(uuid, md);
            setMap(rs.getLayer(), rs.getEast(), rs.getSouth(), uuid, inv);
            saveAll(uuid);
            hasLoadedMap.add(uuid);
            setLore(inv, 10, "Map LOADED");
            switchLevel(inv, (27 + rs.getLayer()), uuid);
        }
    }

    public void setMap(int ul, int ue, int us, UUID uuid, Inventory inv) {
        TARDISARSMapData data = map_data.get(uuid);
        int[][][] grid = data.getData();
        int[][] layer = grid[ul];
        int[][] map = sliceGrid(layer, ue, us);
        int indexx = 0, indexz = 0;
        for (int i = 4; i < 9; i++) {
            for (int j = 0; j < 5; j++) {
                int slot = i + (j * 9);
                int id = map[indexx][indexz];
                String name = TARDISARS.ARSFor(id).getDescriptiveName();
                setSlot(inv, slot, id, name, uuid, false);
                indexz++;
            }
            indexz = 0;
            indexx++;
        }
    }

    /**
     * Move the map to a new position.
     *
     * @param uuid the UUID of the player using the GUI
     * @param inv the inventory to update
     * @param slot the slot number to update
     */
    public void moveMap(UUID uuid, Inventory inv, int slot) {
        if (map_data.containsKey(uuid)) {
            TARDISARSMapData md = map_data.get(uuid);
            int ue, us;
            switch (slot) {
                case 1:
                    ue = md.getE();
                    us = ((md.getS() + 1) < 5) ? md.getS() + 1 : md.getS();
                    break;
                case 9:
                    ue = ((md.getE() + 1) < 5) ? md.getE() + 1 : md.getE();
                    us = md.getS();
                    break;
                case 11:
                    ue = ((md.getE() - 1) >= 0) ? md.getE() - 1 : md.getE();
                    us = md.getS();
                    break;
                default:
                    ue = md.getE();
                    us = ((md.getS() - 1) >= 0) ? md.getS() - 1 : md.getS();
                    break;
            }
            setMap(md.getY(), ue, us, uuid, inv);
            setLore(inv, slot, null);
            md.setE(ue);
            md.setS(us);
            map_data.put(uuid, md);
        } else {
            setLore(inv, slot, "Load map data first!");
        }
    }

    /**
     * Checks whether a player has condensed the required blocks to grow the
     * room (s).
     *
     * @param uuid the UUID of the player to check for
     * @param map a HashMap where the key is the changed room slot and the value
     * is the ARS room type
     * @param id the TARDIS id
     * @return true or false
     */
    public boolean hasCondensables(String uuid, HashMap<TARDISARSSlot, ARS> map, int id) {
        boolean hasRequired = true;
        String wall = "ORANGE_WOOL";
        String floor = "LIGHT_GREY_WOOL";
        HashMap<String, Object> wherepp = new HashMap<>();
        boolean hasPrefs = false;
        wherepp.put("uuid", uuid);
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
        if (rsp.resultSet()) {
            hasPrefs = true;
            wall = rsp.getWall();
            floor = rsp.getFloor();
        }
        HashMap<String, Integer> item_counts = new HashMap<>();
        for (Map.Entry<TARDISARSSlot, ARS> rooms : map.entrySet()) {
            HashMap<String, Integer> roomBlocks = plugin.getBuildKeeper().getRoomBlockCounts().get(rooms.getValue().getActualName());
            for (Map.Entry<String, Integer> entry : roomBlocks.entrySet()) {
                String[] block_data = entry.getKey().split(":");
                String bid = block_data[0];
                String mat;
                String bkey;
                if (hasPrefs && block_data.length == 2 && (block_data[1].equals("1") || block_data[1].equals("8"))) {
                    mat = (block_data[1].equals("1")) ? wall : floor;
                    Pair iddata = plugin.getTardisWalls().blocks.get(mat);
                    bkey = iddata.getType().toString();
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

    public int getTardisId(String uuid) {
        int id = 0;
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        ResultSetTravellers rs = new ResultSetTravellers(plugin, where, false);
        if (rs.resultSet()) {
            id = rs.getTardis_id();
        }
        return id;
    }

    public boolean hasRenderer(UUID uuid) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", ids.get(uuid));
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (rs.resultSet()) {
            return !rs.getTardis().getRenderer().isEmpty();
        }
        return false;
    }

    public boolean checkSlotForConsole(Inventory inv, int slot, String uuid) {
        Material m = inv.getItem(slot).getType();
        if (m.equals(Material.NETHER_BRICK)) {
            // allow only if console is not MASTER
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (rs.resultSet() && !rs.getTardis().getSchematic().getSeed().equals("NETHER_BRICK")) {
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

    public boolean playerIsOwner(String uuid, int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("uuid", uuid);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        return rs.resultSet();
    }
}
