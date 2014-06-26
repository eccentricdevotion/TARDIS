/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.files;

import java.io.File;
import java.io.IOException;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Updates the configured language file with any new message strings. It will
 * then be up to the server administrator to update the translation.
 *
 * @author eccentric_nz
 */
public class TARDISLanguageUpdater {

    private final TARDIS plugin;

    public TARDISLanguageUpdater(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void update() {
        // get currently configured language
        String lang = plugin.getConfig().getString("preferences.language");
        if (lang.equals("en")) {
            return;
        }
        // get the English language config
        String en_path = plugin.getDataFolder() + File.separator + "language" + File.separator + "en.yml";
        File en_file = new File(en_path);
        FileConfiguration en = YamlConfiguration.loadConfiguration(en_file);
        // loop through the keys and set values that are missing
        int i = 0;
        for (String key : en.getKeys(false)) {
            if (!plugin.getLanguage().contains(key)) {
                plugin.getLanguage().set(key, en.getString(key));
                i++;
            }
        }
        try {
            // save the config
            String lang_path = plugin.getDataFolder() + File.separator + "language" + File.separator + lang + ".yml";
            plugin.getLanguage().save(new File(lang_path));
        } catch (IOException ex) {
            plugin.debug("Could not save language config file! " + ex.getMessage());
        }
        if (i > 0) {
            plugin.getConsole().sendMessage(plugin.getPluginName() + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new messages to " + lang + ".yml");
        }
    }
}
