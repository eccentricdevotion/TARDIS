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
package me.eccentric_nz.TARDIS.skins.tv;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.skins.Skin;
import me.eccentric_nz.TARDIS.skins.SkinUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
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
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "TARDIS Television")) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 36) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        ItemStack is = view.getItem(slot);
        if (is == null) {
            return;
        }
        switch (slot) {
            case 0 -> {
                // doctors
                ItemStack[] doctors = new TVDoctorsInventory().getMenu();
                Inventory doctorinv = plugin.getServer().createInventory(player, 36, ChatColor.DARK_RED + "Doctor Skins");
                doctorinv.setContents(doctors);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.openInventory(doctorinv), 2L);
            }
            case 2 -> {
                // companions
                ItemStack[] companions = new TVCompanionsInventory().getMenu();
                Inventory companioninv = plugin.getServer().createInventory(player, 36, ChatColor.DARK_RED + "Companion Skins");
                companioninv.setContents(companions);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.openInventory(companioninv), 2L);
            }
            case 4 -> {
                // characters
                ItemStack[] characters = new TVCharactersInventory().getMenu();
                Inventory characterinv = plugin.getServer().createInventory(player, 36, ChatColor.DARK_RED + "Character Skins");
                characterinv.setContents(characters);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.openInventory(characterinv), 2L);
            }
            case 6 -> {
                // monsters
                ItemStack[] monsters = new TVMonstersInventory().getMenu();
                Inventory monsterinv = plugin.getServer().createInventory(player, 36, ChatColor.DARK_RED + "Monster Skins");
                monsterinv.setContents(monsters);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.openInventory(monsterinv), 2L);
            }
            case 8 -> {
                // cyber variants
                ItemStack[] variants = new TVCyberInventory().getMenu();
                Inventory cyberinv = plugin.getServer().createInventory(player, 36, ChatColor.DARK_RED + "Cyberman Skins");
                cyberinv.setContents(variants);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.openInventory(cyberinv), 2L);
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
