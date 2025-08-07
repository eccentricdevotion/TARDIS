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
package me.eccentric_nz.TARDIS.desktop;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * A control room's look could be changed over time. The process by which an operator could transform a control room was
 * fairly simple, once compared by the Fifth Doctor to changing a "desktop theme".
 *
 * @author eccentric_nz
 */
public class TARDISWallMenuListener extends TARDISWallListener {

    private final TARDIS plugin;

    public TARDISWallMenuListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onWallMenuOpen(InventoryOpenEvent event) {
        if (event.getInventory().getHolder(false) instanceof TARDISWallsInventory) {
            Player player = (Player) event.getPlayer();
            scroll.put(player.getUniqueId(), 0);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onWallMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        InventoryHolder holder = event.getInventory().getHolder(false);
        if (!(holder instanceof TARDISWallsInventory walls)) {
            return;
        }
        boolean isWall = (walls.getTitle().equals("TARDIS Wall Menu"));
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 53) {
            ClickType click = event.getClick();
            if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                plugin.debug("TARDISWallMenuListener");
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

    private void setWallFloorBlock(Player p, UUID uuid, String str, boolean isWall) {
        TARDISUpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(uuid);
        if (isWall) {
            // open the floor block GUI
            tud.setWall(str);
            floor(p);
        } else {
            tud.setFloor(str);
            close(p, false);
            // start the upgrade
            new TARDISThemeProcessor(plugin, uuid).changeDesktop();
        }
        plugin.getTrackerKeeper().getUpgrades().put(uuid, tud);
    }

    /**
     * Closes the inventory and opens the Floor block selector menu.
     *
     * @param p the player using the GUI
     */
    private void floor(Player p) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                p.openInventory(new TARDISWallsInventory(plugin, "TARDIS Floor Menu").getInventory()), 1L);
    }
}
