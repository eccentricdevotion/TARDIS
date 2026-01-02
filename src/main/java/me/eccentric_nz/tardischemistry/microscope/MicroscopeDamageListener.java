/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.tardischemistry.microscope;

import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class MicroscopeDamageListener implements Listener {

    private final TARDIS plugin;

    MicroscopeDamageListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamageEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof ItemFrame frame) {
            if (!frame.getPersistentDataContainer().has(plugin.getMicroscopeKey(), PersistentDataType.INTEGER)) {
                return;
            }
            event.setCancelled(true);
            // get the item in the frame
            ItemStack dye = frame.getItem();
            if (dye.getType().isAir()) {
                return;
            }
            if (!LabEquipment.getByMaterial().containsKey(dye.getType())) {
                return;
            }
            if (event.getDamager() instanceof Player player) {
                LabEquipment equipment = LabEquipment.getByMaterial().get(dye.getType());
                ItemStack drop = ItemStack.of(equipment.getMaterial(), 1);
                ItemMeta dropMeta = drop.getItemMeta();
                dropMeta.displayName(Component.text(equipment.getName()));
                dropMeta.setItemModel(equipment.getModel());
                drop.setItemMeta(dropMeta);
                player.getWorld().dropItem(entity.getLocation(), drop);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, frame::remove, 1L);
            }
        }
    }
}
