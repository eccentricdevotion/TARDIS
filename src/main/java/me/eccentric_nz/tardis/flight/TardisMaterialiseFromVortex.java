/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.flight;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.advancement.TardisAdvancementFactory;
import me.eccentric_nz.tardis.api.event.TardisMalfunctionEvent;
import me.eccentric_nz.tardis.api.event.TardisMaterialisationEvent;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.builders.BuildData;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetNextLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.enumeration.Advancement;
import me.eccentric_nz.tardis.enumeration.CardinalDirection;
import me.eccentric_nz.tardis.enumeration.Preset;
import me.eccentric_nz.tardis.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.tardis.hads.TardisCloisterBell;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.travel.TardisMalfunction;
import me.eccentric_nz.tardis.utility.TardisBlockSetters;
import me.eccentric_nz.tardis.utility.TardisSounds;
import me.eccentric_nz.tardis.utility.TardisStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TardisMaterialiseFromVortex implements Runnable {

    private final TardisPlugin plugin;
    private final int id;
    private final Player player;
    private final Location handbrake;
    private final SpaceTimeThrottle spaceTimeThrottle;

    public TardisMaterialiseFromVortex(TardisPlugin plugin, int id, Player player, Location handbrake, SpaceTimeThrottle spaceTimeThrottle) {
        this.plugin = plugin;
        this.id = id;
        this.player = player;
        this.handbrake = handbrake;
        this.spaceTimeThrottle = spaceTimeThrottle;
    }

    @Override
    public void run() {
        UUID uuid = player.getUniqueId();
        HashMap<String, Object> wherenl = new HashMap<>();
        wherenl.put("tardis_id", id);
        ResultSetNextLocation rsn = new ResultSetNextLocation(plugin, wherenl);
        if (!rsn.resultSet()) {
            TardisMessage.send(player, "DEST_NO_LOAD");
            return;
        }
        Location exit = new Location(rsn.getWorld(), rsn.getX(), rsn.getY(), rsn.getZ());
        boolean is_next_sub = rsn.isSubmarine();
        boolean malfunction = (plugin.getTrackerKeeper().getMalfunction().containsKey(id) && plugin.getTrackerKeeper().getMalfunction().get(id));
        HashMap<String, Object> wherei = new HashMap<>();
        wherei.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, wherei, "", false, 2);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            // get current location for back
            HashMap<String, Object> wherecu = new HashMap<>();
            wherecu.put("tardis_id", id);
            ResultSetCurrentLocation rscl = new ResultSetCurrentLocation(plugin, wherecu);
            if (rscl.resultSet()) {
                BukkitScheduler scheduler = plugin.getServer().getScheduler();
                if (malfunction) {
                    // check for a malfunction
                    TardisMalfunction m = new TardisMalfunction(plugin);
                    exit = m.getMalfunction(id, player, rscl.getDirection(), handbrake, tardis.getEps(), tardis.getCreeper());
                    if (exit != null) {
                        plugin.getTrackerKeeper().getRescue().remove(id);
                        HashMap<String, Object> wheress = new HashMap<>();
                        wheress.put("tardis_id", id);
                        HashMap<String, Object> setsave = new HashMap<>();
                        setsave.put("world", Objects.requireNonNull(exit.getWorld()).getName());
                        setsave.put("x", exit.getBlockX());
                        setsave.put("y", exit.getBlockY());
                        setsave.put("z", exit.getBlockZ());
                        setsave.put("submarine", 0);
                        plugin.getQueryFactory().doSyncUpdate("next", setsave, wheress);
                        if (plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                            int amount = Math.round(plugin.getTrackerKeeper().getHasDestination().get(id) * spaceTimeThrottle.getArtronMultiplier());
                            HashMap<String, Object> wheret = new HashMap<>();
                            wheret.put("tardis_id", id);
                            plugin.getQueryFactory().alterEnergyLevel("tardis", -amount, wheret, player);
                            TardisMessage.send(player, "Q_FLY");
                            plugin.getTrackerKeeper().getHasDestination().remove(id);
                        }
                        plugin.getPluginManager().callEvent(new TardisMalfunctionEvent(player, tardis, exit));
                        // set beacon colour to red
                        if (!tardis.getBeacon().isEmpty()) {
                            setBeaconUpBlock(tardis.getBeacon(), id);
                        }
                        // play tardis crash sound
                        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                            TardisSounds.playTardisSound(handbrake, "tardis_malfunction");
                        }
                        // add a potion effect to the player
                        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 150, 5));
                        long cloister_delay = (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) ? 262L : 360L;
                        Location location = exit;
                        scheduler.scheduleSyncDelayedTask(plugin, () -> {
                            // sound the cloister bell
                            TardisCloisterBell bell = new TardisCloisterBell(plugin, 6, id, location, plugin.getServer().getPlayer(uuid), true, "a flight malfunction", false);
                            int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, bell, 2L, 70L);
                            bell.setTask(taskID);
                            plugin.getTrackerKeeper().getCloisterBells().put(id, taskID);
                        }, cloister_delay);
                    } else {
                        malfunction = false;
                    }
                }
                if (exit != null) {
                    if (!Objects.requireNonNull(exit.getWorld()).isChunkLoaded(exit.getChunk())) {
                        exit.getWorld().loadChunk(exit.getChunk());
                    }
                    Preset preset = tardis.getPreset();
                    CardinalDirection sd = rsn.getDirection();
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
                    boolean minecart = false;
                    boolean bar = false;
                    int flight_mode = 1;
                    SpaceTimeThrottle spaceTimeThrottle = SpaceTimeThrottle.NORMAL;
                    if (rsp.resultSet()) {
                        minecart = rsp.isMinecartOn();
                        bar = rsp.isTravelBarOn();
                        flight_mode = rsp.getFlightMode();
                        if (flight_mode == 1 && !malfunction) {
                            spaceTimeThrottle = SpaceTimeThrottle.getByDelay().get(rsp.getThrottle());
                        }
                    }
                    // set destination flight data
                    BuildData bd = new BuildData(uuid.toString());
                    bd.setDirection(sd);
                    bd.setLocation(exit);
                    bd.setMalfunction(false);
                    bd.setOutside(false);
                    bd.setPlayer(player);
                    bd.setRebuild(false);
                    bd.setSubmarine(is_next_sub);
                    bd.setTardisId(id);
                    bd.setThrottle(spaceTimeThrottle);
                    // determine delay values
                    long flight_mode_delay;
                    long travel_time;
                    String landSFX;
                    switch (spaceTimeThrottle) {
                        case WARP -> {
                            flight_mode_delay = ((plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) ? 0L : 130L);
                            travel_time = (malfunction) ? 400L : 94L;
                            landSFX = "tardis_land_warp";
                        }
                        case RAPID -> {
                            flight_mode_delay = ((plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) ? 0L : 259L);
                            travel_time = (malfunction) ? 400L : 188L;
                            landSFX = "tardis_land_rapid";
                        }
                        case FASTER -> {
                            flight_mode_delay = ((plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) ? 0L : 388L);
                            travel_time = (malfunction) ? 400L : 282L;
                            landSFX = "tardis_land_faster";
                        }
                        default -> { // NORMAL
                            flight_mode_delay = ((plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) ? 0L : 518L);
                            travel_time = (malfunction) ? 400L : 375L;
                            landSFX = "tardis_land";
                        }
                    }
                    // remember flight data
                    plugin.getTrackerKeeper().getFlightData().put(uuid, bd);
                    long materialisation_delay = flight_mode_delay;
                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id) && malfunction) {
                        materialisation_delay += 262L;
                        travel_time += 262L;
                    }
                    // flight mode
                    if (flight_mode == 2 || flight_mode == 3) {
                        materialisation_delay += 650L;
                        travel_time += 650L;
                        Runnable runner = (flight_mode == 2) ? new TardisRegulatorStarter(plugin, player, id) : new TardisManualFlightStarter(plugin, player, id);
                        // start the flying mode (after demat if not in vortex already)
                        scheduler.scheduleSyncDelayedTask(plugin, runner, flight_mode_delay);
                    }
                    if (bar) {
                        long tt = travel_time;
                        // start travel bar
                        scheduler.scheduleSyncDelayedTask(plugin, () -> new TardisTravelBar(plugin).showTravelRemaining(player, tt, false), flight_mode_delay);
                    }
                    // cancel repeating sfx task
                    scheduler.scheduleSyncDelayedTask(plugin, () -> {
                        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                            scheduler.cancelTask(plugin.getTrackerKeeper().getDestinationVortex().get(id));
                        }
                    }, materialisation_delay - 140L);
                    boolean mine_sound = minecart;
                    Location sound_loc = (preset.equals(Preset.JUNK_MODE)) ? exit : handbrake;
                    Location external_sound_loc = exit;
                    boolean malchk = malfunction;
                    scheduler.scheduleSyncDelayedTask(plugin, () -> {
                        Location final_location = bd.getLocation();
                        Location l = new Location(rscl.getWorld(), rscl.getX(), rscl.getY(), rscl.getZ());
                        plugin.getPluginManager().callEvent(new TardisMaterialisationEvent(player, tardis, final_location));
                        plugin.getPresetBuilder().buildPreset(bd);
                        if (!mine_sound) {
                            if (!preset.equals(Preset.JUNK_MODE)) {
                                if (!malchk) {
                                    TardisSounds.playTardisSound(sound_loc, landSFX);
                                    TardisSounds.playTardisSound(external_sound_loc, landSFX);
                                } else {
                                    TardisSounds.playTardisSound(sound_loc, "tardis_emergency_land");
                                    TardisSounds.playTardisSound(external_sound_loc, "tardis_emergency_land");
                                }
                            } else {
                                TardisSounds.playTardisSound(sound_loc, "junk_land");
                            }
                        } else {
                            Objects.requireNonNull(handbrake.getWorld()).playSound(handbrake, Sound.ENTITY_MINECART_INSIDE, 1.0F, 0.0F);
                        }
                        HashMap<String, Object> setcurrent = new HashMap<>();
                        HashMap<String, Object> wherecurrent = new HashMap<>();
                        HashMap<String, Object> setback = new HashMap<>();
                        HashMap<String, Object> whereback = new HashMap<>();
                        HashMap<String, Object> setdoor = new HashMap<>();
                        HashMap<String, Object> wheredoor = new HashMap<>();
                        // current
                        setcurrent.put("world", Objects.requireNonNull(final_location.getWorld()).getName());
                        setcurrent.put("x", final_location.getBlockX());
                        setcurrent.put("y", final_location.getBlockY());
                        setcurrent.put("z", final_location.getBlockZ());
                        setcurrent.put("direction", bd.getDirection().toString());
                        setcurrent.put("submarine", (bd.isSubmarine()) ? 1 : 0);
                        wherecurrent.put("tardis_id", id);
                        // back
                        setback.put("world", rscl.getWorld().getName());
                        setback.put("x", rscl.getX());
                        setback.put("y", rscl.getY());
                        setback.put("z", rscl.getZ());
                        setback.put("direction", rscl.getDirection().toString());
                        setback.put("submarine", (rscl.isSubmarine()) ? 1 : 0);
                        whereback.put("tardis_id", id);
                        // update Police Box door direction
                        setdoor.put("door_direction", bd.getDirection().toString());
                        wheredoor.put("tardis_id", id);
                        wheredoor.put("door_type", 0);
                        if (setcurrent.size() > 0) {
                            plugin.getQueryFactory().doUpdate("current", setcurrent, wherecurrent);
                            plugin.getQueryFactory().doUpdate("back", setback, whereback);
                            plugin.getQueryFactory().doUpdate("doors", setdoor, wheredoor);
                        }
                        if (plugin.getAdvancementConfig().getBoolean("travel.enabled") && !plugin.getTrackerKeeper().getReset().contains(rscl.getWorld().getName())) {
                            if (Objects.requireNonNull(l.getWorld()).equals(final_location.getWorld())) {
                                int distance = (int) l.distance(final_location);
                                if (distance > 0 && plugin.getAdvancementConfig().getBoolean("travel.enabled")) {
                                    TardisAdvancementFactory taf = new TardisAdvancementFactory(plugin, player, Advancement.TRAVEL, 1);
                                    taf.doAdvancement(distance);
                                }
                            }
                        }
                        if (!malchk) {
                            // forget flight data
                            plugin.getTrackerKeeper().getFlightData().remove(uuid);
                        }
                    }, materialisation_delay);
                    plugin.getTrackerKeeper().getDamage().remove(id);
                    // set last use
                    long now;
                    if (TardisPermission.hasPermission(player, "tardis.prune.bypass")) {
                        now = Long.MAX_VALUE;
                    } else {
                        now = System.currentTimeMillis();
                    }
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("lastuse", now);
                    HashMap<String, Object> whereh = new HashMap<>();
                    whereh.put("tardis_id", id);
                    plugin.getQueryFactory().doUpdate("tardis", set, whereh);
                }
            }
        }
    }

    private void setBeaconUpBlock(String str, int id) {
        Location bl = TardisStaticLocationGetters.getLocationFromDB(str);
        assert bl != null;
        Block b = bl.getBlock();
        while (!b.getType().equals(Material.BEACON) && b.getLocation().getBlockY() > 0) {
            b = b.getRelative(BlockFace.DOWN);
        }
        TardisBlockSetters.setBlockAndRemember(b.getRelative(BlockFace.UP), Material.RED_STAINED_GLASS, id, 2);
    }
}
