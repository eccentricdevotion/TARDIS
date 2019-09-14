/*
 * Copyright (C) 2019 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static me.eccentric_nz.TARDIS.commands.preferences.TARDISPrefsCommands.ucfirst;

/**
 * The architectural reconfiguration system is a component of the Doctor's TARDIS in the shape of a tree that, according
 * to the Eleventh Doctor, "reconstructs the particles according to your needs." It is basically "a machine that makes
 * machines," perhaps somewhat like a 3D printer. It is, according to Gregor Van Baalen's scanner, "more valuable than
 * the total sum of any currency.
 *
 * @author eccentric_nz
 */
public class TARDISARSListener extends TARDISARSMethods implements Listener {

    private List<Material> room_materials;
    private List<String> room_names;

    public TARDISARSListener(TARDIS plugin) {
        super(plugin);
        getRoomIdAndNames();
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a TARDIS GUI, then the click is processed
     * accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onARSTerminalClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "Architectural Reconfiguration")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
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
                        moveMap(uuid, view, slot);
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
                        if (!checkSlotForConsole(view, slot, uuid.toString())) {
                            // select slot
                            selected_slot.put(uuid, slot);
                        }
                        break;
                    case 10:
                        // load map
                        loadMap(view, uuid);
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
                            switchLevel(view, slot, uuid);
                            TARDISARSMapData md = map_data.get(uuid);
                            setMap(md.getY(), md.getE(), md.getS(), uuid, view);
                            setLore(view, slot, null);
                        } else {
                            setLore(view, slot, plugin.getLanguage().getString("ARS_LOAD"));
                        }
                        break;
                    case 30:
                        // reset selected slot to empty
                        if (selected_slot.containsKey(uuid)) {
                            // check whether original loaded slot was a room - as it will need to be jettisoned, not reset
                            if (checkSavedGrid(uuid, selected_slot.get(uuid), 0)) {
                                setLore(view, slot, plugin.getLanguage().getString("ARS_RESET_SLOT"));
                                break;
                            } else {
                                ItemStack stone = new ItemStack(Material.STONE, 1);
                                ItemMeta s1 = stone.getItemMeta();
                                s1.setDisplayName("Empty slot");
                                s1.setCustomModelData(1);
                                stone.setItemMeta(s1);
                                setSlot(view, selected_slot.get(uuid), stone, uuid, true);
                                setLore(view, slot, null);
                            }
                        } else {
                            setLore(view, slot, plugin.getLanguage().getString("ARS_NO_SLOT"));
                        }
                        break;
                    case 36:
                        // scroll left
                        int startl;
                        int max = room_materials.size() - 9;
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
                            // setSlot(Inventory inv, int slot, int id, String room, UUID uuid, boolean update)
                            setSlot(view, (45 + i), room_materials.get(startl + i), room_names.get(startl + i), uuid, false);
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
                            // setSlot(Inventory inv, int slot, int id, String room, UUID uuid, boolean update)
                            setSlot(view, (45 + i), room_materials.get(startr + i), room_names.get(startr + i), uuid, false);
                        }
                        break;
                    case 39:
                        // jettison
                        if (selected_slot.containsKey(uuid)) {
                            // need to check for gravity wells, and jettison both layers...
                            ItemStack tnt = new ItemStack(Material.TNT, 1);
                            ItemMeta j = tnt.getItemMeta();
                            j.setDisplayName("Jettison");
                            j.setCustomModelData(1);
                            tnt.setItemMeta(j);
                            setSlot(view, selected_slot.get(uuid), tnt, uuid, true);
                            setLore(view, slot, null);
                        } else {
                            setLore(view, slot, plugin.getLanguage().getString("ARS_NO_SLOT"));
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
                                setLore(view, slot, "Jettison existing room first!");
                                break;
                            } else {
                                ItemStack ris = view.getItem(slot);
                                String displayName = ris.getItemMeta().getDisplayName();
                                String room = TARDISARS.ARSFor(ris.getType().toString()).toString();
                                if (!player.hasPermission("tardis.room." + room.toLowerCase(Locale.ENGLISH))) {
                                    setLore(view, slot, plugin.getLanguage().getString("NO_PERM_ROOM_TYPE"));
                                    break;
                                }
                                if (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) {
                                    int updown = (room.equals("GRAVITY")) ? -1 : 1;
                                    if (checkSavedGrid(uuid, selected_slot.get(uuid), updown)) {
                                        setLore(view, slot, plugin.getLanguage().getString("ARS_GRAVITY"));
                                        break;
                                    }
                                }
                                if (room.equals("RENDERER") && hasRenderer(uuid)) {
                                    setLore(view, slot, plugin.getLanguage().getString("ARS_HAS_RENDERER"));
                                    break;
                                }
                                // setSlot(Inventory inv, int slot, ItemStack is, String player, boolean update)
                                setSlot(view, selected_slot.get(uuid), ris, uuid, true);
                                setSlot(view, slot, ris.getType(), displayName, uuid, false);
                            }
                        } else {
                            setLore(view, slot, plugin.getLanguage().getString("ARS_NO_SLOT"));
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
     * @param uuid   the UUID of the player using the GUI
     * @param slot   the slot that was clicked
     * @param updown the type id of the block in the slot
     * @return true or false
     */
    private boolean checkSavedGrid(UUID uuid, int slot, int updown) {
        TARDISARSMapData md = map_data.get(uuid);
        TARDISARSSaveData sd = save_map_data.get(uuid);
        String[][][] grid = sd.getData();
        int yy = md.getY() + updown;
        // avoid ArrayIndexOutOfBoundsException if gravity well extends beyond ARS area
        if (yy < 0 || yy > 2) {
            return false;
        }
        int[] coords = getCoords(slot, md);
        int xx = coords[0];
        int zz = coords[1];
        Material prior = Material.valueOf(grid[yy][xx][zz]);
        if (room_materials.stream().anyMatch((i) -> (prior == i))) {
            return true;
        }
        return consoleBlocks.contains(grid[yy][xx][zz]);
    }

    /**
     * Populates arrays of room names and seed IDs for the scrollable room buttons.
     */

    private void getRoomIdAndNames() {
        List<String> custom_names = getCustomRoomNames();
        TARDISARS[] ars = TARDISARS.values();
        // less non-room types
        room_materials = new ArrayList<>();
        room_names = new ArrayList<>();
        for (TARDISARS a : ars) {
            if (a.getOffset() != 0) {
                room_materials.add(Material.valueOf(a.getMaterial()));
                room_names.add(a.getDescriptiveName());
            }
        }
        custom_names.forEach((c) -> {
            room_materials.add(Material.valueOf(plugin.getRoomsConfig().getString("rooms." + c + ".seed")));
            String uc = ucfirst(c);
            room_names.add(uc);
            TARDISARS.addNewARS(new ARS() {
                @Override
                public String getMaterial() {
                    return plugin.getRoomsConfig().getString("rooms." + c + ".seed");
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
        });
    }

    /**
     * Checks and gets custom rooms for ARS.
     *
     * @return a list of enabled custom room names
     */
    private List<String> getCustomRoomNames() {
        List<String> crooms = new ArrayList<>();
        Set<String> names = plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false);
        names.forEach((cr) -> {
            if (plugin.getRoomsConfig().getBoolean("rooms." + cr + ".user") && plugin.getRoomsConfig().getBoolean("rooms." + cr + ".enabled")) {
                crooms.add(cr);
            }
        });
        return crooms;
    }
}
