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
package me.eccentric_nz.tardisweepingangels.utils;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisweepingangels.equip.MonsterEquipment;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffectType;

public class Cleaner implements Runnable {

    private final TARDIS plugin;

    public Cleaner(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (World world : plugin.getServer().getWorlds()) {
            // only in loaded chunks
            for (Chunk chunk : world.getLoadedChunks()) {
                for (Entity e : chunk.getEntities()) {
                    // only monsters
                    if (e instanceof Monster monster) {
                        // only invisible TWA monsters
                        if (monster.hasPotionEffect(PotionEffectType.INVISIBILITY) && MonsterEquipment.isMonster(monster)) {
                            // check helmet
                            EntityEquipment ee = monster.getEquipment();
                            if (ee != null && ee.getHelmet() == null) {
                                monster.remove();
                            }
                        }
                    }
                }
            }
        }
    }
}
