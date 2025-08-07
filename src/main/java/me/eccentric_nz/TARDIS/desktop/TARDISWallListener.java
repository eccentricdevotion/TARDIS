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
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * A control room's look could be changed over time. The process by which an operator could transform a control room was
 * fairly simple, once compared by the Fifth Doctor to changing a "desktop theme".
 *
 * @author eccentric_nz
 */
public class TARDISWallListener extends TARDISMenuListener {

    private final TARDIS plugin;
    protected final HashMap<UUID, Integer> scroll = new HashMap<>();
    protected final List<UUID> scrolling = new ArrayList<>();
    private final ItemStack[][] blocks;
    private final int rows;

    public TARDISWallListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
        rows = TARDISWalls.BLOCKS.size() / 8 + 1;
        blocks = getWallBlocks();
    }

    protected String getWallFloor(UUID uuid, boolean wall) {
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
        if (rsp.resultSet()) {
            return wall ? rsp.getWall() : rsp.getFloor();
        } else {
            return wall ? "ORANGE_WOOL" : "LIGHT_GRAY_WOOL";
        }
    }

    /**
     * Closes the inventory.
     *
     * @param p      the player using the GUI
     * @param remove whether to stop tracking the upgrade
     */
    protected void close(Player p, boolean remove) {
        if (remove) {
            plugin.getTrackerKeeper().getUpgrades().remove(p.getUniqueId());
        }
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, p::closeInventory, 1L);
    }

    protected void scroll(InventoryView view, int row, boolean up, UUID uuid) {
        if ((up && row < (rows - 5)) || (!up && row >= 0)) {
            scroll.put(uuid, row);
            setSlots(view, row, uuid);
        } else {
            scrolling.remove(uuid);
        }
    }

    private void setSlots(InventoryView view, int row, UUID uuid) {
        int slot = 0;
        for (int r = row; r < row + 6; r++) {
            for (int c = 0; c < 8; c++) {
                view.setItem(slot, blocks[r][c]);
                if (slot % 9 == 7) {
                    slot += 2;
                } else {
                    slot++;
                }
            }
        }
        scrolling.remove(uuid);
    }

    protected ItemStack[][] getWallBlocks() {
        ItemStack[][] stacks = new ItemStack[rows][8];
        int r = 0;
        int c = 0;
        for (Material entry : TARDISWalls.BLOCKS) {
            ItemStack is = ItemStack.of(entry, 1);
            stacks[r][c] = is;
            c++;
            if (c == 8) {
                r++;
                c = 0;
            }
        }
        return stacks;
    }
}
