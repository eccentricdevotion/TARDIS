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
package me.eccentric_nz.TARDIS.update;

import org.bukkit.ChatColor;

public enum TARDISUpdateableCategory {

    CONTROLS(ChatColor.GREEN, ChatColor.DARK_GREEN, "TARDIS Controls"),
    INTERFACES(ChatColor.RED, ChatColor.DARK_RED, "User Interfaces"),
    LOCATIONS(ChatColor.YELLOW, ChatColor.GOLD, "Spawn Locations"),
    OTHERS(ChatColor.LIGHT_PURPLE, ChatColor.DARK_PURPLE, "Others");

    private final ChatColor keyColour;
    private final ChatColor valueColour;
    private final String name;

    TARDISUpdateableCategory(ChatColor keyColour, ChatColor valueColour, String name) {
        this.keyColour = keyColour;
        this.valueColour = valueColour;
        this.name = name;
    }

    public ChatColor getKeyColour() {
        return keyColour;
    }

    public ChatColor getValueColour() {
        return valueColour;
    }

    public String getName() {
        return name;
    }
}
