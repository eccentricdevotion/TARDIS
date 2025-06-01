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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISCache;
import me.eccentric_nz.TARDIS.artron.TARDISAdaptiveBoxLampToggler;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISHideCommand;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISRebuildCommand;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisPreset;
import me.eccentric_nz.TARDIS.doors.inner.*;
import me.eccentric_nz.TARDIS.doors.outer.OuterDisplayDoorCloser;
import me.eccentric_nz.TARDIS.doors.outer.OuterDoor;
import me.eccentric_nz.TARDIS.doors.outer.OuterMinecraftDoorCloser;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
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

import java.util.HashMap;
import java.util.UUID;

/**
 * At one point, the Tenth Doctor installed a system that allowed him to lock the TARDIS remotely using a fob. As a
 * joke, the TARDIS roof light flashed and an alarm chirp sound was heard, similar to that used on vehicles on Earth.
 *
 * @author eccentric_nz
 */
public class TARDISRemoteKeyListener implements Listener {

    private final TARDIS plugin;
    private final Material material;

    public TARDISRemoteKeyListener(TARDIS plugin) {
        this.plugin = plugin;
        Material material;
        try {
            material = Material.valueOf(plugin.getConfig().getString("preferences.key"));
        } catch (IllegalArgumentException e) {
            material = Material.GOLD_NUGGET;
        }
        this.material = material;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Action action = event.getAction();
        Player player = event.getPlayer();
        if ((!action.equals(Action.LEFT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_AIR)) || player.isSneaking()) {
            return;
        }
        // check item in hand
        ItemStack is = player.getInventory().getItemInMainHand();
        if (!is.getType().equals(Material.OMINOUS_TRIAL_KEY) && !is.getType().equals(material)) {
            return;
        }
        if (is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            if (im.hasDisplayName() && im.getDisplayName().endsWith("TARDIS Remote Key")) {
                // has TARDIS?
                Tardis tardis = TARDISCache.BY_UUID.get(player.getUniqueId());
                if (tardis == null) {
                    return;
                }
                int id = tardis.getTardisId();
                boolean powered = tardis.isPoweredOn();
                ChameleonPreset preset = tardis.getPreset();
                if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
                    return;
                }
                if (plugin.getTrackerKeeper().getDispersedTARDII().contains(id)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_DISPERSED");
                    return;
                }
                boolean hidden = tardis.isHidden();
                if (action.equals(Action.LEFT_CLICK_AIR)) {
                    // get the TARDIS current location
                    Current current = TARDISCache.CURRENT.get(id);
                    if (current == null) {
                        return;
                    }
                    HashMap<String, Object> whered = new HashMap<>();
                    whered.put("tardis_id", id);
                    ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
                    if (rsd.resultSet()) {
                        // toggle door lock
                        int locked = (rsd.isLocked()) ? 0 : 1;
                        HashMap<String, Object> setl = new HashMap<>();
                        setl.put("locked", locked);
                        HashMap<String, Object> wherel = new HashMap<>();
                        wherel.put("tardis_id", id);
                        // always lock / unlock both doors
                        plugin.getQueryFactory().doUpdate("doors", setl, wherel);
                        String message = (rsd.isLocked()) ? plugin.getLanguage().getString("DOOR_UNLOCK") : plugin.getLanguage().getString("DOOR_DEADLOCK");
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "DOOR_LOCK", message);
                        TARDISAdaptiveBoxLampToggler tpblt = new TARDISAdaptiveBoxLampToggler(plugin);
                        TARDISSounds.playTARDISSound(current.location(), "tardis_lock");
                        tpblt.toggleLamp(id, !powered, preset);
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> tpblt.toggleLamp(id, powered, preset), 6L);
                    }
                    if (plugin.getTrackerKeeper().getDispersedTARDII().contains(id)) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_DISPERSED");
                        return;
                    }
                    boolean hidden = tardis.isHidden();
                    if (action.equals(Action.LEFT_CLICK_AIR)) {
                        // get the TARDIS current location
                        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
                        if (!rsc.resultSet()) {
                            return;
                        }
                        Location l = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                        HashMap<String, Object> whered = new HashMap<>();
                        whered.put("tardis_id", id);
                        ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
                        if (rsd.resultSet()) {
                            // toggle door lock
                            int locked = (rsd.isLocked()) ? 0 : 1;
                            HashMap<String, Object> setl = new HashMap<>();
                            setl.put("locked", locked);
                            HashMap<String, Object> wherel = new HashMap<>();
                            wherel.put("tardis_id", id);
                            // always lock / unlock both doors
                            plugin.getQueryFactory().doUpdate("doors", setl, wherel);
                            String message = (rsd.isLocked()) ? plugin.getLanguage().getString("DOOR_UNLOCK") : plugin.getLanguage().getString("DOOR_DEADLOCK");
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "DOOR_LOCK", message);
                            TARDISAdaptiveBoxLampToggler tpblt = new TARDISAdaptiveBoxLampToggler(plugin);
                            TARDISSounds.playTARDISSound(l, "tardis_lock");
                            tpblt.toggleLamp(id, !powered, preset);
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> tpblt.toggleLamp(id, powered, preset), 6L);
                        }
                    } else if (preset.equals(ChameleonPreset.INVISIBLE)) {
                        HashMap<String, Object> whered = new HashMap<>();
                        whered.put("tardis_id", id);
                        whered.put("door_type", 1);
                        ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
                        if (rsd.resultSet()) {
                            // get inner door block
                            Block block = TARDISStaticLocationGetters.getLocationFromDB(rsd.getDoor_location()).getBlock();
                            boolean open = TARDISStaticUtils.isDoorOpen(block);
                            ResultSetTardisPreset rsp = new ResultSetTardisPreset(plugin);
                            if (rsp.fromID(id)) {
                                boolean outerDisplayDoor = rsp.getPreset().usesArmourStand();
                                UUID playerUUID = player.getUniqueId();
                                // toggle door / portals
                                if (open) {
                                    Inner innerDisplayDoor = new InnerDoor(plugin, id).get();
                                    // close inner
                                    if (innerDisplayDoor.display()) {
                                        new InnerDisplayDoorCloser(plugin).close(innerDisplayDoor.block(), id, playerUUID, true);
                                    } else {
                                        new InnerMinecraftDoorCloser(plugin).close(innerDisplayDoor.block(), id, playerUUID);
                                    }
                                    // close outer
                                    if (outerDisplayDoor) {
                                        new OuterDisplayDoorCloser(plugin).close(new OuterDoor(plugin, id).getDisplay(), id, playerUUID);
                                    } else if (rsp.getPreset().hasDoor()) {
                                        new OuterMinecraftDoorCloser(plugin).close(new OuterDoor(plugin, id).getMinecraft(), id, playerUUID);
                                    }
                                } else {
                                    // open inner
                                    Inner innerDisplayDoor = new InnerDoor(plugin, id).get();
                                    // open inner
                                    if (innerDisplayDoor.display()) {
                                        new InnerDisplayDoorOpener(plugin).open(innerDisplayDoor.block(), id, true);
                                    } else {
                                        new InnerMinecraftDoorOpener(plugin).open(innerDisplayDoor.block(), id);
                                    }
                                    // open outer
                                    if (outerDisplayDoor) {
                                        new OuterDisplayDoorCloser(plugin).close(new OuterDoor(plugin, id).getDisplay(), id, playerUUID);
                                    } else if (rsp.getPreset().hasDoor()) {
                                        new OuterMinecraftDoorCloser(plugin).close(new OuterDoor(plugin, id).getMinecraft(), id, playerUUID);
                                    }
                                }
                                String message = (open) ? "DOOR_CLOSED" : "DOOR_OPENED";
                                plugin.getMessenger().send(player, TardisModule.TARDIS, message);
                            }
                        }
                    } else {
                        if (plugin.getTrackerKeeper().getRebuildCooldown().containsKey(player.getUniqueId())) {
                            long now = System.currentTimeMillis();
                            long cooldown = plugin.getConfig().getLong("police_box.rebuild_cooldown");
                            long then = plugin.getTrackerKeeper().getRebuildCooldown().get(player.getUniqueId()) + cooldown;
                            if (now < then) {
                                plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "COOLDOWN", String.format("%d", cooldown / 1000));
                                return;
                            }
                        }
                        if (!powered) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_DOWN");
                            return;
                        }
                        if (hidden) {
                            // rebuild
                            new TARDISRebuildCommand(plugin).rebuildPreset(player);
                        } else {
                            // hide
                            new TARDISHideCommand(plugin).hide(player);
                        }
                    }
                }
            }
        }
    }
}
