/*
 * Copyright (C) 2021 eccentric_nz
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
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
class TARDISSetMaterialCommand {

    private final TARDIS plugin;

    TARDISSetMaterialCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean setConfigMaterial(CommandSender sender, String[] args, String section) {
        String first = (section.isEmpty()) ? args[0].toLowerCase(Locale.ENGLISH) : section + "." + args[0].toLowerCase(Locale.ENGLISH);
        String setMaterial = args[1].toUpperCase(Locale.ENGLISH);
        if (!checkMaterial(setMaterial)) {
            TARDISMessage.send(sender, "MATERIAL_NOT_VALID");
            return false;
        } else {
            plugin.getConfig().set(first, setMaterial);
            plugin.saveConfig();
            TARDISMessage.send(sender, "CONFIG_UPDATED");
            return true;
        }
    }

    public boolean setConfigMaterial(CommandSender sender, String[] args) {
        String first = args[0].toLowerCase(Locale.ENGLISH);
        String setMaterial = args[1].toUpperCase(Locale.ENGLISH);
        if (!checkMaterial(setMaterial)) {
            TARDISMessage.send(sender, "MATERIAL_NOT_VALID");
            return false;
        } else {
            plugin.getArtronConfig().set(first, setMaterial);
            try {
                plugin.getArtronConfig().save(new File(plugin.getDataFolder(), "artron.yml"));
            } catch (IOException io) {
                plugin.debug("Could not save artron.yml, " + io);
            }
            TARDISMessage.send(sender, "CONFIG_UPDATED");
            return true;
        }
    }

    private boolean checkMaterial(String setMaterial) {
        try {
            Material.valueOf(setMaterial);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
