/*
 * Copyright (C) 2023 eccentric_nz
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

import java.util.Collections;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * @author eccentric_nz
 */
public class TARDISExteriorFlight {

    private final TARDIS plugin;

    public TARDISExteriorFlight(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void stopFlight(Player player, ArmorStand stand) {
        FlightReturnData data = plugin.getTrackerKeeper().getFlyingReturnLocation().get(player.getUniqueId());
        // update the TARDISes current location
        Location location = stand.getLocation();
        String direction = player.getFacing().getOppositeFace().toString();
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
        // telport player to interior
        Location interior = data.getLocation();
        player.teleport(interior);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> {
            COMPASS compass = COMPASS.valueOf(direction);
            stand.teleport(new Location(location.getWorld(), location.getBlockX() + 0.5d, location.blockY(), location.getBlockZ() + 0.5d, compass.getYaw(), 0.0f));
        });
        // remove trackers
        plugin.getTrackerKeeper().getMaterialising().removeAll(Collections.singleton(data.getId()));
        plugin.getTrackerKeeper().getInVortex().removeAll(Collections.singleton(data.getId()));
        plugin.getTrackerKeeper().getMalfunction().remove(data.getId());
        if (plugin.getTrackerKeeper().getDidDematToVortex().contains(data.getId())) {
            plugin.getTrackerKeeper().getDidDematToVortex().removeAll(Collections.singleton(data.getId()));
        }
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(data.getId())) {
            int taskID = plugin.getTrackerKeeper().getDestinationVortex().get(data.getId());
            plugin.getServer().getScheduler().cancelTask(taskID);
            plugin.getTrackerKeeper().getDestinationVortex().remove(data.getId());
        }
        new FlightEnd(plugin).process(data.getId(), player, false);
    }

    void startFlying(Player player, int id, Block block, boolean beac_on, String beacon) {
        // save player's current location so we can teleport them back to it when they finish flying
        Location playerLocation = player.getLocation();
        plugin.getTrackerKeeper().getFlyingReturnLocation().put(player.getUniqueId(), new FlightReturnData(id, playerLocation));
        // get TARDISes current location
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, where);
        if (!rsc.resultSet()) {
            plugin.debug("No current location");
            return;
        }
        // set the handbrake
        TARDISHandbrake.setLevers(block, false, true, block.getLocation().toString(), id, plugin);
        if (plugin.getConfig().getBoolean("circuits.damage")) {
            plugin.getTrackerKeeper().getHasNotClickedHandbrake().remove(id);
        }
        TARDISSounds.playTARDISSound(block.getLocation(), "tardis_handbrake_release");
        if (!beac_on && !beacon.isEmpty()) {
            TARDISTakeoff.toggleBeacon(beacon);
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("handbrake_on", 0);
        HashMap<String, Object> whereh = new HashMap<>();
        whereh.put("tardis_id", id);
        plugin.getQueryFactory().doUpdate("tardis", set, whereh);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "HANDBRAKE_OFF");
        plugin.getTrackerKeeper().getInVortex().add(id);
        Location location = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ(), playerLocation.getYaw(), playerLocation.getPitch());
        // teleport player to exterior
        player.teleport(location);
        // get the armour stand
        for (Entity e : location.getWorld().getNearbyEntities(location, 1, 1, 1, (s) -> s.getType() == EntityType.ARMOR_STAND)) {
            if (e instanceof ArmorStand stand) {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    EntityEquipment ee = stand.getEquipment();
                    ItemStack is = ee.getHelmet().clone();
                    // switch the custom model - makes TARDIS bigger to hide player model
                    ee.setHelmet(null);
                    ee.setItemInMainHand(is);
                    // spawn a phantom
                    Phantom phantom = (Phantom) stand.getLocation().getWorld().spawnEntity(stand.getLocation(), EntityType.PHANTOM);
                    stand.addPassenger(player);
                    phantom.addPassenger(stand);
                    phantom.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 15));
                    phantom.setSilent(true);
                    phantom.setInvulnerable(true);
                    phantom.setNoDamageTicks(Integer.MAX_VALUE);
                    phantom.setFireTicks(0);
                }, 2L);
                break;
            }
        }
    }
}
