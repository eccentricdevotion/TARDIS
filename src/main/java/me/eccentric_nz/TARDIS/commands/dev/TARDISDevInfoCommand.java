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
package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import org.bukkit.command.CommandSender;

public class TARDISDevInfoCommand {

    private final TARDIS plugin;
    public TARDISDevInfoCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean test(CommandSender sender) {
        for (TARDISInfoMenu tim : TARDISInfoMenu.values()) {
            sender.sendMessage("---");
            sender.sendMessage("[" + tim.getName() + "]");
            TARDISInfoMenu.getChildren(tim.toString()).forEach((key, value) -> {
                String[] split = key.split(value, 2);
                if (split.length > 1) {
                    String first = "> " + split[0];
                    plugin.getMessenger().sendInfo(sender, first, value, split[1]);
                } else {
                    plugin.debug(key + ", " + value);
                }
            });
        }
        return true;
    }
}
