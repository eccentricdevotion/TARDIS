/*
 * Copyright (C) 2013 eccentric_nz
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

/**
 *
 * @author eccentric_nz
 */
public enum STORAGE {

    AREA("Area Storage", "areas", ""),
    BIOME("Biome Storage", "biomes", ""),
    CIRCUIT("Circuit Storage", "circuits", ""),
    FIRST("First Save Storage", "saves_one", ""),
    PLAYER("Player Storage", "players", ""),
    PRESET("Preset Storage", "presets", ""),
    SECOND("Second Save Storage", "saves_two", "");
    String title;
    String table;
    String empty;

    private STORAGE(String title, String table, String empty) {
        this.title = title;
        this.table = table;
        this.empty = empty;
    }

    public String getTitle() {
        return title;
    }

    public String getTable() {
        return table;
    }

    public String getEmpty() {
        return empty;
    }
}
