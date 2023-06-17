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

import java.io.File;
import java.util.Arrays;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Language;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author eccentric_nz
 */
class TARDISLanguageCommand {

    private final TARDIS plugin;
    private final List<String> codes = Arrays.asList("ar", "bg", "ca", "zh", "cs", "da", "nl", "en", "et", "fi", "fr", "de", "el", "ht", "he", "hi", "mww", "hu", "id", "it", "ja", "ko", "lv", "lt", "ms", "no", "fa", "pl", "pt", "ro", "ru", "sk", "sl", "es", "sv", "th", "tr", "uk", "ur", "vi");

    TARDISLanguageCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean setLanguage(CommandSender sender, String[] args) {
        if (!codes.contains(args[1])) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "LANG_NOT_VALID");
            return true;
        }
        // check file exists
        File file;
        file = new File(plugin.getDataFolder() + File.separator + "language" + File.separator + args[1] + ".yml");
        if (!file.isFile()) {
            // file not found
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "LANG_NOT_FOUND", args[1]);
            return true;
        }
        // load the language
        plugin.setLanguage(YamlConfiguration.loadConfiguration(file));
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "LANG_SET", Language.valueOf(args[1]).getLang());
        // set and save the config
        plugin.getConfig().set("preferences.language", args[1]);
        plugin.saveConfig();
        return true;
    }
}
