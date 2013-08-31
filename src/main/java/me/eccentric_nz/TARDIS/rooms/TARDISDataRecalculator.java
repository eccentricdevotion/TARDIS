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
            // pistons
            case 29: // sticky base
            case 33: // piston base
            case 34: // piston extension
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
                    case 5:
                        data = 4;
                        break;
                    case 6:
                        data = 7;
                        break;
                    case 7:
                        data = 6;
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
                    default:
                        data = bit;
                        break;
                }
                break;
            // stairs
            case 53:
            case 67:
            case 108:
            case 109:
            case 114:
            case 128:
            case 134:
            case 135:
            case 136:
            case 156:
            case -100:
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
            case 23: // dispenser
            case 54: // chest
            case 61: // furnace
            case 65: // ladder
            case 68: // wall sign
            case 130: // ender chest
            case 146: // trapped chest
            case 158: // dropper
            case -98: // dropper
            case -110: // trapped chest
            case -126: // ender chest
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
            // beds
            case 26:
            // repeaters
            case 93:
            case 94:
            // trapdoor
            case 96:
            // cocoa
            case 127:
            // tripwire hooks
            case 131:
            case -125:
            // anvils
            case 145:
            case -111:
            // comparators
            case 149:
            case 150:
            case -106:
            case -107:
                switch (bit) {
                    case 0:
                        data = 2;
                        break;
                    case 1:
                        data = 3;
                        break;
                    case 2:
                        data = 0;
                        break;
                    case 3:
                        data = 1;
                        break;
                    case 4:
                        data = 6;
                        break;
                    case 5:
                        data = 7;
                        break;
                    case 6:
                        data = 4;
                        break;
                    case 7:
                        data = 5;
                        break;
                    case 8:
                        data = 10;
                        break;
                    case 9:
                        data = 11;
                        break;
                    case 10:
                        data = 8;
                        break;
                    case 11:
                        data = 9;
                        break;
                    case 12:
                        data = 14;
                        break;
                    case 13:
                        data = 15;
                        break;
                    case 14:
                        data = 12;
                        break;
                    case 15:
                        data = 13;
                        break;
                    default:
                        data = bit;
                        break;
                }
                break;
            // levers - only switch wall mounted ones
            case 69:
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
                    case 4:
                        data = 3;
                        break;
                    case 9:
                        data = 10;
                        break;
                    case 10:
                        data = 9;
                        break;
                    case 11:
                        data = 12;
                        break;
                    case 12:
                        data = 11;
                        break;
                    default:
                        data = bit;
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
            // torches
            case 50:
            case 75:
            case 76:
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
                    case 4:
                        data = 3;
                        break;
                    default:
                        data = bit;
                        break;
                }
                break;
            // hoppers
            case 154:
            case -102:
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
                    case 15:
                        data = 14;
                        break;
                    default:
                        data = bit;
                        break;
                }
                break;
            default:
                data = bit;
                break;
        }
        return data;
    }
}
