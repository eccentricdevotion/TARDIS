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

import me.eccentric_nz.TARDIS.TARDISConstants;

/**
 *
 * @author eccentric_nz
 */
public class TARDISTrapdoorRecalculator {

    /**
     * Recalculate the data for directional block (TRAPDOOR) when the TARDIS
     * preset changes direction.
     *
     * @param b the byte stored in the preset data
     * @param d the new direction of the TARDIS
     * @return the recalculated byte
     */
    public byte recalculate(byte b, TARDISConstants.COMPASS d) {
        byte recalc;
        switch (d) {
            case SOUTH:
                switch (b) {
                    case 0:
                        recalc = 3;
                        break;
                    case 1:
                        recalc = 2;
                        break;
                    case 2:
                        recalc = 0;
                        break;
                    case 3:
                        recalc = 1;
                        break;
                    case 4:
                        recalc = 7;
                        break;
                    case 5:
                        recalc = 6;
                        break;
                    case 6:
                        recalc = 4;
                        break;
                    case 7:
                        recalc = 5;
                        break;
                    case 8:
                        recalc = 11;
                        break;
                    case 9:
                        recalc = 10;
                        break;
                    case 10:
                        recalc = 8;
                        break;
                    case 11:
                        recalc = 9;
                        break;
                    case 12:
                        recalc = 15;
                        break;
                    case 13:
                        recalc = 14;
                        break;
                    case 14:
                        recalc = 12;
                        break;
                    case 15:
                        recalc = 13;
                        break;
                    default:
                        recalc = b;
                }
                break;
            case WEST:
                switch (b) {
                    case 0:
                        recalc = 1;
                        break;
                    case 1:
                        recalc = 0;
                        break;
                    case 2:
                        recalc = 3;
                        break;
                    case 3:
                        recalc = 2;
                        break;
                    case 4:
                        recalc = 5;
                        break;
                    case 5:
                        recalc = 4;
                        break;
                    case 6:
                        recalc = 7;
                        break;
                    case 7:
                        recalc = 6;
                        break;
                    case 8:
                        recalc = 9;
                        break;
                    case 9:
                        recalc = 8;
                        break;
                    case 10:
                        recalc = 11;
                        break;
                    case 11:
                        recalc = 10;
                        break;
                    case 12:
                        recalc = 13;
                        break;
                    case 13:
                        recalc = 12;
                        break;
                    case 14:
                        recalc = 15;
                        break;
                    case 15:
                        recalc = 14;
                        break;
                    default:
                        recalc = b;
                }
                break;
            default:
                switch (b) {
                    case 0:
                        recalc = 2;
                        break;
                    case 1:
                        recalc = 3;
                        break;
                    case 2:
                        recalc = 1;
                        break;
                    case 3:
                        recalc = 0;
                        break;
                    case 4:
                        recalc = 6;
                        break;
                    case 5:
                        recalc = 7;
                        break;
                    case 6:
                        recalc = 5;
                        break;
                    case 7:
                        recalc = 4;
                        break;
                    case 8:
                        recalc = 10;
                        break;
                    case 9:
                        recalc = 11;
                        break;
                    case 10:
                        recalc = 9;
                        break;
                    case 11:
                        recalc = 8;
                        break;
                    case 12:
                        recalc = 14;
                        break;
                    case 13:
                        recalc = 15;
                        break;
                    case 14:
                        recalc = 13;
                        break;
                    case 15:
                        recalc = 12;
                        break;
                    default:
                        recalc = b;
                }
                break;
        }
        return recalc;
    }

}
