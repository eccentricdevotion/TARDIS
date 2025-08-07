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
package me.eccentric_nz.TARDIS.lights;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLightPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.desktop.TARDISWallListener;
import me.eccentric_nz.TARDIS.desktop.TARDISWallsInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class TARDISVariableLightBlocksListener extends TARDISWallListener {

    private final TARDIS plugin;

    public TARDISVariableLightBlocksListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onVariableLightBlockMenuOpen(InventoryOpenEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TARDISWallsInventory lights)) {
            return;
        }
        if (lights.getTitle().equals("Variable Light Blocks")) {
            Player player = (Player) event.getPlayer();
            scroll.put(player.getUniqueId(), 0);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onVariableLightBlockClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TARDISWallsInventory lights)) {
            return;
        }
        if (!lights.getTitle().equals("Variable Light Blocks")) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        int slot = event.getRawSlot();
        InventoryView view = event.getView();
        if (slot < 0 || slot > 53) {
            ClickType click = event.getClick();
            if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                plugin.debug("TARDISVariableLightBlocksListener");
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
                setVariableLightBlock(player, wall);
            }
            case 35 -> {
                // default floor
                String floor = getWallFloor(uuid, false);
                setVariableLightBlock(player, floor);
            }
            case 53 -> close(player); // close
            default -> {
                // get block type and data
                ItemStack choice = view.getItem(slot);
                setVariableLightBlock(player, choice.getType().toString());
            }
        }
    }

    private void setVariableLightBlock(Player player, String block) {
        // get player's TARDIS
        ResultSetTardisID rst = new ResultSetTardisID(plugin);
        UUID uuid = player.getUniqueId();
        if (rst.fromUUID(uuid.toString())) {
            int id = rst.getTardisId();
            // remember choice
            HashMap<String, Object> set = new HashMap<>();
            set.put("material", block);
            ResultSetLightPrefs rslp = new ResultSetLightPrefs(plugin);
            if (rslp.fromID(rst.getTardisId())) {
                HashMap<String, Object> where = new HashMap<>();
                where.put("tardis_id", id);
                plugin.getQueryFactory().doUpdate("light_prefs", set, where);
            } else {
                set.put("tardis_id", id);
                plugin.getQueryFactory().doSyncInsert("light_prefs", set);
            }
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                // go back to Lights GUI
                player.openInventory(new TARDISLightsInventory(plugin, id, uuid).getInventory());
            }, 5L);
        }
    }
}
