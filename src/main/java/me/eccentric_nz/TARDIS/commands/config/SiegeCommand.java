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

import java.util.List;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
public class SiegeCommand {

    private final TARDIS plugin;
    private final List<String> siegeBool = List.of("enabled", "butcher", "creeper", "healing", "texture");

    public SiegeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void setOption(CommandSender sender, String option, int i) {
        String first = option.toLowerCase(Locale.ROOT);
        if (!siegeBool.contains(first)) {
            plugin.getConfig().set("siege." + first, i);
        }
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG_UPDATED", first);
        plugin.saveConfig();
    }

    public void setOption(CommandSender sender, String option, boolean bool) {
        String first = option.toLowerCase(Locale.ROOT);
        if (siegeBool.contains(first)) {
            plugin.getConfig().set("siege." + first, bool);
        }
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG_UPDATED", first);
        plugin.saveConfig();
    }
}
