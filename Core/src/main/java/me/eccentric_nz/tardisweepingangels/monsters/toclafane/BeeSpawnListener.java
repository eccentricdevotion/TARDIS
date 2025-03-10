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
package me.eccentric_nz.tardisweepingangels.monsters.toclafane;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelSpawnEvent;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Collection;

public class BeeSpawnListener implements Listener {

    private final TARDIS plugin;

    public BeeSpawnListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBeeSpawnEvent(CreatureSpawnEvent event) {
        Entity entity = event.getEntity();
        if (!entity.getType().equals(EntityType.BEE)) {
            return;
        }
        World world = entity.getWorld();
        int limit = plugin.getMonstersConfig().getInt("tocalane.worlds." + world.getName());
        if (limit < 1) {
            return;
        }
        // get the current toclafane count
        int n = 0;
        Collection<Bee> hive = world.getEntitiesByClass(Bee.class);
        for (Bee b : hive) {
            if (!b.getPassengers().isEmpty() && b.getPassengers().getFirst() instanceof ArmorStand) {
                n++;
            }
        }
        if (n < limit && TARDISConstants.RANDOM.nextInt(100) < plugin.getMonstersConfig().getInt("toclafane.spawn_from_bee")) {
            Entity toclafane = world.spawnEntity(entity.getLocation(), EntityType.ARMOR_STAND);
            ToclafaneEquipment.set(toclafane, false);
            plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(toclafane, EntityType.ARMOR_STAND, Monster.TOCLAFANE, entity.getLocation()));
            entity.remove();
        }
    }
}
