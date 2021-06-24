/*
 * The MIT License (MIT)
 *
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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class NmsHandlerBase implements InmsHandler {

    protected int getViewDistance(Player player) {
        return Bukkit.getViewDistance();
    }

    private boolean isVisibleToPlayer(World world, int chunkX, int chunkZ, Player player) {
        Location location = player.getLocation();
        if (!world.equals(location.getWorld())) {
            return false;
        }
        double dx = chunkX - (location.getBlockX() >> 4);
        double dz = chunkZ - (location.getBlockZ() >> 4);
        return (int) Math.sqrt(dx * dx + dz * dz) < getViewDistance(player);
    }

    @Override
    public List<ChunkInfo> collectChunks(World world, int blockX, int blockY, int blockZ, LightType lightType, int lightLevel) {
        List<ChunkInfo> list = new ArrayList<>();
        Collection<Player> players = null;
        if (lightLevel > 0) {
            for (int dx = -1; dx <= 1; dx++) {
                int lightLevelX = lightLevel - getDeltaLight(blockX & 15, dx);
                if (lightLevelX > 0) {
                    for (int dz = -1; dz <= 1; dz++) {
                        int lightLevelZ = lightLevelX - getDeltaLight(blockZ & 15, dz);
                        if (lightLevelZ > 0) {
                            for (int dy = -1; dy <= 1; dy++) {
                                if (lightLevelZ > getDeltaLight(blockY & 15, dy)) {
                                    int sectionY = (blockY >> 4) + dy;
                                    if (isValidSectionY(sectionY)) {
                                        int chunkX = blockX >> 4;
                                        int chunkZ = blockZ >> 4;
                                        ChunkInfo chunkCoord = new ChunkInfo(world, chunkX + dx, sectionY << 4, chunkZ + dz, players != null ? players : (players = world.getPlayers()));
                                        list.add(chunkCoord);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    @Override
    public void sendChunkSectionsUpdate(World world, int chunkX, int chunkZ, int sectionsMaskSky, int sectionsMaskBlock, Collection<? extends Player> players) {
        for (Player player : players) {
            sendChunkSectionsUpdate(world, chunkX, chunkZ, sectionsMaskSky, sectionsMaskBlock, player);
        }
    }

    @Override
    public boolean isValidSectionY(int sectionY) {
        return sectionY >= 0 && sectionY < 16;
    }

    @Override
    public int asSectionMask(int sectionY) {
        return 1 << sectionY;
    }

    @Override
    public Collection<? extends Player> filterVisiblePlayers(World world, int chunkX, int chunkZ, Collection<? extends Player> players) {
        List<Player> result = new ArrayList<>();
        for (Player player : players) {
            if (isVisibleToPlayer(world, chunkX, chunkZ, player)) {
                result.add(player);
            }
        }
        return result;
    }

    private int getDeltaLight(int x, int dx) {
        return (((x ^ ((-dx >> 4) & 15)) + 1) & (-(dx & 1)));
    }

    protected abstract void recalculateLighting(World world, int x, int y, int z, LightType lightType);
}
