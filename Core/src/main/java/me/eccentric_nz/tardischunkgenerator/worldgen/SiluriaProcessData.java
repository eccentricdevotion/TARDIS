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
package me.eccentric_nz.tardischunkgenerator.worldgen;

import java.util.HashMap;
import org.bukkit.util.BlockVector;

public class SiluriaProcessData {

    public int x;
    public int y;
    public int z;
    public HashMap<BlockVector, String> grid;

    public SiluriaProcessData(int x, int y, int z, HashMap<BlockVector, String> grid) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.grid = grid;
    }
}
