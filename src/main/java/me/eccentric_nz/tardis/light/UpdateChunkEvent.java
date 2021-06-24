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

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class UpdateChunkEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancel;
    private ChunkInfo chunkCoord;
    private LightType lightType;

    public UpdateChunkEvent(ChunkInfo chunkCoord) {
        this(chunkCoord, LightType.BLOCK);
    }

    public UpdateChunkEvent(ChunkInfo chunkCoord, LightType lightType) {
        this.chunkCoord = chunkCoord;
        this.lightType = lightType;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean arg0) {
        cancel = arg0;
    }

    public ChunkInfo getChunkInfo() {
        return chunkCoord;
    }

    public void setChunkInfo(ChunkInfo chunkCoord) {
        this.chunkCoord = chunkCoord;
    }

    public LightType getLightType() {
        return lightType;
    }

    public void setLightType(LightType lightType) {
        this.lightType = lightType;
    }
}
