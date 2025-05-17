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
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.builders.exterior.TARDISBuilderUtility;
import me.eccentric_nz.TARDIS.builders.interior.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.flight.FlightEnd;
import me.eccentric_nz.TARDIS.flight.FlightReturnData;
import me.eccentric_nz.TARDIS.flight.vehicle.TARDISArmourStand;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

/**
 * Teleportation is a form of matter transmission and can be either a process of physical/psychological will or a
 * technological one.
 *
 * @author eccentric_nz
 */
public class TARDISTeleportListener implements Listener {

    private final TARDIS plugin;
    private final List<TeleportCause> causes = new ArrayList<>();

    public TARDISTeleportListener(TARDIS plugin) {
        this.plugin = plugin;
        causes.add(TeleportCause.PLUGIN);
        causes.add(TeleportCause.COMMAND);
        causes.add(TeleportCause.UNKNOWN);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        TeleportCause cause = event.getCause();
        String world_to = event.getTo().getWorld().getName();
        String world_from = event.getFrom().getWorld().getName();
        if (plugin.getTrackerKeeper().getFlyingReturnLocation().containsKey(player.getUniqueId())) {
            if (plugin.getTrackerKeeper().getStillFlyingNotReturning().contains(player.getUniqueId())) {
                if (world_to.contains("TARDIS") || world_from.contains("TARDIS") || cause == TeleportCause.UNKNOWN) {
                    return;
                } else {
                    // teleport them back to the interior
                    stopFlying(player);
                }
            } else {
                player.resetPlayerTime();
            }
            return;
        }
        if (causes.contains(cause)) {
            String uuid = player.getUniqueId().toString();
            if (world_from.contains("TARDIS") && !world_to.contains("TARDIS")) {
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid);
                plugin.getQueryFactory().doDelete("travellers", where);
                if (!cause.equals(TeleportCause.PLUGIN)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "OCCUPY_AUTO");
                }
                // stop tracking telepaths
                plugin.getTrackerKeeper().getTelepaths().remove(player.getUniqueId());
                // reset player time
                player.resetPlayerTime();
            } else if (world_to.contains("TARDIS") && !cause.equals(TeleportCause.PLUGIN)) {
                ResultSetTardisID rsid = new ResultSetTardisID(plugin);
                // if TIPS determine tardis_id from player location
                if (plugin.getConfig().getBoolean("creation.default_world")) {
                    if (plugin.getConfig().getBoolean("creation.create_worlds_with_perms") && player.hasPermission("tardis.create_world")) {
                        if (!rsid.fromUUID(uuid)) {
                            return;
                        }
                    } else {
                        int slot = TARDISInteriorPostioning.getTIPSSlot(player.getLocation());
                        if (!rsid.fromTIPSSlot(slot)) {
                            return;
                        }
                    }
                } else if (!rsid.fromUUID(uuid)) {
                    return;
                }
                // remove potential existing records from travellers first
                HashMap<String, Object> wherer = new HashMap<>();
                wherer.put("uuid", uuid);
                plugin.getQueryFactory().doDelete("travellers", wherer);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    HashMap<String, Object> wherei = new HashMap<>();
                    wherei.put("tardis_id", rsid.getTardisId());
                    wherei.put("uuid", uuid);
                    plugin.getQueryFactory().doInsert("travellers", wherei);
                }, 2L);
            }
        }
    }

    private void stopFlying(Player player) {
        if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
        UUID uuid = player.getUniqueId();
        plugin.getTrackerKeeper().getHiddenFlight().remove(uuid);
        String direction = player.getFacing().getOppositeFace().toString();
        FlightReturnData data = plugin.getTrackerKeeper().getFlyingReturnLocation().get(uuid);
        if (data != null) {
            Location interior = data.location();
            // stop animation and sound runnables
            plugin.getServer().getScheduler().cancelTask(data.animation());
            plugin.getServer().getScheduler().cancelTask(data.sound());
            // reset police box model
            ArmorStand stand = (ArmorStand) Bukkit.getEntity(data.stand());
            if (stand != null) {
                TARDISArmourStand tas = (TARDISArmourStand) ((CraftArmorStand) stand).getHandle();
                tas.setPlayer(null);
                tas.setStationary(true);
                Location location = stand.getLocation();
                ItemDisplay display = (ItemDisplay) Bukkit.getEntity(data.display());
                ItemStack is = display.getItemStack();
                EntityEquipment ee = stand.getEquipment();
                ee.setHelmet(is);
                // update the TARDIS's current location
                HashMap<String, Object> set = new HashMap<>();
                set.put("world", location.getWorld().getName());
                set.put("x", location.getBlockX());
                set.put("y", location.getBlockY());
                set.put("z", location.getBlockZ());
                set.put("direction", direction);
                set.put("submarine", (player.isInWater()) ? 1 : 0);
                HashMap<String, Object> where = new HashMap<>();
                where.put("tardis_id", data.id());
                plugin.getQueryFactory().doUpdate("current", set, where);
                // update door location
                TARDISBuilderUtility.saveDoorLocation(location, data.id(), direction);
                Block under = location.getBlock().getRelative(BlockFace.DOWN);
                if (under.getType().isAir()) {
                    // if location is in the air, set under door block
                    TARDISBlockSetters.setUnderDoorBlock(location.getWorld(), under.getX(), under.getY(), under.getZ(), data.id(), false);
                }
                // set the light
                Levelled light = TARDISConstants.LIGHT;
                light.setLevel(7);
                location.getBlock().getRelative(BlockFace.UP, 2).setBlockData(light);
                // add an interaction entity
                TARDISDisplayItemUtils.setInteraction(stand, data.id());
                Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> {
                    COMPASS compass = COMPASS.valueOf(direction);
                    stand.teleport(new Location(location.getWorld(), location.getBlockX() + 0.5d, location.getBlockY(), location.getBlockZ() + 0.5d, compass.getYaw(), 0.0f));
                });
            }
            plugin.getTrackerKeeper().getFlyingReturnLocation().remove(player.getUniqueId());
            plugin.getTrackerKeeper().getStillFlyingNotReturning().remove(player.getUniqueId());
            // teleport player to interior
            player.teleport(interior);
            player.setInvulnerable(false);
            player.setFlying(false);
            player.setAllowFlight(false);
            // add player to travellers
            HashMap<String, Object> sett = new HashMap<>();
            sett.put("tardis_id", data.id());
            sett.put("uuid", uuid.toString());
            plugin.getQueryFactory().doSyncInsert("travellers", sett);
            // remove trackers
            plugin.getTrackerKeeper().getMaterialising().removeAll(Collections.singleton(data.id()));
            plugin.getTrackerKeeper().getMalfunction().remove(data.id());
            plugin.getTrackerKeeper().getInVortex().removeAll(Collections.singleton(data.id()));
            if (plugin.getTrackerKeeper().getDidDematToVortex().contains(data.id())) {
                plugin.getTrackerKeeper().getDidDematToVortex().removeAll(Collections.singleton(data.id()));
            }
            if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(data.id())) {
                int taskID = plugin.getTrackerKeeper().getDestinationVortex().get(data.id());
                plugin.getServer().getScheduler().cancelTask(taskID);
                plugin.getTrackerKeeper().getDestinationVortex().remove(data.id());
            }
            new FlightEnd(plugin).process(data.id(), player, false, true);
        }
    }
}
