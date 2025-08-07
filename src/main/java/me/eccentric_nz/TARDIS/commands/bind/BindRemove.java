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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.commands.bind;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Bind;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

class BindRemove {

    private final TARDIS plugin;

    BindRemove(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean setClick(Bind bind, Player player) {
        plugin.getTrackerKeeper().getBindRemoval().put(player.getUniqueId(), bind);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "BIND_REMOVE_CLICK_BLOCK", bind.toString());
        return true;
    }
}
