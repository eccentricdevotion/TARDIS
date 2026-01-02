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
package me.eccentric_nz.TARDIS.rooms.eye;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class EyeDamageRunnable implements Runnable {

    private final TARDIS plugin;

    public EyeDamageRunnable(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (UUID uuid : plugin.getTrackerKeeper().getEyeDamage()) {
            Player player = plugin.getServer().getPlayer(uuid);
            if (player != null && player.isOnline() && !hasSpaceHelmet(player)) {
                player.damage(plugin.getConfig().getDouble("eye_of_harmony.damage_amount"), DamageSource.builder(DamageType.GENERIC).build());
            }
        }
    }

    private boolean hasSpaceHelmet(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack helmet = inventory.getHelmet();
        return TARDISSpaceHelmetListener.isSpaceHelmet(helmet);
    }
}
