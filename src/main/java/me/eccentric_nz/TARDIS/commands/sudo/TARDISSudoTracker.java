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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.commands.sudo;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TardisSudoTracker {

    public static final HashMap<UUID, UUID> SUDOERS = new HashMap<>();
    private static final UUID CONSOLE_UUID = UUID.randomUUID();

    public static boolean isSudo(CommandSender sender) {
        return SUDOERS.containsKey(getSudo(sender));
    }

    public static UUID getSudoPlayer(CommandSender sender) {
        return SUDOERS.get(getSudo(sender));
    }

    private static UUID getSudo(CommandSender sender) {
        UUID uuid;
        if (sender instanceof Player player) {
            uuid = player.getUniqueId();
        } else {
            // console
            uuid = CONSOLE_UUID;
        }
        return uuid;
    }
}
