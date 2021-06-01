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
package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.blueprints.*;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

class TARDISBlueprintsLister {

    void listBlueprints(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "[TARDIS]" + ChatColor.RESET + " Blueprints:");
        for (BlueprintBase base : BlueprintBase.values()) {
            sender.sendMessage("BLUEPRINT_BASE_" + base.toString() + "(\"" + TARDISStringUtils.capitalise(base.toString()) + "\", Material.MUSIC_DISC_MELLOHI, 10000001, ShopItemRecipe.BLUEPRINT),");
        }
        for (BlueprintConsole console : BlueprintConsole.values()) {
            sender.sendMessage("BLUEPRINT_CONSOLE_" + console.toString() + "(\"" + TARDISStringUtils.capitalise(console.toString()) + "\", Material.MUSIC_DISC_MELLOHI, 10000001, ShopItemRecipe.BLUEPRINT),");
        }
        for (BlueprintFeature feature : BlueprintFeature.values()) {
            sender.sendMessage("BLUEPRINT_FEATURE_" + feature.toString() + "(\"" + TARDISStringUtils.capitalise(feature.toString()) + "\", Material.MUSIC_DISC_MELLOHI, 10000001, ShopItemRecipe.BLUEPRINT),");
        }
        for (BlueprintPreset preset : BlueprintPreset.values()) {
            sender.sendMessage("BLUEPRINT_PRESET_" + preset.toString() + "(\"" + TARDISStringUtils.capitalise(preset.toString()) + "\", Material.MUSIC_DISC_MELLOHI, 10000001, ShopItemRecipe.BLUEPRINT),");
        }
        for (BlueprintRoom room : BlueprintRoom.values()) {
            sender.sendMessage("BLUEPRINT_ROOM_" + room.toString() + "(\"" + TARDISStringUtils.capitalise(room.toString()) + "\", Material.MUSIC_DISC_MELLOHI, 10000001, ShopItemRecipe.BLUEPRINT),");
        }
        for (BlueprintSonic sonic : BlueprintSonic.values()) {
            sender.sendMessage("BLUEPRINT_SONIC_" + sonic.toString() + "(\"" + TARDISStringUtils.capitalise(sonic.toString()) + "\", Material.MUSIC_DISC_MELLOHI, 10000001, ShopItemRecipe.BLUEPRINT),");
        }
        for (BlueprintTravel travel : BlueprintTravel.values()) {
            sender.sendMessage("BLUEPRINT_TRAVEL_" + travel.toString() + "(\"" + TARDISStringUtils.capitalise(travel.toString()) + "\", Material.MUSIC_DISC_MELLOHI, 10000001, ShopItemRecipe.BLUEPRINT),");
        }
    }
}
