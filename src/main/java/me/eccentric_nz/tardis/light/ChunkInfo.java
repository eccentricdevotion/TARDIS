/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Vladimir Mikhailov <beykerykt@gmail.com>
 * Copyright (c) 2020 Qveshn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.eccentric_nz.tardis.light;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;

public class ChunkInfo {

    private final World world;
    private final int x;
    private final int z;
    private int y;
    private Collection<? extends Player> receivers;

    @Deprecated
    public ChunkInfo(World world, int chunkX, int chunkZ, Collection<? extends Player> players) {
        this(world, chunkX, 256, chunkZ, players);
    }

    public ChunkInfo(World world, int chunkX, int chunkYHeight, int chunkZ, Collection<? extends Player> players) {
        this.world = world;
        x = chunkX;
        y = chunkYHeight >> 4;
        z = chunkZ;
        receivers = players;
    }

    public World getWorld() {
        return world;
    }

    public int getChunkX() {
        return x;
    }

    public int getChunkY() {
        return y;
    }

    public int getChunkZ() {
        return z;
    }

    @Deprecated
    public int getChunkYHeight() {
        return y << 4;
    }

    @Deprecated
    public void setChunkYHeight(int y) {
        this.y = y;
    }

    public Collection<? extends Player> getReceivers() {
        return receivers;
    }

    public void setReceivers(Collection<? extends Player> receivers) {
        this.receivers = receivers;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((world == null) ? 0 : world.hashCode());
        result = prime * result + x;
        result = prime * result + z;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof ChunkInfo other)) {
            return false;
        }
        if (world != other.world) {
            return false;
        }
        return x == other.x && z == other.z && y == other.y;
    }

    @Override
    public String toString() {
        return "ChunkInfo [world=" + world + ", x=" + x + ", y=" + y + ", z=" + z + "]";
    }
}
