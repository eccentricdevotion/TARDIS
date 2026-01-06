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
package me.eccentric_nz.TARDIS.lazarus;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISGeneticManipulatorUndisguiseEvent;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.skins.Skin;
import me.eccentric_nz.TARDIS.skins.SkinUtils;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

public class LazarusSkinsListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final HashMap<UUID, Integer> skins = new HashMap<>();

    public LazarusSkinsListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onSkinInventoryClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder(false);
        if (!(holder instanceof LazarusDoctorInventory)
                && !(holder instanceof LazarusCompanionInventory)
                && !(holder instanceof LazarusCharacterInventory)
                && !(holder instanceof LazarusMonstersInventory)) {
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
        if (slot >= 0 && slot < ((LazarusGUI) holder).getMaxSlot()) {
            // get selection
            ItemStack is = event.getView().getItem(slot);
            if (is != null) {
                // remember selection
                skins.put(uuid, slot);
            }
        } else {
            switch (slot) {
                // previous / next
                case 36, 44 -> {
                    LazarusUtils.pagers.add(uuid);
                    ItemStack pageButton = event.getView().getItem(slot);
                    ItemMeta pageMeta = pageButton.getItemMeta();
                    // check the lore
                    String which = ComponentUtils.stripColour(pageMeta.lore().getFirst());
                    InventoryHolder ih = switch (which) {
                        case "Passive Mobs" -> new LazarusPassiveInventory(plugin);
                        case "Neutral Mobs" -> new LazarusNeutralInventory(plugin);
                        case "Hostile Mobs" -> new LazarusHostileInventory(plugin);
                        case "Hostile Adjacent Mobs" -> new LazarusAdjacentInventory(plugin);
                        case "Doctors" -> new LazarusDoctorInventory(plugin);
                        case "Companions" -> new LazarusCompanionInventory(plugin);
                        case "Characters" -> new LazarusCharacterInventory(plugin);
                        case "Monsters" -> new LazarusMonstersInventory(plugin);
                        default -> new LazarusInventory(plugin);
                    };
                    player.openInventory(ih.getInventory());
                }
                // back
                case 40 -> {
                    LazarusUtils.pagers.add(uuid);
                    player.openInventory(new LazarusInventory(plugin).getInventory());
                }
                case 51 -> {
                    close(player);
                    // if the Master Switch is ON turn it off and restore all players
                    if (!plugin.getTrackerKeeper().getImmortalityGate().isEmpty()) {
                        plugin.getServer().getOnlinePlayers().forEach((p) -> {
                            if (plugin.isDisguisesOnServer()) {
                                LazarusLibsDisguises.removeDisguise(p);
                            } else {
                                LazarusDisguise.removeDisguise(p);
                            }
                        });
                        plugin.getTrackerKeeper().setImmortalityGate("");
                        plugin.getPM().callEvent(new TARDISGeneticManipulatorUndisguiseEvent(player));
                    }
                    // remove disguise
                    // animate the manipulator walls
                    LazarusRunnable runnable = new LazarusRunnable(plugin, b);
                    int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 6L, 6L);
                    runnable.setTaskID(taskId);
                    TARDISSounds.playTARDISSound(player.getLocation(), "lazarus_machine");
                    // undisguise the player
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        // remove skin
                        LazarusUtils.geneticModificationOff(player);
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "GENETICS_RESTORED");
                    }, 80L);
                    // open the door
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> LazarusUtils.openDoor(b), 100L);
                }
                case 52 -> {
                    close(player);
                    // animate the manipulator walls
                    LazarusRunnable runnable = new LazarusRunnable(plugin, b);
                    int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 6L, 6L);
                    runnable.setTaskID(taskId);
                    TARDISSounds.playTARDISSound(player.getLocation(), "lazarus_machine");
                    // disguise the player
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        LazarusUtils.geneticModificationOff(player);
                        int rememberedSlot = skins.get(uuid);
                        // put on a skin
                        Skin skin = LazarusUtils.skinForSlot(rememberedSlot, getSkinType(holder));
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
                    untrack(player.getUniqueId(), false);
                }
                default -> { // do nothing
                }
            }
        }
    }

    private int getSkinType(InventoryHolder holder) {
        if (holder instanceof LazarusDoctorInventory) {
            return 0;
        } else if (holder instanceof LazarusCompanionInventory) {
            return 1;
        } else if (holder instanceof LazarusCharacterInventory) {
            return 2;
        } else if (holder instanceof LazarusMonstersInventory) {
            return 3;
        }
        return 0;
    }

    private void untrack(UUID uuid, boolean remove) {
        if (!LazarusUtils.pagers.contains(uuid)) {
            // stop tracking player
            plugin.getTrackerKeeper().getLazarus().remove(uuid);
        }
        if (remove) {
            plugin.getTrackerKeeper().getDisguises().remove(uuid);
        }
        plugin.getTrackerKeeper().getGeneticManipulation().remove(uuid);
    }
}
