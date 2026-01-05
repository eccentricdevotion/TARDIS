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
package me.eccentric_nz.TARDIS.ARS.relocator;

import me.eccentric_nz.TARDIS.ARS.*;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.DamageUtility;
import me.eccentric_nz.TARDIS.commands.sudo.TARDISSudoTracker;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.rooms.RoomRequiredLister;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The architectural reconfiguration system is a component of the Doctor's TARDIS in the shape of a tree that, according
 * to the Eleventh Doctor, "reconstructs the particles according to your needs." It is basically "a machine that makes
 * machines," perhaps somewhat like a 3D printer. It is, according to Gregor Van Baalen's scanner, "more valuable than
 * the total sum of any currency.
 *
 * @author eccentric_nz
 */
public class RoomRelocatorListener extends ARSMethods implements Listener {

    public final HashMap<UUID, Integer> relocation_slot = new HashMap<>();

    public RoomRelocatorListener(TARDIS plugin) {
        super(plugin);
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a TARDIS GUI, then the click is processed
     * accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onRoomRelocatorClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof RoomRelocatorInventory)) {
            return;
        }
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        UUID playerUUID = player.getUniqueId();
        UUID uuid;
        uuid = TARDISSudoTracker.SUDOERS.getOrDefault(playerUUID, playerUUID);
        ids.put(playerUUID, getTardisId(uuid.toString()));
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 53) {
            return;
        }
        if (slot != 3 && slot != 10 && !hasLoadedMap.contains(playerUUID)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARS_LOAD");
            return;
        }
        InventoryView view = event.getView();
        switch (slot) {
            case 1, 9, 11, 19 -> moveMap(playerUUID, view, slot); // up, left, right, down
            // open room relocator GUI
            case 3 -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                    player.openInventory(new RoomRelocatorInventory(plugin, player).getInventory()), 2L);
            case 4, 5, 6, 7, 8, 13, 14, 15, 16, 17, 22, 23, 24, 25, 26, 31, 32, 33, 34, 35, 40, 41, 42, 43, 44 -> {
                if (!checkSlotForConsole(view, slot) && !selected_slot.containsKey(playerUUID)) {
                    // select room to move
                    selected_slot.put(playerUUID, slot);
                } else if (!relocation_slot.containsKey(playerUUID) && selected_slot.containsKey(playerUUID) && isEmptySlot(view, slot)) {
                    relocation_slot.put(playerUUID, slot);
                    // set slot to selected room with glint
                    setRelocationSlots(view, playerUUID);
                } else {
                    // need to reset
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "RELOCATOR_RESET");
                }
            }
            case 10 -> loadMap(view, playerUUID, true); // load map
            case 12 -> {
                // reconfigure
                if (!plugin.getBuildKeeper().getRoomProgress().containsKey(player.getUniqueId())) {
                    relocate(player);
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ARS_ACTIVE");
                }
            }
            case 27, 28, 29 -> {
                // switch level
                if (map_data.containsKey(playerUUID)) {
                    switchLevel(view, slot, playerUUID);
                    ARSMapData md = map_data.get(playerUUID);
                    setMap(md.getY(), md.getE(), md.getS(), playerUUID, view);
                    setLore(view, slot, null);
                } else {
                    setLore(view, slot, plugin.getLanguage().getString("ARS_LOAD", "You need to load the map first!"));
                }
            }
            case 30 -> {
                // reset
                selected_slot.remove(playerUUID);
                relocation_slot.remove(playerUUID);
                loadMap(view, playerUUID, false);
            }
            default -> {
            }
        }
    }

    private void relocate(Player player) {
        UUID playerUUID = player.getUniqueId();
        // start relocation
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            selected_slot.remove(playerUUID);
            relocation_slot.remove(playerUUID);
            hasLoadedMap.remove(playerUUID);
            if (map_data.containsKey(playerUUID)) {
                if (playerIsOwner(playerUUID, ids.get(playerUUID))) {
                    saveAll(playerUUID);
                    ARSProcessor tap = new ARSProcessor(plugin, ids.get(playerUUID));
                    boolean changed = tap.compare3DArray(save_map_data.get(playerUUID).getData(), map_data.get(playerUUID).getData());
                    if (changed && tap.checkCosts(tap.getChanged(), tap.getJettison())) {
                        if (plugin.getConfig().getBoolean("growth.rooms_require_blocks")) {
                            if (!TARDISSudoTracker.SUDOERS.containsKey(playerUUID) && !hasCondensables(playerUUID.toString(), tap.getChanged(), ids.get(playerUUID))) {
                                String message = (tap.getChanged().size() > 1) ? "ARS_CONDENSE_MULTIPLE" : "ARS_CONDENSE";
                                plugin.getMessenger().send(player, TardisModule.TARDIS, message);
                                if (tap.getChanged().size() == 1) {
                                    RoomRequiredLister.listCondensables(plugin, tap.getChanged().entrySet().iterator().next().getValue().toString(), player);
                                }
                                revert(playerUUID);
                                player.closeInventory();
                                return;
                            }
                        }
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "ARS_START");
                        // there should be only one ARS entry, so get that
                        Map.Entry<GrowSlot, ARS> ars = tap.getChanged().entrySet().iterator().next();
                        GrowSlot relocated = ars.getKey();
                        ARSRunnable ar = new ARSRunnable(plugin, relocated, ars.getValue(), player, ids.get(playerUUID));
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, ar, 20L);
                        // there should be only one jettison entry, so get that
                        Map.Entry<JettisonSlot, ARS> entry = tap.getJettison().entrySet().iterator().next();
                        JettisonSlot jettison = entry.getKey();
                        int sy = jettison.getY();
                        int sx = jettison.getX();
                        int sz = jettison.getZ();
                        World world = jettison.getChunk().getWorld();
                        for (int y = sy; y < sy + 16; y++) {
                            for (int x = sx; x < sx + 16; x++) {
                                for (int z = sz; z < sz + 16; z++) {
                                    // remove redstone from the to be jettisoned room
                                    Block block = world.getBlockAt(x, y, z);
                                    if (block.getType() == Material.REDSTONE_WIRE) {
                                        block.setType(Material.AIR);
                                    }
                                }
                            }
                        }
                        // move any entities in the room
                        HashMap<Entity, Location> mobs = new HashMap<>();
                        for (Entity entity : jettison.getChunk().getEntities()) {
                            if (entity instanceof Animals animal) {
                                mobs.put(animal, animal.getLocation());
                            }
                        }
                        // determine new location difference
                        int ey = relocated.getY();
                        int ex = relocated.getX();
                        int ez = relocated.getZ();
                        int diffy = (ey > sy) ? ey - sy : sy - ey;
                        int diffx = (ex > sx) ? ex - sx : sx - ex;
                        int diffz = (ez > sz) ? ez - sz : sz - ez;
                        long a_long_time = (16 * 16 * 16 * (Math.round(20 / plugin.getConfig().getDouble("growth.room_speed"))));
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            for (Map.Entry<Entity, Location> e : mobs.entrySet()) {
                                e.getKey().teleport(e.getValue().add(diffx, diffy, diffz));
                            }
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "ROOM_JETT", String.format("%d", tap.getJettison().size()));
                        }, a_long_time);
                        // do the jettison last
                        JettisonRunnable jr = new JettisonRunnable(plugin, jettison, entry.getValue(), ids.get(playerUUID), player);
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, jr, a_long_time + 50);
                        // damage the circuit if configured
                        DamageUtility.run(plugin, DiskCircuit.ARS, plugin.getTardisAPI().getIdOfTARDISPlayerIsIn(playerUUID), player);
                    } else {
                        // reset map to the previous version
                        revert(playerUUID);
                        plugin.getMessenger().send(player, TardisModule.TARDIS, tap.getError());
                    }
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ROOM_ONLY_TL");
                    revert(playerUUID);
                }
                map_data.remove(playerUUID);
                save_map_data.remove(playerUUID);
                ids.remove(playerUUID);
            }
            player.closeInventory();
        }, 1L);
    }

    private void setRelocationSlots(InventoryView view, UUID uuid) {
        int from_slot = selected_slot.get(uuid);
        int to_slot = relocation_slot.get(uuid);
        ItemStack is = view.getItem(from_slot).clone();
        ItemMeta im = is.getItemMeta();
        im.setEnchantmentGlintOverride(true);
        is.setItemMeta(im);
        setSlot(view, to_slot, is, uuid, true);
        view.setItem(to_slot, is);
        ItemStack tnt = ItemStack.of(Material.TNT, 1);
        ItemMeta j = tnt.getItemMeta();
        j.displayName(Component.text("Jettison"));
        tnt.setItemMeta(j);
        setSlot(view, from_slot, tnt, uuid, true);
    }

    private boolean isEmptySlot(InventoryView view, int slot) {
        ItemStack is = view.getItem(slot);
        return is != null && is.getType() == Material.STONE;
    }
}
