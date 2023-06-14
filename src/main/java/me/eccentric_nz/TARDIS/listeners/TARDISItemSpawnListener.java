/*
 * Copyright (C) 2023 eccentric_nz
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

import java.util.Arrays;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

public class TARDISItemSpawnListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> items = Arrays.asList(Material.WHITE_BED, Material.ORANGE_BED, Material.MAGENTA_BED, Material.YELLOW_BED, Material.LIME_BED, Material.PINK_BED, Material.GRAY_BED, Material.LIGHT_GRAY_BED, Material.PURPLE_BED, Material.CYAN_BED, Material.BLUE_BED, Material.GREEN_BED, Material.BROWN_BED, Material.RED_BED, Material.BLACK_BED, Material.LIGHT_BLUE_BED, Material.WHEAT_SEEDS, Material.DANDELION);

    public TARDISItemSpawnListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBedDrop(ItemSpawnEvent event) {
        if ((!plugin.getTrackerKeeper().getMaterialising().isEmpty() || !plugin.getTrackerKeeper().getDematerialising().isEmpty()) && items.contains(event.getEntity().getItemStack().getType())) {
            event.setCancelled(true);
        }
    }
}
