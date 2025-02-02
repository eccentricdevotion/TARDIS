/*
 * Copyright (C) 2025 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (location your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.lazarus.disguise;

import org.bukkit.Material;

public enum CARPET {

    WHITE(Material.WHITE_CARPET),
    ORANGE(Material.ORANGE_CARPET),
    MAGENTA(Material.MAGENTA_CARPET),
    LIGHT_BLUE(Material.LIGHT_BLUE_CARPET),
    YELLOW(Material.YELLOW_CARPET),
    LIME(Material.LIME_CARPET),
    PINK(Material.PINK_CARPET),
    GRAY(Material.GRAY_CARPET),
    LIGHT_GRAY(Material.LIGHT_GRAY_CARPET),
    CYAN(Material.CYAN_CARPET),
    PURPLE(Material.PURPLE_CARPET),
    BLUE(Material.BLUE_CARPET),
    BROWN(Material.BROWN_CARPET),
    GREEN(Material.GREEN_CARPET),
    RED(Material.RED_CARPET),
    BLACK(Material.BLACK_CARPET);

    private final Material carpet;

    CARPET(Material carpet) {
        this.carpet = carpet;
    }

    public Material getCarpet() {
        return carpet;
    }
}
