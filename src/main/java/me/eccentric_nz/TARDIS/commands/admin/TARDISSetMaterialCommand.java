/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.admin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSetMaterialCommand {

    private final TARDIS plugin;

    public TARDISSetMaterialCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setConfigMaterial(CommandSender sender, String[] args, String section) {
        String first = (section.isEmpty()) ? args[0].toLowerCase() : section + "." + args[0].toLowerCase();
        String setMaterial = args[1].toUpperCase(Locale.ENGLISH);
        if (!checkMaterial(setMaterial)) {
            sender.sendMessage(plugin.pluginName + ChatColor.RED + "That is not a valid Material! Try checking http://jd.bukkit.org/apidocs/org/bukkit/Material.html");
            return false;
        } else {
            plugin.getConfig().set(first, setMaterial);
            plugin.saveConfig();
            sender.sendMessage(plugin.pluginName + "The config was updated!");
            return true;
        }
    }

    public boolean setConfigMaterial(CommandSender sender, String[] args) {
        String first = args[0].toLowerCase();
        String setMaterial = args[1].toUpperCase(Locale.ENGLISH);
        if (!checkMaterial(setMaterial)) {
            sender.sendMessage(plugin.pluginName + ChatColor.RED + "That is not a valid Material! Try checking http://jd.bukkit.org/apidocs/org/bukkit/Material.html");
            return false;
        } else {
            plugin.getArtronConfig().set(first, setMaterial);
            try {
                plugin.getArtronConfig().save(new File(plugin.getDataFolder(), "artron.yml"));
            } catch (IOException io) {
                plugin.debug("Could not save artron.yml, " + io);
            }
            sender.sendMessage(plugin.pluginName + "The config was updated!");
            return true;
        }
    }

    private boolean checkMaterial(String setMaterial) {
        return Arrays.asList(Material.values()).contains(Material.valueOf(setMaterial));
    }
}
