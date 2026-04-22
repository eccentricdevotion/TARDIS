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
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author eccentric_nz
 */
public class OptionsCommand {

    private final TARDIS plugin;
    private final List<String> sections = new ArrayList<>();

    public OptionsCommand(TARDIS plugin) {
        this.plugin = plugin;
        sections.addAll(this.plugin.getConfig().getDefaultSection().getKeys(false));
        sections.remove("debug");
    }

    public void showConfigOptions(CommandSender sender, String arg) {
        String section = arg.toLowerCase(Locale.ROOT);
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG");
        if (sections.contains(section)) {
            plugin.getMessenger().messageWithColour(sender, section + ":", "#55FFFF");
            Set<String> options = plugin.getConfig().getConfigurationSection(section).getKeys(false);
            options.forEach((o) -> {
                if (plugin.getConfig().isConfigurationSection(section + "." + o)) {
                    plugin.getMessenger().messageWithColour(sender, "    " + o + ":", "#55FF55");
                    for (String indent : plugin.getConfig().getConfigurationSection(section + "." + o).getKeys(false)) {
                        plugin.getMessenger().messageWithColour(sender, "        " + indent + ": " + plugin.getConfig().getString(section + "." + o + "." + indent), "#55FF55");
                    }
                } else {
                    plugin.getMessenger().messageWithColour(sender, "    " + o + ": " + plugin.getConfig().getString(section + "." + o), "#55FF55");
                }
            });
            return;
        }
        switch (section) {
            case "worlds" -> {
                plugin.getMessenger().messageWithColour(sender, section + ":", "#55FFFF");
                Set<String> worldNames = plugin.getPlanetsConfig().getConfigurationSection("planets").getKeys(false);
                worldNames.forEach((wname) -> {
                    String enabled = plugin.getPlanetsConfig().getString("planets." + wname + ".time_travel");
                    plugin.getMessenger().sendWithColours(sender, "    " + wname + ": ", "#55FF55", enabled, "#FFFFFF");
                });
            }
            case "rechargers" -> {
                plugin.getMessenger().messageWithColour(sender, section + ":", "#55FFFF");
                Set<String> chargerNames = plugin.getConfig().getConfigurationSection("rechargers").getKeys(false);
                chargerNames.forEach((charname) -> {
                    plugin.getMessenger().messageWithColour(sender, "    " + charname + ":", "#55FF55");
                    plugin.getMessenger().message(sender, "        world: " + plugin.getConfig().getString("rechargers." + charname + ".world"));
                    plugin.getMessenger().message(sender, "        x: " + plugin.getConfig().getString("rechargers." + charname + ".x"));
                    plugin.getMessenger().message(sender, "        y: " + plugin.getConfig().getString("rechargers." + charname + ".y"));
                    plugin.getMessenger().message(sender, "        z: " + plugin.getConfig().getString("rechargers." + charname + ".z"));
                });
            }
            case "rooms" -> {
                plugin.getMessenger().messageWithColour(sender, section + ":", "#55FFFF");
                Set<String> roomNames = plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false);
                roomNames.forEach((r) -> {
                    plugin.getMessenger().messageWithColour(sender, "    " + r + ":", "#55FF55");
                    plugin.getMessenger().message(sender, "        enabled: " + plugin.getRoomsConfig().getString("rooms." + r + ".enabled"));
                    plugin.getMessenger().message(sender, "        cost: " + plugin.getRoomsConfig().getString("rooms." + r + ".cost"));
                    plugin.getMessenger().message(sender, "        offset: " + plugin.getRoomsConfig().getString("rooms." + r + ".offset"));
                    plugin.getMessenger().message(sender, "        seed: " + plugin.getRoomsConfig().getString("rooms." + r + ".seed"));
                });
            }
        }
    }
}
