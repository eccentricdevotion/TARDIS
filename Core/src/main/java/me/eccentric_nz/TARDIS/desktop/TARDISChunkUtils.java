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
package me.eccentric_nz.TARDIS.desktop;

import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author eccentric_nz
 */
public class TARDISChunkUtils {

    public static List<Chunk> getConsoleChunks(Chunk c, Schematic s) {
        List<Chunk> chunks = new ArrayList<>();
        chunks.add(c);
        if (s.getConsoleSize().equals(ConsoleSize.MASSIVE)) {
            chunks.add(c.getWorld().getChunkAt(c.getX() + 2, c.getZ()));
            chunks.add(c.getWorld().getChunkAt(c.getX() + 2, c.getZ() + 1));
            chunks.add(c.getWorld().getChunkAt(c.getX() + 1, c.getZ() + 2));
            chunks.add(c.getWorld().getChunkAt(c.getX(), c.getZ() + 2));
            chunks.add(c.getWorld().getChunkAt(c.getX() + 2, c.getZ() + 2));
        }
        if (!s.getConsoleSize().equals(ConsoleSize.SMALL)) {
            chunks.add(c.getWorld().getChunkAt(c.getX() + 1, c.getZ()));
            chunks.add(c.getWorld().getChunkAt(c.getX(), c.getZ() + 1));
            chunks.add(c.getWorld().getChunkAt(c.getX() + 1, c.getZ() + 1));
        }
        return chunks;
    }

    /**
     * Checks whether a chunk is available to build a TARDIS in.
     *
     * @param w   the world the chunk is in.
     * @param x   the x coordinate of the chunk.
     * @param z   the z coordinate of the chunk.
     * @param wid the width of the schematic.
     * @param len the length of the schematic.
     * @return a list of Chunks.
     */
    public static List<Chunk> getConsoleChunks(World w, int x, int z, int wid, int len) {
        List<Chunk> chunks = new ArrayList<>();
        int cw = TARDISNumberParsers.roundUp(wid, 16);
        int cl = TARDISNumberParsers.roundUp(len, 16);
        // check all the chunks that will be used by the schematic
        for (int cx = 0; cx < cw; cx++) {
            for (int cz = 0; cz < cl; cz++) {
                Chunk chunk = w.getChunkAt((x + cx), (z + cz));
                chunks.add(chunk);
            }
        }
        return chunks;
    }
}
