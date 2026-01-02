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
package me.eccentric_nz.TARDIS.mobfarming;

import me.eccentric_nz.TARDIS.ARS.ARSSound;
import me.eccentric_nz.TARDIS.ARS.TARDISARS;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Room;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Locale;

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
        rooms.put(16, "happy");
        rooms.put(17, "hutch");
        rooms.put(27, "igloo");
        rooms.put(28, "iistubil");
        rooms.put(29, "lava");
        rooms.put(30, "mangrove");
        rooms.put(31, "pen");
        rooms.put(32, "stable");
        rooms.put(33, "stall");
        rooms.put(34, "village");
    }

    @EventHandler(ignoreCancelled = true)
    public void onFarmingMenuClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TARDISFarmingInventory)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        event.setCancelled(true);
        switch (slot) {
            case 9, 10, 11, 12, 13, 14, 15, 16, 17, 27, 28, 29, 30, 31, 32, 33, 34, 35 ->
                    toggleOption(player, event.getView(), slot); // toggle option enabled / disabled
            case 53 -> close(player);
            default -> event.setCancelled(true);
        }
    }

    private void toggleOption(Player player, InventoryView view, int slot) {
        ItemStack option = view.getItem(slot);
        ItemMeta im = option.getItemMeta();
        Material material = option.getType();
        Material m = Material.LIME_WOOL;
        ;
        int onOff = -1;
        switch (material) {
            case LIME_WOOL -> {
                // disable
                im.displayName(Component.text("Disabled"));
                m = Material.RED_WOOL;
                onOff = 0;
            }
            case RED_WOOL -> {
                // enable
                im.displayName(Component.text("Enabled"));
                onOff = 1;
                // get item in slot above
                ItemStack above = view.getItem(slot - 9);
                if (above != null) {
                    // play a room sound
                    Room room = Room.valueOf(TARDISARS.ARSFor(above.getType().toString()).getConfigPath().toUpperCase(Locale.ROOT));
                    if (ARSSound.ROOM_SOUNDS.containsKey(room)) {
                        player.playSound(player.getLocation(), ARSSound.ROOM_SOUNDS.get(room), 1.0f, 1.0f);
                    }
                }
            }
        }
        ItemStack sub = ItemStack.of(m);
        sub.setItemMeta(im);
        view.setItem(slot, sub);
        // update database
        plugin.getQueryFactory().updateFarmingPref(player.getUniqueId(), rooms.get(slot), onOff);
    }
}
