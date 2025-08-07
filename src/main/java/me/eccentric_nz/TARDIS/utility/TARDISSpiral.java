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
package me.eccentric_nz.TARDIS.utility;

import java.awt.*;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 */
class TARDISSpiral {

    static final List<Point> SPIRAL;

    static {

        SPIRAL = new ArrayList<>() {

            @Serial
            private static final long serialVersionUID = 3109256773218160485L;

            {
                add(new Point(0, 0));
                add(new Point(1, 0));
                add(new Point(1, 1));
                add(new Point(0, 1));
                add(new Point(-1, 1));
                add(new Point(-1, 0));
                add(new Point(-1, -1));
                add(new Point(0, -1));
                add(new Point(1, -1));
                add(new Point(2, -1));
                add(new Point(2, 0));
                add(new Point(2, 1));
                add(new Point(2, 2));
                add(new Point(1, 2));
                add(new Point(0, 2));
                add(new Point(-1, 2));
                add(new Point(-2, 2));
                add(new Point(-2, 1));
                add(new Point(-2, 0));
                add(new Point(-2, -1));
                add(new Point(-2, -2));
                add(new Point(-1, -2));
                add(new Point(0, -2));
                add(new Point(1, -2));
                add(new Point(2, -2));
                add(new Point(3, -2));
                add(new Point(3, -1));
                add(new Point(3, 0));
                add(new Point(3, 1));
                add(new Point(3, 2));
                add(new Point(3, 3));
                add(new Point(2, 3));
                add(new Point(1, 3));
                add(new Point(0, 3));
                add(new Point(-1, 3));
                add(new Point(-2, 3));
                add(new Point(-3, 3));
                add(new Point(-3, 2));
                add(new Point(-3, 1));
                add(new Point(-3, 0));
                add(new Point(-3, -1));
                add(new Point(-3, -2));
                add(new Point(-3, -3));
                add(new Point(-2, -3));
                add(new Point(-1, -3));
                add(new Point(0, -3));
                add(new Point(1, -3));
                add(new Point(2, -3));
                add(new Point(3, -3));
                add(new Point(4, -3));
                add(new Point(4, -2));
                add(new Point(4, -1));
                add(new Point(4, 0));
                add(new Point(4, 1));
                add(new Point(4, 2));
                add(new Point(4, 3));
                add(new Point(4, 4));
                add(new Point(3, 4));
                add(new Point(2, 4));
                add(new Point(1, 4));
                add(new Point(0, 4));
                add(new Point(-1, 4));
                add(new Point(-2, 4));
                add(new Point(-3, 4));
                add(new Point(-4, 4));
                add(new Point(-4, 3));
                add(new Point(-4, 2));
                add(new Point(-4, 1));
                add(new Point(-4, 0));
                add(new Point(-4, -1));
                add(new Point(-4, -2));
                add(new Point(-4, -3));
                add(new Point(-4, -4));
                add(new Point(-3, -4));
                add(new Point(-2, -4));
                add(new Point(-1, -4));
                add(new Point(0, -4));
                add(new Point(1, -4));
                add(new Point(2, -4));
                add(new Point(3, -4));
                add(new Point(4, -4));
            }
        };
    }
}
