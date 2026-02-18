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
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
public class DefaultCommand {

    private final TARDIS plugin;

    public DefaultCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setDefaultItem(CommandSender sender, String option, String value) {
        String which = option.toLowerCase(Locale.ROOT);
        plugin.getConfig().set("preferences." + which, value);
        plugin.saveConfig();
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG_UPDATED", "which");
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "RESTART");
        return true;
    }
}
