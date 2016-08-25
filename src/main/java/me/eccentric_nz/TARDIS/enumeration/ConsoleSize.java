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
public enum ConsoleSize {

    SMALL("small", "16 x 16 x 16 blocks"),
    MEDIUM("medium", "32 x 16 x 32 blocks"),
    TALL("tall", "32 x 32 x 32 blocks");

    private final String configPath;
    private final String blocks;

    private ConsoleSize(String configPath, String blocks) {
        this.configPath = configPath;
        this.blocks = blocks;
    }

    public String getConfigPath() {
        return configPath;
    }

    public String getBlocks() {
        return blocks;
    }

    public static ConsoleSize getByWidthAndHeight(int w, int h) {
        if (w < 17 && h < 17) {
            return SMALL;
        } else if (w > 16 && h < 17) {
            return MEDIUM;
        } else {
            return TALL;
        }
    }
}
