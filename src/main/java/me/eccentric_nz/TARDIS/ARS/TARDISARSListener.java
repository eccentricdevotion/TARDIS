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

import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
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
}
