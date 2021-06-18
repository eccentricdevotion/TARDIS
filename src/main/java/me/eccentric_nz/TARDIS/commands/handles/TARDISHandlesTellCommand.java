/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.commands.handles;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
class TardisHandlesTellCommand {

    private final TardisPlugin plugin;

    public TardisHandlesTellCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    boolean message(String[] args) {
        Player player = plugin.getServer().getPlayer(args[1]);
        if (player != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                sb.append(args[i]).append(" ");
            }
            String message = sb.toString();
            TardisMessage.handlesMessage(player, message);
        }
        return true;
    }
}
