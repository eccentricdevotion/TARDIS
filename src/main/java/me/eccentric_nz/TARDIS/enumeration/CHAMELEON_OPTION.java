/*
 * Copyright (C) 2016 eccentric_nz
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
public enum CHAMELEON_OPTION {

    FACTORY(20, false),
    ADAPTIVE(21, true),
    INVISIBLE(22, true),
    PRESET(23, true),
    CONSTRUCT(24, true);

    public int slot;
    public boolean off;

    private CHAMELEON_OPTION(int slot, boolean off) {
        this.slot = slot;
        this.off = off;
    }

    public int getSlot() {
        return slot;
    }

    public boolean getOff() {
        return off;
    }
}
