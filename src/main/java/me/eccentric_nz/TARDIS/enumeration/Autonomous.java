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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.enumeration;

public enum Autonomous {
    OFF(-1),
    HOME(12),
    AREAS(13),
    CONFIGURED_AREAS(14),
    CLOSEST(15),
    SAVE(16);

    final int slot;

    Autonomous(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }

    public enum Default {
        HOME(30),
        STAY(31);

        final int slot;

        Default(int slot) {
            this.slot = slot;
        }

        public int getSlot() {
            return slot;
        }
    }
}