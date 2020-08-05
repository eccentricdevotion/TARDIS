/*
 * Copyright (C) 2020 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
class TARDISSetBooleanCommand {

    private final TARDIS plugin;
    //    private final HashMap<String, String> require_restart = new HashMap<>();
    private final List<String> require_restart = Arrays.asList("use_worldguard", "wg_flag_set", "walk_in_tardis", "zero_room", "open_door_policy", "particles", "switch_resource_packs", "handles");

    TARDISSetBooleanCommand(TARDIS plugin) {
        this.plugin = plugin;
//        require_restart.put("allow_end_after_visit", "");
//        require_restart.put("allow_nether_after_visit", "");
//        require_restart.put("chemistry", "");
//        require_restart.put("use_worldguard", "");
//        require_restart.put("weather_set", "");
//        require_restart.put("wg_flag_set", "TARDISAntiBuildListener");
//        require_restart.put("walk_in_tardis", "");
//        require_restart.put("zero_room", "TARDISZeroRoomChatListener");
//        require_restart.put("open_door_policy", "");
//        require_restart.put("particles", "");
//        require_restart.put("perception_filter", "TARDISPerceptionFilterListener");
//        require_restart.put("switch_resource_packs", "TARDISResourcePackSwitcher");
//        require_restart.put("handles", "");
    }

    boolean setConfigBool(CommandSender sender, String[] args, String section) {
        String tolower = args[0].toLowerCase(Locale.ENGLISH);
        String first = (section.isEmpty()) ? tolower : section + "." + tolower;
        // check they typed true of false
        String tf = args[1].toLowerCase(Locale.ENGLISH);
        if (!tf.equals("true") && !tf.equals("false")) {
            TARDISMessage.send(sender, "TRUE_FALSE");
            return false;
        }
        boolean bool = Boolean.valueOf(tf);
        if (first.equals("switch_resource_packs")) {
            plugin.getPlanetsConfig().set("switch_resource_packs", bool);
            try {
                plugin.getPlanetsConfig().save(new File(plugin.getDataFolder(), "planets.yml"));
            } catch (IOException ex) {
                plugin.debug("Could not save planets.yml, " + ex.getMessage());
            }
        }
        if (first.equals("artron_furnace.particles")) {
            plugin.getArtronConfig().set(first, bool);
            try {
                plugin.getArtronConfig().save(new File(plugin.getDataFolder(), "artron.yml"));
            } catch (IOException ex) {
                plugin.debug("Could not save artron.yml, " + ex.getMessage());
            }
        } else {
            if (first.equals("abandon")) {
                if (tf.equals("true") && (plugin.getConfig().getBoolean("creation.create_worlds") || plugin.getConfig().getBoolean("creation.create_worlds_with_perms"))) {
                    TARDISMessage.message(sender, ChatColor.RED + "Abandoned TARDISes cannot be enabled as TARDISes are not stored in a TIPS world!");
                    return true;
                }
                plugin.getConfig().set("abandon.enabled", bool);
            } else if (first.equals("archive") || first.equals("blueprints")) {
                plugin.getConfig().set(first + ".enabled", bool);
            } else {
                plugin.getConfig().set(first, bool);
            }
            plugin.saveConfig();
        }
        TARDISMessage.send(sender, "CONFIG_UPDATED");
//        if (require_restart.containsKey(tolower)) {
        if (require_restart.contains(tolower)) {
            TARDISMessage.send(sender, "RESTART");
//            if (tf.equals("true")) {
//
//            } else {
//                // false
//                String listener = require_restart.get(tolower);
//                if (!listener.isEmpty()) {
//                    for (RegisteredListener rl : HandlerList.getRegisteredListeners(plugin)) {
//                        if (rl.getListener().getClass().getSimpleName().equalsIgnoreCase(listener)) {
//                            HandlerList.unregisterAll(rl.getListener());
//                        }
//                    }
//                }
//            }
        }
        return true;
    }
}
