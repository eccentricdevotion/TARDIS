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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.sudo.TARDISSudoTracker;
import me.eccentric_nz.TARDIS.enumeration.Storage;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
    private final HashMap<Component, Integer> titles;

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
        Component title = view.title();
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
        Component title = view.title();
        if (!titles.containsKey(title)) {
            return;
        }
        TARDISSudoTracker.SUDOERS.remove(event.getPlayer().getUniqueId());
    }

    public int getPageNumber(InventoryView view) {
        ItemStack is = view.getItem(45);
        ItemMeta im = is.getItemMeta();
        String[] split = ComponentUtils.stripColour(im.displayName()).split(" ");
        return TARDISNumberParsers.parseInt(split[1]);
    }

    private HashMap<Component, Integer> getTitleMap() {
        HashMap<Component, Integer> map = new HashMap<>();
        map.put(Component.text("Add Companion", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("Admin Config Menu", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("Architectural Blueprints", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("Architectural Reconfiguration", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("Room Relocator", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("Area Locations", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("Artron Capacitor Storage", NamedTextColor.DARK_RED), 9);
        map.put(Component.text("Chameleon Circuit", NamedTextColor.DARK_RED), 27);
        map.put(Component.text("Chameleon Construction", NamedTextColor.DARK_RED), 18);
        map.put(Component.text("Chameleon Help", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("Chameleon Police Boxes", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("Chameleon Presets", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("Chameleon Template", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("Colour Picker", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("Companions", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("Custom Chameleon Presets", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("Customise Console", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("Destination Terminal", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("Chemical compounds", NamedTextColor.DARK_RED), 27);
        map.put(Component.text("Element constructor", NamedTextColor.DARK_RED), 27);
        map.put(Component.text("Light Emitting Blocks", NamedTextColor.DARK_RED), 27);
        map.put(Component.text("Material reducer", NamedTextColor.DARK_RED), 27);
        map.put(Component.text("Product crafting", NamedTextColor.DARK_RED), 27);
        map.put(Component.text("Lab table", NamedTextColor.DARK_RED), 27);
        map.put(Component.text("Genetic Manipulator", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("Genetic Skins", NamedTextColor.DARK_RED), 54);
        // TODO should Handles Program GUI be in here?
//        map.put(Component.text("Handles Program", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("Particle Preferences", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("Player Prefs Menu", NamedTextColor.DARK_RED), 36);
        map.put(Component.text("Recipe Categories", NamedTextColor.DARK_RED), 36);
        map.put(Component.text("TARDIS Recipes", NamedTextColor.DARK_RED), 27);
        map.put(Component.text("Saved Programs", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("Sonic Activator", NamedTextColor.DARK_RED), 9);
        map.put(Component.text("Sonic Configurator", NamedTextColor.DARK_RED), 27);
        map.put(Component.text("Sonic Generator", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("Sonic Prefs Menu", NamedTextColor.DARK_RED), 27);
        map.put(Component.text("TARDIS Archive", NamedTextColor.DARK_RED), 27);
        map.put(Component.text("TARDIS Autonomous Menu", NamedTextColor.DARK_RED), 36);
        map.put(Component.text("TARDIS Console", NamedTextColor.DARK_RED), 18);
        map.put(Component.text("TARDIS Control Menu", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("TARDIS Farming Menu", NamedTextColor.DARK_RED), 36);
        map.put(Component.text("TARDIS Floor Menu", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("TARDIS Interior Sounds", NamedTextColor.DARK_RED), 18);
        map.put(Component.text("TARDIS Index File", NamedTextColor.DARK_RED), 27);
        map.put(Component.text("TARDIS Info Category", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("TARDIS Info Entry", NamedTextColor.DARK_RED), 27);
        map.put(Component.text("TARDIS Key Prefs Menu", NamedTextColor.DARK_RED), 27);
        map.put(Component.text("TARDIS Light Levels", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("TARDIS Light Sequence", NamedTextColor.DARK_RED), 36);
        map.put(Component.text("TARDIS Lights", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("TARDIS Map", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("TARDIS Dimension Map", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("TARDIS Seeds Menu", NamedTextColor.DARK_RED), 90);
        map.put(Component.text("TARDIS Seeds Recipe", NamedTextColor.DARK_RED), 90);
        map.put(Component.text("TARDIS Shell Loader", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("TARDIS System Upgrades", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("TARDIS Telepathic Circuit", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("TARDIS Television", NamedTextColor.DARK_RED), 36);
        map.put(Component.text("Doctor Skins", NamedTextColor.DARK_RED), 36);
        map.put(Component.text("Companion Skins", NamedTextColor.DARK_RED), 36);
        map.put(Component.text("Character Skins", NamedTextColor.DARK_RED), 36);
        map.put(Component.text("Monster Skins", NamedTextColor.DARK_RED), 27);
        map.put(Component.text("TARDIS Upgrade Menu", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("TARDIS Wall & Floor Menu", NamedTextColor.DARK_RED), 90);
        map.put(Component.text("TARDIS Wall Menu", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("TARDIS Weather Menu", NamedTextColor.DARK_RED), 9);
        map.put(Component.text("TARDIS areas", NamedTextColor.DARK_RED), 90);
        map.put(Component.text("TARDIS saves", NamedTextColor.DARK_RED), 90);
        map.put(Component.text("TARDIS transmats", NamedTextColor.DARK_RED), 90);
        map.put(Component.text("Temporal Locator", NamedTextColor.DARK_RED), 27);
        map.put(Component.text("Telepathic Biome Finder", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("Telepathic Structure Finder", NamedTextColor.DARK_RED), 54);
        map.put(Component.text("Variable Light Blocks", NamedTextColor.DARK_RED), 54);
        // Vortex Manipulator
        map.put(Component.text("Vortex Manipulator", NamedTextColor.DARK_RED), 81);
        map.put(Component.text("VM Messages", NamedTextColor.DARK_RED), 81);
        map.put(Component.text("VM Saves", NamedTextColor.DARK_RED), 81);
        for (Storage s : Storage.values()) {
            map.put(Component.text(s.getTitle()), 54);
        }
        return map;
    }
}
