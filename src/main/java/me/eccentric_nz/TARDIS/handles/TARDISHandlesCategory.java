/*
 * Copyright (C) 2018 eccentric_nz
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
package me.eccentric_nz.TARDIS.handles;

/**
 * @author eccentric_nz
 */
public enum TARDISHandlesCategory {

    BUTTON("Buttons", 11),
    COMMAND("TARDIS commands and actions", 7),
    CONTROL("Control and loop statements", 8),
    DISK("TARDIS Storage Disks", 4),
    EVENT("TARDIS events", 10),
    NUMBER("Numbers", 12),
    OPERATOR("Operators", 14),
    SELECTOR("Selectors", 7),
    VARIABLE("Variables", 14);

    private final String lore;
    private final int size;

    TARDISHandlesCategory(String lore, int size) {
        this.lore = lore;
        this.size = size;
    }

    public String getLore() {
        return lore;
    }

    public int getSize() {
        return size;
    }
}
