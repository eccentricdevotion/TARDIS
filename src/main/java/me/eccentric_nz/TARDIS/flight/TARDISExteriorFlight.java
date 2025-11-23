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
package me.eccentric_nz.TARDIS.flight;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.builders.exterior.TARDISBuilderUtility;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetBlocks;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.flight.vehicle.InterpolatedAnimation;
import me.eccentric_nz.TARDIS.flight.vehicle.TARDISArmourStand;
import me.eccentric_nz.TARDIS.flight.vehicle.VehicleUtility;
import me.eccentric_nz.TARDIS.sensor.BeaconSensor;
import me.eccentric_nz.TARDIS.sensor.HandbrakeSensor;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.craftbukkit.entity.CraftArmorStand;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

/**
 * The relativity differentiator is a component inside the TARDIS that allows it to travel in space.
 *
 * @author eccentric_nz
 */
public class TARDISExteriorFlight {

    private final TARDIS plugin;

    public TARDISExteriorFlight(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void stopFlying(Player player, ArmorStand stand) {
        if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
        player.getAttribute(Attribute.CAMERA_DISTANCE).setBaseValue(4.0d);
        UUID uuid = player.getUniqueId();
        plugin.getTrackerKeeper().getHiddenFlight().remove(uuid);
        Location location = stand.getLocation();
        boolean drifting = false;
        String direction = player.getFacing().getOppositeFace().toString();
        FlightReturnData data = plugin.getTrackerKeeper().getFlyingReturnLocation().get(uuid);
        if (data != null) {
            Location interior = data.location();
            // check block protection
            if (!plugin.getPluginRespect().getRespect(location, new Parameters(player, Flag.getDefaultFlags())) || plugin.getTardisArea().isInExistingArea(location)) {
                // remove police box
                stand.remove();
                // set drifting in the time vortex
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new TARDISLoopingFlightSound(plugin, interior, data.id()), 20L);
                plugin.getTrackerKeeper().getInVortex().add(data.id());
                plugin.getTrackerKeeper().getDidDematToVortex().add(data.id());
                drifting = true;
                // message player
                plugin.getMessenger().send(player, TardisModule.TARDIS, "FLIGHT_PROTECTED");
            }
            // stop animation and sound runnables
            plugin.getServer().getScheduler().cancelTask(data.animation());
            plugin.getServer().getScheduler().cancelTask(data.sound());
            // get item display
            ItemDisplay display = (ItemDisplay) player.getPassengers().getFirst();
            ItemStack is = display.getItemStack();
            player.eject();
            display.remove();
            if (!drifting) {
                // reset police box model
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
            }
            // teleport player to interior
            player.teleport(interior);
            // add player to travellers
            HashMap<String, Object> sett = new HashMap<>();
            sett.put("tardis_id", data.id());
            sett.put("uuid", uuid.toString());
            plugin.getQueryFactory().doSyncInsert("travellers", sett);
            // remove trackers
            plugin.getTrackerKeeper().getMaterialising().removeAll(Collections.singleton(data.id()));
            plugin.getTrackerKeeper().getMalfunction().remove(data.id());
            if (!drifting) {
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
        Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> {
            COMPASS compass = COMPASS.valueOf(direction);
            stand.teleport(new Location(location.getWorld(), location.getBlockX() + 0.5d, location.getBlockY(), location.getBlockZ() + 0.5d, compass.getYaw(), 0.0f));
        });
    }

    public void startFlying(Player player, int id, Block block, Location current, boolean beac_on, String beacon) {
        // get the TARDIS's current location
        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
        if (!rsc.resultSet()) {
            plugin.debug("No current location");
            return;
        }
        Location interior = player.getLocation();
        // set the handbrake
        if (block != null) {
            TARDISHandbrake.setLevers(block, false, true, block.getLocation().toString(), id, plugin);
            TARDISSounds.playTARDISSound(block.getLocation(), "tardis_handbrake_release");
        }
        if (plugin.getConfig().getBoolean("circuits.damage")) {
            plugin.getTrackerKeeper().getHasNotClickedHandbrake().remove(id);
        }
        if (!beac_on && !beacon.isEmpty()) {
            new BeaconSensor().toggle(beacon, true);
        }
        new HandbrakeSensor(plugin, id).toggle();
        HashMap<String, Object> set = new HashMap<>();
        set.put("handbrake_on", 0);
        HashMap<String, Object> whereh = new HashMap<>();
        whereh.put("tardis_id", id);
        plugin.getQueryFactory().doUpdate("tardis", set, whereh);
        plugin.getMessenger().sendStatus(player, "HANDBRAKE_OFF");
        plugin.getTrackerKeeper().getInVortex().add(id);
        UUID uuid = player.getUniqueId();
        plugin.getTrackerKeeper().getStillFlyingNotReturning().add(uuid);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            // set no gravity to player so that they don't fall out of a floating Police Box
            player.setGravity(false);
            // teleport player to exterior
            player.teleport(current.clone().add(0.5, 0.25, 0.5));
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                // get the armour stand
                for (Entity e : current.getWorld().getNearbyEntities(current, 1d, 1d, 1d, (s) -> s.getType() == EntityType.ARMOR_STAND)) {
                    if (e instanceof ArmorStand stand) {
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new FlyingInit(stand).run(), 3L);
                            ItemStack box = stand.getEquipment().getHelmet();
                            ItemDisplay display = VehicleUtility.getItemDisplay(player, box, switch (box.getType()) {
                                case ENDER_PEARL -> 1.5f;
                                case GRAY_STAINED_GLASS_PANE -> 1.66f;
                                default -> 1.75f;
                            });
                            int animation = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new InterpolatedAnimation(display, 40), 20L, 40L);
                            int sound = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> TARDISSounds.playTARDISSound(player.getLocation(), "time_rotor_flying", 4f), 5L, 33L);
                            player.getPersistentDataContainer().set(plugin.getLoopKey(), PersistentDataType.INTEGER, sound);
                            stand.addPassenger(player);
                            TARDISArmourStand tas = (TARDISArmourStand) ((CraftArmorStand) stand).getHandle();
                            tas.setPlayer(player);
                            tas.setStationary(false);
                            player.setInvulnerable(true);
                            player.setAllowFlight(true);
                            player.setFlying(true);
                            player.getAttribute(Attribute.CAMERA_DISTANCE).setBaseValue(8.0d);
                            // remove the light
                            current.getBlock().getRelative(BlockFace.UP, 2).setBlockData(TARDISConstants.AIR);
                            // save player's current location, so we can teleport them back to it when they finish flying
                            plugin.getTrackerKeeper().getFlyingReturnLocation().put(uuid, new FlightReturnData(id, interior, sound, animation, stand.getUniqueId(), display.getUniqueId()));
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.setGravity(true), 1L);
                            // remove interaction entity
                            Interaction interaction = TARDISDisplayItemUtils.getInteraction(current);
                            if (interaction != null) {
                                interaction.remove();
                            }
                        }, 5L);
                        break;
                    }
                }
            }, 3L);
        }, 5L);
        // check protected blocks - if block has data stored then put the block back!
        HashMap<String, Object> tid = new HashMap<>();
        tid.put("tardis_id", id);
        tid.put("police_box", 1);
        ResultSetBlocks rsb = new ResultSetBlocks(plugin, tid, true);
        if (rsb.resultSet()) {
            rsb.getData().forEach((rb) -> TARDISBlockSetters.setBlock(rb.getLocation(), rb.getBlockData()));
        }
    }
}
