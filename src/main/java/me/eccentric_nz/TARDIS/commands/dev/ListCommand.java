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
package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.BlueprintConsole;
import me.eccentric_nz.TARDIS.commands.dev.lists.BlueprintsLister;
import me.eccentric_nz.TARDIS.commands.dev.lists.CommandsLister;
import me.eccentric_nz.TARDIS.commands.dev.lists.PermissionLister;
import me.eccentric_nz.TARDIS.commands.dev.lists.RecipesLister;
import me.eccentric_nz.TARDIS.enumeration.Desktops;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

/**
 * @author eccentric_nz
 */
class ListCommand {

    private final TARDIS plugin;

    ListCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean listStuff(CommandSender sender, String[] args) {
        if (args.length > 1 && (args[1].equalsIgnoreCase("preset_perms") || args[1].equalsIgnoreCase("perms") || args[1].equalsIgnoreCase("recipes") || args[1].equalsIgnoreCase("blueprints") || args[1].equalsIgnoreCase("commands") || args[1].equalsIgnoreCase("block_colours") || args[1].equalsIgnoreCase("change") || args[1].equalsIgnoreCase("consoles"))) {
            if (args[1].equalsIgnoreCase("perms")) {
                if (args.length > 2) {
                    new PermissionLister(plugin).listPermsHtml(sender);
                } else {
                    new PermissionLister(plugin).listPerms(sender);
                }
                return true;
            } else if (args[1].equalsIgnoreCase("recipes")) {
                new RecipesLister(plugin).listRecipes(sender, args);
                return true;
            } else if (args[1].equalsIgnoreCase("blueprints")) {
                new BlueprintsLister().listBlueprints(sender);
                return true;
            } else if (args[1].equalsIgnoreCase("commands")) {
                if (args.length > 2) {
                    new CommandsLister(plugin).listOtherTARDISCommands(sender);
                } else {
                    new CommandsLister(plugin).listTARDISCommands(sender);
                }
                return true;
            } else if (args[1].equalsIgnoreCase("block_colours")) {
                plugin.getTardisHelper().listBlockColours();
                return true;
            } else if (args[1].equalsIgnoreCase("change")) {
                for (Material m : TARDISConstants.CHAMELEON_BLOCKS_CHANGE) {
                    TARDISConstants.changeToMaterial(m);
                }
                return true;
            } else if (args[1].equalsIgnoreCase("consoles")) {
                for (BlueprintConsole bpc : BlueprintConsole.values()) {
                    String perm = bpc.getPermission().split("\\.")[1];
                    Schematic console = Desktops.getBY_PERMS().get(perm);
                    if (console == null) {
                        plugin.debug("Schematic by perm {" + perm + "} was null");
                    } else {
                        plugin.debug("Schematic by perm {" + perm + "} has material " + console.getSeedMaterial());
                    }
                }
                return true;
            } else {
                // preset permissions
                new PresetPermissionLister().list(sender);
                return true;
            }
        }
        return false;
    }
}
