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
package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.*;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BlueprintLister {

    private final TARDIS plugin;

    public BlueprintLister(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void list(CommandSender sender, String type) {
        String message = "%s -> %s";
        List<String> messages = new ArrayList<>();
        switch (type.toUpperCase(Locale.ROOT)) {
            case "BASE" -> {
                for (BlueprintBase base : BlueprintBase.values()) {
                    messages.add(String.format(message, base.toString(), base.getPermission()));
                }
            }
            case "BLASTER" -> {
                for (BlueprintBlaster blaster : BlueprintBlaster.values()) {
                    messages.add(String.format(message, blaster.toString(), blaster.getPermission()));
                }
            }
            case "CONSOLE" -> {
                for (BlueprintConsole console : BlueprintConsole.values()) {
                    messages.add(String.format(message, console.toString(), console.getPermission()));
                }
            }
            case "FEATURE" -> {
                for (BlueprintFeature feature : BlueprintFeature.values()) {
                    messages.add(String.format(message, feature.toString(), feature.getPermission()));
                }
            }
            case "PRESET" -> {
                for (BlueprintPreset preset : BlueprintPreset.values()) {
                    messages.add(String.format(message, preset.toString(), preset.getPermission()));
                }
            }
            case "ROOM" -> {
                for (BlueprintRoom room : BlueprintRoom.values()) {
                    messages.add(String.format(message, room.toString(), room.getPermission()));
                }
            }
            case "SHOP" -> {
                for (BlueprintShop shop : BlueprintShop.values()) {
                    messages.add(String.format(message, shop.toString(), shop.getPermission()));
                }
            }
            case "SONIC" -> {
                for (BlueprintSonic sonic : BlueprintSonic.values()) {
                    messages.add(String.format(message, sonic.toString(), sonic.getPermission()));
                }
            }
            case "TRAVEL" -> {
                for (BlueprintTravel travel : BlueprintTravel.values()) {
                    messages.add(String.format(message, travel.toString(), travel.getPermission()));
                }
            }
            case "VORTEX_MANIPULATOR" -> {
                for (BlueprintVortexManipulator manipulator : BlueprintVortexManipulator.values()) {
                    messages.add(String.format(message, manipulator.toString(), manipulator.getPermission()));
                }
            }
            case "WEEPING_ANGELS" -> {
                for (BlueprintWeepingAngels angel : BlueprintWeepingAngels.values()) {
                    messages.add(String.format(message, angel.toString(), angel.getPermission()));
                }
            }
            default -> {
                // do nothing
            }
        }
        if (!messages.isEmpty()) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "BLUEPRINTS_LIST", type.toUpperCase(Locale.ROOT));
            for (String s : messages) {
                sender.sendMessage(s);
            }
        }
    }
}
