/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

/**
 * A sonic blaster was type of weapon available in the 51st century. The blaster
 * used digital technology to create a sonic wave, projected into the form of
 * pulsing squares of blue light, which could cut through thick walls. It also
 * had a reverse function which could replace the removed chunk of material
 * afterwards.
 *
 * @author eccentric_nz
 */
public class TARDISChunkListener implements Listener {

    private final TARDIS plugin;

    public TARDISChunkListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for chunks unloading when a TARDIS room is growing. If the chunk
     * is contained in the roomChunksList then it cancels the event.
     *
     * @param event a chunk unloading
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        Chunk c = event.getChunk();
        if (plugin.getGeneralKeeper().getTardisChunkList().contains(c) || plugin.getGeneralKeeper().getRoomChunkList().contains(c)) {
            event.setCancelled(true);
        }
    }
}
