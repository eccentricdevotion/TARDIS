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
package me.eccentric_nz.TARDIS.rooms;

/**
 * Lady Calcula was the wife of Colonel Nasgard and the mother of Yarvell and
 * Davros, the latter by her lover Councillor Quested. She was a senior figure
 * in Kaled politics who lived on Skaro towards the end of the Thousand Year War
 * between the Kaleds and the Thals.
 *
 * @author eccentric_nz
 */
public class TARDISDataRecalculator {

    private TARDISDataRecalculator() {
    }

    /**
     * A method to calculate the data bit for a block based on the direction a
     * room is facing when grown.
     *
     * @param id the TypeId of the block we want to re-calculate the data for.
     * @param bit the current Data of the block we want to re-calculate the data
     * for.
     * @return the new Data value based on the direction the room is facing
     */
    public static byte calculateData(int id, byte bit) {
        byte data;
        switch (id) {
            // stairs
            case 53:
            case 67:
            case 93: // repeater
            case 108:
            case 109:
            case 114:
            case 128:
            case 134:
            case 135:
            case 136:
            case 149:
            case 150:
            case 156:
            case -100:
            case -106:
            case -107:
            case -120:
            case -121:
            case -122:
                switch (bit) {
                    case 0:
                        data = 1;
                        break;
                    case 1:
                        data = 0;
                        break;
                    case 2:
                        data = 3;
                        break;
                    case 3:
                        data = 2;
                        break;
                    case 4:
                        data = 5;
                        break;
                    case 5:
                        data = 4;
                        break;
                    case 6:
                        data = 7;
                        break;
                    case 7:
                        data = 6;
                        break;
                    case 8:
                        data = 9;
                        break;
                    case 9:
                        data = 8;
                        break;
                    case 10:
                        data = 11;
                        break;
                    case 11:
                        data = 10;
                        break;
                    case 12:
                        data = 13;
                        break;
                    case 13:
                        data = 12;
                        break;
                    case 14:
                        data = 15;
                        break;
                    default:
                        data = 14;
                        break;
                }
                break;
            case 54: // chest
            case 61: // furnace
            case 65: // ladder
            case 68: // wall sign
                switch (bit) {
                    case 2:
                        data = 3;
                        break;
                    case 3:
                        data = 2;
                        break;
                    case 4:
                        data = 5;
                        break;
                    default:
                        data = 4;
                        break;
                }
                break;
            // mushroom
            case 99:
            case 100:
                switch (bit) {
                    case 1:
                        data = 9;
                        break;
                    case 2:
                        data = 8;
                        break;
                    case 3:
                        data = 7;
                        break;
                    case 4:
                        data = 6;
                        break;
                    case 6:
                        data = 4;
                        break;
                    case 7:
                        data = 3;
                        break;
                    case 8:
                        data = 2;
                        break;
                    case 9:
                        data = 1;
                        break;
                    default:
                        data = bit;
                        break;
                }
                break;
            // cocoa
            case 127:
                switch (bit) {
                    case 1:
                        data = 3;
                        break;
                    case 2:
                        data = 0;
                        break;
                    case 3:
                        data = 1;
                        break;
                    default:
                        data = 2;
                        break;
                }
                break;
            // buttons
            case 77:
            case 143:
            case -113:
                switch (bit) {
                    case 1:
                        data = 2;
                        break;
                    case 2:
                        data = 1;
                        break;
                    case 3:
                        data = 4;
                        break;
                    default:
                        data = 3;
                        break;
                }
                break;
            default:
                data = 0;
                break;
        }
        return data;
    }
}
