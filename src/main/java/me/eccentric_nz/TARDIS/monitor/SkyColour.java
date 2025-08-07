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
package me.eccentric_nz.TARDIS.monitor;

import java.awt.*;

/**
 *
 * @author eccentric_nz
 */
public class SkyColour {
    
    /**
     * Get a sky colour for a normal world.
     *
     * @param t the time in ticks
     * @return a sky colour suitable for the time of day
     */
    public static Color getNormalFromTime(long t) {
        if (t > 0 && t <= 3500 || t > 23500) {
            return new Color(75, 120, 205);
        }
        if (t > 3500 && t <= 8000) {
            return new Color(95, 145, 220);
        }
        if (t > 8000 && t <= 13000) {
            return new Color(75, 85, 140);
        }
        if ((t > 13000 && t <= 15000) || t > 22000) {
            return new Color(25, 30, 55);
        }
        if (t > 15000) {
            return new Color(10, 20, 30);
        }
        return Color.blue;
    }
    
    /**
     * Get a sky colour for Skaro.
     *
     * @param t the time in ticks
     * @return a sky colour suitable for the time of day
     */
    public static Color getSkaroFromTime(long t) {
        if (t > 0 && t <= 3500 || t > 23500) {
            return new Color(120, 65, 170);
        }
        if (t > 3500 && t <= 8000) {
            return new Color(145, 100, 225);
        }
        if (t > 8000 && t <= 13000) {
            return new Color(120, 70, 205);
        }
        if (t > 13000 && t <= 15000 || t > 22000 && t <= 23500) {
            return new Color(30, 25, 55);
        }
        if (t > 15000 && t <= 22000) {
            return new Color(10, 20, 30);
        }
        return Color.blue;
    }
    
    /**
     * Get a sky colour for Gallifrey.
     *
     * @param t the time in ticks
     * @return a sky colour suitable for the time of day
     */
    public static Color getGallifreyFromTime(long t) {
        if (t > 0 && t <= 3500 || t > 23500) {
            return new Color(200, 135, 70);
        }
        if (t > 3500 && t <= 8000) {
            return new Color(225, 185, 95);
        }
        if (t > 8000 && t <= 13000) {
            return new Color(215, 160, 75);
        }
        if (t > 13000 && t <= 15000 || t > 22000 && t <= 23500) {
            return new Color(45, 35, 30);
        }
        if (t > 15000 && t <= 22000) {
            return new Color(10, 20, 30);
        }
        return Color.blue;
    }
}
