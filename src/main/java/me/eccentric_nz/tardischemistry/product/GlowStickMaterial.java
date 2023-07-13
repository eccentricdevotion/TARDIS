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
package me.eccentric_nz.tardischemistry.product;

import org.bukkit.Material;

class GlowStickMaterial {

    static boolean isCorrectMaterial(Material material) {
        return switch (material) {
            case WHITE_STAINED_GLASS_PANE, ORANGE_STAINED_GLASS_PANE, MAGENTA_STAINED_GLASS_PANE, LIGHT_BLUE_STAINED_GLASS_PANE, YELLOW_STAINED_GLASS_PANE, LIME_STAINED_GLASS_PANE, PINK_STAINED_GLASS_PANE, LIGHT_GRAY_STAINED_GLASS_PANE, BLUE_STAINED_GLASS_PANE, PURPLE_STAINED_GLASS_PANE, GREEN_STAINED_GLASS_PANE, BROWN_STAINED_GLASS_PANE, CYAN_STAINED_GLASS_PANE, RED_STAINED_GLASS_PANE -> true;
            default -> false;
        };
    }
}
