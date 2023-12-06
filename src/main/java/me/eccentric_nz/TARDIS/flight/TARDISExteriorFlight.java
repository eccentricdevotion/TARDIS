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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.builders.TARDISBuilderUtility;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetBlocks;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.tardisweepingangels.nms.MonsterSpawner;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISExteriorFlight {

    private final TARDIS plugin;

    public TARDISExteriorFlight(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void stopFlying(Player player, ArmorStand stand) {
        UUID uuid = player.getUniqueId();
        Location location = stand.getLocation();
        String direction = player.getFacing().getOppositeFace().toString();
        FlightReturnData data = plugin.getTrackerKeeper().getFlyingReturnLocation().get(uuid);
        if (data != null) {
            // stop animation runnable
            plugin.getServer().getScheduler().cancelTask(data.getAnimation());
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
            // set the light
            Levelled light = TARDISConstants.LIGHT;
            light.setLevel(7);
            location.getBlock().getRelative(BlockFace.UP, 2).setBlockData(light);
            // teleport player to interior
            Location interior = data.getLocation();
            player.teleport(interior);
            // add player to travellers
            HashMap<String, Object> sett = new HashMap<>();
            sett.put("tardis_id", data.getId());
            sett.put("uuid", uuid.toString());
            plugin.getQueryFactory().doSyncInsert("travellers", sett);
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
        Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> {
            COMPASS compass = COMPASS.valueOf(direction);
            stand.teleport(new Location(location.getWorld(), location.getBlockX() + 0.5d, location.getBlockY(), location.getBlockZ() + 0.5d, compass.getYaw(), 0.0f));
        });
    }

    void startFlying(Player player, int id, Block block, boolean beac_on, String beacon, boolean pandorica) {
        // get the TARDIS's current location
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
        plugin.getMessenger().sendStatus(player, "HANDBRAKE_OFF");
        plugin.getTrackerKeeper().getInVortex().add(id);
        Location playerLocation = player.getLocation();
        // teleport player to exterior
        Location location = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ(), playerLocation.getYaw(), playerLocation.getPitch());
        player.teleport(location);
        // get the armour stand
        for (Entity e : location.getWorld().getNearbyEntities(location, 1, 1, 1, (s) -> s.getType() == EntityType.ARMOR_STAND)) {
            if (e instanceof ArmorStand stand) {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    int animation = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new FlyingAnimation(stand, pandorica), 5L, 3L);
                    int sound = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                        TARDISSounds.playTARDISSound(player.getLocation(), "time_rotor_flying", 4f);
                    }, 5L, 33L);
                    // save player's current location, so we can teleport them back to it when they finish flying
                    plugin.getTrackerKeeper().getFlyingReturnLocation().put(player.getUniqueId(), new FlightReturnData(id, playerLocation, sound, animation));
                    // spawn a chicken
                    LivingEntity chicken = new MonsterSpawner().create(stand.getLocation(), Monster.FLYER);
                    stand.addPassenger(player);
                    stand.setGravity(false);
                    chicken.addPassenger(stand);
                    chicken.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 15));
                    chicken.setSilent(true);
                    chicken.setInvulnerable(true);
                    chicken.setNoDamageTicks(Integer.MAX_VALUE);
                    chicken.setFireTicks(0);
                    // remove the light
                    location.getBlock().getRelative(BlockFace.UP, 2).setBlockData(TARDISConstants.AIR);
                }, 2L);
                break;
            }
        }
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
