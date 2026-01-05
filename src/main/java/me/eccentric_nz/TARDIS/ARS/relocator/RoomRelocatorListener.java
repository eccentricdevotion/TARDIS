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
package me.eccentric_nz.TARDIS.ARS.relocator;

import me.eccentric_nz.TARDIS.ARS.ARSMapData;
import me.eccentric_nz.TARDIS.ARS.ARSMethods;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.sudo.TARDISSudoTracker;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
public class RoomRelocatorListener extends ARSMethods implements Listener {

    public final HashMap<UUID, Integer> relocation_slot = new HashMap<>();

    public RoomRelocatorListener(TARDIS plugin) {
        super(plugin);
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a TARDIS GUI, then the click is processed
     * accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onRoomRelocatorClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof RoomRelocatorInventory)) {
            return;
        }
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        UUID playerUUID = player.getUniqueId();
        UUID uuid;
        uuid = TARDISSudoTracker.SUDOERS.getOrDefault(playerUUID, playerUUID);
        ids.put(playerUUID, getTardisId(uuid.toString()));
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 53) {
            return;
        }
        if (slot != 3 && slot != 10 && !hasLoadedMap.contains(playerUUID)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARS_LOAD");
            return;
        }
        InventoryView view = event.getView();
        switch (slot) {
            case 1, 9, 11, 19 -> moveMap(playerUUID, view, slot); // up, left, right, down
            // open room relocator GUI
            case 3 -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                    player.openInventory(new RoomRelocatorInventory(plugin, player).getInventory()), 2L);
            case 4, 5, 6, 7, 8, 13, 14, 15, 16, 17, 22, 23, 24, 25, 26, 31, 32, 33, 34, 35, 40, 41, 42, 43, 44 -> {
                if (!checkSlotForConsole(view, slot) && !selected_slot.containsKey(playerUUID)) {
                    // select room to move
                    selected_slot.put(playerUUID, slot);
                } else if (!relocation_slot.containsKey(playerUUID) && selected_slot.containsKey(playerUUID) && isEmptySlot(view, slot)) {
                    relocation_slot.put(playerUUID, slot);
                    // set slot to selected room with glint
                    setRelocationSlots(view, playerUUID);
                } else {
                    // need to reset
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "RELOCATOR_RESET");
                }
            }
            case 10 -> loadMap(view, playerUUID, true); // load map
            case 12 -> {
                // reconfigure
                if (!plugin.getBuildKeeper().getRoomProgress().containsKey(player.getUniqueId())) {
                    relocate(player);
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ARS_ACTIVE");
                }
            }
            case 27, 28, 29 -> {
                // switch level
                if (map_data.containsKey(playerUUID)) {
                    switchLevel(view, slot, playerUUID);
                    ARSMapData md = map_data.get(playerUUID);
                    setMap(md.getY(), md.getE(), md.getS(), playerUUID, view);
                    setLore(view, slot, null);
                } else {
                    setLore(view, slot, plugin.getLanguage().getString("ARS_LOAD", "You need to load the map first!"));
                }
            }
            case 30 -> {
                // reset
                selected_slot.remove(playerUUID);
                relocation_slot.remove(playerUUID);
                loadMap(view, playerUUID, false);
            }
            default -> {
            }
        }
    }

    private void relocate(Player player) {
        UUID playerUUID = player.getUniqueId();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            selected_slot.remove(playerUUID);
            relocation_slot.remove(playerUUID);
            player.closeInventory();
            // start relocation
        }, 1L);
    }

    private void setRelocationSlots(InventoryView view, UUID uuid) {
        int from_slot = selected_slot.get(uuid);
        int to_slot = relocation_slot.get(uuid);
        ItemStack is = view.getItem(from_slot).clone();
        ItemMeta im = is.getItemMeta();
        im.setEnchantmentGlintOverride(true);
        is.setItemMeta(im);
        view.setItem(to_slot, is);
        ItemStack tnt = ItemStack.of(Material.TNT, 1);
        ItemMeta j = tnt.getItemMeta();
        j.displayName(Component.text("Jettison"));
        tnt.setItemMeta(j);
        view.setItem(from_slot, tnt);
    }

    private boolean isEmptySlot(InventoryView view, int slot) {
        ItemStack is = view.getItem(slot);
        return is != null && is.getType() == Material.STONE;
    }
}
