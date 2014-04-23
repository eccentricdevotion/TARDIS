/*
 * Copyright (C) 2014 eccentric_nz
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
public class TARDISStairRecalculator {

    /**
     * Recalculate the data for directional block (TRAPDOOR) when the TARDIS
     * preset changes direction.
     *
     * @param b the byte stored in the preset data
     * @param d the new direction of the TARDIS
     * @param col the preset column that is being calculated
     * @param duck whether this is the Rubber Duck preset
     * @return the recalculated byte
     */
    public byte recalculate(byte b, COMPASS d, int col, boolean duck) {
        byte recalc;
        switch (d) {
            case SOUTH:
                switch (b) {
                    case 0:
                        recalc = (byte) 2;
                        break;
                    case 1:
                        recalc = (byte) 3;
                        break;
                    case 2:
                        recalc = (byte) 1;
                        break;
                    case 3:
                        recalc = (byte) 0;
                        break;
                    case 4:
                        recalc = (byte) 6;
                        break;
                    case 5:
                        recalc = (byte) 7;
                        break;
                    case 6:
                        recalc = (byte) 5;
                        break;
                    case 7:
                        recalc = (byte) 4;
                        break;
                    default:
                        recalc = b;
                }
                break;
            case WEST:
                if (duck) {
                    switch (b) {
                        case 0:
                            recalc = (byte) 1;
                            break;
                        case 1:
                            recalc = (byte) 0;
                            break;
                        case 2:
                            recalc = (byte) 3;
                            break;
                        case 3:
                            recalc = (byte) 2;
                            break;
                        case 4:
                            recalc = (byte) 5;
                            break;
                        case 5:
                            recalc = (byte) 4;
                            break;
                        case 6:
                            recalc = (byte) 7;
                            break;
                        case 7:
                            recalc = (byte) 6;
                            break;
                        default:
                            recalc = b;
                    }
                } else {
                    if (col == 3 || col == 7) {
                        switch (b) {
                            case 0:
                                recalc = (byte) 1;
                                break;
                            case 1:
                                recalc = (byte) 0;
                                break;
                            case 4:
                                recalc = (byte) 5;
                                break;
                            case 5:
                                recalc = (byte) 4;
                                break;
                            default:
                                recalc = b;
                        }
                    } else {
                        recalc = b;
                    }
                }
                break;
            default:
                switch (b) {
                    case 0:
                        recalc = (byte) 3;
                        break;
                    case 1:
                        recalc = (byte) 2;
                        break;
                    case 2:
                        recalc = (byte) 0;
                        break;
                    case 3:
                        recalc = (byte) 1;
                        break;
                    case 4:
                        recalc = (byte) 7;
                        break;
                    case 5:
                        recalc = (byte) 6;
                        break;
                    case 6:
                        recalc = (byte) 4;
                        break;
                    case 7:
                        recalc = (byte) 5;
                        break;
                    default:
                        recalc = b;
                }
                break;
        }
        return recalc;
    }

}
