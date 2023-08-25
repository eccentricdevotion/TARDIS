/*
 * Copyright (C) 2023 eccentric_nz
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
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.commands.sudo.TARDISSudoTracker;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

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

    private List<Material> room_materials;
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
        InventoryView view = event.getView();
        if (view.getTitle().equals(ChatColor.DARK_RED + "Architectural Reconfiguration")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            UUID playerUUID = player.getUniqueId();
            UUID uuid;
            uuid = TARDISSudoTracker.SUDOERS.getOrDefault(playerUUID, playerUUID);
            ids.put(playerUUID, getTardisId(uuid.toString()));
            int slot = event.getRawSlot();
            if (slot != 10 && !hasLoadedMap.contains(playerUUID)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "ARS_LOAD");
                return;
            }
            if (slot >= 0 && slot < 54) {
                switch (slot) {
                    case 1, 9, 11, 19 ->
                        // up, left, right, down
                            moveMap(playerUUID, view, slot);
                    case 4, 5, 6, 7, 8, 13, 14, 15, 16, 17, 22, 23, 24, 25, 26, 31, 32, 33, 34, 35, 40, 41, 42, 43, 44 -> {
                        if (!checkSlotForConsole(view, slot, uuid.toString())) {
                            // select slot
                            selected_slot.put(playerUUID, slot);
                        }
                    }
                    case 10 ->
                        // load map
                            loadMap(view, playerUUID);
                    case 12 -> {
                        // reconfigure
                        if (!plugin.getBuildKeeper().getRoomProgress().containsKey(player.getUniqueId())) {
                            close(player);
                        } else {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARS_ACTIVE");
                        }
                    }
                    case 27, 28, 29 -> {
                        // switch level
                        if (map_data.containsKey(playerUUID)) {
                            switchLevel(view, slot, playerUUID);
                            TARDISARSMapData md = map_data.get(playerUUID);
                            setMap(md.getY(), md.getE(), md.getS(), playerUUID, view);
                            setLore(view, slot, null);
                        } else {
                            setLore(view, slot, plugin.getLanguage().getString("ARS_LOAD"));
                        }
                    }
                    case 30 -> {
                        // reset selected slot to empty
                        if (selected_slot.containsKey(playerUUID)) {
                            // check whether original loaded slot was a room - as it will need to be jettisoned, not reset
                            if (checkSavedGrid(playerUUID, selected_slot.get(playerUUID), 0)) {
                                setLore(view, slot, plugin.getLanguage().getString("ARS_RESET_SLOT"));
                            } else {
                                ItemStack stone = new ItemStack(Material.STONE, 1);
                                ItemMeta s1 = stone.getItemMeta();
                                s1.setDisplayName("Empty slot");
                                s1.setCustomModelData(1);
                                stone.setItemMeta(s1);
                                setSlot(view, selected_slot.get(playerUUID), stone, playerUUID, true);
                                setLore(view, slot, null);
                            }
                        } else {
                            setLore(view, slot, plugin.getLanguage().getString("ARS_NO_SLOT"));
                        }
                    }
                    case 36 -> {
                        // scroll left
                        int startl;
                        int max = room_materials.size() - 9;
                        if (scroll_start.containsKey(playerUUID)) {
                            startl = scroll_start.get(playerUUID) + 1;
                            if (startl >= max) {
                                startl = max;
                            }
                        } else {
                            startl = 1;
                        }
                        scroll_start.put(playerUUID, startl);
                        for (int i = 0; i < 9; i++) {
                            // setSlot(Inventory inv, int slot, int id, String room, UUID uuid, boolean update)
                            setSlot(view, (45 + i), room_materials.get(startl + i), room_names.get(startl + i), playerUUID, true);
                        }
                    }
                    case 38 -> {
                        // scroll right
                        int startr;
                        if (scroll_start.containsKey(playerUUID)) {
                            startr = scroll_start.get(playerUUID) - 1;
                            if (startr <= 0) {
                                startr = 0;
                            }
                        } else {
                            startr = 0;
                        }
                        scroll_start.put(playerUUID, startr);
                        for (int i = 0; i < 9; i++) {
                            // setSlot(Inventory inv, int slot, int id, String room, UUID uuid, boolean update)
                            setSlot(view, (45 + i), room_materials.get(startr + i), room_names.get(startr + i), playerUUID, true);
                        }
                    }
                    case 39 -> {
                        // jettison
                        if (selected_slot.containsKey(playerUUID)) {
                            // need to check for gravity wells, and jettison both layers...
                            ItemStack tnt = new ItemStack(Material.TNT, 1);
                            ItemMeta j = tnt.getItemMeta();
                            j.setDisplayName("Jettison");
                            j.setCustomModelData(1);
                            tnt.setItemMeta(j);
                            setSlot(view, selected_slot.get(playerUUID), tnt, playerUUID, true);
                            setLore(view, slot, null);
                        } else {
                            setLore(view, slot, plugin.getLanguage().getString("ARS_NO_SLOT"));
                        }
                    }
                    case 45, 46, 47, 48, 49, 50, 51, 52, 53 -> {
                        // put room in selected slot
                        if (selected_slot.containsKey(playerUUID)) {
                            // check whether original loaded slot was a room - as it will need to be jettisoned, not reset
                            if (checkSavedGrid(playerUUID, selected_slot.get(playerUUID), 0)) {
                                setLore(view, slot, "Jettison existing room first!");
                            } else {
                                ItemStack ris = view.getItem(slot);
                                String displayName = ris.getItemMeta().getDisplayName();
                                String room = TARDISARS.ARSFor(ris.getType().toString()).getConfigPath();
                                if (!TARDISPermission.hasPermission(player, "tardis.room." + room.toLowerCase(Locale.ENGLISH))) {
                                    break;
                                }
                                if (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) {
                                    int updown = (room.equals("GRAVITY")) ? -1 : 1;
                                    if (checkSavedGrid(playerUUID, selected_slot.get(playerUUID), updown)) {
                                        setLore(view, slot, plugin.getLanguage().getString("ARS_GRAVITY"));
                                        break;
                                    }
                                }
                                if (room.equals("RENDERER") && hasRenderer(playerUUID)) {
                                    setLore(view, slot, plugin.getLanguage().getString("ARS_HAS_RENDERER"));
                                    break;
                                }
                                // setSlot(Inventory inv, int slot, ItemStack is, String player, boolean update)
                                setSlot(view, selected_slot.get(playerUUID), ris, playerUUID, true);
                                setSlot(view, slot, ris.getType(), displayName, playerUUID, false);
                            }
                        } else {
                            setLore(view, slot, plugin.getLanguage().getString("ARS_NO_SLOT"));
                        }
                    }
                    default -> {
                    }
                }
            }
        }
    }

    /**
     * Checks the saved map to see whether the selected slot can be reset.
     *
     * @param playerUUID the UUID of the player using the GUI
     * @param slot       the slot that was clicked
     * @param updown     the type id of the block in the slot
     * @return true or false
     */
    private boolean checkSavedGrid(UUID playerUUID, int slot, int updown) {
        TARDISARSMapData md = map_data.get(playerUUID);
        TARDISARSSaveData sd = save_map_data.get(playerUUID);
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
     * Populates arrays of room names and seed IDs for the scrollable room
     * buttons.
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
            String uc = TARDISStringUtils.uppercaseFirst(c);
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
                public String getConfigPath() {
                    return c;
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
