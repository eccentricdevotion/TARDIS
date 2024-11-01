/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.builders.TARDISBuilderUtility;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetBlocks;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.flight.vehicle.TARDISArmourStand;
import me.eccentric_nz.TARDIS.sensor.BeaconSensor;
import me.eccentric_nz.TARDIS.sensor.HandbrakeSensor;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
        UUID uuid = player.getUniqueId();
        plugin.getTrackerKeeper().getHiddenFlight().remove(uuid);
        Location location = stand.getLocation();
        boolean drifting = false;
        String direction = player.getFacing().getOppositeFace().toString();
        FlightReturnData data = plugin.getTrackerKeeper().getFlyingReturnLocation().get(uuid);
        if (data != null) {
            Location interior = data.getLocation();
            // check block protection
            if (!plugin.getPluginRespect().getRespect(location, new Parameters(player, Flag.getDefaultFlags()))
                    || plugin.getTardisArea().isInExistingArea(location)) {
                // remove police box
                stand.remove();
                // set drifting in the time vortex
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new TARDISLoopingFlightSound(plugin, interior, data.getId()), 20L);
                plugin.getTrackerKeeper().getInVortex().add(data.getId());
                plugin.getTrackerKeeper().getDidDematToVortex().add(data.getId());
                drifting = true;
                // message player
                plugin.getMessenger().send(player, TardisModule.TARDIS, "FLIGHT_PROTECTED");
            }
            // stop animation and sound runnables
            plugin.getServer().getScheduler().cancelTask(data.getAnimation());
            plugin.getServer().getScheduler().cancelTask(data.getSound());
            if (!drifting) {
                // reset police box model
                EntityEquipment ee = stand.getEquipment();
                ItemStack is = ee.getHelmet();
                ItemMeta im = is.getItemMeta();
                im.setCustomModelData(1001);
                is.setItemMeta(im);
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
                where.put("tardis_id", data.getId());
                plugin.getQueryFactory().doUpdate("current", set, where);
                // update door location
                TARDISBuilderUtility.saveDoorLocation(location, data.getId(), direction);
                Block under = location.getBlock().getRelative(BlockFace.DOWN);
                if (under.getType().isAir()) {
                    // if location is in the air, set under door block
                    TARDISBlockSetters.setUnderDoorBlock(location.getWorld(), under.getX(), under.getY(), under.getZ(), data.getId(), false);
                }
                // set the light
                Levelled light = TARDISConstants.LIGHT;
                light.setLevel(7);
                location.getBlock().getRelative(BlockFace.UP, 2).setBlockData(light);
            }
            // teleport player to interior
            player.teleport(interior);
            // add player to travellers
            HashMap<String, Object> sett = new HashMap<>();
            sett.put("tardis_id", data.getId());
            sett.put("uuid", uuid.toString());
            plugin.getQueryFactory().doSyncInsert("travellers", sett);
            // remove trackers
            plugin.getTrackerKeeper().getMaterialising().removeAll(Collections.singleton(data.getId()));
            plugin.getTrackerKeeper().getMalfunction().remove(data.getId());
            if (!drifting) {
                plugin.getTrackerKeeper().getInVortex().removeAll(Collections.singleton(data.getId()));
                if (plugin.getTrackerKeeper().getDidDematToVortex().contains(data.getId())) {
                    plugin.getTrackerKeeper().getDidDematToVortex().removeAll(Collections.singleton(data.getId()));
                }
                if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(data.getId())) {
                    int taskID = plugin.getTrackerKeeper().getDestinationVortex().get(data.getId());
                    plugin.getServer().getScheduler().cancelTask(taskID);
                    plugin.getTrackerKeeper().getDestinationVortex().remove(data.getId());
                }
                new FlightEnd(plugin).process(data.getId(), player, false, true);
            }
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> {
            COMPASS compass = COMPASS.valueOf(direction);
            stand.teleport(new Location(location.getWorld(), location.getBlockX() + 0.5d, location.getBlockY(), location.getBlockZ() + 0.5d, compass.getYaw(), 0.0f));
        });
    }

    public void startFlying(Player player, int id, Block block, Location current, boolean beac_on, String beacon, boolean pandorica) {
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
                for (Entity e : current.getWorld().getNearbyEntities(current, 1.5d, 1.5d, 1.5d, (s) -> s.getType() == EntityType.ARMOR_STAND)) {
                    if (e instanceof ArmorStand stand) {
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            int animation = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new FlyingAnimation(plugin, (ArmorStand) stand, player, pandorica), 5L, 3L);
                            int sound = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> TARDISSounds.playTARDISSound(player.getLocation(), "time_rotor_flying", 4f), 5L, 33L);
                            player.getPersistentDataContainer().set(plugin.getLoopKey(), PersistentDataType.INTEGER, sound);
                            stand.addPassenger(player);
                            TARDISArmourStand tas = (TARDISArmourStand) ((CraftArmorStand) stand).getHandle();
                            tas.setPlayer(player);
                            tas.setStationary(false);
                            player.setInvulnerable(true);
                            player.setAllowFlight(true);
                            player.setFlying(true);
                            // remove the light
                            current.getBlock().getRelative(BlockFace.UP, 2).setBlockData(TARDISConstants.AIR);
                            // save player's current location, so we can teleport them back to it when they finish flying
                            plugin.getTrackerKeeper().getFlyingReturnLocation().put(uuid, new FlightReturnData(id, interior, sound, animation, stand.getUniqueId()));
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.setGravity(true), 1L);
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
