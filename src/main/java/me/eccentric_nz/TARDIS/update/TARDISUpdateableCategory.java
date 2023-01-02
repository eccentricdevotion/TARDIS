/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.update;

import net.md_5.bungee.api.ChatColor;

public enum TARDISUpdateableCategory {

    CONTROLS(ChatColor.GREEN, "TARDIS Controls"),
    INTERFACES(ChatColor.RED, "TARDIS User Interfaces"),
    LOCATIONS(ChatColor.AQUA, "TARDIS Internal Spawn Locations"),
    OTHERS(ChatColor.LIGHT_PURPLE, "Others");

    private final ChatColor colour;
    private final String name;

    TARDISUpdateableCategory(ChatColor colour, String name) {
        this.colour = colour;
        this.name = name;
    }

    public ChatColor getColour() {
        return colour;
    }

    public String getName() {
        return name;
    }
}
