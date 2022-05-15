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
import me.eccentric_nz.TARDIS.chameleon.TARDISShellLoaderListener;
import me.eccentric_nz.TARDIS.listeners.TARDISAntiBuildListener;
import me.eccentric_nz.TARDIS.listeners.TARDISZeroRoomChatListener;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.planets.TARDISResourcePackSwitcher;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredListener;

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
    private final List<String> require_restart = Arrays.asList("use_worldguard", "walk_in_tardis", "open_door_policy", "handles");
    private final List<String> register = Arrays.asList("wg_flag_set", "zero_room", "switch_resource_packs", "load_shells");

    TARDISSetBooleanCommand(TARDIS plugin) {
        this.plugin = plugin;
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
        boolean bool = Boolean.parseBoolean(tf);
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
            } else if (first.equals("archive") || first.equals("blueprints") || first.equals("dynmap")) {
                plugin.getConfig().set(first + ".enabled", bool);
            } else {
                plugin.getConfig().set(first, bool);
            }
            plugin.saveConfig();
        }
        TARDISMessage.send(sender, "CONFIG_UPDATED", first);
        if (require_restart.contains(tolower)) {
            TARDISMessage.send(sender, "RESTART");
        }
        if (register.contains(tolower)) {
            for (RegisteredListener rls : HandlerList.getRegisteredListeners(plugin)) {
                switch (tolower) {
                    case "wg_flag_set" -> {
                        if (rls.getListener() instanceof TARDISAntiBuildListener anti) {
                            HandlerList.unregisterAll(anti);
                        } else {
                            plugin.getPM().registerEvents(new TARDISAntiBuildListener(plugin), plugin);
                        }
                    }
                    case "zero_room" -> {
                        if (rls.getListener() instanceof TARDISZeroRoomChatListener zero) {
                            HandlerList.unregisterAll(zero);
                        } else {
                            plugin.getPM().registerEvents(new TARDISZeroRoomChatListener(plugin), plugin);
                        }
                    }
                    case "switch_resource_packs" -> {
                        if (rls.getListener() instanceof TARDISResourcePackSwitcher pack) {
                            HandlerList.unregisterAll(pack);
                        } else {
                            plugin.getPM().registerEvents(new TARDISResourcePackSwitcher(plugin), plugin);
                        }
                    }
                    case "load_shells" -> {
                        if (rls.getListener() instanceof TARDISShellLoaderListener loader) {
                            HandlerList.unregisterAll(loader);
                        } else {
                            plugin.getPM().registerEvents(new TARDISShellLoaderListener(plugin), plugin);
                        }
                    }
                    default -> {
                        // do nothing
                    }
                }
            }
        }
        return true;
    }
}
