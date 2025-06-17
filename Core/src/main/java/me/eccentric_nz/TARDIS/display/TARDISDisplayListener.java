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
package me.eccentric_nz.TARDIS.display;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.List;

public class TARDISDisplayListener implements Listener {

    private final TARDIS plugin;

    public TARDISDisplayListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getTrackerKeeper().getDisplay().containsKey(player.getUniqueId())) {
            return;
        }
        if (event.getFrom().getWorld().getName().contains("TARDIS")) {
            return;
        }
        TARDISDisplayType displayType = plugin.getTrackerKeeper().getDisplay().get(player.getUniqueId());
        String direction = plugin.getMessenger().sendHeadsUpDisplay(player, plugin, displayType);
        if (!direction.isEmpty() && displayType == TARDISDisplayType.LOCATOR) {
            // update TARDIS Locator model to point to TARDIS location
             updateLocator(player, direction);
        }
    }

    private void updateLocator(Player player, String direction) {
        ItemStack is = player.getInventory().getItemInMainHand();
        if (is.getType() == Material.COMPASS) {
            ItemMeta im = is.getItemMeta();
            if (!im.hasDisplayName()) {
                return;
            }
            float model;
            switch (direction) {
                case "N" -> model = 101.0f;
                case "NW" -> model = 102.0f;
                case "W" -> model = 103.0f;
                case "SW" -> model = 104.0f;
                case "S" -> model = 105.0f;
                case "SE" -> model = 106.0f;
                case "E" -> model = 107.0f;
                // NE
                default -> model = 108.0f;
            }
            CustomModelDataComponent component = im.getCustomModelDataComponent();
            component.setFloats(List.of(model));
            im.setCustomModelDataComponent(component);
            is.setItemMeta(im);
            player.getInventory().setItemInMainHand(is);
        }
    }
}

