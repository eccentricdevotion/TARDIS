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
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Set;

/**
 * @author eccentric_nz
 */
public class SetIntegerCommand {

    private final TARDIS plugin;
    private final Set<Integer> TIPS_SUBS = Set.of(400, 800, 1200, 1600);

    public SetIntegerCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    void setConfigInt(CommandSender sender, String option, int val, String section) {
        String first = (section.isEmpty()) ? option.toLowerCase(Locale.ROOT) : section + "." + option.toLowerCase(Locale.ROOT);
        if (option.toLowerCase(Locale.ROOT).equals("tips_limit") && !TIPS_SUBS.contains(val)) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_TIPS");
            return;
        }
        if (first.equals("preferences.sfx_volume")) {
            TARDISSounds.setVolume(val);
        }
        plugin.getConfig().set(first, val);
        plugin.saveConfig();
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG_UPDATED", first);
        if (first.equals("allow.force_field")) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "RESTART");
        }
    }

    public void setConfigInt(CommandSender sender, String option, int val) {
        plugin.getArtronConfig().set(option, val);
        try {
            plugin.getArtronConfig().save(new File(plugin.getDataFolder(), "artron.yml"));
        } catch (IOException io) {
            plugin.debug("Could not save artron.yml, " + io);
        }
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG_UPDATED", option);
    }
}
