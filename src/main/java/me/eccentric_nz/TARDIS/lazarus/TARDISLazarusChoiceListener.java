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
import me.eccentric_nz.TARDIS.api.event.TARDISGeneticManipulatorDisguiseEvent;
import me.eccentric_nz.TARDIS.api.event.TARDISGeneticManipulatorUndisguiseEvent;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodels.GUIGeneticManipulator;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISLazarusChoiceListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final List<String> twaMonsters = new ArrayList<>();

    public TARDISLazarusChoiceListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
        for (Monster m : Monster.values()) {
            twaMonsters.add(m.toString());
        }
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a TARDIS GUI, then the click is processed
     * accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onLazarusClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder(false);
        if (!(holder instanceof TARDISLazarusInventory)) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        Block b = plugin.getTrackerKeeper().getLazarus().get(uuid);
        if (b == null) {
            return;
        }
        InventoryView view = event.getView();

        InventoryHolder ih;
        switch (slot) {
            // passive
            case 1 -> {
                LazarusUtils.pagers.add(uuid);
                ih = new TARDISLazarusPassiveInventory(plugin);
                player.openInventory(ih.getInventory());
            }
            // neutral
            case 3 -> {
                LazarusUtils.pagers.add(uuid);
                ih = new TARDISLazarusNeutralInventory(plugin);
                player.openInventory(ih.getInventory());
            }
            // hostile
            case 5 -> {
                LazarusUtils.pagers.add(uuid);
                ih = new TARDISLazarusHostileInventory(plugin);
                player.openInventory(ih.getInventory());
            }
            // hostile adjacent
            case 7 -> {
                LazarusUtils.pagers.add(uuid);
                ih = new TARDISLazarusAdjacentInventory(plugin);
                player.openInventory(ih.getInventory());
            }
            // doctors
            case 9 -> {
                LazarusUtils.pagers.add(uuid);
                ih = new TARDISLazarusDoctorInventory(plugin);
                player.openInventory(ih.getInventory());
            }
            // companions
            case 11 -> {
                LazarusUtils.pagers.add(uuid);
                ih = new TARDISLazarusCompanionInventory(plugin);
                player.openInventory(ih.getInventory());
            }
            // characters
            case 13 -> {
                LazarusUtils.pagers.add(uuid);
                ih = new TARDISLazarusCharacterInventory(plugin);
                player.openInventory(ih.getInventory());
            }
            // twa monsters
            case 15 -> {
                LazarusUtils.pagers.add(uuid);
                ih = new TARDISLazarusMonstersInventory(plugin);
                player.openInventory(ih.getInventory());
            }
            // The Master Switch : ON | OFF | process
            case 17 -> {
                ItemStack masterButton = view.getItem(slot);
                ItemMeta masterMeta = masterButton.getItemMeta();
                if (TARDISPermission.hasPermission(player, "tardis.themaster")) {
                    if (plugin.getTrackerKeeper().getImmortalityGate().isEmpty()) {
                        boolean isOff = ComponentUtils.stripColour(masterMeta.lore().getFirst()).equals(plugin.getLanguage().getString("SET_OFF", "OFF"));
                        Component onoff = isOff ? Component.text(plugin.getLanguage().getString("SET_ON", "ON")) : Component.text(plugin.getLanguage().getString("SET_OFF", "OFF"));
                        masterMeta.lore(List.of(onoff));
                        CustomModelDataComponent component = masterMeta.getCustomModelDataComponent();
                        component.setFloats(isOff ? List.of(252f) : List.of(152f));
                        masterMeta.setCustomModelDataComponent(component);
                        // process
                        if (!isReversedPolarity(view)) {
                            plugin.getTrackerKeeper().setImmortalityGate(player.getName());
                            if (plugin.isDisguisesOnServer()) {
                                TARDISLazarusLibs.runImmortalityGate(player);
                            } else {
                                TARDISLazarusDisguise.runImmortalityGate(player);
                            }
                            plugin.getMessenger().broadcast(TardisModule.TARDIS, "The Master (aka " + player.getName() + ") has cloned his genetic template to all players. Behold the Master Race!");
                            plugin.getPM().callEvent(new TARDISGeneticManipulatorDisguiseEvent(player, player.getName()));
                            // schedule a delayed task to remove the disguise
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                plugin.getServer().getOnlinePlayers().forEach((p) -> {
                                    if (plugin.isDisguisesOnServer()) {
                                        TARDISLazarusLibs.removeDisguise(p);
                                    } else {
                                        TARDISLazarusDisguise.removeDisguise(p);
                                    }
                                });
                                plugin.getMessenger().broadcast(TardisModule.TARDIS, "Lord Rassilon has reset the Master Race back to human form.");
                                plugin.getTrackerKeeper().setImmortalityGate("");
                                plugin.getPM().callEvent(new TARDISGeneticManipulatorUndisguiseEvent(player));
                            }, 3600L);
                        }
                    } else {
                        masterMeta.lore(List.of(
                                Component.text("The Master Race is already"),
                                Component.text(" set to " + plugin.getTrackerKeeper().getImmortalityGate() + "!"),
                                Component.text("Use the Restore button"),
                                Component.text("and try again later.")
                        ));
                    }
                } else {
                    masterMeta.lore(List.of(
                            Component.text("You do not have permission"),
                            Component.text("to be The Master!")
                    ));
                }
                masterButton.setItemMeta(masterMeta);
            }
            case 51 -> { // remove disguise
                LazarusUtils.pagers.remove(uuid);
                plugin.getTrackerKeeper().getGeneticManipulation().add(uuid);
                close(player);
                // if the Master Switch is ON turn it off and restore all players
                if (!plugin.getTrackerKeeper().getImmortalityGate().isEmpty()) {
                    plugin.getServer().getOnlinePlayers().forEach((p) -> {
                        if (plugin.isDisguisesOnServer()) {
                            TARDISLazarusLibs.removeDisguise(p);
                        } else {
                            TARDISLazarusDisguise.removeDisguise(p);
                        }
                    });
                    plugin.getMessenger().broadcast(TardisModule.TARDIS, "Lord Rassilon has reset the Master Race back to human form.");
                    plugin.getTrackerKeeper().setImmortalityGate("");
                    plugin.getPM().callEvent(new TARDISGeneticManipulatorUndisguiseEvent(player));
                }
                // animate the manipulator walls
                TARDISLazarusRunnable runnable = new TARDISLazarusRunnable(plugin, b);
                int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 6L, 6L);
                runnable.setTaskID(taskId);
                TARDISSounds.playTARDISSound(player.getLocation(), "lazarus_machine");
                // undisguise the player
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    LazarusUtils.geneticModificationOff(player);
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "GENETICS_RESTORED");
                    plugin.getPM().callEvent(new TARDISGeneticManipulatorUndisguiseEvent(player));
                }, 80L);
                // open the door
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    LazarusUtils.openDoor(b);
                    untrack(uuid, true);
                    plugin.getTrackerKeeper().getGeneticallyModified().remove(uuid);
                }, 100L);
            }
            case 53 -> {
                LazarusUtils.pagers.remove(uuid);
                close(player);
                LazarusUtils.openDoor(b);
                untrack(uuid, false);
            }
            default -> { // do nothing
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onLazarusClose(InventoryCloseEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (plugin.getTrackerKeeper().getGeneticManipulation().contains(uuid)) {
            return;
        }
        InventoryHolder holder = event.getInventory().getHolder(false);
        if ((holder instanceof TARDISLazarusInventory
                || holder instanceof TARDISLazarusPassiveInventory
                || holder instanceof TARDISLazarusNeutralInventory
                || holder instanceof TARDISLazarusHostileInventory
                || holder instanceof TARDISLazarusAdjacentInventory
                || holder instanceof TARDISLazarusMonstersInventory
                || holder instanceof TARDISLazarusDoctorInventory
                || holder instanceof TARDISLazarusCompanionInventory
                || holder instanceof TARDISLazarusCharacterInventory)
        ) {
            Block b = plugin.getTrackerKeeper().getLazarus().get(uuid);
            if (b != null && b.getRelative(BlockFace.SOUTH).getType().equals(Material.COBBLESTONE_WALL)) {
                b.getRelative(BlockFace.SOUTH).setType(Material.AIR);
                b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP).setType(Material.AIR);
            }
            untrack(uuid, false);
            LazarusUtils.pagers.remove(uuid);
        }
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

    private boolean isReversedPolarity(InventoryView i) {
        ItemStack is = i.getItem(GUIGeneticManipulator.BUTTON_MASTER.slot());
        ItemMeta im = is.getItemMeta();
        return ComponentUtils.stripColour(im.lore().getFirst()).equals(plugin.getLanguage().getString("SET_ON", "ON"));
    }
}
