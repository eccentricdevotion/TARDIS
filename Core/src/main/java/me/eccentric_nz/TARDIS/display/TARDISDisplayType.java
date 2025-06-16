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
package me.eccentric_nz.TARDIS.display;

public enum TARDISDisplayType {

    ALL("&6X&7%X% &6Y&7%Y% &6Z&7%Z% &6F&7%FACING% (%FACING_XZ%) %TARGET_BLOCK%"),
    BIOME("&6B&7%BIOME%"),
    COORDS("&6X&7%X% &6Y&7%Y% &6Z&7%Z%"),
    DIRECTION("&6F&7%FACING% (%FACING_XZ%)"),
    LOCATOR("&6TL&7 %DIRECTIONS%"),
    TARGET_BLOCK("&6T&7%TARGET_BLOCK%"),
    WORLD("&6W&7%WORLD%");

    private final String format;

    TARDISDisplayType(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
