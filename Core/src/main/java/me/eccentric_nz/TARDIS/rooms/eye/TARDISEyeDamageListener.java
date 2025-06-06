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
package me.eccentric_nz.TARDIS.rooms.eye;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class TARDISEyeDamageListener implements Listener {

    private final TARDIS plugin;

    public TARDISEyeDamageListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.getFrom().getWorld().getName().contains("TARDIS")) {
            return;
        }
        Player player = event.getPlayer();
        Location l = event.getTo();
        Biome biome = l.getBlock().getBiome();
        UUID uuid = player.getUniqueId();
        if (plugin.getFromRegistry().getKeysKey(biome).equals("eye_of_harmony")) {
            // start tracking player for damage
            plugin.getTrackerKeeper().getEyeDamage().add(uuid);
        } else {
            // stop tracking player
            plugin.getTrackerKeeper().getEyeDamage().remove(uuid);
        }
    }
}

