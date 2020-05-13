/*
 * Copyright (C) 2020 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.STORAGE;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryView;

import java.util.HashMap;
import java.util.Set;

/**
 * @author eccentric_nz
 */
public class TARDISMenuListener implements Listener {

    private final TARDIS plugin;
    private final HashMap<String, Integer> titles;

    protected TARDISMenuListener(TARDIS plugin) {
        this.plugin = plugin;
        titles = getTitleMap();
    }

    /**
     * Closes the inventory.
     *
     * @param p the player using the GUI
     */
    protected void close(Player p) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, p::closeInventory, 1L);
    }

    @EventHandler(ignoreCancelled = true)
    public void onMenuDrag(InventoryDragEvent event) {
        InventoryView view = event.getView();
        String title = view.getTitle();
        if (!titles.containsKey(title)) {
            return;
        }
        Set<Integer> slots = event.getRawSlots();
        slots.forEach((slot) -> {
            if ((slot >= 0 && slot < titles.get(title))) {
                event.setCancelled(true);
            }
        });
    }

    private HashMap<String, Integer> getTitleMap() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put(ChatColor.DARK_RED + "Add Companion", 54);
        map.put(ChatColor.DARK_RED + "Admin Menu", 54);
        map.put(ChatColor.DARK_RED + "Architectural Reconfiguration", 54);
        map.put(ChatColor.DARK_RED + "Chameleon Circuit", 27);
        map.put(ChatColor.DARK_RED + "Chameleon Construction", 18);
        map.put(ChatColor.DARK_RED + "Chameleon Help", 54);
        map.put(ChatColor.DARK_RED + "Chameleon Presets", 54);
        map.put(ChatColor.DARK_RED + "Chameleon Police Boxes", 27);
        map.put(ChatColor.DARK_RED + "Chameleon Template", 54);
        map.put(ChatColor.DARK_RED + "Companions", 54);
        map.put(ChatColor.DARK_RED + "Destination Terminal", 54);
        map.put(ChatColor.DARK_RED + "Genetic Manipulator", 54);
        // TODO should Handles Program GUI be in here?
//        map.put(ChatColor.DARK_RED + "Handles Program", 54);
        map.put(ChatColor.DARK_RED + "Player Prefs Menu", 36);
        map.put(ChatColor.DARK_RED + "Saved Programs", 54);
        map.put(ChatColor.DARK_RED + "Sonic Activator", 9);
        map.put(ChatColor.DARK_RED + "Sonic Generator", 54);
        map.put(ChatColor.DARK_RED + "Sonic Prefs Menu", 27);
        map.put(ChatColor.DARK_RED + "TARDIS Archive", 27);
        map.put(ChatColor.DARK_RED + "TARDIS Console", 9);
        map.put(ChatColor.DARK_RED + "TARDIS Control Menu", 54);
        map.put(ChatColor.DARK_RED + "TARDIS Floor Menu", 54);
        map.put(ChatColor.DARK_RED + "TARDIS Interior Sounds", 18);
        map.put(ChatColor.DARK_RED + "TARDIS Key Prefs Menu", 27);
        map.put(ChatColor.DARK_RED + "TARDIS Map", 54);
        map.put(ChatColor.DARK_RED + "TARDIS Seeds Menu", 90);
        map.put(ChatColor.DARK_RED + "TARDIS Seeds Recipe", 90);
        map.put(ChatColor.DARK_RED + "TARDIS Upgrade Menu", 54);
        map.put(ChatColor.DARK_RED + "TARDIS Wall & Floor Menu", 90);
        map.put(ChatColor.DARK_RED + "TARDIS Wall Menu", 54);
        map.put(ChatColor.DARK_RED + "TARDIS areas", 90);
        map.put(ChatColor.DARK_RED + "TARDIS saves", 90);
        map.put(ChatColor.DARK_RED + "TARDIS transmats", 90);
        map.put(ChatColor.DARK_RED + "Temporal Locator", 27);
        for (STORAGE s : STORAGE.values()) {
            map.put(s.getTitle(), 54);
        }
        return map;
    }
}
