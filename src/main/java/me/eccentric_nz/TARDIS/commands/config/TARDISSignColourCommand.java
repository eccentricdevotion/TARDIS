/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.config;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;

/**
 * @author eccentric_nz
 */
class TARDISSignColourCommand {

    private final TARDIS plugin;
    private final List<String> COLOURS = Arrays.asList("AQUA", "BLACK", "BLUE", "DARK_AQUA", "DARK_BLUE", "DARK_GRAY", "DARK_GREEN", "DARK_PURPLE", "DARK_RED", "GOLD", "GRAY", "GREEN", "LIGHT_PURPLE", "RED", "WHITE", "YELLOW");

    TARDISSignColourCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean setColour(CommandSender sender, String[] args) {
        String colour = args[1].toUpperCase(Locale.ENGLISH);
        if (!COLOURS.contains(colour)) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_COLOUR");
            return true;
        }
        plugin.getConfig().set("police_box.sign_colour", colour);
        plugin.saveConfig();
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG_UPDATED", "sign_colour");
        return true;
    }
}
