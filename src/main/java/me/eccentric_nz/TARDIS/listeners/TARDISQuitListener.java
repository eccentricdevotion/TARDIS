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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.arch.TARDISArchPersister;
import me.eccentric_nz.TARDIS.artron.TARDISAdaptiveBoxLampToggler;
import me.eccentric_nz.TARDIS.artron.TARDISBeaconToggler;
import me.eccentric_nz.TARDIS.artron.TARDISLampToggler;
import me.eccentric_nz.TARDIS.camera.TARDISCameraTracker;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISQuitListener implements Listener {

    private final TARDIS plugin;

    public TARDISQuitListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        // remove camera viewers
        if (TARDISCameraTracker.SPECTATING.containsKey(uuid)) {
            // set their location back to the TARDIS interior
            plugin.getTrackerKeeper().getJunkRelog().put(uuid, TARDISCameraTracker.SPECTATING.get(uuid).getLocation());
            TARDISCameraTracker.SPECTATING.remove(uuid);
        }
        // remove if Junk TARDIS traveller
        if (plugin.getGeneralKeeper().getJunkTravellers().contains(uuid)) {
            // check if they are in the vortex
            if (plugin.getUtils().inTARDISWorld(player)) {
                // set their location to the junk TARDISes destination
                plugin.getTrackerKeeper().getJunkRelog().put(uuid, plugin.getGeneralKeeper().getJunkDestination());
            }
            plugin.getGeneralKeeper().getJunkTravellers().remove(uuid);
        }
        // if player if flying TARDIS exterior stop sound loop
        Optional.ofNullable(plugin.getTrackerKeeper().getFlyingReturnLocation().get(uuid)).ifPresent(value -> {
            plugin.getServer().getScheduler().cancelTask(value.getSound());
        });
        // forget the players Police Box chunk
        HashMap<String, Object> wherep = new HashMap<>();
        wherep.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, wherep, "", false, 0);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            HashMap<String, Object> wherecl = new HashMap<>();
            wherecl.put("tardis_id", tardis.getTardisId());
            if (plugin.getConfig().getBoolean("police_box.keep_chunk_force_loaded")) {
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                if (rsc.resultSet()) {
                    World w = rsc.getWorld();
                    if (w != null) {
                        Chunk chunk = w.getChunkAt(new Location(w, rsc.getX(), rsc.getY(), rsc.getZ()));
                        chunk.setForceLoaded(false);
                    }
                }
            }
            // power down TARDIS
            if (plugin.getConfig().getBoolean("allow.power_down") && plugin.getConfig().getBoolean("allow.power_down_on_quit")) {
                // check if powered on
                if (tardis.isPoweredOn()) {
                    // not if flying or uninitialised
                    int id = tardis.getTardisId();
                    if (!tardis.isTardisInit() || isTravelling(id) || !tardis.isHandbrakeOn()) {
                        return;
                    }
                    // power off
                    ChameleonPreset preset = tardis.getPreset();
                    boolean hidden = tardis.isHidden();
                    boolean lights = tardis.isLightsOn();
                    // police box lamp, delay it incase the TARDIS needs rebuilding
                    long delay = 1L;
                    // if hidden, rebuild
                    if (hidden) {
                        plugin.getServer().dispatchCommand(plugin.getConsole(), "tardisremote " + player.getName() + " rebuild");
                        delay = 20L;
                    }
                    if (preset.equals(ChameleonPreset.ADAPTIVE) || preset.usesArmourStand()) {
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TARDISAdaptiveBoxLampToggler(plugin).toggleLamp(id, false, preset), delay);
                    }
                    // if lights are on, turn them off
                    if (lights) {
                        new TARDISLampToggler(plugin).flickSwitch(id, uuid, true, tardis.getSchematic().getLights());
                    }
                    // if beacon is on turn it off
                    new TARDISBeaconToggler(plugin).flickSwitch(uuid, id, false);
                    // turn force field off
                    plugin.getTrackerKeeper().getActiveForceFields().remove(uuid);
                    // update database
                    HashMap<String, Object> wheret = new HashMap<>();
                    wheret.put("tardis_id", id);
                    HashMap<String, Object> sett = new HashMap<>();
                    sett.put("powered_on", 0);
                    plugin.getQueryFactory().doUpdate("tardis", sett, wheret);
                }
            }
            // save arched status
            if (plugin.isDisguisesOnServer()) {
                if (plugin.getConfig().getBoolean("arch.enabled") && plugin.getTrackerKeeper().getJohnSmith().containsKey(uuid)) {
                    new TARDISArchPersister(plugin).save(uuid);
                }
                plugin.getTrackerKeeper().getGeneticallyModified().remove(uuid);
            }
        }
    }

    private boolean isTravelling(int id) {
        return (plugin.getTrackerKeeper().getDematerialising().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getInVortex().contains(id));
    }
}
