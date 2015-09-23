/*
 *  Copyright 2015 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.junk;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Chunk;
import org.bukkit.Location;

/**
 *
 * @author eccentric_nz
 */
public class TARDISJunkReturnRunnable implements Runnable {

    private final TARDIS plugin;
    private final long waitTime;

    public TARDISJunkReturnRunnable(TARDIS plugin) {
        this.plugin = plugin;
        this.waitTime = this.plugin.getConfig().getLong("junk.return") * 1000;
    }

    @Override
    public void run() {
        // get time junk tardis was last used
        long lastUsed = plugin.getGeneralKeeper().getJunkTime();
        // get current time
        long now = System.currentTimeMillis();
        if (lastUsed + waitTime > now) {
            // check the Junk TARDIS is not home already
            TARDISJunkLocation tjl = new TARDISJunkLocation(plugin);
            // compare locations
            if (tjl.isNotHome()) {
                Location current = tjl.getCurrent();
                Location home = tjl.getHome();
                // load chunks first
                Chunk cChunk = current.getChunk();
                while (!cChunk.isLoaded()) {
                    cChunk.load();
                }
                Chunk hChunk = home.getChunk();
                while (!hChunk.isLoaded()) {
                    hChunk.load();
                }
                // bring her home
                new TARDISJunkReturn(plugin).recall(plugin.getConsole());
            }
        }
    }
}
