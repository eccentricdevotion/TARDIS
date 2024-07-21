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
package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;

public class TARDISPresetPermissionLister {

    public void list(CommandSender sender) {
        TARDIS.plugin.getMessenger().message(sender, TardisModule.TARDIS, " Chameleon Preset Permissions:");
        for (ChameleonPreset preset : ChameleonPreset.values()) {
            if (preset.getSlot() != -1) {
                sender.sendMessage("tardis.preset." + preset.toString().toLowerCase());
            }
        }
    }
}
