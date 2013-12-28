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
package me.eccentric_nz.TARDIS.utility.recalculators;

import me.eccentric_nz.TARDIS.enumeration.COMPASS;

/**
 *
 * @author eccentric_nz
 */
public class TARDISButtonRecalculator {

    /**
     * Recalculate the data for directional block (TRAPDOOR) when the TARDIS
     * preset changes direction.
     *
     * @param b the byte stored in the preset data
     * @param d the new direction of the TARDIS
     * @return the recalculated byte
     */
    public byte recalculate(byte b, COMPASS d) {
        byte recalc;
        switch (d) {
            case SOUTH:
                switch (b) {
                    case 1:
                        recalc = 3;
                        break;
                    case 2:
                        recalc = 4;
                        break;
                    case 3:
                        recalc = 2;
                        break;
                    case 4:
                        recalc = 1;
                        break;
                    default:
                        recalc = b;
                }
                break;
            case WEST:
                switch (b) {
                    case 1:
                        recalc = 2;
                        break;
                    case 2:
                        recalc = 1;
                        break;
                    default:
                        recalc = b;
                }
                break;
            default:
                switch (b) {
                    case 1:
                        recalc = 4;
                        break;
                    case 2:
                        recalc = 3;
                        break;
                    case 3:
                        recalc = 1;
                        break;
                    case 4:
                        recalc = 2;
                        break;
                    default:
                        recalc = b;
                }
                break;
        }
        return recalc;
    }

}
