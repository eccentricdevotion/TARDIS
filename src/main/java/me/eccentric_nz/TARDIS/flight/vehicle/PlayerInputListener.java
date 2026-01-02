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
package me.eccentric_nz.TARDIS.flight.vehicle;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.flight.TARDISExteriorFlight;
import org.bukkit.Bukkit;
import org.bukkit.Input;
import org.bukkit.craftbukkit.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInputEvent;

public class PlayerInputListener implements Listener {

    private final TARDIS plugin;

    public PlayerInputListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInput(PlayerInputEvent event) {
        Player player = event.getPlayer();
        Entity entity = player.getVehicle();
        if (entity instanceof ArmorStand as && ((CraftArmorStand) as).getHandle() instanceof TARDISArmourStand stand) {
            Input input = event.getInput();
            if (input.isSneak()) {
                stand.setStationary(true);
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    plugin.getTrackerKeeper().getStillFlyingNotReturning().remove(player.getUniqueId());
                    // teleport player back to the TARDIS interior
                    new TARDISExteriorFlight(plugin).stopFlying(player, as);
                    stand.setPlayer(null);
                    player.setFlying(false);
                    player.setAllowFlight(false);
                    player.setInvulnerable(false);
                }, 3L);
            }
            if (input.isJump()) {
                stand.setStationary(!stand.isStationary());
            }
            if (input.isForward()) {
                double factor = stand.getSpeedFactor();
                if (factor > 1d) {
                    stand.setSpeedFactor(factor - 0.25d);
                }
            }
            if (input.isBackward()) {
                double factor = stand.getSpeedFactor();
                if (factor < 5d) {
                    stand.setSpeedFactor(factor + 0.25d);
                }
            }
        }
    }
}
