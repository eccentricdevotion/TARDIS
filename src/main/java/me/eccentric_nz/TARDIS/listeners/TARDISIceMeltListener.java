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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;

/**
 * In 1892 London, the Great Intelligence brought snowmen to life to take over the world. The snowmen could be summoned
 * if someone was thinking about them, thanks to being made of telepathic snow, but if said person thought about melting
 * the snowmen, they would melt.
 *
 * @author eccentric_nz
 */
public class TARDISIceMeltListener implements Listener {

    private final TARDIS plugin;

    public TARDISIceMeltListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for ice melting when the TARDIS Police Box is materialising. If the block is contained in the blocks
     * table then it cancels the event.
     *
     * @param event ice melting
     */
    @EventHandler(ignoreCancelled = true)
    public void onIceMelt(BlockFadeEvent event) {
        Block b = event.getBlock();
        Material m = b.getType();
        if (m.equals(Material.ICE)) {
            String l = b.getLocation().toString();
            if (plugin.getGeneralKeeper().getProtectBlockMap().containsKey(l)) {
                event.setCancelled(true);
            }
        }
    }
}
