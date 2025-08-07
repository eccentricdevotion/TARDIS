/*
 * Copyright (C) 2025 eccentric_nz
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
import me.eccentric_nz.TARDIS.commands.dev.wiki.*;
import org.bukkit.command.CommandSender;

public class TARDISWikiRecipeCommand {

    private final TARDIS plugin;

    public TARDISWikiRecipeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean write(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return false;
        }
        if (args[1].equalsIgnoreCase("chest")) {
            return new ChestBuilder(plugin).place(sender);
        }
        if (args[1].equalsIgnoreCase("shaped")) {
            return new ShapedPageBuilder(plugin).compile();
        } else if (args[1].equalsIgnoreCase("shapeless")) {
            return new ShapelessPageBuilder(plugin).compile();
        } else if (args[1].equalsIgnoreCase("chemistry")) {
            return new ChemistryPageBuilder(plugin).compile();
        } else if (args[1].equalsIgnoreCase("custom")) {
            return new CustomPageBuilder(plugin).compile();
        }
        return true;
    }
}
