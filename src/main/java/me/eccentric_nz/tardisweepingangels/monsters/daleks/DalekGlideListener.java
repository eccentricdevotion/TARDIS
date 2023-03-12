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
package me.eccentric_nz.tardisweepingangels.monsters.daleks;

import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class DalekGlideListener implements Listener {

    private final TARDISWeepingAngels plugin;

    public DalekGlideListener(TARDISWeepingAngels plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGlideChange(EntityToggleGlideEvent event) {
        if (!event.isGliding()) {
            Entity entity = event.getEntity();
            if (entity instanceof Skeleton skeleton) {
                PersistentDataContainer pdc = skeleton.getPersistentDataContainer();
                if (pdc.has(TARDISWeepingAngels.DALEK, PersistentDataType.INTEGER)) {
                    EntityEquipment ee = skeleton.getEquipment();
                    if (ee.getChestplate().getType().equals(Material.ELYTRA)) {
                        ee.setChestplate(null);
                    }
                }
            }
        }
    }
}
