/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.tardisweepingangels.monsters.ood;

import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelSpawnEvent;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTransformEvent;

public class VillagerCuredListener implements Listener {

    private final TARDISWeepingAngels plugin;

    public VillagerCuredListener(TARDISWeepingAngels plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onVillagerCured(EntityTransformEvent event) {
        if (!event.getTransformReason().equals(EntityTransformEvent.TransformReason.CURED)) {
            return;
        }
        Entity entity = event.getTransformedEntity();
        World world = entity.getWorld();
        if (!plugin.getConfig().getBoolean("ood.worlds." + world.getName())) {
            return;
        }
        if (TARDISWeepingAngels.random.nextInt(100) < plugin.getConfig().getInt("ood.spawn_from_cured")) {
            Entity ood = world.spawnEntity(entity.getLocation(), EntityType.ARMOR_STAND);
            OodEquipment.set(null, ood, false);
            plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(ood, EntityType.ARMOR_STAND, Monster.OOD, entity.getLocation()));
        }
    }
}
