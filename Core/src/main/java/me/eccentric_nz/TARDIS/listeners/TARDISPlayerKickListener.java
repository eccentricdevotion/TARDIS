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
import me.eccentric_nz.TARDIS.utility.TARDISVoidFall;
import org.bukkit.Location;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

/**
 * @author eccentric_nz
 */
public class TARDISPlayerKickListener implements Listener {

    private final TARDIS plugin;

    public TARDISPlayerKickListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onFallOutOfTARDIS(PlayerKickEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        if (location.getBlockY() < 1 && plugin.getUtils().inTARDISWorld(player)) {
            event.setReason(player.getName() + " fell out of their TARDIS!");
            event.setCancelled(true);
            if (plugin.getConfig().getString("preferences.vortex_fall").equals("kill")) {
                player.damage(Float.MAX_VALUE, DamageSource.builder(DamageType.GENERIC_KILL).build());
            } else {
                new TARDISVoidFall(plugin).teleport(player);
            }
        }
    }
}
