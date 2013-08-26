/*
 * Copyright (C) 2013 eccentric_nz
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

import java.util.List;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants.SCHEMATIC;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSeedBlockListener implements Listener {

    private final TARDIS plugin;

    public TARDISSeedBlockListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSeedBlockPlace(BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        //String playerNameStr = player.getName();
        ItemStack is = player.getItemInHand();
        if (!is.hasItemMeta()) {
            return;
        }
        ItemMeta im = is.getItemMeta();
        if (!im.hasDisplayName() || !im.hasLore()) {
            return;
        }
        if (im.getDisplayName().equals("§6TARDIS Seed Block")) {
            List<String> lore = im.getLore();
            SCHEMATIC schm = SCHEMATIC.valueOf(lore.get(0));
            int middle_id = Material.valueOf(lore.get(1).toUpperCase(Locale.ENGLISH)).getId();
            byte middle_data = DyeColor.valueOf(lore.get(2).toUpperCase(Locale.ENGLISH)).getDyeData();
            player.sendMessage(plugin.pluginName + "You placed a TARDIS seed block!");
        }
    }
}
