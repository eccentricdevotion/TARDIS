/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.entity.Player;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Krakenites ware swamp-living aliens with a cylindrical body covered in a thick, grey shell with the texture of
 * barnacle. They have approximately ten tentacles that protrude from the top of their shell.
 *
 * @author eccentric_nz
 */
public class TARDISResourcePackChanger {

    private final TARDIS plugin;

    public TARDISResourcePackChanger(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Sets a player's texture pack.
     *
     * @param p   The player
     * @param str The URL of the texture pack file
     */
    public void changeRP(Player p, String str) {
        // check the URL
        try {
            new URL(str);
            if (p.isOnline()) {
                UUID uuid = p.getUniqueId();
                plugin.getServer().getPlayer(uuid).setResourcePack(str);
            }
        } catch (MalformedURLException e) {
            TARDISMessage.send(p, "URL", e.getMessage());
        }
    }
}
