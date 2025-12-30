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
package me.eccentric_nz.TARDIS.skins.tv;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.skins.Skin;
import me.eccentric_nz.TARDIS.skins.SkinUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class TVListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TVListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a TARDIS GUI, then the click is processed
     * accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onTelevisionMenuClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TVInventory)) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 36) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        ItemStack is = event.getView().getItem(slot);
        if (is == null) {
            return;
        }
        switch (slot) {
            case 0 -> {
                // doctors
                TVDoctorsInventory doctors = new TVDoctorsInventory(plugin);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.openInventory(doctors.getInventory()), 2L);
            }
            case 2 -> {
                // companions
                TVCompanionsInventory companions = new TVCompanionsInventory(plugin);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.openInventory(companions.getInventory()), 2L);
            }
            case 4 -> {
                // characters
                TVCharactersInventory characters = new TVCharactersInventory(plugin);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.openInventory(characters.getInventory()), 2L);
            }
            case 6 -> {
                // monsters
                TVMonstersInventory monsters = new TVMonstersInventory(plugin);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.openInventory(monsters.getInventory()), 2L);
            }
            case 8 -> {
                // cyber variants
                TVCyberInventory variants = new TVCyberInventory(plugin);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.openInventory(variants.getInventory()), 2L);
            }
            case 31 -> {
                UUID uuid = player.getUniqueId();
                // remove skin
                plugin.getSkinChanger().remove(player);
                Skin skin = SkinUtils.SKINNED.get(uuid);
                SkinUtils.removeExtras(player, skin);
                SkinUtils.SKINNED.remove(uuid);
            }
            default -> close(player); // close
        }
    }
}
