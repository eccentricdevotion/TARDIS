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
import me.eccentric_nz.TARDIS.enumeration.Language;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * @author eccentric_nz
 */
public class LanguageCommand {

    private final TARDIS plugin;

    public LanguageCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setLanguage(CommandSender sender, String arg) {
        // check file exists
        File file;
        file = new File(plugin.getDataFolder() + File.separator + "language" + File.separator + arg + ".yml");
        if (!file.isFile()) {
            // file not found
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "LANG_NOT_FOUND", arg);
            return true;
        }
        // load the language
        plugin.setLanguage(YamlConfiguration.loadConfiguration(file));
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "LANG_SET", Language.valueOf(arg).getLang());
        // set and save the config
        plugin.getConfig().set("preferences.language", arg);
        plugin.saveConfig();
        return true;
    }
}
