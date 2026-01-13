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

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.ARS.*;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.DamageUtility;
import me.eccentric_nz.TARDIS.commands.sudo.TARDISSudoTracker;
import me.eccentric_nz.TARDIS.desktop.BlockScannerData;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.rooms.RoomRequiredLister;
import me.eccentric_nz.TARDIS.schematic.SchematicGZip;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Breedable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Locale;
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
    public final HashMap<UUID, ItemStack> level_switch = new HashMap<>();

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
                // not gravity wells
                if (isGravity(view, slot)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "RELOCATOR_GRAVITY");
                    return;
                }
                if (!checkSlotForConsole(view, slot) && !level_switch.containsKey(playerUUID)) {
                    // select room to move
                    setFromSlot(view, uuid, slot);
                } else if (!relocation_slot.containsKey(playerUUID) && level_switch.containsKey(playerUUID) && isEmptySlot(view, slot)) {
                    relocation_slot.put(playerUUID, slot);
                    // set slot to selected room with glint
                    setRelocationSlot(view, playerUUID);
                } else {
                    // need to reset
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "RELOCATOR_RESET");
                }
            }
            case 10 -> loadMap(view, playerUUID, true); // load map
            case 12 -> {
                if (!level_switch.containsKey(playerUUID) || !relocation_slot.containsKey(playerUUID)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "RELOCATOR_SELECT");
                    return;
                }
                // reconfigure
                if (!plugin.getBuildKeeper().getRoomProgress().containsKey(player.getUniqueId())) {
                    relocate(player, view, relocation_slot.get(playerUUID));
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
                level_switch.remove(playerUUID);
                relocation_slot.remove(playerUUID);
                level_switch.remove(uuid);
                loadMap(view, playerUUID, false);
            }
            default -> {
            }
        }
    }

    private boolean isGravity(InventoryView view, int slot) {
        ItemStack is = view.getItem(slot);
        return is != null && (is.getType() == Material.SANDSTONE || is.getType() == Material.MOSSY_COBBLESTONE);
    }

    private void relocate(Player player, InventoryView view, int slot) {
        UUID playerUUID = player.getUniqueId();
        // start relocation
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            level_switch.remove(playerUUID);
            relocation_slot.remove(playerUUID);
            hasLoadedMap.remove(playerUUID);
            if (map_data.containsKey(playerUUID)) {
                if (playerIsOwner(playerUUID, ids.get(playerUUID))) {
                    saveAll(playerUUID);
                    ARSProcessor tap = new ARSProcessor(plugin, ids.get(playerUUID));
                    boolean changed = tap.compare3DArray(save_map_data.get(playerUUID).getData(), map_data.get(playerUUID).getData());
                    if (changed && tap.checkCosts(tap.getChanged(), tap.getJettison())) {
                        // there should be only one jettison entry, so get that
                        Map.Entry<JettisonSlot, ARS> entry = tap.getJettison().entrySet().iterator().next();
                        JettisonSlot jettison = entry.getKey();
                        String room = getRoomName(view, slot);
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
                        } else {
                            // check they haven't mined room
                            if (!room.isEmpty()) {
                                RoomBlockScanner brs = new RoomBlockScanner(plugin, room, jettison, playerUUID);
                                BlockScannerData check = brs.check();
                                if (!check.allow()) {
                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "RELOCATOR_PERCENT_BLOCKS", plugin.getConfig().getInt("desktop.block_change_percent") + "");
                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "UPGRADE_PERCENT_EXPLAIN", check.count() + "", check.volume() + "", check.changed() + "");
                                    return;
                                }
                            }
                        }
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "ARS_START");
                        // there should be only one ARS entry, so get that
                        Map.Entry<GrowSlot, ARS> ars = tap.getChanged().entrySet().iterator().next();
                        GrowSlot relocated = ars.getKey();
                        ARSRunnable ar = new ARSRunnable(plugin, relocated, ars.getValue(), player, ids.get(playerUUID));
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, ar, 20L);
                        int sy = jettison.getY();
                        int sx = jettison.getX();
                        int sz = jettison.getZ();
                        World world = jettison.getChunk().getWorld();
                        // put REINFORCED_DEEPSLATE blocks in the doorways
                        // as they can't be moved by pistons and are hard to break
                        int sy_offset = sy + Math.abs(plugin.getRoomsConfig().getInt("rooms." + room + ".offset")) + 1;
                        Block westDoor = world.getBlockAt(sx, sy_offset, sz + 8);
                        Block northDoor = world.getBlockAt(sx + 8, sy_offset, sz);
                        Block eastDoor = world.getBlockAt(sx + 15, sy_offset, sz + 8);
                        Block southDoor = world.getBlockAt(sx + 8, sy_offset, sz + 15);
                        westDoor.setType(Material.REINFORCED_DEEPSLATE);
                        northDoor.setType(Material.REINFORCED_DEEPSLATE);
                        eastDoor.setType(Material.REINFORCED_DEEPSLATE);
                        southDoor.setType(Material.REINFORCED_DEEPSLATE);
                        westDoor.getRelative(BlockFace.UP).setType(Material.REINFORCED_DEEPSLATE);
                        northDoor.getRelative(BlockFace.UP).setType(Material.REINFORCED_DEEPSLATE);
                        eastDoor.getRelative(BlockFace.UP).setType(Material.REINFORCED_DEEPSLATE);
                        southDoor.getRelative(BlockFace.UP).setType(Material.REINFORCED_DEEPSLATE);
                        // move any entities in the room
                        HashMap<Entity, Location> mobs = new HashMap<>();
                        Chunk roomChunk = world.getBlockAt(sx, sy, sz).getChunk();
                        for (Entity entity : roomChunk.getEntities()) {
                            if (entity instanceof Breedable animal) {
                                mobs.put(animal, animal.getLocation());
                            }
                        }
                        // determine new location difference
                        int ey = relocated.getY();
                        int ex = relocated.getX();
                        int ez = relocated.getZ();
                        int dy = ey - sy;
                        int dx = ex - sx;
                        int dz = ez - sz;
                        // get the height of the schematic
                        int height = getSchematicHeight(room);
                        long a_long_time = (16 * 16 * height * (Math.round(20 / plugin.getConfig().getDouble("growth.room_speed"))));
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            for (Map.Entry<Entity, Location> e : mobs.entrySet()) {
                                Location tp = e.getValue().add(dx, dy, dz);
                                e.getKey().teleport(tp);
                            }
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "ROOM_JETT", String.format("%d", tap.getJettison().size()));
                        }, a_long_time);
                        // do the jettison last
                        JettisonRunnable jr = new JettisonRunnable(plugin, jettison, entry.getValue(), ids.get(playerUUID), player, true);
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

    private int getSchematicHeight(String room) {
        JsonObject obj = SchematicGZip.getObject(plugin, "rooms", room.toLowerCase(Locale.ROOT), false);
        if (obj == null) {
            return 16;
        }
        // get dimensions
        JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
        return dimensions.get("height").getAsInt();
    }

    private String getRoomName(InventoryView view, int slot) {
        ItemStack is = view.getItem(slot);
        if (is != null) {
            ItemMeta im = is.getItemMeta();
            String name = ComponentUtils.stripColour(im.displayName());
            for (TARDISARS ars : TARDISARS.values()) {
                if (name.equals(ars.getDescriptiveName())) {
                    return ars.toString();
                }
            }
        }
        return "";
    }

    private void setRelocationSlot(InventoryView view, UUID uuid) {
        int to_slot = relocation_slot.get(uuid);
        ItemStack is = level_switch.get(uuid).clone();
        ItemMeta im = is.getItemMeta();
        im.setEnchantmentGlintOverride(true);
        is.setItemMeta(im);
        setSlot(view, to_slot, is, uuid, true);
    }

    private void setFromSlot(InventoryView view, UUID uuid, int slot) {
        level_switch.put(uuid, view.getItem(slot));
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            ItemStack tnt = ItemStack.of(Material.TNT, 1);
            ItemMeta j = tnt.getItemMeta();
            j.displayName(Component.text("Jettison"));
            tnt.setItemMeta(j);
            setSlot(view, slot, tnt, uuid, true);
        }, 2L);
    }

    private boolean isEmptySlot(InventoryView view, int slot) {
        ItemStack is = view.getItem(slot);
        return is != null && is.getType() == Material.STONE;
    }
}
