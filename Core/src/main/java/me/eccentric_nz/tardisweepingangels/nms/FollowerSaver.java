/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.tardisweepingangels.nms;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Husk;

public class FollowerSaver {

    private final TARDIS plugin;

    public FollowerSaver(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void persist() {
        for (World world : plugin.getServer().getWorlds()) {
            for (Entity e : world.getEntitiesByClass(Husk.class)) {
                if (((CraftEntity) e).getHandle() instanceof TWAFollower follower) {
                    // save entity in followers table
                    new FollowerPersister(plugin).save(follower);
                }
            }
        }
    }
}
