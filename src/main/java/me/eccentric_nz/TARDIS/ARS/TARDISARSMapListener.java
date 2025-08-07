/*
 * Copyright (C) 2025 eccentric_nz
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
import me.eccentric_nz.TARDIS.builders.interior.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.interior.TARDISTIPSData;
import me.eccentric_nz.TARDIS.commands.sudo.TARDISSudoTracker;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.move.TARDISDoorListener;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
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

import java.util.HashMap;
import java.util.List;
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
    private final HashMap<UUID, SelectedSlot> selectedSlot = new HashMap<>();

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
        if (!(event.getInventory().getHolder(false) instanceof TARDISARSMap)) {
            return;
        }
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        UUID playerUUID = player.getUniqueId();
        UUID uuid;
        uuid = TARDISSudoTracker.SUDOERS.getOrDefault(playerUUID, playerUUID);
        ids.put(playerUUID, getTardisId(uuid.toString()));
        int slot = event.getRawSlot();
        if (slot != 10 && slot != 45 && !hasLoadedMap.contains(playerUUID)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARS_LOAD");
            return;
        }
        if (slot < 0 || slot > 53) {
            return;
        }
        switch (slot) {
            case 1, 9, 11, 19 -> moveMap(playerUUID, view, slot); // up, left, right, down
            case 10 -> loadMap(view, playerUUID); // load map
            case 45 -> close(player); // close
            case 47 -> findPlayer(player, view); // where am I?
            case 27, 28, 29 -> {
                // change levels
                if (map_data.containsKey(playerUUID)) {
                    switchLevel(view, slot, playerUUID);
                    TARDISARSMapData md = map_data.get(playerUUID);
                    setMap(md.getY(), md.getE(), md.getS(), playerUUID, view);
                    setLore(view, slot, null);
                } else {
                    setLore(view, slot, plugin.getLanguage().getString("ARS_LOAD", "You need to load the map first!"));
                }
            }
            case 46 -> {
                if (map_data.containsKey(playerUUID)) {
                    // transmat
                    if (!selectedLocation.containsKey(playerUUID)) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT_SELECT");
                    } else if (selectedLocation.get(playerUUID).equals("TERRACOTTA")) {
                        setLore(view, slot, plugin.getLanguage().getString("TRANSMAT_RENDER"));
                    } else {
                        Location tp_loc = getRoomLocation(player);
                        if (tp_loc != null) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT");
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                player.playSound(tp_loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                                player.teleport(tp_loc);
                            }, 10L);
                            close(player);
                        }
                    }
                } else {
                    setLore(view, slot, plugin.getLanguage().getString("ARS_LOAD", "You need to load the map first!"));
                }
            }
            default -> {
                if (map_data.containsKey(playerUUID)) {
                    TARDISARSMapData md = map_data.get(playerUUID);
                    ItemStack is = view.getItem(slot);
                    if (is != null) {
                        ItemMeta im = is.getItemMeta();
                        String dn = ComponentUtils.stripColour(im.displayName());
                        if (!dn.equals("Empty slot")) {
                            selectedLocation.put(playerUUID, is.getType().toString());
                            // get selected slot
                            int col = (slot / 9) + md.getS();
                            int row = (slot % 9) - (4 - md.getE());
                            selectedSlot.put(playerUUID, new SelectedSlot(md.getY(), row, col));
                        }
                    }
                } else {
                    setLore(view, slot, plugin.getLanguage().getString("ARS_LOAD", "You need to load the map first!"));
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
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
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
                ItemStack sub = ItemStack.of(Material.SPECTRAL_ARROW);
                ItemMeta im = is.getItemMeta();
                im.lore(List.of(Component.text(plugin.getLanguage().getString("ARS_MAP_HERE", "You are here!"))));
                sub.setItemMeta(im);
                view.setItem(slot, sub);
            }
        } else {
            setLore(view, 47, plugin.getLanguage().getString("ARS_LOAD", "You need to load the map first!"));
        }
    }

    private Location getRoomLocation(Player player) {
        UUID uuid = player.getUniqueId();
        if (map_data.containsKey(uuid)) {
            UUID playerUUID = player.getUniqueId();
            int id = ids.get(playerUUID);
            // determine row and col
            String room = selectedLocation.get(playerUUID);
            if (consoleBlocks.contains(room)) {
                // get inner door tp location
                TARDISDoorLocation idl = TARDISDoorListener.getDoor(1, id);
                return idl.getL();
            } else {
                SelectedSlot selected = selectedSlot.get(uuid);
                if (selected != null) {
                    TARDISARSSlot a = new TARDISARSSlot();
                    a.setChunk(plugin.getLocationUtils().getTARDISChunk(id));
                    a.setY(selected.level());
                    a.setX(selected.row());
                    a.setZ(selected.column());
                    return new Location(a.getChunk().getWorld(), a.getX(), a.getY(), a.getZ()).add(3.5d, 5.0d, 8.5d);
                }
            }
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
