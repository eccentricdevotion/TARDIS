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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.sudo.TARDISSudoTracker;
import me.eccentric_nz.TARDIS.enumeration.Storage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
     * @param player the player using the GUI
     */
    protected void close(Player player) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, player::closeInventory, 1L);
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

    @EventHandler(ignoreCancelled = true)
    public void onMenuClose(InventoryCloseEvent event) {
        InventoryView view = event.getView();
        String title = view.getTitle();
        if (!titles.containsKey(title)) {
            return;
        }
        TARDISSudoTracker.SUDOERS.remove(event.getPlayer().getUniqueId());
    }

    public int getPageNumber(InventoryView view) {
        ItemStack is = view.getItem(45);
        ItemMeta im = is.getItemMeta();
        String[] split = im.getDisplayName().split(" ");
        return TARDISNumberParsers.parseInt(split[1]);
    }

    private HashMap<String, Integer> getTitleMap() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put(ChatColor.DARK_RED + "Add Companion", 54);
        map.put(ChatColor.DARK_RED + "Admin Config Menu", 54);
        map.put(ChatColor.DARK_RED + "Architectural Reconfiguration", 54);
        map.put(ChatColor.DARK_RED + "Area Locations", 54);
        map.put(ChatColor.DARK_RED + "Artron Capacitor Storage", 9);
        map.put(ChatColor.DARK_RED + "Chameleon Circuit", 27);
        map.put(ChatColor.DARK_RED + "Chameleon Construction", 18);
        map.put(ChatColor.DARK_RED + "Chameleon Help", 54);
        map.put(ChatColor.DARK_RED + "Chameleon Presets", 54);
        map.put(ChatColor.DARK_RED + "Chameleon Police Boxes", 54);
        map.put(ChatColor.DARK_RED + "Chameleon Template", 54);
        map.put(ChatColor.DARK_RED + "Colour Picker", 54);
        map.put(ChatColor.DARK_RED + "Companions", 54);
        map.put(ChatColor.DARK_RED + "Destination Terminal", 54);
        map.put(ChatColor.DARK_RED + "Chemical compounds", 27);
        map.put(ChatColor.DARK_RED + "Element constructor", 27);
        map.put(ChatColor.DARK_RED + "Light Emitting Blocks", 27);
        map.put(ChatColor.DARK_RED + "Material reducer", 27);
        map.put(ChatColor.DARK_RED + "Product crafting", 27);
        map.put(ChatColor.DARK_RED + "Lab table", 27);
        map.put(ChatColor.DARK_RED + "Genetic Manipulator", 54);
        map.put(ChatColor.DARK_RED + "Genetic Skins", 54);
        // TODO should Handles Program GUI be in here?
//        map.put(ChatColor.DARK_RED + "Handles Program", 54);
        map.put(ChatColor.DARK_RED + "Particle Preferences", 54);
        map.put(ChatColor.DARK_RED + "Player Prefs Menu", 36);
        map.put(ChatColor.DARK_RED + "Recipe Categories", 36);
        map.put(ChatColor.DARK_RED + "TARDIS Recipes", 27);
        map.put(ChatColor.DARK_RED + "Saved Programs", 54);
        map.put(ChatColor.DARK_RED + "Sonic Activator", 9);
        map.put(ChatColor.DARK_RED + "Sonic Configurator", 27);
        map.put(ChatColor.DARK_RED + "Sonic Generator", 54);
        map.put(ChatColor.DARK_RED + "Sonic Prefs Menu", 27);
        map.put(ChatColor.DARK_RED + "TARDIS Archive", 27);
        map.put(ChatColor.DARK_RED + "TARDIS Autonomous Menu", 36);
        map.put(ChatColor.DARK_RED + "TARDIS Console", 18);
        map.put(ChatColor.DARK_RED + "TARDIS Control Menu", 54);
        map.put(ChatColor.DARK_RED + "TARDIS Farming Menu", 36);
        map.put(ChatColor.DARK_RED + "TARDIS Floor Menu", 54);
        map.put(ChatColor.DARK_RED + "TARDIS Interior Sounds", 18);
        map.put(ChatColor.DARK_RED + "TARDIS Index File", 27);
        map.put(ChatColor.DARK_RED + "TARDIS Info Category", 54);
        map.put(ChatColor.DARK_RED + "TARDIS Info Entry", 27);
        map.put(ChatColor.DARK_RED + "TARDIS Key Prefs Menu", 27);
        map.put(ChatColor.DARK_RED + "TARDIS Light Levels", 54);
        map.put(ChatColor.DARK_RED + "TARDIS Light Sequence", 36);
        map.put(ChatColor.DARK_RED + "TARDIS Lights", 54);
        map.put(ChatColor.DARK_RED + "TARDIS Map", 54);
        map.put(ChatColor.DARK_RED + "TARDIS Dimension Map", 54);
        map.put(ChatColor.DARK_RED + "TARDIS Seeds Menu", 90);
        map.put(ChatColor.DARK_RED + "TARDIS Seeds Recipe", 90);
        map.put(ChatColor.DARK_RED + "TARDIS Shell Loader", 54);
        map.put(ChatColor.DARK_RED + "TARDIS System Upgrades", 54);
        map.put(ChatColor.DARK_RED + "TARDIS Telepathic Circuit", 54);
        map.put(ChatColor.DARK_RED + "TARDIS Television", 36);
        map.put(ChatColor.DARK_RED + "Doctor Skins", 36);
        map.put(ChatColor.DARK_RED + "Companion Skins", 36);
        map.put(ChatColor.DARK_RED + "Character Skins", 36);
        map.put(ChatColor.DARK_RED + "Monster Skins", 27);
        map.put(ChatColor.DARK_RED + "TARDIS Upgrade Menu", 54);
        map.put(ChatColor.DARK_RED + "TARDIS Wall & Floor Menu", 90);
        map.put(ChatColor.DARK_RED + "TARDIS Wall Menu", 54);
        map.put(ChatColor.DARK_RED + "TARDIS Weather Menu", 9);
        map.put(ChatColor.DARK_RED + "TARDIS areas", 90);
        map.put(ChatColor.DARK_RED + "TARDIS saves", 90);
        map.put(ChatColor.DARK_RED + "TARDIS transmats", 90);
        map.put(ChatColor.DARK_RED + "Temporal Locator", 27);
        map.put(ChatColor.DARK_RED + "Telepathic Biome Finder", 54);
        map.put(ChatColor.DARK_RED + "Telepathic Structure Finder", 54);
        map.put(ChatColor.DARK_RED + "Variable Light Blocks", 54);
        // Vortex Manipulator
        map.put(ChatColor.DARK_RED + "Vortex Manipulator", 81);
        map.put(ChatColor.DARK_RED + "VM Messages", 81);
        map.put(ChatColor.DARK_RED + "VM Saves", 81);
        for (Storage s : Storage.values()) {
            map.put(s.getTitle(), 54);
        }
        return map;
    }
}
