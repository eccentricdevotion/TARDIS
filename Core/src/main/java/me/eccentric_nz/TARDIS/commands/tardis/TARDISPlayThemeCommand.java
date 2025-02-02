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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.Theme;
import org.bukkit.entity.Player;

import java.util.Locale;

class TARDISPlayThemeCommand {

    private final TARDIS plugin;

    TARDISPlayThemeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean playTheme(Player p, String[] args) {
        if (plugin.getTrackerKeeper().getEggs().contains(p.getUniqueId())) {
            plugin.getMessenger().send(p, TardisModule.TARDIS, "THEME_PLAYING");
            return true;
        }
        Theme theme = Theme.RANDOM;
        if (args.length == 2) {
            try {
                theme = Theme.valueOf(args[1].toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                plugin.getMessenger().send(p, TardisModule.TARDIS, "ARG_THEME");
                return false;
            }
        }
        plugin.getTrackerKeeper().getEggs().add(p.getUniqueId());
        p.playSound(p.getLocation(), theme.getFilename(), 1.0f, 1.0f);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getTrackerKeeper().getEggs().remove(p.getUniqueId()), theme.getLength());
        return true;
    }
}
