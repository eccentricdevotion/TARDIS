/*
 * Copyright (C) 2021 eccentric_nz
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
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * The Weeping Angels are a species of quantum-locked humanoids from the early universe. They are known for being
 * murderous psychopaths, eradicating their victims "mercifully" by dropping them into the past and letting them live
 * out their full lives, just in a different time period. This, in turn, allows them to live off the remaining time
 * energy of the victim's life.
 *
 * @author eccentric_nz
 */
public class TARDISEntityGriefListener implements Listener {

    private final TARDIS plugin;
    private final List<EntityType> ents = new ArrayList<>();

    public TARDISEntityGriefListener(TARDIS plugin) {
        this.plugin = plugin;
        ents.add(EntityType.ENDER_DRAGON);
        ents.add(EntityType.FIREBALL);
        ents.add(EntityType.WITHER);
    }

    /**
     * Listens for Fireball, Wither and Dragon entity interaction with the TARDIS blocks. If the block is a TARDIS
     * block, then the block change event is cancelled.
     *
     * @param event an entity affecting a block
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void bossBlockBreak(EntityChangeBlockEvent event) {
        Block b = event.getBlock();
        String l = b.getLocation().toString();
        EntityType eType;
        try {
            eType = event.getEntityType();
        } catch (Exception e) {
            eType = null;
        }
        if (eType != null && ents.contains(eType)) {
            if (plugin.getGeneralKeeper().getProtectBlockMap().containsKey(l)) {
                event.setCancelled(true);
            }
        }
    }
}
