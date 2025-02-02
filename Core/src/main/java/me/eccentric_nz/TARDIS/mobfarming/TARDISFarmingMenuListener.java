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
package me.eccentric_nz.TARDIS.mobfarming;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

public class TARDISFarmingMenuListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final HashMap<Integer, String> rooms = new HashMap<>();

    public TARDISFarmingMenuListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
        rooms.put(9, "allay");
        rooms.put(10, "apiary");
        rooms.put(11, "aquarium");
        rooms.put(12, "bamboo");
        rooms.put(13, "birdcage");
        rooms.put(14, "farm");
        rooms.put(15, "geode");
        rooms.put(16, "hutch");
        rooms.put(17, "igloo");
        rooms.put(27, "iistubil");
        rooms.put(28, "lava");
        rooms.put(29, "mangrove");
        rooms.put(30, "pen");
        rooms.put(31, "stable");
        rooms.put(32, "stall");
        rooms.put(33, "village");
    }

    @EventHandler(ignoreCancelled = true)
    public void onFarmingMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "TARDIS Farming Menu")) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        event.setCancelled(true);
        switch (slot) {
            case 9, 10, 11, 12, 13, 14, 15, 16, 17, 27, 28, 29, 30, 31, 32, 33 -> toggleOption(player.getUniqueId(), view, slot); // toggle option enabled / disabled
            case 35 -> close(player);
            default -> event.setCancelled(true);
        }
    }

    private void toggleOption(UUID uuid, InventoryView view, int slot) {
        ItemStack option = view.getItem(slot);
        ItemMeta im = option.getItemMeta();
        Material material = option.getType();
        int onOff = -1;
        switch (material) {
            case LIME_WOOL -> {
                // disable
                im.setDisplayName("Disabled");
                option.setType(Material.RED_WOOL);
                onOff = 0;
            }
            case RED_WOOL -> {
                // enable
                im.setDisplayName("Enabled");
                option.setType(Material.LIME_WOOL);
                onOff = 1;
            }
        }
        option.setItemMeta(im);
        // update database
        plugin.getQueryFactory().updateFarmingPref(uuid, rooms.get(slot), onOff);
    }
}
