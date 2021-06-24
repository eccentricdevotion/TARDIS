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

import me.eccentric_nz.tardis.TardisPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Light {

    // To synchronize nms create/delete light methods to avoid conflicts in multi-threaded calls. Got a better idea?
    private static final Object LOCK = new Object();
    private static final NmsHandler NMS_HANDLER = new NmsHandler();

    /**
     * Create a light source
     *
     * @param location the location where the light will be created
     */
    public static void createLight(Location location) {
        createLight(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), LightType.BLOCK, 15, true);
        Collection<Player> players = Objects.requireNonNull(location.getWorld()).getPlayers();
        for (ChunkInfo chunkInfo : Light.collectChunks(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), LightType.BLOCK, 15)) {
            Light.updateChunk(chunkInfo, LightType.BLOCK, players);
        }
    }

    /**
     * Create a light source
     *
     * @param world     the world where the Light will be created
     * @param x         the x coordinate of the Light
     * @param y         the y coordinate of the Light
     * @param z         the z coordinate of the Light
     * @param lightType the LightType of the Light
     */
    public static boolean createLight(World world, int x, int y, int z, LightType lightType, int lightlevel, boolean async) {
        CreateLightEvent event = new CreateLightEvent(world, x, y, z, lightType, lightlevel, async);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            Runnable request = () -> {
                synchronized (LOCK) {
                    NMS_HANDLER.createLight(event.getWorld(), event.getX(), event.getY(), event.getZ(), event.getLightType(), event.getLightLevel());
                }
            };
            if (event.isAsync()) {
                TardisPlugin.MACHINE.addToQueue(request);
            } else {
                request.run();
            }
            return true;
        }
        return false;
    }

    /**
     * Delete a light source
     *
     * @param location the location where the light will be removed
     */
    public static void deleteLight(Location location) {
        deleteLight(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), LightType.BLOCK, true);
        Collection<Player> players = Objects.requireNonNull(location.getWorld()).getPlayers();
        for (ChunkInfo chunkInfo : Light.collectChunks(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), LightType.BLOCK, 15)) {
            Light.updateChunk(chunkInfo, LightType.BLOCK, players);
        }
    }

    /**
     * Delete a light source
     *
     * @param world     the world where the Light will be removed
     * @param x         the x coordinate of the Light
     * @param y         the y coordinate of the Light
     * @param z         the z coordinate of the Light
     * @param lightType the LightType of the Light
     */
    public static boolean deleteLight(World world, int x, int y, int z, LightType lightType, boolean async) {
        DeleteLightEvent event = new DeleteLightEvent(world, x, y, z, lightType, async);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            Runnable request = () -> NMS_HANDLER.deleteLight(event.getWorld(), event.getX(), event.getY(), event.getZ(), event.getLightType());
            if (event.isAsync()) {
                TardisPlugin.MACHINE.addToQueue(request);
            } else {
                request.run();
            }
            return true;
        }
        return false;
    }

    public static List<ChunkInfo> collectChunks(World world, int x, int y, int z, LightType lightType, int lightLevel) {
        return NMS_HANDLER.collectChunks(world, x, y, z, lightType, lightLevel);
    }

    public static boolean updateChunk(ChunkInfo info, LightType lightType, Collection<? extends Player> players) {
        UpdateChunkEvent event = new UpdateChunkEvent(info, lightType);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            TardisPlugin.MACHINE.addChunkToUpdate(info, lightType, players);
            return true;
        }
        return false;
    }
}
