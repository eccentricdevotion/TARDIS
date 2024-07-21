/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class TARDISHandlesTransmatCommand {

    private final TARDIS plugin;

    public TARDISHandlesTransmatCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * A site-to-site transport is a special type of transport in which an object or person is transported from one site
     * directly to another, with neither site being a transporter platform.
     *
     * @param player   The player to transmat
     * @param transmat the location to send the player
     */
    public void siteToSiteTransport(Player player, Location transmat) {
        Location location = player.getLocation();
        transmat.setPitch(location.getPitch());
        plugin.getMessenger().handlesSend(player, "TRANSMAT");
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            player.playSound(transmat, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
            player.teleport(transmat);
        }, 10L);
    }
}
