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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.destroyers;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Wolf;

/**
 * The Daleks were a warlike race who waged war across whole civilisations and
 * races all over the universe. Advance and Attack! Attack and Destroy! Destroy
 * and Rejoice!
 *
 * @author eccentric_nz
 */
public class TARDISExterminator {

    /**
     * Removes all living non-human, untamed entities from a world.
     *
     * @param w the world to remove entities from.
     */
    public void exterminate(World w) {
        for (Chunk c : w.getLoadedChunks()) {
            Entity[] entities = c.getEntities();
            for (Entity e : entities) {
                if (e instanceof LivingEntity && !(e instanceof HumanEntity)) {
                    if (e instanceof Wolf && ((Wolf) e).isTamed()) {
                        return;
                    }
                    if (e instanceof Ocelot && ((Ocelot) e).isTamed()) {
                        return;
                    }
                    e.remove();
                }
            }
        }
    }
}
