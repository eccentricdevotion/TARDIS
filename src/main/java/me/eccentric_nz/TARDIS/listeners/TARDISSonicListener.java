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

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSonicListener implements Listener {

    private final TARDIS plugin;
    private final Material sonic;
    private final HashMap<String, Long> timeout = new HashMap<String, Long>();

    public TARDISSonicListener(TARDIS plugin, String str) {
        this.plugin = plugin;
        String[] split = str.split(":");
        if (split.length > 1) {
            this.sonic = Material.valueOf(split[0]);
        } else {
            this.sonic = Material.valueOf(str);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        long now = System.currentTimeMillis();
        if (player.getItemInHand().getType().equals(sonic)) {
            ItemMeta im = player.getItemInHand().getItemMeta();
            if (im.getDisplayName().equals("Sonic Screwdriver")) {
                if (event.getAction().equals(Action.RIGHT_CLICK_AIR) && (!timeout.containsKey(player.getName()) || timeout.get(player.getName()) < now)) {
                    timeout.put(player.getName(), now + 3050);
                    plugin.utils.playTARDISSound(player.getLocation(), player, "sonic_screwdriver");
                }
            }
        }
    }
}
