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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.commands.sudo.TARDISSudoTracker;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

/**
 * The architectural reconfiguration system is a component of the Doctor's TARDIS in the shape of a tree that, according
 * to the Eleventh Doctor, "reconstructs the particles according to your needs." It is basically "a machine that makes
 * machines," perhaps somewhat like a 3D printer. It is, according to Gregor Van Baalen's scanner, "more valuable than
 * the total sum of any currency.
 *
 * @author eccentric_nz
 */
public class TARDISARSMapListener extends TARDISARSMethods implements Listener {

    private final HashMap<UUID, String> selectedLocation = new HashMap<>();

    public TARDISARSMapListener(TARDIS plugin) {
        super(plugin);
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a TARDIS GUI, then the click is processed
     * accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onARSMapClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "TARDIS Map")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            UUID playerUUID = player.getUniqueId();
            UUID uuid;
            if (TARDISSudoTracker.SUDOERS.containsKey(playerUUID)) {
                uuid = TARDISSudoTracker.SUDOERS.get(playerUUID);
            } else {
                uuid = playerUUID;
            }
            ids.put(playerUUID, getTardisId(uuid.toString()));
            int slot = event.getRawSlot();
            if (slot != 10 && slot != 45 && !hasLoadedMap.contains(playerUUID)) {
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
                        moveMap(playerUUID, view, slot);
                        break;
                    case 10:
                        // load map
                        loadMap(view, playerUUID);
                        break;
                    case 45:
                        // close
                        close(player);
                        break;
                    case 47:
                        // where am I?
                        findPlayer(player, view);
                        break;
                    case 27:
                    case 28:
                    case 29:
                        // change levels
                        if (map_data.containsKey(playerUUID)) {
                            switchLevel(view, slot, playerUUID);
                            TARDISARSMapData md = map_data.get(playerUUID);
                            setMap(md.getY(), md.getE(), md.getS(), playerUUID, view);
                            setLore(view, slot, null);
                        } else {
                            setLore(view, slot, plugin.getLanguage().getString("ARS_LOAD"));
                        }
                        break;
                    case 46:
                        if (map_data.containsKey(playerUUID)) {
                            // transmat
                            if (!selectedLocation.containsKey(playerUUID)) {
                                TARDISMessage.send(player, "TRANSMAT_SELECT");
                            } else if (selectedLocation.get(playerUUID).equals("TERRACOTTA")) {
                                setLore(view, slot, plugin.getLanguage().getString("TRANSMAT_RENDER"));
                            } else {
                                Location tp_loc = getRoomLocation(player);
                                if (tp_loc != null) {
                                    TARDISMessage.send(player, "TRANSMAT");
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                        player.playSound(tp_loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                                        player.teleport(tp_loc);
                                    }, 10L);
                                    close(player);
                                }
                            }
                        } else {
                            setLore(view, slot, plugin.getLanguage().getString("ARS_LOAD"));
                        }
                        break;
                    default:
                        if (map_data.containsKey(playerUUID)) {
                            ItemStack is = view.getItem(slot);
                            if (is != null) {
                                ItemMeta im = is.getItemMeta();
                                String dn = im.getDisplayName();
                                if (!dn.equals("Empty slot")) {
                                    selectedLocation.put(playerUUID, is.getType().toString());
                                }
                            }
                        } else {
                            setLore(view, slot, plugin.getLanguage().getString("ARS_LOAD"));
                        }
                        break;
                }
            }
        }
    }

    private void findPlayer(Player player, InventoryView view) {
        if (map_data.containsKey(player.getUniqueId())) {
            UUID playerUUID = player.getUniqueId();
            int id = ids.get(playerUUID);
            // need to get the console location - will be different for non-TIPS TARDISes
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (rs.resultSet()) {
                Tardis tardis = rs.getTardis();
                int pos = tardis.getTIPS();
                int tx = 0, tz = 0;
                if (pos != -1) {
                    // tips slot
                    TARDISInteriorPostioning tips = new TARDISInteriorPostioning(plugin);
                    TARDISTIPSData coords = tips.getTIPSData(pos);
                    tx = coords.getCentreX();
                    tz = coords.getCentreZ();
                }
                Location loc = player.getLocation();
                int px = loc.getBlockX();
                int pz = loc.getBlockZ();
                // determine row and col
                int col = (int) (4 + (Math.floor((px - tx) / 16.0d)));
                int row = (int) (4 + (Math.floor((pz - tz) / 16.0d)));
                if (col < 0 || col > 8 || row < 0 || row > 8) {
                    // outside ARS grid
                    setLore(view, 47, plugin.getLanguage().getString("ARS_MAP_OUTSIDE"));
                    return;
                }
                int east = getOffset(col);
                int south = getOffset(row);
                int py = loc.getBlockY();
                int level = 28;
                if (py >= 48 && py < 64) {
                    level = 27;
                }
                if (py >= 80 && py < 96) {
                    level = 29;
                }
                // set map
                switchLevel(view, level, playerUUID);
                TARDISARSMapData md = map_data.get(playerUUID);
                md.setY(level - 27);
                md.setE(east);
                md.setS(south);
                setMap(level - 27, east, south, playerUUID, view);
                setLore(view, level, null);
                map_data.put(playerUUID, md);
                // get itemstack to change lore
                int slot = ((row - south) * 9) + 4 + (col - east);
                ItemStack is = view.getItem(slot);
                is.setType(Material.ARROW);
                ItemMeta im = is.getItemMeta();
                im.setLore(Collections.singletonList(plugin.getLanguage().getString("ARS_MAP_HERE")));
                im.setCustomModelData(6);
                is.setItemMeta(im);
            }
        } else {
            setLore(view, 47, plugin.getLanguage().getString("ARS_LOAD"));
        }
    }

    private Location getRoomLocation(Player player) {
        if (map_data.containsKey(player.getUniqueId())) {
            UUID playerUUID = player.getUniqueId();
            int id = ids.get(playerUUID);
            // determine row and col
            String room = selectedLocation.get(playerUUID);
            TARDISARSMapData md = map_data.get(playerUUID);
            TARDISARSSlot a = null;
            for (int l = 0; l < 3; l++) {
                if (l != md.getY()) {
                    // skip levels that are not currently showing on the map because they can't be selected
                    continue;
                }
                for (int r = 0; r < 9; r++) {
                    for (int c = 0; c < 9; c++) {
                        if (md.getData()[l][r][c].equals(room)) {
                            // will always get the first room of this type on this level
                            a = new TARDISARSSlot();
                            a.setChunk(plugin.getLocationUtils().getTARDISChunk(id));
                            a.setY(l);
                            a.setX(r);
                            a.setZ(c);
                            break;
                        }
                    }
                }
            }
            return (a != null) ? new Location(a.getChunk().getWorld(), a.getX(), a.getY(), a.getZ()).add(3.5d, 5.0d, 8.5d) : null;
        }
        // should never get here
        return null;
    }

    private int getOffset(double d) {
        int offset = 2;
        if (d >= 6) {
            offset = 4;
        }
        if (d == 5) {
            offset = 3;
        }
        if (d == 3) {
            offset = 1;
        }
        if (d <= 2) {
            offset = 0;
        }
        return offset;
    }

    @Override
    public void close(Player player) {
        UUID playerUUID = player.getUniqueId();
        hasLoadedMap.remove(playerUUID);
        map_data.remove(playerUUID);
        ids.remove(playerUUID);
        selectedLocation.remove(playerUUID);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, player::closeInventory, 1L);
    }
}
