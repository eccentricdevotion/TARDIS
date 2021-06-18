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
package me.eccentric_nz.tardis.commands.dev;

import me.eccentric_nz.tardis.enumeration.Preset;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class TardisPresetPermissionLister {

    public void list(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "[tardis]" + ChatColor.RESET + " Chameleon Preset Permissions:");
        for (Preset preset : Preset.values()) {
            if (preset.getSlot() != -1) {
                sender.sendMessage("tardis.preset." + preset.toString().toLowerCase());
            }
        }
    }
}
