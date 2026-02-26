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
import me.eccentric_nz.TARDIS.commands.dev.lists.*;
import me.eccentric_nz.TARDIS.enumeration.Desktops;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

/**
 * @author eccentric_nz
 */
public class ListCommand {

    private final TARDIS plugin;

    public ListCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void listStuff(CommandSender sender, String which, String other) {
        if (!which.isEmpty() && (
                which.equalsIgnoreCase("preset_perms") ||
                which.equalsIgnoreCase("perms") ||
                which.equalsIgnoreCase("recipes") ||
                which.equalsIgnoreCase("blueprints") ||
                which.equalsIgnoreCase("commands") ||
                which.equalsIgnoreCase("block_colours") ||
                which.equalsIgnoreCase("change") ||
                which.equalsIgnoreCase("consoles") ||
                which.equalsIgnoreCase("trades")
        )) {
            if (which.equalsIgnoreCase("perms")) {
                if (!other.isEmpty()) {
                    new PermissionLister(plugin).listPermsHtml(sender);
                } else {
                    new PermissionLister(plugin).listPerms(sender);
                }
            } else if (which.equalsIgnoreCase("recipes")) {
                new RecipesLister(plugin).listRecipes(sender, other);
            } else if (which.equalsIgnoreCase("trades")) {
                new TradesLister(plugin).listConsolesAndRooms();
            } else if (which.equalsIgnoreCase("blueprints")) {
                new BlueprintsLister().listBlueprints(sender);
            } else if (which.equalsIgnoreCase("commands")) {
                if (!other.isEmpty()) {
                    new CommandsLister(plugin).listOtherTARDISCommands(sender);
                } else {
                    new CommandsLister(plugin).listTARDISCommands(sender);
                }
            } else if (which.equalsIgnoreCase("block_colours")) {
                plugin.getTardisHelper().listBlockColours();
            } else if (which.equalsIgnoreCase("change")) {
                for (Material m : TARDISConstants.CHAMELEON_BLOCKS_CHANGE) {
                    TARDISConstants.changeToMaterial(m);
                }
            } else if (which.equalsIgnoreCase("consoles")) {
                if (other.isEmpty()) {
                    for (BlueprintConsole bpc : BlueprintConsole.values()) {
                        String perm = bpc.getPermission().split("\\.")[1];
                        Schematic console = Desktops.getBY_PERMS().get(perm);
                        if (console == null) {
                            plugin.debug("Schematic by perm {" + perm + "} was null");
                        } else {
                            plugin.debug("Schematic by perm {" + perm + "} has material " + console.getSeedMaterial());
                        }
                    }
                } else {
                    new ConsoleCostLister(plugin).actualArtron();
                }
            } else {
                // preset permissions
                new PresetPermissionLister().list(sender);
            }
        }
    }
}
