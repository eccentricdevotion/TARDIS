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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.achievement.TARDISBook;
import me.eccentric_nz.TARDIS.arch.TARDISArchPersister;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.camera.TARDISCameraTracker;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.flight.FlightReturnData;
import me.eccentric_nz.TARDIS.flight.FlyingAnimation;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.tardisweepingangels.nms.MonsterSpawner;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

/**
 * Tylos was a member of Varsh's group of Outlers on Alzarius. When Adric asked to join them, Tylos challenged him to
 * prove his worth by stealing some riverfruit.
 *
 * @author eccentric_nz
 */
public class TARDISJoinListener implements Listener {

    private final TARDIS plugin;

    public TARDISJoinListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for a player joining the server. If the player has TARDIS permissions (ie not a guest), then check
     * whether they have achieved the building of a TARDIS. If not then insert an achievement record and give them the
     * tardis book.
     *
     * @param event a player joining the server
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        if (plugin.getKitsConfig().getBoolean("give.join.enabled")) {
            if (TARDISPermission.hasPermission(player, "tardis.kit.join")) {
                // check if they have the tardis kit
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid);
                where.put("name", "joinkit");
                ResultSetAchievements rsa = new ResultSetAchievements(plugin, where, false);
                if (!rsa.resultSet()) {
                    //add a record
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("uuid", uuid);
                    set.put("name", "joinkit");
                    plugin.getQueryFactory().doInsert("achievements", set);
                    // give the join kit
                    String kit = plugin.getKitsConfig().getString("give.join.kit");
                    plugin.getServer().dispatchCommand(plugin.getConsole(), "tardisgive " + player.getName() + " kit " + kit);
                }
            }
        }
        if (plugin.getConfig().getBoolean("allow.achievements")) {
            if (TARDISPermission.hasPermission(player, "tardis.book")) {
                // check if they have started building a TARDIS yet
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid);
                where.put("name", "tardis");
                ResultSetAchievements rsa = new ResultSetAchievements(plugin, where, false);
                if (!rsa.resultSet()) {
                    //add a record
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("uuid", uuid);
                    set.put("name", "tardis");
                    plugin.getQueryFactory().doInsert("achievements", set);
                    TARDISBook book = new TARDISBook(plugin);
                    // title, author, filename, player
                    book.writeBook("Get transport", "Rassilon", "tardis", player);
                }
            }
        }
        if (!plugin.getDifficulty().equals(Difficulty.EASY) && ((plugin.getConfig().getBoolean("allow.player_difficulty") && TARDISPermission.hasPermission(player, "tardis.difficulty")) || (plugin.getConfig().getInt("travel.grace_period") > 0 && TARDISPermission.hasPermission(player, "tardis.create")))) {
            // check if they have t_count record - create one if not
            ResultSetCount rsc = new ResultSetCount(plugin, uuid);
            if (!rsc.resultSet()) {
                HashMap<String, Object> setc = new HashMap<>();
                setc.put("uuid", uuid);
                setc.put("player", player.getName());
                plugin.getQueryFactory().doInsert("t_count", setc);
            }
        }
        if (plugin.getConfig().getBoolean("creation.keep_night")) {
            // are they in the TARDIS?
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid);
            ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
            if (rst.resultSet()) {
                // set the player's time to midnight
                player.setPlayerTime(18000, false);
            }
        }
        // load and remember the players Police Box chunk
        HashMap<String, Object> wherep = new HashMap<>();
        wherep.put("uuid", uuid);
        ResultSetTardis rs = new ResultSetTardis(plugin, wherep, "", false, 0);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            int id = tardis.getTardis_id();
            String owner = tardis.getOwner();
            String last_known_name = tardis.getLastKnownName();
            HashMap<String, Object> wherecl = new HashMap<>();
            wherecl.put("tardis_id", id);
            if (plugin.getConfig().getBoolean("police_box.keep_chunk_force_loaded")) {
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                if (rsc.resultSet()) {
                    World w = rsc.getWorld();
                    if (w != null) {
                        Chunk chunk = w.getChunkAt(new Location(w, rsc.getX(), rsc.getY(), rsc.getZ()));
                        while (!chunk.isLoaded()) {
                            chunk.load();
                        }
                        chunk.setForceLoaded(true);
                    }
                }
            }
            long now;
            if (TARDISPermission.hasPermission(player, "tardis.prune.bypass")) {
                now = Long.MAX_VALUE;
            } else {
                now = System.currentTimeMillis();
            }
            HashMap<String, Object> set = new HashMap<>();
            set.put("lastuse", now);
            set.put("monsters", 0);
            set.put("bedrock", (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) ? 1 : 0);
            if (!last_known_name.equals(player.getName())) {
                // update the player's name WG region as it may have changed
                if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                    World cw = TARDISStaticLocationGetters.getWorldFromSplitString(tardis.getChunk());
                    // tardis region
                    plugin.getWorldGuardUtils().updateRegionForNameChange(cw, owner, player.getUniqueId(), "tardis");
                }
                set.put("last_known_name", player.getName());
            }
            HashMap<String, Object> wherel = new HashMap<>();
            wherel.put("tardis_id", id);
            plugin.getQueryFactory().doUpdate("tardis", set, wherel);
        }
        // re-arch the player
        if (plugin.isDisguisesOnServer() && plugin.getConfig().getBoolean("arch.enabled")) {
            new TARDISArchPersister(plugin).reArch(player.getUniqueId());
        }
        // add to perception filter team
        if (plugin.getConfig().getBoolean("allow.perception_filter")) {
            plugin.getFilter().addPlayer(player);
        }
        // add to zero room occupants
        if (plugin.getConfig().getBoolean("allow.zero_room")) {
            if (player.getLocation().getWorld().getName().equals("TARDIS_Zero_Room")) {
                plugin.getTrackerKeeper().getZeroRoomOccupants().add(player.getUniqueId());
            }
        }
        // recreate custom flying chicken if player was flying the TARDIS exterior
        if (plugin.getTrackerKeeper().getFlyingReturnLocation().containsKey(player.getUniqueId())) {
            // get the chicken
            Entity vehicle = player.getVehicle();
            if (vehicle instanceof ArmorStand stand) {
                Entity flyer = stand.getVehicle();
                if (flyer != null) {
                    FlightReturnData data = plugin.getTrackerKeeper().getFlyingReturnLocation().get(player.getUniqueId());
                    // get exterior preset
                    boolean pandorica = false;
                    ResultSetTardisPreset rsp = new ResultSetTardisPreset(plugin);
                    if (rsp.fromID(data.getId())) {
                        pandorica = (rsp.getPreset() == ChameleonPreset.PANDORICA);
                    }
                    // stop current flying tasks
                    plugin.getServer().getScheduler().cancelTask(data.getAnimation());
                    plugin.getServer().getScheduler().cancelTask(data.getSound());
                    // restart animation and sound
                    int animation = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new FlyingAnimation(stand, pandorica), 5L, 3L);
                    int sound = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                        TARDISSounds.playTARDISSound(player.getLocation(), "time_rotor", 100f);
                    }, 5L, 280L);
                    // spawn a new TARDISChicken
                    LivingEntity chicken = new MonsterSpawner().create(stand.getLocation(), Monster.FLYER);
                    stand.removePassenger(player);
                    flyer.removePassenger(stand);
                    stand.addPassenger(player);
                    stand.setGravity(false);
                    chicken.addPassenger(stand);
                    chicken.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 15));
                    chicken.setSilent(true);
                    chicken.setInvulnerable(true);
                    chicken.setNoDamageTicks(Integer.MAX_VALUE);
                    chicken.setFireTicks(0);
                    // re-save flight data
                    plugin.getTrackerKeeper().getFlyingReturnLocation().put(player.getUniqueId(), new FlightReturnData(data.getId(), data.getLocation(), sound, animation, chicken.getUniqueId()));
                }
            }
        }
        // teleport players that rejoined after logging out while in Junk TARDIS or using external camera
        if (plugin.getTrackerKeeper().getJunkRelog().containsKey(player.getUniqueId())) {
            Location location = plugin.getTrackerKeeper().getJunkRelog().remove(player.getUniqueId());
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                player.teleport(location);
                // remove invisibility
                if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                    player.removePotionEffect(PotionEffectType.INVISIBILITY);
                }
                // occupy tardis
                ResultSetTardisID rsid = new ResultSetTardisID(plugin);
                // if TIPS determine tardis_id from player location
                if (plugin.getConfig().getBoolean("creation.default_world") && !player.hasPermission("tardis.create_world")) {
                    int slot = TARDISInteriorPostioning.getTIPSSlot(player.getLocation());
                    if (!rsid.fromTIPSSlot(slot)) {
                        return;
                    }
                } else if (!rsid.fromUUID(player.getUniqueId().toString())) {
                    return;
                }
                int id = rsid.getTardis_id();
                HashMap<String, Object> wherei = new HashMap<>();
                wherei.put("tardis_id", id);
                wherei.put("uuid", player.getUniqueId().toString());
                plugin.getQueryFactory().doInsert("travellers", wherei);
                TARDISCameraTracker.SPECTATING.remove(player.getUniqueId());
            }, 2L);
        }
        // notify updates
        if (plugin.getConfig().getBoolean("preferences.notify_update") && plugin.isUpdateFound() && player.isOp()) {
            plugin.getMessenger().sendJenkinsUpdateReady(player, plugin.getBuildNumber(), plugin.getUpdateNumber());
        }
    }
}
