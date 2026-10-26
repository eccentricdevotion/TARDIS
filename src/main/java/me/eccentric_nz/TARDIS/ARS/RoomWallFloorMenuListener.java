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
package me.eccentric_nz.TARDIS.ARS;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.desktop.WallListener;
import me.eccentric_nz.TARDIS.desktop.WallsInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

/**
 * A control room's look could be changed over time. The process by which an operator could transform a control room was
 * fairly simple, once compared by the Fifth Doctor to changing a "desktop theme".
 *
 * @author eccentric_nz
 */
public class RoomWallFloorMenuListener extends WallListener {

    private final TARDIS plugin;
    private final Set<String> titles = Set.of("Room Wall Menu", "Room Floor Menu");

    public RoomWallFloorMenuListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onRoomWallFloorMenuOpen(InventoryOpenEvent event) {
        if (event.getInventory().getHolder(false) instanceof WallsInventory roomWallFloor) {
            if (!titles.contains(roomWallFloor.getTitle())) {
                return;
            }
            Player player = (Player) event.getPlayer();
            scroll.put(player.getUniqueId(), 0);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onRoomWallFloorMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        InventoryHolder holder = event.getInventory().getHolder(false);
        if (!(holder instanceof WallsInventory roomWallFloor)) {
            return;
        }
        if (!titles.contains(roomWallFloor.getTitle())) {
            return;
        }
        boolean isWall = (roomWallFloor.getTitle().equals("Room Wall Menu"));
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 53) {
            ClickType click = event.getClick();
            if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);
        switch (slot) {
            case 8 -> {
                // scroll up
                if (!scrolling.contains(uuid)) {
                    scrolling.add(uuid);
                    scroll(view, scroll.get(uuid) + 1, true, uuid);
                }
            }
            case 17 -> {
                // scroll down
                if (!scrolling.contains(uuid)) {
                    scrolling.add(uuid);
                    scroll(view, scroll.get(uuid) - 1, false, uuid);
                }
            }
            case 26 -> {
                // default wall
                String wall = getWallFloor(uuid, true);
                setWallFloorBlock(player, uuid, wall, isWall);
            }
            case 35 -> {
                // default floor
                String floor = getWallFloor(uuid, false);
                setWallFloorBlock(player, uuid, floor, isWall);
            }
            case 53 -> close(player, true); // close
            default -> {
                // get block type and data
                ItemStack choice = view.getItem(slot);
                // set the tardis wall/floor block
                setWallFloorBlock(player, uuid, choice.getType().toString(), isWall);
            }
        }
    }

    private void setWallFloorBlock(Player player, UUID uuid, String str, boolean isWall) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        HashMap<String, Object> set = new HashMap<>();
        if (isWall) {
            set.put("wall", str);
            // open the floor block GUI
            floor(player);
        } else {
            set.put("floor", str);
            // return to the ARS GUI
            ars(player);
        }
        plugin.getQueryFactory().doUpdate("player_prefs", set, where);
    }

    /**
     * Closes the inventory and opens the Floor block selector menu.
     *
     * @param player the player using the GUI
     */
    private void floor(Player player) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                player.openInventory(new WallsInventory(plugin, "Room Floor Menu").getInventory()), 1L);
    }

    /**
     * Closes the inventory and opens the ARS menu.
     *
     * @param player the player using the GUI
     */
    private void ars(Player player) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                player.openInventory(new ARSInventory(plugin, player).getInventory()), 1L);
    }
}
