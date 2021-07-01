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
package me.eccentric_nz.TARDIS.schematic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class TARDISSchematicListener implements Listener {

    private final TARDIS plugin;
    private final Material wand;

    public TARDISSchematicListener(TARDIS plugin) {
        this.plugin = plugin;
        wand = getWand();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null || !itemStack.getType().equals(wand) || !player.hasPermission("tardis.admin") || !isWand(itemStack)) {
            return;
        }
        Block b = event.getClickedBlock();
        if (b == null) {
            return;
        }
        Location l = b.getLocation();
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            plugin.getTrackerKeeper().getStartLocation().put(uuid, l);
            TARDISMessage.send(player, "SCHM_START");
        }
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            plugin.getTrackerKeeper().getEndLocation().put(uuid, l);
            TARDISMessage.send(player, "SCHM_END");
        }
        event.setCancelled(true);
    }

    private Material getWand() {
        Material mat;
        try {
            mat = Material.valueOf(plugin.getRecipesConfig().getString("shapeless.TARDIS Schematic Wand.result"));
        } catch (IllegalArgumentException e) {
            mat = Material.BONE;
        }
        return mat;
    }

    private boolean isWand(ItemStack is) {
        if (!is.hasItemMeta()) {
            return false;
        }
        ItemMeta im = is.getItemMeta();
        if (!im.hasDisplayName()) {
            return false;
        }
        return im.getDisplayName().equals("TARDIS Schematic Wand");
    }
}
