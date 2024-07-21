/*
 * Copyright (C) 2024 eccentric_nz
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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.enumeration;

public enum Bind {

    SAVE(0, 3),
    CAVE(1, 2),
    HIDE(1, 2),
    HOME(1, 2),
    MAKE_HER_BLUE(1, 2),
    OCCUPY(1, 2),
    REBUILD(1, 2),
    PLAYER(2, 3),
    AREA(3, 3),
    BIOME(4, 3),
    CHAMELEON(5, 3),
    TRANSMAT(6, 2);

    private final int type;
    private final int args;

    Bind(int type, int args) {
        this.type = type;
        this.args = args;
    }

    public int getType() {
        return type;
    }

    public int getArgs() {
        return args;
    }
}
