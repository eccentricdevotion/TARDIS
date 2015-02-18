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
import java.util.List;
import java.util.Set;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import static me.eccentric_nz.TARDIS.commands.preferences.TARDISPrefsCommands.ucfirst;
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
public class TARDISARSListener extends TARDISARSMethods implements Listener {

    private List<Integer> room_ids;
    private List<String> room_names;

    public TARDISARSListener(TARDIS plugin) {
        super(plugin);
        getRoomIdAndNames();
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
            ids.put(uuid, getTardisId(player.getUniqueId().toString()));
            int slot = event.getRawSlot();
            if (slot != 10 && !hasLoadedMap.contains(uuid)) {
                TARDISMessage.send(player, "ARS_LOAD");
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
                        // switch level
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
                        int max = room_ids.size() - 9;
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
                            setSlot(inv, (45 + i), room_ids.get(startl + i), room_names.get(startl + i), uuid, false);
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
                            setSlot(inv, (45 + i), room_ids.get(startr + i), room_names.get(startr + i), uuid, false);
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
                                room = TARDISARS.ARSFor(displayName).getActualName();
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
     * Checks the saved map to see whether the selected slot can be reset.
     *
     * @param uuid the UUID of the player using the GUI
     * @param slot the slot that was clicked
     * @param updown the type id of the block in the slot
     * @return true or false
     */
    public boolean checkSavedGrid(UUID uuid, int slot, int updown) {
        TARDISARSMapData md = map_data.get(uuid);
        TARDISARSSaveData sd = save_map_data.get(uuid);
        int[][][] grid = sd.getData();
        int yy = md.getY() + updown;
        // avoid ArrayIndexOutOfBoundsException if gravity well extends beyond ARS area
        if (yy < 0 || yy > 2) {
            return false;
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
     * Populates arrays of room names and seed IDs for the scrollable room
     * buttons.
     */
    @SuppressWarnings("deprecation")
    public final void getRoomIdAndNames() {
        List<String> custom_names = getCustomRoomNames();
        TARDISARS[] ars = TARDISARS.values();
        // less non-room types
        int l = (custom_names.size() + ars.length) - 3;
        this.room_ids = new ArrayList<Integer>();
        this.room_names = new ArrayList<String>();
        for (TARDISARS a : ars) {
            if (a.getOffset() != 0) {
                this.room_ids.add(a.getId());
                this.room_names.add(a.getDescriptiveName());
            }
        }
        for (final String c : custom_names) {
            this.room_ids.add(Material.valueOf(plugin.getRoomsConfig().getString("rooms." + c + ".seed")).getId());
            final String uc = ucfirst(c);
            this.room_names.add(uc);
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
    public List<String> getCustomRoomNames() {
        List<String> crooms = new ArrayList<String>();
        Set<String> names = plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false);
        for (String cr : names) {
            if (plugin.getRoomsConfig().getBoolean("rooms." + cr + ".user") && plugin.getRoomsConfig().getBoolean("rooms." + cr + ".enabled")) {
                crooms.add(cr);
            }
        }
        return crooms;
    }
}
