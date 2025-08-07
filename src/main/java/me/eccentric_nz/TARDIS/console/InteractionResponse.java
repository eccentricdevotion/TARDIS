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
package me.eccentric_nz.TARDIS.console;

import me.eccentric_nz.TARDIS.TARDIS;

import java.util.HashMap;
import java.util.List;

public class InteractionResponse {

    public static final HashMap<ConsoleInteraction, List<String>> randomRange = new HashMap<>();
    public static final List<String> environment = List.of("", "Current world", "Overworld", "The Nether", "The End");
    public static final List<Integer> levels = List.of(15, 13, 12, 11, 9, 7, 5, 3);
    private final int quarter;

    public InteractionResponse(TARDIS plugin) {
        int max = plugin.getConfig().getInt("travel.tp_radius");
        this.quarter = (max + 4 - 1) / 4;
    }

    public void init() {
        randomRange.put(ConsoleInteraction.MULTIPLIER, List.of("", "1x", "2x", "3x", "4x"));
        randomRange.put(ConsoleInteraction.X, List.of("", "r = " + (quarter), "r = " + (quarter * 2), "r = " + (quarter * 3), "r = " + (quarter * 4)));
        randomRange.put(ConsoleInteraction.Z, List.of("", "r = " + (quarter), "r = " + (quarter * 2), "r = " + (quarter * 3), "r = " + (quarter * 4)));
    }
}
