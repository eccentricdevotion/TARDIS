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

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.command.CommandSender;

/**
 * @author eccentric_nz
 */
class TARDISDevListCommand {

    private final TARDIS plugin;

    TARDISDevListCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean listStuff(CommandSender sender, String[] args) {
        if (args.length > 1 && (args[1].equalsIgnoreCase("preset_perms") || args[1].equalsIgnoreCase("perms") || args[1].equalsIgnoreCase("recipes") || args[1].equalsIgnoreCase("blueprints"))) {
            if (args[1].equalsIgnoreCase("perms")) {
                new TARDISPermissionLister(plugin).listPerms(sender);
                return true;
            } else if (args[1].equalsIgnoreCase("recipes")) {
                new TARDISRecipesLister(plugin).listRecipes(sender, args);
                return true;
            } else if (args[1].equalsIgnoreCase("blueprints")) {
                new TARDISBlueprintsLister().listBlueprints(sender);
                return true;
            } else {
                // preset permissions
                new TARDISPresetPermissionLister().list(sender);
                return true;
            }
        }
        return false;
    }
}
