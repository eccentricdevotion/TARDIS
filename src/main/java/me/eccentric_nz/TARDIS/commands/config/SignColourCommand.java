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
package me.eccentric_nz.TARDIS.commands.config;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import java.util.Locale;

/**
 * @author eccentric_nz
 */
public class SignColourCommand {

    private final TARDIS plugin;

    public SignColourCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setColour(CommandSender sender, NamedTextColor colour) {
        plugin.getConfig().set("police_box.sign_colour", NamedTextColor.NAMES.key(colour).toUpperCase(Locale.ROOT));
        plugin.saveConfig();
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG_UPDATED", "sign_colour");
        return true;
    }
}
