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
package me.eccentric_nz.TARDIS.listeners;

import java.util.HashMap;
import java.util.Set;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.STORAGE;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

/**
 *
 * @author eccentric_nz
 */
public class TARDISMenuListener {

    private final TARDIS plugin;
    private final HashMap<String, Integer> titles;

    public TARDISMenuListener(TARDIS plugin) {
        this.plugin = plugin;
        this.titles = getTitleMap();
    }

    /**
     * Closes the inventory.
     *
     * @param p the player using the GUI
     */
    public void close(final Player p) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                p.closeInventory();
            }
        }, 1L);
    }

    @EventHandler(ignoreCancelled = true)
    public void onMenuDrag(InventoryDragEvent event) {
        Inventory inv = event.getInventory();
        String title = inv.getTitle();
        if (!titles.containsKey(title)) {
            return;
        }
        Set<Integer> slots = event.getRawSlots();
        for (Integer slot : slots) {
            if ((slot >= 0 && slot < titles.get(title))) {
                event.setCancelled(true);
            }
        }
    }

    public final HashMap<String, Integer> getTitleMap() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("§4Admin Menu", 54);
        map.put("§4Architectural Reconfiguration", 54);
        map.put("§4Chameleon Circuit", 54);
        map.put("§4Chameleon Construction", 18);
        map.put("§4Chameleon Help", 54);
        map.put("§4Chameleon Template", 54);
        map.put("§4Destination Terminal", 54);
        map.put("§4Even More Presets", 54);
        map.put("§4Genetic Manipulator", 54);
        map.put("§4More Presets", 54);
        map.put("§4Player Prefs Menu", 18);
        map.put("§4Police Box Wall Menu", 90);
        map.put("§4Sonic Prefs Menu", 27);
        map.put("§4TARDIS Console", 9);
        map.put("§4TARDIS Control Menu", 27);
        map.put("§4TARDIS Floor Menu", 54);
        map.put("§4TARDIS Key Prefs Menu", 27);
        map.put("§4TARDIS Map", 54);
        map.put("§4TARDIS Seeds Menu", 90);
        map.put("§4TARDIS Seeds Recipe", 90);
        map.put("§4TARDIS Upgrade Menu", 27);
        map.put("§4TARDIS Wall & Floor Menu", 90);
        map.put("§4TARDIS Wall Menu", 54);
        map.put("§4TARDIS areas", 90);
        map.put("§4TARDIS saves", 90);
        map.put("§4Temporal Locator", 27);
        for (STORAGE s : STORAGE.values()) {
            map.put(s.getTitle(), 54);
        }
        return map;
    }
}
