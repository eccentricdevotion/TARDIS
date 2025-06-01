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
package me.eccentric_nz.TARDIS.lazarus;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.skins.Skin;
import me.eccentric_nz.TARDIS.skins.SkinUtils;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class TARDISLazarusSkinsListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final HashMap<UUID, Integer> skins = new HashMap<>();

    public TARDISLazarusSkinsListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onSkinInventoryClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "Genetic Skins")) {
            return;
        }
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        Block b = plugin.getTrackerKeeper().getLazarus().get(uuid);
        if (b == null) {
            return;
        }
        int slot = event.getRawSlot();
        int max_slot = 47;
        if (slot >= 0 && slot <= max_slot) {
            // get selection
            ItemStack is = view.getItem(slot);
            if (is != null) {
                // remember selection
                skins.put(uuid, slot);
            }
        } else {
            switch (slot) {
                case 48 -> {
                    LazarusUtils.pagers.add(uuid);
                    // go to page one
                    Inventory inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Genetic Manipulator");
                    inv.setContents(new TARDISLazarusInventory(plugin).getPageOne());
                    player.openInventory(inv);
                }
                case 49 -> {
                    LazarusUtils.pagers.add(uuid);
                    // go to page two
                    Inventory inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Genetic Manipulator");
                    inv.setContents(new TARDISLazarusPageTwoInventory(plugin).getPageTwo());
                    player.openInventory(inv);
                }
                case 50 -> {
                    LazarusUtils.pagers.add(uuid);
                    // go to monsters
                    Inventory inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Genetic Manipulator");
                    inv.setContents(new TARDISWeepingAngelsMonstersInventory(plugin).getMonsters());
                    player.openInventory(inv);
                }
                case 51 -> {
                    // remove disguise
                    close(player);
                    // animate the manipulator walls
                    TARDISLazarusRunnable runnable = new TARDISLazarusRunnable(plugin, b);
                    int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 6L, 6L);
                    runnable.setTaskID(taskId);
                    TARDISSounds.playTARDISSound(player.getLocation(), "lazarus_machine");
                    // undisguise the player
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        // remove skin
                        Skin skin = SkinUtils.SKINNED.get(uuid);
                        if (skin != null) {
                            plugin.getSkinChanger().remove(player);
                            SkinUtils.removeExtras(player, skin);
                            SkinUtils.SKINNED.remove(uuid);
                        }
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "GENETICS_RESTORED");
                    }, 80L);
                    // open the door
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> LazarusUtils.openDoor(b), 100L);
                }
                case 52 -> {
                    close(player);
                    // animate the manipulator walls
                    TARDISLazarusRunnable runnable = new TARDISLazarusRunnable(plugin, b);
                    int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 6L, 6L);
                    runnable.setTaskID(taskId);
                    TARDISSounds.playTARDISSound(player.getLocation(), "lazarus_machine");
                    // disguise the player
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        Skin previous = SkinUtils.SKINNED.get(uuid);
                        if (previous != null) {
                            plugin.getSkinChanger().remove(player);
                            SkinUtils.removeExtras(player, previous);
                        }
                        int rememberedSlot = skins.get(uuid);
                        // put on a skin
                        Skin skin = LazarusUtils.skinForSlot(rememberedSlot);
                        if (skin != null) {
                            plugin.getSkinChanger().set(player, skin);
                            SkinUtils.setExtras(player, skin);
                            SkinUtils.SKINNED.put(uuid, skin);
                        }
                    }, 80L);
                    // open the door
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> LazarusUtils.openDoor(b), 100L);
                }
                case 53 -> {
                    close(player);
                    LazarusUtils.openDoor(b);
                }
                default -> { // do nothing
                }
            }
        }
    }
}
