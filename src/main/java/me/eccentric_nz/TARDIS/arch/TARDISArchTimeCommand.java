/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.arch;

import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISArchTimeCommand {

    private final TARDIS plugin;

    public TARDISArchTimeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean getTime(Player player) {
        UUID uuid = player.getUniqueId();
        long time = plugin.getTrackerKeeper().getJohnSmith().get(uuid).getTime();
        long now = System.currentTimeMillis();
        long diff = (time - now) / 60000l;
        TARDISMessage.send(player, "ARCH_TIME", String.format("%d", diff));
        //
        return true;
    }
}
