/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.rooms.loader;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.ARS.TARDISARS;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.interior.TARDISInteriorPositioning;
import me.eccentric_nz.TARDIS.builders.interior.TIPSData;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetARSWithTIPS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetChunkTickets;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTIPS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class TicketMethods {

    public final HashMap<UUID, TicketData> ticketData = new HashMap<>();
    public final HashMap<UUID, Integer> ids = new HashMap<>();
    public final List<UUID> hasLoadedMap = new ArrayList<>();
    protected final TARDIS plugin;
    private final String[] levels = new String[]{"Bottom level", "Main level", "Top level"};

    public TicketMethods(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Converts the JSON data stored in the database to a 3D array.
     *
     * @param js the JSON from the database
     * @return a 3D array of Strings
     */
    public static ItemStack[][][] getGridFromJSON(String js, List<Ticket> tickets, TIPSData coords) {
        int cx = (coords.getCentreX() >> 4) - 4; // chunk x coord at [0][0][0] in ARS grid
        int cz = (coords.getCentreZ() >> 4) - 4; // chunk z coord at [0][0][0] in ARS grid
        ItemStack[][][] grid = new ItemStack[3][9][9];
        JsonArray json = JsonParser.parseString(js).getAsJsonArray();
        for (int y = 0; y < 3; y++) {
            JsonArray jsonx = json.get(y).getAsJsonArray();
            for (int x = 0; x < 9; x++) {
                JsonArray jsonz = jsonx.get(x).getAsJsonArray();
                for (int z = 0; z < 9; z++) {
                    if (jsonz.get(z).getAsString().equals("TNT")) {
                        grid[y][x][z] = ItemStack.of(Material.valueOf("STONE"));
                    } else {
                        int gx = cx + x;
                        int gz = cz + z;
                        boolean hasTicket = false;
                        for (Ticket ticket : tickets) {
                            if (ticket.x() == gx && ticket.z() == gz) {
                                hasTicket = true;
                                break;
                            }
                        }
                        ItemStack is = ItemStack.of(hasTicket ?
                                Material.NAME_TAG :
                                Material.valueOf(jsonz.get(z).getAsString())
                        );
                        String name = TARDISARS.ARSFor(jsonz.get(z).getAsString()).getDescriptiveName();
                        is.setData(DataComponentTypes.CUSTOM_NAME, Component.text(name));
                        is.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(hasTicket ? "Loaded" : "Not loaded")).build());
                        grid[y][x][z] = is;
                    }
                }
            }
        }
        return grid;
    }

    /**
     * Gets a 5x5 2D slice from a 3D array
     *
     * @param layer the level to get
     * @param x     the x position of the slice
     * @param z     the z position of the slice
     * @return a slice of the larger array
     */
    private ItemStack[][] sliceGrid(ItemStack[][] layer, int x, int z) {
        ItemStack[][] slice = new ItemStack[5][5];
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
     * Sets an ItemStack to the specified inventory slot.
     *
     * @param view       the inventory to update
     * @param slot       the slot number to update
     * @param is         the item stack to set
     * @param playerUUID the player using the GUI
     * @param update     whether to update the grid display
     */
    public void setSlot(InventoryView view, int slot, ItemStack is, UUID playerUUID, boolean update) {
        view.setItem(slot, is);
        if (update) {
            updateTickets(playerUUID, slot, is);
        }
    }

    /**
     * Get the coordinates of the clicked slot in relation to the ARS map.
     *
     * @param slot the slot that was clicked
     * @param td   an instance of the TicketData class from which to retrieve the map offset
     * @return an array of ints
     */
    int[] getCoords(int slot, TicketData td) {
        int[] coords = new int[2];
        if (slot <= 8) {
            coords[0] = (slot - 4) + td.getE();
            coords[1] = td.getS();
        }
        if (slot > 8 && slot <= 17) {
            coords[0] = (slot - 13) + td.getE();
            coords[1] = td.getS() + 1;
        }
        if (slot > 17 && slot <= 26) {
            coords[0] = (slot - 22) + td.getE();
            coords[1] = td.getS() + 2;
        }
        if (slot > 26 && slot <= 35) {
            coords[0] = (slot - 31) + td.getE();
            coords[1] = td.getS() + 3;
        }
        if (slot > 35 && slot <= 44) {
            coords[0] = (slot - 40) + td.getE();
            coords[1] = td.getS() + 4;
        }
        return coords;
    }

    /**
     * Saves the current map to the TicketData instance associated with the player using the GUI.
     *
     * @param playerUUID the UUID of the player using the GUI
     * @param slot       the slot that was clicked
     * @param is         the item stack in the slot
     */
    public void updateTickets(UUID playerUUID, int slot, ItemStack is) {
        TicketData td = ticketData.get(playerUUID);
        int yy = td.getY();
        ItemStack[][][] grid = td.getData();
        int[] coords = getCoords(slot, td);
        int newx = coords[0];
        int newz = coords[1];
        grid[yy][newx][newz] = is;
        int[] otherYs = getYs(yy);
        ItemStack otherOne = grid[otherYs[0]][newx][newz];
        ItemStack otherTwo = grid[otherYs[1]][newx][newz];
        if (is.getType() == Material.NAME_TAG) {
            // set all columns to name tag as chunk runs through all three rooms
            ItemLore lore = ItemLore.lore().addLine(Component.text("Loaded")).build();
            ItemStack one = otherOne.withType(Material.NAME_TAG);
            one.setData(DataComponentTypes.LORE, lore);
            ItemStack two = otherTwo.withType(Material.NAME_TAG);
            one.setData(DataComponentTypes.LORE, lore);
            grid[otherYs[0]][newx][newz] = one;
            grid[otherYs[1]][newx][newz] = two;
        } else {
            // set type and lore for other item stacks
            ItemLore lore = ItemLore.lore().addLine(Component.text("Not loaded")).build();
            String otherOneName = ComponentUtils.stripColour(otherOne.getData(DataComponentTypes.CUSTOM_NAME));
            if (otherOneName.equals("Empty slot")) {
                otherOneName = "SLOT";
            }
            TARDISARS ars1 = TARDISARS.valueOf(otherOneName.toUpperCase(Locale.ROOT));
            ItemStack one = otherOne.withType(Material.valueOf(ars1.getMaterial()));
            one.setData(DataComponentTypes.LORE, lore);
            grid[otherYs[0]][newx][newz] = one;
            String otherTwoName = ComponentUtils.stripColour(otherTwo.getData(DataComponentTypes.CUSTOM_NAME));
            if (otherTwoName.equals("Empty slot")) {
                otherTwoName = "SLOT";
            }
            TARDISARS ars2 = TARDISARS.valueOf(otherTwoName.toUpperCase(Locale.ROOT));
            ItemStack two = otherTwo.withType(Material.valueOf(ars2.getMaterial()));
            one.setData(DataComponentTypes.LORE, lore);
            grid[otherYs[1]][newx][newz] = two;
        }
        td.setData(grid);
        ticketData.put(playerUUID, td);
    }

    private int[] getYs(int yy) {
        int[] ys;
        if (yy == 0) {
            ys = new int[]{1, 2};
        } else if (yy == 1) {
            ys = new int[]{0, 2};
        } else {
            ys = new int[]{0, 1};
        }
        return ys;
    }

    /**
     * Sets the lore of the ItemStack in the specified slot.
     *
     * @param view the inventory to update
     * @param slot the slot to update
     * @param str  the lore to set
     */
    public void setLore(InventoryView view, int slot, String str) {
        ItemStack is = view.getItem(slot);
        if (is != null) {
            if (str != null) {
                is.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(str)).build());
            } else {
                is.resetData(DataComponentTypes.LORE);
            }
        }
    }

    /**
     * Switches the indicator block for the map level.
     *
     * @param view       the inventory to update
     * @param slot       the slot to update
     * @param playerUUID the UUID of the player using the GUI
     */
    public void switchLevel(InventoryView view, int slot, UUID playerUUID) {
        TicketData td = ticketData.get(playerUUID);
        for (int i = 27; i < 30; i++) {
            Material material = Material.WHITE_WOOL;
            if (i == slot) {
                material = Material.YELLOW_WOOL;
                td.setY(i - 27);
                ticketData.put(playerUUID, td);
            }
            ItemStack is = ItemStack.of(material, 1);
            is.setData(DataComponentTypes.CUSTOM_NAME, Component.text(levels[i - 27]));
            setSlot(view, i, is, playerUUID, false);
        }
    }

    /**
     * Closes the inventory.
     *
     * @param player the player using the GUI
     */
    public void close(Player player) {
        UUID playerUUID = player.getUniqueId();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            hasLoadedMap.remove(playerUUID);
            if (ticketData.containsKey(playerUUID)) {
                ticketData.remove(playerUUID);
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
    public void loadTickets(InventoryView view, UUID playerUUID, boolean check) {
        if (check && ComponentUtils.hasLore(view.getItem(10))) {
            setLore(view, 10, plugin.getLanguage().getString("ARS_MAP_ERROR", "Map already loaded!"));
            return;
        }
        setLore(view, 10, "Loading...");
        ResultSetARSWithTIPS rs = new ResultSetARSWithTIPS(plugin);
        if (rs.fromId(ids.get(playerUUID))) {
            TARDISInteriorPositioning tips = new TARDISInteriorPositioning(plugin);
            TIPSData coords = tips.getTIPSData(rs.getTips());
            TicketData td = new TicketData();
            // get chunk tickets
            ResultSetChunkTickets rsc = new ResultSetChunkTickets(plugin);
            List<Ticket> tickets = (rsc.fromId(ids.get(playerUUID))) ? rsc.getData() : new ArrayList<>();
            ItemStack[][][] json = getGridFromJSON(rs.getJson(), tickets, coords);
            td.setData(json);
            td.setE(rs.getEast());
            td.setS(rs.getSouth());
            td.setY(rs.getLayer());
            td.setId(rs.getId());
            ticketData.put(playerUUID, td);
            setMap(rs.getLayer(), rs.getEast(), rs.getSouth(), playerUUID, view);
            hasLoadedMap.add(playerUUID);
            setLore(view, 10, plugin.getLanguage().getString("ARS_MAP_LOADED", "Map LOADED"));
            switchLevel(view, (27 + rs.getLayer()), playerUUID);
        }
    }

    public void setMap(int ul, int ue, int us, UUID playerUUID, InventoryView view) {
        TicketData data = ticketData.get(playerUUID);
        ItemStack[][][] grid = data.getData();
        ItemStack[][] layer = grid[ul];
        ItemStack[][] map = sliceGrid(layer, ue, us);
        int indexx = 0, indexz = 0;
        for (int i = 4; i < 9; i++) {
            for (int j = 0; j < 5; j++) {
                int slot = i + (j * 9);
                ItemStack is = map[indexx][indexz];
                setSlot(view, slot, is, playerUUID, false);
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
    public void moveMap(UUID playerUUID, InventoryView view, int slot) {
        if (ticketData.containsKey(playerUUID)) {
            TicketData td = ticketData.get(playerUUID);
            int ue, us;
            switch (slot) {
                case 1 -> {
                    ue = td.getE();
                    us = ((td.getS() + 1) < 5) ? td.getS() + 1 : td.getS();
                }
                case 9 -> {
                    ue = ((td.getE() + 1) < 5) ? td.getE() + 1 : td.getE();
                    us = td.getS();
                }
                case 11 -> {
                    ue = ((td.getE() - 1) >= 0) ? td.getE() - 1 : td.getE();
                    us = td.getS();
                }
                default -> {
                    ue = td.getE();
                    us = ((td.getS() - 1) >= 0) ? td.getS() - 1 : td.getS();
                }
            }
            setMap(td.getY(), ue, us, playerUUID, view);
            setLore(view, slot, null);
            td.setE(ue);
            td.setS(us);
            ticketData.put(playerUUID, td);
        } else {
            setLore(view, slot, plugin.getLanguage().getString("ARS_LOAD", "You need to load the map first!"));
        }
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

    public void processTickets(UUID playerUUID, Player player) {
        if (ticketData.containsKey(playerUUID)) {
            int id = ids.get(playerUUID);
            // get the TARDIS world
            World world = player.getWorld();
            // clear current tickets
            clearTickets(playerUUID, null);
            // get interior chunk coordinates
            ResultSetTIPS rs = new ResultSetTIPS(plugin);
            TARDISInteriorPositioning tips = new TARDISInteriorPositioning(plugin);
            TIPSData coords = tips.getTIPSData(rs.getSlot(id));
            int cx = (coords.getCentreX() >> 4) - 4; // chunk x coord at [0][0][0] in ARS grid
            int cz = (coords.getCentreZ() >> 4) - 4; // chunk z coord at [0][0][0] in ARS grid
            // get current tickets from grid
            TicketData td = ticketData.get(playerUUID);
            ItemStack[][][] grid = td.getData();
            // process the grid - only need to process map layer 0
            for (int x = 0; x < 9; x++) {
                for (int z = 0; z < 9; z++) {
                    ItemStack is = grid[0][x][z];
                    // for each ticket
                    if (is.getType() == Material.NAME_TAG) {
                        // get ticket chunk coordinates
                        int gx = cx + x;
                        int gz = cz + z;
                        // add chunk coordinate records to database
                        HashMap<String, Object> set = new HashMap<>();
                        set.put("tardis_id", id);
                        set.put("uuid", playerUUID);
                        set.put("world", world.getKey().toString());
                        set.put("x", gx);
                        set.put("z", gz);
                        set.put("ticket", 1);
                        plugin.getQueryFactory().doInsert("chunks", set);
                        // add chunk ticket for the chunk
                        world.getChunkAt(gx, gz).addPluginChunkTicket(plugin);
                    }
                }
            }
            close(player);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CHUNK_TICKETS");
        }
    }

    public void clearTickets(UUID playerUUID, Player player) {
        // remove chunk tickets
        ResultSetChunkTickets rsct = new ResultSetChunkTickets(plugin);
        World world = null;
        if (rsct.fromId(ids.get(playerUUID))) {
            for (Ticket t : rsct.getData()) {
                if (world == null) {
                    world = t.world();
                }
                Chunk chunk = world.getChunkAt(t.x(), t.z());
                chunk.removePluginChunkTicket(plugin);
            }
        }
        // delete chunk records
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", ids.get(playerUUID));
        where.put("ticket", 1);
        plugin.getQueryFactory().doDelete("chunks", where);
        if (player != null) {
            // close
            close(player);
        }
    }
}
