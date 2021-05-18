/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.junk;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Chunk;
import org.bukkit.Location;

/**
 * @author eccentric_nz
 */
public class TARDISJunkReturnRunnable implements Runnable {

	private final TARDIS plugin;
	private final long waitTime;

	public TARDISJunkReturnRunnable(TARDIS plugin) {
		this.plugin = plugin;
		waitTime = this.plugin.getConfig().getLong("junk.return") * 1000;
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
