/*
 * Copyright (C) 2019 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;

/**
 * @author eccentric_nz
 */
public class TARDISNumberParsers {

    /**
     * Parses a string for an integer.
     *
     * @param i the string to convert to an int.
     * @return a number
     */
    public static int parseInt(String i) {
        int num = 0;
        try {
            num = Integer.parseInt(i);
        } catch (NumberFormatException n) {
            TARDIS.plugin.debug("Could not convert to int, the string was: " + i);
            n.printStackTrace();
        }
        return num;
    }

    /**
     * Parses a string for a float.
     *
     * @param i the string to convert to an float.
     * @return a floating point number
     */
    public static float parseFloat(String i) {
        float num = 0.0f;
        try {
            num = Float.parseFloat(i);
        } catch (NumberFormatException n) {
            TARDIS.plugin.debug("Could not convert to float, the string was: " + i);
        }
        return num;
    }

    /**
     * Parses a string for a double.
     *
     * @param i the string to convert to an double.
     * @return a floating point number
     */
    public static double parseDouble(String i) {
        double num = 0.0d;
        try {
            num = Double.parseDouble(i);
        } catch (NumberFormatException n) {
            TARDIS.plugin.debug("Could not convert to double, the string was: " + i);
        }
        return num;
    }

    /**
     * Parses a string for a double.
     *
     * @param i the string to convert to an double.
     * @return a floating point number
     */
    public static long parseLong(String i) {
        long num = 0L;
        try {
            num = Long.parseLong(i);
        } catch (NumberFormatException n) {
            TARDIS.plugin.debug("Could not convert to double, the string was: " + i);
        }
        return num;
    }

    /**
     * Returns a rounded integer after division.
     *
     * @param num     the number being divided.
     * @param divisor the number to divide by.
     * @return a rounded number.
     */
    public static int roundUp(int num, int divisor) {
        return (num + divisor - 1) / divisor;
    }
}
