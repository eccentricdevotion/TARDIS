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
package me.eccentric_nz.TARDIS.commands.dev.wiki;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.command.CommandSender;

public class WikiRecipeCommand {

    private final TARDIS plugin;

    public WikiRecipeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void write(CommandSender sender, String arg) {
        switch (arg) {
            case "chest" -> new ChestBuilder(plugin).place(sender);
            case "boxes" -> new BoxesBuilder().place(sender);
            case "rotors" -> new RotorBuilder(plugin).place(sender);
            case "doors" -> new DoorBuilder(plugin).place(sender);
            case "shaped" -> new ShapedPageBuilder(plugin).compile();
            case "shapeless" -> new ShapelessPageBuilder(plugin).compile();
            case "chemistry" -> new ChemistryPageBuilder(plugin).compile();
            case "custom" -> new CustomPageBuilder(plugin).compile();
            default -> { }
        }
    }
}
