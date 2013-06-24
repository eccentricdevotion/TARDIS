/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.listeners;

import java.util.Arrays;
import me.eccentric_nz.TARDIS.rooms.TARDISARSMapData;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetARS;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.JSONArray;

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
    HashMap<String, Block> trackPosi = new HashMap<String, Block>();
    private ItemStack[] items;
    private int[] room_ids = new int[]{82, 18, 89, 86, 47, 80, 23, 112, 20, 48, 24, 109, 121, 5, 3, 88, 103, 25, 13};
    private String[] room_names = new String[]{"Passage", "Arboretum", "Bedroom", "Kitchen", "Library", "Pool", "Vault", "Workshop", "Empty", "Gravity", "Antigravity", "Harmony", "Baker", "Wood", "Farm", "Cross", "Greenhouse", "Long", "Mushroom"};
    private HashMap<String, Integer> scroll_start = new HashMap<String, Integer>();
    private HashMap<String, Integer> selected_slot = new HashMap<String, Integer>();
    private HashMap<String, TARDISARSMapData> map_data = new HashMap<String, TARDISARSMapData>();
    private HashMap<Integer, String> name_map = new HashMap<Integer, String>();
    private String[] levels = new String[]{"Bottom level", "Main level", "Top level"};

    public TARDISARSListener(TARDIS plugin) {
        this.plugin = plugin;
        this.name_map = new HashMap<Integer, String>();
        this.name_map.put(1, "Empty slot");
        this.name_map.put(3, "Farm");
        this.name_map.put(5, "Wood");
        this.name_map.put(13, "Mushroom");
        this.name_map.put(18, "Arboretum");
        this.name_map.put(20, "Empty");
        this.name_map.put(23, "Vault");
        this.name_map.put(24, "Antigravity");
        this.name_map.put(25, "Long");
        this.name_map.put(47, "Library");
        this.name_map.put(48, "Gravity");
        this.name_map.put(80, "Pool");
        this.name_map.put(82, "Passage");
        this.name_map.put(86, "Kitchen");
        this.name_map.put(88, "Cross");
        this.name_map.put(89, "Bedroom");
        this.name_map.put(103, "Greenhouse");
        this.name_map.put(109, "Harmony");
        this.name_map.put(112, "Workshop");
        this.name_map.put(121, "Baker");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onTerminalClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4Architectural Reconfiguration")) {
            final Player player = (Player) event.getWhoClicked();
            String playerNameStr = player.getName();
            int slot = event.getRawSlot();
            switch (slot) {
                case 1:
                case 9:
                case 11:
                case 19:
                    // up
                    moveMap(playerNameStr, inv, slot);
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
                    // select slot
                    selected_slot.put(playerNameStr, slot);
                    break;
                case 10:
                    // load map
                    loadMap(inv, playerNameStr);
                    break;
                case 12:
                    // reconfigure
                    close(player);
                    break;
                case 27:
                case 28:
                case 29:
                    // top level
                    if (map_data.containsKey(playerNameStr)) {
                        switchLevel(inv, slot, playerNameStr);
                        TARDISARSMapData md = map_data.get(playerNameStr);
                        setMap(md.getY(), md.getE(), md.getS(), playerNameStr, inv);
                        setLore(inv, slot, null);
                    } else {
                        setLore(inv, slot, "Load map data first!");
                    }
                    break;
                case 30:
                    // reset selected slot to empty
                    if (selected_slot.containsKey(playerNameStr)) {
                        ItemStack stone = new ItemStack(1, 1);
                        ItemMeta s1 = stone.getItemMeta();
                        s1.setDisplayName("Empty slot");
                        stone.setItemMeta(s1);
                        setSlot(inv, selected_slot.get(playerNameStr), stone, playerNameStr, true);
                        setLore(inv, slot, null);
                    } else {
                        setLore(inv, slot, "No slot selected!");
                    }
                    break;
                case 36:
                    // scroll left
                    int startl;
                    int max = room_ids.length - 9;
                    if (scroll_start.containsKey(playerNameStr)) {
                        startl = scroll_start.get(playerNameStr) + 1;
                        if (startl >= max) {
                            startl = max;
                        }
                    } else {
                        startl = 1;
                    }
                    scroll_start.put(playerNameStr, startl);
                    for (int i = 0; i < 9; i++) {
                        // setSlot(Inventory inv, int slot, int id, String room)
                        setSlot(inv, (45 + i), room_ids[(startl + i)], room_names[(startl + i)], playerNameStr, false);
                    }
                    break;
                case 38:
                    // scroll right
                    int startr;
                    if (scroll_start.containsKey(playerNameStr)) {
                        startr = scroll_start.get(playerNameStr) - 1;
                        if (startr <= 0) {
                            startr = 0;
                        }
                    } else {
                        startr = 0;
                    }
                    scroll_start.put(playerNameStr, startr);

                    for (int i = 0; i < 9; i++) {
                        // setSlot(Inventory inv, int slot, int id, String room)
                        setSlot(inv, (45 + i), room_ids[(startr + i)], room_names[(startr + i)], playerNameStr, false);
                    }
                    break;
                case 39:
                    // jettison
                    if (selected_slot.containsKey(playerNameStr)) {
                        ItemStack tnt = new ItemStack(46, 1);
                        ItemMeta j = tnt.getItemMeta();
                        j.setDisplayName("Jettison");
                        tnt.setItemMeta(j);
                        setSlot(inv, selected_slot.get(playerNameStr), tnt, playerNameStr, true);
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
                    if (selected_slot.containsKey(playerNameStr)) {
                        ItemStack ris = inv.getItem(slot);
                        // setSlot(Inventory inv, int slot, ItemStack is)
                        setSlot(inv, selected_slot.get(playerNameStr), ris, playerNameStr, true);
                        setLore(inv, slot, null);

                    } else {
                        setLore(inv, slot, "No slot selected!");
                    }
                    break;
                default:
                    break;
            }
            event.setCancelled(true);
        }
    }

    private void saveAll(String p) {
        TARDISARSMapData md = map_data.get(p);
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

    private int[][][] getGridFronJSON(String js) {
        int[][][] grid = new int[3][9][9];
        JSONArray json = new JSONArray(js);
        for (int y = 0; y < 3; y++) {
            JSONArray jsonx = json.getJSONArray(y);
            for (int x = 0; x < 9; x++) {
                JSONArray jsonz = jsonx.getJSONArray(x);
                for (int z = 0; z < 9; z++) {
                    grid[y][x][z] = jsonz.getInt(z);
                }
            }
        }
        return grid;
    }

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

    private void setSlot(Inventory inv, int slot, int id, String room, String p, boolean update) {
        ItemStack is = new ItemStack(id, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(room);
        im.setLore(null);
        is.setItemMeta(im);
        inv.setItem(slot, is);
        if (update) {
            updateGrid(p, slot, id);
        }
    }

    private void setSlot(Inventory inv, int slot, ItemStack is, String p, boolean update) {
        inv.setItem(slot, is);
        int id = is.getTypeId();
        if (update) {
            updateGrid(p, slot, id);
        }
    }

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

    private void updateGrid(String p, int slot, int id) {
        TARDISARSMapData md = map_data.get(p);
        int[][][] grid = md.getData();
        int yy = md.getY();
        int[] coords = getCoords(slot, md);
        int newx = coords[0];
        int newz = coords[1];
        grid[yy][newx][newz] = id;
        md.setData(grid);
        map_data.put(p, md);
    }

    private void setLore(Inventory inv, int slot, String str) {
        List<String> lore = (str != null) ? Arrays.asList(new String[]{str}) : null;
        ItemStack is = inv.getItem(slot);
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);
    }

    private void switchLevel(Inventory inv, int slot, String p) {
        TARDISARSMapData md = map_data.get(p);
        for (int i = 27; i < 30; i++) {
            byte data = 0;
            if (i == slot) {
                data = 4;
                md.setY(i - 27);
                map_data.put(p, md);
            }
            ItemStack is = new ItemStack(35, 1, data);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(levels[i - 27]);
            is.setItemMeta(im);
            setSlot(inv, i, is, p, false);
        }
    }

    private void close(final Player p) {
        final String n = p.getName();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (scroll_start.containsKey(n)) {
                    scroll_start.remove(n);
                }
                if (selected_slot.containsKey(n)) {
                    selected_slot.remove(n);
                }
                if (map_data.containsKey(n)) {
                    saveAll(n);
                    p.sendMessage(plugin.pluginName + "Architectural reconfiguration starting...");
                    map_data.remove(n);
                }
                p.closeInventory();
            }
        }, 1L);
    }

    private void loadMap(Inventory inv, String player) {
        if (inv.getItem(10).getItemMeta().hasLore()) {
            setLore(inv, 10, "Map already loaded!");
            return;
        }
        setLore(inv, 10, "Loading...");
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("player", player);
        ResultSetARS rs = new ResultSetARS(plugin, where);
        if (rs.resultSet()) {
            TARDISARSMapData md = new TARDISARSMapData();
            md.setData(getGridFronJSON(rs.getJson()));
            md.setE(rs.getEast());
            md.setS(rs.getSouth());
            md.setY(rs.getLayer());
            md.setId(rs.getId());
            map_data.put(player, md);
            setMap(rs.getLayer(), rs.getEast(), rs.getSouth(), player, inv);
            setLore(inv, 10, "Map LOADED");
            switchLevel(inv, (27 + rs.getLayer()), player);
        }
    }

    private void setMap(int ul, int ue, int us, String player, Inventory inv) {
        TARDISARSMapData data = map_data.get(player);
        int[][][] grid = data.getData();
        int[][] layer = grid[ul];
        int[][] map = sliceGrid(layer, ue, us);
        int indexx = 0, indexz = 0;
        for (int i = 4; i < 9; i++) {
            for (int j = 0; j < 5; j++) {
                int slot = i + (j * 9);
                int id = map[indexx][indexz];
                String name = name_map.get(Integer.valueOf(id));
                setSlot(inv, slot, id, name, player, false);
                indexz++;
            }
            indexz = 0;
            indexx++;
        }
    }

    private void moveMap(String p, Inventory inv, int slot) {
        if (map_data.containsKey(p)) {
            TARDISARSMapData md = map_data.get(p);
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
            setMap(md.getY(), ue, us, p, inv);
            setLore(inv, slot, null);
            md.setE(ue);
            md.setS(us);
            map_data.put(p, md);
        } else {
            setLore(inv, slot, "Load map data first!");
        }
    }
}
