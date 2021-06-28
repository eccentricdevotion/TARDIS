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

import java.util.*;
import java.util.concurrent.*;

public class RequestSteamMachine implements Runnable {

    private final Queue<Runnable> REQUEST_QUEUE = new ConcurrentLinkedQueue<>();
    private final Map<ChunkLocation, ChunkUpdateInfo> chunksToUpdate = new HashMap<>();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private boolean started;
    private int maxIterationsPerTick;
    // THREADS
    private ScheduledFuture<?> scheduledFuture;

    public void start(int ticks, int maxIterationsPerTick) {
        if (!started) {
            this.maxIterationsPerTick = maxIterationsPerTick;
            scheduledFuture = executor.scheduleWithFixedDelay(this, 0, 50L * ticks, TimeUnit.MILLISECONDS);
            started = true;
        }
    }

    public void shutdown() {
        if (started) {
            REQUEST_QUEUE.clear();
            maxIterationsPerTick = 0;
            scheduledFuture.cancel(false);
            started = false;
        }
    }

    public boolean isStarted() {
        return started;
    }

    public void addToQueue(Runnable request) {
        if (request != null) {
            REQUEST_QUEUE.add(request);
        }
    }

    public void addChunkToUpdate(ChunkInfo chunkInfo, LightType lightType, Collection<? extends Player> receivers) {
        int sectionY = chunkInfo.getChunkY();
        World world = chunkInfo.getWorld();
        InmsHandler nmsHandler = new NmsHandler();
        if (nmsHandler.isValidSectionY(world, sectionY)) {
            ChunkLocation chunkLocation = new ChunkLocation(chunkInfo.getWorld(), chunkInfo.getChunkX(), chunkInfo.getChunkZ());
            int sectionYMask = nmsHandler.asSectionMask(sectionY);
            Collection<Player> players = new ArrayList<>(receivers != null ? receivers : chunkInfo.getReceivers());
            addToQueue(() -> {
                ChunkUpdateInfo chunkUpdateInfo = chunksToUpdate.get(chunkLocation);
                if (chunkUpdateInfo == null) {
                    chunksToUpdate.put(chunkLocation, chunkUpdateInfo = new ChunkUpdateInfo());
                }
                chunkUpdateInfo.add(lightType, sectionYMask, players);
            });
        }
    }

    @Override
    public void run() {
        try {
            int iterationsCount = 0;
            Runnable request;
            while (iterationsCount < maxIterationsPerTick && (request = REQUEST_QUEUE.poll()) != null) {
                request.run();
                iterationsCount++;
            }
            InmsHandler nmsHandler = new NmsHandler();
            for (Map.Entry<ChunkLocation, ChunkUpdateInfo> item : chunksToUpdate.entrySet()) {
                ChunkLocation chunk = item.getKey();
                ChunkUpdateInfo chunkUpdateInfo = item.getValue();
                int sectionMaskSky = chunkUpdateInfo.getSectionMaskSky();
                int sectionMaskBlock = chunkUpdateInfo.getSectionMaskBlock();
                Collection<? extends Player> players = nmsHandler.filterVisiblePlayers(chunk.getWorld(), chunk.getX(), chunk.getZ(), chunkUpdateInfo.getPlayers());
                nmsHandler.sendChunkSectionsUpdate(chunk.getWorld(), chunk.getX(), chunk.getZ(), sectionMaskSky, sectionMaskBlock, players);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            chunksToUpdate.clear();
        }
    }
}
