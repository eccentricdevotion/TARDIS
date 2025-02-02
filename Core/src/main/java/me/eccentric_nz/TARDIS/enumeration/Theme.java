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
package me.eccentric_nz.TARDIS.enumeration;

public enum Theme {

    SIXTY_THREE("theme_63", 2620),
    ZERO_FIVE("theme_05", 1340),
    TWENTY_TWENTY("theme_20", 2080),
    RANDOM("theme", 2620);

    final String filename;
    final long length;

    Theme(String filename, long length) {
        this.filename = filename;
        this.length = length;
    }

    public String getFilename() {
        return filename;
    }

    public long getLength() {
        return length;
    }
}
