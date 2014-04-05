/*
 * Copyright (C) 2014 eccentric_nz
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
import static me.eccentric_nz.TARDIS.commands.preferences.TARDISPrefsCommands.ucfirst;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetARS;
import me.eccentric_nz.TARDIS.database.ResultSetCondenser;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
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
public class TARDISARSListener implements Listener {

    private final TARDIS plugin;
    private int[] room_ids;
    private String[] room_names;
    private final HashMap<UUID, Integer> scroll_start = new HashMap<UUID, Integer>();
    private final HashMap<UUID, Integer> selected_slot = new HashMap<UUID, Integer>();
    private final HashMap<UUID, TARDISARSSaveData> save_map_data = new HashMap<UUID, TARDISARSSaveData>();
    private final HashMap<UUID, TARDISARSMapData> map_data = new HashMap<UUID, TARDISARSMapData>();
    private final String[] levels = new String[]{"Bottom level", "Main level", "Top level"};
    private final List<TARDISARS> notrooms;
    private final List<Material> consoleBlocks;
    private final HashMap<UUID, Integer> ids = new HashMap<UUID, Integer>();
    private final List<UUID> hasLoadedMap = new ArrayList<UUID>();

    public TARDISARSListener(TARDIS plugin) {
        this.plugin = plugin;
        this.notrooms = Arrays.asList(TARDISARS.ARS, TARDISARS.BIGGER, TARDISARS.BUDGET, TARDISARS.DELUXE, TARDISARS.ELEVENTH, TARDISARS.JETTISON, TARDISARS.PLANK, TARDISARS.REDSTONE, TARDISARS.SLOT, TARDISARS.STEAMPUNK, TARDISARS.TOM);
        getRoomIdAndNames();
        this.consoleBlocks = Arrays.asList(Material.IRON_BLOCK, Material.GOLD_BLOCK, Material.DIAMOND_BLOCK, Material.EMERALD_BLOCK, Material.REDSTONE_BLOCK, Material.COAL_BLOCK, Material.QUARTZ_BLOCK, Material.LAPIS_BLOCK, Material.BOOKSHELF);
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a
     * TARDIS GUI, then the click is processed accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onARSTerminalClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4Architectural Reconfiguration")) {
            event.setCancelled(true);
            final Player player = (Player) event.getWhoClicked();
            UUID uuid = player.getUniqueId();
            ids.put(uuid, getTardisId(player.getUniqueId().toString(), player.isOp()));
            int slot = event.getRawSlot();
            if (slot != 10 && !hasLoadedMap.contains(uuid)) {
                TARDISMessage.send(player, plugin.getPluginName() + "You need to load the map first!");
                return;
            }
            if (slot >= 0 && slot < 54) {
                switch (slot) {
                    case 1:
                    case 9:
                    case 11:
                    case 19:
                        // up
                        moveMap(uuid, inv, slot);
                        break;
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case 31:
                    case 32:
                    case 33:
                    case 34:
                    case 35:
                    case 40:
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                        if (!checkSlotForConsole(inv, slot)) {
                            // select slot
                            selected_slot.put(uuid, slot);
                        }
                        break;
                    case 10:
                        // load map
                        loadMap(inv, uuid);
                        break;
                    case 12:
                        // reconfigure
                        close(player);
                        break;
                    case 27:
                    case 28:
                    case 29:
                        // top level
                        if (map_data.containsKey(uuid)) {
                            switchLevel(inv, slot, uuid);
                            TARDISARSMapData md = map_data.get(uuid);
                            setMap(md.getY(), md.getE(), md.getS(), uuid, inv);
                            setLore(inv, slot, null);
                        } else {
                            setLore(inv, slot, "Load map data first!");
                        }
                        break;
                    case 30:
                        // reset selected slot to empty
                        if (selected_slot.containsKey(uuid)) {
                            // check whether original loaded slot was a room - as it will need to be jettisoned, not reset
                            if (checkSavedGrid(uuid, selected_slot.get(uuid), 0)) {
                                setLore(inv, slot, "You cannot reset the selected slot!");
                                break;
                            } else {
                                ItemStack stone = new ItemStack(Material.STONE, 1);
                                ItemMeta s1 = stone.getItemMeta();
                                s1.setDisplayName("Empty slot");
                                stone.setItemMeta(s1);
                                setSlot(inv, selected_slot.get(uuid), stone, uuid, true);
                                setLore(inv, slot, null);
                            }
                        } else {
                            setLore(inv, slot, "No slot selected!");
                        }
                        break;
                    case 36:
                        // scroll left
                        int startl;
                        int max = room_ids.length - 9;
                        if (scroll_start.containsKey(uuid)) {
                            startl = scroll_start.get(uuid) + 1;
                            if (startl >= max) {
                                startl = max;
                            }
                        } else {
                            startl = 1;
                        }
                        scroll_start.put(uuid, startl);
                        for (int i = 0; i < 9; i++) {
                            // setSlot(Inventory inv, int slot, int id, String room)
                            setSlot(inv, (45 + i), room_ids[(startl + i)], room_names[(startl + i)], uuid, false);
                        }
                        break;
                    case 38:
                        // scroll right
                        int startr;
                        if (scroll_start.containsKey(uuid)) {
                            startr = scroll_start.get(uuid) - 1;
                            if (startr <= 0) {
                                startr = 0;
                            }
                        } else {
                            startr = 0;
                        }
                        scroll_start.put(uuid, startr);
                        for (int i = 0; i < 9; i++) {
                            // setSlot(Inventory inv, int slot, int id, String room)
                            setSlot(inv, (45 + i), room_ids[(startr + i)], room_names[(startr + i)], uuid, false);
                        }
                        break;
                    case 39:
                        // jettison
                        if (selected_slot.containsKey(uuid)) {
                            // need to check for gravity wells, and jettison both layers...
                            ItemStack tnt = new ItemStack(Material.TNT, 1);
                            ItemMeta j = tnt.getItemMeta();
                            j.setDisplayName("Jettison");
                            tnt.setItemMeta(j);
                            setSlot(inv, selected_slot.get(uuid), tnt, uuid, true);
                            setLore(inv, slot, null);
                        } else {
                            setLore(inv, slot, "No slot selected!");
                        }
                        break;
                    case 45:
                    case 46:
                    case 47:
                    case 48:
                    case 49:
                    case 50:
                    case 51:
                    case 52:
                    case 53:
                        // put room in selected slot
                        if (selected_slot.containsKey(uuid)) {
                            // check whether original loaded slot was a room - as it will need to be jettisoned, not reset
                            if (checkSavedGrid(uuid, selected_slot.get(uuid), 0)) {
                                setLore(inv, slot, "Jettison existing room first!");
                                break;
                            } else {
                                ItemStack ris = inv.getItem(slot);
                                String displayName = ris.getItemMeta().getDisplayName();
                                String room;
//                                if (displayName.startsWith("§3(Custom)§r ")) {
//                                    room = displayName.substring(13);
//                                } else {
                                room = TARDISARS.ARSFor(displayName).getActualName();
//                                }
                                if (!player.hasPermission("tardis.room." + room.toLowerCase())) {
                                    setLore(inv, slot, "You don't have permission for this room!");
                                    break;
                                }
                                if (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) {
                                    int updown = (room.equals("Gravity Well")) ? -1 : 1;
                                    if (checkSavedGrid(uuid, selected_slot.get(uuid), updown)) {
                                        setLore(inv, slot, "Using a gravity well here would overwrite an existing room!");
                                        break;
                                    }
                                }
                                if (plugin.getConfig().getBoolean("growth.rooms_require_blocks")) {
                                    if (!hasCondensables(player.getUniqueId().toString(), room, ids.get(uuid))) {
                                        setLore(inv, slot, "You haven't condensed enough blocks for this room!");
                                        break;
                                    }
                                }
                                if (room.equals("RENDERER") && hasRenderer(uuid)) {
                                    setLore(inv, slot, "You already have one of these!");
                                    break;
                                }
                                // setSlot(Inventory inv, int slot, ItemStack is, String player, boolean update)
                                setSlot(inv, selected_slot.get(uuid), ris, uuid, true);
                                setLore(inv, slot, null);
                            }
                        } else {
                            setLore(inv, slot, "No slot selected!");
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * Saves the current ARS data to the database.
     *
     * @param uuid the UUID of the player who is using the ARS GUI
     */
    private void saveAll(UUID uuid) {
        TARDISARSMapData md = map_data.get(uuid);
        JSONArray json = new JSONArray(md.getData());
        HashMap<String, Object> set = new HashMap<String, Object>();
        set.put("ars_x_east", md.getE());
        set.put("ars_z_south", md.getS());
        set.put("ars_y_layer", md.getY());
        set.put("json", json.toString());
        HashMap<String, Object> wherea = new HashMap<String, Object>();
        wherea.put("ars_id", md.getId());
        new QueryFactory(plugin).doUpdate("ars", set, wherea);
    }

    /**
     * Saves the current ARS data to the database.
     *
     * @param uuid the UUID of the player who is using the ARS GUI
     */
    private void revert(UUID uuid) {
        TARDISARSSaveData sd = save_map_data.get(uuid);
        JSONArray json = new JSONArray(sd.getData());
        HashMap<String, Object> set = new HashMap<String, Object>();
        set.put("json", json.toString());
        HashMap<String, Object> wherea = new HashMap<String, Object>();
        wherea.put("ars_id", sd.getId());
        new QueryFactory(plugin).doUpdate("ars", set, wherea);
    }

    /**
     * Converts the JSON data stored in the database to a 3D array.
     *
     * @param js the JSON from the database
     * @return a 3D array of ints
     */
    private int[][][] getGridFromJSON(String js) {
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
    private int[][] sliceGrid(int[][] layer, int x, int z) {
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
    private void setSlot(Inventory inv, int slot, int id, String room, UUID uuid, boolean update) {
        ItemStack is = new ItemStack(id, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(room);
        im.setLore(null);
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
    private void setSlot(Inventory inv, int slot, ItemStack is, UUID uuid, boolean update) {
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
    private int[] getCoords(int slot, TARDISARSMapData md) {
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
     * Checks the saved map to see whether the selected slot can be reset.
     *
     * @param uuid the UUID of the player using the GUI
     * @param slot the slot that was clicked
     * @param id the type id of the block in the slot
     */
    private boolean checkSavedGrid(UUID uuid, int slot, int updown) {
        TARDISARSMapData md = map_data.get(uuid);
        TARDISARSSaveData sd = save_map_data.get(uuid);
        int[][][] grid = sd.getData();
        int yy = md.getY() + updown;
        // avoid ArrayIndexOutOfBoundsException if gravity well extends beyond ARS area
        if (yy < 0 || yy > 2) {
            return true;
        }
        int[] coords = getCoords(slot, md);
        int xx = coords[0];
        int zz = coords[1];
        int prior = grid[yy][xx][zz];
        for (int i : room_ids) {
            if (prior == i) {
                return true;
            }
        }
        return false;
    }

    /**
     * Saves the current map to the TARDISARSMapData instance associated with
     * the player using the GUI.
     *
     * @param uuid the UUID of the player using the GUI
     * @param slot the slot that was clicked
     * @param id the type id of the block in the slot
     */
    private void updateGrid(UUID uuid, int slot, int id) {
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
    private void setLore(Inventory inv, int slot, String str) {
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
    private void switchLevel(Inventory inv, int slot, UUID uuid) {
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
    private void close(final Player p) {
        final UUID uuid = p.getUniqueId();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
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
                            TARDISMessage.send(p, plugin.getPluginName() + "Architectural reconfiguration starting...");
                            // do all jettisons first
                            if (tap.getJettison().size() > 0) {
                                TARDISMessage.send(p, plugin.getPluginName() + "Jettisoning " + tap.getJettison().size() + " rooms...");
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
                        } else {
                            TARDISMessage.send(p, plugin.getPluginName() + tap.getError());
                            // reset map to the previous version
                            revert(uuid);
                        }
                    } else {
                        TARDISMessage.send(p, plugin.getPluginName() + "Only the Timelord of this TARDIS can reconfigure rooms!");
                        revert(uuid);
                    }
                    map_data.remove(uuid);
                    save_map_data.remove(uuid);
                }
                p.closeInventory();
            }
        }, 1L);
    }

    /**
     * Loads the map from the database ready for use in the GUI.
     *
     * @param inv the inventory to load the map into
     * @param p the UUID of the player using the GUI
     */
    private void loadMap(Inventory inv, UUID uuid) {
        if (inv.getItem(10).getItemMeta().hasLore()) {
            setLore(inv, 10, "Map already loaded!");
            return;
        }
        setLore(inv, 10, "Loading...");
        HashMap<String, Object> where = new HashMap<String, Object>();
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

    private void setMap(int ul, int ue, int us, UUID uuid, Inventory inv) {
        TARDISARSMapData data = map_data.get(uuid);
        int[][][] grid = data.getData();
        int[][] layer = grid[ul];
        int[][] map = sliceGrid(layer, ue, us);
        int indexx = 0, indexz = 0;
        for (int i = 4; i < 9; i++) {
            for (int j = 0; j < 5; j++) {
                int slot = i + (j * 9);
                int id = map[indexx][indexz];
                String name = TARDISARS.ARSFor(Integer.valueOf(id)).getDescriptiveName();
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
    private void moveMap(UUID uuid, Inventory inv, int slot) {
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
     * Populates arrays of room names and seed IDs for the scrollable room
     * buttons.
     */
    @SuppressWarnings("deprecation")
    private void getRoomIdAndNames() {
        List<String> custom_names = getCustomRoomNames();
        TARDISARS[] ars = TARDISARS.values();
        // less non-room types
        int l = (custom_names.size() + ars.length) - notrooms.size();
//        int l = ars.length - notrooms.size();
        this.room_ids = new int[l];
        this.room_names = new String[l];
        int i = 0;
        for (TARDISARS a : ars) {
            if (!notrooms.contains(a)) {
                this.room_ids[i] = a.getId();
                this.room_names[i] = a.getDescriptiveName();
                i++;
            }
        }
        for (final String c : custom_names) {
            this.room_ids[i] = Material.valueOf(plugin.getRoomsConfig().getString("rooms." + c + ".seed")).getId();
            final String uc = ucfirst(c);
            this.room_names[i] = uc;
            i++;
            TARDISARS.addNewARS(new ARS() {
                @Override
                public int getId() {
                    return Material.valueOf(plugin.getRoomsConfig().getString("rooms." + c + ".seed")).getId();
                }

                @Override
                public String getActualName() {
                    return c;
                }

                @Override
                public String getDescriptiveName() {
                    return uc;
                }

                @Override
                public int getOffset() {
                    return 1;
                }
            });
        }
    }

    /**
     * Checks and gets custom rooms for ARS.
     *
     * @return a list of enabled custom room names
     */
    private List<String> getCustomRoomNames() {
        List<String> crooms = new ArrayList<String>();
        Set<String> names = plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false);
        for (String cr : names) {
            if (plugin.getRoomsConfig().getBoolean("rooms." + cr + ".user") && plugin.getRoomsConfig().getBoolean("rooms." + cr + ".enabled")) {
                // check room dimensions
                short[] dim = plugin.getBuildKeeper().getRoomDimensions().get(cr);
                if (dim[0] <= (short) 16 && dim[1] == (short) 16) {
                    crooms.add(cr);
                }
            }
        }
        return crooms;
    }

    /**
     * Checks whether a player has condensed the required blocks to grow the
     * room.
     *
     * @param uuid the UUID of the player to check for
     * @param room the room to check
     * @param id the TARDIS id
     * @return true or false
     */
    public boolean hasCondensables(String uuid, String room, int id) {
        boolean hasRequired = true;
        HashMap<String, Integer> roomBlocks = plugin.getBuildKeeper().getRoomBlockCounts().get(room);
        String wall = "ORANGE_WOOL";
        String floor = "LIGHT_GREY_WOOL";
        HashMap<String, Object> wherepp = new HashMap<String, Object>();
        boolean hasPrefs = false;
        wherepp.put("uuid", uuid);
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
        if (rsp.resultSet()) {
            hasPrefs = true;
            wall = rsp.getWall();
            floor = rsp.getFloor();
        }
        HashMap<Integer, Integer> item_counts = new HashMap<Integer, Integer>();
        for (Map.Entry<String, Integer> entry : roomBlocks.entrySet()) {
            String[] block_data = entry.getKey().split(":");
            int bid = plugin.getUtils().parseInt(block_data[0]);
            String mat;
            int bdata;
            if (hasPrefs && block_data.length == 2 && (block_data[1].equals("1") || block_data[1].equals("8"))) {
                mat = (block_data[1].equals("1")) ? wall : floor;
                int[] iddata = plugin.getTardisWalls().blocks.get(mat);
                bdata = iddata[0];
            } else {
                bdata = bid;
            }
            int tmp = Math.round((entry.getValue() / 100.0F) * plugin.getConfig().getInt("growth.rooms_condenser_percent"));
            int required = (tmp > 0) ? tmp : 1;
            if (item_counts.containsKey(bdata)) {
                item_counts.put(bdata, (item_counts.get(bdata) + required));
            } else {
                item_counts.put(bdata, required);
            }
        }
        for (Map.Entry<Integer, Integer> map : item_counts.entrySet()) {
            HashMap<String, Object> wherec = new HashMap<String, Object>();
            wherec.put("tardis_id", id);
            wherec.put("block_data", map.getKey());
            ResultSetCondenser rsc = new ResultSetCondenser(plugin, wherec, false);
            if (rsc.resultSet()) {
                if (rsc.getBlock_count() < map.getValue()) {
                    hasRequired = false;
                }
            } else {
                hasRequired = false;
            }
        }
        return hasRequired;
    }

    private int getTardisId(String uuid, boolean isOP) {
        int id = 0;
        HashMap<String, Object> where = new HashMap<String, Object>();
        if (isOP) {
            where.put("uuid", uuid);
            ResultSetTravellers rs = new ResultSetTravellers(plugin, where, false);
            if (rs.resultSet()) {
                id = rs.getTardis_id();
            }
        } else {
            where.put("uuid", uuid);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (rs.resultSet()) {
                id = rs.getTardis_id();
            }
        }
        return id;
    }

    private boolean hasRenderer(UUID uuid) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", ids.get(uuid));
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            return !rs.getRenderer().isEmpty();
        }
        return false;
    }

    private boolean checkSlotForConsole(Inventory inv, int slot) {
        Material m = inv.getItem(slot).getType();
        return (consoleBlocks.contains(m));
    }

    private boolean playerIsOwner(String uuid, int id) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        where.put("uuid", uuid);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        return rs.resultSet();
    }
}
