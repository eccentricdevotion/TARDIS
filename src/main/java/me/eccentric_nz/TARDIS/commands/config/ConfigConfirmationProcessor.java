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

import io.papermc.paper.dialog.DialogResponseView;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.entity.Player;

public class ConfigConfirmationProcessor {

    private final TARDIS plugin;
    private final Player player;

    public ConfigConfirmationProcessor(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void show(String key, DialogResponseView response) {
        // {database:"sqlite",id:[I;-880964298,2042840065,-1400329863,-338644980],mysql9database:"TARDIS",mysql9host:"localhost",mysql9password:"mysecurepassword",mysql9port:"3306",mysql9prefix:"",mysql9useSSL:0b,mysql9user:"bukkit"}
        // get the keys from the string - split(",")
        String raw = response.payload().string();
        for (String part : raw.substring(1, raw.length() - 1).split(",")) {
            // if the key is 'id' ignore it
            if (part.startsWith("id")) {
                continue;
            }
            if (!part.contains(":")) {
                continue;
            }
            // get the key and value - split(":")
            String[] colon = part.split(":");
            if (colon.length > 1) {
                // if the key contains a 9 it's a deep key, so replace it with a period (.)
                String sub = colon[0].replace("9", ".");
                // get the string value of the key
                String value = colon[1];
                // parse whether they are bool / string / float - booleans are 0b and 1b
                // and write the new values to the config
                if (value.startsWith("\"")) {
                    String str = StringUtils.strip(value, "\"");
                    // is it a number?
                    if (NumberUtils.isCreatable(str)) {
                        // damage_amount: 0.5 is the only float all other numbers are ints
                        if (sub.equals("damage_amount")) {
                            plugin.debug("float");
                            plugin.getConfig().set(key + "." + sub, TARDISNumberParsers.parseFloat(str));
                        } else {
                            plugin.debug("int");
                            plugin.getConfig().set(key + "." + sub, TARDISNumberParsers.parseInt(str));
                        }
                    } else {
                        // it's a string value
                        plugin.getConfig().set(key + "." + sub, str);
                    }
                } else if (value.equals("0b") || value.equals("1b")) {
                    // it's a boolean
                    plugin.getConfig().set(key + "." + sub, value.equals("1b"));
                } else {
                    // it's a number
                    // damage_amount: 0.5 is the only float all other numbers are ints
                    plugin.getConfig().set(key + "." + sub, sub.equals(key + ".damage_amount") ? TARDISNumberParsers.parseFloat(value) : TARDISNumberParsers.parseInt(value));
                }
            }
        }
        // save the config
        plugin.saveConfig();
        plugin.getMessenger().send(player, TardisModule.TARDIS, "CONFIG_SECTION_UPDATED", key);
//        plugin.debug(raw);
    }
}
