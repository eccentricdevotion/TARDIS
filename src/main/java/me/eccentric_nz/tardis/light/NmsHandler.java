/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 The ImplexDevOne Project
 * Copyright (c) 2019 Vladimir Mikhailov <beykerykt@gmail.com>
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

import me.eccentric_nz.tardischunkgenerator.TardisHelperPlugin;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class NmsHandler extends NmsHandlerBase {

    private final Field lightEngineThreadedMailbox;
    private final Field threadedMailboxState;
    private final Method threadedMailboxDoLoopStep;
    private final Field lightEngineLayerC;
    private final Method lightEngineStorageD;
    private final Method lightEngineGraphA;

    public NmsHandler() {
        try {
            threadedMailboxDoLoopStep = ThreadedMailbox.class.getDeclaredMethod("f");
            threadedMailboxDoLoopStep.setAccessible(true);
            threadedMailboxState = ThreadedMailbox.class.getDeclaredField("c");
            threadedMailboxState.setAccessible(true);
            lightEngineThreadedMailbox = LightEngineThreaded.class.getDeclaredField("b");
            lightEngineThreadedMailbox.setAccessible(true);

            lightEngineLayerC = LightEngineLayer.class.getDeclaredField("c");
            lightEngineLayerC.setAccessible(true);
            lightEngineStorageD = LightEngineStorage.class.getDeclaredMethod("d");
            lightEngineStorageD.setAccessible(true);
            lightEngineGraphA = LightEngineGraph.class.getDeclaredMethod("a", long.class, long.class, int.class, boolean.class);
            lightEngineGraphA.setAccessible(true);
        } catch (Exception e) {
            throw toRuntimeException(e);
        }
    }

    private static RuntimeException toRuntimeException(Throwable e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        Class<? extends Throwable> cls = e.getClass();
        return new RuntimeException(String.format("(%s) %s", RuntimeException.class.getPackage().equals(cls.getPackage()) ? cls.getSimpleName() : cls.getName(), e.getMessage()), e);
    }

    @Override
    public void createLight(World world, int x, int y, int z, LightType lightType, int light) {
        setRawLightLevel(world, lightType, x, y, z, light);
        recalculateLighting(world, x, y, z, lightType);
    }

    @Override
    public void deleteLight(World world, int x, int y, int z, LightType lightType) {
        setRawLightLevel(world, lightType, x, y, z, 0);
        recalculateLighting(world, x, y, z, lightType);
    }

    @Override
    public void sendChunkSectionsUpdate(World world, int chunkX, int chunkZ, int sectionsMaskSky, int sectionsMaskBlock, Player player) {
        Chunk chunk = ((CraftWorld) world).getHandle().getChunkAt(chunkX, chunkZ);
        // https://wiki.vg/index.php?title=Pre-release_protocol&oldid=14804#Update_Light
        // https://github.com/flori-schwa/VarLight/blob/b9349499f9c9fb995c320f95eae9698dd85aad5c/v1_14_R1/src/me/florian/varlight/nms/v1_14_R1/NmsAdapter_1_14_R1.java#L451
        //
        // Two last argument is bit-mask what chunk sections to update. Mask containing
        // 18 bits, with the lowest bit corresponding to chunk section -1 (in the void,
        // y=-16 to y=-1) and the highest bit for chunk section 16 (above the world,
        // y=256 to y=271).
        PacketPlayOutLightUpdate packet = new PacketPlayOutLightUpdate(chunk.getPos(), chunk.e(), sectionsMaskSky, sectionsMaskBlock, true);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    private void setRawLightLevel(World world, LightType lightType, int blockX, int blockY, int blockZ, int lightLevel) {
        WorldServer worldServer = ((CraftWorld) world).getHandle();
        BlockPosition position = new BlockPosition(blockX, blockY, blockZ);
        LightEngineThreaded lightEngine = worldServer.getChunkProvider().getLightEngine();

        int finalLightLevel = lightLevel < 0 ? 0 : Math.min(lightLevel, 15);
        executeSync(lightEngine, () -> {
            if (lightType == LightType.SKY) {
                LightEngineLayerEventListener layer = lightEngine.a(EnumSkyBlock.SKY);
                if (!(layer instanceof LightEngineSky lightEngineSky)) {
                    return;
                }
                if (finalLightLevel == 0) {
                    lightEngineSky.a(position);
                } else if (lightEngineSky.a(SectionPosition.a(position)) != null) {
                    try {
                        lightEngineLayerA(lightEngineSky, position, finalLightLevel);
                    } catch (NullPointerException ignore) {
                        // To prevent problems with the absence of the NibbleArray, even
                        // if les.a(SectionPosition.a(position)) returns non-null value (corrupted data)
                    }
                }
            } else {
                LightEngineLayerEventListener layer = lightEngine.a(EnumSkyBlock.BLOCK);
                if (!(layer instanceof LightEngineBlock lightEngineBlock)) {
                    return;
                }
                if (finalLightLevel == 0) {
                    lightEngineBlock.a(position);
                } else if (lightEngineBlock.a(SectionPosition.a(position)) != null) {
                    try {
                        lightEngineBlock.a(position, finalLightLevel);
                    } catch (NullPointerException ignore) {
                        // To prevent problems with the absence of the NibbleArray, even
                        // if leb.a(SectionPosition.a(position)) returns non-null value (corrupted data)
                    }
                }
            }
        });
    }

    @Override
    protected int getViewDistance(Player player) {
        return player.getClientViewDistance();
    }

    @Override
    public List<ChunkInfo> collectChunks(World world, int blockX, int blockY, int blockZ, LightType lightType, int lightLevel) {
        if (lightType != LightType.SKY || lightLevel < 15) {
            return super.collectChunks(world, blockX, blockY, blockZ, lightType, lightLevel);
        }
        List<ChunkInfo> list = new ArrayList<>();
        Collection<Player> players = null;
        for (int dx = -1; dx <= 1; dx++) {
            int lightLevelX = lightLevel - getDeltaLight(blockX & 15, dx);
            if (lightLevelX > 0) {
                for (int dz = -1; dz <= 1; dz++) {
                    int lightLevelZ = lightLevelX - getDeltaLight(blockZ & 15, dz);
                    if (lightLevelZ > 0) {
                        if (lightLevelZ > getDeltaLight(blockY & 15, 1)) {
                            int sectionY = (blockY >> 4) + 1;
                            if (isValidSectionY(sectionY)) {
                                int chunkX = blockX >> 4;
                                int chunkZ = blockZ >> 4;
                                ChunkInfo chunkCoord = new ChunkInfo(world, chunkX + dx, sectionY << 4, chunkZ + dz, players != null ? players : (players = world.getPlayers()));
                                list.add(chunkCoord);
                            }
                        }
                        for (int sectionY = blockY >> 4; sectionY >= -1; sectionY--) {
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
        return list;
    }

    @Override
    public boolean isValidSectionY(int sectionY) {
        return sectionY >= -1 && sectionY <= 16;
    }

    @Override
    public int asSectionMask(int sectionY) {
        return 1 << sectionY + 1;
    }

    @Override
    protected void recalculateLighting(World world, int blockX, int blockY, int blockZ, LightType lightType) {
        WorldServer worldServer = ((CraftWorld) world).getHandle();
        LightEngineThreaded lightEngine = worldServer.getChunkProvider().getLightEngine();

        // Do not recalculate if no changes!
        if (!lightEngine.a()) {
            return;
        }

        executeSync(lightEngine, () -> {
            if (lightType == LightType.SKY) {
                LightEngineSky lightEngineSky = (LightEngineSky) lightEngine.a(EnumSkyBlock.SKY);
                lightEngineSky.a(Integer.MAX_VALUE, true, true);
            } else {
                LightEngineBlock lightEngineBlock = (LightEngineBlock) lightEngine.a(EnumSkyBlock.BLOCK);
                lightEngineBlock.a(Integer.MAX_VALUE, true, true);
            }
        });
    }

    private void executeSync(LightEngineThreaded lightEngine, Runnable task) {
        try {
            // ##### STEP 1: Pause light engine mailbox to process its tasks. #####
            ThreadedMailbox<Runnable> threadedMailbox = (ThreadedMailbox<Runnable>) lightEngineThreadedMailbox.get(lightEngine);
            // State flags bit mask:
            // 0x0001 - Closing flag (ThreadedMailbox is closing if non zero).
            // 0x0002 - Busy flag (ThreadedMailbox performs a task from queue if non zero).
            AtomicInteger stateFlags = (AtomicInteger) threadedMailboxState.get(threadedMailbox);
            int flags; // to hold values from stateFlags
            long timeToWait = -1;
            // Trying to set bit 1 in state bit mask when it is not set yet.
            // This will break the loop in other thread where light engine mailbox processes the taks.
            while (!stateFlags.compareAndSet(flags = stateFlags.get() & ~2, flags | 2)) {
                if ((flags & 1) != 0) {
                    // ThreadedMailbox is closing. The light engine mailbox may also stop processing tasks.
                    // The light engine mailbox can be close due to server shutdown or unloading (closing) the world.
                    // I am not sure is it unsafe to process our tasks while the world is closing is closing,
                    // but will try it (one can throw exception here if it crashes the server).
                    if (timeToWait == -1) {
                        // Try to wait 3 seconds until light engine mailbox is busy.
                        timeToWait = System.currentTimeMillis() + 3 * 1000;
                        Bukkit.getLogger().log(Level.INFO, TardisHelperPlugin.MESSAGE_PREFIX + "ThreadedMailbox is closing. Will wait...");
                    } else if (System.currentTimeMillis() >= timeToWait) {
                        throw new RuntimeException("Failed to enter critical section while ThreadedMailbox is closing");
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
            try {
                // ##### STEP 2: Safely running the task while the mailbox process is stopped. #####
                task.run();
            } finally {
                // STEP 3: ##### Continue light engine mailbox to process its tasks. #####
                // Firstly: Clearing busy flag to allow ThreadedMailbox to use it for running light engine tasks.
                while (!stateFlags.compareAndSet(flags = stateFlags.get(), flags & ~2)) {
                }
                // Secondly: IMPORTANT! The main loop of ThreadedMailbox was broken. Not completed tasks may still be
                // in the queue. Therefore, it is important to start the loop again to process tasks from the queue.
                // Otherwise, the main server thread may be frozen due to tasks stuck in the queue.
                threadedMailboxDoLoopStep.invoke(threadedMailbox);
            }
        } catch (InvocationTargetException e) {
            throw toRuntimeException(e.getCause());
        } catch (IllegalAccessException e) {
            throw toRuntimeException(e);
        }
    }

    private void lightEngineLayerA(LightEngineLayer<LightEngineStorageSky.a, LightEngineStorageSky> lightEngineLayer, BlockPosition var0, int var1) {
        try {
            LightEngineStorage lightEngineStorage = (LightEngineStorage) lightEngineLayerC.get(lightEngineLayer);
            lightEngineStorageD.invoke(lightEngineStorage);
            lightEngineGraphA.invoke(lightEngineLayer, 9223372036854775807L, var0.asLong(), 15 - var1, true);
        } catch (InvocationTargetException e) {
            throw toRuntimeException(e.getCause());
        } catch (IllegalAccessException e) {
            throw toRuntimeException(e);
        }
    }

    private int getDeltaLight(int x, int dx) {
        return (((x ^ ((-dx >> 4) & 15)) + 1) & (-(dx & 1)));
    }
}
