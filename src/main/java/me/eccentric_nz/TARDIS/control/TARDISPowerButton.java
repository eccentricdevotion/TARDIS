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
package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.artron.TARDISAdaptiveBoxLampToggler;
import me.eccentric_nz.TARDIS.artron.TARDISBeaconToggler;
import me.eccentric_nz.TARDIS.artron.TARDISLampToggler;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISPowerButton {

    private final TARDIS plugin;
    private final int id;
    private final Player player;
    private final PRESET preset;
    private final boolean powered;
    private final boolean hidden;
    private final boolean lights;
    private final Location loc;
    private final int level;
    private final boolean lanterns;

    public TARDISPowerButton(TARDIS plugin, int id, Player player, PRESET preset, boolean powered, boolean hidden, boolean lights, Location loc, int level, boolean lanterns) {
        this.plugin = plugin;
        this.id = id;
        this.player = player;
        this.preset = preset;
        this.powered = powered;
        this.hidden = hidden;
        this.lights = lights;
        this.loc = loc;
        this.level = level;
        this.lanterns = lanterns;
    }

    public void clickButton() {
        HashMap<String, Object> wherep = new HashMap<>();
        wherep.put("tardis_id", id);
        HashMap<String, Object> setp = new HashMap<>();
        boolean isAdaptive = preset.equals(PRESET.ADAPTIVE);
        UUID uuid = player.getUniqueId();
        if (powered) {
            if (isTravelling(id)) {
                TARDISMessage.send(player, "POWER_NO");
                return;
            }
            TARDISSounds.playTARDISSound(loc, "power_down");
            // power down
            setp.put("powered_on", 0);
            TARDISMessage.send(player, "POWER_OFF");
            long delay = 0;
            // if hidden, rebuild
            if (hidden) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "tardisremote " + player.getName() + " rebuild");
                TARDISMessage.send(player, "POWER_FAIL");
                delay = 20L;
            }
            // police box lamp, delay it incase the TARDIS needs rebuilding
            if (isAdaptive || preset.usesItemFrame()) {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TARDISAdaptiveBoxLampToggler(plugin).toggleLamp(id, false, preset), delay);
            }
            // if lights are on, turn them off
            if (lights) {
                new TARDISLampToggler(plugin).flickSwitch(id, uuid, true, lanterns);
            }
            // if beacon is on turn it off
            new TARDISBeaconToggler(plugin).flickSwitch(uuid, id, false);
            // turn force field off
            if (plugin.getTrackerKeeper().getActiveForceFields().containsKey(uuid)) {
                plugin.getTrackerKeeper().getActiveForceFields().remove(uuid);
                TARDISMessage.send(player, "FORCE_FIELD", "OFF");
            }
        } else {
            // don't power up if there is no power
            if (level <= plugin.getArtronConfig().getInt("standby")) {
                TARDISMessage.send(player, "POWER_LOW");
                return;
            }
            TARDISSounds.playTARDISSound(loc, "power_up");
            // power up
            setp.put("powered_on", 1);
            TARDISMessage.send(player, "POWER_ON");
            // if lights are off, turn them on
            if (lights) {
                new TARDISLampToggler(plugin).flickSwitch(id, uuid, false, lanterns);
            }
            // determine beacon prefs
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
            boolean beacon_on = true;
            if (rsp.resultSet()) {
                beacon_on = rsp.isBeaconOn();
            }
            // if beacon is off turn it on
            if (beacon_on) {
                new TARDISBeaconToggler(plugin).flickSwitch(uuid, id, true);
            }
            // police box lamp
            if (isAdaptive || preset.usesItemFrame()) {
                new TARDISAdaptiveBoxLampToggler(plugin).toggleLamp(id, true, preset);
            }
        }
        plugin.getQueryFactory().doUpdate("tardis", setp, wherep);
    }

    private boolean isTravelling(int id) {
        return (plugin.getTrackerKeeper().getDematerialising().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getInVortex().contains(id));
    }
}
